package net.zam.melodyapi.common.cases;

import net.minecraft.resources.ResourceLocation;
import net.zam.melodyapi.common.item.rarity.RarityItem;

import java.util.List;

public record CaseEntry(ResourceLocation id, List<RarityItem> possibleRewards) {}