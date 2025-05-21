package net.zam.melodyapi.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.zam.melodyapi.MelodyAPI;
import net.zam.melodyapi.common.item.*;

public class MelodyItems {
    public static void init() {}

    public static final Item TEST_CASE = register(
        "test_case",
        new TestCase(new Item.Properties())
    );
    public static final Item SINGLETON_CASE = register(
        "singleton_case",
        new SingletonCase(new Item.Properties())
    );
    public static final Item TEST_KEY = register(
        "test_key",
        new Item(new Item.Properties())
    );
    public static final Item MUSICBOX = register(
        "musicbox",
        new MusicBoxItem(new Item.Properties())
    );
    public static final Item CARD_TEST_PACK = register(
        "card_test_pack",
        new TradingCardPackItem(new Item.Properties().stacksTo(16), TestCardSet.TEST_CARD_SET)
    );

    public static <T extends Item> T register(String name, T item) {
        return Registry.register(BuiltInRegistries.ITEM, MelodyAPI.id(name), item);
    }
}