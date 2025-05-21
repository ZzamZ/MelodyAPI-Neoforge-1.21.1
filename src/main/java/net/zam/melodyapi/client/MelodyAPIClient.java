package net.zam.melodyapi.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.zam.melodyapi.common.gui.casetest.SingletonCaseScreen;
import net.zam.melodyapi.common.gui.casetest.TestCaseScreen;
import net.zam.melodyapi.registry.MelodyMenuTypes;

public class MelodyAPIClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.register(MelodyMenuTypes.TEST_CASE, TestCaseScreen::new);
        MenuScreens.register(MelodyMenuTypes.SINGLETON_CASE, SingletonCaseScreen::new);
    }
}