package com.davidiba.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

/*import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

class GameScreen extends Screen {
    WebSocket socket;
    String address = "localhost";
    int port = 8888;

    // constructor de l'objecte Screen
    public GameScreen {
        if( Gdx.app.getType()== Application.ApplicationType.Android )
            // en Android el host és accessible per 10.0.2.2
            address = "10.0.2.2";
        socket = WebSockets.newSocket(WebSockets.toWebSocketUrl(address, port));
        socket.setSendGracefully(false);
        socket.addListener((WebSocketListener) new MyWSListener());
        socket.connect();
        socket.send("Enviar dades");
    }

    // Es poden enviar dades al render() en tems real!
    // Millor no fer-ho a cada frame per no saturar el server
    // ni ralentitzar el joc
    public void render() {
        if( stateTime-lastSend > 1.0f ) {
            lastSend = stateTime;
            socket.send("Enviar dades");
        }
    }

    // COMUNICACIONS (rebuda de missatges)
    /////////////////////////////////////////////
    class MyWSListener implements WebSocketListener {

        @Override
        public boolean onOpen(WebSocket webSocket) {
            System.out.println("Opening...");
            return false;
        }

        @Override
        public boolean onClose(WebSocket webSocket, int closeCode, String reason) {
            System.out.println("Closing...");
            return false;
        }

        @Override
        public boolean onMessage(WebSocket webSocket, String packet) {
            System.out.println("Message:");
            return false;
        }

        @Override
        public boolean onMessage(WebSocket webSocket, byte[] packet) {
            System.out.println("Message:");
            return false;
        }

        @Override
        public boolean onError(WebSocket webSocket, Throwable error) {
            System.out.println("ERROR:"+error.toString());
            return false;
        }

        @Override
        public void onWebSocketBinary(byte[] payload, int offset, int len) {

        }

        @Override
        public void onWebSocketClose(int statusCode, String reason) {

        }

        @Override
        public void onWebSocketConnect(Session session) {

        }

        @Override
        public void onWebSocketError(Throwable cause) {

        }

        @Override
        public void onWebSocketText(String message) {

        }
    }
}*/