package com.novaclient.module;

/**
 * Categorizes modules for GUI organization.
 */
public enum ModuleCategory {
    RENDER("Render"),
    MOVEMENT("Movement"),
    UTILITY("Utility"),
    MISC("Misc");

    public final String displayName;

    ModuleCategory(String displayName) {
        this.displayName = displayName;
    }
}
