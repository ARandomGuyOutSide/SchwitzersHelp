package com.schwitzer.schwitzersHelp.failsaves;

import com.schwitzer.schwitzersHelp.macros.Macro;
import com.schwitzer.schwitzersHelp.macros.MacroController;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Worldchange {
    private Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load event) {
        // Check if any macro is currently running
        if (MacroController.isAnyMacroRunning()) {
            if (mc.thePlayer == null || mc.theWorld == null)
                return;

            Macro runningMacro = MacroController.getCurrentlyRunningMacro();

            // Only disable if it's the Island Foraging Macro
            /*
            if (runningMacro instanceof IslandForaginMacro) {
                String macroName = runningMacro.getName();

                // Disable the Island Foraging Macro
                runningMacro.setState(MacroController.MacroState.DISABLED);
                Chat.formatedChatMessage(macroName + " was disabled due to world change");
            }
             */
        }
    }
}