package net.sapphirehollow.marriage;

import net.sapphirehollow.marriage.commands.DivorceCommand;
import net.sapphirehollow.marriage.commands.MarriageCommand;
import net.sapphirehollow.marriage.commands.MarriedCommand;
import net.sapphirehollow.marriage.commands.MarryCommand;
import net.sapphirehollow.marriage.controllers.StorageController;
import net.sapphirehollow.marriage.storage.PlayerStorage;
import org.bukkit.OfflinePlayer;
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
        MarriageCommand.registerCommands();
        MarriedCommand.registerCommands();
        DivorceCommand.registerCommands();
    }

    public static StorageController getStorageController() { return storageController; }

    @EventHandler( priority = EventPriority.MONITOR)
    public void cachePlayerName(PlayerJoinEvent event) {
        storageController.storeUniqueId(event.getPlayer().getName(), event.getPlayer().getUniqueId().toString());
        PlayerStorage storage = storageController.getPlayerStorage(event.getPlayer());
        for (OfflinePlayer player : storage.getAllPartners()) {
            if (player.isOnline()) {
                player.getPlayer().sendMessage("Your partner has joined the server!");
            }
        }
    }

    public static boolean isVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
    }
}
