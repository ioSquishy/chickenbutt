package chicken.butt;

import java.util.concurrent.ExecutionException;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;

import chicken.butt.Commands.BRSign;
import chicken.butt.Utility.Data;
import io.github.cdimascio.dotenv.Dotenv;

public class App {
    public static final DiscordApi api = new DiscordApiBuilder().setToken(Dotenv.load().get("KANNA_TOKEN")).setAllNonPrivilegedIntentsAnd(Intent.MESSAGE_CONTENT, Intent.DIRECT_MESSAGES).login().join();
    
    public static void main( String[] args )
    {
        System.out.println("logged in :O");
        //init data
        new Data();

        // slash commands
        long climbMaxing = 1069042405388583053L;
        new BRSign().createPeerexCmd().createForServer(api, climbMaxing).join();

        // listeners
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

        api.addSlashCommandCreateListener(event -> {
            String cmd = event.getSlashCommandInteraction().getCommandName();
            switch (cmd) {
                case "peerex" :
                    Data.brbUpdate(event.getInteraction().getUser().getId());
                    break;
            }
        });

        api.addButtonClickListener(event -> {
            String id = event.getButtonInteraction().getIdAsString();
            switch (id) {
                case "peerex" :
                    Data.brbUpdate(event.getInteraction().getUser().getId());
                    break;
            }
        });
    }
}
