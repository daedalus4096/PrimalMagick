package com.verdantartifice.primalmagic.common.items.concoctions;

import java.util.List;

import com.verdantartifice.primalmagic.common.concoctions.ConcoctionType;
import com.verdantartifice.primalmagic.common.concoctions.ConcoctionUtils;
import com.verdantartifice.primalmagic.common.concoctions.FuseType;
import com.verdantartifice.primalmagic.common.entities.projectiles.AlchemicalBombEntity;
import com.verdantartifice.primalmagic.common.stats.StatsManager;
import com.verdantartifice.primalmagic.common.stats.StatsPM;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/**
 * Definition of an alchemical bomb.  Similar to a splash potion, but a single bomb has multiple
 * charges.  Plus, they bounce on a timed fuse.
 * 
 * @author Daedalus4096
 */
public class AlchemicalBombItem extends Item {
    public AlchemicalBombItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getDefaultInstance() {
        return ConcoctionUtils.setFuseType(ConcoctionUtils.setConcoctionType(PotionUtils.addPotionToItemStack(super.getDefaultInstance(), Potions.WATER), ConcoctionType.WATER), FuseType.MEDIUM);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (playerIn.isSecondaryUseActive()) {
            // Set bomb fuse
            FuseType fuse = ConcoctionUtils.getFuseType(stack);
            if (!worldIn.isRemote && fuse != null && fuse.getNext() != null) {
                playerIn.setHeldItem(handIn, ConcoctionUtils.setFuseType(stack, fuse.getNext()));
                ITextComponent fuseText = new TranslationTextComponent(fuse.getNext().getTranslationKey());
                playerIn.sendMessage(new TranslationTextComponent("concoctions.primalmagic.fuse_set", fuseText), Util.DUMMY_UUID);
            }
        } else {
            worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
            worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
            
            // Throw bomb entity
            AlchemicalBombEntity entity = new AlchemicalBombEntity(worldIn, playerIn);
            entity.setItem(stack);
            entity.setDirectionAndMovement(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, -20.0F, 0.5F, 1.0F);
            worldIn.addEntity(entity);
            
            // Increment stat
            StatsManager.incrementValue(playerIn, StatsPM.CONCOCTIONS_USED);

            // Deduct charge
            if (!playerIn.abilities.isCreativeMode) {
                int charges = ConcoctionUtils.getCurrentDoses(stack);
                if (charges <= 1) {
                    stack.shrink(1);
                } else {
                    ConcoctionUtils.setCurrentDoses(stack, charges - 1);
                }
            }
        }
        return ActionResult.func_233538_a_(stack, worldIn.isRemote());
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        Potion potion = PotionUtils.getPotionFromItem(stack);
        ConcoctionType type = ConcoctionUtils.getConcoctionType(stack);
        if (type == null) {
            potion = Potions.EMPTY;
            type = ConcoctionType.WATER;
        }
        return potion.getNamePrefixed(this.getTranslationKey() + "." + type.getString() + ".effect.");
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
        tooltip.add(new TranslationTextComponent("concoctions.primalmagic.charges_remaining", ConcoctionUtils.getCurrentDoses(stack)).mergeStyle(EffectType.BENEFICIAL.getColor()));
        FuseType fuse = ConcoctionUtils.getFuseType(stack);
        if (fuse == null) {
            fuse = FuseType.MEDIUM;
        }
        ITextComponent fuseText = new TranslationTextComponent(fuse.getTranslationKey()).mergeStyle(EffectType.BENEFICIAL.getColor());
        tooltip.add(new TranslationTextComponent("concoctions.primalmagic.fuse_length", fuseText).mergeStyle(EffectType.BENEFICIAL.getColor()));
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return super.hasEffect(stack) || !PotionUtils.getEffectsFromStack(stack).isEmpty();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)) {
            items.add(this.getDefaultInstance());   // Add basic water bomb separately
            for (Potion potion : Registry.POTION) {
                if (!potion.getEffects().isEmpty()) {
                    items.add(ConcoctionUtils.setFuseType(ConcoctionUtils.setConcoctionType(PotionUtils.addPotionToItemStack(new ItemStack(this), potion), ConcoctionType.BOMB), FuseType.MEDIUM));
                }
            }
        }
    }
}
