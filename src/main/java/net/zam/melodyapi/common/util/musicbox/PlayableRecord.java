package net.zam.melodyapi.common.util.musicbox;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxSong;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.zam.melodyapi.registry.MelodyComponents;

import java.util.Optional;

public final class PlayableRecord {

    private PlayableRecord() {
    }

    public static boolean isPlayableRecord(ItemStack stack) {
        return stack.has(MelodyComponents.MUSIC) || stack.has(DataComponents.JUKEBOX_PLAYABLE);
    }

    /**
     * Checks if the local player is close enough to receive "now playing" text.
     *
     * @param x The x position of the sound source
     * @param y The y position of the sound source
     * @param z The z position of the sound source
     * @return True if the player is within 64 blocks (8^2 = 64 distance squared)
     */
    @OnlyIn(Dist.CLIENT)
    public static boolean canShowMessage(double x, double y, double z) {
        LocalPlayer player = Minecraft.getInstance().player;
        return player == null || player.distanceToSqr(x, y, z) <= 4096.0;
    }

    /**
     * Creates a SoundInstance for playing a standard JukeboxSong (vanilla-like record) on an entity.
     *
     * @param stack               The record-like item
     * @param entity              The entity that "holds" or plays the record
     * @param track               The track index (only 0 is valid for simple discs)
     * @param attenuationDistance How far the sound attenuates
     * @return A SoundInstance wrapped in an Optional if the disc is valid, otherwise empty
     */
    @OnlyIn(Dist.CLIENT)
    public static Optional<SoundInstance> createEntitySound(ItemStack stack, Entity entity, int track, int attenuationDistance) {
        // For vanilla-like discs, only track == 0 exists
        if (track != 0) {
            return Optional.empty();
        }

        // Check if the item is a standard JukeboxSong
        Optional<Holder<JukeboxSong>> maybeSong = JukeboxSong.fromStack(entity.registryAccess(), stack);
        if (maybeSong.isEmpty()) {
            return Optional.empty();
        }

        // Show "Now Playing" message if not muffled and the player is within distance
        JukeboxSong song = maybeSong.get().value();
        if (entity.level().getBlockState(entity.blockPosition().above()).isAir() &&
                canShowMessage(entity.getX(), entity.getY(), entity.getZ())) {
            Minecraft.getInstance().gui.setNowPlaying(song.description());
        }

        // Return a simple SoundInstance (provided by vanilla or your own custom logic)
        return Optional.of(new EntityRecordSoundInstance(song.soundEvent().value(), entity));
    }
}
