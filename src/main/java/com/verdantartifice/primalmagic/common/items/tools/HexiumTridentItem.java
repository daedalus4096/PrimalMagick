package com.verdantartifice.primalmagic.common.items.tools;

import java.util.function.Consumer;

import com.verdantartifice.primalmagic.client.renderers.itemstack.HexiumTridentISTER;
import com.verdantartifice.primalmagic.common.entities.projectiles.AbstractTridentEntity;
import com.verdantartifice.primalmagic.common.entities.projectiles.HexiumTridentEntity;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;

/**
 * Definition for a trident made of the magical metal hexium.
 * 
 * @author Daedalus4096
 */
public class HexiumTridentItem extends AbstractTieredTridentItem {
    protected final RenderProperties renderProperties;
    
    public HexiumTridentItem(Item.Properties properties) {
        super(ItemTierPM.HEXIUM, properties);
        this.renderProperties = new RenderProperties();
    }

    @Override
    protected AbstractTridentEntity getThrownEntity(Level world, LivingEntity thrower, ItemStack stack) {
        return new HexiumTridentEntity(world, thrower, stack);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(this.getRenderProperties());
    }
    
    public RenderProperties getRenderProperties() {
        return this.renderProperties;
    }
    
    public static class RenderProperties implements IItemRenderProperties {
        protected final BlockEntityWithoutLevelRenderer renderer;
        
        public RenderProperties() {
            this.renderer = new HexiumTridentISTER();
        }

        @Override
        public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
            return renderer;
        }
    }
}
