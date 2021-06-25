import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.user.User;

import java.awt.*;
import java.util.concurrent.ExecutionException;

public class CommandActions {
    /*
     * A Class that's used for running or acting commands
     * this is the most important class for commands
     * If you need help to know usage of every command without reading the method, check the last command method called *commandHelp()*
     * */
    public static final Color ACCESS_DENIED = Color.RED.brighter(), ERROR = Color.RED.darker(), INFO = Color.BLUE, DONE = Color.GREEN, ALREADY_DONE = Color.GREEN.darker(); // Colors for every embed message
    Warn warn; // For warn users
    // ---------------------------------------------------- \\

    /* It's called ping but actually it's only for testing the bot */
    public void commandPing(CommandType command) {
        command.event().getMessage().reply(Command.embedBuilder(command.command(), command.event()).setColor(INFO));
    }

    /* Used for get server information */
    public void commandServer(CommandType command) {
        var server = command.event().getServer().get(); // may receive a warning that says "'Optional.get()' without 'isPresent()' check ", i think better to ignore it.
        command.event().getMessage().reply(Command.embedBuilder(command.command(), command.event()).setDescription("Server Name: " + server.getName() + "\nServer ID: " + server.getId() + "\nServer Description: " + (server.getDescription().isPresent() ? server.getDescription().get() : "Empty")
                + "\nServer Members: " + server.getMemberCount() + "\nServer Text Channels: " + server.getTextChannels().size() + "\nServer Voice Channels: " + server.getVoiceChannels().size()
                + "\nServer Roles: " + server.getRoles().size()).setColor(INFO));
    }

    /* Used for get self or other users main information */
    public void commandInf(CommandType command) {
        var message = command.event().getMessage();
        var users = message.getMentionedUsers(); // These two lines may included in every command method because of code efficiency and shorter code
        User targetUser; // the user that we want her information
        if (users.size() < 1) // if there's any mentioned users
            targetUser = command.event().getMessageAuthor().asUser().get();
        else
            targetUser = users.get(0); // if there's not any mentioned users then targetUser is message or command author
        message.reply(Command.embedBuilder(command.command(), command.event()).setDescription("Username: " + targetUser.getName() + "\nUser Nickname: " + (targetUser.getNickname(command.event().getServer().get()).isPresent() ? targetUser.getNickname(command.event().getServer().get()).get() : "NONE")
                + "\nUser ID: " + targetUser.getId() + "\nUser's Top Role: " + targetUser.getRoles(command.event().getServer().get()).get(targetUser.getRoles(command.event().getServer().get()).size() - 1).getMentionTag()).setColor(INFO));
    }

    /* Used for get bot invite link */
    public void commandInvite(CommandType command) {
        command.event().getMessage().reply(Command.embedBuilder(command.command(), command.event()).setDescription("JGuard bot invite link: " + command.api().createBotInvite(Permissions.fromBitmask(8 /*Should be 8 for administrator access*/))).setColor(INFO));
    }

    /* Used for kick users */
    public void commandKick(CommandType command) {
        var message = command.event().getMessage();
        var embedBuilder = Command.embedBuilder(command.command(), command.event());
        var user = message.getMentionedUsers();
        if (user.size() < 1)
            message.reply(embedBuilder.setDescription("Please specify a user.").setColor(ERROR));
        else if (!command.event().getMessageAuthor().canKickUserFromServer(user.get(0))) // Message or command author's access check for the specified user
            message.reply(embedBuilder.setDescription("You haven't permission to kick " + user.get(0).getMentionTag()).setColor(ACCESS_DENIED));
        else {
            command.event().getServer().get().kickUser(user.get(0));
            message.reply(embedBuilder.setDescription(user.get(0).getMentionTag() + " has been kicked.").setColor(DONE));
        }
    }

    /* Used for ban users */
    public void commandBan(CommandType command) {
        var message = command.event().getMessage();
        var embedBuilder = Command.embedBuilder(command.command(), command.event());
        var user = message.getMentionedUsers();
        if (user.size() < 1) message.reply(embedBuilder.setDescription("Please specify a user.").setColor(ERROR));
        else if (!command.event().getMessageAuthor().canBanUserFromServer(user.get(0)))
            message.reply(embedBuilder.setDescription("You haven't permission to ban " + user.get(0).getMentionTag()).setColor(ACCESS_DENIED));
        else {
            command.event().getServer().get().banUser(user.get(0));
            message.reply(embedBuilder.setDescription(user.get(0).getMentionTag() + " has been banned.").setColor(DONE));
        }
    }

