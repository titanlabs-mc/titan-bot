package dev.titanlabs.titanbot.storage;

import dev.titanlabs.titanbot.objects.TitanUser;
import pink.zak.simplediscord.bot.SimpleBot;
import pink.zak.simplediscord.storage.storage.Storage;
import pink.zak.simplediscord.storage.storage.load.Deserializer;
import pink.zak.simplediscord.storage.storage.load.Serializer;

public class UserStorage extends Storage<TitanUser> {

    public UserStorage(SimpleBot plugin) {
        super(plugin, factory -> factory.create("json", path -> path.resolve("data"), "users"));
    }

    @Override
    public Serializer<TitanUser> serializer() {
        return ((user, json, gson) -> {
            json.addProperty("id", user.getId());
            json.addProperty("ticketsOpened", user.getTicketsOpened().intValue());
            json.addProperty("ticketChannelId", user.getTicketChannelIdLegacy());
            return json;
        });
    }

    @Override
    public Deserializer<TitanUser> deserializer() {
        return ((json, gson) -> {
            String id = json.get("id").getAsString();
            int ticketsOpened = json.get("ticketsOpened").getAsInt();
            String ticketChannelId = json.get("ticketChannelId").getAsString();
            return new TitanUser(id, ticketsOpened, ticketChannelId);
        });
    }
}
