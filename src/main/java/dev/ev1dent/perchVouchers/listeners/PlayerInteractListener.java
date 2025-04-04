package dev.ev1dent.perchVouchers.listeners;

import dev.ev1dent.perchVouchers.VouchersMain;
import dev.ev1dent.perchVouchers.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class PlayerInteractListener implements Listener {

    Utils Utils = new Utils();

    private VouchersMain voucherPlugin() {
        return VouchersMain.getPlugin(VouchersMain.class);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if(item == null) return;
        if(item.getType() != Material.BOOK) return;
        if(!item.getItemMeta().getPersistentDataContainer().has(voucherPlugin().getGuideBookKey(), PersistentDataType.BOOLEAN)){
            return;
        }
        for (String command : voucherPlugin().guideBookCommands){
            handleCommands(command, player);
        }
    }

    private void handleCommands(String command, Player player) {
        if(command.startsWith("[message]")) {
            player.sendMessage(Utils.formatMM(command.replace("[message] ", "")));
            return;
        }
        voucherPlugin().getServer().dispatchCommand(voucherPlugin().getServer().getConsoleSender(), command.replace("{PLAYER}", player.getName()));
    }
}
