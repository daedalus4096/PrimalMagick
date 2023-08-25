package com.verdantartifice.primalmagick.common.items.wands;

import java.util.function.Consumer;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.verdantartifice.primalmagick.client.renderers.itemstack.ModularStaffISTER;
import com.verdantartifice.primalmagick.common.wands.IStaff;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

/**
 * Item definition for a modular staff.  Modular staves are made up of cores, caps, and gems, and their
 * properties are determined by those components.  Staves also serve as basic melee weapons.
 * 
 * @author Daedalus4096
 */
public class ModularStaffItem extends ModularWandItem implements IStaff {
    protected final float attackDamage;
    protected final float attackSpeed;
    protected final Multimap<Attribute, AttributeModifier> attributeModifiers;

    public ModularStaffItem(int attackDamage, float attackSpeed, Properties properties) {
        super(properties);
        this.attackDamage = (float)attackDamage;
        this.attackSpeed = attackSpeed;
        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", (double)this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", (double)this.attackSpeed, AttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }
    
    @Override
    public Component getName(ItemStack stack) {
        // A modular staff's display name is determined by its components (e.g. "Apprentice's Iron-Shod Heartwood Staff")
        WandCore core = this.getWandCore(stack);
        Component coreName = (core == null) ? Component.translatable("wand_core.primalmagick.unknown") : Component.translatable(core.getNameTranslationKey());
        WandCap cap = this.getWandCap(stack);
        Component capName = (cap == null) ? Component.translatable("wand_cap.primalmagick.unknown") : Component.translatable(cap.getNameTranslationKey());
        WandGem gem = this.getWandGem(stack);
        Component gemName = (gem == null) ? Component.translatable("wand_gem.primalmagick.unknown") : Component.translatable(gem.getNameTranslationKey());
        return Component.translatable("item.primalmagick.modular_staff", gemName, capName, coreName);
    }
    
    @Override
    protected int getCoreSpellSlotCount(WandCore core) {
        // Staves get double the normal spell slots provided by their core
        return (core == null) ? 0 : (2 * core.getSpellSlots());
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            final BlockEntityWithoutLevelRenderer renderer = new ModularStaffISTER();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }
}
