package me.droreo002.advancedanimatorapi.command;

import me.droreo002.advancedanimator.AdvancedAnimator;
import me.droreo002.advancedanimator.config.ConfigManager;
import me.droreo002.advancedanimator.utils.CommonUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public abstract class AnimationCommand {

    private String activator;
    private boolean consoleOnly;
    private boolean usePermission;
    private String permission;

    public AnimationCommand(String activator) {
        this.activator = activator;
    }

    /**
     * Register the command
     *
     * @param addToTabCompleter : If this true then the activator will be added to the tab completer,
     *                          will not be added if false.
     */
    public void register(boolean addToTabCompleter) {
        CommandManager.addCommand(this);
        if (addToTabCompleter) {
            CommandManager.addTabComplete(activator);
        }
    }

    public void setActivator(String activator) {
        this.activator = activator;
    }

    public void setUsePermission(boolean usePermission) {
        this.usePermission = usePermission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getActivator() {
        return activator;
    }

    public boolean isUsePermission() {
        return usePermission;
    }

    public String getPermission() {
        return permission;
    }

    public boolean isConsoleOnly() {
        return consoleOnly;
    }

    public void setConsoleOnly(boolean consoleOnly) {
        this.consoleOnly = consoleOnly;
    }

    /**
     * The method to be called by the {@link CommandManager}
     *
     * @param sender : The command sender
     * @param args : The current command argument
     */
    public abstract void execute(CommandSender sender, String[] args);

    /**
     * Play the success sound that is specified on the config to the player
     *
     * @param player : The targeted player
     */
    public void success(Player player) {
        ConfigManager config = AdvancedAnimator.getInstance().getConfigManager();
        CommonUtils.convertFromString(config.getCommandSuccessSound()).play(player);
    }

    /**
     * Play the error sound that is specified on the config to the player
     *
     * @param player : The targeted player
     */
    public void error(Player player) {
        ConfigManager config = AdvancedAnimator.getInstance().getConfigManager();
        CommonUtils.convertFromString(config.getCommandFailureSound()).play(player);
    }
}
