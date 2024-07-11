package com.verdantartifice.primalmagick.common.items.wands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.verdantartifice.primalmagick.client.renderers.itemstack.ModularWandISTER;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.wands.IWandComponent;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

/**
 * Item definition for a modular wand.  Modular wands are made up of cores, caps, and gems, and their
 * properties are determined by those components.
 * 
 * @author Daedalus4096
 */
public class ModularWandItem extends AbstractWandItem {
    public ModularWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxMana(ItemStack stack) {
        // The maximum amount of real mana a wand can hold is determined by its gem
        if (stack == null) {
            return 2500;
        }
        WandGem gem = this.getWandGem(stack);
        if (gem == null) {
            return 2500;
        } else if (gem.getCapacity() == -1) {
            return -1;
        } else {
            return 100 * gem.getCapacity();
        }
    }
    
    @Override
    public double getBaseCostModifier(ItemStack stack) {
        // The base mana cost modifier of a wand is determined by its cap
        if (stack == null) {
            return 1.2D;
        }
        WandCap cap = this.getWandCap(stack);
        return (cap == null) ? 1.2D : cap.getBaseCostModifier();
    }
    
    @Override
    public int getSiphonAmount(ItemStack stack) {
        // The siphon amount of a wand is determined by its cap
        if (stack == null) {
            return 1;
        }
        WandCap cap = this.getWandCap(stack);
        return (cap == null) ? 1 : cap.getSiphonAmount();
    }

    @Override
    public double getTotalCostModifier(ItemStack stack, Player player, Source source, HolderLookup.Provider registries) {
        double mod = super.getTotalCostModifier(stack, player, source, registries);
        
        // Subtract discount for wand core alignment, if any
        WandCore core = this.getWandCore(stack);
        if (core != null && core.getAlignedSources().contains(source)) {
            mod -= 0.05D;
        }
        
        return mod;
    }
    
    @Override
    public boolean isGlamoured(ItemStack stack) {
        return stack.has(DataComponentsPM.WAND_CORE_APPEARANCE.get()) || stack.has(DataComponentsPM.WAND_CAP_APPEARANCE.get()) || stack.has(DataComponentsPM.WAND_GEM_APPEARANCE.get());
    }

    @Nullable
    public WandCore getWandCore(@Nonnull ItemStack stack) {
        return stack.get(DataComponentsPM.WAND_CORE.get());
    }
    
    public void setWandCore(@Nonnull ItemStack stack, @Nonnull WandCore core) {
        stack.set(DataComponentsPM.WAND_CORE.get(), core);
        stack.set(DataComponents.RARITY, this.calculateRarity(stack));
    }
    
    @Nullable
    public WandCore getWandCoreAppearance(@Nonnull ItemStack stack) {
        return stack.get(DataComponentsPM.WAND_CORE_APPEARANCE.get());
    }
    
    public void setWandCoreAppearance(@Nonnull ItemStack stack, @Nullable WandCore core) {
        if (core == null) {
            stack.remove(DataComponentsPM.WAND_CORE_APPEARANCE.get());
        } else {
            stack.set(DataComponentsPM.WAND_CORE_APPEARANCE.get(), core);
        }
    }
    
    @Nullable 
    public WandCap getWandCap(@Nonnull ItemStack stack) {
        return stack.get(DataComponentsPM.WAND_CAP.get());
    }
    
    public void setWandCap(@Nonnull ItemStack stack, @Nonnull WandCap cap) {
        stack.set(DataComponentsPM.WAND_CAP.get(), cap);
        stack.set(DataComponents.RARITY, this.calculateRarity(stack));
    }
    
    @Nullable 
    public WandCap getWandCapAppearance(@Nonnull ItemStack stack) {
        return stack.get(DataComponentsPM.WAND_CAP_APPEARANCE.get());
    }
    
    public void setWandCapAppearance(@Nonnull ItemStack stack, @Nullable WandCap cap) {
        if (cap == null) {
            stack.remove(DataComponentsPM.WAND_CAP_APPEARANCE.get());
        } else {
            stack.set(DataComponentsPM.WAND_CAP_APPEARANCE.get(), cap);
        }
    }
    
    @Nullable
    public WandGem getWandGem(@Nonnull ItemStack stack) {
        return stack.get(DataComponentsPM.WAND_GEM.get());
    }
    
    public void setWandGem(@Nonnull ItemStack stack, @Nonnull WandGem gem) {
        stack.set(DataComponentsPM.WAND_GEM.get(), gem);
        stack.set(DataComponents.RARITY, this.calculateRarity(stack));
    }
    
