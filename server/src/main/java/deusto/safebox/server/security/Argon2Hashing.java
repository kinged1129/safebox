package deusto.safebox.server.security;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Argon2Hashing {

    private static final Logger logger = LoggerFactory.getLogger(Argon2Hashing.class);

    private static final Argon2 ARGON2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
    /** Memory usage in kibibytes. Using 1 GiB. */
    private static final int ARGON2_MEMORY_USAGE = 1024 * 1024;
    /** Thread count. Its recommended to use the same amount of threads as available cores. */
    private static final int ARGON2_THREAD_COUNT = 8;

    static {
        logger.info("Argon2 memory usage: {} MiB", ARGON2_MEMORY_USAGE / 1024);
        logger.info("Argon2 thread count: {}", ARGON2_THREAD_COUNT);
    }

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(4);

    /**
     * Hashes a password.
     *
     * @param password the password.
     * @param iterations Argon2 iteration count.
     * @return the hashed password.
     */
    public static CompletableFuture<String> hash(String password, int iterations) {
        CompletableFuture<String> future = new CompletableFuture<>();
        EXECUTOR_SERVICE.submit(() -> {
            String hash = ARGON2.hash(iterations, ARGON2_MEMORY_USAGE, ARGON2_THREAD_COUNT, password.toCharArray());
            future.complete(hash);
        });
        return future;
    }

    /**
     * Verifies a password against a hash.
     *
     * @param hash the hash.
     * @param password the password.
     * @return true if the password matches the hash, false otherwise.
     */
    public static CompletableFuture<Boolean> verify(String hash, String password) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        EXECUTOR_SERVICE.submit(() -> {
            boolean result = ARGON2.verify(hash, password.toCharArray());
            future.complete(result);
        });
        return future;
    }

    private Argon2Hashing() {
        throw new AssertionError();
    }
}
