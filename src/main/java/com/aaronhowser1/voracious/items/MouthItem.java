package com.aaronhowser1.voracious.items;

import com.aaronhowser1.voracious.Config;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import static com.aaronhowser1.voracious.Voracious.EAT_MOB;
import static com.aaronhowser1.voracious.Voracious.SCREAM;

public class MouthItem extends Item {

    public MouthItem() {
        super(new Item.Properties()
                .maxStackSize(1)
                .group(ItemGroup.FOOD));
    }

    //Most of the code below here written by LatvianModder, who refused to teach me how to make this and just made it himself
    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
        player.getCooldownTracker().setCooldown(this, Config.COOLDOWN.get());
        if (player.world.isRemote()) {
            return false;
        }
        float hp = 0F;
        if (stack.hasTag()) {
            hp = stack.getTag().getFloat("stored_hp");
        }
        //      If target is player and eating players is disabled, stop
        if (target instanceof PlayerEntity && !Config.CAN_EAT_PLAYERS.get()) {
            return false;
        }
        //      If target is boss and eating bosses is disabled, stop
        if (!target.isNonBoss() && !Config.CAN_EAT_BOSSES.get()) {
            return false;
        }
        //      If mob is NOT monster, run normally
        if (!(target instanceof IMob)) {
            hp += target.getHealth();
            stack.setTagInfo("stored_hp", FloatNBT.func_229689_a_(hp));
            player.getEntityWorld().playSound(null, player.func_226277_ct_(), //posX
                    player.func_226278_cu_(), //posY
                    player.func_226281_cx_(), //posZ
                    EAT_MOB, SoundCategory.PLAYERS, 1.0F, 1.0F);
            target.remove();
        }
        //      If mob IS monster and eating monsters is ENABLED, run normally
        if (target instanceof IMob && Config.CAN_EAT_MONSTERS.get()) {
            hp += target.getHealth();
            stack.setTagInfo("stored_hp", FloatNBT.func_229689_a_(hp));
            player.getEntityWorld().playSound(null, player.func_226277_ct_(), player.func_226278_cu_(), player.func_226281_cx_(), EAT_MOB, SoundCategory.PLAYERS, 1.0F, 1.0F);
            target.remove();
        //          If poisoning config is enabled, poison
            if (Config.MONSTERS_POISON.get()) {
                player.addPotionEffect(new EffectInstance(Effects.POISON, Config.MONSTER_POISON_LENGTH.get(), Config.MONSTER_POISON_INTENSITY.get()-1));
            }
        }
        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand handIn) {
        ItemStack itemstack = player.getHeldItem(handIn);
        if(Config.SCREAM.get()) {
            player.getCooldownTracker().setCooldown(this, 34); //34 ticks is roughly how long the scream sound takes
            worldIn.playSound((PlayerEntity) null,
                    player.func_226277_ct_(), //poxX
                    player.func_226278_cu_(), //posY
                    player.func_226281_cx_(), //posZ
                    SCREAM, SoundCategory.PLAYERS, 1.0F, 1.0F);

            return ActionResult.func_226248_a_(itemstack); //func_226248_a_=success
        } else {
            return ActionResult.func_226251_d_(itemstack); //func_226251_d_=fail
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof ServerPlayerEntity && stack.hasTag() && entity.ticksExisted % Config.WAIT_TIME.get() == 0) {
            float hp = stack.getTag().getFloat("stored_hp");
            if(hp >= 1F) {
                if (!Config.FEED_WHEN_UNSATURATED.get() && ((ServerPlayerEntity) entity).getFoodStats().getFoodLevel()<Config.FEED_WHEN_UNDER.get()) {
                    stack.setTagInfo("stored_hp", FloatNBT.func_229689_a_(hp - 1F));
                    ((ServerPlayerEntity) entity).getFoodStats().addStats(Config.FOOD_AMOUNT.get(), Config.SATURATION_AMOUNT.get());
                }
                if (Config.FEED_WHEN_UNSATURATED.get() && ((ServerPlayerEntity) entity).getFoodStats().getSaturationLevel()<Config.FEED_WHEN_UNDER.get()) {
                    stack.setTagInfo("stored_hp", FloatNBT.func_229689_a_(hp - 1F));
                    ((ServerPlayerEntity) entity).getFoodStats().addStats(Config.FOOD_AMOUNT.get(), Config.SATURATION_AMOUNT.get());
                }
            }
        }
    }



//    TODO: Add tooltip
}
