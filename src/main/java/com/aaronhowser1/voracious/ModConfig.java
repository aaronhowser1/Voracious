package com.aaronhowser1.voracious;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;

@Mod.EventBusSubscriber
@SuppressWarnings("WeakerAccess")
public class ModConfig {

    public static final String CATEGORY_GENERAL = "general";
    public static final String SUBCATEGORY_DURABILITY = "durability";
    public static final String SUBCATEGORY_FOOD_VALUES = "foodvalues";
    public static final String SUBCATEGORY_EDIBLES = "edibles";
    public static final String SUBCATEGORY_DIGESTION_VALUES = "digestionvalues";
    public static final String SUBCATEGORY_SCREAM_VALUES = "screamvalues";

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.IntValue COOLDOWN;
    public static ForgeConfigSpec.BooleanValue USE_TOOLTIP;

    public static ForgeConfigSpec.BooleanValue ENABLE_DURABILITY;
    public static ForgeConfigSpec.IntValue MAX_DAMAGE;

    public static ForgeConfigSpec.IntValue FOOD_AMOUNT;
    public static ForgeConfigSpec.IntValue SATURATION_AMOUNT;

    public static ForgeConfigSpec.BooleanValue CAN_EAT_MONSTERS;
    public static ForgeConfigSpec.BooleanValue MONSTERS_POISON;
    public static ForgeConfigSpec.IntValue MONSTER_POISON_INTENSITY;
    public static ForgeConfigSpec.IntValue MONSTER_POISON_LENGTH;
    public static ForgeConfigSpec.BooleanValue CAN_EAT_BOSSES;
    public static ForgeConfigSpec.BooleanValue CAN_EAT_PLAYERS;

    public static ForgeConfigSpec.IntValue WAIT_TIME;
    public static ForgeConfigSpec.BooleanValue FEED_WHEN_UNSATURATED;
    public static ForgeConfigSpec.IntValue FEED_WHEN_UNDER;

    public static ForgeConfigSpec.BooleanValue SCREAM;
    public static ForgeConfigSpec.BooleanValue SCREAM_REPAIRS;
    public static ForgeConfigSpec.IntValue SCREAM_REPAIR_CHANCE;
    public static ForgeConfigSpec.IntValue SCREAM_REPAIR_AMOUNT;

    static {

        COMMON_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        COOLDOWN = COMMON_BUILDER.comment("\nAmount of ticks for cooldown")
                .defineInRange("cooldown", 20, 0, Integer.MAX_VALUE);
        USE_TOOLTIP = COMMON_BUILDER.comment("\nEnable tooltip")
                .define("enable_tooltip", true);

        setupDurabilityConfigs();
        setupFeedingConfigs();
        setupDigestionConfigs();
        setupEdibleConfigs();
        setupScreamingConfigs();
        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    private static void setupDurabilityConfigs() {
        COMMON_BUILDER.comment("Durability values").push(SUBCATEGORY_DURABILITY);

        ENABLE_DURABILITY = COMMON_BUILDER.comment("\nEnables Mouth requiring durability")
                .define("use_durability", true);
        MAX_DAMAGE = COMMON_BUILDER.comment("\nMaximum durability for the Mouth")
                .defineInRange("max_mouth_durability", 100, 1, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();
    }

    private static void setupFeedingConfigs() {
        COMMON_BUILDER.comment("Food values").push(SUBCATEGORY_FOOD_VALUES);

        FOOD_AMOUNT = COMMON_BUILDER.comment("\nAmount of hunger to refill")
                .defineInRange("foodAmount", 1, 0, Integer.MAX_VALUE);
        SATURATION_AMOUNT = COMMON_BUILDER.comment("\nAmount of saturation to refill")
                .defineInRange("saturationAmount", 1, 0, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();
    }

    private static void setupEdibleConfigs() {
        COMMON_BUILDER.comment("Edibility configs").push(SUBCATEGORY_EDIBLES);

        CAN_EAT_MONSTERS = COMMON_BUILDER.comment("\nEnable eating monsters")
                .define("can_eat_monsters", true);
        MONSTERS_POISON = COMMON_BUILDER.comment("\nEating monsters poisons you")
                .define("monsters_poison", true);
        MONSTER_POISON_INTENSITY = COMMON_BUILDER.comment("\nPoison level")
                .defineInRange("monster_poison_intensity", 1, 1, 32);
        MONSTER_POISON_LENGTH = COMMON_BUILDER.comment("\nPoison duration in ticks")
                .defineInRange("monster_poison_length", 200, 1, Integer.MAX_VALUE);
        CAN_EAT_BOSSES = COMMON_BUILDER.comment("\nEnable eating bosses")
                .define("can_eat_bosses", false);
        CAN_EAT_PLAYERS = COMMON_BUILDER.comment("\nEnable eating players")
                .define("can_eat_players", false);

        COMMON_BUILDER.pop();
    }

    private static void setupDigestionConfigs() {
        COMMON_BUILDER.comment("Digestion values").push(SUBCATEGORY_DIGESTION_VALUES);

        WAIT_TIME = COMMON_BUILDER.comment("\nAmount of ticks to wait between giving food")
                .defineInRange("digestionTime", 1, 1, Integer.MAX_VALUE);
        FEED_WHEN_UNSATURATED = COMMON_BUILDER.comment("\nSet to true to feed when saturation is low, \nset to false to feed when hunger is low")
                .define("feed_when_unsaturated",false);
        FEED_WHEN_UNDER = COMMON_BUILDER.comment("\nFeeds you when you are under this amount of either saturation or hunger, based on feed_when_unsaturated value")
                .defineInRange("feed_when_under", 20, 1, 20);

        COMMON_BUILDER.pop();
    }

    public static void setupScreamingConfigs() {
        COMMON_BUILDER.comment("Scream values").push(SUBCATEGORY_SCREAM_VALUES);

        SCREAM = COMMON_BUILDER.comment("\nSCREAM?!")
                .define("enable_screaming", true);
        SCREAM_REPAIRS = COMMON_BUILDER.comment("\nEnable screaming having a random chance to repair the Mouth")
                .define("screaming_repairs", true);
        SCREAM_REPAIR_CHANCE = COMMON_BUILDER.comment("\nPercentage chance that screaming repairs the Mouth")
                .defineInRange("scream_repair_chance", 10, 0, 100);
        SCREAM_REPAIR_AMOUNT = COMMON_BUILDER.comment("\nAmount of damage to repair")
                .defineInRange("scream_repair_chance", 5, 0, 100);

        COMMON_BUILDER.pop();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {

        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);
    }

}