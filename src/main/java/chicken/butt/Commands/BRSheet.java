package chicken.butt.Commands;

import java.awt.Color;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.ButtonStyle;

public class BRSheet {
    private static String msgID;

    public void createEmbed(TextChannel channel) {
        EmbedBuilder embed = new EmbedBuilder()
            .setTitle("Sign In/Out")
            .setDescription("Click the button to sign in and out!")
            .setColor(Color.YELLOW);
        
        msgID = new MessageBuilder()
            .setEmbed(embed)
            .addActionRow(Button.create("peerex", ButtonStyle.PRIMARY, "Sign In/Out"))
            .send(channel).join().getIdAsString();
    }

    public static String getMsgID() {
        return msgID;
    }
}
