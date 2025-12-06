package com.verdantartifice.primalmagick.mixin;

import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTileSidedInventoryPM;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Self-mixin to implement a Forge-specific method override.
 *
 * @author Daedalus4096
 */
@Mixin(AbstractTileSidedInventoryPM.class)
public abstract class AbstractTileSidedInventoryPMMixinForge extends AbstractTilePM {
    public AbstractTileSidedInventoryPMMixinForge(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Shadow
    protected abstract void doInventorySync();

    @Override
    public void onLoad() {
        super.onLoad();
        this.doInventorySync();
    }
}