    /* Used for server mute users */
    public void commandMute(CommandType command) {
        var message = command.event().getMessage();
        var embedBuilder = Command.embedBuilder(command.command(), command.event());
        var user = message.getMentionedUsers();
        if (user.size() < 1) message.reply(embedBuilder.setDescription("Please specify a user.").setColor(ERROR));
        else if (!command.event().getMessageAuthor().canMuteMembersOnServer()) // Message or command author's access check for the all users
            message.reply(embedBuilder.setDescription("You haven't permission to mute members.").setColor(ACCESS_DENIED));
        else {
            command.event().getServer().get().muteUser(user.get(0));
            message.reply(embedBuilder.setDescription(user.get(0).getMentionTag() + " has been muted.").setColor(DONE));
        }
    }

    /* Opposite of mute command*/
    public void commandUnMute(CommandType command) {
        var message = command.event().getMessage();
        var embedBuilder = Command.embedBuilder(command.command(), command.event());
        var user = message.getMentionedUsers();
        if (user.size() < 1) message.reply(embedBuilder.setDescription("Please specify a user.").setColor(ERROR));
        else if (!command.event().getMessageAuthor().canMuteMembersOnServer())
            message.reply(embedBuilder.setDescription("You haven't permission to mute or unmute members.").setColor(ACCESS_DENIED));
        else {
            command.event().getServer().get().unmuteUser(user.get(0));
            message.reply(embedBuilder.setDescription(user.get(0).getMentionTag() + " has been unmuted.").setColor(DONE));
        }
    }

    /* Used for server deafen users */
    public void commandDef(CommandType command) {
        var message = command.event().getMessage();
        var embedBuilder = Command.embedBuilder(command.command(), command.event());
        var user = message.getMentionedUsers();
        if (user.size() < 1) message.reply(embedBuilder.setDescription("Please specify a user.").setColor(ERROR));
        else if (!command.event().getMessageAuthor().canDeafenMembersOnServer())
            message.reply(embedBuilder.setDescription("You haven't permission to deafen members.").setColor(ACCESS_DENIED));
        else {
            command.event().getServer().get().deafenUser(user.get(0));
            message.reply(embedBuilder.setDescription(user.get(0).getMentionTag() + " has been deafened.").setColor(DONE));
        }
    }

    /* Opposite of deafen command */
    public void commandUnDef(CommandType command) {
        var message = command.event().getMessage();
        var embedBuilder = Command.embedBuilder(command.command(), command.event());
        var user = message.getMentionedUsers();
        if (user.size() < 1) message.reply(embedBuilder.setDescription("Please specify a user.").setColor(ERROR));
        else if (!command.event().getMessageAuthor().canDeafenMembersOnServer())
            message.reply(embedBuilder.setDescription("You haven't permission to deafen members.").setColor(ACCESS_DENIED));
        else {
            command.event().getServer().get().undeafenUser(user.get(0));
            message.reply(embedBuilder.setDescription(user.get(0).getMentionTag() + " has been undeafened.").setColor(DONE));
        }
    }

    /* Used for warn users */
    public void commandWarn(CommandType command) {
        var message = command.event().getMessage();
        var embedBuilder = Command.embedBuilder(command.command(), command.event());
        var users = message.getMentionedUsers();
        var user = new UserSingleType(users.get(0), command.event().getServer().get()); // See UserWarnType.java & UserSingleType.java
        if (users.size() < 1) message.reply(embedBuilder.setDescription("Please specify a user.").setColor(ERROR));
        else if (!command.event().getMessageAuthor().canKickUserFromServer(users.get(0)))
            message.reply(embedBuilder.setDescription("You haven't permission to warn " + users.get(0).getMentionTag()).setColor(ACCESS_DENIED));
        else {
            warn.warn(user);
            int warns = warn.getUser(user).warns;
            if (warns <= warn.limit)
                message.reply(embedBuilder.setDescription(users.get(0).getMentionTag() + " has been warned. (" + warns + '/' + warn.limit + ')').setColor(DONE));
            else {
                String action;
                var server = command.event().getServer().get();
                if (warn.action == 0) {
                    server.banUser(users.get(0));
                    action = "banned";
                } else {
                    server.kickUser(users.get(0));
                    action = "kicked";
                }
                server.kickUser(users.get(0));
                message.reply(embedBuilder.setDescription(users.get(0).getMentionTag() + " has been " + action + ". Passed (" + warn.limit + '/' + warn.limit + ") warnings.")
                        .setColor(DONE));
                warn.rem_warn(user);
            }
        }
    }

