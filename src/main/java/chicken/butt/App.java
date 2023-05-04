package chicken.butt;

import java.util.concurrent.ExecutionException;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.message.MessageFlag;

import chicken.butt.Commands.BRSheet;
import chicken.butt.Commands.BRSign;
import chicken.butt.Commands.ChickenButtRanks;
import chicken.butt.Utility.Data;
import io.github.cdimascio.dotenv.Dotenv;

public class App {
    public static final DiscordApi api = new DiscordApiBuilder().setToken(Dotenv.load().get("TOKEN")).setAllNonPrivilegedIntentsAnd(Intent.MESSAGE_CONTENT, Intent.DIRECT_MESSAGES).login().join();
    
    public static void main( String[] args )
    {
        System.out.println("logged in :O");
        //init data
        new Data();

        // slash commands
        long climbMaxing = 1069042405388583053L;
        BRSign.createPeerexCmd().createForServer(api, climbMaxing).join();
        BRSheet.createCmd().createForServer(api, climbMaxing).join();
        ChickenButtRanks.createCmd().createForServer(api, climbMaxing).join();

        // listeners
        api.addMessageCreateListener(event -> {
            String msg = event.getMessage().getContent().toLowerCase();

            if (msg.contains("what") || msg.contains("wut")) {
                try {
                    if (msg.contains("what")) {
                        event.getMessage().reply("chicken butt").get();
                    } else {
                        event.getMessage().reply("chicken but").get();
                    }
                    Data.addChickenButt(event.getMessageAuthor().getId());
                } catch (Error | InterruptedException | ExecutionException e) {
                    try {
                        if (msg.contains("what")) {
                            event.getMessageAuthor().asUser().get().sendMessage("chicken butt").get();
                        } else {
                            event.getMessageAuthor().asUser().get().sendMessage("chicken but").get();
                        }
                    } catch (Error | InterruptedException | ExecutionException ee) {}
                }
            }
            
            switch (msg) {
                case "/chickenbutts" :
                    event.getMessage().reply(ChickenButtRanks.createEmbed()).join();
                    break;
                case "/peerex" :
                    Data.brbUpdate(event.getMessageAuthor().getId());
                    break;
                case "/downloaddata" :
                    break;
            }
            
        });

        api.addSlashCommandCreateListener(event -> {
            String cmd = event.getSlashCommandInteraction().getCommandName();
            switch (cmd) {
                case "peerex" :
                    Data.brbUpdate(event.getInteraction().getUser().getId());
                    event.getInteraction().createImmediateResponder().setContent("gogogogo").setFlags(MessageFlag.EPHEMERAL).respond().join();
                    break;
                case "createsheet" :
                    BRSheet.createEmbed(event.getInteraction().getChannel().get());
                    event.getInteraction().createImmediateResponder().setContent(":D").setFlags(MessageFlag.EPHEMERAL).respond().join();
                    break;
                case "chickenbutts" :
                    event.getInteraction().createImmediateResponder().addEmbed(ChickenButtRanks.createEmbed()).respond().join();
            }
        });

        api.addButtonClickListener(event -> {
            String id = event.getButtonInteraction().getCustomId();
            switch (id) {
                case "peerex" :
                    Data.brbUpdate(event.getInteraction().getUser().getId());
                    event.getButtonInteraction().acknowledge().join();
                    break;
                case "undo" :
                    Data.removeLastBRB(event.getInteraction().getUser().getId());
                    event.getButtonInteraction().acknowledge().join();
                    break;
            }
        });
    }
}
