import java.util.Vector;
import java.util.stream.IntStream;

public class Warn {
    /*
    * Used for warn, unwarn and clear users.
    * the limit can be changed dynamically with another command (i wrote that "set") or statically from the code
    * */
    public Short limit;
    public Short action = 0; // 0 for ban 1 for kick
    public Vector<UserWarnType> users;
    public Warn(Short limit){
        this.limit = limit;
        users = new Vector<>();
    }
    public void warn(UserSingleType user){ // warn users
        for(var user_ : users)
            if (user_.userSingleType.equals(user)) {
                user_.warns++;
                return;
            }
        users.add(new UserWarnType(new UserSingleType(user.user(), user.server())));
    }
    public void unwarn(UserSingleType user){ // unwarn or decrement a warning from a user
        users.stream().filter(user_ -> user_.userSingleType.equals(user)).findFirst().ifPresent(user_ -> user_.warns--);
    }
    public void rem_warn(UserSingleType user){ // clears warnings of a user (remove the user from users vector)
        IntStream.range(0, users.size()).filter(i -> users.get(i).userSingleType.equals(user)).findFirst().ifPresent(i -> users.remove(i));
    }

    public UserWarnType getUser(UserSingleType user){ // returns the specified user from the list or returns null if no one found
        return users.stream().filter(user_ -> user_.userSingleType.equals(user)).findFirst().orElse(null);
    }
    public void parseArg(CommandType command){ // used for 'set' command or change options
        var embedBuilder = Command.embedBuilder(command.command(), command.event());
        var message = command.event().getMessage();
        if(!command.event().getMessageAuthor().isServerAdmin()) // for change warning settings, command author needs administrator access
            message.reply(embedBuilder.setDescription("You cannot change Warning settings.").setColor(CommandActions.ACCESS_DENIED));
        else if(command.args().length < 3)
            message.reply(embedBuilder.setDescription("You should specify an option for warning settings.").setColor(CommandActions.ERROR));
        else if(command.args().length < 4)
            message.reply(embedBuilder.setDescription("You should specify a value.").setColor(CommandActions.ERROR));
        else{
            String arg2 = command.args()[2], arg3 = command.args()[3];
            if(arg2.equalsIgnoreCase("limit")){ // changes the warning limit value
                try {
                    limit = Short.parseShort(arg3);
                    message.reply(embedBuilder.setDescription("Warning limit has been set to " + limit).setColor(CommandActions.DONE));
                }catch (NumberFormatException exception){
                    message.reply(embedBuilder.setDescription("Input value must be a Number from 0 to " + Short.MAX_VALUE).setColor(CommandActions.ERROR));
                }
            } else if(arg2.equalsIgnoreCase("action")){ // changes the action that performs after target user passes the last warning
                if(arg3.equalsIgnoreCase("ban"))
                    action = 0;
                else if(arg3.equalsIgnoreCase("kick"))
                    action = 1;
                else{
                    message.reply(embedBuilder.setDescription("Invalid value.").setColor(CommandActions.ERROR));
                    return;
                }
                message.reply(embedBuilder.setDescription("Passing last-warning action has changed to " + arg3.toUpperCase()).setColor(CommandActions.DONE));
            }else
                message.reply(embedBuilder.setDescription("Invalid option.").setColor(CommandActions.ERROR));
        }
    }
}
