package dev.ev1dent.perchVouchers.listeners;

import com.destroystokyo.paper.MaterialTags;
import dev.ev1dent.perchVouchers.VouchersMain;
import dev.ev1dent.perchVouchers.utils.Utils;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class InventoryClickListener implements Listener {

    private VouchersMain voucherPlugin() {
        return VouchersMain.getPlugin(VouchersMain.class);
    }

    Utils Utils = new Utils();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack cursorItem = event.getCursor();

        if (!isCustomItem(cursorItem)) {
            return;
        }

        if (isArmor(event.getCurrentItem()) && isArmorVoucher(cursorItem)) {
            handleUnbreakable(event);
            event.setCancelled(true);
        }

        if (isTool(event.getCurrentItem()) && isToolVoucher(cursorItem)) {
            handleUnbreakable(event);
            event.setCancelled(true);
        }
    }

    public boolean isArmor(ItemStack item) {
        return item != null && isTaggedArmor(item);
    }

    public boolean isTool(ItemStack item) {
        return item != null && isTaggedTool(item);
    }

    public boolean isTaggedArmor(ItemStack item) {
        List armorList = voucherPlugin().armorItemList;
        return MaterialTags.ARMOR.isTagged(item.getType()) ||
                armorList.contains(item.getType().toString());
    }

    public boolean isTaggedTool(ItemStack item) {
        return isTaggedToolMaterial(item) ||
                MaterialTags.WOODEN_TOOLS.isTagged(item.getType()) ||
                MaterialTags.STONE_TOOLS.isTagged(item.getType()) ||
                MaterialTags.IRON_TOOLS.isTagged(item.getType()) ||
                MaterialTags.GOLDEN_TOOLS.isTagged(item.getType()) ||
                MaterialTags.DIAMOND_TOOLS.isTagged(item.getType()) ||
                MaterialTags.NETHERITE_TOOLS.isTagged(item.getType());
    }

    public boolean isTaggedToolMaterial(ItemStack item) {
        List list = voucherPlugin().toolItemList;
        return list.contains(item.getType().toString());

    }

    private boolean isCustomItem(ItemStack item) {
        return item.getItemMeta() != null && item.getItemMeta().getPersistentDataContainer().has(voucherPlugin().getKey(), PersistentDataType.STRING);
    }

    private boolean isArmorVoucher(ItemStack item) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        if(!container.has(new NamespacedKey(voucherPlugin(), "ItemType"), PersistentDataType.STRING)) {
            return false;
        }
        return item.getItemMeta() != null && container.get(new NamespacedKey(voucherPlugin(), "ItemType"),
                PersistentDataType.STRING).equalsIgnoreCase("armor");
    }

    private boolean isToolVoucher(ItemStack item) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        if(!container.has(new NamespacedKey(voucherPlugin(), "ItemType"), PersistentDataType.STRING)) {
            return false;
        }
        return item.getItemMeta() != null && container.get(new NamespacedKey(voucherPlugin(), "ItemType"),
                PersistentDataType.STRING).equalsIgnoreCase("tool");
    }

    private void handleUnbreakable(InventoryClickEvent event) {
        ItemStack itemPiece = event.getCurrentItem();
        ItemMeta meta = itemPiece.getItemMeta();
        if (meta.isUnbreakable()) {
            event.getWhoClicked().sendMessage(Utils.formatMM("<red>This item is already unbreakable!"));
            return;
        }
        meta.setUnbreakable(true);
        itemPiece.setItemMeta(meta);
        event.getWhoClicked().sendMessage(Utils.formatMM("<green>Successfully made item unbreakable!"));
        event.getCursor().setAmount(event.getCursor().getAmount() - 1);
    }

}
