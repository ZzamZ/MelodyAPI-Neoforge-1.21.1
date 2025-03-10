package net.zam.melodyapi.registry;

import net.minecraft.world.item.Item;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zam.melodyapi.MelodyAPI;
import net.zam.melodyapi.common.item.*;

public class MelodyItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MelodyAPI.MOD_ID);


    public static final DeferredItem<Item> TEST_CASE = ITEMS.register("test_case", () -> new TestCase(new Item.Properties()));
    public static final DeferredItem<Item> TEST_KEY = ITEMS.register("test_key", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MUSICBOX = ITEMS.register("musicbox", () -> new MusicBoxItem(new Item.Properties()));
    public static final DeferredItem<Item> CARD_TEST_PACK = ITEMS.register("card_test_pack", () ->
            new TradingCardPackItem(new Item.Properties().stacksTo(16), TestCardSet.TEST_CARD_SET));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}


