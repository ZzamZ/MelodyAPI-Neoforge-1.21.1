package net.zam.melodyapi.common.gui.casetest;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.zam.melodyapi.MelodyAPI;
import net.zam.melodyapi.common.gui.cases.BaseLootBoxScreen;
import net.zam.melodyapi.registry.MelodyItems;

public class TestCaseScreen extends BaseLootBoxScreen<TestCaseMenu> {
    private static final ResourceLocation CUSTOM_TEXTURE = ResourceLocation.fromNamespaceAndPath(MelodyAPI.MOD_ID, "textures/gui/case.png");
    private static final ItemStack REQUIRED_KEY_ITEM = new ItemStack(MelodyItems.TEST_KEY.get());
    private static final ItemStack REQUIRED_CASE_ITEM = new ItemStack(MelodyItems.TEST_CASE.get());

    public TestCaseScreen(TestCaseMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, CUSTOM_TEXTURE, REQUIRED_KEY_ITEM, REQUIRED_CASE_ITEM);
    }
}
