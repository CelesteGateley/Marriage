package net.sapphirehollow.marriage.storage;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerStorage implements ConfigurationSerializable {

    private static final String DEFAULT_COLOR = ChatColor.DARK_RED.toString();

    private final List<String> engagements;
    private final List<String> marriages;
    private String preferredColor;

    public PlayerStorage() {
        engagements = new ArrayList<>();
        marriages = new ArrayList<>();
        preferredColor = DEFAULT_COLOR;
    }

    public PlayerStorage(Map<String, Object> data) {
        engagements = (List<String>) data.getOrDefault("engagements", new ArrayList<>());
        marriages = (List<String>) data.getOrDefault("marriages", new ArrayList<>());
        preferredColor = "" + data.getOrDefault("preferredColor", DEFAULT_COLOR);
    }

    public List<String> getEngagements() {
        return engagements;
    }

    public List<String> getMarriages() {
        return marriages;
    }

    public String getPreferredColor() {
        return preferredColor;
    }

    public boolean isMarried(OfflinePlayer player) {
        return marriages.contains(player.getUniqueId().toString());
    }

    public boolean isEngaged(OfflinePlayer player) {
        return marriages.contains(player.getUniqueId().toString());
    }

    public boolean isMarried(String uuid) {
        return marriages.contains(uuid);
    }

    public boolean isEngaged(String uuid) {
        return marriages.contains(uuid);
    }

    public void addEngagement(OfflinePlayer player) {
        engagements.add(player.getUniqueId().toString());
    }

    public void removeEngagement(OfflinePlayer player) {
        engagements.remove(player.getUniqueId().toString());
    }

    public void addMarriage(OfflinePlayer player) {
        marriages.add(player.getUniqueId().toString());
    }

    public void removeMarriage(OfflinePlayer player) {
        marriages.remove(player.getUniqueId().toString());
    }

    public void addEngagement(String uuid) {
        engagements.add(uuid);
    }

    public void removeEngagement(String uuid) {
        engagements.remove(uuid);
    }

    public void addMarriage(String uuid) {
        marriages.add(uuid);
    }

    public void removeMarriage(String uuid) {
        marriages.remove(uuid);
    }

    public void setPreferredColor(String preferredColor) {
        this.preferredColor = preferredColor;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("engagements", engagements);
        map.put("marriages", marriages);
        if (!preferredColor.equals(DEFAULT_COLOR)) {
            map.put("preferredColor", preferredColor);
        }

        return map;
    }
}
