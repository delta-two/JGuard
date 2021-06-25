import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

public record UserSingleType(User user, Server server){}
/*
* A record or data class used for separate users
* from their servers.
* as you know or not, each User class points to a user in whole (Discord) not in a server.
* so we have to separate each User and Server to prevent multi-server actions.
* */