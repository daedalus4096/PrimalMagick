package com.verdantartifice.primalmagick.common.items.wands;

import com.verdantartifice.primalmagick.client.renderers.itemstack.ModularWandISTER;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentHelperPM;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.wands.IManaContainer;
import com.verdantartifice.primalmagick.common.wands.IWandComponent;
import com.verdantartifice.primalmagick.common.wands.ManaManager;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Item definition for a modular wand.  Modular wands are made up of cores, caps, and gems, and their
 * properties are determined by those components.
 * 
 * @author Daedalus4096
 */
public abstract class ModularWandItem extends AbstractWandItem implements IHasWandComponents {
    protected static final int BASE_CORE_REGEN_PER_TICK = 5;

    private BlockEntityWithoutLevelRenderer customRenderer;

    public ModularWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxMana(ItemStack stack, @Nullable Source source) {
        // The maximum amount of mana a wand can hold is determined by its gem
        if (stack == null) {
            return MundaneWandItem.MAX_MANA;
        }
        WandGem gem = this.getWandGem(stack);
        if (gem == null) {
            return MundaneWandItem.MAX_MANA;
        } else if (gem.getCapacity() == IManaContainer.INFINITE_MANA) {
            return IManaContainer.INFINITE_MANA;
        } else {
            return gem.getCapacity();
        }
    }
    
    @Override
    public int getBaseCostModifier(ItemStack stack) {
        // The base mana cost modifier of a wand is determined by its cap
        if (stack == null) {
            return 0;
        }
        WandCap cap = this.getWandCap(stack);
        return (cap == null) ? 0 : cap.getBaseCostModifier();
    }
    
    @Override
    public int getSiphonAmount(ItemStack stack) {
        // The siphon amount of a wand is determined by its cap
        if (stack == null) {
            return WandCap.IRON.getSiphonAmount();
        }
        WandCap cap = this.getWandCap(stack);
        return (cap == null) ? WandCap.IRON.getSiphonAmount() : cap.getSiphonAmount();
    }

    @Override
    public int getTotalCostModifier(ItemStack stack, Player player, Source source, HolderLookup.Provider registries) {
        int mod = super.getTotalCostModifier(stack, player, source, registries);
        
        // Add discount for wand core alignment, if any
        WandCore core = this.getWandCore(stack);
        if (core != null && core.getAlignedSources().contains(source)) {
            mod += 5;
        }
        
        return mod;
    }
    
    @Override
    public boolean isGlamoured(ItemStack stack) {
        return stack.has(DataComponentsPM.WAND_CORE_APPEARANCE.get()) || stack.has(DataComponentsPM.WAND_CAP_APPEARANCE.get()) || stack.has(DataComponentsPM.WAND_GEM_APPEARANCE.get());
    }

    @Nullable
    @Override
    public WandCore getWandCore(@Nonnull ItemStack stack) {
        return stack.get(DataComponentsPM.WAND_CORE.get());
    }
    
    @Override
    public void setWandCore(@Nonnull ItemStack stack, @Nonnull WandCore core) {
        stack.set(DataComponentsPM.WAND_CORE.get(), core);
        stack.set(DataComponents.RARITY, this.calculateRarity(stack));
    }
    
    @Nullable
    @Override
    public WandCore getWandCoreAppearance(@Nonnull ItemStack stack) {
        return stack.getOrDefault(DataComponentsPM.WAND_CORE_APPEARANCE.get(), this.getWandCore(stack));
    }
    
    @Override
    public void setWandCoreAppearance(@Nonnull ItemStack stack, @Nullable WandCore core) {
        if (core == null) {
            stack.remove(DataComponentsPM.WAND_CORE_APPEARANCE.get());
        } else {
            stack.set(DataComponentsPM.WAND_CORE_APPEARANCE.get(), core);
        }
    }
    
    @Nullable
    @Override
    public WandCap getWandCap(@Nonnull ItemStack stack) {
        return stack.get(DataComponentsPM.WAND_CAP.get());
    }
    
    @Override
    public void setWandCap(@Nonnull ItemStack stack, @Nonnull WandCap cap) {
        stack.set(DataComponentsPM.WAND_CAP.get(), cap);
        stack.set(DataComponents.RARITY, this.calculateRarity(stack));
    }
    
