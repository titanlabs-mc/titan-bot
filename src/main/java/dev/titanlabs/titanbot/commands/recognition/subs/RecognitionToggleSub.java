package dev.titanlabs.titanbot.commands.recognition.subs;

import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.managers.RecognitionManager;
import dev.titanlabs.titanbot.recognition.RecognitionType;
import net.dv8tion.jda.api.entities.Member;
import pink.zak.simplediscord.command.CommandContainer;
import pink.zak.simplediscord.command.command.SubCommand;

import java.util.Optional;

public class RecognitionToggleSub extends SubCommand {
    private final RecognitionManager recognitionManager;

    public RecognitionToggleSub(TitanBot bot) {
        super(bot, bot.getGuild().getRoleById("686658320425025537"), false);
        this.recognitionManager = bot.getRecognitionManager();

        this.addArgument(String.class, "recognitionName");
        this.addFlat("toggle");
    }

    @Override
    public void onExecute(Member sender, CommandContainer container, String[] args) {
        String input = this.parseArgument(args, null, 0);
        Optional<RecognitionType> optionalRecognition = this.recognitionManager.getType(input);
        if (!optionalRecognition.isPresent()) {
            container.getChannel().sendMessage("Could not find the recognition type ".concat(input)).queue();
            return;
        }
        RecognitionType recognition = optionalRecognition.get();
        if (recognition.isEnabled()) {
            recognition.disable();
        } else {
            recognition.enable();
        }
        container.getChannel().sendMessage(
                "The recognition for ".concat(recognition.getIdentifier()).concat(" has been **").concat(recognition.isEnabled() ? "enabled" : "disabled").concat("**!"))
                .queue();
    }
}
