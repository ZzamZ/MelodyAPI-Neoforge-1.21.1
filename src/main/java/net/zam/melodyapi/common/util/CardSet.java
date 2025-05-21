package net.zam.melodyapi.common.util;

import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CardSet {
    private final String name;
    private final List<Supplier<Item>> commonCards;
    private final List<Supplier<Item>> rareCards;
    private final Supplier<Item> ultraRareCard;

    public CardSet(String name, List<Supplier<Item>> commonCards, List<Supplier<Item>> rareCards, Supplier<Item> ultraRareCard) {
        this.name = name;
        this.commonCards = commonCards;
        this.rareCards = rareCards;
        this.ultraRareCard = ultraRareCard;
    }

    public String getName() {
        return this.name;
    }

    // Resolve items only when needed
    public List<Item> getCommonCards() {
        return this.commonCards.stream().map(Supplier::get).collect(Collectors.toList());
    }

    public List<Item> getRareCards() {
        return this.rareCards.stream().map(Supplier::get).collect(Collectors.toList());
    }

    public Item getUltraRareCard() {
        return this.ultraRareCard.get();
    }
}