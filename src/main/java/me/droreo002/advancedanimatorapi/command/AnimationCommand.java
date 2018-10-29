package me.droreo002.advancedanimatorapi.command;

import me.droreo002.advancedanimator.AdvancedAnimator;
import me.droreo002.advancedanimator.config.ConfigManager;
import me.droreo002.advancedanimator.utils.CommonUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public abstract class AnimationCommand {

    private String activator;
    private boolean playerOnly;
    private boolean usePermission;
    private String permission;

    public AnimationCommand(String activator) {
        this.activator = activator;
    }

    public void register() {
        CommandManager.addCommand(this);
    }

    public void setActivator(String activator) {
        this.activator = activator;
    }

    public void setPlayerOnly(boolean playerOnly) {
        this.playerOnly = playerOnly;
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

    public boolean isPlayerOnly() {
        return playerOnly;
    }

    public boolean isUsePermission() {
        return usePermission;
    }

    public String getPermission() {
        return permission;
    }

    public abstract void execute(CommandSender sender, String[] args);

    public void success(Player player) {
        ConfigManager config = AdvancedAnimator.getInstance().getConfigManager();
        CommonUtils.convertFromString(config.getCommandSuccessSound()).play(player);
    }

    public void error(Player player) {
        ConfigManager config = AdvancedAnimator.getInstance().getConfigManager();
        CommonUtils.convertFromString(config.getCommandFailureSound()).play(player);
    }
}
