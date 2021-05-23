package com.verdantartifice.primalmagic.common.items.concoctions;

import java.util.List;

import com.verdantartifice.primalmagic.common.concoctions.ConcoctionType;
import com.verdantartifice.primalmagic.common.concoctions.ConcoctionUtils;
import com.verdantartifice.primalmagic.common.items.ItemsPM;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Definition of an alchemical concoction.  Similar to a brewed potion, but a single vial contains
 * multiple doses.
 * 
 * @author Daedalus4096
 */
public class ConcoctionItem extends Item {
    public ConcoctionItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getDefaultInstance() {
        return ConcoctionUtils.setConcoctionType(PotionUtils.addPotionToItemStack(super.getDefaultInstance(), Potions.WATER), ConcoctionType.WATER);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        PlayerEntity player = entityLiving instanceof PlayerEntity ? (PlayerEntity)entityLiving : null;

        if (!worldIn.isRemote) {
            for (EffectInstance instance : PotionUtils.getEffectsFromStack(stack)) {
                if (instance.getPotion().isInstant()) {
                    instance.getPotion().affectEntity(player, player, entityLiving, instance.getAmplifier(), 1.0D);
                } else {
                    entityLiving.addPotionEffect(new EffectInstance(instance));
                }
            }
        }
        
        if (player != null) {
            // TODO Increment stat?
            if (!player.abilities.isCreativeMode) {
                int doses = ConcoctionUtils.getCurrentDoses(stack);
                if (doses <= 1) {
                    stack.shrink(1);
                } else {
                    ConcoctionUtils.setCurrentDoses(stack, doses - 1);
                }
            }
        }
        
        return stack.isEmpty() ? new ItemStack(ItemsPM.SKYGLASS_FLASK.get()) : stack;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        return DrinkHelper.startDrinking(worldIn, playerIn, handIn);
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

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
        tooltip.add(new TranslationTextComponent("concoctions.primalmagic.doses_remaining", ConcoctionUtils.getCurrentDoses(stack)).mergeStyle(EffectType.BENEFICIAL.getColor()));
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return super.hasEffect(stack) || !PotionUtils.getEffectsFromStack(stack).isEmpty();
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)) {
            items.add(this.getDefaultInstance());   // Add basic water concoction separately
            for (ConcoctionType concoctionType : ConcoctionType.values()) {
                if (concoctionType != ConcoctionType.WATER) {
                    for (Potion potion : ForgeRegistries.POTION_TYPES.getValues()) {
                        if (ConcoctionUtils.hasBeneficialEffect(potion)) {
                            items.add(ConcoctionUtils.setConcoctionType(PotionUtils.addPotionToItemStack(new ItemStack(this), potion), concoctionType));
                        }
                    }
                }
            }
        }
    }
}
