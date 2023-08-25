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

import com.verdantartifice.primalmagick.client.renderers.itemstack.ModularWandISTER;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.wands.IWandComponent;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
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
    public double getTotalCostModifier(ItemStack stack, Player player, Source source) {
        double mod = super.getTotalCostModifier(stack, player, source);
        
        // Subtract discount for wand core alignment, if any
        WandCore core = this.getWandCore(stack);
        if (core != null && core.getAlignedSources().contains(source)) {
            mod -= 0.05D;
        }
        
        return mod;
    }
    
    @Override
    public boolean isGlamoured(ItemStack stack) {
        return stack.hasTag() && (stack.getTag().contains("coreAppearance") || stack.getTag().contains("capAppearance") || stack.getTag().contains("gemAppearance"));
    }

    @Nullable
    public WandCore getWandCore(@Nonnull ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("core")) {
            return WandCore.getWandCore(stack.getTag().getString("core"));
        } else {
            return null;
        }
    }
    
    public void setWandCore(@Nonnull ItemStack stack, @Nonnull WandCore core) {
        stack.addTagElement("core", StringTag.valueOf(core.getTag()));
    }
    
    @Nullable
    public WandCore getWandCoreAppearance(@Nonnull ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("coreAppearance")) {
            return WandCore.getWandCore(stack.getTag().getString("coreAppearance"));
        } else {
            return this.getWandCore(stack);
        }
    }
    
    public void setWandCoreAppearance(@Nonnull ItemStack stack, @Nullable WandCore core) {
        if (core == null) {
            stack.removeTagKey("coreAppearance");
        } else {
            stack.addTagElement("coreAppearance", StringTag.valueOf(core.getTag()));
        }
    }
    
    @Nullable 
    public WandCap getWandCap(@Nonnull ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("cap")) {
            return WandCap.getWandCap(stack.getTag().getString("cap"));
        } else {
            return null;
        }
    }
    
    public void setWandCap(@Nonnull ItemStack stack, @Nonnull WandCap cap) {
        stack.addTagElement("cap", StringTag.valueOf(cap.getTag()));
    }
    
    @Nullable 
    public WandCap getWandCapAppearance(@Nonnull ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("capAppearance")) {
            return WandCap.getWandCap(stack.getTag().getString("capAppearance"));
        } else {
            return this.getWandCap(stack);
        }
    }
    
    public void setWandCapAppearance(@Nonnull ItemStack stack, @Nullable WandCap cap) {
        if (cap == null) {
            stack.removeTagKey("capAppearance");
        } else {
            stack.addTagElement("capAppearance", StringTag.valueOf(cap.getTag()));
        }
    }
    
    @Nullable
    public WandGem getWandGem(@Nonnull ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("gem")) {
            return WandGem.getWandGem(stack.getTag().getString("gem"));
        } else {
            return null;
        }
    }
    
    public void setWandGem(@Nonnull ItemStack stack, @Nonnull WandGem gem) {
        stack.addTagElement("gem", StringTag.valueOf(gem.getTag()));
    }
    
    @Nullable
    public WandGem getWandGemAppearance(@Nonnull ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("gemAppearance")) {
            return WandGem.getWandGem(stack.getTag().getString("gemAppearance"));
        } else {
            return this.getWandGem(stack);
        }
    }
    
    public void setWandGemAppearance(@Nonnull ItemStack stack, @Nullable WandGem gem) {
        if (gem == null) {
            stack.removeTagKey("gemAppearance");
        } else {
            stack.addTagElement("gemAppearance", StringTag.valueOf(gem.getTag()));
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
    
    @Override
    public Rarity getRarity(ItemStack stack) {
        // A modular wand's rarity is the highest of those of its components
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
        
        // Increase rarity if enchanted
        if (stack.isEnchanted()) {
            switch (retVal) {
            case COMMON:
            case UNCOMMON:
                return Rarity.RARE;
            case RARE:
                return Rarity.EPIC;
            case EPIC:
            default:
                return retVal;
            }
        } else {
            return retVal;
        }
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
        // Deserialize the list of inscribed spells from the given wand stack's NBT data
        List<SpellPackage> retVal = new ArrayList<>();
        if (stack != null) {
            ListTag spellTagsList = stack.getTag().getList("Spells", Tag.TAG_COMPOUND);
            for (int index = 0; index < spellTagsList.size(); index++) {
                CompoundTag spellTag = spellTagsList.getCompound(index);
                SpellPackage newSpell = new SpellPackage(spellTag);
                if (newSpell != null) {
                    retVal.add(newSpell);
                }
            }
        }
        return retVal;
    }
    
    @Override
    public int getSpellCount(ItemStack stack) {
        if (stack != null) {
            return stack.getTag().getList("Spells", Tag.TAG_COMPOUND).size();
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
        return (stack != null && stack.getTag().contains("ActiveSpell")) ? stack.getTag().getInt("ActiveSpell") : -1;
    }
    
    @Override
    public SpellPackage getActiveSpell(ItemStack stack) {
        // Deserialize the active inscribed spell from the given wand stack's NBT data
        SpellPackage retVal = null;
        if (stack != null) {
            ListTag spellTagsList = stack.getTag().getList("Spells", Tag.TAG_COMPOUND);
            int index = this.getActiveSpellIndex(stack);
            if (index >= 0 && index < spellTagsList.size()) {
                CompoundTag spellTag = spellTagsList.getCompound(index);
                retVal = new SpellPackage(spellTag);
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
            stack.addTagElement("ActiveSpell", IntTag.valueOf(index));
            return true;
        }
        return false;
    }

    @Override
    public boolean canAddSpell(ItemStack stack, SpellPackage spell) {
        if (stack == null || spell == null || spell.getPayload() == null) {
            return false;
        }
        
        // The number of spells which can be inscribed onto a wand is determined by its core
        WandCore core = this.getWandCore(stack);
        if (core == null) {
            return false;
        }
        
        // Determine the payload sources of all spells to be included in the given wand
        List<Source> spellSources = this.getSpells(stack).stream()
                .filter(p -> (p != null && p.getPayload() != null))
                .map(p -> p.getPayload().getSource())
                .collect(Collectors.toCollection(() -> new ArrayList<>()));
        spellSources.add(spell.getPayload().getSource());
        
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
            // Save the given spell into the wand stack's NBT data
            if (!stack.getTag().contains("Spells")) {
                ListTag newList = new ListTag();
                newList.add(spell.serializeNBT());
                stack.addTagElement("Spells", newList);
                return true;
            } else {
                return stack.getTag().getList("Spells", Tag.TAG_COMPOUND).add(spell.serializeNBT());
            }
        } else {
            return false;
        }
    }

    @Override
    public void clearSpells(ItemStack stack) {
        stack.getTag().remove("Spells");
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
