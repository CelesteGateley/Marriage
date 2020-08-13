package net.sapphirehollow.marriage.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.sapphirehollow.marriage.Marriage;
import net.sapphirehollow.marriage.controllers.MarriageController;
import net.sapphirehollow.marriage.storage.PlayerStorage;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;

public class MarryCommand {

    public static void registerCommands() {
        LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
        arguments.put("player", new PlayerArgument());
        new CommandAPICommand("marry")
                .withArguments(arguments)
                .executes((sender, args) -> {
                    Player player = (Player) args[0];
                    if (sender instanceof OfflinePlayer) {
                        PlayerStorage playerStorage = Marriage.getStorageController().getPlayerStorage(player);
                        if (playerStorage.isEngaged((OfflinePlayer) sender) || playerStorage.isMarried((OfflinePlayer) sender)) {
                            sender.sendMessage(Marriage.getLanguageController().generateMessage("alreadyEngMar"));
                            return;
                        }
                        MarriageController.addEngageRequest((OfflinePlayer) sender, player);
                        player.spigot().sendMessage(getMarryComponent((Player) sender));
                    }
                })
        .register();
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
