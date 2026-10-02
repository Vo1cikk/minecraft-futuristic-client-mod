package com.novaclient.module;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.novaclient.NovaClientMod;

/**
 * Base class for all mod modules.
 * Provides common functionality for enabling/disabling and configuration.
 */
public abstract class Module {
    protected final String name;
    protected final String description;
    protected boolean enabled;

    public Module(String name, String description) {
        this.name = name;
        this.description = description;
        this.enabled = false;
        loadSettings();
    }

    /**
     * Called every game tick if module is enabled.
     */
    public void onTick() {
        // Override in subclasses
    }

    /**
     * Called when render event fires.
     */
    public void onRender(float partialTicks) {
        // Override in subclasses
    }

    /**
     * Called when module is enabled.
     */
    public void onEnable() {
        // Override in subclasses
    }

    /**
     * Called when module is disabled.
     */
    public void onDisable() {
        // Override in subclasses
    }

    /**
     * Toggles the module.
     */
    public void toggle() {
        if (enabled) {
            disable();
        } else {
            enable();
        }
    }

    /**
     * Enables the module.
     */
    public void enable() {
        if (!enabled) {
            enabled = true;
            onEnable();
            saveSettings();
        }
    }

    /**
     * Disables the module.
     */
    public void disable() {
        if (enabled) {
            enabled = false;
            onDisable();
            saveSettings();
        }
    }

    /**
     * Checks if module is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Gets the module name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the module description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the module's settings as JSON.
     */
    public JsonObject getSettings() {
        var configManager = NovaClientMod.getConfigManager();
        return configManager.getModuleConfig(name);
    }

    /**
     * Loads settings from configuration.
     */
    protected void loadSettings() {
        var configManager = NovaClientMod.getConfigManager();
        enabled = configManager.getBoolean(name, "enabled", false);
    }

    /**
     * Saves settings to configuration.
     */
    protected void saveSettings() {
        var configManager = NovaClientMod.getConfigManager();
        configManager.set(name, "enabled", new JsonPrimitive(enabled));
        configManager.saveConfig();
    }

    /**
     * Gets the category this module belongs to.
     */
    public ModuleCategory getCategory() {
        return ModuleCategory.MISC;
    }
}
