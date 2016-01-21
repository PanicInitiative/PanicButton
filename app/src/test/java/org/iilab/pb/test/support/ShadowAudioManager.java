package org.iilab.pb.test.support;

import android.media.AudioManager;
import android.net.rtp.AudioStream;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

import java.util.HashMap;

/**
 * As of robolectrc 3.0 ShadowAudioManager is not overriding setMode() method and to test the panic alert, the mode needs to be set, otherwise NPE is thrown.
 * This class can be replaced later by Robolectric new releases which includes the implemenation of this method.
 */

@SuppressWarnings({"UnusedDeclaration"})
@Implements(AudioManager.class)
public class ShadowAudioManager {
    public static final int MAX_VOLUME_MUSIC_DTMF = 15;
    public static final int DEFAULT_MAX_VOLUME = 7;
    public static final int DEFAULT_VOLUME = 7;
    public static final int INVALID_VOLUME = 0;
    public static final int FLAG_NO_ACTION = 0;
    public static final int[] ALL_STREAMS = {
            AudioManager.STREAM_MUSIC,
            AudioManager.STREAM_ALARM,
            AudioManager.STREAM_NOTIFICATION,
            AudioManager.STREAM_RING,
            AudioManager.STREAM_SYSTEM,
            AudioManager.STREAM_VOICE_CALL,
            AudioManager.STREAM_DTMF
    };

    private int nextResponseValue = AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    private AudioManager.OnAudioFocusChangeListener lastAbandonedAudioFocusListener;
    private HashMap<Integer, AudioStream> streamStatus = new HashMap<>();
    private int ringerMode = AudioManager.RINGER_MODE_NORMAL;
    private int mode = AudioManager.MODE_NORMAL;
    private boolean wiredHeadsetOn;
    private boolean bluetoothA2dpOn;

    public ShadowAudioManager() {
        for (int stream : ALL_STREAMS) {
//                streamStatus.put(stream, new AudioStream(DEFAULT_VOLUME, DEFAULT_MAX_VOLUME, FLAG_NO_ACTION));
        }
//            streamStatus.get(AudioManager.STREAM_MUSIC).setMaxVolume(MAX_VOLUME_MUSIC_DTMF);
//            streamStatus.get(AudioManager.STREAM_DTMF).setMaxVolume(MAX_VOLUME_MUSIC_DTMF);
    }


    @Implementation
    public void setMode(int mode) {
        this.mode = mode;
    }

    @Implementation
    public int getMode() {
        return this.mode;
    }
}
