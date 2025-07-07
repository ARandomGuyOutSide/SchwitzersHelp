package org.polyfrost.schwitzersHelp.config;

import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.annotations.Number;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.InfoType;
import cc.polyfrost.oneconfig.config.migration.VigilanceName;
import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;

/**
 * The main Config entrypoint that extends the Config type and inits the config options.
 * See <a href="https://docs.polyfrost.cc/oneconfig/config/adding-options">this link</a> for more config Options
 */
public class SchwitzerHelpConfig extends Config {

    private static SchwitzerHelpConfig instance; // Singleton Instanz

    private static final transient String General = "General";
    private static final transient String WallHacks = "Wall hacks stuff";
    private static final transient String Dungeons = "Dungeons";
    private static final transient String Mining = "Mining";
    private static final transient String Help = "Help";
    private static final transient String Minigames = "Minigames";
    private static final transient String Macros = "Macros";
    private static final transient String Guild = "Guild";
    private static final transient String Discord = "Discord";

    //General

    //Bedwars

    @VigilanceName(name = "Player ESP", category = WallHacks, subcategory = "Players")
    @Switch(name = "Player ESP", category = WallHacks, subcategory = "Players")
    public boolean entitiy_ESP = true;

    @VigilanceName(name = "Color", category = WallHacks, subcategory = "Players")
    @Color(name = "Color", category = WallHacks, subcategory = "Players")
    public OneColor player_ESP_color = new OneColor(0, 100, 100, 255);

    @VigilanceName(name = "Player Lines", category = WallHacks, subcategory = "Players")
    @Switch(name = "Player Lines", category = WallHacks, subcategory = "Players")
    public boolean player_lines = true;

    @VigilanceName(name = "Color", category = WallHacks, subcategory = "Players")
    @Color(name = "Player Lines", category = WallHacks, subcategory = "Players")
    public OneColor player_lines_color = new OneColor(320, 100, 100, 255);

    @VigilanceName(name = "Bed ESP", category = WallHacks, subcategory = "Bed ESP")
    @Switch(name = "Bed ESP", category = WallHacks, subcategory = "Bed ESP")
    public boolean bed_ESP = true;

    @VigilanceName(name = "Color", category = WallHacks, subcategory = "Bed ESP")
    @Color(name = "Bed Color", category = WallHacks, subcategory = "Bed ESP")
    public OneColor bed_ESP_color = new OneColor(290, 100, 100, 255);

    @VigilanceName(name = "Bed Protection", category = WallHacks, subcategory = "Bed ESP")
    @Switch(name = "Bed Protection", category = WallHacks, subcategory = "Bed ESP")
    public boolean bed_protection = true;

    @VigilanceName(name = "Color", category = WallHacks, subcategory = "Bed ESP")
    @Color(name = "Obsidian Color", category = WallHacks, subcategory = "Bed ESP")
    public OneColor bedrock_color = new OneColor(0, 0, 0, 255);

    @VigilanceName(name = "Color", category = WallHacks, subcategory = "Bed ESP")
    @Color(name = "Endstone Color", category = WallHacks, subcategory = "Bed ESP")
    public OneColor endstone_color = new OneColor(50, 100, 100, 255);

    @VigilanceName(name = "Color", category = WallHacks, subcategory = "Bed ESP")
    @Color(name = "Wood Color", category = WallHacks, subcategory = "Bed ESP")
    public OneColor wood_color = new OneColor(25, 100, 80, 255);


    //General
    @VigilanceName(name = "Rainbow mode", category = General, subcategory = "Blocks")
    @Switch(name = "Rainbow mode", category = General, subcategory = "Blocks")
    public boolean rainbow = false;

    @Switch(name = "Block ESP outline", category = General, subcategory = "Blocks")
    public boolean blockOutline = false;

    @Switch(name = "Debug mode", category = General, subcategory = "Debuging")
    public boolean debugMode = false;

    @Switch(name = "Fun mode ;)", category = General, subcategory = "Fun")
    public boolean funMode = false;


    //Carneval

    //Dungeons

    @VigilanceName(name = "Key Mob ESP", category = Dungeons, subcategory = "Key Mobs ESP")

