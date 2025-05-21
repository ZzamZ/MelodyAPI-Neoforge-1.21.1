package net.zam.melodyapi.common.cases;

import net.minecraft.resources.ResourceLocation;
import net.zam.melodyapi.MelodyAPI;
import net.zam.melodyapi.common.item.rarity.RarityItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CaseRewards {
    private static final Map<ResourceLocation, CaseEntry> CASES = new HashMap<>();

    public static CaseEntry register(ResourceLocation id, List<RarityItem> possibleRewards) {
        CaseEntry entry = new CaseEntry(id, possibleRewards);
        CASES.put(id, entry);
        return entry;
    }

    public static CaseEntry register(String id, List<RarityItem> possibleRewards) {
        return register(MelodyAPI.id(id), possibleRewards);
    }

    public static Optional<CaseEntry> getById(ResourceLocation id) {
        return Optional.ofNullable(CASES.get(id));
    }

    public static List<RarityItem> getPossibleRewardsById(ResourceLocation id) {
        return getById(id).map(CaseEntry::possibleRewards).orElse(List.of());
    }
}