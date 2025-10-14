package eu.chargetime.ocpp;
/*
 ChargeTime.eu - Java-OCA-OCPP
 Copyright (C) 2015-2016 Thomas Volden <tv@chargetime.eu>

 MIT License

 Copyright (C) 2016-2018 Thomas Volden

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

import eu.chargetime.ocpp.wss.WssSocketBuilder;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Proxy;
import java.net.URI;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;
import java.util.logging.Logger;

/** Web Socket implementation of the Transmitter. */
public class WebSocketTransmitter implements Transmitter {
  private static final Logger logger = Logger.getLogger(WebSocketTransmitter.class.getName());

  public static final String WSS_SCHEME = "wss";
  private final Draft draft;

  private final JSONConfiguration configuration;
  private volatile boolean closed = true;
  private volatile WebSocketClient client;
  private WssSocketBuilder wssSocketBuilder;

  public WebSocketTransmitter(JSONConfiguration configuration, Draft draft) {
    this.configuration = configuration;
    this.draft = draft;
  }

  public WebSocketTransmitter(Draft draft) {
    this(JSONConfiguration.get(), draft);
  }

  @Override
  public void connect(String uri, RadioEvents events) {
    final URI resource = URI.create(uri);

    client =
        new WebSocketClient(resource, draft) {
          @Override
          public void onOpen(ServerHandshake serverHandshake) {
            logger.fine(String.format("On connection open (HTTP status: %s)", serverHandshake.getHttpStatus()));
            events.connected();
          }

          @Override
          public void onMessage(String message) {
            events.receivedMessage(message);
          }

          @Override
          public void onClose(int code, String reason, boolean remote) {
            logger.info(
                String.format("On connection close (code: %s, reason: %s, remote: %s)", code, reason, remote));

            events.disconnected();
          }

          @Override
          public void onError(Exception ex) {
            if (ex instanceof ConnectException) {
              logger.severe(String.format("On error triggered caused by: %s", ex));
            } else {
              logger.severe(String.format("On error triggered: %s", ex));
            }
          }
        };

    if (WSS_SCHEME.equals(resource.getScheme())) {

      if (wssSocketBuilder == null) {
        throw new IllegalStateException(
            "wssSocketBuilder must be set to support " + WSS_SCHEME + " scheme");
      }

      try {
        client.setSocket(wssSocketBuilder.uri(resource).build());
      } catch (IOException ex) {
        logger.severe(String.format("SSL socket creation failed: %s", ex));
      }
    }

    configure();

    logger.fine(String.format("Trying to connect to: %s", resource));

    try {
      client.connectBlocking();
      closed = false;
    } catch (Exception ex) {
      logger.warning(String.format("client.connectBlocking() failed: %s", ex));
    }
  }

  void configure() {
    client.setReuseAddr(configuration.getParameter(JSONConfiguration.REUSE_ADDR_PARAMETER, false));
    client.setTcpNoDelay(
        configuration.getParameter(JSONConfiguration.TCP_NO_DELAY_PARAMETER, false));
    client.setConnectionLostTimeout(
        configuration.getParameter(JSONConfiguration.PING_INTERVAL_PARAMETER, 60));
    client.setProxy(configuration.getParameter(JSONConfiguration.PROXY_PARAMETER, Proxy.NO_PROXY));
  }

  void enableWSS(WssSocketBuilder wssSocketBuilder) {
    if (client != null) {
      throw new IllegalStateException("Cannot enable WSS on already connected client");
    }
    this.wssSocketBuilder = wssSocketBuilder;
  }

  @Override
  public void disconnect() {
    if (client == null) {
      return;
    }
    try {
      client.closeBlocking();
    } catch (Exception ex) {
      logger.info(String.format("client.closeBlocking() failed: %s", ex));
    } finally {
      client = null;
      closed = true;
    }
  }

  @Override
  public void send(Object request) throws NotConnectedException {
    if (client == null) {
      throw new NotConnectedException();
    }

    try {
      client.send(request.toString());
    } catch (WebsocketNotConnectedException ex) {
      throw new NotConnectedException();
    }
  }

  public boolean isClosed() {
    return closed;
  }
}
