package deusto.safebox.server.net;

import deusto.safebox.common.net.SocketHandler;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
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

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private final int port;
    private final Path keyPath;
    private final String keyPassword;

    private SSLServerSocket serverSocket;
    private boolean running = false;
    private final Collection<ClientHandler> clients = new HashSet<>();

    /**
     * Creates a {@link Server} with the specified port.
     *
     * @param port server port number.
     * @param keyPath JKS file path.
     * @param keyPassword JKS file password.
     */
    public Server(int port, Path keyPath, String keyPassword) {
        this.port = port;
        this.keyPath = keyPath;
        this.keyPassword = keyPassword;
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
        SSLServerSocketFactory ssf;
        try {
            ssf = createServerSocketFactory();
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException
                | IOException | UnrecoverableKeyException | KeyManagementException e) {
            logger.error("Could not create a secure socket factory. ", e);
            return;
        }

        try {
            serverSocket = (SSLServerSocket) ssf.createServerSocket(port);
        } catch (IOException e) {
            logger.error("Could not create a server socket.", e);
            return;
        }

        logger.trace("Server started listening on port {}.", port);
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
                ClientHandler client = new ClientHandler(socket) {
                    @Override
                    protected void disconnect() {
                        removeClient(this);
                    }
                };
                clients.add(client);
                client.start();
            }
        } catch (SocketException e) {
            logger.debug("Socket closing exception.");
        } catch (IOException e) {
            logger.error("Error while server socket was listening.", e);
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
            logger.debug("Server socket closed.");
        } catch (IOException e) {
            logger.error("Error closing server socket.", e);
        }
    }

    private void removeClient(ClientHandler client) {
        clients.remove(client);
        client.close();
    }

    private SSLServerSocketFactory createServerSocketFactory()
            throws KeyStoreException, CertificateException, NoSuchAlgorithmException,
            IOException, UnrecoverableKeyException, KeyManagementException {
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
