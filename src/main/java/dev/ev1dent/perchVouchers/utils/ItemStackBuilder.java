package dev.ev1dent.perchVouchers.utils;

import dev.ev1dent.perchVouchers.VouchersMain;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemStackBuilder {
    Utils Utils = new Utils();

    private VouchersMain voucherPlugin() {
        return VouchersMain.getPlugin(VouchersMain.class);
    }

    private String displayName;
    private List loreLine;
    private String itemType;


    public ItemStackBuilder setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public ItemStackBuilder setLore(List line) {
        this.loreLine = line;
        return this;
    }
    public ItemStackBuilder setItemType(String itemType) {
        this.itemType = itemType;
        return this;
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(Material.PAPER);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.displayName(Utils.formatMM(displayName));

            List<Component> lore = new ArrayList<>();
            loreLine.stream()
                    .map(line -> Utils.formatMM(line.toString()))
                    .forEach(coloredLine -> {
                        lore.add((Component) coloredLine);
                    });
            meta.lore(lore);

            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(voucherPlugin().getKey(), PersistentDataType.STRING, UUID.randomUUID().toString());
            container.set(new NamespacedKey(voucherPlugin(), "ItemType"), PersistentDataType.STRING, itemType);

            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }
}
