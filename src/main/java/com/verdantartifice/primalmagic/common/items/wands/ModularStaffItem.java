package com.verdantartifice.primalmagic.common.items.wands;

import com.google.common.collect.Multimap;
import com.verdantartifice.primalmagic.common.wands.IStaff;
import com.verdantartifice.primalmagic.common.wands.WandCap;
import com.verdantartifice.primalmagic.common.wands.WandCore;
import com.verdantartifice.primalmagic.common.wands.WandGem;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Item definition for a modular staff.  Modular staves are made up of cores, caps, and gems, and their
 * properties are determined by those components.  Staves also serve as basic melee weapons.
 * 
 * @author Daedalus4096
 */
public class ModularStaffItem extends ModularWandItem implements IStaff {
    protected final float attackDamage;
    protected final float attackSpeed;
    
    public ModularStaffItem(int attackDamage, float attackSpeed, Properties properties) {
        super(properties);
        this.attackDamage = (float)attackDamage;
        this.attackSpeed = attackSpeed;
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
    
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        @SuppressWarnings("deprecation")
        Multimap<Attribute, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot);
        if (equipmentSlot == EquipmentSlotType.MAINHAND) {
            multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)this.attackDamage, AttributeModifier.Operation.ADDITION));
            multimap.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double)this.attackSpeed, AttributeModifier.Operation.ADDITION));
        }
        return multimap;
    }
}
