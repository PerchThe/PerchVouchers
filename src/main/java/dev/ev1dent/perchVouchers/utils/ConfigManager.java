package dev.ev1dent.perchVouchers.utils;


import dev.ev1dent.perchVouchers.VouchersMain;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class ConfigManager {

    private VouchersMain voucherPlugin() {
        return VouchersMain.getPlugin(VouchersMain.class);
    }

    public void loadConfig() {
        voucherPlugin().reloadConfig();
        FileConfiguration config = voucherPlugin().getConfig();
        copyDefaults();

        voucherPlugin().armorItemName = config.getString("armor.name");
        voucherPlugin().armorItemLore = config.getList("armor.lore");

        voucherPlugin().toolItemName = config.getString("tools.name");
        voucherPlugin().toolItemLore = config.getList("tools.lore");

    }

    private void copyDefaults(){
        final File configFile = new File(voucherPlugin().getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            voucherPlugin().getLogger().info("Config Doesn't exist. Creating default config file.");
            voucherPlugin().saveResource("config.yml", false);
        }
    }
}