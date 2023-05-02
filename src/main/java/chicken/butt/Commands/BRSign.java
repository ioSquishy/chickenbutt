package chicken.butt.Commands;

import org.javacord.api.interaction.SlashCommandBuilder;

public class BRSign {
    public SlashCommandBuilder createPeerexCmd() {
        return new SlashCommandBuilder()
            .setName("peerex")
            .setDescription("you need to pee RN!! 🦖")
            .setDefaultEnabledForEveryone();
    }

	public SlashCommandBuilder createSignInCmd() {
		return new SlashCommandBuilder()
			.setName("signin")
			.setDescription("back from the bathroom :D")
			.setDefaultEnabledForEveryone();
	}

	public SlashCommandBuilder createSignOutCmd() {
		return new SlashCommandBuilder()
			.setName("signout")
			.setDescription("going to the bathroom :O")
			.setDefaultEnabledForEveryone();
	}

    public void runCmd(String userID) {
        
    }

}