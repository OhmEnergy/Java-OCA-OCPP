package eu.chargetime.ocpp;
/*
   ChargeTime.eu - Java-OCA-OCPP

   MIT License

   Copyright (C) 2016-2018 Thomas Volden <tv@chargetime.eu>

   Permission is hereby granted, free of charge, to any person obtaining a copy
   of this software and associated documentation files (the "Software"), to deal
   in the Software without restriction, including without limitation the rights
   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
   copies of the Software, and to permit persons to whom the Software is
   furnished to do so, subject to the following conditions:

   The above copyright notice and this permission notice shall be included in all
   copies or substantial portions of the Software.

   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
   SOFTWARE.
*/

import java.util.logging.Logger;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import eu.chargetime.ocpp.feature.Feature;
import eu.chargetime.ocpp.model.Confirmation;
import eu.chargetime.ocpp.model.Request;

/**
 * Handles basic server logic: Holds a list of supported features. Keeps track of outgoing requests.
 * Calls back when a confirmation is received.
 */
public class Server {

  private static final Logger logger = Logger.getLogger(Server.class.getName());

  public static final int INITIAL_SESSIONS_NUMBER = 1000;

  private Map<UUID, ISession> sessions;
  private Listener listener;
  private final IFeatureRepository featureRepository;
  private final IPromiseRepository promiseRepository;

  /**
   * Constructor. Handles the required injections.
   *
   * @param listener injected listener.
   */
  public Server(
      Listener listener,
      IFeatureRepository featureRepository,
      IPromiseRepository promiseRepository) {
    this.listener = listener;
    this.featureRepository = featureRepository;
    this.promiseRepository = promiseRepository;
    this.sessions = new ConcurrentHashMap<>(INITIAL_SESSIONS_NUMBER);
  }

  /**
   * start listening for clients. Assumes {@link #initialize(ServerEvents)} was or will be called.
   *
   * @param hostname Url or IP of the server as String.
   * @param port     the port number of the server.
   */
  public void open(String hostname, int port) {
    listener.open(hostname, port);
  }

  /**
   * Start listening for clients.
   *
   * @param hostname     Url or IP of the server as String.
   * @param port         the port number of the server.
   * @param serverEvents Callback handler for server specific events.
   */
  public void open(String hostname, int port, ServerEvents serverEvents) {
    initialize(serverEvents);
    listener.open(hostname, port);
  }

  /**
   * Start listening for clients.
   *
   * @param serverEvents Callback handler for server specific events.
   */
  public void initialize(ServerEvents serverEvents) {

    listener.setEventHandler(
        (session, information) -> {
          session.accept(
              new SessionEvents() {
                @Override
                public void handleConfirmation(String uniqueId, Confirmation confirmation) {
				  logger.info(String.format("Confirmation received on Server: %s", confirmation));
                  Optional<CompletableFuture<Confirmation>> promiseOptional =
                      promiseRepository.getPromise(uniqueId);
                  if (promiseOptional.isPresent()) {
                    promiseOptional.get().complete(confirmation);
                    promiseRepository.removePromise(uniqueId);
                  } else {
                    logger.fine(String.format("Promise not found for confirmation %s", confirmation));
                  }
                }

                @Override
                public Confirmation handleRequest(Request request)
                    throws UnsupportedFeatureException {
				  logger.info(String.format("Request received on Server: %s", request));
                  Optional<Feature> featureOptional = featureRepository.findFeature(request);
                  if (featureOptional.isPresent()) {
                    Optional<UUID> sessionIdOptional = getSessionID(session);
                    if (sessionIdOptional.isPresent()) {
                      return featureOptional.get().handleRequest(sessionIdOptional.get(), request);
                    } else {
                      logger.severe(
                          String.format("Unable to handle request (%s), the active session was not found.",
                          request
					  ));
                      throw new IllegalStateException("Active session not found");
                    }
                  } else {
                    throw new UnsupportedFeatureException();
                  }
                }

                @Override
                public void handleError(
                    String uniqueId, String errorCode, String errorDescription, Object payload) {
				  logger.info(String.format("Error received on Server: %s, %s, %s", errorCode, errorDescription, payload));
                  Optional<CompletableFuture<Confirmation>> promiseOptional =
                      promiseRepository.getPromise(uniqueId);
                  if (promiseOptional.isPresent()) {
                    promiseOptional
                        .get()
                        .completeExceptionally(
                            new CallErrorException(errorCode, errorCode, payload));
                    promiseRepository.removePromise(uniqueId);
                  } else {
                    logger.fine(String.format("Promise not found for error %s", errorDescription));
                  }
                }

                @Override
                public void handleConnectionClosed() {
                  Optional<UUID> sessionIdOptional = getSessionID(session);
                  if (sessionIdOptional.isPresent()) {
                    serverEvents.lostSession(sessionIdOptional.get());
                    sessions.remove(sessionIdOptional.get());
                  } else {
                    logger.warning("Active session not found");
                  }
                }

                @Override
                public void handleConnectionOpened() {}
              });

          sessions.put(session.getSessionId(), session);

          Optional<UUID> sessionIdOptional = getSessionID(session);
          if (sessionIdOptional.isPresent()) {
            serverEvents.newSession(sessionIdOptional.get(), information);
            logger.fine(String.format("Session created: %s", session.getSessionId()));
          } else {
            throw new IllegalStateException("Failed to create a session");
          }
        });
  }

  private Optional<UUID> getSessionID(ISession session) {
    if (!sessions.containsValue(session)) {
      return Optional.empty();
    }

    return Optional.of(session.getSessionId());
  }

  /** Close all connections and stop listening for clients. */
  public void close() {
    listener.close();
  }

  /**
   * Send a message to a client.
   *
   * @param sessionIndex Session index of the client.
   * @param request Request for the client.
   * @return Callback handler for when the client responds.
   * @throws UnsupportedFeatureException Thrown if the feature isn't among the list of supported
   *     featured.
   * @throws OccurenceConstraintException Thrown if the request isn't valid.
   */
  public CompletableFuture<Confirmation> send(UUID sessionIndex, Request request)
      throws UnsupportedFeatureException, OccurenceConstraintException, NotConnectedException {
  	logger.info(String.format("Sending request from Server: %s", request));
    Optional<Feature> featureOptional = featureRepository.findFeature(request);
    if (!featureOptional.isPresent()) {
      throw new UnsupportedFeatureException();
    }

    if (!request.validate()) {
      throw new OccurenceConstraintException();
    }

    ISession session = sessions.get(sessionIndex);

    if (session == null) {
      logger.warning(String.format("Session not found by index: %s", sessionIndex));

      // No session found means client disconnected and request should be cancelled
      throw new NotConnectedException();
    }

    String id = session.storeRequest(request);
    CompletableFuture<Confirmation> promise = promiseRepository.createPromise(id);
    session.sendRequest(featureOptional.get().getAction(), request, id);
    return promise;
  }

  /**
   * Close connection to a client
   *
   * @param sessionIndex Session index of the client.
   */
  public void closeSession(UUID sessionIndex) {
    ISession session = sessions.get(sessionIndex);
    if (session != null) {
      session.close();
    }
  }
}
