package chicken.butt.Commands;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.interaction.SlashCommandBuilder;

import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

public class DownloadData {
    public static SlashCommandBuilder createCmd() {
        return new SlashCommandBuilder()
            .setName("downloaddata")
            .setDescription("Downloads all data in an excel.")
            .setDefaultEnabledForEveryone()
			.setEnabledInDms(false);
    }

    public static void getDownload(TextChannel channel) {


        //channel.sendMessage(Inputstream, "data");
    }
}
