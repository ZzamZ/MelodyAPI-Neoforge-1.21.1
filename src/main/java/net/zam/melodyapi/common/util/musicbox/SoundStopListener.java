package net.zam.melodyapi.common.util.musicbox;

@FunctionalInterface
public interface SoundStopListener {

    /**
     * Called just before the sound is removed from the map.
     */
    void onStop();
}