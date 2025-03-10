package net.zam.melodyapi.common.util.musicbox;

import net.minecraft.client.resources.sounds.SoundInstance;

/**
 * Provides the source sound instance for a wrapped sound instance
 *
 * @author Jackson
 */
public interface WrappedSoundInstance {

    /**
     * @return The parent sound instance
     */
    SoundInstance getParent();
}