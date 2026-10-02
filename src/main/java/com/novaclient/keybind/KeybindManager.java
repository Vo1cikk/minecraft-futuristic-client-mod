package com.novaclient.keybind;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import com.novaclient.NovaClientMod;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages keybinds for all modules.
 * Supports custom keybind configuration and conflict detection.
 */
public class KeybindManager {
    private final Map<String, KeyBinding> keyBindings = new HashMap<>();
    private final Map<String, String> keyBindingDescriptions = new HashMap<>();

    public KeybindManager() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> handleKeybinds());
    }

    /**
     * Initializes all keybinds for modules.
     */
    public void initializeKeybinds() {
        // Initialize keybinds with defaults
        registerKeyBinding("gui_open", GLFW.GLFW_KEY_RIGHT_SHIFT, "Nova GUI");
        registerKeyBinding("freecam_toggle", GLFW.GLFW_KEY_F, "Toggle Freecam");
        registerKeyBinding("storage_esp_toggle", GLFW.GLFW_KEY_E, "Toggle Storage ESP");
        registerKeyBinding("player_esp_toggle", GLFW.GLFW_KEY_P, "Toggle Player ESP");
    }

    /**
     * Registers a new keybinding.
     */
    public void registerKeyBinding(String id, int keyCode, String description) {
        String translationKey = "key.novaclient." + id;
        keyBindingDescriptions.put(id, description);

        KeyBinding binding = KeyBindingHelper.registerKeyBinding(
            new KeyBinding(translationKey, keyCode, "category.novaclient")
        );
        keyBindings.put(id, binding);

        // Load saved keybind if exists
        loadKeybind(id);
    }

    /**
     * Handles keybind presses.
     */
    private void handleKeybinds() {
        for (Map.Entry<String, KeyBinding> entry : keyBindings.entrySet()) {
            while (entry.getValue().wasPressed()) {
                onKeybindPressed(entry.getKey());
            }
        }
    }

    /**
     * Called when a keybind is pressed.
     */
    private void onKeybindPressed(String keybindId) {
        // Delegate to module manager
        var moduleManager = NovaClientMod.getModuleManager();
        if (moduleManager != null) {
            moduleManager.handleKeybind(keybindId);
        }
    }

    /**
     * Gets a keybinding by ID.
     */
    public KeyBinding getKeyBinding(String id) {
        return keyBindings.get(id);
    }

    /**
     * Sets a keybinding to a new key.
     */
    public void setKeybinding(String id, int keyCode) {
        KeyBinding binding = keyBindings.get(id);
        if (binding != null) {
            binding.setBoundKey(InputUtil.fromKeyCode(keyCode, -1));
            saveKeybind(id, keyCode);
        }
    }

    /**
     * Gets the current key code for a keybind.
     */
    public int getKeyCode(String id) {
        KeyBinding binding = keyBindings.get(id);
        return binding != null ? binding.getKey().getCode() : -1;
    }

    /**
     * Gets the human-readable name of a keybind.
     */
    public String getKeybindName(String id) {
        KeyBinding binding = keyBindings.get(id);
        if (binding != null) {
            return binding.getBoundKeyLocalizedText().getString();
        }
        return "Unbound";
    }

    /**
     * Saves a keybind to configuration.
     */
    private void saveKeybind(String id, int keyCode) {
        var configManager = NovaClientMod.getConfigManager();
        if (configManager != null) {
            JsonObject keybinds = configManager.getModuleConfig("keybinds");
            keybinds.addProperty(id, keyCode);
            configManager.saveConfig();
        }
    }

    /**
     * Loads a keybind from configuration.
     */
    private void loadKeybind(String id) {
        var configManager = NovaClientMod.getConfigManager();
        if (configManager != null) {
            int keyCode = configManager.getInt("keybinds", id, -1);
            if (keyCode != -1) {
                setKeybinding(id, keyCode);
            }
        }
    }

    /**
     * Gets all keybindings.
     */
    public Map<String, KeyBinding> getAllKeybindings() {
        return new HashMap<>(keyBindings);
    }

    /**
     * Checks for keybind conflicts.
     */
    public boolean hasConflict(String id1, String id2) {
        int code1 = getKeyCode(id1);
        int code2 = getKeyCode(id2);
        return code1 != -1 && code1 == code2;
    }
}
