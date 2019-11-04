package deusto.safebox.common.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** JSON formatted config file loader. */
public class JsonConfig implements ConfigFile {

    private static final Logger logger = LoggerFactory.getLogger(JsonConfig.class);

    private final Path file;
    private JsonObject root;

    public JsonConfig(Path file) throws IOException {
        this.file = file;
        root = Constants.GSON.fromJson(Files.newBufferedReader(file, StandardCharsets.UTF_8), JsonObject.class);
    }

    @Override
    public int getInt(String path) {
        return getFromPath(path).getAsInt();
    }

    @Override
    public void setInt(int value, String path) {
        // TODO
    }

    @Override
    public String getString(String path) {
        return getFromPath(path).getAsString();
    }

    @Override
    public void setString(String value, String path) {
        // TODO
    }

    private JsonObject getPathObject(String[] path) {
        JsonObject current = root;
        for (int i = 0; i < path.length - 1; i++) {
            current = current.getAsJsonObject(path[i]);
        }
        return current;
    }

    @Override
    public void save() {
        try {
            Constants.GSON.toJson(root, Files.newBufferedWriter(file));
        } catch (IOException e) {
            logger.error("Error saving JSON config file.", e);
        }
    }

    @Override
    public String toString() {
        return root.toString();
    }

    /**
     * Returns the element in the specified JSON path.
     * The path is a string of keys separated by a period.
     *
     * <p>For example, the path to access the street attribute in this JSON object is <code>address.street</code>
     * <pre>
     * {
     *  name: "John",
     *  age: 31,
     *  address: {
     *      street: "Sesame Street"
     *  }
     * }
     * </pre>
     *
     * @param path the path to the element.
     * @return the {@link JsonElement} in the path.
     */
    private JsonElement getFromPath(String path) {
        JsonElement current = root;
        String[] s = path.split("\\.");
        for (String value : s) {
            if (current instanceof JsonObject) {
                current = current.getAsJsonObject().get(value);
            }
        }
        return current;
    }

    /**
     * Creates a {@link JsonConfig} from a JSON file in the system disk.
     * If the file doesn't exist in the system disk, then the resource file in {@code resourcePath}
     * is first extracted to {@code extractionPath} and then read.
     *
     * @param resource file path to the JSON file in the application resources.
     * @param extract file path where to read or extract the file.
     * @throws IOException if there is an error reading or extracting the file.
     */
    public static JsonConfig ofResource(Path resource, Path extract) throws IOException {
        if (!Files.exists(extract)) {
            logger.trace("Config file doesn't exist, creating it.");
            Files.copy(resource, extract);
        }
        return new JsonConfig(extract);
    }
}
