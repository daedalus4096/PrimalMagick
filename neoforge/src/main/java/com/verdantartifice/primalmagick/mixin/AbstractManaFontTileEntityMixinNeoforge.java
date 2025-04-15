package com.verdantartifice.primalmagick.mixin;

import com.verdantartifice.primalmagick.common.mana.network.IManaSupplier;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;
import com.verdantartifice.primalmagick.common.tiles.mana.AbstractManaFontTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Self-mixin to implement a Neoforge-specific method override.
 *
 * @author Daedalus4096
 */
@Mixin(AbstractManaFontTileEntity.class)
public abstract class AbstractManaFontTileEntityMixinNeoforge extends AbstractTilePM implements IManaSupplier {
    public AbstractManaFontTileEntityMixinNeoforge(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.getLevel() != null) {
            this.loadManaNetwork(this.getLevel());
        }
    }
}
