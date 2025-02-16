package net.zam.melodyapi.common.gui.cases;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zam.melodyapi.common.item.rarity.RarityItem;
import net.zam.melodyapi.common.network.ConsumeLootBoxItemsPacket;

import java.util.List;

public abstract class BaseLootBoxMenu<T extends BaseLootBoxMenu<T>> extends AbstractContainerMenu {
    private final ReadOnlyInventory lootInventory;
    private final List<RarityItem> lootItems;

    protected BaseLootBoxMenu(MenuType<T> menuType, int id, Inventory playerInventory, List<RarityItem> lootItems) {
        super(menuType, id);
        this.lootItems = lootItems;

        this.lootInventory = new ReadOnlyInventory(this.lootItems.size());
        for (int i = 0; i < this.lootItems.size(); i++) {
            this.lootInventory.setItem(i, this.lootItems.get(i).getItemStack());
        }

        // Add slots for displaying loot items
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new ReadOnlySlot(lootInventory, row * 9 + col, 8 + col * 18, 70 + row * 18));
            }
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public List<RarityItem> getLootItems() {
        return lootItems;
    }

    public boolean consumeItems(Player player, ItemStack keyItem, ItemStack caseItem) {
        Inventory inventory = player.getInventory();
        boolean keyConsumed = false;
        boolean caseConsumed = false;

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!keyConsumed && ItemStack.isSameItem(stack, keyItem) && stack.getCount() >= keyItem.getCount()) {
                stack.shrink(keyItem.getCount());
                keyConsumed = true;
                break;
            }
        }

        if (keyConsumed) {
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                ItemStack stack = inventory.getItem(i);
                if (!caseConsumed && ItemStack.isSameItem(stack, caseItem) && stack.getCount() >= caseItem.getCount()) {
                    stack.shrink(caseItem.getCount());
                    caseConsumed = true;
                    break;
                }
            }
        }

        if (keyConsumed && caseConsumed) {
            if (player.level().isClientSide()) {
                PacketDistributor.sendToServer(new ConsumeLootBoxItemsPacket(keyItem, caseItem));
            }
            return true;
        }

        return false;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        return ItemStack.EMPTY;
    }

    // ReadOnlySlot class to prevent interaction
    private static class ReadOnlySlot extends Slot {
        public ReadOnlySlot(Container inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public boolean mayPickup(Player playerIn) {
            return false;
        }
    }
}
