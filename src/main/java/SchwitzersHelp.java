import DungeonsStuff.GhostBlock;
import bedwars.PlayerLinesMod;
import bedwars.Wallhacks;
import commands.DiscordCommand;
import commands.PlaceBlock;
import debug.InventorySlots;
import failsaves.Worldchange;
import features.DisconnectWhenISayIt;
import guilde.WelcomeMessages;
import helpStuff.*;
import macros.MacroController;
import mining.MiningStuff;
import mining.PinglessHardStone;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.polyfrost.schwitzersHelp.config.SchwitzerHelpConfig;

@Mod(modid = "schwitzershelp", name = "Schwitzers Help", version = "1.01")
public class SchwitzersHelp {

    public static SchwitzerHelpConfig config;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        // Initialize all modules
        config = SchwitzerHelpConfig.getInstance();
        MinecraftForge.EVENT_BUS.register(this);

        // Commands
        ClientCommandHandler.instance.registerCommand(new DiscordCommand());
        ClientCommandHandler.instance.registerCommand(new PlaceBlock());

        // Bedwars
        MinecraftForge.EVENT_BUS.register(new PlayerLinesMod());
        MinecraftForge.EVENT_BUS.register(new Wallhacks());

        // Dungeons
        MinecraftForge.EVENT_BUS.register(new GhostBlock());

        // Failsaves
        MinecraftForge.EVENT_BUS.register(new Worldchange());

        // Features
        MinecraftForge.EVENT_BUS.register(new DisconnectWhenISayIt());

        // Guilde
        MinecraftForge.EVENT_BUS.register(new WelcomeMessages());

        // Help
        MinecraftForge.EVENT_BUS.register(new AimAssist());
        MinecraftForge.EVENT_BUS.register(new ItemCount());
        MinecraftForge.EVENT_BUS.register(new MovementHelp());
        MinecraftForge.EVENT_BUS.register(new Reach());
        MinecraftForge.EVENT_BUS.register(new SafeWalk());

        // Macros
        MinecraftForge.EVENT_BUS.register(new MacroController());

        // Mining
        MinecraftForge.EVENT_BUS.register(new MiningStuff());
        MinecraftForge.EVENT_BUS.register(new PinglessHardStone());

        // Debug
        MinecraftForge.EVENT_BUS.register(new InventorySlots());
    }
}
