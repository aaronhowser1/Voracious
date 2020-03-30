package com.aaronhowser1.voracious.items;

import com.aaronhowser1.voracious.ModConfig;
import com.aaronhowser1.voracious.ModItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static com.aaronhowser1.voracious.Voracious.EAT_MOB;
import static com.aaronhowser1.voracious.Voracious.SCREAM;

@SuppressWarnings("all")
public class MouthItem extends Item {

    public MouthItem() {
        super(new Item.Properties()
                .maxStackSize(1)
                .maxDamage(ModConfig.MAX_DAMAGE.get())
                .group(ItemGroup.FOOD));
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
        player.getCooldownTracker().setCooldown(this, ModConfig.COOLDOWN.get());
        if (player.world.isRemote()) {
            return false;
        }
        float hp = 0F;
        if (stack.hasTag()) {
            hp = stack.getTag().getFloat("stored_hp");
        }
        //      If target is player and eating players is disabled, stop
        if (target instanceof PlayerEntity && !ModConfig.CAN_EAT_PLAYERS.get()) {
            return false;
        }
        //      If target is boss and eating bosses is disabled, stop
        if (!target.isNonBoss() && !ModConfig.CAN_EAT_BOSSES.get()) {
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
            if(ModConfig.ENABLE_DURABILITY.get()) {
                player.getHeldItem(hand).damageItem(1, player, playerEntity -> {
                    playerEntity.sendBreakAnimation(hand);
                });
            }
        }
        //      If mob IS monster and eating monsters is ENABLED, run normally
        if (target instanceof IMob && ModConfig.CAN_EAT_MONSTERS.get()) {
            hp += target.getHealth();
            stack.setTagInfo("stored_hp", FloatNBT.func_229689_a_(hp));
            player.getEntityWorld().playSound(null, player.func_226277_ct_(), player.func_226278_cu_(), player.func_226281_cx_(), EAT_MOB, SoundCategory.PLAYERS, 1.0F, 1.0F);
            target.remove();
        //          If poisoning config is enabled, poison
            if (ModConfig.MONSTERS_POISON.get()) {
                player.addPotionEffect(new EffectInstance(Effects.POISON, ModConfig.MONSTER_POISON_LENGTH.get(), ModConfig.MONSTER_POISON_INTENSITY.get()-1));
            }
            if(ModConfig.ENABLE_DURABILITY.get()) {
                player.getHeldItem(hand).damageItem(1, player, playerEntity -> {
                    playerEntity.sendBreakAnimation(hand);
                });
            }
        }

        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand handIn) {
        ItemStack itemstack = player.getHeldItem(handIn);
        if(ModConfig.SCREAM.get()) {
            player.getCooldownTracker().setCooldown(this, 34); //34 ticks is roughly how long the scream sound takes
            worldIn.playSound((PlayerEntity) null,
                    player.func_226277_ct_(), //poxX
                    player.func_226278_cu_(), //posY
                    player.func_226281_cx_(), //posZ
                    SCREAM, SoundCategory.PLAYERS, 1.0F, 1.0F);
//          Scream repairing
            if(ModConfig.SCREAM_REPAIRS.get()) {
                Random r = new Random();
                float chance = r.nextInt();
                if(chance <= ModConfig.SCREAM_REPAIR_CHANCE.get()) {
                    player.getHeldItem(handIn).damageItem(-5, player, null);
                }
            }
            return ActionResult.func_226248_a_(itemstack); //func_226248_a_=success
        } else {
            return ActionResult.func_226251_d_(itemstack); //func_226251_d_=fail
        }

    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof ServerPlayerEntity && stack.hasTag() && entity.ticksExisted % ModConfig.WAIT_TIME.get() == 0) {
            float hp = stack.getTag().getFloat("stored_hp");
            if(hp >= 1F) {
                if (!ModConfig.FEED_WHEN_UNSATURATED.get() && ((ServerPlayerEntity) entity).getFoodStats().getFoodLevel()< ModConfig.FEED_WHEN_UNDER.get()) {
                    stack.setTagInfo("stored_hp", FloatNBT.func_229689_a_(hp - 1F));
                    ((ServerPlayerEntity) entity).getFoodStats().addStats(ModConfig.FOOD_AMOUNT.get(), ModConfig.SATURATION_AMOUNT.get());
                }
                if (ModConfig.FEED_WHEN_UNSATURATED.get() && ((ServerPlayerEntity) entity).getFoodStats().getSaturationLevel()< ModConfig.FEED_WHEN_UNDER.get()) {
                    stack.setTagInfo("stored_hp", FloatNBT.func_229689_a_(hp - 1F));
                    ((ServerPlayerEntity) entity).getFoodStats().addStats(ModConfig.FOOD_AMOUNT.get(), ModConfig.SATURATION_AMOUNT.get());
                }
            }
        }
    }

    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == ModItems.FLOSS;
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack.hasTag() && ModConfig.USE_TOOLTIP.get()) {
            CompoundNBT compoundnbt = stack.getTag();
            Integer i = Math.round(compoundnbt.getFloat("stored_hp"));

            tooltip.add((new TranslationTextComponent("voracious.tooltip.health", i)).applyTextStyle(TextFormatting.YELLOW));
        }

    }

//    TODO: Make floss repair it more
}
