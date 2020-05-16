package dev.titanlabs.titanbot.commands.recognition.subs;

import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.managers.RecognitionManager;
import dev.titanlabs.titanbot.recognition.RecognitionType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import pink.zak.simplediscord.command.CommandContainer;
import pink.zak.simplediscord.command.command.SubCommand;

import java.awt.*;

public class GeneralInfoSub extends SubCommand {
    private final RecognitionManager recognitionManager;

    public GeneralInfoSub(TitanBot bot) {
        super(bot, false);
        this.recognitionManager = bot.getRecognitionManager();

        this.addFlat("info");
    }


    @Override
    public void onExecute(Member sender, CommandContainer container, String[] args) {
        StringBuilder recognitionStats = new StringBuilder();
        for (RecognitionType type : this.recognitionManager.getTypes().values()) {
            recognitionStats.append(type.isEnabled() ? ":white_check_mark: " : ":x: ")
                    .append(" - ")
                    .append(type.getName())
                    .append(" - ")
                    .append(type.getIdentifier())
                    .append("\n");
        }
        container.getChannel().sendMessage(new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(":brain: Recognition Stats (Enabled/Disabled - Name - Identifier)")
                .setDescription(recognitionStats.toString())
                .build()).queue();
    }
}
