package deusto.safebox.server.net;

import deusto.safebox.common.net.SocketHandler;
import deusto.safebox.server.dao.DaoManager;
import java.io.IOException;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.Collection;
import java.util.HashSet;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server extends Thread implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    private final int port;
    private final Path keyPath;
    private final String keyPassword;

    private SSLServerSocket serverSocket;
    private boolean running = false;
    private final Collection<ClientHandler> clients = new HashSet<>();

    private final PacketHandler packetHandler;

    /**
     * Creates a {@link Server} with the specified port.
     *
     * @param port server socket port number
     * @param keyPath JKS file path
     * @param keyPassword JKS file password
     * @param daoManager the {@link DaoManager} that is being used.
     *                   It is used for dependency injection on {@link PacketHandler}
     */
    public Server(int port, Path keyPath, String keyPassword, DaoManager daoManager) {
        this.port = port;
        this.keyPath = keyPath;
        this.keyPassword = keyPassword;
        packetHandler = new PacketHandler(this, daoManager);
    }

    public int getPort() {
        return port;
    }

    public synchronized boolean isRunning() {
        return running;
    }

    /** Start the socket server. */
    @Override
    public void run() {
        try {
            SSLServerSocketFactory ssf = createServerSocketFactory();
            serverSocket = (SSLServerSocket) ssf.createServerSocket(port);
        } catch (IOException | GeneralSecurityException e) {
            LOGGER.error("Could not create a server socket.", e);
            return;
        }

        LOGGER.info("Server started listening on port {}.", port);
        listen();
    }

    /**
     * Start listening to new clients.
     *
     * <p>Each time a new socket is accepted, a new {@link ClientHandler} is linked
     * to the socket and then added to {@link #clients}.
     */
    private void listen() {
        running = true;
        try {
            while (running) {
                SSLSocket socket = (SSLSocket) serverSocket.accept();
                addClient(socket);
            }
        } catch (SocketException e) {
            LOGGER.debug("Socket closing exception.");
        } catch (IOException e) {
            LOGGER.error("Error while server socket was listening.", e);
        } finally {
            if (!serverSocket.isClosed()) {
                close();
            }
        }
    }

    /** Close the socket server. */
    @Override
    public synchronized void close() {
        running = false;
        clients.forEach(SocketHandler::close);
        try {
            serverSocket.close();
            LOGGER.debug("Server socket closed.");
        } catch (IOException e) {
            LOGGER.error("Error closing server socket.", e);
        }
    }

    private void addClient(SSLSocket socket) {
        ClientHandler client = new ClientHandler(socket);
        client.setPacketReceived(packet -> {
            LOGGER.trace("Received a packet: {}", packet);
            packetHandler.fire(client, packet);
        });
        clients.add(client);
        client.start();
    }

    void removeClient(ClientHandler client) {
        clients.remove(client);
        client.close();
    }

    public int getClientCount() {
        return clients.size();
    }

    public int getAuthenticatedClientCount() {
        return packetHandler.getAuthenticatedClientCount();
    }

    private SSLServerSocketFactory createServerSocketFactory() throws IOException, GeneralSecurityException {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(Files.newInputStream(keyPath), keyPassword.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, keyPassword.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        SSLContext sc = SSLContext.getInstance("TLS");
        TrustManager[] trustManagers = tmf.getTrustManagers();
        sc.init(kmf.getKeyManagers(), trustManagers, null);

        return sc.getServerSocketFactory();
    }
}
