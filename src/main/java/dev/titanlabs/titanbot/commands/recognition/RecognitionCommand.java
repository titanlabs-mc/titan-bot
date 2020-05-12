package dev.titanlabs.titanbot.commands.recognition;

import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.recognition.RecognitionType;
import dev.titanlabs.titanbot.recognition.manager.RecognitionManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import pink.zak.simplediscord.command.CommandContainer;
import pink.zak.simplediscord.command.command.SimpleCommand;

import java.awt.*;

public class RecognitionCommand extends SimpleCommand {
    private final RecognitionManager recognitionManager;

    public RecognitionCommand(TitanBot bot) {
        super(bot, "recognition", false);
        this.recognitionManager = bot.getRecognitionManager();
    }

    @Override
    public void onExecute(Member sender, CommandContainer container, String[] args) {
        StringBuilder recognitionStats = new StringBuilder();
        for (RecognitionType type : this.recognitionManager.getTypes().values()) {
            recognitionStats.append(type.isEnabled() ? ":white_check_mark: " : ":x: ")
                    .append(type.getName())
                    .append(" - ")
                    .append(type.getUses())
                    .append(" triggers")
                    .append("\n");
        }
        container.getChannel().sendMessage(new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(":brain: Recognition Stats (Since Start)")
                .setDescription(recognitionStats.toString())
                .build()).queue();
    }
}
