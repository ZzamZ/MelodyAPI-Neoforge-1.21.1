package net.zam.melodyapi.common.gui.cases;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * A read-only inventory implementation. Modifications to this inventory are not allowed.
 */
public class ReadOnlyInventory implements Container {
    private final ItemStack[] items;

    public ReadOnlyInventory(int size) {
        this.items = new ItemStack[size];
        for (int i = 0; i < size; i++) {
            this.items[i] = ItemStack.EMPTY;
        }
    }

    @Override
    public int getContainerSize() {
        return this.items.length;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : this.items) {
            if (!itemStack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return isValidIndex(index) ? this.items[index] : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        // Prevent modification
        throw new UnsupportedOperationException("This inventory is read-only.");
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        // Prevent modification
        throw new UnsupportedOperationException("This inventory is read-only.");
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        // Prevent modification
        throw new UnsupportedOperationException("This inventory is read-only.");
    }

    @Override
    public void setChanged() {
        // No changes are allowed, so this is effectively a no-op.
    }

    @Override
    public boolean stillValid(Player player) {
        return true; // Always valid for read-only inventories.
    }

    @Override
    public void clearContent() {
        // Prevent modification
        throw new UnsupportedOperationException("This inventory is read-only.");
    }

    @Override
    public int getMaxStackSize() {
        return 64; // Standard stack size.
    }

    /**
     * Validates if the given index is within the inventory bounds.
     *
     * @param index The slot index to validate.
     * @return True if the index is valid, false otherwise.
     */
    private boolean isValidIndex(int index) {
        return index >= 0 && index < this.items.length;
    }
}
