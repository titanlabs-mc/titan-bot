package dev.titanlabs.titanbot.recognition.types.mysql;

import dev.titanlabs.titanbot.recognition.RecognitionType;
import net.dv8tion.jda.api.entities.TextChannel;

public class AccessDeniedRecognition extends RecognitionType {

    public AccessDeniedRecognition(boolean enabled) {
        super("Access Denied (MySQL)", "mysqlaccessdenied", enabled);
    }

    @Override
    public boolean query(String message) {
        return (message.contains("io.github.luxuryquests.shade.shade.hikari.pool.hikaripool$poolinitializationexception: failed to initialize pool: access denied for user") ||
                message.contains("io.github.battlepass.shade.shade.hikari.pool.hikaripool$poolinitializationexception: failed to initialize pool: access denied for user")) &&
                message.contains("using password");
    }


    public void run(TextChannel channel) {
        channel.sendMessage("It seems like you have an incorrect password for MySQL or the user you're logging in with does not have access to the database. "
                .concat("Please check your database settings and try again.")).queue();
        this.uses.getAndIncrement();
    }
}
