package chicken.butt;

import java.util.concurrent.ExecutionException;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;

import chicken.butt.Commands.BRSign;

public class App 
{
    public static final DiscordApi api = new DiscordApiBuilder().setToken("MTEwMjEwMDU4NjExMzAyODEzOA.GcNK8Y.x802rtYDzgHfp593rUeU89UKcTj-N1Aov19Cuc").setAllNonPrivilegedIntentsAnd(Intent.MESSAGE_CONTENT, Intent.DIRECT_MESSAGES).login().join();
    public static void main( String[] args )
    {
        System.out.println("logged in :O");

        long climbMaxing = 1069042405388583053L;
        new BRSign().createCmd().createForServer(api, climbMaxing).join();

        api.addMessageCreateListener(event -> {
            if (event.getMessage().getContent().toLowerCase().contains("what")) {
                try {
                    event.getMessage().reply("chicken butt").get();
                } catch (Error | InterruptedException | ExecutionException e) {
                    try {
                        event.getMessageAuthor().asUser().get().sendMessage("chicken butt").get();
                    } catch (Error | InterruptedException | ExecutionException ee) {}
                }
            }
        });
    }
}
