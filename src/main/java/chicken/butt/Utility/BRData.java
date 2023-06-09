package chicken.butt.Utility;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class BRData implements Serializable {
    private static final long serialVersionUID = 2;

    private long epochId;
    private long userID;
    private ZonedDateTime signOut = null;
    private ZonedDateTime signIn = null;
    private int breakLength;

    public BRData (long userID) {
        epochId = Instant.now().toEpochMilli();
        this.userID = userID;
        breakLength = 0;
    }

    public long getEpochID() {
        return epochId;
    }

    public long getUserID() {
        return userID;
    }

    public void signOut() {
        signOut = ZonedDateTime.now(ZoneId.of("America/Los_Angeles"));
    }

    public void signIn() {
        signIn = ZonedDateTime.now(ZoneId.of("America/Los_Angeles"));
        breakLength = (int) (signIn.toEpochSecond() - signOut.toEpochSecond());
        System.out.println(signOut.toEpochSecond() + " -> " + signIn.toEpochSecond() + " = " + breakLength); //
    }

    public ZonedDateTime getSignOutTime() {
        return signOut;
    }
    public ZonedDateTime getSignInTime() {
        return signIn;
    }

    public int getBreakLength() {
        return breakLength;
    }
}
