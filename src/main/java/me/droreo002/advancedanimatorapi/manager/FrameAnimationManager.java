package me.droreo002.advancedanimatorapi.manager;

import me.droreo002.advancedanimator.AdvancedAnimator;
import me.droreo002.advancedanimatorapi.frameanimation.FrameAnimation;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * This is the class to handle the FrameAnimation object (An particle / item animation that
 * can be ran inside a frame). It contains a lot of good method
 * if you're confuse on that x method do. Please contact the developer!
 */
public final class FrameAnimationManager {

    private static final Map<UUID, Integer> RUNNABLES_ID = new HashMap<>();
    private static final Set<FrameAnimation> FRAME_ANIMATION = new HashSet<>();

    /**
     * Remove the animation from the list of FrameAnimation (Using object)
     *
     * @param animation : The animation that will be removed
     * @return {@true} if it removed successfully
     * @throws NullPointerException if the animation cannot be found
     */
    public static void removeAnimation(FrameAnimation animation) {
        FRAME_ANIMATION.remove(animation);
    }

    /**
     * Register an animation and put it on the hashmap
     * put if absent here will return null if it already contained on the hashmap
     * it return the object it self if it didn't contain on the hashmap
     *
     * @param animation : The frame animation class
     * @return true if it registered successfully. False otherwise
     */
    public static boolean registerAnimation(FrameAnimation animation) {
        ItemStack display = animation.getInventoryDisplay();
        for (FrameAnimation registered : FRAME_ANIMATION) {
            if (registered.getInventoryDisplay().isSimilar(display)) {
                throw new IllegalArgumentException("That item already taken by other frame animation class!. Class name " + registered.getClass());
            }
            if (registered.getName().equalsIgnoreCase(animation.getName())) {
                throw new IllegalArgumentException("That name already taken as an animation by other class!. Class name " + registered.getName());
            }
        }

        Class<?> an = animation.getClass();
        if (!an.getPackage().getName().contains("me.droreo002.advancedanimatorapi.frameanimation.objects")) {
            System.out.println("Successfully registerd an external FrameAnimation. Class : ( " + an.getName() + " )");
        } else {
            System.out.println("Successfully registered an internal FrameAnimation. Class : ( " + an.getName() + " )");
        }
        FRAME_ANIMATION.add(animation);
        return true;
    }

    /**
     * Get the Frame animation
     *
     * @param name : The name of the animation that will be pulled from the list
     * @return {@link FrameAnimation} if its contains, null otherwise
     */
    public static FrameAnimation getAnimation(String name) {
        for (FrameAnimation anim : FRAME_ANIMATION) {
            if (anim.getName().equals(name)) {
                return anim;
            }
        }
        return null;
    }

    /**
     * This is the method that will get called when the plugin start
     */
    public static void init() {

    }

    /**
     * Start the animation for the x player. Will throw an error if the animation cannot be find!
     *
     * @param name : The animation name
     * @param player : The player target
     */
    public static void startAnimation(String name, Player player) {
        FrameAnimation anim = getAnimation(name);
        Validate.notNull(anim, "Animaton is null!");
        if (RUNNABLES_ID.containsKey(player.getUniqueId())) {
            Bukkit.getScheduler().cancelTask(RUNNABLES_ID.get(player.getUniqueId()));
        }
        RUNNABLES_ID.put(player.getUniqueId(), Bukkit.getScheduler().scheduleSyncRepeatingTask(AdvancedAnimator.getInstance(), anim.getRunnable(), anim.getDelay(), anim.getTime()));
    }

    /**
     * Stop the animation that is running
     *
     * @param player : The target player that has an animation ran on
     * @throws NullPointerException if there's no runnable running on that player
     */
    public static void stopAnimation(Player player) {
        if (!RUNNABLES_ID.containsKey(player.getUniqueId())) {
            throw new NullPointerException("There's no animation runnable started by that player!");
        }
        Bukkit.getScheduler().cancelTask(RUNNABLES_ID.get(player.getUniqueId()));
        RUNNABLES_ID.remove(player.getUniqueId());
    }
}
