package net.zam.melodyapi.common.util.musicbox;

import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
public interface JukeboxSongExt {

    List<TrackData> veil$tracks();
}