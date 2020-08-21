package dev.titanlabs.titanbot.service;

import dev.titanlabs.titanbot.TitanBot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class PasteUtils {
    private final String apiKey;

    public PasteUtils(TitanBot bot) {
        this.apiKey = bot.getConfig("settings").string("pastebin-key");
    }

    public String getRawPaste(String id) {
        if (id.contains("paste_key") || id.contains("/")) {
            return "";
        }
        try {
            String link = "https://pastebin.com/raw/".concat(id).concat("?paste_key=").concat(this.apiKey);
            URL url = new URL(link);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder dataBuilder = new StringBuilder();
            reader.lines().forEach(dataBuilder::append);
            reader.close();
            return dataBuilder.toString();
        } catch (FileNotFoundException ex) {
            TitanBot.getLogger().info("Pastebin: Non existent paste entered");
        } catch (MalformedURLException ex) {
            TitanBot.getLogger().info("Pastebin: Bad id entered: {}", id);
        } catch (IOException ex) {
            TitanBot.getLogger().error("Pastebin: Unknown error. ", ex);
            ex.printStackTrace();
        }
        return "";
    }
}
