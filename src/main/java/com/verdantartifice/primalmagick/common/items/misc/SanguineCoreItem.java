package com.verdantartifice.primalmagick.common.items.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * Definition of a non-blank sanguine core.  Slotted into a sanguine crucible to determine what
 * type of creature will be spawned.
 * 
 * @author Daedalus4096
 */
public class SanguineCoreItem extends Item {
    protected static final List<SanguineCoreItem> CORES = new ArrayList<>();
    
    protected final Supplier<EntityType<?>> typeSupplier;
    protected final int soulsPerSpawn;

    public SanguineCoreItem(Supplier<EntityType<?>> typeSupplier, int soulsPerSpawn, Properties properties) {
        super(properties);
        this.typeSupplier = typeSupplier;
        this.soulsPerSpawn = soulsPerSpawn;
        CORES.add(this);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(Component.translatable("tooltip.primalmagick.sanguine_core.1", this.getMaxDamage(stack) - this.getDamage(stack) + 1));
        tooltip.add(Component.translatable("tooltip.primalmagick.sanguine_core.2", this.soulsPerSpawn));
    }

    public EntityType<?> getEntityType() {
        return this.typeSupplier.get();
    }
    
    public int getSoulsPerSpawn() {
        return this.soulsPerSpawn;
    }
    
    public static Collection<SanguineCoreItem> getAllCores() {
        return Collections.unmodifiableCollection(CORES);
    }
}
