package com.verdantartifice.primalmagick.common.items.concoctions;

import com.verdantartifice.primalmagick.common.concoctions.ConcoctionType;
import com.verdantartifice.primalmagick.common.concoctions.ConcoctionUtils;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Definition of an alchemical concoction.  Similar to a brewed potion, but a single vial contains
 * multiple doses.
 * 
 * @author Daedalus4096
 */
public class ConcoctionItem extends Item {
    public static final CauldronInteraction FILL_EMPTY_CAULDRON = (BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) -> {
        if (stack.has(DataComponents.POTION_CONTENTS) && stack.get(DataComponents.POTION_CONTENTS).is(Potions.WATER)) {
            if (!level.isClientSide()) {
                Item item = stack.getItem();
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(ItemsPM.SKYGLASS_FLASK.get())));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(item));
                level.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState());
                level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    };
    public static final CauldronInteraction FILL_WATER_CAULDRON = (BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) -> {
        if (state.getValue(LayeredCauldronBlock.LEVEL) < 3 && stack.has(DataComponents.POTION_CONTENTS) && stack.get(DataComponents.POTION_CONTENTS).is(Potions.WATER)) {
            if (!level.isClientSide()) {
                Item item = stack.getItem();
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(ItemsPM.SKYGLASS_FLASK.get())));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(item));
                level.setBlockAndUpdate(pos, state.cycle(LayeredCauldronBlock.LEVEL));
                level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    };
    
    public ConcoctionItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    @NotNull
    public ItemStack getDefaultInstance() {
        return ConcoctionUtils.setConcoctionType(PotionContents.createItemStack(this, Potions.WATER), ConcoctionType.WATER);
    }

    @Override
    @NotNull
    public ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull LivingEntity entityLiving) {
        Player player = entityLiving instanceof Player ? (Player)entityLiving : null;

        if (worldIn instanceof ServerLevel serverLevel) {
            PotionContents contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            contents.forEachEffect(instance -> {
                if (instance.getEffect().value().isInstantenous()) {
                    instance.getEffect().value().applyInstantenousEffect(serverLevel, player, player, entityLiving, instance.getAmplifier(), 1.0D);
                } else {
                    entityLiving.addEffect(new MobEffectInstance(instance));
                }
            }, 1F);
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
    @NotNull
    public ItemUseAnimation getUseAnimation(@NotNull ItemStack stack) {
        return ItemUseAnimation.DRINK;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity pEntity) {
        return 32;
    }

    @Override
    @NotNull
    public InteractionResult use(@NotNull Level worldIn, @NotNull Player playerIn, @NotNull InteractionHand handIn) {
        return ItemUtils.startUsingInstantly(worldIn, playerIn, handIn);
    }

    @Override
    @NotNull
    public Component getName(@NotNull ItemStack stack) {
        ConcoctionType type = ConcoctionUtils.getConcoctionType(stack);
        PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
        if (contents == null) {
            contents = new PotionContents(Potions.WATER);
        }
        return contents.getName(this.descriptionId + "." + type.getSerializedName() + ".effect.");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, @NotNull TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.accept(Component.translatable("concoctions.primalmagick.doses_remaining", ConcoctionUtils.getCurrentDoses(stack)).withStyle(MobEffectCategory.BENEFICIAL.getTooltipFormatting()));
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return super.isFoil(stack) || stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).hasEffects();
    }

    public static void registerCreativeTabItems(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output, Supplier<? extends ItemLike> itemSupplier) {
        Item item = itemSupplier.get().asItem();
        output.accept(item.getDefaultInstance());    // Add basic water concoction separately
        for (ConcoctionType concoctionType : ConcoctionType.values()) {
            if (concoctionType.hasDrinkablePotion()) {
                params.holders().lookup(Registries.POTION).ifPresent(registryLookup -> {
                    registryLookup.listElements().filter(potionRef -> {
                        return !potionRef.value().getEffects().isEmpty() && ConcoctionUtils.hasBeneficialEffect(potionRef.value());
                    }).map(potionRef -> {
                        return ConcoctionUtils.setConcoctionType(PotionContents.createItemStack(item, potionRef), concoctionType);
                    }).forEach(output::accept);
                });
            }
        }
    }
}
