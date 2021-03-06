package com.aaronhowser1.voracious;

//import com.aaronhowser1.voracious.items.FlossItem;
import com.aaronhowser1.voracious.items.MouthItem;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("voracious")
public class Voracious {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public Voracious() {
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, ModConfig.COMMON_CONFIG);

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        ModConfig.loadConfig(ModConfig.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("voracious-common.toml"));

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
            event.getRegistry().register(new Block(Block.Properties
                    .create(Material.EARTH)
                    .sound(SoundType.STONE)
                    .hardnessAndResistance(-1.0F,3600000.0F))
                    .setRegistryName("kidney_stone"));
        }
        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
            event.getRegistry().register(new MouthItem().setRegistryName("mouth"));
            event.getRegistry().register(new Item(new Item.Properties().group(ItemGroup.MISC)).setRegistryName("floss"));
            event.getRegistry().register(new BlockItem(ModBlocks.KIDNEY_STONE, new Item.Properties()
                    .group(ItemGroup.BUILDING_BLOCKS))
                    .setRegistryName("kidney_stone"));
        }
        @SubscribeEvent
        public static void onSoundsRegistry(final RegistryEvent.Register<SoundEvent> event) {
            event.getRegistry().register(new SoundEvent(new ResourceLocation("voracious:gulp")).setRegistryName("gulp"));
            event.getRegistry().register(new SoundEvent(new ResourceLocation("voracious:scream")).setRegistryName("scream"));
        }
    }

    @ObjectHolder("voracious:gulp")
    public static SoundEvent EAT_MOB;
    @ObjectHolder("voracious:scream")
    public static SoundEvent SCREAM;

}
