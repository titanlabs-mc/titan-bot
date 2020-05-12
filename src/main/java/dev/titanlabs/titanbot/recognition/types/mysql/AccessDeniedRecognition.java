package dev.titanlabs.titanbot.recognition.types.mysql;

import dev.titanlabs.titanbot.recognition.RecognitionType;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.concurrent.atomic.AtomicInteger;

public class AccessDeniedRecognition implements RecognitionType {
    private boolean enabled;
    private final AtomicInteger uses;

    public AccessDeniedRecognition(boolean enabled) {
        this.enabled = enabled;
        this.uses = new AtomicInteger();
    }

    @Override
    public boolean query(String message) {
        return (message.contains("io.github.luxuryquests.shade.shade.hikari.pool.hikaripool$poolinitializationexception: failed to initialize pool: communications link failure") ||
                message.contains("io.github.battlepass.shade.shade.hikari.pool.hikaripool$poolinitializationexception: failed to initialize pool: communications link failure"))
                && message.contains("using password");
    }

    @Override
    public void run(TextChannel channel) {
        channel.sendMessage("It seems like you have an incorrect password for MySQL or the user you're logging in with does not have access to the database. "
                .concat("Please check your database settings and try again.")).queue();
        this.uses.getAndIncrement();
    }

    @Override
    public String getName() {
        return "MySQL - Access Denied";
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
