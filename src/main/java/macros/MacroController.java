package macros;

import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.libs.checker.units.qual.C;
import discord.DiscordNotifications;
import features.MouseUngrab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.polyfrost.schwitzersHelp.config.SchwitzerHelpConfig;
import util.Chat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MacroController {

    private static SchwitzerHelpConfig config = SchwitzerHelpConfig.getInstance();

    public enum MacroState {
        ENABLED,
        DISABLED
    }

    // Map um alle registrierten Macros zu verwalten
    private static Map<OneKeyBind, Macro> registeredMacros = new HashMap<>();
    private static Macro currentlyRunningMacro = null;
    private static boolean initialized = false;

    // Verschiedene Macro-Instanzen
    private static GuildeAdvertisementMacro guildeAdvertisement = new GuildeAdvertisementMacro();
    private static CollectStashMacro collectStashMacro = new CollectStashMacro();
    // Hier kannst du weitere Macros hinzufügen, z.B.:
    // private static FishingMacro fishingMacro = new FishingMacro();
    // private static MiningMacro miningMacro = new MiningMacro();

    // Lazy initialization der Macros
    private static void initializeMacros() {
        if (initialized) return;

        try {
            if (config != null) {
                registerMacro(config.getGuildMacroKey(), guildeAdvertisement);
                registerMacro(config.getStashMacroKey(), collectStashMacro);

                initialized = true;
            }
        } catch (Exception e) {
            System.err.println("Error initializing macros: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        // Stelle sicher, dass Macros initialisiert sind
        initializeMacros();

        if (registeredMacros.isEmpty()) {
            return; // Noch nicht bereit
        }

        try {
            for (Map.Entry<OneKeyBind, Macro> entry : registeredMacros.entrySet()) {
                OneKeyBind keyBind = entry.getKey();
                Macro macro = entry.getValue();

                if (keyBind != null && keyBind.isActive()) {
                    handleMacroToggle(macro);
                    break; // Nur ein Macro pro Tastendruck
                }
            }
        } catch (Exception e) {
            System.err.println("Error in macro key press: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleMacroToggle(Macro macro) throws IOException {
        // Wenn bereits ein anderes Macro läuft, stoppe es zuerst
        if (currentlyRunningMacro != null && currentlyRunningMacro != macro) {
            disableMacro(currentlyRunningMacro);
        }

        if (macro.getState() == MacroState.DISABLED) {
            enableMacro(macro);
        } else {
            disableMacro(macro);
        }
    }

    private static void enableMacro(Macro macro) throws IOException {
        macro.setState(MacroState.ENABLED);
        currentlyRunningMacro = macro;

        Chat.formatedChatMessage(macro.getName() + " enabled");
        macro.onEnable();


        DiscordNotifications.sendEmbedToWebhook(
                "Macro started",
                macro.getName() + " was started!",
                65280,
                config.getDiscordWebhook()
        );


        MouseUngrab.ungrabMouse();
    }

    private static void disableMacro(Macro macro) {
        macro.setState(MacroState.DISABLED);
        if (currentlyRunningMacro == macro) {
            currentlyRunningMacro = null;
        }

        Chat.formatedChatMessage(macro.getName() + " disabled");
        macro.onDisable();

        try {
            DiscordNotifications.sendEmbedToWebhook(
                    "Macro stopped",
                    macro.getName() + " was stopped!",
                    16711680,
                    config.getDiscordWebhook()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        cancelAllInputs();
        MouseUngrab.regrabMouse();
    }

    // Öffentliche Methoden um Macros zu registrieren und zu verwalten
    public static void registerMacro(OneKeyBind keyBind, Macro macro) {
        if (keyBind != null && macro != null) {
            registeredMacros.put(keyBind, macro);
        }
    }

    public static void unregisterMacro(OneKeyBind keyBind) {
        if (keyBind == null) return;

        Macro macro = registeredMacros.get(keyBind);
        if (macro != null && macro.getState() == MacroState.ENABLED) {
            disableMacro(macro);
        }
        registeredMacros.remove(keyBind);
    }

    // Manuelle Initialisierung für bessere Kontrolle
    public static void initialize() {
        initializeMacros();
    }

    public static Macro getCurrentlyRunningMacro() {
        return currentlyRunningMacro;
    }

    public static boolean isAnyMacroRunning() {
        return currentlyRunningMacro != null;
    }

    private static void cancelAllInputs() {
        Minecraft mc = Minecraft.getMinecraft();

        KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
    }
}