package net.sapphirehollow.marriage;

import net.sapphirehollow.marriage.controllers.PlayerStorageController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Marriage extends JavaPlugin implements Listener {

    public static Marriage instance;
    private static PlayerStorageController playerStorageController;

    @Override
    public void onEnable() {
        instance = this;
        playerStorageController = new PlayerStorageController(this, "storage.yml");
        getServer().getPluginManager().registerEvents(this, this);
    }

    public static PlayerStorageController getPlayerStorageController() { return playerStorageController; }

    @EventHandler( priority = EventPriority.MONITOR)
    public void cachePlayerName(PlayerJoinEvent event) {
        playerStorageController.storeUniqueId(event.getPlayer().getName(), event.getPlayer().getUniqueId().toString());
    }
}
