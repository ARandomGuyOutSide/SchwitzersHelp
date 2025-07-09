package com.schwitzer.schwitzersHelp.bedwars;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BedProtectionChecker {

    private World world;
    Minecraft mc;

    public BedProtectionChecker(World world) {
        this.world = world;
    }

    /**
     * Diese Methode überprüft, ob sich neben einem Bett bestimmte Blöcke wie Obsidian, Holz oder Endstein befinden.
     * Es wird in einem Bereich von 2 Blöcken um das Bett in x, y und z überprüft.
     * @param bedPos Die Position des Bettes.
     * @return True, wenn sich mindestens einer der gesuchten Blöcke neben dem Bett befindet, sonst false.
     */
    public List<BlockPos> getNearbyObsidian(BlockPos bedPos) {
        // Den Bereich um das Bett in 2 Blöcken in jede Richtung scannen
        List<BlockPos> obsidianPositions = new ArrayList<>();


        for (int x = -2; x <= 2; x++) {
            for (int y = 0; y <= 2; y++) {
                for (int z = -2; z <= 2; z++) {
                    BlockPos posToCheck = bedPos.add(x, y, z);
                    Block block = world.getBlockState(posToCheck).getBlock();

                    // Prüfen, ob der Block Obsidian, Holz oder Endstein ist
                    if (block == Blocks.obsidian) {
                        obsidianPositions.add(posToCheck); // Obsidian-Position speichern
                    }
                }
            }
        }
        return obsidianPositions;
    }

    public List<BlockPos> getNearbyEndstone(BlockPos bedPos) {
        // Den Bereich um das Bett in 2 Blöcken in jede Richtung scannen
        List<BlockPos> enstonePosition = new ArrayList<>();


        for (int x = -4; x <= 4; x++) {
            for (int y = 0; y <= 4; y++) {
                for (int z = -4; z <= 4; z++) {
                    BlockPos posToCheck = bedPos.add(x, y, z);
                    Block block = world.getBlockState(posToCheck).getBlock();

                    // Prüfen, ob der Block Obsidian, Holz oder Endstein ist
                    if (block == Blocks.end_stone) {
                        enstonePosition.add(posToCheck); // Obsidian-Position speichern
                    }
                }
            }
        }
        return enstonePosition;
    }

    public List<BlockPos> getNearbyWood(BlockPos bedPos) {
        // Den Bereich um das Bett in 2 Blöcken in jede Richtung scannen
        List<BlockPos> woodPosition = new ArrayList<>();


        for (int x = -4; x <= 4; x++) {
            for (int y = 0; y <= 4; y++) {
                for (int z = -4; z <= 4; z++) {
                    BlockPos posToCheck = bedPos.add(x, y, z);
                    Block block = world.getBlockState(posToCheck).getBlock();

                    // Prüfen, ob der Block Obsidian, Holz oder Endstein ist
                    if (block == Blocks.planks) {
                        woodPosition.add(posToCheck); // Obsidian-Position speichern
                    }
                }
            }
        }
        return woodPosition;
    }
}