    /* Opposite of the warn command */
    public void commandUnWarn(CommandType command) {
        var message = command.event().getMessage();
        var embedBuilder = Command.embedBuilder(command.command(), command.event());
        var users = message.getMentionedUsers();
        var user = new UserSingleType(users.get(0), command.event().getServer().get());
        if (users.size() < 1) message.reply(embedBuilder.setDescription("Please specify a user.").setColor(ERROR));
        else if (!command.event().getMessageAuthor().canBanUserFromServer(users.get(0)))
            message.reply(embedBuilder.setDescription("You haven't permission to unwarn " + users.get(0).getMentionTag()).setColor(ACCESS_DENIED));
        else {
            warn.unwarn(user);
            if (warn.getUser(user) == null) {
                message.reply(embedBuilder.setDescription(users.get(0).getMentionTag() + " is clear.").setColor(ALREADY_DONE));
                return;
            }
            int warns = warn.getUser(user).warns;
            if (warns >= 1)
                message.reply(embedBuilder.setDescription(users.get(0).getMentionTag() + " has been unwarned. (" + warns + '/' + warn.limit + ')').setColor(DONE));
            else {
                message.reply(embedBuilder.setDescription(users.get(0).getMentionTag() + " has been cleared. (" + 0 + '/' + warn.limit + ") warnings.").setColor(DONE));
                warn.rem_warn(user);
            }
        }
    }

    /* Removes the specified user from warned-users list */
    public void commandClear(CommandType command) {
        var message = command.event().getMessage();
        var embedBuilder = Command.embedBuilder(command.command(), command.event());
        var users = command.event().getMessage().getMentionedUsers();
        var user = new UserSingleType(users.get(0), command.event().getServer().get());
        if (users.size() < 1) message.reply(embedBuilder.setDescription("Please specify a user.").setColor(ERROR));
        else if (!command.event().getMessageAuthor().canBanUserFromServer(users.get(0)))
            message.reply(embedBuilder.setDescription("You haven't permission to clear " + users.get(0).getMentionTag()).setColor(ACCESS_DENIED));
        else {
            warn.unwarn(user);
            if (warn.getUser(user) == null) {
                message.reply(embedBuilder.setDescription(users.get(0).getMentionTag() + " is clear.").setColor(ALREADY_DONE));
                return;
            }
            message.reply(embedBuilder.setDescription(users.get(0).getMentionTag() + " has been cleared.").setColor(DONE));
            warn.rem_warn(user);
        }
    }

    /* Used for lock a user */
    public void commandLock(CommandType command, Lock lockType) {
        var message = command.event().getMessage();
        var embedBuilder = Command.embedBuilder(command.command(), command.event());
        var users = command.event().getMessage().getMentionedUsers();
        if (users.size() < 1)
            message.reply(embedBuilder.setDescription("Please specify a user.").setColor(ERROR));
        else if (!command.event().getMessageAuthor().canKickUserFromServer(users.get(0)))
            message.reply(embedBuilder.setDescription("You cannot lock " + users.get(0).getMentionTag()).setColor(ACCESS_DENIED));
        else if (lockType.lock(new UserSingleType(users.get(0), command.event().getServer().get())))
            message.reply(embedBuilder.setDescription(users.get(0).getMentionTag() + " has been locked.").setColor(DONE));
        else
            message.reply(embedBuilder.setDescription(users.get(0).getMentionTag() + " is already locked.").setColor(ALREADY_DONE));
    }

