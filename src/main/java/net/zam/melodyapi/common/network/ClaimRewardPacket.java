package net.zam.melodyapi.common.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.zam.melodyapi.MelodyAPI;
import net.zam.melodyapi.common.item.rarity.Rarity;
import net.zam.melodyapi.common.item.rarity.RarityItem;

public class ClaimRewardPacket implements CustomPacketPayload {

    public static final Type<ClaimRewardPacket> TYPE = new Type<>(MelodyAPI.id("claim_reward"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClaimRewardPacket> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            packet -> packet.reward().getItemStack(),
            ByteBufCodecs.STRING_UTF8,
            packet -> packet.reward.getRarity().name(),
            ClaimRewardPacket::new
    );

    private final RarityItem reward;

    public ClaimRewardPacket(RarityItem reward) {
        this.reward = reward;
    }

    public ClaimRewardPacket(ItemStack itemStack, String rarity) {
        this.reward = new RarityItem(itemStack, Rarity.valueOf(rarity));
    }

    @Override
    public Type<ClaimRewardPacket> type() {
        return TYPE;
    }

    public static void handle(ClaimRewardPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();
            player.closeContainer();
            ItemStack stack = packet.reward().getItemStack().copy();
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
        });
    }

    public RarityItem reward() {
        return reward;
    }
}
