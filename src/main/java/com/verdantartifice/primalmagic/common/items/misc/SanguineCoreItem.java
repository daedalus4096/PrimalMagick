package com.verdantartifice.primalmagic.common.items.misc;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/**
 * Definition of a non-blank sanguine core.  Slotted into a sanguine crucible to determine what
 * type of creature will be spawned.
 * 
 * @author Daedalus4096
 */
public class SanguineCoreItem extends Item {
    protected final Supplier<EntityType<?>> typeSupplier;
    protected final int soulsPerSpawn;

    public SanguineCoreItem(Supplier<EntityType<?>> typeSupplier, int soulsPerSpawn, Properties properties) {
        super(properties);
        this.typeSupplier = typeSupplier;
        this.soulsPerSpawn = soulsPerSpawn;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("tooltip.primalmagic.sanguine_core.1", this.getMaxDamage(stack) - this.getDamage(stack) + 1));
        tooltip.add(new TranslationTextComponent("tooltip.primalmagic.sanguine_core.2", this.soulsPerSpawn));
    }

    public EntityType<?> getEntityType() {
        return this.typeSupplier.get();
    }
    
    public int getSoulsPerSpawn() {
        return this.soulsPerSpawn;
    }
}
