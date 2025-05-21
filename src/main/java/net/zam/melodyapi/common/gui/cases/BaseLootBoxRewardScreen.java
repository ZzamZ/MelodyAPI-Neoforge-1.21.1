package net.zam.melodyapi.common.gui.cases;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.zam.melodyapi.MelodyAPI;
import net.zam.melodyapi.common.cases.CaseEntry;
import net.zam.melodyapi.common.cases.CaseRewards;
import net.zam.melodyapi.common.item.rarity.RarityItem;
import net.zam.melodyapi.common.network.ClaimRewardPacket;
import net.zam.melodyapi.common.util.EntityDataSaver;
import net.zam.melodyapi.common.util.TextUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class BaseLootBoxRewardScreen extends Screen {
    private static final Logger LOGGER = LogManager.getLogger();
    private final ResourceLocation texture;
    private final List<RarityItem> rewardItems;
    private Button claimButton;
    private final Player player;
    private final Component title;
    private final int titleColor;
    private final Component caseTitle;
    private final CaseEntry entry;
    private boolean rewardClaimed = false;

    public BaseLootBoxRewardScreen(List<RarityItem> rewardItems, Player player, Component caseTitle, CaseEntry entry) {
        super(Component.literal("Reward"));
        this.texture = MelodyAPI.id("textures/gui/spin_gui.png");
        this.rewardItems = rewardItems;
        this.player = player;
        RarityItem rewardItem = rewardItems.get(0);
        this.title = determineTitle(rewardItem);
        this.titleColor = rewardItem.getRarity().getColor();
        this.caseTitle = caseTitle;
        this.entry = entry;
    }

    private void updatePlayerCollectedItem(Player player, RarityItem item) {
        CompoundTag playerData = ((EntityDataSaver) player).melody$getPersistentData();

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
        if (itemStack.getItem().components().has(DataComponents.JUKEBOX_PLAYABLE)) {
            return Component.translatable(itemStack.getDescriptionId() + ".desc");
        } else {
            return itemStack.getHoverName();
        }
    }

    @Override
    protected void init() {
        super.init();
        int screenWidth = this.width;
        int screenHeight = this.height;
        int x = (screenWidth - 176) / 2;
        int y = (screenHeight - 70) / 2;
        int buttonX = x + 38;
        int buttonY = y + 55;

        this.claimButton = Button.builder(Component.literal("Claim"), button -> {
            sendClaimRewardPacket();
            rewardClaimed = true;
        }).bounds(buttonX, buttonY, 100, 20).build();

        this.addRenderableWidget(this.claimButton);
    }

    private void sendClaimRewardPacket() {
        RarityItem selectedItem = rewardItems.get(0);

        // Update the player's collected item data
        updatePlayerCollectedItem(player, selectedItem);

        CompoundTag playerData = ((EntityDataSaver) player).melody$getPersistentData();

        if (!playerData.contains("receivedItems")) {
            playerData.put("receivedItems", new CompoundTag());
        }

        CompoundTag receivedItems = playerData.getCompound("receivedItems");

        ResourceLocation itemRegistryName = BuiltInRegistries.ITEM.getKey(selectedItem.getItemStack().getItem());
        receivedItems.putBoolean(itemRegistryName.toString(), true);

        playerData.put("receivedItems", receivedItems);

        LOGGER.info("Player's received items saved: " + receivedItems);

        boolean isComplete = checkCaseCompletion(player);

        // Send the updated packet with player name and case title
        ClientPlayNetworking.send(new ClaimRewardPacket(selectedItem, this.player.getName().getString(), this.caseTitle, this.entry.id(), isComplete));
    }

    private boolean checkCaseCompletion(Player player) {
        // Get the saved data for this player
        CompoundTag playerData = ((EntityDataSaver) player).melody$getPersistentData();

        // If no data exists yet, player hasn't collected any items
        if (!playerData.contains("receivedItems")) {
            LOGGER.info("Player has no received items data");
            return false;
        }

        // Get case items - we need to look up what items belong to this case
        List<RarityItem> caseItems = CaseRewards.getPossibleRewardsById(this.entry.id());
        LOGGER.info("Found {} possible rewards for case {}", caseItems.size(), this.entry.id());

        if (caseItems.isEmpty()) {
            LOGGER.warn("No items found for case {}", this.entry.id());
            return false;
        }

        // Check if all items have been collected
        CompoundTag receivedItems = playerData.getCompound("receivedItems");
        LOGGER.info("Player has {} received items", receivedItems.getAllKeys().size());

        for (RarityItem item : caseItems) {
            ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item.getItemStack().getItem());
            if (!receivedItems.getBoolean(itemId.toString())) {
                LOGGER.info("Item {} not collected yet", itemId);
                return false; // At least one item hasn't been collected
            }
        }

        LOGGER.info("All {} items for case {} have been collected!", caseItems.size(), this.entry.id());
        return true; // All items have been collected
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        RenderSystem.setShaderTexture(0, texture);
        int screenWidth = this.width;
        int screenHeight = this.height;
        int x = (screenWidth - 176) / 2; // Adjust width for the top part
        int y = (screenHeight - 70) / 2; // Adjust height for the top part
        guiGraphics.blit(texture, x, y, 0, 0, 176, 79); // Only draw the top part of the texture (176x70)
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        int screenWidth = this.width;
        int screenHeight = this.height;
        int x = (screenWidth - 176) / 2;
        int y = (screenHeight - 70) / 2;
        TextUtils.drawCenteredVerticallyWrappedString(guiGraphics, this.font, this.title.getString(), this.width / 2, y + 18, 170, titleColor);

        for (int i = 0; i < rewardItems.size(); i++) {
            ItemStack itemStack = rewardItems.get(i).getItemStack();
            int itemX = (screenWidth - 16) / 2;
            int itemY = y + 17 + (i * 18) + 15;

            guiGraphics.renderItem(itemStack, itemX, itemY);
            guiGraphics.renderItemDecorations(this.font, itemStack, itemX, itemY);
        }
    }

    @Override
    public void onClose() {
        if (!rewardClaimed) {
            return; // Prevent closing the screen unless reward is claimed
        }
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}