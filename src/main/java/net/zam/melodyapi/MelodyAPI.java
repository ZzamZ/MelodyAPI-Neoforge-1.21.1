package net.zam.melodyapi;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.zam.melodyapi.common.network.ClaimRewardPacket;
import net.zam.melodyapi.common.network.ConsumeLootBoxItemsPacket;
import net.zam.melodyapi.registry.MelodyItems;
import net.zam.melodyapi.registry.MelodyMenuTypes;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(MelodyAPI.MOD_ID)
public class MelodyAPI {
    public static final String MOD_ID = "melodyapi";
    private static final Logger LOGGER = LogUtils.getLogger();

    public MelodyAPI(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerPayloadHandlers);

        MelodyItems.register(modEventBus);
        MelodyMenuTypes.register(modEventBus);

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    private void registerPayloadHandlers(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(ClaimRewardPacket.TYPE, ClaimRewardPacket.STREAM_CODEC, ClaimRewardPacket::handle);
        registrar.playToServer(ConsumeLootBoxItemsPacket.TYPE, ConsumeLootBoxItemsPacket.STREAM_CODEC, ConsumeLootBoxItemsPacket::handle);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MelodyAPI.MOD_ID, path);
    }


    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}
