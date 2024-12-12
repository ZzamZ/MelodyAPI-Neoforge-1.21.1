package net.zam.melodyapi.common.gui.cases;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.zam.melodyapi.MelodyAPI;
import net.zam.melodyapi.common.item.rarity.RarityItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BaseLootBoxRewardScreen extends Screen {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseLootBoxRewardScreen.class);

    private final ResourceLocation texture;
    private final List<RarityItem> rewardItems;
    private Button claimButton;
    private final Player player;
    private final Component title;
    private final int titleColor;
    private final Component caseTitle;
    private boolean rewardClaimed = false;

    public BaseLootBoxRewardScreen(List<RarityItem> rewardItems, Player player, Component caseTitle) {
        super(Component.literal("Reward"));
        this.texture = new ResourceLocation(MelodyAPI.MOD_ID, "textures/gui/case.png");
        this.rewardItems = rewardItems;
        this.player = player;
        RarityItem rewardItem = rewardItems.get(0);
        this.title = determineTitle(rewardItem);
        this.titleColor = rewardItem.getRarity().getColor();
        this.caseTitle = caseTitle;
    }

    private void updatePlayerCollectedItem(Player player, RarityItem item) {
        CompoundTag playerData = player.getPersistentData();
        if (!playerData.contains("receivedItems")) {
            playerData.put("receivedItems", new CompoundTag());
        }
        CompoundTag receivedItems = playerData.getCompound("receivedItems");
        ResourceLocation itemRegistryName = BuiltInRegistries.ITEM.getKey(item.getItemStack().getItem());
        receivedItems.putBoolean(itemRegistryName.toString(), true);
        playerData.put("receivedItems", receivedItems);
    }

    private Component determineTitle(RarityItem rarityItem) {
        ItemStack itemStack = rarityItem.getItemStack();
        if (isMusicDisc(itemStack)) {
            return Component.translatable(itemStack.getDescriptionId() + ".desc");
        } else {
            return itemStack.getHoverName();
        }
    }

    private boolean isMusicDisc(ItemStack itemStack) {
        ResourceLocation itemRegistryName = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
        return itemRegistryName != null && itemRegistryName.getPath().startsWith("music_disc_");
    }

    @Override
    protected void init() {
        super.init();
        int screenWidth = this.width;
        int screenHeight = this.height;
        int x = (screenWidth - 176) / 2;
        int y = (screenHeight - 70) / 2;

        this.claimButton = Button.builder(Component.literal("Claim"), button -> {
            sendClaimRewardPacket();
            announceReward();
            rewardClaimed = true;
        }).bounds(x + 38, y + 35, 100, 20).build();

        this.addRenderableWidget(this.claimButton);
    }

    private void sendClaimRewardPacket() {
        RarityItem selectedItem = rewardItems.get(0);
        updatePlayerCollectedItem(player, selectedItem);
        LOGGER.info("Player's received items saved: {}", player.getPersistentData().getCompound("receivedItems"));
        NetworkHandler.CHANNEL.sendToServer(new ClaimRewardPacket(selectedItem));
    }

    private void announceReward() {
        RarityItem selectedItem = rewardItems.get(0);
        ItemStack selectedItemStack = selectedItem.getItemStack();
        Component itemNameOrDescription = isMusicDisc(selectedItemStack)
                ? Component.translatable(selectedItemStack.getDescriptionId() + ".desc")
                : selectedItemStack.getHoverName();

        Component message = Component.literal("")
                .append(player.getName().copy().withStyle(style -> style.withColor(0x55FF55)))
                .append(Component.literal(" opened a ").withStyle(style -> style.withColor(0xADD8E6)))
                .append(Component.literal("[").withStyle(style -> style.withColor(0xADD8E6)))
                .append(caseTitle.copy().withStyle(style -> style.withColor(0x90EE90)))
                .append(Component.literal("] ").withStyle(style -> style.withColor(0xADD8E6)))
                .append(Component.literal("and received ").withStyle(style -> style.withColor(0xADD8E6)))
                .append(Component.literal("[").withStyle(style -> style.withColor(selectedItem.getRarity().getColor())))
                .append(itemNameOrDescription.copy().withStyle(style -> style.withColor(selectedItem.getRarity().getColor())))
                .append(Component.literal("]").withStyle(style -> style.withColor(selectedItem.getRarity().getColor())));

        player.sendSystemMessage(message);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        RenderSystem.setShaderTexture(0, texture);

        int x = (this.width - 176) / 2;
        int y = (this.height - 70) / 2;
        guiGraphics.blit(texture, x, y, 0, 0, 176, 61);
        drawCenteredString(guiGraphics, this.font, this.title.getString(), this.width / 2, y + 8, titleColor);

        for (int i = 0; i < rewardItems.size(); i++) {
            ItemStack itemStack = rewardItems.get(i).getItemStack();
            int itemX = (this.width - 16) / 2;
            int itemY = y + 17 + (i * 18);

            guiGraphics.renderItem(itemStack, itemX, itemY);
            guiGraphics.renderItemDecorations(this.font, itemStack, itemX, itemY);
        }

        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    private void drawCenteredString(GuiGraphics guiGraphics, Font font, String text, int centerX, int y, int color) {
        int width = font.width(text);
        guiGraphics.drawString(font, text, centerX - width / 2, y, color, false);
    }

    @Override
    public void onClose() {
        if (!rewardClaimed) return; // Prevent closing the screen unless reward is claimed
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
