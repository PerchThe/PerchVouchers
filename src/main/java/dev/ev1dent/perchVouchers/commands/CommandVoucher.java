package dev.ev1dent.perchVouchers.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.ev1dent.perchVouchers.VouchersMain;
import dev.ev1dent.perchVouchers.utils.ConfigManager;
import dev.ev1dent.perchVouchers.utils.ItemStackBuilder;
import dev.ev1dent.perchVouchers.utils.Utils;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.List;

@SuppressWarnings({"UnstableApiUsage", "SameReturnValue"})
@NullMarked
public final class CommandVoucher {

    private VouchersMain voucherPlugin() {
        return VouchersMain.getPlugin(VouchersMain.class);
    }

    private final ConfigManager configManager = new ConfigManager();
    private final Utils Utils = new Utils();

    private final List<String> amountSuggestions = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9");
    private final SuggestionProvider<CommandSourceStack> amountSuggestionsProvider = (ctx, builder) -> {
        amountSuggestions.stream()
            .filter(amount -> amount.startsWith(builder.getRemaining()))
            .forEach(builder::suggest);
        return builder.buildFuture();
    };

    public LiteralCommandNode<CommandSourceStack> constructCommand() {
        return Commands.literal("perchvouchers")
            .requires(source -> source.getSender().hasPermission("perchvouchers.use"))

            .then(Commands.literal("give")
                .then(Commands.argument("player", ArgumentTypes.player())
                    
                    .then(Commands.literal("tool")
                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                            .suggests(amountSuggestionsProvider)
                            .executes(this::giveTool)
                        )
                    )
                    
                    .then(Commands.literal("armor")
                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                            .suggests(amountSuggestionsProvider)
                            .executes(this::giveArmor)
                        )
                    )
                    
                )
            )
            
            .then(Commands.literal("reload")
                .executes(ctx -> {
                    configManager.loadConfig();
                    ctx.getSource().getSender().sendMessage(Utils.formatMM("<green>Reloading config..."));
                    return Command.SINGLE_SUCCESS;
                })
            )
            
            .then(Commands.literal("set")
                .requires(source -> source.getExecutor() instanceof Player)
                    .then(Commands.literal("guidebook")
                            .executes(this::setGuideBook)
                    )
                    .then(Commands.literal("unbreakable")
                            .executes(this::setUnbreakable)
                            .then(Commands.literal("false")
                                    .executes(this::setBreakable)
                            )
                    )
            )
            .build();
    }
    
    private int setGuideBook(final CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        final CommandSender sender = ctx.getSource().getSender();
        if (!(ctx.getSource().getExecutor() instanceof Player player)) {
            return Command.SINGLE_SUCCESS;
        }
        
        boolean success = player.getInventory().getItemInMainHand().editPersistentDataContainer(container -> {
            if (container.has(voucherPlugin().getGuideBookKey(), PersistentDataType.BOOLEAN)) {
                sender.sendMessage(Utils.formatMM("<red>This item is already a guide!"));
            }
            else {
                container.set(voucherPlugin().getGuideBookKey(), PersistentDataType.BOOLEAN, true);
            }
        });
        
        if (!success) {
            final Message message = MessageComponentSerializer.message().serialize(Component.text("You are not holding a valid item!"));
            throw new CommandSyntaxException(new SimpleCommandExceptionType(message), message);
        }

        return Command.SINGLE_SUCCESS;
    }

    private int giveTool(final CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        final CommandSender sender = ctx.getSource().getSender();
        final Player player = ctx.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst();
        final int amount = IntegerArgumentType.getInteger(ctx, "amount");

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

        sender.sendMessage(Utils.formatMM(String.format("<green>Successfully gave %s %s %s voucher(s)", player.getName(), amount, "tool")));
        return Command.SINGLE_SUCCESS;
    }

    private int giveArmor(final CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        final CommandSender sender = ctx.getSource().getSender();
        final Player player = ctx.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst();
        final int amount = IntegerArgumentType.getInteger(ctx, "amount");

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

        sender.sendMessage(Utils.formatMM(String.format("<green>Successfully gave %s %s %s voucher(s)", player.getName(), amount, "armor")));
        return Command.SINGLE_SUCCESS;
    }

    private int setUnbreakable(final CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        final CommandSender sender = ctx.getSource().getSender();
        if (!(ctx.getSource().getExecutor() instanceof Player player)) {
            return Command.SINGLE_SUCCESS;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        if (meta.isUnbreakable()) {
            sender.sendMessage(Utils.formatMM("<red>This item is already unbreakable!"));
            return Command.SINGLE_SUCCESS;
        }
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        sender.sendMessage(Utils.formatMM("<green>Successfully made item unbreakable!"));
        return Command.SINGLE_SUCCESS;
    }

    private int setBreakable(final CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        final CommandSender sender = ctx.getSource().getSender();
        if (!(ctx.getSource().getExecutor() instanceof Player player)) {
            return Command.SINGLE_SUCCESS;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        if (!meta.isUnbreakable()) {
            sender.sendMessage(Utils.formatMM("<red>This item is already breakable!"));
            return Command.SINGLE_SUCCESS;
        }
        meta.setUnbreakable(false);
        item.setItemMeta(meta);
        sender.sendMessage(Utils.formatMM("<green>Successfully made item breakable!"));
        return Command.SINGLE_SUCCESS;
    }
}