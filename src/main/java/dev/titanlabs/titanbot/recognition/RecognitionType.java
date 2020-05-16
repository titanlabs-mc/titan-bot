package dev.titanlabs.titanbot.recognition;

import net.dv8tion.jda.api.entities.TextChannel;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class RecognitionType {
    private final String name;
    private final String identifier;
    private boolean enabled;
    public AtomicInteger uses;

    public RecognitionType(String name, String identifier, boolean enabled) {
        this.name = name;
        this.identifier = identifier;
        this.enabled = enabled;
        this.uses = new AtomicInteger();
    }

    public abstract boolean query(String message);

    public abstract void run(TextChannel channel);

    public String getName() {
        return this.name;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void disable() {
        this.enabled = false;
    }

    public void enable() {
        this.enabled = true;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public int getUses() {
        return this.uses.intValue();
    }
}