        @Switch(name = "Key Mobs ESP", category = Dungeons, subcategory = "Key Mob ESP")
        public boolean key_mob_esp = true;

        @Color(name = "Color of the Key Mobs", category = Dungeons, subcategory = "Key Mob ESP")
        public OneColor key_mob_esp_color = new OneColor(50, 100, 100, 255);

        @Switch(name = "Ghost Block", category = Dungeons, subcategory = "Ghost Block",  size = 2)
        public boolean make_ghost_block = false;

        @Slider(name = "Distance", category = Dungeons, subcategory = "Ghost Block", min = 1, max = 100)
        public int ghost_block_distance = 5;

        @Slider(name = "Delay (ms)", category = Dungeons, subcategory = "Ghost Block", min = 0, max = 50)
        public int ghost_block_delay = 5;

        @Switch(name = "Make Essence/Readstone Head Ghost Block", category = Dungeons, subcategory = "Ghost Block")
        public boolean ghost_block_essence = true;



    // Mining

    @VigilanceName(name = "Titanium ESP", category = Mining, subcategory = "Titanium ESP")

    @Switch(name = "Titanium ESP", category = Mining, subcategory = "Titanium ESP")
    public boolean scanForTitanium = true;

    @Switch(name = "Coal ESP", category = Mining, subcategory = "Coal ESP")
    public boolean coalESP = false;

    @Slider(name = "Distance", category = Mining, subcategory = "Coal ESP", min = 1, max = 50)
    public int coalESPRange = 5;

    @Color(name = "Color of Coal ESP", category = Mining, subcategory = "Coal ESP")
    public OneColor coalESPColor = new OneColor(50, 100, 100, 255);

    @Switch(name = "Pingless Hardstone", category = Mining, subcategory = "Crystal Hollows")
    public boolean pinglessHardstone = false;

    @Slider(name = "Packets per Second", category = Mining, subcategory = "Crystal Hollows", min = 1, max = 30)
    public int packetsPerSecond = 10;

    // Help

    @VigilanceName(name = "Config Movement", category = Help, subcategory = "Config Movement")

    @Switch(name = "Modify Movementspeed", category = Help, subcategory = "Modify Movement", size = 1)
    public boolean modify_movementspeed = false;

    @Number(name = "Set movementspeed value", category = Help, subcategory = "Modify Movement",  min = 1.0f, max = 25.0f, size = 1)
    public float movementspeed = 1;

    @Switch(name = "Modify Movementspeed Hypixel", category = Help, subcategory = "Modify Movement", size = 2)
    public boolean modify_movementspeed_hypixel = false;

    @Switch(name = "Fly", category = Help, subcategory = "Modify Movement" , size = 2)
    public boolean fly = false;

    @Switch(name = "No Fall", category = Help, subcategory = "Modify Movement" , size = 2)
    public boolean noFall = false;

    @Switch(name = "Auto jump", category = Help, description = "Activating it can bypass SOME anit cheat plugins", subcategory = "Modify Movement",  size = 2)
    public boolean auto_jump = true;


    @VigilanceName(name = "World Render Help", category = Help, subcategory = "World Render Help")

    @Switch(name = "Display Item Count", category = Help, subcategory = "World Render Help", size = 2)
    public boolean itemCount = false;

    @VigilanceName(name = "Bridging", category = Help, subcategory = "Bridging")

    @Switch(name = "Fast Place", category = Help, subcategory = "Bridging", size = 2)
    public boolean fastPlace = false;

    @Switch(name = "Safe Walk", category = Help, subcategory = "Bridging", size = 2)
    public boolean safeWalk = false;

    @VigilanceName(name = "Combat", category = Help, subcategory = "Combat")

    @Switch(name = "Reach", category = Help, subcategory = "Combat", size = 1)
    public boolean reach = false;

    @Number(name = "Reach distance", category = Help, subcategory = "Combat",  min = 3.0f, max = 6.0f, size = 1)
    public float reach_distance = 1;

    @Switch(name = "Aim Assist", category = Help, subcategory = "Combat", size = 1)
    public boolean aim_assist = false;

    @Switch(name = "Show Circle", category = Help, subcategory = "Combat", size = 1)
    public boolean show_circle = false;

