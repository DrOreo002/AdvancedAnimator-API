package me.droreo002.advancedanimatorapi.command;

import me.droreo002.advancedanimator.AdvancedAnimator;
import me.droreo002.advancedanimator.enums.LangEnum;
import me.droreo002.advancedanimator.enums.LogType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

/**
This is the class to manage the command things. You can create ur own module here! {@see ExampleCommand}
 */
public class CommandManager implements CommandExecutor, TabCompleter {

    private static final Set<AnimationCommand> COMMAND_REGISTRY = new HashSet<>();
    private static final Set<String> TAB_COMPLETER = new HashSet<>();
    private AdvancedAnimator main;

    /**
     * Add the command to the list
     *
     * @param command : The command object
     */
    public static void addCommand(AnimationCommand command) {
        for (AnimationCommand obj : COMMAND_REGISTRY) {
            if (command.getActivator().equalsIgnoreCase(obj.getActivator())) {
                throw new IllegalStateException("Command activator (" + command.getActivator() + ") already taken!. Please select a different one!");
            }
        }
        // Loop first, then add.
        COMMAND_REGISTRY.add(command);
        LangEnum.sendMessageToConsole("Successfully registered a command!. Command activator is (" + command.getActivator() + ")", LogType.INFO);
    }

    public CommandManager(AdvancedAnimator main) {
        this.main = main;

        /*
        Register tab completer
         */
    }

    @Override
    public boolean onCommand(CommandSender sender, Command com, String label, String[] args) {
        if (args.length >= 1) {
            for (AnimationCommand command : COMMAND_REGISTRY) {
                if (command.getActivator().equalsIgnoreCase(args[0])) {
                    if (!command.isPlayerOnly()) {
                        if (sender instanceof Player) {
                            LangEnum.CONSOLE_ONLY.send((Player) sender);
                            command.error((Player) sender);
                            break;
                        }
                        executeConsole(sender, args);
                        break;
                    } else {
                        command.execute(sender, args);
                        break;
                    }
                }
            }
        } else {
            // Send about message
            if (sender instanceof Player) {
                LangEnum.sendAbout((Player) sender);
            } else {
                LangEnum.sendAbout(sender);
            }
        }
        return true;
    }

    private void executeConsole(CommandSender sender, String[] args) {

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            Set<String> re = createReturnList(TAB_COMPLETER, args[0]);
            return new ArrayList<>(re);
        }
//        if (args.length == 2) {
//            if (args[0].equalsIgnoreCase("delete")) {
//                Set<String> re = createReturnList(main.getKitsManager().getKits().keySet(), args[1]);
//                if (re.isEmpty()) {
//                    return new ArrayList<>(main.getKitsManager().getKits().keySet());
//                } else {
//                    return new ArrayList<>(re);
//                }
//            }
//        }
        return null;
    }


    private Set<String> createReturnList(Set<String> list, String string) {
        if (string.equals("")) return list;

        Set<String> returnList = new HashSet<>();
        for (String item : list) {
            if (item.toLowerCase().startsWith(string.toLowerCase())) {
                returnList.add(item);
            }
        }
        return returnList;
    }
}
