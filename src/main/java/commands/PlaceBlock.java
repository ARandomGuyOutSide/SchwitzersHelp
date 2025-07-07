package commands;

import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class PlaceBlock extends CommandBase {

    @Override
    public String getCommandName() {
        return "schwitza";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/schwitza place <block> <x> <y> <z>";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true; // Jeder Spieler darf den Command nutzen
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 5 || !"place".equalsIgnoreCase(args[0])) {
            sender.addChatMessage(new ChatComponentText("§cFalsche Nutzung! Nutze: /schwitza place <block> <x> <y> <z>"));
            return;
        }

        try {
            if (!(sender instanceof EntityPlayer)) {
                sender.addChatMessage(new ChatComponentText("§cNur Spieler können diesen Befehl nutzen!"));
                return;
            }

            EntityPlayer player = (EntityPlayer) sender;
            World world = sender.getEntityWorld();

            String blockName = args[1];
            Block block = Block.getBlockFromName(blockName);

            if (block == null) {
                sender.addChatMessage(new ChatComponentText("§cBlock " + blockName + " existiert nicht!"));
                return;
            }

            // Spielerposition abrufen und runden
            BlockPos playerPos = roundPlayerPosition(player);
            int x = parseRelativeCoordinate(args[2], playerPos.getX());
            int y = parseRelativeCoordinate(args[3], playerPos.getY());
            int z = parseRelativeCoordinate(args[4], playerPos.getZ());

            BlockPos pos = new BlockPos(x, y, z);

            if (!world.isRemote) {
                // Block auf Server setzen
                world.setBlockState(pos, block.getDefaultState());
                sender.addChatMessage(new ChatComponentText("§aBlock " + blockName + " platziert bei " + pos));
            } else {
                // Ghost-Block für den Client setzen (optional)
                sender.addChatMessage(new ChatComponentText("§7Ghost-Block " + blockName + " an " + pos));
                sender.getEntityWorld().setBlockState(pos, block.getDefaultState());
            }

        } catch (Exception e) {
            sender.addChatMessage(new ChatComponentText("§cFehler beim Platzieren des Blocks!"));
            e.printStackTrace();
        }
    }

    /**
     * Rundet die Spielerposition auf ganze Blöcke:
     * - X und Z werden mathematisch gerundet.
     * - Y wird immer **abgerundet** (auf den Block, auf dem der Spieler steht).
     */
    private BlockPos roundPlayerPosition(EntityPlayer player) {
        int roundedX = (int) player.posX; // Rundet normal
        int roundedY = (int) player.posY; // Immer abrunden
        int roundedZ = (int) player.posZ; // Rundet normal
        return new BlockPos(roundedX, roundedY, roundedZ);
    }

    /**
     * Berechnet relative Koordinaten (~, ~<offset>) oder absolute Koordinaten.
     */
    private int parseRelativeCoordinate(String input, int playerCoord) {
        if (input.startsWith("~")) {
            if (input.length() == 1) {
                return playerCoord; // Nur "~" → Nutze gerundete Spielerkoordinate
            }
            return playerCoord + Integer.parseInt(input.substring(1)); // "~5" → Spielerkoordinate + 5
        }
        return Integer.parseInt(input); // Absolute Zahl → Direkt nutzen
    }
}
