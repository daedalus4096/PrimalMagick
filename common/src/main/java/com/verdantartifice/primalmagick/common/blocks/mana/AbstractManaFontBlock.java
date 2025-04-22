package com.verdantartifice.primalmagick.common.blocks.mana;

import com.verdantartifice.primalmagick.common.mana.network.IManaNetworkNode;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.misc.ITieredDevice;
import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base block definition for a mana font.  Mana fonts contain a slowly replenishing supply of mana,
 * which can be drained by a wand to power it.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractManaFontBlock extends BaseEntityBlock implements ITieredDevice {
    protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    
    protected static final Map<DeviceTier, List<AbstractManaFontBlock>> REGISTRY = new HashMap<>();

    protected Source source;
    protected DeviceTier tier;
    
    public AbstractManaFontBlock(Source source, DeviceTier tier, Block.Properties properties) {
        super(properties);
        this.source = source;
        this.tier = tier;
        REGISTRY.computeIfAbsent(tier, (t) -> new ArrayList<>()).add(this);
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        // Before the block entity is removed, test if it's a mana network node and if so, invalidate its route table
        if (!pState.is(pNewState.getBlock()) && pLevel.getBlockEntity(pPos) instanceof IManaNetworkNode node) {
            node.getRouteTable().invalidate();
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    public Source getSource() {
        return this.source;
    }

    @Override
    public DeviceTier getDeviceTier() {
        return this.tier;
    }
    
    public String getTierDescriptor() {
        return switch (this.tier) {
            case BASIC -> "ancient";
            case ENCHANTED -> "artificial";
            default -> this.tier.getSerializedName();
        };
    }
    
    public int getManaCapacity() {
        return switch (this.tier) {
            // The "basic" tier refers to ancient mana fonts, which cannot be constructed
            case BASIC, FORBIDDEN -> 10000;
            case ENCHANTED -> 1000;
            case HEAVENLY -> 100000;
            default -> 0;
        };
    }

    public static Collection<AbstractManaFontBlock> getAll() {
        return REGISTRY.entrySet().stream().flatMap(e -> e.getValue().stream()).toList();
    }
    
    public static Collection<AbstractManaFontBlock> getAllManaFontsForTier(DeviceTier tier) {
        return Collections.unmodifiableList(REGISTRY.getOrDefault(tier, List.of()));
    }
}
