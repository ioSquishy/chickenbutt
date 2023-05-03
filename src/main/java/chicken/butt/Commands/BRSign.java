package chicken.butt.Commands;

import org.javacord.api.interaction.SlashCommandBuilder;

public class BRSign {
	
    public SlashCommandBuilder createPeerexCmd() {
        return new SlashCommandBuilder()
            .setName("peerex")
            .setDescription("you need to pee RN!! 🦖")
            .setDefaultEnabledForEveryone()
			.setEnabledInDms(false);
    }
}