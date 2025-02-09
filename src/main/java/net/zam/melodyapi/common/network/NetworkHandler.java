package net.zam.melodyapi.common.network;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.zam.melodyapi.MelodyAPI;
import net.zam.melodyapi.api.util.packet.ClaimRewardPacket;
import net.zam.melodyapi.api.util.packet.ConsumeLootBoxItemsPacket;


public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            MelodyAPI.id("network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        CHANNEL.registerMessage(0, ClaimRewardPacket.class, ClaimRewardPacket::toBytes, ClaimRewardPacket::new, ClaimRewardPacket::handle);
        CHANNEL.messageBuilder(ConsumeLootBoxItemsPacket.class, 1).encoder(ConsumeLootBoxItemsPacket::toBytes).decoder(ConsumeLootBoxItemsPacket::new).consumerMainThread(ConsumeLootBoxItemsPacket::handle).add();
    }

    public static <MSG> void sendToServer(MSG packet) {
        CHANNEL.send(PacketDistributor.SERVER.noArg(), packet);
    }
}

