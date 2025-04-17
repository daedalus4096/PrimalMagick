package com.verdantartifice.primalmagick.common.tiles.mana;

import com.verdantartifice.primalmagick.common.capabilities.IManaStorage;
import com.verdantartifice.primalmagick.common.mana.network.IManaConsumer;
import com.verdantartifice.primalmagick.common.mana.network.RouteTable;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.IOwnedTileEntity;
import com.verdantartifice.primalmagick.common.tiles.base.ITieredDeviceBlockEntity;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class ManaInjectorTileEntity extends AbstractTilePM implements ITieredDeviceBlockEntity, IManaConsumer, IOwnedTileEntity {
    protected static final int TICKS_PER_PHASE = 40;
    protected static final List<Source> ALLOWED_SOURCES = Arrays.asList(Sources.EARTH, Sources.SEA, Sources.SKY, Sources.SUN, Sources.MOON);

    protected final RouteTable routeTable = new RouteTable();
    protected int ticks = 0;
    protected Source lastSource = Sources.SKY;
    protected Source nextSource = Sources.SKY;
    protected UUID ownerUUID;

    public ManaInjectorTileEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesPM.MANA_INJECTOR.get(), pos, state);
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        this.ownerUUID = null;
        if (compound.contains("OwnerUUID")) {
            String ownerUUIDStr = compound.getString("OwnerUUID");
            if (!ownerUUIDStr.isEmpty()) {
                this.ownerUUID = UUID.fromString(ownerUUIDStr);
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        if (this.ownerUUID != null) {
            compound.putString("OwnerUUID", this.ownerUUID.toString());
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ManaInjectorTileEntity entity) {
        if (entity.ticks % TICKS_PER_PHASE == 0) {
            entity.nextPhase();
        }

        // Determine if the attached device needs mana and pull if so
        if (!level.isClientSide && entity.ticks % 5 == 0) {
            entity.getConnectedStorage().ifPresent(storage -> {
                final int throughput = entity.getManaThroughput();
                Sources.streamSorted()
                        .filter(entity::canConsume)
                        .forEach(s -> entity.doSiphon(entity.getTileOwner(), level, s, Math.min(throughput, storage.getMaxManaStored(s) - storage.getManaStored(s))));
            });
        }

        entity.ticks++;
    }

    @Override
    public void setTileOwner(@Nullable Player owner) {
        this.ownerUUID = owner == null ? null : owner.getUUID();
    }

    @Override
    public @Nullable Player getTileOwner() {
        if (this.level instanceof ServerLevel serverLevel) {
            return serverLevel.getServer().getPlayerList().getPlayer(this.ownerUUID);
        } else {
            return null;
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

    protected Optional<IManaStorage<?>> getConnectedStorage() {
        Level level = this.getLevel();
        return level != null ? Services.CAPABILITIES.manaStorage(level, this.getBlockPos().below(), Direction.UP) : Optional.empty();
    }

    @Override
    public boolean canConsume(@NotNull Source source) {
        return this.getConnectedStorage().map(cap -> cap.canReceive(source)).orElse(false);
    }

    @Override
    public int receiveMana(@NotNull Source source, int maxReceive, boolean simulate) {
        return this.getConnectedStorage().map(cap -> cap.receiveMana(source, maxReceive, simulate)).orElse(0);
    }

    @Override
    public int getNetworkRange() {
        return 5;
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
