package dev.ev1dent.perchVouchers.commands.tabcompletion;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TabCompletionCommandVoucher implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        final List<String> completions = new ArrayList<>();

        switch (args.length) {
            case 1 ->{
                StringUtil.copyPartialMatches(args[args.length - 1], Arrays.asList("give", "reload"), completions);
                Collections.sort(completions);
                return completions;
            }
            case 2 ->{
                if(args[0].equalsIgnoreCase("reload")) return  Collections.emptyList();
                else return null;
            }
            case 3 ->{
                switch (args[0].toLowerCase()) {
                    case "reload" -> {
                        return null;
                    }
                    case "give" -> {
                        StringUtil.copyPartialMatches(args[args.length - 1], Arrays.asList("tool", "armor"), completions);
                        Collections.sort(completions);
                        return completions;
                    }
                }
            }
            case 4 -> {
                if(args[0].equalsIgnoreCase("give")){
                    String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
                    StringUtil.copyPartialMatches(args[args.length - 1], Arrays.asList(numbers), completions);
                    Collections.sort(completions);
                    return completions;
                }
                return Collections.emptyList();
            }
        }
        return null;
    }
}
