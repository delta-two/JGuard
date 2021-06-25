import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import java.util.concurrent.ExecutionException;
/*
* Project main file.
* Use Java-14 or higher.
*
* */
public class JGuard {
    private static final String TOKEN = ""// Your Top-Secret Bot Token
    public static CommandType getCommandType(DiscordApi api, MessageCreateEvent event){
        String[] args = event.getMessage().getContent().split(" "); // Split every word
        return new CommandType(args[0], args, api, event); // See CommandType.java
    }
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var bot = new DiscordApiBuilder().setToken(TOKEN).login().get(); // The bot or API
        var command = new Command("?");
        /*
        * See Command.java
        * Set the prefix whatever you want i preferred Question mark
        * as the prefix, better to use single character prefixes.
        * */
        bot.addMessageCreateListener(event -> {
            try {
                command.parseCommand(getCommandType(bot, event));
            } catch (ExecutionException | InterruptedException ignored) { }
        }); // The listener that parses any new messages from every users and all servers the bot is added
    }
}
