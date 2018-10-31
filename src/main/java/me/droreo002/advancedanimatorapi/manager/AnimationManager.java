package me.droreo002.advancedanimatorapi.manager;

import me.droreo002.advancedanimator.AdvancedAnimator;
import me.droreo002.advancedanimator.database.AnimationData;
import me.droreo002.advancedanimator.enums.LangEnum;
import me.droreo002.advancedanimator.enums.LogType;
import me.droreo002.advancedanimator.objects.Animation;
import me.droreo002.advancedanimator.utils.CommonUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.ArmorStand;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the class to handle the Animation object. It contains a lot of useful method,
 * please contact the dev if you need help on x method!
 */
public final class AnimationManager {

    private static final Map<String, Animation> ANIMATION_REGISTRY = new HashMap<>();

    /**
     * Load an animation
     *
     * @param name : The animation name that will be used
     * @throws NullPointerException if there's no animation file with that name
     * @throws IllegalStateException if the animation already available inside the HashMap
     */
    public static void loadAnimation(final String name) {
        File file = new File(AdvancedAnimator.getInstance().getDataFolder(), "animations" + File.separator + name.toLowerCase() + ".yml");
        if (!file.exists()) {
            throw new NullPointerException("Cannot find the animation file inside the animations data folder!");
        }
        AnimationData data = AnimationData.getConfig(AdvancedAnimator.getInstance(), name.toLowerCase());
        if (ANIMATION_REGISTRY.containsKey(name.toLowerCase())) {
            throw new IllegalStateException("That animation already exists on the data!. Please remove it first!");
        }
        synchronized (ANIMATION_REGISTRY) {
            ANIMATION_REGISTRY.computeIfAbsent(name, k -> new Animation(name, CommonUtils.getFromConfig(data)));
        }
    }

    /**
     * Reload the animation on the hashmap. Useful if you made some changes
     *
     * @param name : The animation name that will be reloaded
     * @throws NullPointerException if there's no animation available
     */
    public static void reloadAnimation(String name) {
        name = name.toLowerCase();
        if (!ANIMATION_REGISTRY.containsKey(name)) {
            throw new NullPointerException("Cannot reload the animation " + name + ", because its not inside the ANIMATION_RESGISTRY class!");
        } else {
            ANIMATION_REGISTRY.remove(name);
            loadAnimation(name);
        }
    }

    /**
     * Get the animation from the hashmap with the key of name
     *
     * @param name : The name of the animation
     * @return : {@see Animation} if contains or null if not
     */
    public static Animation getAnimation(String name) {
        name = name.toLowerCase();
       return ANIMATION_REGISTRY.get(name);
    }

    /**
     * Remove the animation from the hashmap and the data
     *
     * @param name : The animation name
     * @param delete : If true then it will delete the data, if false them it will remove the entry from the HashMap {@see ANIMATION_REGISTRY}
     * @return true if succeeded, false otherwise
     */
    public static boolean removeAnimation(String name, boolean delete) {
        name = name.toLowerCase();
        File file = new File(AdvancedAnimator.getInstance().getDataFolder(), "animations" + File.separator + name + ".yml");
        if (delete) {
            // In here we'll just check for the file
            if (!file.exists()) {
                throw new NullPointerException("Cannot find that animation data!. (" + name + ")");
            }
            AnimationData.remove(name);
            ANIMATION_REGISTRY.remove(name);
            file.delete();
            return true;
        } else {
            Validate.notNull(ANIMATION_REGISTRY.get(name), "Cannot find (" + name + ") animation inside the ANIMATION_REGISTRY HashMap!");
            ANIMATION_REGISTRY.remove(name);
            AnimationData.remove(name);
            return true;
        }
    }

    /**
     * Create the animation
     *
     * @param name : The animation name that will be used as the KEY
     * @param armorStand : The {@link ArmorStand} that will be used on the animation
     */
    public static void createAnimation(String name, ArmorStand armorStand) {
        name = name.toLowerCase();
        AnimationData data = AnimationData.getConfig(AdvancedAnimator.getInstance(), name);
        data.setup(armorStand);
        loadAnimation(name);
    }

    /**
     * This is the method that will get called when the plugin start
     */
    public static void init() {
        File dir = new File(AdvancedAnimator.getInstance().getDataFolder(), "animations");
        if (dir.listFiles() == null) {
            LangEnum.sendMessageToConsole("There's no animation available. Cancelling animation initialization", LogType.WARNING);
            return;
        }
        long startTime = System.currentTimeMillis();
        System.out.println("=================== Animation Loader ===================");
        for (File f : dir.listFiles()) {
            String name = f.getName().replaceFirst("[.][^.]+$", "");
            loadAnimation(name);
        }
        System.out.println("=================== Animation Loader ===================");
        System.out.println(" ");
        long endTime = System.currentTimeMillis();
        LangEnum.sendMessageToConsole("Animation loader finished at " + (endTime - startTime) + " ms!", LogType.INFO);
    }
}
