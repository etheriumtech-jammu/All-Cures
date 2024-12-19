package Chat_Function;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.security.KeyStore;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Set;
import java.util.Map;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.java_websocket.WebSocket;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;
import org.java_websocket.server.WebSocketServer;
import org.java_websocket.handshake.ClientHandshake;

import org.json.JSONObject;
import util.Constant;
import util.Encryption;

public class SocketIOServer1 extends WebSocketServer {

    private static final String KEYSTORE_PASSWORD = "Password@123";
    private static final String KEY_PASSWORD = "Password@123";
    private static final String KEYSTORE_PATH = "/home/etheriumtechnologies/all-cures.com5/all-cures.com.jks";

    private static final AtomicInteger connectionCount = new AtomicInteger(0);
    private static final Map<String, Set<WebSocket>> rooms = new ConcurrentHashMap<>();
    private static final Map<WebSocket, String> clients = new ConcurrentHashMap<>();
    private static SocketIOServer1 serverInstance; // Singleton instance
    private boolean running = false;

    // Change constructor access to public for instantiation
    public SocketIOServer1(int port) {
        super(new InetSocketAddress("0.0.0.0", port));
        SSLContext sslContext = getSSLContext();
        if (sslContext != null) {
            setWebSocketFactory(new DefaultSSLWebSocketServerFactory(sslContext));
        }
    }

    public static SocketIOServer1 getInstance(int port) {
        if (serverInstance == null) {
            serverInstance = new SocketIOServer1(port);
        }
        return serverInstance;
    }

    public static boolean isPortAvailable(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            return true; // Port is available
        } catch (IOException e) {
            return false; // Port is in use
        }
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void start() {
        super.start();
        running = true;
        System.out.println("WebSocket server started on port: " + this.getPort());
    }

    @Override
    public void stop() throws IOException, InterruptedException {
        super.stop();
        running = false;
        SocketIOServer1.serverInstance = null; // Reset singleton instance
        System.out.println("WebSocket server instance has been stopped and cleared.");
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("A client has connected: " + conn.getRemoteSocketAddress());
        clients.put(conn, null);
        connectionCount.incrementAndGet();
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("A client has disconnected: " + conn.getRemoteSocketAddress());
        connectionCount.decrementAndGet();
        String roomName = clients.get(conn);
        if (roomName != null && rooms.containsKey(roomName)) {
            rooms.get(roomName).remove(conn);
            if (rooms.get(roomName).isEmpty()) {
                rooms.remove(roomName);
            }
        }
        clients.remove(conn);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        try {
            if (message.contains("Room_No")) {
                JSONObject json = new JSONObject(message);
                String roomName = json.getString("Room_No");
                rooms.computeIfAbsent(roomName, k -> new CopyOnWriteArraySet<>()).add(conn);
                clients.put(conn, roomName);
            } else {
                handleMessage(conn, message);
            }
        } catch (Exception e) {
            System.err.println("Error handling message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleMessage(WebSocket sender, String message) {
        try {
            String[] parts = message.split(":");
            if (parts.length < 4) {
                sender.send("Invalid message format");
                return;
            }

            String sendId = parts[0];
            String recipientId = parts[1];
            String roomName = parts[2];
            String messageContent = parts[3];

            Encryption encrypt = new Encryption();
            final String secretKey = Constant.SECRETE;
            String encryptedMessage = encrypt.encrypt(messageContent, secretKey);

            if (rooms.containsKey(roomName)) {
                for (WebSocket client : rooms.get(roomName)) {
                    if (client != sender) {
                        client.send(message);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error in handleMessage: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("Error on connection: " + (conn != null ? conn.getRemoteSocketAddress() : "Unknown"));
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("WebSocket server started successfully.");
    }

    private SSLContext getSSLContext() {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(KEYSTORE_PATH), KEYSTORE_PASSWORD.toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, KEY_PASSWORD.toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            return sslContext;
        } catch (Exception e) {
            System.err.println("Failed to initialize SSLContext: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
