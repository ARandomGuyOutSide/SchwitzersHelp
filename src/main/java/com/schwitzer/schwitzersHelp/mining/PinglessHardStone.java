package com.schwitzer.schwitzersHelp.mining;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import com.schwitzer.schwitzersHelp.config.SchwitzerHelpConfig;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PinglessHardStone {
    private final SchwitzerHelpConfig config = SchwitzerHelpConfig.getInstance();
    private final Minecraft mc = Minecraft.getMinecraft();
    private final Map<BlockPos, Long> activeBlocks = new HashMap<>();
    private final Map<BlockPos, Long> lastPacketTime = new HashMap<>();

    // Individual timers for each block
    private final Map<BlockPos, Long> blockForceUpdateTimers = new HashMap<>();
    private final int FORCE_UPDATE_INTERVAL = 510; // 510ms as in original code

    // Packet rate limiting
    private int packetsThisSecond = 0;
    private long currentSecond = System.currentTimeMillis() / 1000;
    private int packetLimit;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START || mc.thePlayer == null || mc.theWorld == null) return;
        if(!config.isPinglessHardstone()) return;

        packetLimit = config.getPacketsPerSecond();
        long now = System.currentTimeMillis();
        long secondNow = now / 1000;

        // Reset packet count if we've entered a new second
        if (secondNow != currentSecond) {
            currentSecond = secondNow;
            packetsThisSecond = 0;
        }

        if (mc.gameSettings.keyBindAttack.isKeyDown()) {
            MovingObjectPosition mop = mc.objectMouseOver;
            ItemStack heldItem = mc.thePlayer.getHeldItem();

            if (isValidMiningTool(heldItem)) {
                if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    BlockPos pos = mop.getBlockPos();
                    Block block = mc.theWorld.getBlockState(pos).getBlock();

                    if (block == Blocks.stone && !activeBlocks.containsKey(pos)) {
                        if (packetsThisSecond + 1 <= packetLimit) {
                            activeBlocks.put(pos, now);
                            lastPacketTime.put(pos, 0L);
                            // Set individual force update timer for this block
                            blockForceUpdateTimers.put(pos, now + FORCE_UPDATE_INTERVAL);
                            mc.theWorld.setBlockToAir(pos);
                            packetsThisSecond += 1;
                        }
                    }
                }
            }
        }

        // Maintain air blocks and send org.polyfrost.schwitzersHelp.mining packets
        Iterator<Map.Entry<BlockPos, Long>> iterator = activeBlocks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<BlockPos, Long> entry = iterator.next();
            BlockPos pos = entry.getKey();
            long startTime = entry.getValue();

            // Remove blocks that have been active for too long
            if (now - startTime > 500) {
                iterator.remove();
                lastPacketTime.remove(pos);
                blockForceUpdateTimers.remove(pos);
                continue;
            }

            // Check if this specific block needs a force update
            Long forceUpdateTime = blockForceUpdateTimers.get(pos);
            if (forceUpdateTime != null && now >= forceUpdateTime) {
                forceUpdateBlock(pos);
                // Reset the timer for this block (or remove it if you only want one update per block)
                blockForceUpdateTimers.put(pos, now + FORCE_UPDATE_INTERVAL);
            }

            // Continuously maintain air blocks - force them to stay air
            Block currentBlock = mc.theWorld.getBlockState(pos).getBlock();
            if (currentBlock != Blocks.air) {
                mc.theWorld.setBlockToAir(pos);
            }

            long lastSent = lastPacketTime.getOrDefault(pos, 0L);
            if (now - lastSent >= 100 && packetsThisSecond < packetLimit) {
                // Send org.polyfrost.schwitzersHelp.mining packets
                mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(
                        C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.UP));
                lastPacketTime.put(pos, now);
                packetsThisSecond += 1;
            }
        }
    }

    private boolean isValidMiningTool(ItemStack item) {
        if (item == null) {
            //Chat.debugMessage("No item in hand");
            return false;
        }

        if (!item.hasDisplayName()) {
            //Chat.debugMessage("Item has no display name: " + item.getItem().getUnlocalizedName());
            return false;
        }

        String itemName = item.getDisplayName().toLowerCase().replaceAll("§.", "");
        boolean isValid = itemName.contains("drill") || itemName.contains("gemstone gauntlet");

        //Chat.debugMessage("Tool check - Name: '" + itemName + "', Valid: " + isValid);
        return isValid;
    }

    /**
     * Force update a specific block
     * If it's stone on server, it becomes stone again
     * If it's air on server, it stays air
     */
    private void forceUpdateBlock(BlockPos pos) {
        try {
            // Force the client to request the actual block state from server
            // This will sync the client with the server state
            mc.theWorld.markBlockForUpdate(pos);

            // Alternative method: Send a block update request
            // This forces the server to send us the real block state
            // mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(
            //                    C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, pos, EnumFacing.UP));

        } catch (Exception e) {
            // Ignore any errors during force update
        }
    }
}