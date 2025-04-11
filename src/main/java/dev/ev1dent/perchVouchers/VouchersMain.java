package dev.ev1dent.perchVouchers;

import dev.ev1dent.perchVouchers.commands.CmdVoucher;
import dev.ev1dent.perchVouchers.listeners.InventoryClickListener;
import dev.ev1dent.perchVouchers.listeners.PlayerInteractListener;
import dev.ev1dent.perchVouchers.utils.ConfigManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
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

    public List<String> guideBookCommands;

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onLoad() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, 
            event -> event.registrar().register(new CommandVoucher().constructCommand(), "gives voucher")
        );
    }

    @Override
    public void onEnable() {
        configManager.loadConfig();

        this.getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
    }

    public NamespacedKey getKey() {
        return new NamespacedKey(this, "generatedUUID");
    }

    public NamespacedKey getGuideBookKey() {
        return new NamespacedKey(this, "guideBook");
    }

}
