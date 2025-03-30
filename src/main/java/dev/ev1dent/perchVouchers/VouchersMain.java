package dev.ev1dent.perchVouchers;

import dev.ev1dent.perchVouchers.commands.CommandVoucher;
import dev.ev1dent.perchVouchers.commands.tabcompletion.TabCompletionCommandVoucher;
import dev.ev1dent.perchVouchers.listeners.InventoryClickListener;
import dev.ev1dent.perchVouchers.utils.ConfigManager;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class VouchersMain extends JavaPlugin implements Listener {

    ConfigManager configManager = new ConfigManager();

    public String armorItemName;
    public List armorItemLore;

    public String toolItemName;
    public List toolItemLore;

    @Override
    public void onEnable() {
        configManager.loadConfig();

        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        this.getCommand("perchvouchers").setExecutor(new CommandVoucher());
        this.getCommand("perchvouchers").setTabCompleter(new TabCompletionCommandVoucher());
    }

    public NamespacedKey getKey() {
        return new NamespacedKey(this, "generatedUUID");
    }

}
