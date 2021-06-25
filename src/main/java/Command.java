import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.event.message.MessageCreateEvent;

import java.awt.*;
import java.util.concurrent.ExecutionException;

public class Command {
    /*
    * Used for parse commands and Actions like locking a user
    * have one static method, embedBuilder used for make embeds (for make lines and code shorter and more efficient)
    * */
    private final String prefix; // The prefix of commands
    private final CommandActions commandAction = new CommandActions(); // See CommandActions.java
    private final String[] commands = {"ping", "server", "inf", "invite", "kick", "ban", "mute", "unmute", "def", "undef", "warn", "unwarn", "clear",
    "lock", "unlock", "delete", "chlock", "chunlock", "unban-all", "unlock-all", "set", "help"}; // List of bot commands (prefix will be included while parsing commands)
    private final Lock lock = new Lock(); // For locking users from sending messages
    public Command(String prefix){
        this.prefix = prefix;
    }
    public static EmbedBuilder embedBuilder(String command, MessageCreateEvent event){ // method that returns an almost-ready EmbedBuilder
        return new EmbedBuilder().setTitle(command).setAuthor(event.getMessageAuthor().getName(), "", event.getMessageAuthor().getAvatar());
    }
    public void parseCommand(CommandType command) throws ExecutionException, InterruptedException { // method that parses every command (or message for locked users)
        try {
            if (lock.isLocked(new UserSingleType(command.event().getMessageAuthor().asUser().get(), command.event().getServer().get()))) {
                command.event().getMessage().delete();
                return;
            } else if(lock.isChLocked(new UserChannelLockType(new UserSingleType(command.event().getMessageAuthor().asUser().get(), command.event().getServer().get()), command.event().getChannel()))){
                command.event().getMessage().delete();
                return;
            }
        }catch (Exception ignored){}
        if(command.command().startsWith(prefix)) { // to prevent expressions for unrelated messages
            command.api().getServerById(command.event().getServer().get().getId()).get();
            if (!command.event().getServer().get().hasPermission(command.api().getYourself(), PermissionType.ADMINISTRATOR)) {// Bot permissions check, without administrator access our bot should be not work
                command.event().getMessage().reply("This bot cannot be work without Administrator permission.");
                return;
            }
            if (command.command().equals(prefix + commands[0])) commandAction.commandPing(command);
            else if (command.command().equals(prefix + commands[1])) commandAction.commandServer(command);
            else if (command.command().equals(prefix + commands[2])) commandAction.commandInf(command);
            else if (command.command().equals(prefix + commands[3])) commandAction.commandInvite(command);
            else if (command.command().equals(prefix + commands[4])) commandAction.commandKick(command);
            else if (command.command().equals(prefix + commands[5])) commandAction.commandBan(command);
            else if (command.command().equals(prefix + commands[6])) commandAction.commandMute(command);
            else if (command.command().equals(prefix + commands[7])) commandAction.commandUnMute(command);
            else if (command.command().equals(prefix + commands[8])) commandAction.commandDef(command);
            else if (command.command().equals(prefix + commands[9])) commandAction.commandUnDef(command);
            else if (command.command().equals(prefix + commands[10])) commandAction.commandWarn(command);
            else if (command.command().equals(prefix + commands[11])) commandAction.commandUnWarn(command);
            else if (command.command().equals(prefix + commands[12])) commandAction.commandClear(command);
            else if (command.command().equals(prefix + commands[13])) commandAction.commandLock(command, lock);
            else if (command.command().equals(prefix + commands[14])) commandAction.commandUnlock(command, lock);
            else if (command.command().equals(prefix + commands[15])) commandAction.commandDelete(command);
            else if (command.command().equals(prefix + commands[16])) commandAction.commandChLock(command, lock);
            else if (command.command().equals(prefix + commands[17])) commandAction.commandChUnlock(command, lock);
            else if (command.command().equals(prefix + commands[18])) commandAction.commandUnBanAll(command);
            else if (command.command().equals(prefix + commands[19])) commandAction.commandUnlockAll(command, lock);
            else if (command.command().equals(prefix + commands[20])) commandAction.commandSet(command);
            else if (command.command().equals(prefix + commands[21])) commandAction.commandHelp(command);
        }
    }
}