package net.zam.melodyapi.common.util.musicbox;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

/**
 * A simpler SoundTracker: starts/stops music playback for an entity.
 * No looping logic or callbacks.
 */
public class SoundTracker {

    private static final Int2ObjectArrayMap<SoundInstance> ENTITY_SOUNDS = new Int2ObjectArrayMap<>();

    /**
     * Start (or replace) music for a given entity with the provided record.
     * If the record is empty, it simply stops any existing sound.
     */
    public static void playMusicBox(int entityId, ItemStack record) {
        stopMusicBox(entityId);

        if (record.isEmpty()) {
            return;
        }

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }

        Entity entity = level.getEntity(entityId);
        if (entity == null) {
            return;
        }

        Optional<? extends SoundInstance> soundOpt = PlayableRecord.createEntitySound(record, entity, 0, 8);
        if (soundOpt.isEmpty()) {
            return;
        }

        SoundInstance newSound = soundOpt.get();
        ENTITY_SOUNDS.put(entityId, newSound);
        Minecraft.getInstance().getSoundManager().play(newSound);
    }

    /**
     * Stop any currently playing sound for the given entity.
     */
    public static void stopMusicBox(int entityId) {
        SoundInstance oldSound = ENTITY_SOUNDS.remove(entityId);
        if (oldSound != null) {
            Minecraft.getInstance().getSoundManager().stop(oldSound);
        }
    }

    /**
     * Optional getter if you need to check whether the entity is currently playing anything.
     */
    public static SoundInstance getEntitySound(int entityId) {
        return ENTITY_SOUNDS.get(entityId);
    }
}
