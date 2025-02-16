package com.wasted_ticks.featherclans.config;

import com.wasted_ticks.featherclans.FeatherClans;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Locale;

public class FeatherClansConfig {

    private final FeatherClans plugin;
    private FileConfiguration config;

    /* SETTINGS */
    private boolean economyEnabled;
    private double economyCreationPrice;
    private double economyInvitePrice;
    private double economyPartnershipPrice;
    private double economySetHomePrice;
    private boolean cleanupEnabled;
    private int cleanupArchiveDays;
    private int cleanupDeleteDays;
    private int clanInviteTimeout;
    private int clanTeleportDelaySeconds;
    private int clanMinTagSize;
    private int clanMaxTagSize;
    private int clanMaxMembers;
    private int clanInactiveDays;
    private int clanActiveStatusCount;
    private String defaultClanColor;
    private boolean mysqlEnabled;
    private String mysqlUsername;
    private String mysqlHost;
    private int mysqlPort;
    private String mysqlPassword;
    private String mysqlDatabase;
    private List<String> denyTags;
    private int linesPerPage;
    private int pvpScoreRelevantDays;
    private String pvpScoreCalculationExplained;
    private boolean displayEnabled;
    private List<Integer> displayOriginSignLocation;
    private String displayFacing;
    private int displayIncrement;
    private int displayCount;


    public FeatherClansConfig(FeatherClans plugin) {
        this.plugin = plugin;
        this.plugin.saveDefaultConfig();
        config = this.plugin.getConfig();
        this.loadConfig();
    }

    private void loadConfig() {
        this.economyEnabled = config.getBoolean("settings.economy.enabled");
        this.economyCreationPrice = config.getDouble("settings.economy.creation_price");
        this.economyInvitePrice = config.getDouble("settings.economy.invite_price");
        this.economyPartnershipPrice = config.getDouble("settings.economy.partnership_price");
        this.economySetHomePrice = config.getDouble("settings.economy.set_home_price");
        this.cleanupEnabled = config.getBoolean("settings.clean_up.enabled");
        this.cleanupArchiveDays = config.getInt("settings.clean_up.archive_days");
        this.cleanupDeleteDays = config.getInt("settings.clean_up.delete_days");
        this.clanInviteTimeout = config.getInt("settings.clan.invite_timeout");
        this.clanTeleportDelaySeconds = config.getInt("settings.clan.teleport_delay_seconds");
        this.clanMinTagSize = config.getInt("settings.clan.min_tag_size");
        this.clanMaxTagSize = config.getInt("settings.clan.max_tag_size");
        this.clanMaxMembers = config.getInt("settings.clan.max_members");
        this.clanInactiveDays = config.getInt("settings.clan.inactive_days");
        this.clanActiveStatusCount = config.getInt("settings.clan.active_status_count");
        this.defaultClanColor = config.getString("settings.clan.default_clan_tag_color");
        this.mysqlEnabled = config.getBoolean("settings.mysql.enabled");
        this.mysqlUsername = config.getString("settings.mysql.username");
        this.mysqlHost = config.getString("settings.mysql.host");
        this.mysqlPort = config.getInt("settings.mysql.port");
        this.mysqlPassword = config.getString("settings.mysql.password");
        this.mysqlDatabase = config.getString("settings.mysql.database");
        this.denyTags = config.getStringList("settings.deny_tags");
        this.linesPerPage = config.getInt("settings.page-formats.lines-per-page");
        this.pvpScoreRelevantDays = config.getInt("settings.pvp-score.relevant-days");
        this.pvpScoreCalculationExplained = config.getString("settings.pvp-score.calculation-explained");
        this.displayEnabled = config.getBoolean("settings.display.enabled");
        this.displayOriginSignLocation = config.getIntegerList("settings.display.origin-sign-location");
        this.displayFacing = config.getString("settings.display.facing").toUpperCase();
        this.displayIncrement = config.getInt("settings.display.increment", 1);
        this.displayCount = config.getInt("settings.display.count", 20);
    }

    public boolean isEconomyEnabled() {
        return economyEnabled;
    }
    public void setEconomyEnabled(boolean economyEnabled) {
        this.economyEnabled = economyEnabled;
    }
    public double getEconomyCreationPrice() {
        return economyCreationPrice;
    }
    public double getEconomyInvitePrice() {
        return economyInvitePrice;
    }
    public double getEconomyPartnershipPrice() {
        return economyPartnershipPrice;
    }
    public double getEconomySetHomePrice() {
        return economySetHomePrice;
    }
    public boolean isCleanupEnabled() {
        return cleanupEnabled;
    }
    public int getCleanupArchiveDays() {
        return cleanupArchiveDays;
    }
    public int getCleanupDeleteDays() {
        return cleanupDeleteDays;
    }
    public int getClanInviteTimeout() {
        return clanInviteTimeout;
    }
    public int getClanTeleportDelaySeconds() {
        return clanTeleportDelaySeconds;
    }
    public int getClanMinTagSize() {
        return clanMinTagSize;
    }
    public int getClanMaxTagSize() {
        return clanMaxTagSize;
    }
    public int getClanMaxMembers() {
        return clanMaxMembers;
    }
    public int getInactiveDays() {
        return clanInactiveDays;
    }
    public int getClanActiveStatusCount() {
        return clanActiveStatusCount;
    }
    public String getDefaultClanColor() {
        return defaultClanColor;
    }
    public boolean isMysqlEnabled() {
        return mysqlEnabled;
    }
    public String getMysqlUsername() {
        return mysqlUsername;
    }
    public String getMysqlHost() {
        return mysqlHost;
    }
    public int getMysqlPort() {
        return mysqlPort;
    }
    public String getMysqlPassword() {
        return mysqlPassword;
    }
    public String getMysqlDatabase() {
        return mysqlDatabase;
    }
    public List<String> getDenyTags() {
        return denyTags;
    }
    public int getLinesPerPage() {
        return linesPerPage;
    }
    public int getPVPScoreRelevantDays() {
        return pvpScoreRelevantDays;
    }
    public String getPVPScoreCalculationExplained() {
        return pvpScoreCalculationExplained;
    }
    public boolean isDisplayEnabled() {
        return displayEnabled;
    }
    public List<Integer> getDisplayOriginSignLocation() {
        return displayOriginSignLocation;
    }
    public String getDisplayFacing() {
        return displayFacing;
    }
    public int getDisplayIncrement() {
        return displayIncrement;
    }
    public int getDisplayCount() {
        return displayCount;
    }

}
