package net.sapphirehollow.marriage.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.sapphirehollow.marriage.Marriage;
import net.sapphirehollow.marriage.controllers.MarriageController;
import net.sapphirehollow.marriage.storage.PlayerStorage;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import xyz.fluxinc.fluxcore.command.Command;

public class MarryCommand {

    public static void register() {
        Command command = new Command("marry").player("player");;
        command.executor((sender, args) -> {
            Player player = (Player) args.get(0);
            if (sender instanceof OfflinePlayer) {
                if (((OfflinePlayer) sender).getUniqueId() == player.getUniqueId()) {
                    sender.sendMessage(Marriage.getLanguageController().generateMessage("marryFail"));
                    return;
                }
                PlayerStorage playerStorage = Marriage.getStorageController().getPlayerStorage(player);
                if (playerStorage.isEngaged((OfflinePlayer) sender) || playerStorage.isMarried((OfflinePlayer) sender)) {
                    sender.sendMessage(Marriage.getLanguageController().generateMessage("alreadyEngMar"));
                    return;
                }
                MarriageController.addEngageRequest((OfflinePlayer) sender, player);
                player.spigot().sendMessage(getMarryComponent((Player) sender));
            }
        });
        command.register();
    }

    private static TextComponent getMarryComponent(Player player) {
        String message = ChatColor.translateAlternateColorCodes('&', "&7[&9SH Marriage&7] &b" + player.getDisplayName() + "&b would like to marry you. Would you like to ");
        TextComponent mainComponent = new TextComponent(TextComponent.fromLegacyText(message));
        TextComponent yesComponent = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', "&7[&aAccept&7]")));
        TextComponent noComponent = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', "&7[&cDeny&7]")));

        yesComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/marriage confirm " + player.getName()));
        noComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/marriage deny " + player.getName()));

        mainComponent.addExtra(yesComponent);
        mainComponent.addExtra(new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', "&b or "))));
        mainComponent.addExtra(noComponent);
        return mainComponent;
    }
}
