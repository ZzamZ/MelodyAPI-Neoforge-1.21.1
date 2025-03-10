package net.zam.melodyapi.common.gui.cases;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.zam.melodyapi.MelodyAPI;
import net.zam.melodyapi.common.item.rarity.Rarity;
import net.zam.melodyapi.common.item.rarity.RarityItem;
import net.zam.melodyapi.common.util.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BaseSpinScreen extends Screen {
    private final ResourceLocation texture;
    private final List<RarityItem> items;
    private final List<RarityItem> displayedItems;
    private final int spinDuration;
    private final Random random;
    private int tickCounter;
    private float itemScrollPosition;
    private float speed;
    private boolean isSlowingDown;
    private long stopTime; // To track when the spinning stops
    private boolean rewardSelected;
    private final Component caseTitle; // Add a case title
    private Player player;
    private RarityItem selectedItem;

    public BaseSpinScreen(List<RarityItem> lootItems, Component caseTitle) {
        super(Component.literal("Spinning..."));
        this.texture = MelodyAPI.id("textures/gui/spin_gui.png");
        this.caseTitle = caseTitle;
        this.displayedItems = new ArrayList<>();
        this.spinDuration = 400 + new Random().nextInt(100);
        this.random = new Random();
        this.tickCounter = 0;
        this.itemScrollPosition = 0;
        this.speed = 15.0f; // Start with a higher speed
        this.isSlowingDown = false;
        this.rewardSelected = false;

        this.items = new ArrayList<>(lootItems);

        // Prepopulate the displayed items list to ensure it starts full
        while (displayedItems.size() < 30) { // Ensure the displayed items fill the entire area
            addRandomItemToDisplayedItems();
        }

        // Adjust initial position to start at the left bound
        itemScrollPosition = 0.0f;
    }

    @Override
    protected void init() {
        super.init();
        this.player = this.minecraft.player;
    }

    @Override
    public void tick() {
        tickCounter++;
        if (tickCounter >= 30 && !isSlowingDown) { // Start slowing down after 30 ticks (1.5 seconds)
            isSlowingDown = true;
        }
        if (isSlowingDown) {
            speed = Math.max(0.1f, speed * 0.95f); // Adjusted slow down rate for approximately 5 seconds duration
            if (speed <= 0.1f && !rewardSelected) {
                speed = 0;
                rewardSelected = true;
                stopTime = System.currentTimeMillis(); // Set the stop time when the spinning stops
                selectedItem = getSelectedReward(); // Ensure the reward is selected
            }
        }
        itemScrollPosition += speed;

        // Ensure the displayedItems list always has enough items to scroll through
        while (displayedItems.size() < itemScrollPosition / 18 + 50) {
            addRandomItemToDisplayedItems();
        }

        if (rewardSelected && System.currentTimeMillis() - stopTime >= 500) { // Check if half a second has passed
            this.minecraft.setScreen(new BaseLootBoxRewardScreen(List.of(selectedItem), this.minecraft.player, caseTitle));
        }
    }

    private RarityItem getSelectedReward() {
        int selectedIndex = (int) ((itemScrollPosition + 8) / 16) % displayedItems.size();
        if (selectedIndex < 0) selectedIndex += displayedItems.size(); // Ensure positive index
        return displayedItems.get(selectedIndex);
    }

    @Override
    public void onClose() {
        // Prevent closing the screen until claim button is clicked
    }

    private void addRandomItemToDisplayedItems() {
        int rarityChance = random.nextInt(10000); // 0 to 9999

        if (rarityChance < 8750) {
            // 87.5% chance for common (8750 out of 10000)
            displayedItems.add(getRandomItemByRarity(Rarity.COMMON));
        } else if (rarityChance < 9675) {
            // 9.25% chance for uncommon (925 out of 10000)
            displayedItems.add(getRandomItemByRarity(Rarity.UNCOMMON));
        } else if (rarityChance < 9850) {
            // 1.75% chance for rare (175 out of 10000)
            displayedItems.add(getRandomItemByRarity(Rarity.RARE));
        } else if (rarityChance < 9966) {
            // 1.16% chance for very rare (116 out of 10000)
            displayedItems.add(getRandomItemByRarity(Rarity.VERY_RARE));
        } else {
            // 0.34% chance for ultra rare (34 out of 10000)
            displayedItems.add(getRandomItemByRarity(Rarity.ULTRA_RARE));
        }
    }

    private RarityItem getRandomItemByRarity(Rarity rarity) {
        List<RarityItem> filteredItems = new ArrayList<>();
        for (RarityItem item : items) {
            if (item.getRarity() == rarity) {
                filteredItems.add(item);
            }
        }
        if (filteredItems.isEmpty()) {
            return new RarityItem(new ItemStack(Items.MUSIC_DISC_13), Rarity.COMMON);
        }
        return filteredItems.get(random.nextInt(filteredItems.size()));
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

        int x = (screenWidth - 176) / 2; // Adjust width for the top part
        int y = (screenHeight - 70) / 2; // Adjust height for the top part

        // Enable scissor test to clip the rendering area
        int scissorX = (int) ((double) (x + 3.5) / this.width * this.minecraft.getWindow().getScreenWidth()); // Adjusted left boundary
        int scissorY = (int) ((double) (this.height - (y + 23 + 18)) / this.height * this.minecraft.getWindow().getScreenHeight());
        int scissorWidth = (int) ((double) 169.75 / this.width * this.minecraft.getWindow().getScreenWidth()); // Adjusted right boundary
        int scissorHeight = (int) ((double) 18 / this.height * this.minecraft.getWindow().getScreenHeight());
        RenderSystem.enableScissor(scissorX, scissorY, scissorWidth, scissorHeight);

        renderScrollingItems(guiGraphics, x, y + 23); // Adjusted y position for scrolling items

        RenderSystem.disableScissor();

        int selectedIndex = (int) ((itemScrollPosition + 8) / 16) % displayedItems.size();
        if (selectedIndex < 0) selectedIndex += displayedItems.size(); // Ensure positive index
        RarityItem selectedItem = displayedItems.get(selectedIndex);

        ItemStack selectedItemStack = selectedItem.getItemStack();
        Component displayNameOrDescription = selectedItemStack.getItem().components().has(DataComponents.JUKEBOX_PLAYABLE) ?
                Component.translatable(selectedItemStack.getDescriptionId() + ".desc") :
                selectedItemStack.getHoverName();

        TextUtils.drawCenteredWrappedString(guiGraphics, this.font, displayNameOrDescription.getString(), this.width / 2, y + 45, 170, getRarityColor(selectedItem.getRarity()));

        // Draw the case title above the spinning items (after super.render to ensure it's on top)
        drawCenteredString(guiGraphics, this.font, caseTitle.getString(), this.width / 2, y - 5 + 14, 0xFFFFFF); // Moved the title down
    }

    private void drawCenteredString(GuiGraphics guiGraphics, Font font, String text, int centerX, int y, int color) {
        int width = font.width(text);
        guiGraphics.drawString(font, text, centerX - width / 2, y, color, false);
    }

    private void renderScrollingItems(GuiGraphics guiGraphics, int x, int y) {
        int itemSize = 16; // Size of each item slot
        int totalItems = displayedItems.size();
        int guiLeftBound = x + 5; // Adjusted left boundary
        int guiRightBound = x + 173; // Adjusted right boundary

        // Calculate the starting x position for the center item
        int startX = x + (176 / 2) - (itemSize / 2);
        for (int i = 0; i < totalItems; i++) {
            float itemX = startX + ((i - (itemScrollPosition / itemSize)) * itemSize);
            if (itemX >= guiLeftBound - itemSize && itemX <= guiRightBound) { // Ensure items stay within bounds
                RarityItem rarityItem = displayedItems.get(i % totalItems);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f); // Remove fade effect
                guiGraphics.fill((int) itemX - 1, y - 1, (int) itemX + 17, y + 17, getRarityColor(rarityItem.getRarity())); // Draw border
                guiGraphics.renderItem(rarityItem.getItemStack(), (int) itemX, y);
                guiGraphics.renderItemDecorations(this.font, rarityItem.getItemStack(), (int) itemX, y);
                RenderSystem.disableBlend();
            }
        }

        guiGraphics.fill(startX + itemSize / 2, y - 1, startX + itemSize / 2 + 1, y + 17, 0xFF555555); // Dark grey line
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

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}