package com.schwitzer.schwitzersHelp;

import com.schwitzer.schwitzersHelp.DungeonsStuff.GhostBlock;
import com.schwitzer.schwitzersHelp.bedwars.PlayerLinesMod;
import com.schwitzer.schwitzersHelp.bedwars.Wallhacks;
import com.schwitzer.schwitzersHelp.commands.DiscordCommand;
import com.schwitzer.schwitzersHelp.commands.PlaceBlock;
import com.schwitzer.schwitzersHelp.debug.InventorySlots;
import com.schwitzer.schwitzersHelp.failsaves.Worldchange;
import com.schwitzer.schwitzersHelp.features.DisconnectWhenISayIt;
import com.schwitzer.schwitzersHelp.features.PlayerMentionedInChat;
import com.schwitzer.schwitzersHelp.guilde.WelcomeMessages;
import com.schwitzer.schwitzersHelp.helpStuff.*;
import com.schwitzer.schwitzersHelp.macros.MacroController;
import com.schwitzer.schwitzersHelp.mining.MiningStuff;
import com.schwitzer.schwitzersHelp.mining.PinglessHardStone;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import com.schwitzer.schwitzersHelp.config.SchwitzerHelpConfig;
import com.schwitzer.schwitzersHelp.util.RenderUtil;

import java.io.File;

@Mod(modid = "schwitzershelp", name = "Schwitzers Help", version = "1.01")
public class SchwitzersHelp {
    public static final String VERSION = "%%VERSION%%";
    public static File jarFile = null;
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
        MinecraftForge.EVENT_BUS.register(new PlayerMentionedInChat());


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

        //org.polyfrost.schwitzersHelp.util
        MinecraftForge.EVENT_BUS.register(new RenderUtil());
    }
}
