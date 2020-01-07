package com.aaronhowser1.voracious.items;

import com.google.common.collect.Lists;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class MouthItem extends Item {

    public MouthItem() {
        super(new Item.Properties()
                .maxStackSize(1)
                .group(ItemGroup.FOOD));
    }

    //All code below here written by LatvianModder, who refused to teach me how to make this and just made it himself
    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
        if (player.world.isRemote() || target instanceof PlayerEntity) {
            return true;
        }

        float hp = 0F;

        if (stack.hasTag()) {
            hp = stack.getTag().getFloat("stored_hp");
        }

        hp += target.getHealth();
        stack.setTagInfo("stored_hp", FloatNBT.func_229689_a_(hp));
        target.remove();

        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof ServerPlayerEntity && stack.hasTag()) {
            float efl = ((ServerPlayerEntity) entity).getFoodStats().getSaturationLevel();
            float hp = stack.getTag().getFloat("stored_hp");
            float a = Math.max(Math.min(hp, 1F), (20 - efl));
            stack.setTagInfo("stored_hp", FloatNBT.func_229689_a_(hp - a));
            ((ServerPlayerEntity) entity).getFoodStats().addStats(1, 1F);
//            ((ServerPlayerEntity) entity).getFoodStats().setFoodSaturationLevel(Math.round(efl + a));
        }
    }
}
