package net.zam.melodyapi.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.zam.melodyapi.MelodyAPI;
import net.zam.melodyapi.common.gui.casetest.SingletonCaseMenu;
import net.zam.melodyapi.common.gui.casetest.TestCaseMenu;

public class MelodyMenuTypes {
    public static void init() {}

    public static final MenuType<TestCaseMenu> TEST_CASE = register("test_case_menu", TestCaseMenu::new);
    public static final MenuType<SingletonCaseMenu> SINGLETON_CASE = register("singleton_case_menu", SingletonCaseMenu::new);

    public static <T extends AbstractContainerMenu> MenuType<T> register(String name, MenuType.MenuSupplier<T> factory) {
        return Registry.register(BuiltInRegistries.MENU, MelodyAPI.id(name), new MenuType<>(factory, FeatureFlags.VANILLA_SET));
    }
}