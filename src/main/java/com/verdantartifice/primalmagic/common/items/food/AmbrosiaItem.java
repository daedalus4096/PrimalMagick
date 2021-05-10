package com.verdantartifice.primalmagic.common.items.food;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Iterables;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Item definition for ambrosia.  Magical food that induces attunement to a primal source in the eater, to a limit.
 * 
 * @author Daedalus4096
 */
public class AmbrosiaItem extends Item {
    public static final List<AmbrosiaItem> AMBROSIAS = new ArrayList<>();
    
    protected final Source source;
    protected final int limit;
    
    public AmbrosiaItem(Source source, int limit, Item.Properties properties) {
        super(properties);
        this.source = source;
        this.limit = limit;
        AMBROSIAS.add(this);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        // TODO Auto-generated method stub
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
    
    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public int getColor(int tintIndex) {
        return tintIndex == 0 ? 0xFFFFFF : this.source.getColor();
    }
    
    public static Iterable<AmbrosiaItem> getAmbrosias() {
        return Iterables.unmodifiableIterable(AMBROSIAS);
    }
}
