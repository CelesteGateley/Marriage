package net.sapphirehollow.marriage;

import net.sapphirehollow.marriage.controllers.PlayerStorageController;
import org.bukkit.plugin.java.JavaPlugin;

public final class Marriage extends JavaPlugin {

    public static Marriage instance;
    private static PlayerStorageController playerStorageController;

    @Override
    public void onEnable() {
        instance = this;
        playerStorageController = new PlayerStorageController(this, "storage.yml");
    }

    public static PlayerStorageController getPlayerStorageController() { return playerStorageController; }
}
