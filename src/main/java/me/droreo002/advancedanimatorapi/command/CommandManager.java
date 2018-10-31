package me.droreo002.advancedanimatorapi.command;

import me.droreo002.advancedanimator.AdvancedAnimator;
import me.droreo002.advancedanimator.enums.LangEnum;
import me.droreo002.advancedanimator.enums.LogType;
import me.droreo002.advancedanimator.objects.Animation;
import me.droreo002.advancedanimator.utils.CommonUtils;
import org.bukkit.Sound;
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
     * @throws IllegalStateException : If the command already inside the registry list
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

    /**
     * Add that string to the tab completer!
     *
     * @param tab : The tab completer (String)
     * @throws IllegalStateException : If the tab complete already contains inside the registry
     */
    public static void addTabComplete(String tab) {
        for (String s : TAB_COMPLETER) {
            if (s.equalsIgnoreCase(tab)) {
                throw new IllegalStateException("Tab complete with the activator of (" + tab.toLowerCase() + "). Already contains in the tab completer!");
            }
        }
        // Loop first, then add
        TAB_COMPLETER.add(tab.toLowerCase());
        LangEnum.sendMessageToConsole("Successfully registered a tab completer. Activator is (" + tab.toLowerCase() + ")", LogType.INFO);
    }

    /**
     * Unregister the command from the registry
     *
     * @param activator : The command's activator. We don't want to use object here
     * @throws NullPointerException : If the activator cannot be found!
     *
     * @return : True if the unregistering is successful, false otherwise
     */
    public static boolean unregisterCommand(String activator) {
        for (AnimationCommand command : COMMAND_REGISTRY) {
            if (command.getActivator().equalsIgnoreCase(activator)) {
                COMMAND_REGISTRY.remove(command);
                return true;
            }
        }
        throw new NullPointerException("Error on unregistering command with the activator of (" + activator + "). System cannot find the activator!");
    }

    /**
     * Unregister that tab completer activator from the tab completer
     *
     * @param tab : The tab completer ID
     * @throws NullPointerException : If the ID/Activator cannot be found!
     *
     * @return : true if unregistering is successful, false otherwise
     */
    public static boolean unregisterTabCompleter(String tab) {
        for (String s : TAB_COMPLETER) {
            if (s.equalsIgnoreCase(tab)) {
                TAB_COMPLETER.remove(tab);
                return true;
            }
        }
        throw new NullPointerException("Error on unregistering tab completer with the activator of (" + tab + "). System cannot find the activator!");
    }

    public CommandManager(AdvancedAnimator main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command com, String label, String[] args) {
        if (args.length >= 1) {
            AnimationCommand sample = null;
            int count = 0;
            // Just to get the sound k?
            for (AnimationCommand command : COMMAND_REGISTRY) {
                sample = command;
                if (command.getActivator().equalsIgnoreCase(args[0])) {
                    count++;
                    if (command.isConsoleOnly()) {
                        if (sender instanceof Player) {
                            LangEnum.CONSOLE_ONLY.send((Player) sender);
                            command.error((Player) sender);
                            break;
                        }
                        executeConsole(sender, args);
                        break;
                    } else {
                        if (!(sender instanceof Player)) {
                            LangEnum.PLAYER_ONLY.send(sender);
                            break;
                        }
                        Player player = (Player) sender;
                        if (command.isUsePermission()) {
                            if (!player.hasPermission(command.getPermission())) {
                                LangEnum.NO_PERMISSION.send(player);
                                command.error(player);
                                break;
                            }
                        }
                        command.execute(sender, args);
                        break;
                    }
                }
            }

            // That mean there's no command found with that activator / argument
            if (count == 0) {
                if (sender instanceof Player) {
                    LangEnum.COMMAND_NOT_FOUND.send((Player) sender);
                    if (sample != null) {
                        sample.error((Player) sender);
                    }
                } else {
                    LangEnum.COMMAND_NOT_FOUND.send(sender);
                }
            }
        } else {
            // Send about message
            if (sender instanceof Player) {
                LangEnum.sendAbout((Player) sender);
            } else {
                LangEnum.onEnableLog();
            }
        }
        return true;
    }

    /**
     * Execute the command as a console
     *
     * @param sender : The command sender (CONSOLE)
     * @param args : Te current argument on the command
     */
    private void executeConsole(CommandSender sender, String[] args) {

    }

    /**
     * The listener for the TabCompleter
     *
     * @param sender : Command sender
     * @param command : The command object that is executed
     * @param alias : The alias of the command
     * @param args : The current argument that the command has
     *
     * @return List of string that will be used for the tab completer
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            CommonUtils.convertFromString(main.getConfigManager().getTabCompleteSound()).play(player);
        }
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

    /**
     * Create a return list with the specified list.
     *
     * @param list : The list that you want to add
     * @param string : The latest argument on the command
     *
     * @return a set of string where everything has been built inside
     */
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
