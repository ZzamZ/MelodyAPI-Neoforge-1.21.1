package net.zam.melodyapi.common.gui.casetest;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.zam.melodyapi.MelodyAPI;
import net.zam.melodyapi.common.gui.cases.BaseLootBoxScreen;
import net.zam.melodyapi.registry.MelodyCaseRewards;
import net.zam.melodyapi.registry.MelodyItems;

public class SingletonCaseScreen extends BaseLootBoxScreen<SingletonCaseMenu> {
    private static final ResourceLocation CUSTOM_TEXTURE = ResourceLocation.fromNamespaceAndPath(MelodyAPI.MOD_ID, "textures/gui/case.png");
    private static final ItemStack REQUIRED_KEY_ITEM = new ItemStack(MelodyItems.TEST_KEY);
    private static final ItemStack REQUIRED_CASE_ITEM = new ItemStack(MelodyItems.SINGLETON_CASE);

    public SingletonCaseScreen(SingletonCaseMenu menu, Inventory playerInventory, Component title) {
        super(menu, MelodyCaseRewards.SINGLETON_CASE, playerInventory, title, CUSTOM_TEXTURE, REQUIRED_KEY_ITEM, REQUIRED_CASE_ITEM);
    }
}