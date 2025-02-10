package net.zam.melodyapi.common.events;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.zam.melodyapi.MelodyAPI;
import net.zam.melodyapi.common.gui.casetest.TestCaseScreen;
import net.zam.melodyapi.registry.MelodyMenuTypes;

@EventBusSubscriber(modid = MelodyAPI.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onClientSetup(RegisterMenuScreensEvent event) {
        event.register(MelodyMenuTypes.TEST_CASE.get(), TestCaseScreen::new);
    }
}
