package dev.titanlabs.titanbot.objects;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class TitanUser {
    private final String id;
    private AtomicInteger ticketsOpened;
    private String ticketChannelId;

    public TitanUser(String id) {
        this.id = id;
        this.ticketsOpened = new AtomicInteger();
        this.ticketChannelId = "N/A";
    }

    public TitanUser(String id, int ticketsOpened, String ticketChannelId) {
        this.id = id;
        this.ticketsOpened = new AtomicInteger(ticketsOpened);
        this.ticketChannelId = ticketChannelId;
    }

    public String getId() {
        return this.id;
    }

    public AtomicInteger getTicketsOpened() {
        return this.ticketsOpened;
    }

    public Optional<String> getTicketChannelId() {
        return this.ticketChannelId.equals("N/A") ? Optional.empty() : Optional.of(this.ticketChannelId);
    }

    public String getTicketChannelIdLegacy() {
        return this.ticketChannelId;
    }

    public void setTicketChannelId(String id) {
        this.ticketChannelId = id;
    }
}
