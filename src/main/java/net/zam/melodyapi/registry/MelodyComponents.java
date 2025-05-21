package net.zam.melodyapi.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.zam.melodyapi.MelodyAPI;
import net.zam.melodyapi.common.component.MusicTrackComponent;
import net.zam.melodyapi.common.component.PausedComponent;
import net.zam.melodyapi.common.component.PlayingRecordComponent;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class MelodyComponents {
    public static void init() {}

    public static final DataComponentType<PausedComponent> PAUSED = register(
        "paused",
        builder -> builder
            .persistent(PausedComponent.CODEC)
            .networkSynchronized(PausedComponent.STREAM_CODEC)
            .cacheEncoding()
    );
    public static final DataComponentType<PlayingRecordComponent> PLAYING_RECORD = register(
        "playing_record",
        builder -> builder
            .persistent(PlayingRecordComponent.CODEC)
            .networkSynchronized(PlayingRecordComponent.STREAM_CODEC)
            .cacheEncoding()
    );
    public static final DataComponentType<MusicTrackComponent> MUSIC = register(
        "music",
        builder -> builder
            .persistent(MusicTrackComponent.CODEC)
            .networkSynchronized(MusicTrackComponent.STREAM_CODEC)
            .cacheEncoding()
    );

    public static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> component) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, MelodyAPI.id(name), component.apply(DataComponentType.builder()).build());
    }
}