    /* Opposite of the lock command */
    public void commandUnlock(CommandType command, Lock lockType) {
        var message = command.event().getMessage();
        var embedBuilder = Command.embedBuilder(command.command(), command.event());
        var users = command.event().getMessage().getMentionedUsers();
        if (users.size() < 1)
            message.reply(embedBuilder.setDescription("Please specify a user.").setColor(ERROR));
        else if (!command.event().getMessageAuthor().canKickUserFromServer(users.get(0)))
            message.reply(embedBuilder.setDescription("You cannot unlock " + users.get(0).getMentionTag()).setColor(ACCESS_DENIED));
        else if (lockType.unlock(new UserSingleType(users.get(0), command.event().getServer().get())))
            message.reply(embedBuilder.setDescription(users.get(0).getMentionTag() + " has been unlocked.").setColor(DONE));
        else
            message.reply(embedBuilder.setDescription(users.get(0).getMentionTag() + " is already unlocked.").setColor(ALREADY_DONE));
    }

    /* Used for delete messages (No argument for delete as possible (Usually 100))*/
    public void commandDelete(CommandType command) throws ExecutionException, InterruptedException {
        var embedBuilder = Command.embedBuilder(command.command(), command.event());
        var message = command.event().getMessage();
        var channel = command.event().getChannel();
        if (!command.event().getMessageAuthor().canCreateChannelsOnServer())
            message.reply(embedBuilder.setDescription("You cannot delete messages.").setColor(ACCESS_DENIED));
        else {
            int messages_size = channel.getMessages(0).get().size(), deleted = messages_size;
            if (command.args() == null) channel.getMessages(0).get().deleteAll();
            else {
                int specified_size = Math.min(Integer.parseInt(command.args()[0]), messages_size);
                channel.getMessages(specified_size).get().deleteAll();
                deleted = specified_size;
            }
            channel.sendMessage(embedBuilder.setDescription(deleted + " messages has been deleted.").setColor(DONE));
        }
    }

    /* Used for channel-lock a user */
    public void commandChLock(CommandType command, Lock lockType) {
        var embedBuilder = Command.embedBuilder(command.command(), command.event());
        var message = command.event().getMessage();
        var users = command.event().getMessage().getMentionedUsers();
        if (users.size() < 1)
            message.reply(embedBuilder.setDescription("Please specify a user.").setColor(ERROR));
        else if (!command.event().getMessageAuthor().canKickUserFromServer(users.get(0)))
            message.reply(embedBuilder.setDescription("You cannot channel-lock " + users.get(0).getMentionTag()).setColor(ACCESS_DENIED));
        else if (lockType.chLock(new UserChannelLockType(new UserSingleType(users.get(0), command.event().getServer().get()), command.event().getChannel())))
            message.reply(embedBuilder.setDescription(users.get(0).getMentionTag() + " has been channel-locked.").setColor(DONE));
        else
            message.reply(embedBuilder.setDescription(users.get(0).getMentionTag() + " is already channel-locked.").setColor(ALREADY_DONE));
    }

    /* Opposite of the chlock command */
    public void commandChUnlock(CommandType command, Lock lockType) {
        var embedBuilder = Command.embedBuilder(command.command(), command.event());
        var message = command.event().getMessage();
        var users = command.event().getMessage().getMentionedUsers();
        if (users.size() < 1)
            message.reply(embedBuilder.setDescription("Please specify a user.").setColor(ERROR));
        else if (!command.event().getMessageAuthor().canKickUserFromServer(users.get(0)))
            message.reply(embedBuilder.setDescription("You cannot channel-unlock " + users.get(0).getMentionTag()).setColor(ACCESS_DENIED));
        else if (lockType.chUnlock(new UserChannelLockType(new UserSingleType(users.get(0), command.event().getServer().get()), command.event().getChannel())))
            message.reply(embedBuilder.setDescription(users.get(0).getMentionTag() + " has been channel-unlocked.").setColor(DONE));
        else
            message.reply(embedBuilder.setDescription(users.get(0).getMentionTag() + " is already channel-unlocked.").setColor(DONE));
    }

    /* Unbans all banned users */
    public void commandUnBanAll(CommandType command) throws ExecutionException, InterruptedException {
        var embedBuilder = Command.embedBuilder(command.command(), command.event());
        var message = command.event().getMessage();
        var bannedUsers = command.event().getServer().get().getBans().get();
        if (!command.event().getMessageAuthor().canBanUsersFromServer())
            message.reply(embedBuilder.setDescription("You cannot unban users.").setColor(ACCESS_DENIED));
        else if (bannedUsers.size() < 1)
            message.reply(embedBuilder.setDescription("There's no banned users in this server.").setColor(ERROR));
        else {
            bannedUsers.forEach(pre -> pre.getServer().unbanUser(pre.getUser()));
            message.reply(embedBuilder.setDescription("All users has been unbanned.").setColor(DONE));
        }
    }

