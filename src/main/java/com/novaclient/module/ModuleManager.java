package com.novaclient.module;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages all modules and their lifecycle.
 */
public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();
    private final Map<String, Module> moduleMap = new HashMap<>();

    public ModuleManager() {
        // Register render event
        WorldRenderEvents.END.register(context -> {
            float partialTicks = context.tickDelta();
            for (Module module : modules) {
                if (module.isEnabled()) {
                    module.onRender(partialTicks);
                }
            }
        });
    }

    /**
     * Initializes all modules.
     */
    public void initializeModules() {
        // Modules will be registered here as they are implemented
    }

    /**
     * Registers a module.
     */
    public void registerModule(Module module) {
        modules.add(module);
        moduleMap.put(module.getName(), module);
    }

    /**
     * Called every tick to update modules.
     */
    public void onTick() {
        for (Module module : modules) {
            if (module.isEnabled()) {
                module.onTick();
            }
        }
    }

    /**
     * Handles keybind presses.
     */
    public void handleKeybind(String keybindId) {
        // Map keybind IDs to modules
        Module module = null;

        switch (keybindId) {
            case "freecam_toggle" -> module = moduleMap.get("Freecam");
            case "storage_esp_toggle" -> module = moduleMap.get("Storage ESP");
            case "player_esp_toggle" -> module = moduleMap.get("Player ESP");
        }

        if (module != null) {
            module.toggle();
        }
    }

    /**
     * Gets a module by name.
     */
    public Module getModule(String name) {
        return moduleMap.get(name);
    }

    /**
     * Gets all modules.
     */
    public List<Module> getAllModules() {
        return new ArrayList<>(modules);
    }

    /**
     * Gets modules by category.
     */
    public List<Module> getModulesByCategory(ModuleCategory category) {
        List<Module> result = new ArrayList<>();
        for (Module module : modules) {
            if (module.getCategory() == category) {
                result.add(module);
            }
        }
        return result;
    }

    /**
     * Enables all modules.
     */
    public void enableAll() {
        for (Module module : modules) {
            module.enable();
        }
    }

    /**
     * Disables all modules.
     */
    public void disableAll() {
        for (Module module : modules) {
            module.disable();
        }
    }
}
