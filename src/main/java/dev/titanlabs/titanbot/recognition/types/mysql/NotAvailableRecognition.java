package dev.titanlabs.titanbot.recognition.types.mysql;

import dev.titanlabs.titanbot.recognition.RecognitionType;
import net.dv8tion.jda.api.entities.TextChannel;

public class NotAvailableRecognition extends RecognitionType {

    public NotAvailableRecognition(boolean enabled) {
        super("Wrong Address/Unavailable (MySQL)", "mysqlnotavailable", enabled);
    }

    @Override
    public boolean query(String message) {
        return (message.contains("io.github.luxuryquests.shade.shade.hikari.pool.hikaripool$poolinitializationexception: failed to initialize pool: communications link failure") ||
                message.contains("io.github.battlepass.shade.shade.hikari.pool.hikaripool$poolinitializationexception: failed to initialize pool: communications link failure")) &&
                message.contains("the last packet sent successfully to the server was") &&
                message.contains("the driver has not received any packets from the server");
    }

    @Override
    public void run(TextChannel channel) {
        channel.sendMessage("There was an issue connecting to your database. Check the following:\n"
                .concat("1) You are using the correct IP address.\n")
                .concat("2) The MySQL database is accessible from the server.")
        ).queue();
        this.uses.getAndIncrement();
    }
}