    @Nullable
    @Override
    public WandCap getWandCapAppearance(@Nonnull ItemStack stack) {
        return stack.getOrDefault(DataComponentsPM.WAND_CAP_APPEARANCE.get(), this.getWandCap(stack));
    }
    
    @Override
    public void setWandCapAppearance(@Nonnull ItemStack stack, @Nullable WandCap cap) {
        if (cap == null) {
            stack.remove(DataComponentsPM.WAND_CAP_APPEARANCE.get());
        } else {
            stack.set(DataComponentsPM.WAND_CAP_APPEARANCE.get(), cap);
        }
    }
    
    @Nullable
    @Override
    public WandGem getWandGem(@Nonnull ItemStack stack) {
        return stack.get(DataComponentsPM.WAND_GEM.get());
    }
    
    @Override
    public void setWandGem(@Nonnull ItemStack stack, @Nonnull WandGem gem) {
        stack.set(DataComponentsPM.WAND_GEM.get(), gem);
        stack.set(DataComponents.RARITY, this.calculateRarity(stack));
    }
    
    @Nullable
    @Override
    public WandGem getWandGemAppearance(@Nonnull ItemStack stack) {
        return stack.getOrDefault(DataComponentsPM.WAND_GEM_APPEARANCE.get(), this.getWandGem(stack));
    }
    
    @Override
    public void setWandGemAppearance(@Nonnull ItemStack stack, @Nullable WandGem gem) {
        if (gem == null) {
            stack.remove(DataComponentsPM.WAND_GEM_APPEARANCE.get());
        } else {
            stack.set(DataComponentsPM.WAND_GEM_APPEARANCE.get(), gem);
        }
    }
    
    @Nonnull
    protected List<IWandComponent> getComponents(@Nonnull ItemStack stack) {
        return Arrays.asList(this.getWandCore(stack), this.getWandCap(stack), this.getWandGem(stack)).stream().filter(Objects::nonNull).collect(Collectors.toList());
    }
    
    @Override
    public Component getName(ItemStack stack) {
        // A modular wand's display name is determined by its components (e.g. "Apprentice's Iron-Shod Heartwood Wand")
        WandCore core = this.getWandCore(stack);
        Component coreName = (core == null) ? Component.translatable("wand_core.primalmagick.unknown") : Component.translatable(core.getNameTranslationKey());
        WandCap cap = this.getWandCap(stack);
        Component capName = (cap == null) ? Component.translatable("wand_cap.primalmagick.unknown") : Component.translatable(cap.getNameTranslationKey());
        WandGem gem = this.getWandGem(stack);
        Component gemName = (gem == null) ? Component.translatable("wand_gem.primalmagick.unknown") : Component.translatable(gem.getNameTranslationKey());
        return Component.translatable("item.primalmagick.modular_wand", gemName, capName, coreName);
    }
    
    protected Rarity calculateRarity(ItemStack stack) {
        // A modular wand's unenchanted rarity is the highest of those of its components
        Rarity retVal = Rarity.COMMON;
        WandCore core = this.getWandCore(stack);
        if (core != null && core.getRarity().compareTo(retVal) > 0) {
            retVal = core.getRarity();
        }
        WandCap cap = this.getWandCap(stack);
        if (cap != null && cap.getRarity().compareTo(retVal) > 0) {
            retVal = cap.getRarity();
        }
        WandGem gem = this.getWandGem(stack);
        if (gem != null && gem.getRarity().compareTo(retVal) > 0) {
            retVal = gem.getRarity();
        }
        return retVal;
    }
    
    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    public static void registerCreativeTabItems(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output, Supplier<? extends ItemLike> itemSupplier) {
        Item item = itemSupplier.get().asItem();
        if (item instanceof ModularWandItem wandItem) {
            ItemStack stack = new ItemStack(wandItem);
            wandItem.setWandCore(stack, WandCore.HEARTWOOD);
            wandItem.setWandCap(stack, WandCap.IRON);
            wandItem.setWandGem(stack, WandGem.APPRENTICE);
            stack.set(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.emptyWand(WandGem.APPRENTICE.getCapacity()));
            output.accept(stack);
            
            stack = new ItemStack(wandItem);
            wandItem.setWandCore(stack, WandCore.HEARTWOOD);
            wandItem.setWandCap(stack, WandCap.IRON);
            wandItem.setWandGem(stack, WandGem.CREATIVE);
            stack.set(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.emptyWand(ManaStorage.INFINITE));
            output.accept(stack);
        }
    }

