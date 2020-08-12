package net.sapphirehollow.marriage.controllers;

import net.sapphirehollow.marriage.Marriage;
import net.sapphirehollow.marriage.storage.PlayerStorage;
import org.bukkit.OfflinePlayer;
import xyz.fluxinc.fluxcore.configuration.ConfigurationManager;

import java.util.HashMap;
import java.util.Map;
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
        return this.getGeneric(uuid);
    }
    public PlayerStorage getPlayerStorage(OfflinePlayer player) {
        return this.getGeneric(player.getUniqueId().toString());
    }

    public void setPlayerStorage(OfflinePlayer player, PlayerStorage storage) {
        this.getConfiguration().set(player.getUniqueId().toString(), storage);
    }

    public void setPlayerStorage(String uuid, PlayerStorage storage) {
        this.getConfiguration().set(uuid, storage);
    }

    public void storeUniqueId(String name, String uuid) {
        Map<String, String> names = this.getGeneric("id-map");
        names.put(name, uuid);
    }

    public OfflinePlayer getOfflinePlayer(String name) {
        Map<String, String> names = this.getGeneric("id-map");
        if (names.containsKey(name)) return Marriage.instance.getServer().getOfflinePlayer(UUID.fromString(names.get(name)));
        return Marriage.instance.getServer().getOfflinePlayer(name);
    }
}
