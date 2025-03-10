package net.zam.melodyapi.common.gui.casetest;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.zam.melodyapi.common.gui.cases.BaseLootBoxMenu;
import net.zam.melodyapi.common.item.rarity.Rarity;
import net.zam.melodyapi.common.item.rarity.RarityItem;
import net.zam.melodyapi.registry.MelodyMenuTypes;

import java.util.List;

public class TestCaseMenu extends BaseLootBoxMenu<TestCaseMenu> {
    public static final MenuType<TestCaseMenu> TEST_CASE_MENU = MelodyMenuTypes.TEST_CASE.get();

    public TestCaseMenu(int id, Inventory inv, Player player) {
        super(TEST_CASE_MENU, id, inv, getCustomLootItems(), player);
    }

    public TestCaseMenu(int id, Inventory inventory, RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this(id, inventory, (Player) null);
    }


    private static List<RarityItem> getCustomLootItems() {
        return List.of(
                new RarityItem(new ItemStack(Items.WOODEN_AXE), Rarity.COMMON),
                new RarityItem(new ItemStack(Items.WOODEN_PICKAXE), Rarity.COMMON),
                new RarityItem(new ItemStack(Items.WOODEN_SHOVEL), Rarity.COMMON),
                new RarityItem(new ItemStack(Items.IRON_AXE), Rarity.UNCOMMON),
                new RarityItem(new ItemStack(Items.IRON_PICKAXE), Rarity.UNCOMMON),
                new RarityItem(new ItemStack(Items.IRON_SHOVEL), Rarity.UNCOMMON),
                new RarityItem(new ItemStack(Items.DIAMOND_AXE), Rarity.RARE),
                new RarityItem(new ItemStack(Items.DIAMOND_PICKAXE), Rarity.RARE),
                new RarityItem(new ItemStack(Items.DIAMOND_SHOVEL), Rarity.RARE),
                new RarityItem(new ItemStack(Items.NETHERITE_AXE), Rarity.VERY_RARE),
                new RarityItem(new ItemStack(Items.NETHERITE_SHOVEL), Rarity.VERY_RARE),
                new RarityItem(new ItemStack(Items.NETHERITE_PICKAXE), Rarity.ULTRA_RARE),
                new RarityItem(new ItemStack(Items.DISC_FRAGMENT_5), Rarity.COMMON),
                new RarityItem(new ItemStack(Items.MUSIC_DISC_11), Rarity.COMMON),
                new RarityItem(new ItemStack(Items.MUSIC_DISC_MELLOHI), Rarity.COMMON),
                new RarityItem(new ItemStack(Items.MUSIC_DISC_CREATOR_MUSIC_BOX), Rarity.COMMON),
                new RarityItem(new ItemStack(Items.CRACKED_POLISHED_BLACKSTONE_BRICKS), Rarity.COMMON)
        );
    }
}