    @Checkbox(name = "Click Aim", category = Help, subcategory = "Combat", size = 1)
    public boolean click_aim;

    @Checkbox(name = "Weapon Only", category = Help, subcategory = "Combat", size = 1)
    public boolean weapon_only;

    @Checkbox(name = "Aim Invis", category = Help, subcategory = "Combat", size = 1)
    public boolean aim_invis;

    @Checkbox(name = "Blatant Mode", category = Help, subcategory = "Combat", size = 1)
    public boolean blatant_mode;

    @Checkbox(name = "Ignore Friends", category = Help, subcategory = "Combat", size = 1)
    public boolean ignore_friends;



    @Slider(name = "Speed", category = Help, subcategory = "Combat", min = 1, max = 10)
    public int aim_assist_speed = 1;

    @Slider(name = "Distance", category = Help, subcategory = "Combat", min = 1, max = 10)
    public int aim_assist_distance = 5;

    @Slider(name = "Fov", category = Help, subcategory = "Combat", min = 0, max = 180)
    public int aim_assist_fov = 90;


    //Minigames

    @VigilanceName(name = "Minigames", category = Minigames, subcategory = "Minigames")

    @Switch(name = "Zombies ESP", category = Minigames, subcategory = "Zombies")
    public boolean zombie_esp = false;

    @Color(name = "Zombies ESP Color", category = Minigames, subcategory = "Zombies")
    public OneColor zombies_color = new OneColor(50, 100, 100, 255);

    @Switch(name = "Dragon ESP", category = Minigames, subcategory = "Disasters")
    public boolean dragon_esp = false;

    @Color(name = "Dragon ESP Color", category = Minigames, subcategory = "Disasters")
    public OneColor dragon_esp_color = new OneColor(50, 100, 100, 255);

    @Switch(name = "Dragon Line", category = Minigames, subcategory = "Disasters")
    public boolean dragon_line = false;

    @Color(name = "Dragon Line Color", category = Minigames, subcategory = "Disasters")
    public OneColor dragon_line_color = new OneColor(50, 100, 100, 255);

    // Macros

    @VigilanceName(name = "Macros", category = Macros, subcategory = "Macros")

    @KeyBind(name = "Macro Key", category = Macros, subcategory = "Guild Macro")
    public OneKeyBind guildMacroKey = new OneKeyBind();

    @KeyBind(name = "Macro Key", category = Macros, subcategory = "Stash Macro")
    public OneKeyBind stashMacroKey = new OneKeyBind();

    @Dropdown(name = "Item variants", options = {"Enchanted Item", "Enchanted Item Block"}, category = Macros, subcategory = "Stash Macro")
    public int itemVariantsOptions = 0;

    @Dropdown(name = "Selling options", options = {"Sell offer", "Instasell"}, category = Macros, subcategory = "Stash Macro")
    public int sellingOptions = 0;

    // Guild

    @VigilanceName(name = "Guild", category = Guild, subcategory = "Guild")
    @Switch(name = "Message on join", category = Guild, subcategory = "On Join")
    public boolean sendMessageOnGuildJoin = true;

    // Discord

    @VigilanceName(name = "Guild", category = Discord, subcategory = "Discord")
    @Switch(name = "Send information to discord webhook", category = Discord, subcategory = "Webhook")
    public boolean sendDiscordInformation = true;

    @Text(name = "Discord Webhook", category = Discord, subcategory = "Webhook", size = 2, placeholder = "Webhook")
    public String discordWebhook = "";

    public SchwitzerHelpConfig() {
        super(new Mod("Schwitzers help", ModType.SKYBLOCK), "schwitzers_conf.json");

        // Initialisierung der Konfiguration
        initialize();
    }

    // Methode, um die einzige Instanz der Klasse zu erhalten
    public static synchronized SchwitzerHelpConfig getInstance() {
        if (instance == null) {
            instance = new SchwitzerHelpConfig();
        }
        return instance;
    }

    public OneKeyBind getStashMacroKey() {
        return stashMacroKey;
    }

    public int getItemVariantsOptions() {
        return itemVariantsOptions;
    }

    public int getSellingOptions() {
        return sellingOptions;
    }

