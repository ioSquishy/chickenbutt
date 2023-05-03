package chicken.butt.Utility;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class BRData implements Serializable {
    private static final long serialVersionUID = 3;

    private long epochId;
    private ZonedDateTime signOut;
    private ZonedDateTime signIn;
    private int breakLength;

    public BRData () {
        epochId = Instant.now().toEpochMilli();
        breakLength = 0;
    }

    public long getEpochID() {
        return epochId;
    }

    public void signOut() {
        signOut = ZonedDateTime.now(ZoneId.of("America/Los_Angeles"));
    }

    public void signIn() {
        signIn = ZonedDateTime.now(ZoneId.of("America/Los_Angeles"));
        breakLength = (int) (signIn.toEpochSecond() - signOut.toEpochSecond());
    }

    public int getBreakLength() {
        return breakLength;
    }
}
