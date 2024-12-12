package net.zam.melodyapi.common.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.zam.melodyapi.common.gui.cases.BaseLootBoxMenu;


public record ConsumeLootBoxItemsPacket(ItemStack keyItem, ItemStack caseItem) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ConsumeLootBoxItemsPacket> TYPE =
            new CustomPacketPayload.Type<>(new ResourceLocation("melodyapi", "consume_lootbox_items"));
    public static final StreamCodec<FriendlyByteBuf, ConsumeLootBoxItemsPacket> STREAM_CODEC =
            StreamCodec.composite(
                    FriendlyByteBuf::writeItemStack, ConsumeLootBoxItemsPacket::keyItem,
                    FriendlyByteBuf::writeItemStack, ConsumeLootBoxItemsPacket::caseItem,
                    ConsumeLootBoxItemsPacket::new
            );

    @Override
    public CustomPacketPayload.Type<?> type() {
        return TYPE;
    }

    public static void handleOnServer(ConsumeLootBoxItemsPacket packet, ServerPlayer player) {
        if (player.containerMenu instanceof BaseLootBoxMenu lootBoxMenu) {
            lootBoxMenu.consumeItems(player, packet.keyItem, packet.caseItem);
        }
    }

    public static void handleOnClient(ConsumeLootBoxItemsPacket packet, net.minecraft.client.Minecraft client) {
        // Client-side handling code (if needed)
    }
}
