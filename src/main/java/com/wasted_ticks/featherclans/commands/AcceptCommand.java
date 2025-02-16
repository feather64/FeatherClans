package com.wasted_ticks.featherclans.commands;

import com.wasted_ticks.featherclans.FeatherClans;
import com.wasted_ticks.featherclans.config.FeatherClansMessages;
import com.wasted_ticks.featherclans.managers.ClanManager;
import com.wasted_ticks.featherclans.managers.RequestManager;
import com.wasted_ticks.featherclans.utilities.RequestUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class AcceptCommand implements CommandExecutor {

    private final FeatherClans plugin;
    private final FeatherClansMessages messages;

    private final ClanManager manager;

    public AcceptCommand(FeatherClans plugin) {
        this.plugin = plugin;
        this.messages = plugin.getFeatherClansMessages();
        this.manager = plugin.getClanManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(messages.get("clan_error_player", null));
            return true;
        }

        if (!sender.hasPermission("feather.clans.accept")) {
            sender.sendMessage(messages.get("clan_error_permission", null));
            return true;
        }

        Player acceptingPlayer = (Player) sender;
        RequestUtil request = this.plugin.getRequestManager().getRequest(acceptingPlayer);

        if (request == null) {
            acceptingPlayer.sendMessage(messages.get("clan_accept_no_request", null));
            return false;
        }

        switch (request.getType()) {
            case CLAN_INVITE:
                return handleClanInvite(acceptingPlayer, request);
            case PARTNERSHIP_INVITE:
                return handlePartnershipRequest(acceptingPlayer, request);
            default:
                return true;
        }
    }

    private boolean handleClanInvite(Player player, RequestUtil request) {

        boolean inClan = plugin.getMembershipManager().isOfflinePlayerInClan(player);
        if (inClan) {
            player.sendMessage(messages.get("clan_accept_in_clan", null));
            return true;
        }

        String tag = request.getClan();
        Player originator = request.getOriginator();

        boolean success;
        if (this.plugin.getFeatherClansConfig().isEconomyEnabled()) {
            Economy economy = plugin.getEconomy();
            double amount = this.plugin.getFeatherClansConfig().getEconomyInvitePrice();

            if (economy.has(player, amount)) {
                economy.withdrawPlayer(player, amount);
                success = plugin.getMembershipManager().addOfflinePlayerToClan(player, tag);
            } else {
                player.sendMessage(messages.get("clan_accept_error_economy", Map.of(
                        "amount", String.valueOf((int) amount)
                )));
                return true;
            }
        } else success = plugin.getMembershipManager().addOfflinePlayerToClan(player, tag);

        if(success) {
            plugin.getRequestManager().clearRequest(player);

            player.sendMessage(messages.get("clan_accept_success_player", Map.of(
                    "clan", tag
            )));
            originator.sendMessage(messages.get("clan_accept_success_originator", Map.of(
                    "player", player.getName()
            )));
        }
        return true;
    }

    private boolean handlePartnershipRequest(Player acceptingPlayer, RequestUtil request) {

        boolean isLeader = plugin.getMembershipManager().isOfflinePlayerLeader(acceptingPlayer);
        String acceptingClan = plugin.getMembershipManager().getClanByOfflinePlayer(acceptingPlayer);
        if (!isLeader) {
            acceptingPlayer.sendMessage(messages.get("clan_accept_error_not_leader", null));
            return true;
        }

        String tag = request.getClan();
        Player originator = request.getOriginator();

        if (manager.hasPartner(acceptingClan) || manager.hasPartner(tag)) {
            acceptingPlayer.sendMessage(messages.get("clan_partner_request_error_already_partnered", null));
            plugin.getRequestManager().clearRequest(acceptingPlayer);
            return true;
        }

        if (!plugin.getActivityManager().isClanActive(acceptingClan) || !plugin.getActivityManager().isClanActive(tag)) {
            acceptingPlayer.sendMessage(messages.get("clan_partner_request_error_not_active_status", null));
            plugin.getRequestManager().clearRequest(acceptingPlayer);
            return true;
        }

        boolean success;
        if (this.plugin.getFeatherClansConfig().isEconomyEnabled()) {
            Economy economy = plugin.getEconomy();
            double amount = this.plugin.getFeatherClansConfig().getEconomyPartnershipPrice();

            if (economy.has(acceptingPlayer, amount) && economy.has(request.getOriginator(), amount)) {
                economy.withdrawPlayer(acceptingPlayer, amount);
                economy.withdrawPlayer(request.getOriginator(), amount);
                success = manager.setPartnership(tag, acceptingClan);
            } else {
                acceptingPlayer.sendMessage(messages.get("clan_partner_request_error_economy", Map.of(
                        "amount", String.valueOf((int) amount)
                )));
                return true;
            }
        } else success = manager.setPartnership(tag, acceptingClan);

        if(success) {
            plugin.getRequestManager().clearRequest(acceptingPlayer);
            acceptingPlayer.sendMessage(messages.get("clan_accept_partnership_success_player", Map.of(
                    "clan", tag
            )));
            originator.sendMessage(messages.get("clan_accept_partnership_success_originator", Map.of(
                    "clan", acceptingClan
            )));
        }
        return true;
    }
}
