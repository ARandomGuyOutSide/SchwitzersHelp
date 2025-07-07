package util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.polyfrost.schwitzersHelp.config.SchwitzerHelpConfig;

public class Chat {

    private static Minecraft mc = Minecraft.getMinecraft();
    private static SchwitzerHelpConfig config = SchwitzerHelpConfig.getInstance();

    public static void formatedChatMessage(String message) {
        mc.thePlayer.addChatMessage(new ChatComponentText("§0[§1SchwitzersHelp§0]§f >> " + message));
    }

    public static void debugMessage(String message) {
        if (config.isDebugMode())
            mc.thePlayer.addChatMessage(new ChatComponentText("§0[§1SchwitzersHelp§0]§f (DEBUG) >> " + message));
    }

    public static void sendMessage(String message)
    {
        mc.thePlayer.sendChatMessage(message);
    }

}