    public int getPacketsPerSecond() {
        return packetsPerSecond;
    }

    public boolean isFunMode() { return funMode;}

    public String getDiscordWebhook() {return discordWebhook;}

    public OneKeyBind getGuildMacroKey() {return guildMacroKey;}

    public boolean isPinglessHardstone() {return pinglessHardstone;}

    public boolean isSendDiscordInformation() { return sendDiscordInformation;}

    public boolean isDebugMode() {
        return debugMode;
    }

    public boolean isSendMessageOnGuildJoin() {
        return sendMessageOnGuildJoin;
    }

    public boolean isBlockOutline() {
        return blockOutline;
    }

    public boolean isEntitiy_ESP() {
        return entitiy_ESP;
    }

    public boolean isBett_ESP() {
        return bed_ESP;
    }

    public OneColor playerESP_color()
    {
        return player_ESP_color;
    }

    public OneColor getBed_ESP_color()
    {
        return bed_ESP_color;
    }

    public boolean isBed_protection()
    {
        return bed_protection;
    }

    public OneColor getBedrock_color()
    {
        return bedrock_color;
    }

    public OneColor getEntstone_color()
    {
        return endstone_color;
    }

    public OneColor getWood_color()
    {
        return wood_color;
    }

    public boolean getKey_mob_esp()
    {
        return key_mob_esp;
    }

    public OneColor getKey_mob_esp_color()
    {
        return key_mob_esp_color;
    }

    public boolean getPlayerLines()
    {
        return player_lines;
    }

    public OneColor getPlayer_lines_color()
    {
        return player_lines_color;
    }

    public boolean isScanForTitanium() {
        return scanForTitanium;
    }

    public boolean isModify_movementspeed()
    {
        return modify_movementspeed;
    }

    public boolean isAuto_jump()
    {
        return auto_jump;
    }

    public float getMovementspeed()
    {
        return movementspeed;
    }

    public boolean isFly()
    {
        return fly;
    }

    public boolean isNoFall()
    {
        return noFall;
    }

    public boolean isModify_movementspeed_hypixel()
    {
        return modify_movementspeed_hypixel;
    }

    public boolean isItemCount()
    {
        return itemCount;
    }

    public boolean isFastPlace()
    {
        return fastPlace;
    }

    public boolean isReach()
    {
        return reach;
    }

    public float getReach_distance()
    {
        return reach_distance;
    }

    public boolean isSafeWalk()
    {
        return safeWalk;
    }

    public boolean isZombie_esp()
    {
        return zombie_esp;
    }

    public OneColor getZombies_color()
    {
        return zombies_color;
    }

    public boolean isDragon_esp() {
        return dragon_esp;
    }

    public OneColor getDragon_esp_color() {
        return dragon_esp_color;
    }

    public boolean isDragon_line() {
        return dragon_line;
    }

    public OneColor getDragon_line_color() {
        return dragon_line_color;
    }

    public boolean isGhost_block()
    {
        return make_ghost_block;
    }

    public int getGhost_block_distance() {return ghost_block_distance;}

    public boolean isAim_Assist()
    {
        return aim_assist;
    }

    public int getAimAssistSpeed()
    {
        return aim_assist_speed;
    }

    public int getGhost_block_delay() {
        return ghost_block_delay;
    }

    public boolean isGhost_block_essence() {
        return ghost_block_essence;
    }

    public int getAimAssistDistance()
    {
        return aim_assist_distance;
    }

    public int getAimAssistFov()
    {
        return aim_assist_fov;
    }

    public boolean isShow_circle()
    {
        return show_circle;
    }

    public boolean isBlatant_mode() {
        return blatant_mode;
    }

    public boolean isCoalESP(){ return coalESP;}

    public OneColor getCoalESPColor() { return coalESPColor;}

    public boolean isRainbow() {return rainbow;}

    public int getCoalESPRange(){ return coalESPRange;}

    public boolean isAim_invis() {
        return aim_invis;
    }

    public boolean isWeapon_only() {
        return weapon_only;
    }

    public boolean isClick_aim() {
        return click_aim;
    }

    public boolean isIgnore_friends() {
        return ignore_friends;
    }
}

