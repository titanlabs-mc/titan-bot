package dev.titanlabs.titanbot.recognition.types.configuration;

import dev.titanlabs.titanbot.recognition.RecognitionType;
import net.dv8tion.jda.api.entities.TextChannel;

public class WrongZombiePigmanRecognition extends RecognitionType {

    public WrongZombiePigmanRecognition(boolean enabled) {
        super("Wrong Zombie Pigman (Configuration)", "wrongzombiepigman", enabled);
    }

    @Override
    public boolean query(String message) {
        return message.contains("variable: zombie_pigman") || message.contains("root: zombie_pigman");
    }

    @Override
    public void run(TextChannel channel) {
        channel.sendMessage("I noticed you were using `zombie_pigman` as a variable. You should be using `pig_zombie`.").queue();
        this.uses.getAndIncrement();
    }
}
