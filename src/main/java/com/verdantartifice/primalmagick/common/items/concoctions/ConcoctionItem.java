package com.verdantartifice.primalmagick.common.items.concoctions;

import java.util.List;
import java.util.function.Supplier;

import com.verdantartifice.primalmagick.common.concoctions.ConcoctionType;
import com.verdantartifice.primalmagick.common.concoctions.ConcoctionUtils;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;

import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

/**
 * Definition of an alchemical concoction.  Similar to a brewed potion, but a single vial contains
 * multiple doses.
 * 
 * @author Daedalus4096
 */
public class ConcoctionItem extends Item {
    public static final CauldronInteraction FILL_EMPTY_CAULDRON = (BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) -> {
        if (PotionUtils.getPotion(stack) == Potions.WATER) {
            if (!level.isClientSide) {
                Item item = stack.getItem();
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(ItemsPM.SKYGLASS_FLASK.get())));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(item));
                level.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState());
                level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    };
    public static final CauldronInteraction FILL_WATER_CAULDRON = (BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) -> {
        if (state.getValue(LayeredCauldronBlock.LEVEL) < 3 && PotionUtils.getPotion(stack) == Potions.WATER) {
            if (!level.isClientSide) {
                Item item = stack.getItem();
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(ItemsPM.SKYGLASS_FLASK.get())));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(item));
                level.setBlockAndUpdate(pos, state.cycle(LayeredCauldronBlock.LEVEL));
                level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    };
    
    public ConcoctionItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getDefaultInstance() {
        return ConcoctionUtils.setConcoctionType(PotionUtils.setPotion(super.getDefaultInstance(), Potions.WATER), ConcoctionType.WATER);
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

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
        tooltip.add(Component.translatable("concoctions.primalmagick.doses_remaining", ConcoctionUtils.getCurrentDoses(stack)).withStyle(MobEffectCategory.BENEFICIAL.getTooltipFormatting()));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return super.isFoil(stack) || !PotionUtils.getMobEffects(stack).isEmpty();
    }

    public static void registerCreativeTabItems(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output, Supplier<? extends ItemLike> itemSupplier) {
        Item item = itemSupplier.get().asItem();
        output.accept(item.getDefaultInstance());    // Add basic water concoction separately
        for (ConcoctionType concoctionType : ConcoctionType.values()) {
            if (concoctionType.hasDrinkablePotion()) {
                params.holders().lookup(Registries.POTION).ifPresent(registryLookup -> {
                    registryLookup.listElements().filter(potionRef -> {
                        return !potionRef.is(Potions.EMPTY_ID) && ConcoctionUtils.hasBeneficialEffect(potionRef.value());
                    }).map(potionRef -> {
                        return ConcoctionUtils.setConcoctionType(PotionUtils.setPotion(new ItemStack(item), potionRef.value()), concoctionType);
                    }).forEach(stack -> {
                        output.accept(stack);
                    });
                });
            }
        }
    }
}
