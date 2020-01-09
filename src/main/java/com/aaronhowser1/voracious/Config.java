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
    public static final String SUBCATEGORY_DIGESTION_VALUES = "digestionvalues";

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;


    public static ForgeConfigSpec.IntValue FOOD_AMOUNT;
    public static ForgeConfigSpec.IntValue SATURATION_AMOUNT;
    public static ForgeConfigSpec.IntValue WAIT_TIME;

    static {

        COMMON_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        setupFoodValuesConfig();
        setupDigestionValues();
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