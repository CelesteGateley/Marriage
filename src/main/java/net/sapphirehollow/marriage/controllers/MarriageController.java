package net.sapphirehollow.marriage.controllers;

import net.sapphirehollow.marriage.Marriage;
import net.sapphirehollow.marriage.storage.PlayerStorage;
import org.bukkit.entity.Player;

public class MarriageController {

    public static boolean isMarriedTo(Player player1, Player player2) {
        return Marriage.getPlayerStorageController().getPlayerStorage(player1).isMarried(player2);
    }

    public static boolean isEngagedTo(Player player1, Player player2) {
        return Marriage.getPlayerStorageController().getPlayerStorage(player1).isEngaged(player2);
    }

    public static boolean marryPlayers(Player player1, Player player2) {
        PlayerStorage player1Storage = Marriage.getPlayerStorageController().getPlayerStorage(player1);
        PlayerStorage player2Storage = Marriage.getPlayerStorageController().getPlayerStorage(player2);
        if (!player1Storage.isEngaged(player2)) return false;
        if (!player2Storage.isEngaged(player1)) return false;
        player1Storage.removeEngagement(player2);
        player2Storage.removeEngagement(player1);
        player1Storage.addMarriage(player2);
        player2Storage.addMarriage(player1);
        Marriage.getPlayerStorageController().setPlayerStorage(player1, player1Storage);
        Marriage.getPlayerStorageController().setPlayerStorage(player2, player2Storage);
        return true;
    }

    public static boolean forceMarryPlayers(Player player1, Player player2) {
        PlayerStorage player1Storage = Marriage.getPlayerStorageController().getPlayerStorage(player1);
        PlayerStorage player2Storage = Marriage.getPlayerStorageController().getPlayerStorage(player2);
        player1Storage.removeEngagement(player2);
        player2Storage.removeEngagement(player1);
        player1Storage.addMarriage(player2);
        player2Storage.addMarriage(player1);
        Marriage.getPlayerStorageController().setPlayerStorage(player1, player1Storage);
        Marriage.getPlayerStorageController().setPlayerStorage(player2, player2Storage);
        return true;
    }

    public static boolean engagePlayers(Player player1, Player player2) {
        PlayerStorage player1Storage = Marriage.getPlayerStorageController().getPlayerStorage(player1);
        PlayerStorage player2Storage = Marriage.getPlayerStorageController().getPlayerStorage(player2);
        if (player1Storage.isMarried(player2)) return false;
        if (player2Storage.isMarried(player1)) return false;
        player1Storage.addEngagement(player2);
        player2Storage.addEngagement(player1);
        Marriage.getPlayerStorageController().setPlayerStorage(player1, player1Storage);
        Marriage.getPlayerStorageController().setPlayerStorage(player2, player2Storage);
        return true;
    }

    public static boolean divorcePlayers(Player player1, Player player2) {
        PlayerStorage player1Storage = Marriage.getPlayerStorageController().getPlayerStorage(player1);
        PlayerStorage player2Storage = Marriage.getPlayerStorageController().getPlayerStorage(player2);
        if (!(player1Storage.isEngaged(player2) || player2Storage.isEngaged(player1)
                || player1Storage.isMarried(player2) || player2Storage.isMarried(player1))) return false;
        player1Storage.removeEngagement(player2);
        player2Storage.removeEngagement(player1);
        player1Storage.removeMarriage(player2);
        player2Storage.removeMarriage(player1);
        Marriage.getPlayerStorageController().setPlayerStorage(player1, player1Storage);
        Marriage.getPlayerStorageController().setPlayerStorage(player2, player2Storage);
        return true;
    }

    public static boolean forceDivorcePlayers(String player1, String player2) {
        PlayerStorage player1Storage = Marriage.getPlayerStorageController().getPlayerStorage(player1);
        PlayerStorage player2Storage = Marriage.getPlayerStorageController().getPlayerStorage(player2);
        player1Storage.removeEngagement(player2);
        player2Storage.removeEngagement(player1);
        player1Storage.removeMarriage(player2);
        player2Storage.removeMarriage(player1);
        Marriage.getPlayerStorageController().setPlayerStorage(player1, player1Storage);
        Marriage.getPlayerStorageController().setPlayerStorage(player2, player2Storage);
        return true;
    }
}
