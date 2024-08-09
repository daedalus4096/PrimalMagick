package com.verdantartifice.primalmagick.common.items.wands;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.spells.SpellManager;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * Item definition for a filled spell scroll.  These can be used to cast their held spell directly or
 * to be inscribed onto a wand.
 * 
 * @author Daedalus4096
 */
public class SpellScrollItem extends Item {
    protected static final Component TOOLTIP = Component.translatable("tooltip.primalmagick.spell_scroll").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY);
    
    public SpellScrollItem() {
        super(new Item.Properties());
    }
    
    @Nullable
    public SpellPackage getSpell(@Nonnull ItemStack stack) {
        // Get the held spell from the given scroll stack's data
        return stack.get(DataComponentsPM.SPELL_PACKAGE.get());
    }
    
    public void setSpell(@Nonnull ItemStack stack, @Nonnull SpellPackage spell) {
        // Save the given spell into the scroll stack's data
        stack.set(DataComponentsPM.SPELL_PACKAGE.get(), spell);
        stack.set(DataComponents.RARITY, spell == null ? Rarity.COMMON : spell.getRarity());
    }

    @Override
    public Component getName(ItemStack stack) {
        // A scroll's name is determined by that of the spell it holds (e.g. "Scroll of Lightning Bolt")
        SpellPackage spell = this.getSpell(stack);
        Component spellName = (spell == null) ? Component.translatable("tooltip.primalmagick.none") : spell.getDisplayName();
        return Component.translatable(this.getDescriptionId(stack), spellName).withStyle(ChatFormatting.ITALIC);
    }
    
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.addAll(SpellManager.getSpellPackageDetailTooltip(this.getSpell(stack), stack, false, context.registries()));
        tooltip.add(TOOLTIP);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        // Cast the held spell, if any, and consume the scroll
        ItemStack stack = playerIn.getItemInHand(handIn);
        playerIn.startUsingItem(handIn);
        if (!worldIn.isClientSide) {
            SpellPackage spell = this.getSpell(stack);
            // Check to see if the player's spells are on cooldown
            if (spell != null && !SpellManager.isOnCooldown(playerIn)) {
                SpellManager.setCooldown(playerIn, spell.getCooldownTicks());
                spell.cast(worldIn, playerIn, stack);
                if (!playerIn.hasInfiniteMaterials()) {
                    stack.shrink(1);
                }
                return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
            } else {
                return new InteractionResultHolder<>(InteractionResult.FAIL, stack);
            }
        } else {
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
        }
    }
    
    @Override
    public void onCraftedBy(ItemStack stack, Level worldIn, Player playerIn) {
        // Increment spell crafting statistics
        super.onCraftedBy(stack, worldIn, playerIn);
        SpellPackage spell = this.getSpell(stack);
        if (spell != null) {
            StatsManager.incrementValue(playerIn, StatsPM.SPELLS_CRAFTED, stack.getCount());
            StatsManager.setValueIfMax(playerIn, StatsPM.SPELLS_CRAFTED_MAX_COST, spell.getManaCost().getManaSize());
        }
    }
}
