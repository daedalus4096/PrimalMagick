package com.verdantartifice.primalmagic.common.blocks.base;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockTilePM<T extends TileEntity> extends BlockPM {
    protected final Class<T> tileClass;

    public BlockTilePM(String name, Class<T> tileClass, Properties properties) {
        super(name, properties);
        this.tileClass = tileClass;
    }
    
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if (this.tileClass == null) {
            return null;
        }
        try {
            return this.tileClass.newInstance();
        } catch (InstantiationException e) {
            PrimalMagic.LOGGER.catching(e);
        } catch (IllegalAccessException e) {
            PrimalMagic.LOGGER.catching(e);
        }
        return null;
    }
    
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tile = worldIn.getTileEntity(pos);
        return (tile == null) ? false : tile.receiveClientEvent(id, param);
    }
}
