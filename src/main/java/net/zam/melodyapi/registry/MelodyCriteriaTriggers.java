package net.zam.melodyapi.registry;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.zam.melodyapi.MelodyAPI;
import net.zam.melodyapi.common.advancement.CaseCompletitionTrigger;
import net.zam.melodyapi.common.advancement.CaseRewardTrigger;

public class MelodyCriteriaTriggers {
    public static void init() {}

    public static final CaseRewardTrigger CASE_REWARD = register(
        "case_reward",
        new CaseRewardTrigger()
    );

    public static final CaseCompletitionTrigger CASE_COMPLETITION = register(
        "case_completition",
        new CaseCompletitionTrigger()
    );

    public static <T extends CriterionTrigger<?>> T register(String name, T trigger) {
        return Registry.register(BuiltInRegistries.TRIGGER_TYPES, MelodyAPI.id(name), trigger);
    }
}