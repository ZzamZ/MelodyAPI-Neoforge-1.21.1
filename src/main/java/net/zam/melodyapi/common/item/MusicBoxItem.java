package net.zam.melodyapi.common.item;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.zam.melodyapi.common.component.PausedComponent;
import net.zam.melodyapi.common.component.PlayingRecordComponent;
import net.zam.melodyapi.common.util.musicbox.PlayableRecord;
import net.zam.melodyapi.common.util.musicbox.SoundTracker;
import net.zam.melodyapi.registry.MelodyComponents;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class MusicBoxItem extends Item {

    private static final Map<Integer, ItemStack> PLAYING_RECORDS = new Int2ObjectArrayMap<>();

    public MusicBoxItem(Properties properties) {
        super(properties);
    }

    /**
     * Called each client tick for living entities (via mixin or event).
     */
    public static void onLivingEntityUpdateClient(LivingEntity entity) {
        if (!(entity instanceof Player player)) {
            return;
        }

        ItemStack newPlayingRecord = ItemStack.EMPTY;

        // 1) Check carried stack
        ItemStack carried = player.inventoryMenu.getCarried();
        if (hasRecord(carried) && !carried.has(MelodyComponents.PAUSED)) {
            newPlayingRecord = getRecord(carried);
        }

        // 2) Check each hand
        if (newPlayingRecord.isEmpty()) {
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack handStack = player.getItemInHand(hand);
                if (hasRecord(handStack) && !handStack.has(MelodyComponents.PAUSED)) {
                    newPlayingRecord = getRecord(handStack);
                    break;
                }
            }
        }

        // 3) Check main inventory
        if (newPlayingRecord.isEmpty()) {
            for (ItemStack invStack : player.getInventory().items) {
                if (hasRecord(invStack) && !invStack.has(MelodyComponents.PAUSED)) {
                    newPlayingRecord = getRecord(invStack);
                    break;
                }
            }
        }

        updatePlaying(entity, newPlayingRecord);
    }

    /**
     * Switch from old record to new record, then start/stop music.
     */
    private static void updatePlaying(Entity entity, ItemStack newRecord) {
        ItemStack oldRecord = PLAYING_RECORDS.getOrDefault(entity.getId(), ItemStack.EMPTY);
        if (ItemStack.matches(oldRecord, newRecord)) {
            return;
        }

        SoundTracker.playMusicBox(entity.getId(), newRecord);
        if (newRecord.isEmpty()) {
            PLAYING_RECORDS.remove(entity.getId());
        } else {
            PLAYING_RECORDS.put(entity.getId(), newRecord);
        }
    }

    /**
     * If item is on the ground as an ItemEntity, update playing if unpaused.
     */
    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (!entity.level().isClientSide()) {
            return false;
        }
        updatePlaying(entity, !stack.has(MelodyComponents.PAUSED) ? getRecord(stack) : ItemStack.EMPTY);
        return false;
    }

    /**
     * Right-click:
     * - If sneaking => toggle paused
     * - Otherwise => do nothing (no GUI)
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isSecondaryUseActive()) {
            if (stack.has(MelodyComponents.PAUSED)) {
                stack.remove(MelodyComponents.PAUSED);
            } else {
                stack.set(MelodyComponents.PAUSED, PausedComponent.INSTANCE);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }
        return InteractionResultHolder.pass(stack);
    }

    /**
     * "Bundle style" SHIFT-click insertion in GUIs/inventories.
     */
    @Override
    public boolean overrideStackedOnOther(ItemStack boombox, Slot slot, ClickAction clickAction, Player player) {
        if (clickAction != ClickAction.SECONDARY) {
            return false;
        }

        ItemStack clickItem = slot.getItem();
        if (clickItem.isEmpty()) {
            ItemStack record = getRecord(boombox);
            if (!record.isEmpty()) {
                player.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F,
                        0.8F + player.level().getRandom().nextFloat() * 0.4F);
                setRecord(boombox, slot.safeInsert(record));
                return true;
            }
        } else if (PlayableRecord.isPlayableRecord(clickItem)) {
            player.playSound(SoundEvents.BUNDLE_INSERT, 0.8F,
                    0.8F + player.level().getRandom().nextFloat() * 0.4F);
            ItemStack old = getRecord(boombox);
            setRecord(boombox, slot.safeTake(clickItem.getCount(), 1, player).split(1));
            slot.set(old);
            return true;
        }

        return false;
    }

    /**
     * "Bundle style" SHIFT-click from boombox onto another slot.
     */
    @Override
    public boolean overrideOtherStackedOnMe(ItemStack boombox, ItemStack clickItem,
                                            Slot slot, ClickAction clickAction,
                                            Player player, SlotAccess slotAccess) {
        if (clickAction != ClickAction.SECONDARY) {
            return false;
        }
        if (!slot.allowModification(player)) {
            return false;
        }

        if (clickItem.isEmpty()) {
            ItemStack record = getRecord(boombox);
            if (!record.isEmpty()) {
                player.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F,
                        0.8F + player.level().getRandom().nextFloat() * 0.4F);
                slotAccess.set(record);
                setRecord(boombox, ItemStack.EMPTY);
                return true;
            }
        } else if (PlayableRecord.isPlayableRecord(clickItem)) {
            ItemStack old = getRecord(boombox);
            if (old.isEmpty() || clickItem.getCount() == 1) {
                player.playSound(SoundEvents.BUNDLE_INSERT, 0.8F,
                        0.8F + player.level().getRandom().nextFloat() * 0.4F);
                setRecord(boombox, clickItem.split(1));
                slotAccess.set(old);
                return true;
            }
        }
        return false;
    }

    /**
     * Tooltip: "Sneak + RightClick to Pause"
     */
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (hasRecord(stack)) {
            ItemStack discStack = getRecord(stack);

            Component itemNameOrDescription;
            if (discStack.getItem().components().has(DataComponents.JUKEBOX_PLAYABLE)) {
                itemNameOrDescription = Component.translatable(discStack.getDescriptionId() + ".desc");
            } else {
                itemNameOrDescription = discStack.getHoverName();
            }

            tooltipComponents.add(itemNameOrDescription.copy().withStyle(ChatFormatting.GOLD));
        } else {
            tooltipComponents.add(Component.literal("No disc inserted").withStyle(ChatFormatting.GRAY));
        }
    }


    /**
     * Helper: Which hand is playing (if any)?
     */
    @Nullable
    public static InteractionHand getPlayingHand(LivingEntity entity) {
        if (!PLAYING_RECORDS.containsKey(entity.getId())) {
            return null;
        }
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack stack = entity.getItemInHand(hand);
            if (hasRecord(stack) && !stack.has(MelodyComponents.PAUSED)) {
                return hand;
            }
        }
        return null;
    }

    public static boolean hasRecord(ItemStack stack) {
        return stack.has(MelodyComponents.PLAYING_RECORD);
    }

    public static ItemStack getRecord(ItemStack stack) {
        PlayingRecordComponent recordComp = stack.get(MelodyComponents.PLAYING_RECORD);
        return recordComp != null ? recordComp.stack() : ItemStack.EMPTY;
    }

    public static void setRecord(ItemStack stack, ItemStack record) {
        if (record.isEmpty()) {
            stack.remove(MelodyComponents.PLAYING_RECORD);
        } else {
            stack.set(MelodyComponents.PLAYING_RECORD, new PlayingRecordComponent(record.copyWithCount(1)));
        }
    }
}
