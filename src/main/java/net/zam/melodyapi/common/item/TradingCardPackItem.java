package net.zam.melodyapi.common.item;

import java.util.List;
import java.util.Random;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.zam.melodyapi.common.util.CardSet;

public class TradingCardPackItem extends Item {
    private static final Random RANDOM = new Random();
    private static final float ULTRA_RARE_CHANCE = 0.01F;

    // Always final, but now we ensure it's non-null in the constructor
    private final CardSet cardSet;

    public TradingCardPackItem(Item.Properties properties, CardSet cardSet) {
        super(properties);

        // Fail fast if the modder passes in a null CardSet
        if (cardSet == null) {
            throw new IllegalArgumentException("TradingCardPackItem was passed a null CardSet!");
        }
        this.cardSet = cardSet;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            // Grant 3 common items
            for (int i = 0; i < 3; i++) {
                ItemStack commonStack = new ItemStack(this.getRandomCommon());
                player.addItem(commonStack);
            }

            // 5% chance to get Ultra Rare, otherwise Rare
            if (RANDOM.nextFloat() < ULTRA_RARE_CHANCE) {
                ItemStack ultraStack = new ItemStack(this.cardSet.getUltraRareCard());
                player.addItem(ultraStack);
            } else {
                ItemStack rareStack = new ItemStack(this.getRandomRare());
                player.addItem(rareStack);
            }

            // Consume 1 pack
            stack.shrink(1);

            // Play sound effect
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.BUNDLE_DROP_CONTENTS, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    private Item getRandomCommon() {
        // cardSet is guaranteed to be non-null now.
        List<Item> commons = this.cardSet.getCommonCards();
        return commons.get(RANDOM.nextInt(commons.size()));
    }

    private Item getRandomRare() {
        List<Item> rares = this.cardSet.getRareCards();
        return rares.get(RANDOM.nextInt(rares.size()));
    }
}
