package net.sapphirehollow.marriage.controllers;

import net.sapphirehollow.marriage.Marriage;
import net.sapphirehollow.marriage.storage.PlayerStorage;
import org.bukkit.OfflinePlayer;
import xyz.fluxinc.fluxcore.configuration.ConfigurationManager;

public class PlayerStorageController extends ConfigurationManager<Marriage> {
    /**
     * A Class used for managing configuration files within a FluxCore plugin
     *
     * @param instance      The plugin that is being used
     * @param configuration The name of the configuration file
     */
    public PlayerStorageController(Marriage instance, String configuration) {
        super(instance, configuration);
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
}
