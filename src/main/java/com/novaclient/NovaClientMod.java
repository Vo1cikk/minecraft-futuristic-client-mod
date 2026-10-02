package com.novaclient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import com.novaclient.config.ConfigManager;
import com.novaclient.keybind.KeybindManager;
import com.novaclient.module.ModuleManager;

/**
 * Main entry point for NovaClient mod.
 * Initializes all subsystems including config, keybinds, and modules.
 */
public class NovaClientMod implements ClientModInitializer {
    public static final String MOD_ID = "novaclient";
    public static final String MOD_NAME = "NovaClient";

    private static ConfigManager configManager;
    private static KeybindManager keybindManager;
    private static ModuleManager moduleManager;

    @Override
    public void onInitializeClient() {
        // Initialize configuration system
        configManager = new ConfigManager();
        configManager.loadConfig();

        // Initialize keybind system
        keybindManager = new KeybindManager();
        keybindManager.initializeKeybinds();

        // Initialize module system
        moduleManager = new ModuleManager();
        moduleManager.initializeModules();

        // Register lifecycle events
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            onClientStarted();
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            onClientTick();
        });
    }

    private static void onClientStarted() {
        // Additional initialization if needed
    }

    private static void onClientTick() {
        if (moduleManager != null) {
            moduleManager.onTick();
        }
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }

    public static KeybindManager getKeybindManager() {
        return keybindManager;
    }

    public static ModuleManager getModuleManager() {
        return moduleManager;
    }
}
