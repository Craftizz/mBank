package io.github.craftizz.mbank.configuration;

import org.bukkit.Sound;

public enum MessageType {

    DENY(Sound.BLOCK_BEACON_DEACTIVATE),
    INFORMATION(Sound.BLOCK_NOTE_BLOCK_BELL);

    final Sound sound;

    MessageType(Sound sound) {
        this.sound = sound;
    }

    public Sound getSound() {
        return sound;
    }

}
