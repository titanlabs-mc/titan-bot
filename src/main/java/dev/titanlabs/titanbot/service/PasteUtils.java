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
            return dataBuilder.toString();
        } catch (FileNotFoundException ex) {
            System.out.println("Pastebin: Non existent paste entered");
        } catch (MalformedURLException ex) {
            System.out.println("Pastebin: Bad URL entered.");
        } catch (IOException ex) {
            System.out.println("Pastebin: Unknown error.");
        }
        return "";
    }
}
