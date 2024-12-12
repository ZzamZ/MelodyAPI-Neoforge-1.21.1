package net.zam.melodyapi.common.item.rarity;

import net.minecraft.world.item.ItemStack;
import net.zam.melodyapi.common.item.rarity.Rarity;

/**
 * Represents an item with an associated rarity.
 */
public class RarityItem {
    private final ItemStack itemStack;
    private final Rarity rarity;

    /**
     * Constructs a new RarityItem.
     *
     * @param itemStack The item stack to associate with this rarity item.
     * @param rarity    The rarity of the item.
     */
    public RarityItem(ItemStack itemStack, Rarity rarity) {
        // Ensure immutability of the ItemStack
        this.itemStack = itemStack.copy();
        this.rarity = rarity;
    }

    /**
     * Gets the ItemStack associated with this rarity item.
     *
     * @return A copy of the ItemStack to maintain immutability.
     */
    public ItemStack getItemStack() {
        return itemStack.copy();
    }

    /**
     * Gets the rarity of the item.
     *
     * @return The item's rarity.
     */
    public Rarity getRarity() {
        return rarity;
    }

    @Override
    public String toString() {
        return "RarityItem{" +
                "itemStack=" + itemStack +
                ", rarity=" + rarity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RarityItem that = (RarityItem) o;

        if (!itemStack.equals(that.itemStack)) return false;
        return rarity == that.rarity;
    }

    @Override
    public int hashCode() {
        int result = itemStack.hashCode();
        result = 31 * result + rarity.hashCode();
        return result;
    }
}
