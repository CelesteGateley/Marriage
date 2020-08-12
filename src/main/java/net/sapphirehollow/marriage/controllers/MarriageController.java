package net.sapphirehollow.marriage.controllers;

import net.sapphirehollow.marriage.Marriage;
import net.sapphirehollow.marriage.storage.PlayerStorage;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarriageController {

    public static final Map<OfflinePlayer, List<OfflinePlayer>> marriageRequests = new HashMap<>();

    public static boolean isMarriedTo(OfflinePlayer player1, OfflinePlayer player2) {
        return Marriage.getStorageController().getPlayerStorage(player1).isMarried(player2);
    }

    public static boolean isEngagedTo(OfflinePlayer player1, OfflinePlayer player2) {
        return Marriage.getStorageController().getPlayerStorage(player1).isEngaged(player2);
    }

    public static boolean marryPlayers(OfflinePlayer player1, OfflinePlayer player2) {
        PlayerStorage player1Storage = Marriage.getStorageController().getPlayerStorage(player1);
        PlayerStorage player2Storage = Marriage.getStorageController().getPlayerStorage(player2);
        if (!player1Storage.isEngaged(player2)) return false;
        if (!player2Storage.isEngaged(player1)) return false;
        player1Storage.removeEngagement(player2);
        player2Storage.removeEngagement(player1);
        player1Storage.addMarriage(player2);
        player2Storage.addMarriage(player1);
        Marriage.getStorageController().setPlayerStorage(player1, player1Storage);
        Marriage.getStorageController().setPlayerStorage(player2, player2Storage);
        return true;
    }

    public static boolean forceMarryPlayers(OfflinePlayer player1, OfflinePlayer player2) {
        PlayerStorage player1Storage = Marriage.getStorageController().getPlayerStorage(player1);
        PlayerStorage player2Storage = Marriage.getStorageController().getPlayerStorage(player2);
        player1Storage.removeEngagement(player2);
        player2Storage.removeEngagement(player1);
        player1Storage.addMarriage(player2);
        player2Storage.addMarriage(player1);
        Marriage.getStorageController().setPlayerStorage(player1, player1Storage);
        Marriage.getStorageController().setPlayerStorage(player2, player2Storage);
        return true;
    }

    public static boolean engagePlayers(OfflinePlayer player1, OfflinePlayer player2) {
        PlayerStorage player1Storage = Marriage.getStorageController().getPlayerStorage(player1);
        PlayerStorage player2Storage = Marriage.getStorageController().getPlayerStorage(player2);
        if (player1Storage.isMarried(player2)) return false;
        if (player2Storage.isMarried(player1)) return false;
        player1Storage.addEngagement(player2);
        player2Storage.addEngagement(player1);
        Marriage.getStorageController().setPlayerStorage(player1, player1Storage);
        Marriage.getStorageController().setPlayerStorage(player2, player2Storage);
        return true;
    }

    public static boolean divorcePlayers(OfflinePlayer player1, OfflinePlayer player2) {
        PlayerStorage player1Storage = Marriage.getStorageController().getPlayerStorage(player1);
        PlayerStorage player2Storage = Marriage.getStorageController().getPlayerStorage(player2);
        if (!(player1Storage.isEngaged(player2) || player2Storage.isEngaged(player1)
                || player1Storage.isMarried(player2) || player2Storage.isMarried(player1))) return false;
        player1Storage.removeEngagement(player2);
        player2Storage.removeEngagement(player1);
        player1Storage.removeMarriage(player2);
        player2Storage.removeMarriage(player1);
        Marriage.getStorageController().setPlayerStorage(player1, player1Storage);
        Marriage.getStorageController().setPlayerStorage(player2, player2Storage);
        return true;
    }

    public static boolean forceDivorcePlayers(OfflinePlayer player1, OfflinePlayer player2) {
        PlayerStorage player1Storage = Marriage.getStorageController().getPlayerStorage(player1);
        PlayerStorage player2Storage = Marriage.getStorageController().getPlayerStorage(player2);
        player1Storage.removeEngagement(player2);
        player2Storage.removeEngagement(player1);
        player1Storage.removeMarriage(player2);
        player2Storage.removeMarriage(player1);
        Marriage.getStorageController().setPlayerStorage(player1, player1Storage);
        Marriage.getStorageController().setPlayerStorage(player2, player2Storage);
        return true;
    }

    public static void addEngageRequest(OfflinePlayer sender, OfflinePlayer recipient) {
        List<OfflinePlayer> arr = marriageRequests.getOrDefault(sender, new ArrayList<>());
        arr.add(recipient);
        marriageRequests.put(sender, arr);
    }

    public static boolean confirmEngageRequest(OfflinePlayer sender, OfflinePlayer recipient) {
        List<OfflinePlayer> arr = marriageRequests.getOrDefault(recipient, new ArrayList<>());
        if (!arr.contains(sender)) return false;
        arr.remove(sender);
        marriageRequests.put(sender, arr);
        return engagePlayers(sender, recipient);
    }

    public static boolean denyEngageRequest(OfflinePlayer sender, OfflinePlayer recipient) {
        List<OfflinePlayer> arr = marriageRequests.getOrDefault(recipient, new ArrayList<>());
        if (!arr.contains(sender)) return false;
        arr.remove(sender);
        return true;
    }
}
