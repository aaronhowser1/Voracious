package com.aaronhowser1.voracious;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.nio.file.Path;

@Mod.EventBusSubscriber
public class Config {

    public static final String CATEGORY_GENERAL = "general";
    public static final String SUBCATEGORY_FOOD_VALUES = "foodvalues";
    public static final String SUBCATEGORY_EDIBLES = "edibles";
    public static final String SUBCATEGORY_DIGESTION_VALUES = "digestionvalues";

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;


    public static ForgeConfigSpec.IntValue FOOD_AMOUNT;
    public static ForgeConfigSpec.IntValue SATURATION_AMOUNT;
    public static ForgeConfigSpec.IntValue WAIT_TIME;
    public static ForgeConfigSpec.BooleanValue CAN_EAT_MONSTERS;
    public static ForgeConfigSpec.BooleanValue MONSTERS_POISON;
    public static ForgeConfigSpec.IntValue MONSTER_POISON_INTENSITY;
    public static ForgeConfigSpec.IntValue MONSTER_POISON_LENGTH;
    public static ForgeConfigSpec.BooleanValue CAN_EAT_BOSSES;
    public static ForgeConfigSpec.BooleanValue CAN_EAT_PLAYERS;

    static {

        COMMON_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        setupFoodValuesConfig();
        setupDigestionValues();
        setupEdibleConfigs();
        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    private static void setupFoodValuesConfig() {
        COMMON_BUILDER.comment("Food values").push(SUBCATEGORY_FOOD_VALUES);

        FOOD_AMOUNT = COMMON_BUILDER.comment("Amount of hunger to refill")
                .defineInRange("foodAmount", 1, 0, Integer.MAX_VALUE);
        SATURATION_AMOUNT = COMMON_BUILDER.comment("Amount of saturation to refill")
                .defineInRange("saturationAmount", 1, 0, Integer.MAX_VALUE);

        COMMON_BUILDER.pop();
    }

    private static void setupEdibleConfigs() {
        COMMON_BUILDER.comment("Edibility configs").push(SUBCATEGORY_EDIBLES);

        CAN_EAT_MONSTERS = COMMON_BUILDER.comment("Enable eating monsters").define("can_eat_monsters", true);
        MONSTERS_POISON = COMMON_BUILDER.comment("Eating monsters poisons you").define("monsters_poison", true);
        MONSTER_POISON_INTENSITY = COMMON_BUILDER.comment("Poison level").defineInRange("monster_poison_intensity", 1, 1, 32);
        MONSTER_POISON_LENGTH = COMMON_BUILDER.comment("Poison duration in ticks").defineInRange("monster_poison_length", 200, 1, Integer.MAX_VALUE);
        CAN_EAT_BOSSES = COMMON_BUILDER.comment("Enable eating bosses").define("can_eat_bosses", false);
        CAN_EAT_PLAYERS = COMMON_BUILDER.comment("Enable eating players").define("can_eat_players", false);

        COMMON_BUILDER.pop();
    }

    private static void setupDigestionValues() {
        COMMON_BUILDER.comment("Digestion values").push(SUBCATEGORY_DIGESTION_VALUES);

//      I hate that this is a thing that I made
        WAIT_TIME = COMMON_BUILDER.comment("Amount of ticks to wait between giving food")
                .defineInRange("digestionTime", 1, 1, Integer.MAX_VALUE);

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

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {

    }

    @SubscribeEvent
    public static void onReload(final ModConfig.ConfigReloading configEvent) {
    }

}