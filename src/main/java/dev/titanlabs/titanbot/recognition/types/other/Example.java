package dev.titanlabs.titanbot.recognition.types.other;

import dev.titanlabs.titanbot.recognition.RecognitionType;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.concurrent.atomic.AtomicInteger;

public class Example implements RecognitionType {
    private boolean enabled;
    private final AtomicInteger uses;

    public Example(boolean enabled) {
        this.enabled = enabled;
        this.uses = new AtomicInteger();
    }

    @Override
    public boolean query(String message) {
        return false;
    }

    @Override
    public void run(TextChannel channel) {

        this.uses.getAndIncrement();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void disable() {
        this.enabled = false;
    }

    @Override
    public void enable() {
        this.enabled = true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public int getUses() {
        return this.uses.intValue();
    }
}
