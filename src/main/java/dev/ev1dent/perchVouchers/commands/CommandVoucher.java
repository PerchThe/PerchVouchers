package dev.ev1dent.perchVouchers.commands;

import dev.ev1dent.perchVouchers.VouchersMain;
import dev.ev1dent.perchVouchers.utils.ItemStackBuilder;
import dev.ev1dent.perchVouchers.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class CommandVoucher implements CommandExecutor {
    private VouchersMain voucherPlugin() {
        return VouchersMain.getPlugin(VouchersMain.class);
    }

    Utils Utils = new Utils();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        switch (args[0].toLowerCase()) {
            case "give": {
                if (args.length < 4) return false;
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(Utils.formatMM("<red>Player not found!"));
                    return true;
                }
                int amount = Integer.parseInt(args[3]);
                if (amount <= 0) return false;
                switch (args[2].toLowerCase()) {
                    case "tool": {
                        for (int i = 0; i < amount; i++) {
                        ItemStack tool = new ItemStackBuilder()
                                .setDisplayName(voucherPlugin().toolItemName)
                                .setLore(voucherPlugin().toolItemLore)
                                .setItemType("tool")
                                .build();
                            HashMap<Integer, ItemStack> hashMap = player.getInventory().addItem(tool);
                            if (!hashMap.isEmpty()) {
                                player.getWorld().dropItem(player.getLocation(), tool);
                            }
                        }
                        sender.sendMessage(Utils.formatMM(String.format("<green>Successfully gave %s %s %s voucher(s)", player.getName(), amount, args[2].toLowerCase())));
                        return true;
                    }
                    case "armor": {
                        for (int i = 0; i < amount; i++) {
                        ItemStack armor = new ItemStackBuilder()
                                .setDisplayName(voucherPlugin().armorItemName)
                                .setLore(voucherPlugin().armorItemLore)
                                .setItemType("armor")
                                .build();
                            HashMap<Integer, ItemStack> hashMap = player.getInventory().addItem(armor);
                            if (!hashMap.isEmpty()) {
                                player.getWorld().dropItem(player.getLocation(), armor);
                            }
                        }
                        sender.sendMessage(Utils.formatMM(String.format("<green>Successfully gave %s %s %s voucher(s)", player.getName(), amount, args[2].toLowerCase())));
                        return true;
                    }
                    default: {
                        return false;
                    }
                }
            }
            case "reload":

        }
        return true;
    }
}