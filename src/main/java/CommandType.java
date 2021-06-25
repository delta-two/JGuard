import org.javacord.api.DiscordApi;
import org.javacord.api.event.message.MessageCreateEvent;

public record CommandType(String command, String[] args, DiscordApi api,
                          MessageCreateEvent event) {}
/*
* Used for every command. Usually every command needs one or more of these
* records and if your java version doesn't support record (lower than Java 14)
* you can simply convert it to a class and define a getter for each argument.
* */