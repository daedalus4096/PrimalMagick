package com.verdantartifice.primalmagick.common.blocks.mana;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.misc.ITieredDevice;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ManaInjectorBlock extends BaseEntityBlock implements ITieredDevice {
    public static final MapCodec<ManaInjectorBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            DeviceTier.CODEC.fieldOf("tier").forGetter(b -> b.tier),
            propertiesCodec()
        ).apply(instance, ManaInjectorBlock::new));

    protected final DeviceTier tier;

    public ManaInjectorBlock(DeviceTier pTier, Block.Properties pProperties) {
        super(pProperties);
        this.tier = pTier;
    }

    @Override
    public DeviceTier getDeviceTier() {
        return this.tier;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
//        return Services.BLOCK_ENTITY_PROTOTYPES.manaRelay().create(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
//        return createTickerHelper(pBlockEntityType, BlockEntityTypesPM.MANA_RELAY.get(), ManaRelayTileEntity::tick);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
