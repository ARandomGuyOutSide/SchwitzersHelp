package com.schwitzer.schwitzersHelp.bedwars;

import cc.polyfrost.oneconfig.config.core.OneColor;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import com.schwitzer.schwitzersHelp.config.SchwitzerHelpConfig;

import java.util.Collection;

public class PlayerLinesMod {
    public static final String MODID = "villagerlines";
    public static final String VERSION = "1.0";

    private SchwitzerHelpConfig config;

    boolean playerLines;
    OneColor playerLinesColor;
    boolean necron_boss_highlit;
    OneColor necron_line_color;
    private boolean dragonLine;
    private OneColor dragonLineColor;

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        config = SchwitzerHelpConfig.getInstance();

        playerLines = config.player_lines;
        playerLinesColor = config.player_lines_color;
        dragonLine = config.isDragon_line();
        dragonLineColor = config.getDragon_line_color();

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null || mc.thePlayer == null) return;

        // Hole die Position des Spielers
        double posX = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * event.partialTicks;
        double posY = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * event.partialTicks;
        double posZ = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * event.partialTicks;

        // Farbe aus der Konfiguration

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glLineWidth(2.0F); // Dicke der Linie

        // Zeichne Spielerlinien, wenn player_lines aktiv ist
        if (playerLines) {

            float red = playerLinesColor.getRed() / 255.0f;
            float green = playerLinesColor.getGreen() / 255.0f;
            float blue = playerLinesColor.getBlue() / 255.0f;
            float alpha = playerLinesColor.getAlpha() / 255.0f;

            GlStateManager.color(red, green, blue, alpha);

            Collection<NetworkPlayerInfo> tabListPlayers = mc.getNetHandler().getPlayerInfoMap();

            for (EntityPlayer player : mc.theWorld.playerEntities) {
                if (player == mc.thePlayer) continue;

                boolean isInTabList = tabListPlayers.stream()
                        .anyMatch(info -> info.getGameProfile().getId().equals(player.getGameProfile().getId()));

                if (!isInTabList) continue;

                String playerName = player.getDisplayName().getUnformattedText();
                if (playerName.startsWith("[NPC]")) continue;

                Vec3 playerPos = new Vec3(
                        player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks - posX,
                        player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks - posY,
                        player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks - posZ
                );

                GL11.glBegin(GL11.GL_LINES);
                GL11.glVertex3d(0, mc.thePlayer.getEyeHeight(), 0);
                GL11.glVertex3d(playerPos.xCoord, playerPos.yCoord + player.getEyeHeight(), playerPos.zCoord);
                GL11.glEnd();
            }
        }
        if (dragonLine) {
            // Setze die Farbe für die Drachenlinie
            float red = dragonLineColor.getRed() / 255.0f;
            float green = dragonLineColor.getGreen() / 255.0f;
            float blue = dragonLineColor.getBlue() / 255.0f;
            float alpha = dragonLineColor.getAlpha() / 255.0f;

            GlStateManager.color(red, green, blue, alpha);

            // Suche alle Drachen in der Welt
            mc.theWorld.loadedEntityList.stream()
                    .filter(entity -> entity instanceof net.minecraft.entity.boss.EntityDragon)
                    .forEach(entity -> {
                        net.minecraft.entity.boss.EntityDragon dragon = (net.minecraft.entity.boss.EntityDragon) entity;

                        // Berechne die Position des Drachen relativ zum Spieler
                        Vec3 dragonPos = new Vec3(
                                dragon.lastTickPosX + (dragon.posX - dragon.lastTickPosX) * event.partialTicks - posX,
                                dragon.lastTickPosY + (dragon.posY - dragon.lastTickPosY) * event.partialTicks - posY,
                                dragon.lastTickPosZ + (dragon.posZ - dragon.lastTickPosZ) * event.partialTicks - posZ
                        );

                        // Zeichne die Linie vom Spieler zum Drachen
                        GL11.glBegin(GL11.GL_LINES);
                        GL11.glVertex3d(0, mc.thePlayer.getEyeHeight(), 0); // Startpunkt: Spieler
                        GL11.glVertex3d(dragonPos.xCoord, dragonPos.yCoord + dragon.getEyeHeight() - dragon.height / 2, dragonPos.zCoord); // Endpunkt: Drache
                        GL11.glEnd();
                    });
        }


        /*
        // Zeichne Linie zum Wither, wenn necron_boss_highlit aktiv ist
        if (necron_boss_highlit) {

            float red = necron_line_color.getRed() / 255.0f;
            float green = necron_line_color.getGreen() / 255.0f;
            float blue = necron_line_color.getBlue() / 255.0f;
            float alpha = necron_line_color.getAlpha() / 255.0f;

            GlStateManager.color(red, green, blue, alpha);

            mc.theWorld.loadedEntityList.stream()
                    .filter(entity -> entity instanceof net.minecraft.entity.boss.EntityWither)
                    .forEach(entity -> {
                        net.minecraft.entity.boss.EntityWither wither = (net.minecraft.entity.boss.EntityWither) entity;

                        // Überprüfung, ob der Boss den gewünschten Namen hat
                        String bossName = wither.getDisplayName().getUnformattedText(); // Name des Bosses abrufen
                        if (!bossName.equals("Storm") && !bossName.equals("Necron") &&
                                !bossName.equals("Maxor") && !bossName.equals("Goldor")) {
                            return; // Linien nur zeichnen, wenn der Name passt
                        }

                        Vec3 witherPos = new Vec3(
                                wither.lastTickPosX + (wither.posX - wither.lastTickPosX) * event.partialTicks - posX,
                                wither.lastTickPosY + (wither.posY - wither.lastTickPosY) * event.partialTicks - posY,
                                wither.lastTickPosZ + (wither.posZ - wither.lastTickPosZ) * event.partialTicks - posZ
                        );

                        GL11.glBegin(GL11.GL_LINES);
                        GL11.glVertex3d(0, mc.thePlayer.getEyeHeight(), 0);
                        GL11.glVertex3d(witherPos.xCoord, witherPos.yCoord + wither.getEyeHeight(), witherPos.zCoord);
                        GL11.glEnd();
                    });
                    }
         */



        // Setze die OpenGL-Einstellungen zurück
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

}

