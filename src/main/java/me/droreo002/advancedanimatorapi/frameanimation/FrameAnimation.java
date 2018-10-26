package me.droreo002.advancedanimatorapi.frameanimation;

import org.bukkit.inventory.ItemStack;

/**
 * This is the class for the FrameAnimation. The usage can be found at the class inside the 'objects' package
 */
public abstract class FrameAnimation {

    private long delay;
    private long time;
    private String name;
    private ItemStack inventoryDisplay;
    private Runnable runnable;

    public FrameAnimation(long delay, long time, String name, ItemStack inventoryDisplay, Runnable runnable) {
        this.delay = delay;
        this.time = time;
        this.name = name;
        this.inventoryDisplay = inventoryDisplay;
        this.runnable = runnable;
    }

    public FrameAnimation(long delay, long time, String name, ItemStack inventoryDisplay) {
        this.delay = delay;
        this.time = time;
        this.name = name;
        this.inventoryDisplay = inventoryDisplay;
    }

    public FrameAnimation(long delay, long time, String name) {
        this.delay = delay;
        this.time = time;
        this.name = name;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStack getInventoryDisplay() {
        return inventoryDisplay;
    }

    public void setInventoryDisplay(ItemStack inventoryDisplay) {
        this.inventoryDisplay = inventoryDisplay;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }
}
