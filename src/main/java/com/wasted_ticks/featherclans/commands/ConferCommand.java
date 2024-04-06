package com.wasted_ticks.featherclans.commands;

import com.wasted_ticks.featherclans.FeatherClans;
import com.wasted_ticks.featherclans.config.FeatherClansMessages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;

public class ConferCommand implements CommandExecutor {

    private final FeatherClans plugin;
    private final FeatherClansMessages messages;

    public ConferCommand(FeatherClans plugin) {
        this.plugin = plugin;
        this.messages = plugin.getFeatherClansMessages();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(messages.get("clan_error_player", null));
            return true;
        }

        if (!sender.hasPermission("feather.clans.confer")) {
            sender.sendMessage(messages.get("clan_error_permission", null));
            return true;
        }


        Player originator = (Player) sender;
        if (!plugin.getClanManager().isOfflinePlayerLeader(originator)) {
            originator.sendMessage(messages.get("clan_error_leader", null));
            return true;
        }

        if (args.length < 2) {
            originator.sendMessage(messages.get("clan_confer_no_player", null));
            return true;
        }

        // TODO: Player is required to be online. Should be possible for offline.

        Player potentialLeader = Bukkit.getPlayer(args[1]);

        if (potentialLeader == null) {
            originator.sendMessage(messages.get("clan_confer_unresolved_player", null));
            return true;
        }

        if (plugin.getClanManager().isOfflinePlayerLeader(potentialLeader)) {
            sender.sendMessage(messages.get("clan_confer_error_leader", null));
            return true;
        }

        String clan = this.plugin.getClanManager().getClanByOfflinePlayer(originator);
        if (!this.plugin.getClanManager().isOfflinePlayerInSpecificClan(potentialLeader, clan)) {
            originator.sendMessage(messages.get("clan_confer_not_in_clan", null));
            return true;
        }
        if (args.length == 2) {
            originator.sendMessage(messages.get("clan_confirm_notice", Map.of(
                    "label", label,
                    "args", String.join(" ", args)
            )));
            return true;
        }
        else if (args.length == 3 && !args[2].equalsIgnoreCase("confirm")) {
            originator.sendMessage(messages.get("clan_confirm_notice", Map.of(
                    "label", label,
                    "args", String.join(" ", Arrays.copyOf(args, args.length-1))
            )));
            return true;
        }
        else if (args.length >= 4) {
            originator.sendMessage(messages.get("clan_confer_error_generic", null));
            return true;
        }

        boolean successful = this.plugin.getClanManager().setClanLeader(clan, potentialLeader);
        if (successful) {
            originator.sendMessage(messages.get("clan_confer_success_originator", Map.of(
                    "player", potentialLeader.getName()
            )));
            potentialLeader.sendMessage(messages.get("clan_confer_success_player", Map.of(
                    "player", originator.getName(),
                    "clan", clan
            )));
        } else {
            originator.sendMessage(messages.get("clan_confer_error_generic", null));
        }
        return true;

    }
}
