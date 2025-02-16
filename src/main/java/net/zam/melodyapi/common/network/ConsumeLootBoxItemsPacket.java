package net.zam.melodyapi.common.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.zam.melodyapi.MelodyAPI;
import net.zam.melodyapi.common.gui.cases.BaseLootBoxMenu;

public class ConsumeLootBoxItemsPacket implements CustomPacketPayload {

    public static final Type<ConsumeLootBoxItemsPacket> TYPE = new Type<>(MelodyAPI.id("consume_lootbox_items"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ConsumeLootBoxItemsPacket> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            packet -> packet.keyItem,
            ItemStack.STREAM_CODEC,
            packet -> packet.caseItem,
            ConsumeLootBoxItemsPacket::new
    );

    private final ItemStack keyItem;
    private final ItemStack caseItem;

    public ConsumeLootBoxItemsPacket(final ItemStack keyItem, final ItemStack caseItem) {
        this.keyItem = keyItem;
        this.caseItem = caseItem;
    }

    @Override
    public Type<ConsumeLootBoxItemsPacket> type() {
        return TYPE;
    }

    public static void handle(ConsumeLootBoxItemsPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();
            if(player.containerMenu instanceof BaseLootBoxMenu<?>) {
                ((BaseLootBoxMenu<?>) player.containerMenu).consumeItems(player, packet.keyItem, packet.caseItem);
            }
        });
    }
}
