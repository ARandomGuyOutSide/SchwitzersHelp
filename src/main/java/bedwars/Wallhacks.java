package bedwars;

import cc.polyfrost.oneconfig.config.core.OneColor;
import net.minecraft.block.BlockBed;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.polyfrost.schwitzersHelp.config.SchwitzerHelpConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.EnumFacing;


import java.util.List;

import static net.minecraft.client.gui.Gui.drawRect;

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
    private boolean bed_protection;
    private OneColor bedrock_color;
    private OneColor endstone_color;
    private OneColor wood_color;
    boolean key_mob_esp;
    OneColor key_mob_esp_color;
    boolean playerLines;
    OneColor playerLinesColor;
    boolean necron_boss_highlit;
    OneColor necron_esp_color;
    private boolean zombiesmode;
    private OneColor zombies_mode_mobs_color;
    private boolean dragonEsp;
    private OneColor dragonEspColor;

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        config = SchwitzerHelpConfig.getInstance();

        entityESP = config.isEntitiy_ESP();
        bedESP = config.isBett_ESP();
        playerESP_color = config.playerESP_color();
        bed_ESP_color = config.getBed_ESP_color();
        bed_protection = config.isBed_protection();
        bedrock_color = config.getBedrock_color();
        endstone_color = config.getEntstone_color();
        wood_color = config.getWood_color();
        key_mob_esp = config.getKey_mob_esp();
        key_mob_esp_color = config.getKey_mob_esp_color();
        playerLines = config.getPlayerLines();
        playerLinesColor = config.getPlayer_lines_color();
        zombiesmode = config.isZombie_esp();
        zombies_mode_mobs_color = config.getZombies_color();
        dragonEsp = config.isDragon_esp();
        dragonEspColor = config.getDragon_esp_color();
        //necron_boss_highlit = config.isNecron_boss_highlight();
        //necron_esp_color = config.getNecron_esp_color();

        World world = Minecraft.getMinecraft().theWorld;

        BedProtectionChecker bPC = new BedProtectionChecker(world);

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

        // Zeichne die Boxen um andere Spieler
        if (entityESP || key_mob_esp || necron_boss_highlit || zombiesmode || dragonEsp) {
            for (Object playerObj : mc.theWorld.loadedEntityList) {
                if (playerObj instanceof EntityLivingBase) {
                    EntityLivingBase otherPlayer = (EntityLivingBase) playerObj;

                    // Den eigenen Spieler ignorieren
                    if (otherPlayer == entityPlayerSP) continue;

                    if (key_mob_esp) {
                        if (otherPlayer.hasCustomName()) {
                            String formattedName = otherPlayer.getDisplayName().getFormattedText();

                            if (formattedName.contains("✯")) {
                                drawEntityBox(otherPlayer.posX, otherPlayer.posY - 2, otherPlayer.posZ, 0.5, 1.8, key_mob_esp_color, 0.0D);
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
                            drawEntityBox(otherPlayer.posX, otherPlayer.posY, otherPlayer.posZ, 0.5, 1.8, color, 0.5F);

                            // 🛠 OpenGL-States speichern + Namen rendern
                            GlStateManager.pushMatrix();
                            GlStateManager.disableLighting();
                            GlStateManager.enableDepth();

                            double nameX = otherPlayer.posX;
                            double nameY = otherPlayer.posY + 2.2;
                            double nameZ = otherPlayer.posZ;
                            drawNameTag(otherEntityPlayer.getName(), nameX, nameY, nameZ);

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

                            drawEntityBox(((EntityCreature) playerObj).posX, ((EntityCreature) playerObj).posY, ((EntityCreature) playerObj).posZ, 0.5, ((EntityCreature) playerObj).height, zombies_mode_mobs_color, 0.0D);
                        }
                    }

                    if (dragonEsp) {
                        if(playerObj instanceof EntityDragon)
                        {
                            EntityDragon dragon = (EntityDragon) playerObj;

                            // Box zeichnen
                            drawEntityBox(dragon.posX, dragon.posY, dragon.posZ, dragon.width / 2.0, dragon.height, dragonEspColor, 0.0D);
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
                            drawBedBox(pos);

                            if(bed_protection)
                            {
                                // Überprüfen, ob das Bett geschützt ist
                                List<BlockPos> nearbyObsidian = bPC.getNearbyObsidian(pos);
                                List<BlockPos> nearbyEndstone = bPC.getNearbyEndstone(pos);
                                List<BlockPos> nearbyWood = bPC.getNearbyWood(pos);

                                // Zeichne Boxen um alle nahegelegenen Obsidianblöcke
                                for (BlockPos obsidianPos : nearbyObsidian) {
                                    drawObsidianBox(obsidianPos);
                                }

                                for (BlockPos endsidianPos : nearbyEndstone) {
                                    drawEnstoneBox(endsidianPos);
                                }

                                for (BlockPos woodPos : nearbyWood) {
                                    drawWoodBox(woodPos);
                                }
                            }
                        }
                    }
                }
            }
        }

        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    private void drawNameTag(String name, double x, double y, double z) {
        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer fontRenderer = mc.fontRendererObj;
        RenderManager renderManager = mc.getRenderManager();

        float scale = 0.02F; // Kleinere Skalierung für bessere Lesbarkeit

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + 0.5, z); // Position leicht über der Box
        GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale(-scale, -scale, scale);

        // Richtige 2D-Einstellungen setzen
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();

        // Hintergrund für bessere Lesbarkeit (schwarz mit Transparenz)
        int width = fontRenderer.getStringWidth(name) / 2;
        drawRect(-width - 2, -2, width + 2, 10, 0x80000000); // Schwarz, leicht transparent

        // Namen in Weiß mit Schatten rendern
        fontRenderer.drawStringWithShadow(name, -width, 0, 0xFFFFFF);

        // Ursprüngliche OpenGL-States wiederherstellen
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }




    // Neue Methode zum Zeichnen einer Box um Obsidian
    private void drawObsidianBox(BlockPos pos) {
        double centerX = pos.getX() + 0.25f;
        double centerY = pos.getY() + 0.25f;
        double centerZ = pos.getZ() + 0.25f;

        double width = 0.5D;  // Ein Block breit
        double height = 0.5D; // Ein Block hoch

        float red = bedrock_color.getRed() / 255.0f;
        float green = bedrock_color.getGreen() / 255.0f;
        float blue = bedrock_color.getBlue() / 255.0f;
        float alpha = bedrock_color.getAlpha() / 255.0f;


        GL11.glLineWidth(3.0f);

        GlStateManager.color(red, green, blue, alpha);

        GL11.glBegin(GL11.GL_LINES);

        // Linienbreite auf 2.0f (oder einen gewünschten Wert) setzen
        // Hier kannst du die Dicke der Linien anpassen

        // Untere Ebene (0.5x0.5 Quadrat)
        GL11.glVertex3d(centerX, centerY, centerZ);                  // Startpunkt
        GL11.glVertex3d(centerX + width, centerY, centerZ);          // x-Achse, 0.5 weiter

        GL11.glVertex3d(centerX + width, centerY, centerZ);          // Ecke rechts vorne
        GL11.glVertex3d(centerX + width, centerY, centerZ + width);  // y-Achse, 0.5 weiter

        GL11.glVertex3d(centerX + width, centerY, centerZ + width);  // Ecke rechts hinten
        GL11.glVertex3d(centerX, centerY, centerZ + width);          // x-Achse zurück nach links

        GL11.glVertex3d(centerX, centerY, centerZ + width);          // Ecke links hinten
        GL11.glVertex3d(centerX, centerY, centerZ);                  // Schließt das Quadrat

        // Obere Ebene (0.5x0.5 Quadrat, 0.5 nach oben versetzt)
        GL11.glVertex3d(centerX, centerY + height, centerZ);                  // Startpunkt oben
        GL11.glVertex3d(centerX + width, centerY + height, centerZ);          // x-Achse, 0.5 weiter

        GL11.glVertex3d(centerX + width, centerY + height, centerZ);          // Ecke rechts vorne oben
        GL11.glVertex3d(centerX + width, centerY + height, centerZ + width);  // y-Achse, 0.5 weiter

        GL11.glVertex3d(centerX + width, centerY + height, centerZ + width);  // Ecke rechts hinten oben
        GL11.glVertex3d(centerX, centerY + height, centerZ + width);          // x-Achse zurück nach links

        GL11.glVertex3d(centerX, centerY + height, centerZ + width);          // Ecke links hinten oben
        GL11.glVertex3d(centerX, centerY + height, centerZ);                  // Schließt das Quadrat oben

        // Verbindungen zwischen oberer und unterer Ebene (Vertikale Linien)
        GL11.glVertex3d(centerX, centerY, centerZ);                           // Linke vordere Kante
        GL11.glVertex3d(centerX, centerY + height, centerZ);                  // Linke vordere Kante oben

        GL11.glVertex3d(centerX + width, centerY, centerZ);                   // Rechte vordere Kante
        GL11.glVertex3d(centerX + width, centerY + height, centerZ);          // Rechte vordere Kante oben

        GL11.glVertex3d(centerX + width, centerY, centerZ + width);           // Rechte hintere Kante
        GL11.glVertex3d(centerX + width, centerY + height, centerZ + width);  // Rechte hintere Kante oben

        GL11.glVertex3d(centerX, centerY, centerZ + width);                   // Linke hintere Kante
        GL11.glVertex3d(centerX, centerY + height, centerZ + width);          // Linke hintere Kante oben

        GL11.glEnd();
    }

    private void drawEnstoneBox(BlockPos pos) {
        double centerX  = pos.getX() + 0.25f;
        double centerY  = pos.getY() + 0.25f;
        double centerZ  = pos.getZ() + 0.25f;

        double width = 0.5D;  // Ein Block breit
        double height = 0.5D; // Ein Block hoch

        float red = endstone_color.getRed() / 255.0f;
        float green = endstone_color.getGreen() / 255.0f;
        float blue = endstone_color.getBlue() / 255.0f;
        float alpha = endstone_color.getAlpha() / 255.0f;

        GL11.glLineWidth(3.0f);

        GL11.glEnable(GL11.GL_LINE_SMOOTH);  // Linienglättung (Anti-Aliasing)
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);  // Beste Qualität

        GlStateManager.color(red, green, blue, alpha);

        GL11.glDisable(GL11.GL_DEPTH_TEST);

        GL11.glBegin(GL11.GL_LINES);


        // Untere Ebene (0.5x0.5 Quadrat)
        GL11.glVertex3d(centerX, centerY, centerZ);                  // Startpunkt
        GL11.glVertex3d(centerX + width, centerY, centerZ);          // x-Achse, 0.5 weiter

        GL11.glVertex3d(centerX + width, centerY, centerZ);          // Ecke rechts vorne
        GL11.glVertex3d(centerX + width, centerY, centerZ + width);  // y-Achse, 0.5 weiter

        GL11.glVertex3d(centerX + width, centerY, centerZ + width);  // Ecke rechts hinten
        GL11.glVertex3d(centerX, centerY, centerZ + width);          // x-Achse zurück nach links

        GL11.glVertex3d(centerX, centerY, centerZ + width);          // Ecke links hinten
        GL11.glVertex3d(centerX, centerY, centerZ);                  // Schließt das Quadrat

        // Obere Ebene (0.5x0.5 Quadrat, 0.5 nach oben versetzt)
        GL11.glVertex3d(centerX, centerY + height, centerZ);                  // Startpunkt oben
        GL11.glVertex3d(centerX + width, centerY + height, centerZ);          // x-Achse, 0.5 weiter

        GL11.glVertex3d(centerX + width, centerY + height, centerZ);          // Ecke rechts vorne oben
        GL11.glVertex3d(centerX + width, centerY + height, centerZ + width);  // y-Achse, 0.5 weiter

        GL11.glVertex3d(centerX + width, centerY + height, centerZ + width);  // Ecke rechts hinten oben
        GL11.glVertex3d(centerX, centerY + height, centerZ + width);          // x-Achse zurück nach links

        GL11.glVertex3d(centerX, centerY + height, centerZ + width);          // Ecke links hinten oben
        GL11.glVertex3d(centerX, centerY + height, centerZ);                  // Schließt das Quadrat oben

        // Verbindungen zwischen oberer und unterer Ebene (Vertikale Linien)
        GL11.glVertex3d(centerX, centerY, centerZ);                           // Linke vordere Kante
        GL11.glVertex3d(centerX, centerY + height, centerZ);                  // Linke vordere Kante oben

        GL11.glVertex3d(centerX + width, centerY, centerZ);                   // Rechte vordere Kante
        GL11.glVertex3d(centerX + width, centerY + height, centerZ);          // Rechte vordere Kante oben

        GL11.glVertex3d(centerX + width, centerY, centerZ + width);           // Rechte hintere Kante
        GL11.glVertex3d(centerX + width, centerY + height, centerZ + width);  // Rechte hintere Kante oben

        GL11.glVertex3d(centerX, centerY, centerZ + width);                   // Linke hintere Kante
        GL11.glVertex3d(centerX, centerY + height, centerZ + width);          // Linke hintere Kante oben

        GL11.glEnd();
    }

    private void drawWoodBox(BlockPos pos) {
        double centerX = pos.getX() + 0.25f;
        double centerY = pos.getY() + 0.25f;
        double centerZ = pos.getZ() + 0.25f;

        double width = 0.5D;  // Ein Block breit
        double height = 0.5D; // Ein Block hoch

        float red = wood_color.getRed() / 255.0f;
        float green = wood_color.getGreen() / 255.0f;
        float blue = wood_color.getBlue() / 255.0f;
        float alpha = wood_color.getAlpha() / 255.0f;

        GL11.glLineWidth(3.0f);

        GlStateManager.color(red, green, blue, alpha);

        GL11.glBegin(GL11.GL_LINES);

        // Untere Ebene (0.5x0.5 Quadrat)
        GL11.glVertex3d(centerX, centerY, centerZ);                  // Startpunkt
        GL11.glVertex3d(centerX + width, centerY, centerZ);          // x-Achse, 0.5 weiter

        GL11.glVertex3d(centerX + width, centerY, centerZ);          // Ecke rechts vorne
        GL11.glVertex3d(centerX + width, centerY, centerZ + width);  // y-Achse, 0.5 weiter

        GL11.glVertex3d(centerX + width, centerY, centerZ + width);  // Ecke rechts hinten
        GL11.glVertex3d(centerX, centerY, centerZ + width);          // x-Achse zurück nach links

        GL11.glVertex3d(centerX, centerY, centerZ + width);          // Ecke links hinten
        GL11.glVertex3d(centerX, centerY, centerZ);                  // Schließt das Quadrat

        // Obere Ebene (0.5x0.5 Quadrat, 0.5 nach oben versetzt)
        GL11.glVertex3d(centerX, centerY + height, centerZ);                  // Startpunkt oben
        GL11.glVertex3d(centerX + width, centerY + height, centerZ);          // x-Achse, 0.5 weiter

        GL11.glVertex3d(centerX + width, centerY + height, centerZ);          // Ecke rechts vorne oben
        GL11.glVertex3d(centerX + width, centerY + height, centerZ + width);  // y-Achse, 0.5 weiter

        GL11.glVertex3d(centerX + width, centerY + height, centerZ + width);  // Ecke rechts hinten oben
        GL11.glVertex3d(centerX, centerY + height, centerZ + width);          // x-Achse zurück nach links

        GL11.glVertex3d(centerX, centerY + height, centerZ + width);          // Ecke links hinten oben
        GL11.glVertex3d(centerX, centerY + height, centerZ);                  // Schließt das Quadrat oben

        // Verbindungen zwischen oberer und unterer Ebene (Vertikale Linien)
        GL11.glVertex3d(centerX, centerY, centerZ);                           // Linke vordere Kante
        GL11.glVertex3d(centerX, centerY + height, centerZ);                  // Linke vordere Kante oben

        GL11.glVertex3d(centerX + width, centerY, centerZ);                   // Rechte vordere Kante
        GL11.glVertex3d(centerX + width, centerY + height, centerZ);          // Rechte vordere Kante oben

        GL11.glVertex3d(centerX + width, centerY, centerZ + width);           // Rechte hintere Kante
        GL11.glVertex3d(centerX + width, centerY + height, centerZ + width);  // Rechte hintere Kante oben

        GL11.glVertex3d(centerX, centerY, centerZ + width);                   // Linke hintere Kante
        GL11.glVertex3d(centerX, centerY + height, centerZ + width);          // Linke hintere Kante oben

        GL11.glEnd();
    }

    // Neue Methode zum Zeichnen einer Box um das Bett (1 Block hoch, 2 Blöcke lang)
    private void drawBedBox(BlockPos pos) {
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();

        float red = bed_ESP_color.getRed() / 255.0f;
        float green = bed_ESP_color.getGreen() / 255.0f;
        float blue = bed_ESP_color.getBlue() / 255.0f;
        float alpha = bed_ESP_color.getAlpha() / 255.0f;

        GlStateManager.color(red, green, blue, alpha);

        // Stelle sicher, dass nur die Fußseite des Bettes die Box erhält
        BlockBed bedBlock = (BlockBed) Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
        BlockBed.EnumPartType partType = Minecraft.getMinecraft().theWorld.getBlockState(pos).getValue(BlockBed.PART);
        boolean isFoot = partType == BlockBed.EnumPartType.FOOT;  // Prüfe, ob es die Fußseite ist

        if (isFoot) {
            // Finde die Richtung des Bettes heraus
            EnumFacing facing = Minecraft.getMinecraft().theWorld.getBlockState(pos).getValue(BlockBed.FACING);

            double width = 1.0D;  // Ein Block breit
            double height = 1.0D; // Ein Block hoch
            double length = 2.0D; // Zwei Blöcke lang (für das Bett)

            // Anpassen der Box basierend auf der Ausrichtung des Bettes
            double x1 = x;
            double z1 = z;
            double x2 = x;
            double z2 = z;

            switch (facing) {
                case NORTH:
                    x1 = x;
                    z1 = z - 1;  // Box zeigt nach Norden (negative Z-Richtung)
                    x2 = x + width;
                    z2 = z + length - 1;
                    break;
                case SOUTH:
                    x1 = x;
                    z1 = z;
                    x2 = x + width;
                    z2 = z + length;
                    break;
                case WEST:
                    x1 = x - 1;  // Box zeigt nach Westen (negative X-Richtung)
                    z1 = z;
                    x2 = x + length - 1;
                    z2 = z + width;
                    break;
                case EAST:
                    x1 = x;
                    z1 = z;
                    x2 = x + length;
                    z2 = z + width;
                    break;
            }

            GL11.glBegin(GL11.GL_LINES);

            // Bottom layer
            GL11.glVertex3d(x1, y, z1);
            GL11.glVertex3d(x2, y, z1);

            GL11.glVertex3d(x2, y, z1);
            GL11.glVertex3d(x2, y, z2);

            GL11.glVertex3d(x2, y, z2);
            GL11.glVertex3d(x1, y, z2);

            GL11.glVertex3d(x1, y, z2);
            GL11.glVertex3d(x1, y, z1);

            // Top layer
            GL11.glVertex3d(x1, y + height, z1);
            GL11.glVertex3d(x2, y + height, z1);

            GL11.glVertex3d(x2, y + height, z1);
            GL11.glVertex3d(x2, y + height, z2);

            GL11.glVertex3d(x2, y + height, z2);
            GL11.glVertex3d(x1, y + height, z2);

            GL11.glVertex3d(x1, y + height, z2);
            GL11.glVertex3d(x1, y + height, z1);

            // Connect top and bottom layers
            GL11.glVertex3d(x1, y, z1);
            GL11.glVertex3d(x1, y + height, z1);

            GL11.glVertex3d(x2, y, z1);
            GL11.glVertex3d(x2, y + height, z1);

            GL11.glVertex3d(x2, y, z2);
            GL11.glVertex3d(x2, y + height, z2);

            GL11.glVertex3d(x1, y, z2);
            GL11.glVertex3d(x1, y + height, z2);

            GL11.glEnd();
        }
    }


    private void drawEntityBox(double x, double y, double z, double width, double height, OneColor color, double yOffset) {
        // Extrahiere RGB-Werte aus der Farbe
        float red = color.getRed() / 255.0f;
        float green = color.getGreen() / 255.0f;
        float blue = color.getBlue() / 255.0f;
        float alpha = color.getAlpha() / 255.0f;

        // OpenGL Farbe setzen
        GlStateManager.color(red, green, blue, alpha);

        // Zeichnen der Box
        GL11.glBegin(GL11.GL_LINES);

        // Untere Ebene der Box
        GL11.glVertex3d(x - width, y + yOffset, z - width);
        GL11.glVertex3d(x + width, y + yOffset, z - width);

        GL11.glVertex3d(x + width, y + yOffset, z - width);
        GL11.glVertex3d(x + width, y + yOffset, z + width);

        GL11.glVertex3d(x + width, y + yOffset, z + width);
        GL11.glVertex3d(x - width, y + yOffset, z + width);

        GL11.glVertex3d(x - width, y + yOffset, z + width);
        GL11.glVertex3d(x - width, y + yOffset, z - width);

        // Obere Ebene der Box
        GL11.glVertex3d(x - width, y + height + yOffset, z - width);
        GL11.glVertex3d(x + width, y + height + yOffset, z - width);

        GL11.glVertex3d(x + width, y + height + yOffset, z - width);
        GL11.glVertex3d(x + width, y + height + yOffset, z + width);

        GL11.glVertex3d(x + width, y + height + yOffset, z + width);
        GL11.glVertex3d(x - width, y + height + yOffset, z + width);

        GL11.glVertex3d(x - width, y + height + yOffset, z + width);
        GL11.glVertex3d(x - width, y + height + yOffset, z - width);

        // Verbinden der oberen und unteren Ebenen
        GL11.glVertex3d(x - width, y + yOffset, z - width);
        GL11.glVertex3d(x - width, y + height + yOffset, z - width);

        GL11.glVertex3d(x + width, y + yOffset, z - width);
        GL11.glVertex3d(x + width, y + height + yOffset, z - width);

        GL11.glVertex3d(x + width, y + yOffset, z + width);
        GL11.glVertex3d(x + width, y + height + yOffset, z + width);

        GL11.glVertex3d(x - width, y + yOffset, z + width);
        GL11.glVertex3d(x - width, y + height + yOffset, z + width);

        GL11.glEnd();
    }


}


