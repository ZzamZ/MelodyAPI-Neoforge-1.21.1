package net.zam.melodyapi.common.item;

import net.zam.melodyapi.common.util.CardSet;
import net.zam.melodyapi.registry.MelodyItems;

import java.util.List;

public class TestCardSet {

    public static final CardSet TEST_CARD_SET = new CardSet("test_card_set",
            List.of(MelodyItems.TEST_CASE),
            List.of(MelodyItems.TEST_CASE),
            MelodyItems.TEST_KEY);
}
