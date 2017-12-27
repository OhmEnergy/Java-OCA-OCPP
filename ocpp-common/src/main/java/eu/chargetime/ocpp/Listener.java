package eu.chargetime.ocpp;
/*
    ChargeTime.eu - Java-OCA-OCPP
    
    MIT License

    Copyright (C) 2016 Thomas Volden <tv@chargetime.eu>

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

public interface Listener {

    /**
     * register handler that reacts to the messages received via the communication layer.
     *
     * @param eventHandler
     */
    void setEventHandler(ListenerEvents eventHandler);

    /**
     * start a server and wait for clients to connect. Received messages are forwarded to the handler
     * that was registered via {@link #setEventHandler(ListenerEvents)}
     *
     * @param hostname host name used to find the proper network interface that this server listens to
     * @param port     port to listen to clients to
     */
    void open(String hostname, int port);

    /**
     * close / showdown server
     */
    void close();

    /**
     * switch request processing mode between asynchronous and synchronous.
     *
     * @param async
     */
    void setAsyncRequestHandler(boolean async);
}
