package net.zam.melodyapi.common.gui.cases;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.zam.melodyapi.MelodyAPI;
import net.zam.melodyapi.common.item.rarity.Rarity;
import net.zam.melodyapi.common.item.rarity.RarityItem;

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
    private long stopTime;
    private boolean rewardSelected;
    private final Component caseTitle;
    private Player player;
    private RarityItem selectedItem;

    public BaseSpinScreen(List<RarityItem> lootItems, Component caseTitle) {
        super(Component.literal("Spinning..."));
        this.texture = new ResourceLocation(MelodyAPI.MOD_ID, "textures/gui/spin_gui.png");
        this.caseTitle = caseTitle;
        this.displayedItems = new ArrayList<>();
        this.spinDuration = 400 + new Random().nextInt(100);
        this.random = new Random();
        this.tickCounter = 0;
        this.itemScrollPosition = 0;
        this.speed = 15.0f;
        this.isSlowingDown = false;
        this.rewardSelected = false;

        this.items = new ArrayList<>(lootItems);

        while (displayedItems.size() < 30) {
            addRandomItemToDisplayedItems();
        }

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
        if (tickCounter >= 30 && !isSlowingDown) {
            isSlowingDown = true;
        }
        if (isSlowingDown) {
            speed = Math.max(0.1f, speed * 0.95f);
            if (speed <= 0.1f && !rewardSelected) {
                speed = 0;
                rewardSelected = true;
                stopTime = System.currentTimeMillis();
                selectedItem = getSelectedReward();
            }
        }
        itemScrollPosition += speed;

        while (displayedItems.size() < itemScrollPosition / 18 + 50) {
            addRandomItemToDisplayedItems();
        }

        if (rewardSelected && System.currentTimeMillis() - stopTime >= 500) {
            this.minecraft.setScreen(new BaseLootBoxRewardScreen(List.of(selectedItem), this.minecraft.player, caseTitle));
        }
    }

    private RarityItem getSelectedReward() {
        int selectedIndex = (int) ((itemScrollPosition + 8) / 16) % displayedItems.size();
        if (selectedIndex < 0) selectedIndex += displayedItems.size();
        return displayedItems.get(selectedIndex);
    }

    private void addRandomItemToDisplayedItems() {
        int rarityChance = random.nextInt(10000);

        if (rarityChance < 8750) {
            displayedItems.add(getRandomItemByRarity(Rarity.COMMON));
        } else if (rarityChance < 9675) {
            displayedItems.add(getRandomItemByRarity(Rarity.UNCOMMON));
        } else if (rarityChance < 9850) {
            displayedItems.add(getRandomItemByRarity(Rarity.RARE));
        } else if (rarityChance < 9966) {
            displayedItems.add(getRandomItemByRarity(Rarity.VERY_RARE));
        } else {
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
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        RenderSystem.setShaderTexture(0, texture);

        int screenWidth = this.width;
        int screenHeight = this.height;

        int x = (screenWidth - 176) / 2;
        int y = (screenHeight - 70) / 2;

        guiGraphics.blit(texture, x, y, 0, 0, 176, 79);

        int scissorX = (int) ((double) (x + 5) / this.width * this.minecraft.getWindow().getScreenWidth());
        int scissorY = (int) ((double) (this.height - (y + 23 + 18)) / this.height * this.minecraft.getWindow().getScreenHeight());
        int scissorWidth = (int) ((double) 170 / this.width * this.minecraft.getWindow().getScreenWidth());
        int scissorHeight = (int) ((double) 18 / this.height * this.minecraft.getWindow().getScreenHeight());
        RenderSystem.enableScissor(scissorX, scissorY, scissorWidth, scissorHeight);

        renderScrollingItems(guiGraphics, x, y + 23);

        RenderSystem.disableScissor();

        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        drawCenteredString(guiGraphics, this.font, caseTitle.getString(), this.width / 2, y - 10, 0xFFFFFF);
    }

    private void renderScrollingItems(GuiGraphics guiGraphics, int x, int y) {
        int itemSize = 16;
        int totalItems = displayedItems.size();
        int guiLeftBound = x + 5;
        int guiRightBound = x + 173;

        int startX = x + (176 / 2) - (itemSize / 2);
        for (int i = 0; i < totalItems; i++) {
            float itemX = startX + ((i - (itemScrollPosition / itemSize)) * itemSize);
            if (itemX >= guiLeftBound - itemSize && itemX <= guiRightBound) {
                RarityItem rarityItem = displayedItems.get(i % totalItems);
                guiGraphics.fill((int) itemX - 1, y - 1, (int) itemX + 17, y + 17, getRarityColor(rarityItem.getRarity()));
                guiGraphics.renderItem(rarityItem.getItemStack(), (int) itemX, y);
                guiGraphics.renderItemDecorations(this.font, rarityItem.getItemStack(), (int) itemX, y);
            }
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

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
