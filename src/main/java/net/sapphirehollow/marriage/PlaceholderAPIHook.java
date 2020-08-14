package net.sapphirehollow.marriage;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.sapphirehollow.marriage.controllers.MarriageController;
import net.sapphirehollow.marriage.storage.PlayerStorage;
import org.bukkit.entity.Player;

public class PlaceholderAPIHook extends PlaceholderExpansion {


    @Override
    public boolean persist(){ return true; }

    @Override
    public boolean canRegister(){ return true; }

    @Override
    public String getIdentifier() {
        return "marriage";
    }

    @Override
    public String getAuthor() {
        return Marriage.instance.getDescription().getAuthors().get(0);
    }

    @Override
    public String getVersion() {
        return Marriage.instance.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) return "";
        return Marriage.getStorageController().getPlayerStorage(player).getChatPrefix() + " ";
    }
}