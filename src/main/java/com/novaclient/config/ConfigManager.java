package com.novaclient.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages persistent configuration for the mod.
 * Automatically saves and loads settings from JSON files in the Minecraft config directory.
 */
public class ConfigManager {
    private static final String CONFIG_FILE = "novaclient/config.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Path configPath;
    private final Map<String, JsonObject> configs;

    public ConfigManager() {
        this.configPath = FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE);
        this.configs = new HashMap<>();
        ensureConfigDirectory();
    }

    /**
     * Ensures the configuration directory exists.
     */
    private void ensureConfigDirectory() {
        try {
            Files.createDirectories(configPath.getParent());
        } catch (IOException e) {
            System.err.println("[NovaClient] Failed to create config directory: " + e.getMessage());
        }
    }

    /**
     * Loads configuration from disk.
     */
    public void loadConfig() {
        if (!Files.exists(configPath)) {
            createDefaultConfig();
            return;
        }

        try (FileReader reader = new FileReader(configPath.toFile())) {
            JsonElement element = GSON.fromJson(reader, JsonElement.class);
            if (element != null && element.isJsonObject()) {
                JsonObject root = element.getAsJsonObject();
                for (String key : root.keySet()) {
                    if (root.get(key).isJsonObject()) {
                        configs.put(key, root.get(key).getAsJsonObject());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[NovaClient] Failed to load config: " + e.getMessage());
            createDefaultConfig();
        }
    }

    /**
     * Saves configuration to disk.
     */
    public void saveConfig() {
        JsonObject root = new JsonObject();

        for (Map.Entry<String, JsonObject> entry : configs.entrySet()) {
            root.add(entry.getKey(), entry.getValue());
        }

        try (FileWriter writer = new FileWriter(configPath.toFile())) {
            GSON.toJson(root, writer);
        } catch (IOException e) {
            System.err.println("[NovaClient] Failed to save config: " + e.getMessage());
        }
    }

    /**
     * Creates default configuration.
     */
    private void createDefaultConfig() {
        configs.clear();
        saveConfig();
    }

    /**
     * Gets or creates a configuration section for a module.
     */
    public JsonObject getModuleConfig(String moduleName) {
        return configs.computeIfAbsent(moduleName, k -> new JsonObject());
    }

    /**
     * Sets a value in a module's configuration.
     */
    public void set(String moduleName, String key, JsonElement value) {
        JsonObject moduleConfig = getModuleConfig(moduleName);
        moduleConfig.add(key, value);
        saveConfig();
    }

    /**
     * Gets a value from a module's configuration.
     */
    public JsonElement get(String moduleName, String key) {
        JsonObject moduleConfig = getModuleConfig(moduleName);
        return moduleConfig.get(key);
    }

    /**
     * Gets a string value with a default.
     */
    public String getString(String moduleName, String key, String defaultValue) {
        JsonElement element = get(moduleName, key);
        return element != null && element.isJsonPrimitive()
            ? element.getAsString()
            : defaultValue;
    }

    /**
     * Gets a boolean value with a default.
     */
    public boolean getBoolean(String moduleName, String key, boolean defaultValue) {
        JsonElement element = get(moduleName, key);
        return element != null && element.isJsonPrimitive()
            ? element.getAsBoolean()
            : defaultValue;
    }

    /**
     * Gets an integer value with a default.
     */
    public int getInt(String moduleName, String key, int defaultValue) {
        JsonElement element = get(moduleName, key);
        return element != null && element.isJsonPrimitive()
            ? element.getAsInt()
            : defaultValue;
    }

    /**
     * Gets a double value with a default.
     */
    public double getDouble(String moduleName, String key, double defaultValue) {
        JsonElement element = get(moduleName, key);
        return element != null && element.isJsonPrimitive()
            ? element.getAsDouble()
            : defaultValue;
    }

    /**
     * Gets a float value with a default.
     */
    public float getFloat(String moduleName, String key, float defaultValue) {
        JsonElement element = get(moduleName, key);
        return element != null && element.isJsonPrimitive()
            ? element.getAsFloat()
            : defaultValue;
    }
}
