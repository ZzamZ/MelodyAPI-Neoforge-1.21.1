package net.zam.melodyapi.common.util;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.List;
import java.util.stream.Collectors;

public class CardSet {
    private final String name;
    private final List<DeferredItem<Item>> commonCards;
    private final List<DeferredItem<Item>> rareCards;
    private final DeferredItem<Item> ultraRareCard;

    public CardSet(String name, List<DeferredItem<Item>> commonCards, List<DeferredItem<Item>> rareCards, DeferredItem<Item> ultraRareCard) {
        this.name = name;
        this.commonCards = commonCards;
        this.rareCards = rareCards;
        this.ultraRareCard = ultraRareCard;
    }

    public String getName() {
        return name;
    }

    // Resolve items only when needed
    public List<Item> getCommonCards() {
        return commonCards.stream().map(DeferredItem::get).collect(Collectors.toList());
    }

    public List<Item> getRareCards() {
        return rareCards.stream().map(DeferredItem::get).collect(Collectors.toList());
    }

    public Item getUltraRareCard() {
        return ultraRareCard.get();
    }
}