    /* Unlocks all locked users */
    public void commandUnlockAll(CommandType command, Lock lockType) {
        var embedBuilder = Command.embedBuilder(command.command(), command.event());
        var message = command.event().getMessage();
        if (!command.event().getMessageAuthor().canKickUsersFromServer())
            message.reply(embedBuilder.setDescription("You cannot unlock users.").setColor(ACCESS_DENIED));
        else if (lockType.users.size() < 1 && lockType.channelUsers.size() < 1)
            message.reply(embedBuilder.setDescription("There's no locked user.").setColor(ERROR));
        else {
            if (lockType.users.size() > 0)
                lockType.users.clear();
            if (lockType.channelUsers.size() > 0)
                lockType.channelUsers.clear();
            message.reply(embedBuilder.setDescription("All locked users has been unlocked.").setColor(DONE));
        }
    }

    /* Settings and options of commands*/
    public void commandSet(CommandType command) {
        var embedBuilder = Command.embedBuilder(command.command(), command.event());
        var message = command.event().getMessage();
        if (command.args().length < 1)
            message.reply(embedBuilder.setDescription("You should specify an option.").setColor(ERROR));
        else if (command.args()[1].equals("warn"))
            warn.parseArg(command);
        // and the options you want to add but i only prefer to add 1 option to simplify the code (or maybe too lazy)
    }
    public void commandHelp(CommandType command){
        command.event().getMessage().reply(Command.embedBuilder(command.command(), command.event()).setDescription(
                """
                        Welcome ot JGuard Version 1.0 (The UnLicence (BSD0)) (Java Version)
                        Source Code: https://github.com/delta-two/JGuard
                        JGuard commands:
                        Information:
                            ?ping : Used for test the bot.
                            ?server : Used for get server information.
                            ?inf : Used for get your information.
                            ?inf @USER : Used for get mentioned user's information.
                            ?help : Shows this message.
                        Administrative:
                            ?kick @USER : Used for kick the mentioned user.
                            ?ban @USER : Used for ban the mentioned user.
                            ?warn @USER : Used for warn the mentioned user.
                            ?unwarn @USER : Used for unwarn the mentioned user.
                            ?lock @USER : Used for lock the mentioned user (mentioned user cannot send any messages at all (the message will be deleted by bot)).
                            ?unlock @USER : Used for unlock the mentioned user.
                            ?chlock @USER : Used for lock the mentioned user at current channel.
                            ?chunlock @USER : Used for unlock the mentioned user at current channel (if locked by chlock).
                            ?def @USER : Used for deafen the mentioned user. (Simplified as 'def')
                            ?undef @USER : Used for undeafen the mentioned user.
                            ?mute @USER : Used for mute the mentioned user.
                            ?unmute @USER : Used for unmute the mentioned user.
                            ?set COMMAND OPTION VALUE : Used for change settings of a command
                            Next Update:
                                ?auto-delete (on/off) : Turn auto message delete on or off.
                                ?unlock this @USER : Unlocks a locked user only for current channel.
                                ?lock this @USER : Locks a user only for current channel.
                                ?chlock will be removed.
                                ?chunlock will be removed.
                                ?ban @USER TIME(hours) : Scheduled ban.
                                ?lock @USER TIME(MINUTES) : Scheduled lock.
                                ?lock this @USER TIME(MINUTES) : Scheduled channel lock.
                                ?nick @USER : Resets user's nickname.
                                ?nick @USER NEW_NAME : Changes user's nickname.
                                and many new features.
                            Settings:
                                ?set warn OPTION VALUE : Used for change the warning settings
                                Warning Settings:
                                    ?set warn limit NUMBER : Used for change the warning limit
                                    ?set warn action (ban,kick) : Used for change the action while a user passed warning limit.
                                Ban Settings:
                                    Next Update.
                                Kick Settings:
                                    Next Update.
                                Lock Settings:
                                    Next Update.
                                Auto-Delete Settings:
                                    Next Update.
                """
        ).setColor(INFO));
    }
}
