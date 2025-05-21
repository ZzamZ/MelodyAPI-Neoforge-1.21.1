package net.zam.melodyapi.registry;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.zam.melodyapi.common.cases.CaseEntry;
import net.zam.melodyapi.common.cases.CaseRewards;
import net.zam.melodyapi.common.item.rarity.Rarity;
import net.zam.melodyapi.common.item.rarity.RarityItem;

import java.util.List;

public class MelodyCaseRewards {
    public static void init() {}

    public static final CaseEntry TEST_CASE = register(
        "test_case",
        List.of(
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
        )
    );

    public static final CaseEntry SINGLETON_CASE = register(
        "singleton_case",
        List.of(
            new RarityItem(new ItemStack(Items.WOODEN_AXE), Rarity.COMMON),
            new RarityItem(new ItemStack(Items.WOODEN_PICKAXE), Rarity.COMMON)
        )
    );

    public static CaseEntry register(String name, List<RarityItem> rewards) {
        return CaseRewards.register(name, rewards);
    }
}