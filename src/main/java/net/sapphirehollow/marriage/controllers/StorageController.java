package net.sapphirehollow.marriage.controllers;

import net.sapphirehollow.marriage.Marriage;
import net.sapphirehollow.marriage.storage.PlayerStorage;
import org.bukkit.OfflinePlayer;
import xyz.fluxinc.fluxcore.configuration.ConfigurationManager;

import java.util.HashMap;
import java.util.UUID;

public class StorageController extends ConfigurationManager<Marriage> {
    /**
     * A Class used for managing configuration files within a FluxCore plugin
     *
     * @param instance      The plugin that is being used
     * @param configuration The name of the configuration file
     */
    public StorageController(Marriage instance, String configuration) {
        super(instance, configuration);
        if (!this.getConfiguration().contains("id-map")) {
            this.getConfiguration().set("id-map", new HashMap<String, String>());
        }
    }

    public PlayerStorage getPlayerStorage(String uuid) {
        PlayerStorage storage = this.getGeneric(uuid);
        return storage != null ? storage : new PlayerStorage();
    }
    public PlayerStorage getPlayerStorage(OfflinePlayer player) {
        PlayerStorage storage = this.getGeneric(player.getUniqueId().toString());
        return storage != null ? storage : new PlayerStorage();
    }

    public void setPlayerStorage(OfflinePlayer player, PlayerStorage storage) {
        if (storage.serialize().size() > 0) {
            this.getConfiguration().set(player.getUniqueId().toString(), storage);
            saveConfiguration();
        }
    }

    public void setPlayerStorage(String uuid, PlayerStorage storage) {
        this.getConfiguration().set(uuid, storage);
    }

    public void storeUniqueId(String name, String uuid) {
        this.getConfiguration().set("id-map." + name, uuid);
        saveConfiguration();
    }

    public OfflinePlayer getOfflinePlayer(String name) {
        String id = this.getString("id-map." + name);
        if (id != null && !id.equals("")) return Marriage.instance.getServer().getOfflinePlayer(UUID.fromString(id));
        return Marriage.instance.getServer().getOfflinePlayer(name);
    }
}
