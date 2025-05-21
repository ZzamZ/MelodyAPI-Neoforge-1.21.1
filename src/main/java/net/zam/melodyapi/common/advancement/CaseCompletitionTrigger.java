package net.zam.melodyapi.common.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.zam.melodyapi.registry.MelodyCriteriaTriggers;

import java.util.Optional;

public class CaseCompletitionTrigger extends SimpleCriterionTrigger<CaseCompletitionTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, ResourceLocation id) {
        this.trigger(player, instance -> instance.matches(id));
    }

    public record TriggerInstance(
        Optional<ContextAwarePredicate> player,
        ResourceLocation id
    ) implements SimpleInstance {

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                ResourceLocation.CODEC.fieldOf("id").forGetter(TriggerInstance::id)
            ).apply(instance, TriggerInstance::new)
        );

        public boolean matches(ResourceLocation id) {
            return this.id.equals(id);
        }

        public static Criterion<CaseCompletitionTrigger.TriggerInstance> fromCase(ResourceLocation id) {
            return MelodyCriteriaTriggers.CASE_COMPLETITION.createCriterion(new CaseCompletitionTrigger.TriggerInstance(Optional.empty(), id));
        }

        @Override
        public Optional<ContextAwarePredicate> player() {
            return this.player;
        }
    }
}