package com.aaronhowser1.voracious.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class KidneyStoneBlock extends Block {
    public KidneyStoneBlock() {
        super(Properties.create(Material.EARTH).sound(SoundType.STONE).hardnessAndResistance(100f,100f));
        setRegistryName("kidney_stone");
    }
}
