package com.wasted_ticks.featherclans.commands.completers;

import com.wasted_ticks.featherclans.FeatherClans;
import com.wasted_ticks.featherclans.managers.ClanManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClanTabCompleter implements TabCompleter {

    private static final List<String> EVERYONE_COMMANDS = Arrays.asList(
            "accept",
            "create",
            "decline",
            "help",
            // "leaderboard",
            "list",
            "roster"
    );
    private static final List<String> MEMBER_COMMANDS = Arrays.asList(
            "chat",
            "elect",
            "friendlyfire",
            "help",
            "home",
            "list",
            "resign",
            "roster"
    );
    private static final List<String> OFFICER_COMMANDS = Arrays.asList(
            "chat",
            "colortag",
            "friendlyfire",
            "help",
            "home",
            "invite",
            "kick",
            "list",
            "promote",
            "resign",
            "roster",
            "sethome"
    );
    private static final List<String> LEADER_COMMANDS = Arrays.asList(
            "chat",
            "colortag",
            "confer",
            "demote",
            "disband",
            "friendlyfire",
            "help",
            "home",
            "invite",
            "kick",
            "list",
            "promote",
            "resign",
            "roster",
            "sethome"
    );
    private final FeatherClans plugin;
    private final ClanManager manager;


    public ClanTabCompleter(FeatherClans plugin) {
        this.plugin = plugin;
        this.manager = this.plugin.getClanManager();
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();


        switch (args.length) {
            case 1:
                if (manager.isOfflinePlayerLeader((OfflinePlayer) sender)) StringUtil.copyPartialMatches(args[0], LEADER_COMMANDS, completions);

                else if (manager.isOfflinePlayerInClan((OfflinePlayer) sender)) StringUtil.copyPartialMatches(args[0], MEMBER_COMMANDS, completions);

                else StringUtil.copyPartialMatches(args[0], EVERYONE_COMMANDS, completions);

                if(sender.isOp()) StringUtil.copyPartialMatches(args[0], List.of("banner","manage"), completions);

                break;

            case 2:
                switch (args[0]) {
                    case "invite":
                        if (manager.isOfflinePlayerLeader((Player) sender)) {
                            StringUtil.copyPartialMatches(args[1], plugin.getServer().getOnlinePlayers().stream()
                                    .filter(p -> !manager.isOfflinePlayerInClan(p))
                                    .map(Player::getName)
                                    .collect(Collectors.toList()), completions);
                        }
                        break;
                    case "partner":
                        if (manager.isOfflinePlayerLeader((Player) sender)) {
                            StringUtil.copyPartialMatches(args[1], manager.getClans().stream()
                                    .filter(c -> !Bukkit.getOfflinePlayer(manager.getLeader(c)).isOnline())
                                    .filter(c -> !c.equals(manager.getClanByOfflinePlayer((OfflinePlayer) sender)))
                                    .collect(Collectors.toList()), completions);
                        }
                        break;
                    case "kick":
                    case "confer":
                        if (manager.isOfflinePlayerLeader((Player) sender)) {
                            String tag = manager.getClanByOfflinePlayer((Player) sender);
                            StringUtil.copyPartialMatches(args[1], manager.getOfflinePlayersByClan(tag).stream()
                                    .filter(p -> !manager.isOfflinePlayerLeader(p))
                                    .map(OfflinePlayer::getName)
                                    .collect(Collectors.toList()), completions);
                        }
                        break;

                    case "promote":
                        if (manager.isOfflinePlayerLeader((Player) sender)) {
                            String tag = manager.getClanByOfflinePlayer((Player) sender);
                            StringUtil.copyPartialMatches(args[1], manager.getOfflinePlayersByClan(tag).stream()
                                    .filter(p -> !manager.isOfflinePlayerLeader(p) && !manager.isOfflinePlayerOfficer(p))
                                    .map(OfflinePlayer::getName)
                                    .collect(Collectors.toList()), completions);
                        }
                        break;

                    case "demote":
                        if (manager.isOfflinePlayerLeader((Player) sender)) {
                            String tag = manager.getClanByOfflinePlayer((Player) sender);
                            StringUtil.copyPartialMatches(args[1], manager.getOfflinePlayersByClan(tag).stream()
                                    .filter(manager::isOfflinePlayerOfficer)
                                    .map(OfflinePlayer::getName)
                                    .collect(Collectors.toList()), completions);
                        }
                        break;

                    case "banner":
                        if (sender.hasPermission("feather.clans.banner")) StringUtil.copyPartialMatches(args[1], manager.getClans(), completions);
                        break;

                    case "home":
                        if (sender.hasPermission("feather.clans.home.others")) StringUtil.copyPartialMatches(args[1], manager.getClans(), completions);
                        break;

                    case "roster":
                        if (sender.hasPermission("feather.clans.roster")) StringUtil.copyPartialMatches(args[1], manager.getClans(), completions);
                        // name
                        // hours
                        // kdr
                        // last seen
                        // completions = manager.getClans();
                        break;

                    case "leaderboard":
                        // hours: total of all offline players current hours
                        // kdr: mode, resets monthly
                        // completions.addAll(Arrays.asList("hours", "kdr"));
                        break;

                    case "list":
                        // tag
                        // creation date
                        // num of players
                        // last seen
                        // completions.addAll(Arrays.asList("alpha", "creation"));
                        break;

                    case "manage":
                        if (sender.hasPermission("feather.clans.manage")) StringUtil.copyPartialMatches(args[1], manager.getClans(), completions);
                        break;

                }
                break;

            case 3:
                // clan manage test chat    <message>

                // clan manage test confer  <clan-member>
                // clan manage test kick    <clan-member>
                // clan manage test invite  <player>


                // clan manage test disband
                // clan manage test sethome

                if (sender.hasPermission("feather.clans.manage") && args[0].equalsIgnoreCase("manage")) {
                    StringUtil.copyPartialMatches(args[2], List.of("confer","kick","invite","sethome","disband","chat"), completions);
                }
                break;

            case 4:
                if (sender.hasPermission("feather.clans.manage") && args[0].equalsIgnoreCase("manage")) {
                    switch (args[2]) {

                        case "kick":
                        case "confer":
                            String tag = args[1];
                            StringUtil.copyPartialMatches(args[3], manager.getOfflinePlayersByClan(tag).stream().map(OfflinePlayer::getName).collect(Collectors.toList()), completions);
                            break;

                        case "invite":
                            StringUtil.copyPartialMatches(args[3], plugin.getServer().getOnlinePlayers().stream().filter(p -> !manager.isOfflinePlayerInClan(p)).map(Player::getName).collect(Collectors.toList()), completions);
                            break;
                    }
                }

            default:
                break;
        }
        return completions;
    }
}
