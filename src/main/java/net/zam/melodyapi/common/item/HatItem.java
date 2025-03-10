package net.zam.melodyapi.common.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class HatItem extends Item {

    public HatItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);

        EquipmentSlot slot = EquipmentSlot.HEAD; // Hard-coded if we only equip on head

        ItemStack existingHat = player.getItemBySlot(slot);

        if (!existingHat.isEmpty()) {
            if (!player.getInventory().add(existingHat)) {
                player.drop(existingHat, false);
            }
            player.setItemSlot(slot, ItemStack.EMPTY);
        }

        ItemStack hatToEquip = heldStack.copy();
        hatToEquip.setCount(1);

        player.setItemSlot(slot, hatToEquip);

        heldStack.shrink(1);

        playEquipSound(world, player);

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, heldStack);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.HEAD;
    }

    private void playEquipSound(Level world, Player player) {
        if (!world.isClientSide) {
            world.playSound(
                    null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SoundEvents.ARMOR_EQUIP_LEATHER,
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F
            );
        }
    }
}
