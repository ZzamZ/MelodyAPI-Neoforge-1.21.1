package net.zam.melodyapi.common.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.zam.melodyapi.common.item.rarity.Rarity;
import net.zam.melodyapi.common.item.rarity.RarityItem;

import java.util.function.Supplier;

public class ClaimRewardPacket {

    private final RarityItem reward;

    public ClaimRewardPacket(RarityItem reward) {
        this.reward = reward;
    }

    public ClaimRewardPacket(FriendlyByteBuf buf) {
        this.reward = new RarityItem(buf.readItem(), buf.readEnum(Rarity.class));
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeItem(reward.getItemStack());
        buf.writeEnum(reward.getRarity());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                player.closeContainer();
                ItemStack stack = reward.getItemStack().copy();
                boolean added = player.getInventory().add(stack);
                if(added && stack.isEmpty()) {
                    player.inventoryMenu.broadcastChanges();
                } else {
                    ItemEntity itemEntity;
                    itemEntity = player.drop(stack, false);
                    if (itemEntity != null) {
                        itemEntity.setNoPickUpDelay();
                        itemEntity.setTarget(player.getUUID());
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
