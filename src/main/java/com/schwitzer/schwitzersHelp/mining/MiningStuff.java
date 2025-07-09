package com.schwitzer.schwitzersHelp.mining;

import cc.polyfrost.oneconfig.config.core.OneColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.schwitzer.schwitzersHelp.config.SchwitzerHelpConfig;
import com.schwitzer.schwitzersHelp.util.RenderUtil;

public class MiningStuff {

    private SchwitzerHelpConfig config = SchwitzerHelpConfig.getInstance();

    private boolean scanForTitanium;
    private boolean isCoalESP;
    private int coalESPRange;


    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        World world = Minecraft.getMinecraft().theWorld;

        if (player == null || world == null) return;

        scanForTitanium = config.isScanForTitanium();
        isCoalESP = config.isCoalESP();
        coalESPRange = config.getCoalESPRange();

        // Kamera-Position für Rendering-Offset
        double renderPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks;
        double renderPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks;
        double renderPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks;

        if (scanForTitanium) {
            int playerX = (int) player.posX;
            int playerY = (int) player.posY;
            int playerZ = (int) player.posZ;

            // Optimiert: Äußere Schleifen für bessere Cache-Performance
            for (int y = playerY - 20; y <= playerY + 20; y++) {
                for (int x = playerX - 25; x <= playerX + 25; x++) {
                    for (int z = playerZ - 25; z <= playerZ + 25; z++) {
                        BlockPos pos = new BlockPos(x, y, z);

                        // Nur geladene Chunks prüfen
                        if (!world.isBlockLoaded(pos)) continue;

                        IBlockState blockState = world.getBlockState(pos);

                        // Polished Diorite Erkennung
                        if (blockState.getBlock() == Blocks.stone &&
                                blockState.getBlock().getMetaFromState(blockState) == 4) {

                            AxisAlignedBB box = new AxisAlignedBB(
                                    pos.getX() - renderPosX,
                                    pos.getY() - renderPosY,
                                    pos.getZ() - renderPosZ,
                                    pos.getX() + 1 - renderPosX,
                                    pos.getY() + 1 - renderPosY,
                                    pos.getZ() + 1 - renderPosZ
                            );


                            RenderUtil.drawFilledBox(box, 1.0f, 0.0f, 0.0f, 0.3f);
                        }
                    }
                }
            }
        }

        if (isCoalESP) {
            int playerX = (int) player.posX;
            int playerY = (int) player.posY;
            int playerZ = (int) player.posZ;

            // Real-time scanning in definiertem Bereich
            for (int y = playerY - coalESPRange; y <= playerY + coalESPRange; y++) {
                for (int x = playerX - coalESPRange; x <= playerX + coalESPRange; x++) {
                    for (int z = playerZ - coalESPRange; z <= playerZ + coalESPRange; z++) {
                        BlockPos blockPos = new BlockPos(x, y, z);

                        // Nur geladene Chunks prüfen für Performance
                        if (!world.isBlockLoaded(blockPos)) continue;

                        if (world.getBlockState(blockPos).getBlock() == Blocks.coal_ore) {
                            AxisAlignedBB box = new AxisAlignedBB(
                                    blockPos.getX() - renderPosX,
                                    blockPos.getY() - renderPosY,
                                    blockPos.getZ() - renderPosZ,
                                    blockPos.getX() + 1 - renderPosX,
                                    blockPos.getY() + 1 - renderPosY,
                                    blockPos.getZ() + 1 - renderPosZ
                            );

                            // Komplett schwarze Box mit höherem Alpha um Stacking zu reduzieren

                            OneColor color = config.getCoalESPColor();

                            if(config.isRainbow())
                                RenderUtil.drawFilledBox(box, color);
                            else
                                RenderUtil.drawFilledBox(box, color);
                        }
                    }
                }
            }
        }
    }
}
