package net.sapphirehollow.marriage.storage;

import net.sapphirehollow.marriage.Marriage;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

public class PlayerStorage implements ConfigurationSerializable {

    private static final String DEFAULT_COLOR = ChatColor.DARK_RED.toString();

    private final List<String> engagements;
    private final List<String> marriages;
    private String preferredColor;
    private boolean teleportToggle;
    private boolean marriageSpyToggle;

    public PlayerStorage() {
        engagements = new ArrayList<>();
        marriages = new ArrayList<>();
        preferredColor = DEFAULT_COLOR;
        teleportToggle = false;
        marriageSpyToggle = true;
    }

    public PlayerStorage(Map<String, Object> data) {
        engagements = (List<String>) data.getOrDefault("engagements", new ArrayList<>());
        marriages = (List<String>) data.getOrDefault("marriages", new ArrayList<>());
        preferredColor = "" + data.getOrDefault("preferredColor", DEFAULT_COLOR);
        teleportToggle = (boolean) data.getOrDefault("teleportToggle", false);
        marriageSpyToggle = (boolean) data.getOrDefault("marriageSpyToggle", true);
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
        return engagements.contains(player.getUniqueId().toString());
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

    public boolean toggleMarriageSpy() { marriageSpyToggle = !marriageSpyToggle; return marriageSpyToggle; }

    public boolean marriageSpy() { return marriageSpyToggle; }

    public List<String> getAllPartnerUuids() {
        List<String> uuids = new ArrayList<>(marriages);
        uuids.addAll(engagements);
        return uuids;
    }

    public List<OfflinePlayer> getAllPartners() {
        List<OfflinePlayer> partners = new ArrayList<>();
        for (String uuid : marriages) {
            partners.add(Marriage.instance.getServer().getOfflinePlayer(UUID.fromString(uuid)));
        }
        for (String uuid : engagements) {
            partners.add(Marriage.instance.getServer().getOfflinePlayer(UUID.fromString(uuid)));
        }
        return partners;
    }

    public String getChatPrefix(boolean addSpace) {
        if (engagements.size() != 0 || marriages.size() != 0) {
            return this.preferredColor + "\u2665" + (addSpace ? " " : "");
        }
        return "";
    }

    public boolean allowTeleport() {
        return !teleportToggle;
    }

    public boolean toggleTeleport() {
        teleportToggle = !teleportToggle;
        return !teleportToggle;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        if (engagements.size() > 0) {
            map.put("engagements", engagements);
        }
        if (marriages.size() > 0) {
            map.put("marriages", marriages);
        }
        if (teleportToggle) {
            map.put("teleportToggle", true);
        }
        if (!preferredColor.equals(DEFAULT_COLOR)) {
            map.put("preferredColor", preferredColor);
        }
        if (!marriageSpyToggle) {
            map.put("marriageSpyToggle", false);
        }

        return map;
    }
}
