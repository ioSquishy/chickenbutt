package chicken.butt.Commands;

import org.javacord.api.interaction.SlashCommandBuilder;

public class GetDadJoke {
    public static SlashCommandBuilder createDadJokeCmd() {
        return new SlashCommandBuilder()
            .setName("dadjoke")
            .setDescription("when ur not funni enough")
            .setDefaultEnabledForEveryone()
			.setEnabledInDms(true);
    }
}
