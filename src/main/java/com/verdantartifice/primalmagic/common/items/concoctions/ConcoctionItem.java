package com.verdantartifice.primalmagic.common.items.concoctions;

import java.util.List;

import com.verdantartifice.primalmagic.common.concoctions.ConcoctionType;
import com.verdantartifice.primalmagic.common.concoctions.ConcoctionUtils;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.stats.StatsManager;
import com.verdantartifice.primalmagic.common.stats.StatsPM;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
        return ConcoctionUtils.setConcoctionType(PotionUtils.setPotion(super.getDefaultInstance(), Potions.WATER), ConcoctionType.WATER);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState blockState = world.getBlockState(pos);
        if (blockState.getBlock() == Blocks.CAULDRON && PotionUtils.getPotion(stack) == Potions.WATER) {
            int waterLevel = blockState.getValue(CauldronBlock.LEVEL);
            if (waterLevel < 3 && !world.isClientSide) {
                Player player = context.getPlayer();
                if (!player.getAbilities().instabuild) {
                    player.setItemInHand(context.getHand(), new ItemStack(ItemsPM.SKYGLASS_FLASK.get()));
                    if (player instanceof ServerPlayer) {
                        ((ServerPlayer)player).refreshContainer(player.inventoryMenu);
                    }
                }
                world.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                ((CauldronBlock)Blocks.CAULDRON).setWaterLevel(world, pos, blockState, waterLevel + 1);
            }
            return InteractionResult.sidedSuccess(world.isClientSide);
        }
        return super.useOn(context);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        Player player = entityLiving instanceof Player ? (Player)entityLiving : null;

        if (!worldIn.isClientSide) {
            for (MobEffectInstance instance : PotionUtils.getMobEffects(stack)) {
                if (instance.getEffect().isInstantenous()) {
                    instance.getEffect().applyInstantenousEffect(player, player, entityLiving, instance.getAmplifier(), 1.0D);
                } else {
                    entityLiving.addEffect(new MobEffectInstance(instance));
                }
            }
        }
        
        if (player != null) {
            StatsManager.incrementValue(player, StatsPM.CONCOCTIONS_USED);
            if (!player.getAbilities().instabuild) {
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
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        return ItemUtils.startUsingInstantly(worldIn, playerIn, handIn);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        Potion potion = PotionUtils.getPotion(stack);
        ConcoctionType type = ConcoctionUtils.getConcoctionType(stack);
        if (type == null) {
            potion = Potions.EMPTY;
            type = ConcoctionType.WATER;
        }
        return potion.getName(this.getDescriptionId() + "." + type.getSerializedName() + ".effect.");
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
        tooltip.add(new TranslatableComponent("concoctions.primalmagic.doses_remaining", ConcoctionUtils.getCurrentDoses(stack)).withStyle(MobEffectCategory.BENEFICIAL.getTooltipFormatting()));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return super.isFoil(stack) || !PotionUtils.getMobEffects(stack).isEmpty();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (this.allowdedIn(group)) {
            items.add(this.getDefaultInstance());   // Add basic water concoction separately
            for (ConcoctionType concoctionType : ConcoctionType.values()) {
                if (concoctionType.hasDrinkablePotion()) {
                    for (Potion potion : Registry.POTION) { // Use Vanilla registry to preserve item ordering
                        if (ConcoctionUtils.hasBeneficialEffect(potion)) {
                            items.add(ConcoctionUtils.setConcoctionType(PotionUtils.setPotion(new ItemStack(this), potion), concoctionType));
                        }
                    }
                }
            }
        }
    }
}
