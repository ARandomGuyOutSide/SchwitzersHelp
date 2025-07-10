package com.schwitzer.schwitzersHelp.bedwars;

import cc.polyfrost.oneconfig.config.core.OneColor;
import net.minecraft.block.BlockBed;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.Mod;
import com.schwitzer.schwitzersHelp.config.SchwitzerHelpConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.schwitzer.schwitzersHelp.util.RenderUtil;

@Mod(modid = Wallhacks.MODID, version = Wallhacks.VERSION, name = Wallhacks.NAME)
public class Wallhacks {

    public static final String MODID = "@ID@";
    public static final String NAME = "Schwitzers help";
    public static final String VERSION = "@VER@";

    private SchwitzerHelpConfig config;

    FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
    private boolean entityESP;
    private OneColor playerESP_color;
    private boolean bedESP;
    private OneColor bed_ESP_color;
    boolean key_mob_esp;
    OneColor key_mob_esp_color;
    boolean playerLines;
    OneColor playerLinesColor;
    private boolean zombiesmode;
    private OneColor zombies_mode_mobs_color;
    private boolean dragonEsp;
    private OneColor dragonEspColor;
    private boolean bat_esp;
    private OneColor bat_esp_color;

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        config = SchwitzerHelpConfig.getInstance();

        entityESP = config.isEntitiy_ESP();
        bedESP = config.isBett_ESP();
        playerESP_color = config.playerESP_color();
        bed_ESP_color = config.getBed_ESP_color();
        key_mob_esp = config.getKey_mob_esp();
        key_mob_esp_color = config.getKey_mob_esp_color();
        playerLines = config.getPlayerLines();
        playerLinesColor = config.getPlayer_lines_color();
        zombiesmode = config.isZombie_esp();
        zombies_mode_mobs_color = config.getZombies_color();
        dragonEsp = config.isDragon_esp();
        dragonEspColor = config.getDragon_esp_color();
        bat_esp = config.isBat_esp();
        bat_esp_color = config.getBat_esp_color();

        Minecraft mc = Minecraft.getMinecraft();

        // Überprüfen, ob die Welt und der Spieler geladen sind
        if (mc.theWorld == null || mc.thePlayer == null) {
            return;
        }

