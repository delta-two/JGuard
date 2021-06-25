import java.util.Vector;

public class Lock {
    Vector<UserSingleType> users = new Vector<>();
    Vector<UserChannelLockType> channelUsers = new Vector<>();
    public boolean lock(UserSingleType user){
        for(var user_: users) if (user_.equals(user)) return false;
        users.add(user);
        return true;
    }
    public boolean unlock(UserSingleType user){
        for(var user_: users)
            if (user_.equals(user)) {
                users.remove(user_);
                return true;
            }
        return false;
    }
    public boolean isLocked(UserSingleType user){
        for(var user_: users) if (user_.equals(user)) return true;
        return false;
    }
    public boolean chLock(UserChannelLockType user){
        for(var user_: channelUsers) if (user_.equals(user)) return false;
        channelUsers.add(user);
        return true;
    }
    public boolean chUnlock(UserChannelLockType user){
        for(var user_: channelUsers){
            if(user_.equals(user)){
                channelUsers.remove(user_);
                return true;
            }
        }
        return false;
    }
    public boolean isChLocked(UserChannelLockType user){
        for(var user_: channelUsers) if (user_.equals(user)) return true;
        return false;
    }
}
