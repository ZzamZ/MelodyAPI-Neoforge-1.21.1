package net.zam.melodyapi.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.zam.melodyapi.MelodyAPI;
import net.zam.melodyapi.common.advancement.CaseCompletitionTrigger;
import net.zam.melodyapi.common.advancement.CaseRewardTrigger;
import net.zam.melodyapi.registry.MelodyCaseRewards;
import net.zam.melodyapi.registry.MelodyItems;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AdvancementGenerator extends FabricAdvancementProvider {
    public AdvancementGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer) {
        Advancement.Builder.advancement()
            .display(
                MelodyItems.SINGLETON_CASE,
                Component.translatable("advancement.melodyapi.singleton_case.title"),
                Component.translatable("advancement.melodyapi.singleton_case.description"),
                null, // background
                AdvancementType.TASK,
                true,  // showToast
                true,  // announceToChat
                false  // hidden
            )
            .addCriterion("got_reward_singleton_case", CaseRewardTrigger.TriggerInstance.fromCase(MelodyCaseRewards.SINGLETON_CASE.id()))
            .save(consumer, MelodyAPI.MOD_ID + ":cases/singleton_case");

        Advancement.Builder.advancement()
            .display(
                MelodyItems.SINGLETON_CASE,
                Component.translatable("advancement.melodyapi.singleton_case_complete.title"),
                Component.translatable("advancement.melodyapi.singleton_case_complete.description"),
                null, // background
                AdvancementType.CHALLENGE,
                true,  // showToast
                true,  // announceToChat
                false  // hidden
            )
            .addCriterion("completed_singleton_case", CaseCompletitionTrigger.TriggerInstance.fromCase(MelodyCaseRewards.SINGLETON_CASE.id()))
            .save(consumer, MelodyAPI.MOD_ID + ":cases/singleton_case_complete");
    }
}
