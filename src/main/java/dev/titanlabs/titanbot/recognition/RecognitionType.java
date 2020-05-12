package dev.titanlabs.titanbot.recognition;

import net.dv8tion.jda.api.entities.TextChannel;

public interface RecognitionType {

    boolean query(String message);

    void run(TextChannel channel);

    String getName();

    void disable();

    void enable();

    boolean isEnabled();

    int getUses();
}
