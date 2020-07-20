package com.verdantartifice.primalmagic.common.items.wands;

import com.verdantartifice.primalmagic.common.wands.IStaff;
import com.verdantartifice.primalmagic.common.wands.WandCap;
import com.verdantartifice.primalmagic.common.wands.WandCore;
import com.verdantartifice.primalmagic.common.wands.WandGem;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Item definition for a modular staff.  Modular staves are made up of cores, caps, and gems, and their
 * properties are determined by those components.
 * 
 * @author Daedalus4096
 */
public class ModularStaffItem extends ModularWandItem implements IStaff {
    public ModularStaffItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        // A modular staff's display name is determined by its components (e.g. "Apprentice's Iron-Shod Heartwood Staff")
        WandCore core = this.getWandCore(stack);
        ITextComponent coreName = (core == null) ? new TranslationTextComponent("primalmagic.wand_core.unknown.name") : new TranslationTextComponent(core.getNameTranslationKey());
        WandCap cap = this.getWandCap(stack);
        ITextComponent capName = (cap == null) ? new TranslationTextComponent("primalmagic.wand_cap.unknown.name") : new TranslationTextComponent(cap.getNameTranslationKey());
        WandGem gem = this.getWandGem(stack);
        ITextComponent gemName = (gem == null) ? new TranslationTextComponent("primalmagic.wand_gem.unknown.name") : new TranslationTextComponent(gem.getNameTranslationKey());
        return new TranslationTextComponent("item.primalmagic.modular_staff", gemName, capName, coreName);
    }
    
    @Override
    protected int getCoreSpellSlotCount(WandCore core) {
        // Staves get double the normal spell slots provided by their core
        return (core == null) ? 0 : (2 * core.getSpellSlots());
    }
}