        double partialTicks = event.partialTicks;
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GL11.glLineWidth(2.0F);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 0.0F, 0.0F, 1.0F);

        EntityPlayerSP entityPlayerSP = mc.thePlayer;
        double playerX = entityPlayerSP.posX + (entityPlayerSP.posX - entityPlayerSP.prevPosX) * partialTicks;
        double playerY = entityPlayerSP.posY + (entityPlayerSP.posY - entityPlayerSP.prevPosY) * partialTicks;
        double playerZ = entityPlayerSP.posZ + (entityPlayerSP.posZ - entityPlayerSP.prevPosZ) * partialTicks;

        // Welt relativ zur Spielerposition rendern
        GlStateManager.translate(-playerX, -playerY, -playerZ);

        if (entityESP || key_mob_esp || zombiesmode || dragonEsp || bat_esp) {
            for (Object playerObj : mc.theWorld.loadedEntityList) {
                if (playerObj instanceof EntityLivingBase) {
                    EntityLivingBase otherPlayer = (EntityLivingBase) playerObj;

                    // Den eigenen Spieler ignorieren
                    if (otherPlayer == entityPlayerSP) continue;

                    if (key_mob_esp) {
                        if (otherPlayer.hasCustomName()) {
                            String formattedName = otherPlayer.getDisplayName().getFormattedText();

                            if (formattedName.contains("✯")) {
                                RenderUtil.drawEntityBox(otherPlayer.posX, otherPlayer.posY - 2, otherPlayer.posZ, 0.5, 1.8, key_mob_esp_color, 0.0D);
                            }
                        }
                    }

                    if (entityESP) {
                        // Logik für Spieler
                        if (otherPlayer instanceof EntityPlayer) {
                            EntityPlayer otherEntityPlayer = (EntityPlayer) otherPlayer; // Typumwandlung zu EntityPlayer
                            boolean isInTabList = false;

                            // Get the player's name and check if it starts with "[NPC]"
                            String playerName = otherEntityPlayer.getDisplayName().getUnformattedText();
                            if (playerName.startsWith("[NPC]")) continue; // Skip players that start with "[NPC]"

                            for (NetworkPlayerInfo playerInfo : mc.getNetHandler().getPlayerInfoMap()) {
                                if (playerInfo.getGameProfile().getName().equals(otherEntityPlayer.getGameProfile().getName())) {
                                    isInTabList = true;
                                    break;
                                }
                            }

                            if (!isInTabList) continue;

                            OneColor color = playerESP_color; // Falls jeder eine eigene Farbe haben soll, kannst du das hier dynamisch setzen
                            RenderUtil.drawEntityBox(otherPlayer.posX, otherPlayer.posY, otherPlayer.posZ, 0.5, 1.8, color, 0.5F);

                            // 🛠 OpenGL-States speichern + Namen rendern
                            GlStateManager.pushMatrix();
                            GlStateManager.disableLighting();
                            GlStateManager.enableDepth();

                            double nameX = otherPlayer.posX;
                            double nameY = otherPlayer.posY + 2.2;
                            double nameZ = otherPlayer.posZ;
                            RenderUtil.drawNameTag(otherEntityPlayer.getName(), nameX, nameY, nameZ);

                            GlStateManager.popMatrix(); // OpenGL-States wiederherstellen ✅
                        }
                    }

                    if (zombiesmode) {
                        if (playerObj instanceof EntityZombie ||
                                playerObj instanceof EntityBlaze ||
                                playerObj instanceof EntityEndermite ||
                                playerObj instanceof EntitySkeleton ||
                                playerObj instanceof EntitySilverfish ||
                                playerObj instanceof EntityWolf) {

                            RenderUtil.drawEntityBox(((EntityCreature) playerObj).posX, ((EntityCreature) playerObj).posY, ((EntityCreature) playerObj).posZ, 0.5, ((EntityCreature) playerObj).height, zombies_mode_mobs_color, 0.0D);
                        }
                    }

                    if (dragonEsp) {
                        if(playerObj instanceof EntityDragon)
                        {
                            EntityDragon dragon = (EntityDragon) playerObj;

                            // Box zeichnen
                            RenderUtil.drawEntityBox(dragon.posX, dragon.posY, dragon.posZ, dragon.width / 2.0, dragon.height, dragonEspColor, 0.0D);
                        }
                    }

                    if(bat_esp) {

                        if (playerObj instanceof EntityBat) {
                            EntityBat bat = (EntityBat) playerObj;

                            // Box zeichnen
                            RenderUtil.drawEntityBox(bat.posX, bat.posY, bat.posZ, bat.width, bat.height, bat_esp_color, 0.0D);
                        }
                    }
               }
            }
        }

        // Zeichne die Boxen um Betten, wenn Bett-ESP aktiviert ist
        if (bedESP) {
            int scanRadius = 20; // Auf 20 Blöcke begrenzt

            for (int x = -scanRadius; x <= scanRadius; x++) {
                for (int z = -scanRadius; z <= scanRadius; z++) {
                    BlockPos blockPos = new BlockPos(playerX + x, playerY, playerZ + z);
                    Chunk chunk = mc.theWorld.getChunkFromBlockCoords(blockPos);



                    for (int y = 0; y < 256; y++) {
                        BlockPos pos = new BlockPos(blockPos.getX(), y, blockPos.getZ());
                        // Überprüfen, ob das Bett gefunden wurde
                        if (chunk.getBlock(pos) instanceof BlockBed) {

                            // Zeichne die Box um das Bett (1 Block hoch, 2 Blöcke lang)
                            RenderUtil.drawBedBox(pos, bed_ESP_color);
                        }
                    }
                }
            }
        }

        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }
}