    @Nullable
    public WandGem getWandGemAppearance(@Nonnull ItemStack stack) {
        return stack.get(DataComponentsPM.WAND_GEM_APPEARANCE.get());
    }
    
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
    
    @Override
    public int getEnchantmentValue(ItemStack stack) {
        // The enchantability of a wand is determined by its components
        return this.getComponents(stack).stream().mapToInt(IWandComponent::getEnchantability).sum();
    }
    
    public static void registerCreativeTabItems(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output, Supplier<? extends ItemLike> itemSupplier) {
        Item item = itemSupplier.get().asItem();
        if (item instanceof ModularWandItem wandItem) {
            ItemStack stack = new ItemStack(wandItem);
            wandItem.setWandCore(stack, WandCore.HEARTWOOD);
            wandItem.setWandCap(stack, WandCap.IRON);
            wandItem.setWandGem(stack, WandGem.APPRENTICE);
            output.accept(stack);
            
            stack = new ItemStack(wandItem);
            wandItem.setWandCore(stack, WandCore.HEARTWOOD);
            wandItem.setWandCap(stack, WandCap.IRON);
            wandItem.setWandGem(stack, WandGem.CREATIVE);
            output.accept(stack);
        }
    }
    
    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        
        // Regenerate one mana per second for core-aligned sources
        if (stack != null && entityIn.tickCount % 20 == 0) {
            int maxMana = this.getMaxMana(stack);
            WandCore core = this.getWandCore(stack);
            if (core != null && maxMana != -1) {
                for (Source alignedSource : core.getAlignedSources()) {
                    int curMana = this.getMana(stack, alignedSource);
                    if (curMana < (0.1D * maxMana)) {
                        this.addMana(stack, alignedSource, 100, (int)(0.1D * maxMana));
                    }
                }
            }
        }
    }
    
    protected int getCoreSpellSlotCount(@Nullable WandCore core) {
        return (core == null) ? 0 : core.getSpellSlots();
    }

    @Override
    public List<SpellPackage> getSpells(ItemStack stack) {
        // Deserialize the list of inscribed spells from the given wand stack's data
        return ImmutableList.copyOf(stack.getOrDefault(DataComponentsPM.SPELL_PACKAGE_LIST.get(), ImmutableList.of()));
    }
    
    @Override
    public int getSpellCount(ItemStack stack) {
        if (stack != null) {
            return stack.getOrDefault(DataComponentsPM.SPELL_PACKAGE_LIST.get(), ImmutableList.of()).size();
        } else {
            return 0;
        }
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
    public int getActiveSpellIndex(ItemStack stack) {
        // Return -1 if no active spell is selected
        return stack.getOrDefault(DataComponentsPM.ACTIVE_SPELL_INDEX.get(), -1);
    }
    
    @Override
    public SpellPackage getActiveSpell(ItemStack stack) {
        // Deserialize the active inscribed spell from the given wand stack's NBT data
        SpellPackage retVal = null;
        if (stack != null) {
            List<SpellPackage> spellList = stack.get(DataComponentsPM.SPELL_PACKAGE_LIST.get());
            int index = this.getActiveSpellIndex(stack);
            if (spellList != null && index >= 0 && index < spellList.size()) {
                retVal = spellList.get(index);
            }
        }
        return retVal;
    }

    @Override
    public boolean setActiveSpellIndex(ItemStack stack, int index) {
        if (stack == null) {
            return false;
        } else if (index >= -1 && index < this.getSpells(stack).size()) {
            // -1 is a valid value and means "no active spell"
            stack.set(DataComponentsPM.ACTIVE_SPELL_INDEX.get(), index);
            return true;
        }
        return false;
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
                .collect(Collectors.toCollection(() -> new ArrayList<>()));
        spellSources.add(spell.payload().getComponent().getSource());
        
        int coreSlots = this.getCoreSpellSlotCount(core);
        if (spellSources.size() < coreSlots + 1) {
            // If the spells would fit in the base slots without the bonus, then it's fine
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
    public boolean addSpell(ItemStack stack, SpellPackage spell) {
        if (this.canAddSpell(stack, spell)) {
            // Save the given spell into the wand stack's data
            stack.set(DataComponentsPM.SPELL_PACKAGE_LIST.get(), ImmutableList.<SpellPackage>builder().addAll(this.getSpells(stack)).add(spell).build());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void clearSpells(ItemStack stack) {
        stack.remove(DataComponentsPM.SPELL_PACKAGE_LIST.get());
        stack.remove(DataComponentsPM.ACTIVE_SPELL_INDEX.get());
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            final BlockEntityWithoutLevelRenderer renderer = new ModularWandISTER();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }
}
