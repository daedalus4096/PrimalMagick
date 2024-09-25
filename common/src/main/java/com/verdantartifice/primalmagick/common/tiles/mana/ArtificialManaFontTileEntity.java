package com.verdantartifice.primalmagick.common.tiles.mana;

import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Definition of an artificial mana font tile entity.
 * 
 * @author Daedalus4096
 */
public class ArtificialManaFontTileEntity extends AbstractManaFontTileEntity {
    public ArtificialManaFontTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.ARTIFICIAL_MANA_FONT.get(), pos, state);
    }
    
    @Override
    protected int getInitialMana() {
        return 0;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ArtificialManaFontTileEntity entity) {
        entity.ticksExisted++;
        if (!level.isClientSide && entity.ticksExisted % RECHARGE_TICKS == 0) {
            entity.doRecharge();
        }
    }
}
