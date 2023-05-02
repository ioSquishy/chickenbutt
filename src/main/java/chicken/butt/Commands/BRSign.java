package chicken.butt.Commands;

import org.javacord.api.interaction.SlashCommandBuilder;

public class BRSign {
    public SlashCommandBuilder createCmd() {
        return new SlashCommandBuilder()
            .setName("peerex")
            .setDescription("you need to pee 🦖")
            .setDefaultEnabledForEveryone();
    }

    public void runCmd(String userID) {
        
    }

}