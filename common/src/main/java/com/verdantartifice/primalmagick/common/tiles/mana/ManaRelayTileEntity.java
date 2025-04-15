package com.verdantartifice.primalmagick.common.tiles.mana;

import com.mojang.logging.LogUtils;
import com.verdantartifice.primalmagick.common.mana.network.IManaRelay;
import com.verdantartifice.primalmagick.common.mana.network.RouteTable;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.ITieredDeviceBlockEntity;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

public abstract class ManaRelayTileEntity extends AbstractTilePM implements ITieredDeviceBlockEntity, IManaRelay {
    public static final int BOB_CYCLE_TIME_TICKS = 200;
    protected static final int TICKS_PER_PHASE = 40;
    protected static final List<Source> ALLOWED_SOURCES = Arrays.asList(Sources.EARTH, Sources.SEA, Sources.SKY, Sources.SUN, Sources.MOON);
    protected static final Logger LOGGER = LogUtils.getLogger();

    protected final RouteTable routeTable = new RouteTable();
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
            this.nextSource = nextPossibleSources[this.level.random.nextInt(nextPossibleSources.length)];
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
            return FastColor.ARGB32.lerp(blend, lastColor, nextColor);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        this.ticks = pTag.getInt("Ticks");
        Source last = Sources.get(ResourceLocation.parse(pTag.getString("LastSource")));
        this.lastSource = last == null ? Sources.EARTH : last;
        Source next = Sources.get(ResourceLocation.parse(pTag.getString("NextSource")));
        this.nextSource = next == null ? Sources.EARTH : next;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.putInt("Ticks", this.ticks);
        pTag.putString("LastSource", this.lastSource.getId().toString());
        pTag.putString("NextSource", this.nextSource.getId().toString());
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
            case BASIC -> 200;
            case ENCHANTED -> 400;
            case FORBIDDEN -> 800;
            case HEAVENLY, CREATIVE -> 1600;
        };
    }

    @Override
    public @NotNull RouteTable getRouteTable() {
        return this.routeTable;
    }
}
