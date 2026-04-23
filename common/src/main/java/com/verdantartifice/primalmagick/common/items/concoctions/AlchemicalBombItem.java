package com.verdantartifice.primalmagick.common.items.concoctions;

import com.verdantartifice.primalmagick.common.concoctions.ConcoctionType;
import com.verdantartifice.primalmagick.common.concoctions.ConcoctionUtils;
import com.verdantartifice.primalmagick.common.concoctions.FuseType;
import com.verdantartifice.primalmagick.common.entities.projectiles.AlchemicalBombEntity;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

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
    @NotNull
    public ItemStack getDefaultInstance() {
        return ConcoctionUtils.setFuseType(ConcoctionUtils.setConcoctionType(PotionContents.createItemStack(this, Potions.WATER), ConcoctionType.WATER), FuseType.MEDIUM);
    }

    @Override
    @NotNull
    public InteractionResult use(@NotNull Level worldIn, @NotNull Player playerIn, @NotNull InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        if (playerIn.isSecondaryUseActive()) {
            // Set bomb fuse
            FuseType fuse = ConcoctionUtils.getFuseType(stack);
            if (!worldIn.isClientSide() && fuse != null && fuse.getNext() != null) {
                playerIn.setItemInHand(handIn, ConcoctionUtils.setFuseType(stack, fuse.getNext()));
                Component fuseText = Component.translatable(fuse.getNext().getTranslationKey());
                playerIn.sendSystemMessage(Component.translatable("concoctions.primalmagick.fuse_set", fuseText));
            }
        } else {
            worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.SPLASH_POTION_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (worldIn.getRandom().nextFloat() * 0.4F + 0.8F));
            worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            
            // Throw bomb entity
            AlchemicalBombEntity entity = new AlchemicalBombEntity(worldIn, playerIn);
            entity.setItem(stack);
            entity.shootFromRotation(playerIn, playerIn.getXRot(), playerIn.getYRot(), -20.0F, 0.5F, 1.0F);
            worldIn.addFreshEntity(entity);
            
            // Increment stat
            StatsManager.incrementValue(playerIn, StatsPM.CONCOCTIONS_USED);

            // Deduct charge
            if (!playerIn.getAbilities().instabuild) {
                int charges = ConcoctionUtils.getCurrentDoses(stack);
                if (charges <= 1) {
                    stack.shrink(1);
                } else {
                    ConcoctionUtils.setCurrentDoses(stack, charges - 1);
                }
            }
        }
        return InteractionResult.SUCCESS_SERVER.heldItemTransformedTo(stack);
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
    public void appendHoverText(@NotNull ItemStack stack, @NotNull Item.TooltipContext context, @NotNull TooltipDisplay tooltipDisplay, @NotNull Consumer<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.accept(Component.translatable("concoctions.primalmagick.charges_remaining", ConcoctionUtils.getCurrentDoses(stack)).withStyle(MobEffectCategory.BENEFICIAL.getTooltipFormatting()));
        FuseType fuse = ConcoctionUtils.getFuseType(stack);
        if (fuse == null) {
            fuse = FuseType.MEDIUM;
        }
        Component fuseText = Component.translatable(fuse.getTranslationKey()).withStyle(MobEffectCategory.BENEFICIAL.getTooltipFormatting());
        tooltip.accept(Component.translatable("concoctions.primalmagick.fuse_length", fuseText).withStyle(MobEffectCategory.BENEFICIAL.getTooltipFormatting()));
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return super.isFoil(stack) || stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).hasEffects();
    }

    public static void registerCreativeTabItems(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output, Supplier<? extends ItemLike> itemSupplier) {
        Item item = itemSupplier.get().asItem();
        output.accept(item.getDefaultInstance());    // Add basic water bomb separately
        params.holders().lookup(Registries.POTION).ifPresent(registryLookup ->
                registryLookup.listElements()
                        .filter(potionRef -> !potionRef.value().getEffects().isEmpty())
                        .map(potionRef -> ConcoctionUtils.setFuseType(ConcoctionUtils.setConcoctionType(PotionContents.createItemStack(item, potionRef), ConcoctionType.BOMB), FuseType.MEDIUM))
                        .forEach(output::accept));
    }
}
