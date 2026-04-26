package com.verdantartifice.primalmagick.common.tiles.mana;

import com.mojang.logging.LogUtils;
import com.verdantartifice.primalmagick.common.mana.network.IManaRelay;
import com.verdantartifice.primalmagick.common.mana.network.RouteManager;
import com.verdantartifice.primalmagick.common.mana.network.RouteTable;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;
import com.verdantartifice.primalmagick.common.tiles.base.ITieredDeviceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

public abstract class ManaRelayTileEntity extends AbstractTilePM implements ITieredDeviceBlockEntity, IManaRelay {
    public static final int BOB_CYCLE_TIME_TICKS = 200;
    protected static final int TICKS_PER_PHASE = 40;
    protected static final List<Source> ALLOWED_SOURCES = Arrays.asList(Sources.EARTH, Sources.SEA, Sources.SKY, Sources.SUN, Sources.MOON);
    protected static final Logger LOGGER = LogUtils.getLogger();

    protected int ticks = 0;
    protected Source lastSource = Sources.SKY;
    protected Source nextSource = Sources.SKY;

    public ManaRelayTileEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesPM.MANA_RELAY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ManaRelayTileEntity entity) {
        if (entity.ticks++ % TICKS_PER_PHASE == 0) {
            entity.nextPhase();
        }
    }

    protected void nextPhase() {
        // Pick a new, different, random source to lerp to at the start of each phase
        if (this.level != null) {
            this.lastSource = this.nextSource;
            Source[] nextPossibleSources = ALLOWED_SOURCES.stream().filter(s -> !s.equals(this.lastSource)).toArray(Source[]::new);
            this.nextSource = nextPossibleSources[this.level.getRandom().nextInt(nextPossibleSources.length)];
        }

        this.setChanged();
        this.syncTile(true);
    }

    public int getCurrentColor(float partialTicks) {
        if (this.ticks % TICKS_PER_PHASE == 0) {
            return this.nextSource.getColor();
        } else {
            int lastColor = this.lastSource.getColor();
            int nextColor = this.nextSource.getColor();
            float blend = Mth.clamp(((this.ticks % TICKS_PER_PHASE) + partialTicks) / (TICKS_PER_PHASE / 2F), 0F, 1F);
            return ARGB.linearLerp(blend, lastColor, nextColor);
        }
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);
        this.ticks = input.getIntOr("Ticks", 0);
        this.lastSource = input.read("LastSource", Source.CODEC).orElse(Sources.EARTH);
        this.nextSource = input.read("NextSource", Source.CODEC).orElse(Sources.EARTH);
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("Ticks", this.ticks);
        output.store("LastSource", Source.CODEC, this.lastSource);
        output.store("NextSource", Source.CODEC, this.nextSource);
    }

    @Override
    public boolean canRelay(Source source) {
        return true;
    }

    @Override
    public int getNetworkRange() {
        return switch (this.getDeviceTier()) {
            case BASIC -> 5;
            case ENCHANTED -> 10;
            case FORBIDDEN -> 15;
            case HEAVENLY, CREATIVE -> 20;
        };
    }

    @Override
    public int getManaThroughput() {
        return switch (this.getDeviceTier()) {
            case BASIC -> 400;
            case ENCHANTED -> 800;
            case FORBIDDEN -> 1600;
            case HEAVENLY, CREATIVE -> 3200;
        };
    }

    @Override
    public @NotNull RouteTable getRouteTable() {
        return RouteManager.getRouteTable(this.getLevel());
    }

    @Override
    public void preRemoveSideEffects(@NotNull BlockPos pos, @NotNull BlockState state) {
        this.getRouteTable().invalidate();
        super.preRemoveSideEffects(pos, state);
    }
}
