package net.zam.melodyapi.registry;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zam.melodyapi.MelodyAPI;
import net.zam.melodyapi.common.component.LoopingComponent;
import net.zam.melodyapi.common.component.MusicTrackComponent;
import net.zam.melodyapi.common.component.PausedComponent;
import net.zam.melodyapi.common.component.PlayingRecordComponent;

import java.util.function.UnaryOperator;

public class MelodyComponents {
    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MelodyAPI.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PausedComponent>> PAUSED = register(
            "paused", builder -> builder
                    .persistent(PausedComponent.CODEC)
                    .networkSynchronized(PausedComponent.STREAM_CODEC)
                    .cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PlayingRecordComponent>> PLAYING_RECORD = register(
            "playing_record", builder -> builder
                    .persistent(PlayingRecordComponent.CODEC)
                    .networkSynchronized(PlayingRecordComponent.STREAM_CODEC)
                    .cacheEncoding());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MusicTrackComponent>> MUSIC = register(
            "music", builder -> builder
                    .persistent(MusicTrackComponent.CODEC)
                    .networkSynchronized(MusicTrackComponent.STREAM_CODEC)
                    .cacheEncoding());

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return COMPONENTS.register(name, () -> builder.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        COMPONENTS.register(eventBus);
    }
}
