package com.aaronhowser1.voracious.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class mouth extends Item {

    public mouth() {
        super(new Item.Properties()
        .maxStackSize(1)
        .group(ItemGroup.FOOD));
        setRegistryName("mouth");
    }
}
