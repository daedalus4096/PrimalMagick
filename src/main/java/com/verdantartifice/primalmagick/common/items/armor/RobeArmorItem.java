package com.verdantartifice.primalmagick.common.items.armor;

import java.util.Optional;

import com.verdantartifice.primalmagick.common.armortrim.TrimPatternsPM;
import com.verdantartifice.primalmagick.common.items.misc.RuneItem;
import com.verdantartifice.primalmagick.common.runes.SourceRune;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;

/**
 * Item definition for armor that gives a mana discount.  Intended for wizard robes.
 * 
 * @author Daedalus4096
 */
public class RobeArmorItem extends ArmorItem implements IManaDiscountGear {
    protected final int manaDiscount;
    
    public RobeArmorItem(ArmorMaterial material, ArmorItem.Type type, int manaDiscount, Item.Properties properties) {
        super(material, type, properties);
        this.manaDiscount = manaDiscount;
    }
    
    @Override
    public int getManaDiscount(ItemStack stack, Player player, Source source) {
        Optional<Source> sourceOpt = this.getAttunedSource(stack, player);
        if (sourceOpt.isPresent()) {
            if (sourceOpt.get().equals(source)) {
                // If the robe has runic armor trim of the given source, return the bonus-multiplied discount
                return 2 * this.manaDiscount;
            } else {
                // If the robe has runic armor trim of a different source, then no discount is provided for the given source
                return 0;
            }
        } else {
            // If the robe has no trim or a different trim, return the base discount
            return this.manaDiscount;
        }
    }

    @Override
    public int getBestManaDiscount(ItemStack stack, Player player) {
        Optional<ArmorTrim> trimOpt = player == null ? Optional.empty() : ArmorTrim.getTrim(player.level().registryAccess(), stack, true);
        if (trimOpt.isPresent() && trimOpt.get().pattern().is(TrimPatternsPM.RUNIC)) {
            // If the robe has runic armor trim, return the bonus-multiplied discount for its attuned source
            return 2 * this.manaDiscount;
        } else {
            // If the robe has no trim or a different trim, return the base discount
            return this.manaDiscount;
        }
    }

    @Override
    public Optional<Source> getAttunedSource(ItemStack stack, Player player) {
        Optional<ArmorTrim> trimOpt = player == null ? Optional.empty() : ArmorTrim.getTrim(player.level().registryAccess(), stack, true);
        if (trimOpt.isPresent() && trimOpt.get().pattern().is(TrimPatternsPM.RUNIC)) {
            Item trimItem = trimOpt.get().material().value().ingredient().value();
            if (trimItem instanceof RuneItem runeItem && runeItem.getRune() instanceof SourceRune sourceRune) {
                return Optional.ofNullable(sourceRune.getSource());
            }
        }
        return Optional.empty();
    }
}
