package net.zam.melodyapi.common.network.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public record ClaimRewardPacket(ItemStack reward) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ClaimRewardPacket> TYPE =
            new CustomPacketPayload.Type<>(new ResourceLocation("melodyapi", "claim_reward"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClaimRewardPacket> STREAM_CODEC =
            StreamCodec.of(
                    RegistryFriendlyByteBuf::writeItem,
                    buf -> new ClaimRewardPacket(buf.readItem())
            );

    @Override
    public CustomPacketPayload.Type<?> type() {
        return TYPE;
    }

    public static void handleOnServer(ClaimRewardPacket packet, ServerPlayer player) {
        player.getInventory().add(packet.reward());
    }

    public static void handleOnClient(ClaimRewardPacket packet, net.minecraft.world.entity.player.Player player) {
        // Implement client-side logic here
    }
}
