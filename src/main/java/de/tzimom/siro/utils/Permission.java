package de.tzimom.siro.utils;

public enum Permission {

    COMMAND_SIRO("siro.command.siro");

    private String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

}
