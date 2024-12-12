package net.zam.melodyapi.common.network;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.zam.melodyapi.common.network.packet.ClaimRewardPacket;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);

        // Register ClaimRewardPacket
        registrar.playToClient(
                ClaimRewardPacket.TYPE,
                ClaimRewardPacket.STREAM_CODEC,
                ClaimRewardPacket::handleOnClient
        );

        registrar.playToServer(
                ClaimRewardPacket.TYPE,
                ClaimRewardPacket.STREAM_CODEC,
                ClaimRewardPacket::handleOnServer
        );
    }
}
