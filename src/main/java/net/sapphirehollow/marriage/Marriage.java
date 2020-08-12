package net.sapphirehollow.marriage;

import net.sapphirehollow.marriage.commands.MarryCommand;
import net.sapphirehollow.marriage.controllers.StorageController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public final class Marriage extends JavaPlugin implements Listener {

    public static Marriage instance;
    private static StorageController storageController;

    @Override
    public void onEnable() {
        instance = this;
        storageController = new StorageController(this, "storage.yml");
        getServer().getPluginManager().registerEvents(this, this);
        MarryCommand.registerCommands();
    }

    public static StorageController getStorageController() { return storageController; }

    @EventHandler( priority = EventPriority.MONITOR)
    public void cachePlayerName(PlayerJoinEvent event) {
        storageController.storeUniqueId(event.getPlayer().getName(), event.getPlayer().getUniqueId().toString());
    }

    public static boolean isVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
    }
}