    protected int getCoreRegenPerTick(ItemStack wandStack, LivingEntity wielderEntity) {
        return BASE_CORE_REGEN_PER_TICK + EnchantmentHelperPM.getEquippedEnchantmentLevel(wielderEntity, EnchantmentsPM.PONDERING);
    }
    
    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        
        // Smoothly regenerate one mana per second for core-aligned sources
        WandCore core = this.getWandCore(stack);
        if (core != null && entityIn instanceof Player player) {
            int regenAmount = this.getCoreRegenPerTick(stack, player);
            for (Source alignedSource : core.getAlignedSources()) {
                int maxMana = ManaManager.getMaxMana(player, alignedSource);
                int curMana = ManaManager.getMana(player, alignedSource);
                double targetMax = (0.1D * maxMana);
                if (maxMana != IManaContainer.INFINITE_MANA && curMana < targetMax) {
                    ManaManager.addMana(player, stack, alignedSource, regenAmount, (int)targetMax);
                }
            }
        }
    }
    
    protected int getCoreSpellSlotCount(@Nullable WandCore core) {
        return (core == null) ? 0 : core.getSpellSlots();
    }

    @Override
    public Component getSpellCapacityText(ItemStack stack) {
        if (stack == null) {
            return Component.translatable("tooltip.primalmagick.spells.capacity", 0);
        } else {
            WandCore core = this.getWandCore(stack);
            if (core == null) {
                return Component.translatable("tooltip.primalmagick.spells.capacity", 0);
            } else {
                int baseSlots = this.getCoreSpellSlotCount(core);
                Source bonusSlot = core.getBonusSlot();
                if (bonusSlot == null) {
                    return Component.translatable("tooltip.primalmagick.spells.capacity", baseSlots);
                } else {
                    Component bonusText = Component.translatable(bonusSlot.getNameTranslationKey());
                    return Component.translatable("tooltip.primalmagick.spells.capacity_with_bonus", baseSlots, bonusText);
                }
            }
        }
    }

    @Override
    public boolean canAddSpell(ItemStack stack, SpellPackage spell) {
        if (stack == null || spell == null || spell.payload() == null) {
            return false;
        }
        
        // The number of spells which can be inscribed onto a wand is determined by its core
        WandCore core = this.getWandCore(stack);
        if (core == null) {
            return false;
        }
        
        // Determine the payload sources of all spells to be included in the given wand
        List<Source> spellSources = this.getSpells(stack).stream()
                .filter(p -> (p != null && p.payload() != null))
                .map(p -> p.payload().getComponent().getSource())
                .collect(Collectors.toCollection(ArrayList::new));
        spellSources.add(spell.payload().getComponent().getSource());
        
        int coreSlots = this.getCoreSpellSlotCount(core);
        if (spellSources.size() < coreSlots + 1) {
            // If the spells fit in the base slots without the bonus, then it's fine
            return true;
        } else if (spellSources.size() > coreSlots + 1) {
            // If the bonus slot wouldn't be enough to make them fit, then reject
            return false;
        } else {
            // If using exactly the slot count plus bonus slot, only allow if one of the spells is of the same source as the bonus
            return core.getBonusSlot() != null && spellSources.contains(core.getBonusSlot());
        }
    }

    @Override
    public Supplier<BlockEntityWithoutLevelRenderer> getCustomRendererSupplier() {
        if (this.customRenderer == null) {
            this.customRenderer = this.getCustomRendererSupplierUncached().get();
        }
        return () -> this.customRenderer;
    }

    @Override
    public Supplier<BlockEntityWithoutLevelRenderer> getCustomRendererSupplierUncached() {
        return ModularWandISTER::new;
    }
}
