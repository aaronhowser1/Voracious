package com.aaronhowser1.voracious.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class KidneyStoneBlock extends Block {
    public KidneyStoneBlock() {
        super(Properties.create(Material.EARTH).sound(SoundType.STONE).hardnessAndResistance(-1.0F,3600000.0F));
        setRegistryName("kidney_stone");
    }
}
