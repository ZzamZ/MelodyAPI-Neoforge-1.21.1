package net.zam.melodyapi;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.zam.melodyapi.common.network.ClaimRewardPacket;
import net.zam.melodyapi.common.network.ConsumeLootBoxItemsPacket;
import net.zam.melodyapi.registry.*;
import org.slf4j.Logger;

public class MelodyAPI implements ModInitializer {
    public static final String MOD_ID = "melodyapi";
    public static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        MelodyComponents.init();
        MelodyItems.init();
        MelodyMenuTypes.init();
        MelodyCaseRewards.init();
        MelodyCriteriaTriggers.init();

        registerPayloadHandlers();
    }

    private static void registerPayloadHandlers() {
        PayloadTypeRegistry.playC2S().register(ClaimRewardPacket.TYPE, ClaimRewardPacket.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ClaimRewardPacket.TYPE, ClaimRewardPacket::handle);

        PayloadTypeRegistry.playC2S().register(ConsumeLootBoxItemsPacket.TYPE, ConsumeLootBoxItemsPacket.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ConsumeLootBoxItemsPacket.TYPE, ConsumeLootBoxItemsPacket::handle);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MelodyAPI.MOD_ID, path);
    }
}