package net.zam.melodyapi.common.gui.cases;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.zam.melodyapi.MelodyAPI;
import net.zam.melodyapi.common.item.rarity.Rarity;
import net.zam.melodyapi.common.item.rarity.RarityItem;

import java.util.List;

public class BaseLootBoxScreen<T extends BaseLootBoxMenu<T>> extends AbstractContainerScreen<T> {
    private final ResourceLocation texture;
    private final ItemStack requiredKeyItem;
    private final ItemStack requiredCaseItem;
    private Button openButton;
    private boolean showMessage;
    private Component message;


    public BaseLootBoxScreen(T menu, Inventory playerInventory, Component title, ResourceLocation texture, ItemStack requiredKeyItem, ItemStack requiredCaseItem) {
        super(menu, playerInventory, title);
        this.texture = texture;
        this.requiredKeyItem = requiredKeyItem;
        this.requiredCaseItem = requiredCaseItem;
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.showMessage = false;
        this.message = Component.empty();
    }

    @Override
    protected void init() {
        super.init();
        int buttonX = this.leftPos + (this.imageWidth / 2) - 50;
        int buttonY = this.topPos + 25;

        this.openButton = Button.builder(Component.literal("Open Case"), button -> {
            if (menu.consumeItems(minecraft.player, requiredKeyItem, requiredCaseItem)) {
                this.minecraft.setScreen(new BaseSpinScreen(menu.getLootItems(), this.title));
            } else {
                this.showMessage = true;
                this.message = Component.literal("You need a key open");
            }
        }).bounds(buttonX, buttonY, 100, 20).build();

        this.addRenderableWidget(this.openButton);
        populateSlotsWithItems();
    }

    private void populateSlotsWithItems() {
        List<RarityItem> items = this.menu.getLootItems();
        for (int i = 0; i < items.size() && i < this.menu.slots.size(); i++) {
            this.menu.slots.get(i).set(items.get(i).getItemStack());
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        this.renderBackground(guiGraphics);
        guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        renderRarityBorders(guiGraphics);
        drawCenteredString(guiGraphics, this.font, this.title.getString(), this.leftPos + this.imageWidth / 2, this.topPos + 10, 0xFFFFFF);

        if (showMessage) {
            displayMessage(guiGraphics, message);
        }

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }

    private void renderRarityBorders(GuiGraphics guiGraphics) {
        List<RarityItem> items = this.menu.getLootItems();
        for (int i = 0; i < items.size() && i < this.menu.slots.size(); i++) {
            int slotX = this.leftPos + this.menu.slots.get(i).x;
            int slotY = this.topPos + this.menu.slots.get(i).y;
            Rarity rarity = items.get(i).getRarity();
            int color = getRarityColor(rarity);
            guiGraphics.fill(slotX, slotY, slotX + 16, slotY + 16, color);

        }
    }

    private int getRarityColor(Rarity rarity) {
        switch (rarity) {
            case COMMON:
                return 0xFF3498DB;
            case UNCOMMON:
                return 0xFF8A2BE2;
            case RARE:
                return 0xFFFF69B4;
            case VERY_RARE:
                return 0xFFE74C3C;
            case ULTRA_RARE:
                return 0xFFFFD700;
            default:
                return 0xFFAAAAAA;
        }
    }

    private void drawCenteredString(GuiGraphics guiGraphics, Font font, String text, int centerX, int y, int color) {
        int width = font.width(text);
        guiGraphics.drawString(font, text, centerX - width / 2, y, color, false);
    }

    private void displayMessage(GuiGraphics guiGraphics, Component message) {
        int messageX = this.leftPos + (this.imageWidth / 2);
        int messageY = this.topPos + 47;
        drawCenteredString(guiGraphics, this.font, message.getString(), messageX, messageY, 0xFF0000);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
