package com.schwitzer.schwitzersHelp.mixin.gui;

import com.schwitzer.schwitzersHelp.gui.AutoUpdaterGUI;
import net.minecraft.client.gui.GuiMainMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public class MixinGuiMainMenu {
    @Shadow
    private String splashText;

    @Final
    @Inject(method = "updateScreen", at = @At("RETURN"))
    private void initGui(CallbackInfo ci) {
        if (!AutoUpdaterGUI.checkedForUpdates) {
            AutoUpdaterGUI.checkedForUpdates = true;
            AutoUpdaterGUI.getLatestVersion();
            if (AutoUpdaterGUI.isOutdated) {
                AutoUpdaterGUI.showGUI();
            }
        }
        if (AutoUpdaterGUI.isOutdated)
            this.splashText = "Update Farm Helper <3";
    }
}