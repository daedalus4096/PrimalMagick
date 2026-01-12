package com.verdantartifice.primalmagick.common.tiles.mana;

import com.verdantartifice.primalmagick.common.advancements.criterion.CriteriaTriggersPM;
import com.verdantartifice.primalmagick.common.capabilities.IManaStorage;
import com.verdantartifice.primalmagick.common.mana.network.IManaConsumer;
import com.verdantartifice.primalmagick.common.mana.network.RouteManager;
import com.verdantartifice.primalmagick.common.mana.network.RouteTable;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;
import com.verdantartifice.primalmagick.common.tiles.base.IOwnedTileEntity;
import com.verdantartifice.primalmagick.common.tiles.base.ITieredDeviceBlockEntity;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class ManaInjectorTileEntity extends AbstractTilePM implements ITieredDeviceBlockEntity, IManaConsumer, IOwnedTileEntity {
    protected static final int TICKS_PER_PHASE = 40;
    protected static final List<Source> ALLOWED_SOURCES = Arrays.asList(Sources.EARTH, Sources.SEA, Sources.SKY, Sources.SUN, Sources.MOON);

    protected int ticks = 0;
    protected Source lastSource = Sources.SKY;
    protected Source nextSource = Sources.SKY;
    protected EntityReference<Player> owner;

    public ManaInjectorTileEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesPM.MANA_INJECTOR.get(), pos, state);
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);
        this.owner = EntityReference.read(input, "Owner");
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);
        EntityReference.store(this.owner, output, "Owner");
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ManaInjectorTileEntity entity) {
        if (entity.ticks % TICKS_PER_PHASE == 0) {
            entity.nextPhase();
        }

        // Determine if the attached device needs mana and pull if so
        if (!level.isClientSide()) {
            if (entity.ticks % 5 == 0) {
                entity.getConnectedStorage().ifPresent(storage -> {
                    final int throughput = entity.getManaThroughput();
                    final int totalSiphoned = Sources.streamSorted()
                            .filter(entity::canConsume)
                            .mapToInt(s -> entity.doSiphon(entity.getTileOwner(), level, s, Math.min(throughput, storage.getMaxManaStored(s) - storage.getManaStored(s))))
                            .sum();
                    if (entity.getTileOwner() instanceof ServerPlayer serverPlayer) {
                        CriteriaTriggersPM.MANA_NETWORK_SIPHON.get().trigger(serverPlayer, totalSiphoned);
                    }
                });
            }
        }

        entity.ticks++;
    }

    @Override
    public void setTileOwner(@Nullable Player owner) {
        this.owner = EntityReference.of(owner);
    }

    @Override
    @Nullable
    public Player getTileOwner() {
        Level level = this.getLevel();
        return level != null ? EntityReference.getPlayer(this.owner, level) : null;
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
            return ARGB.lerp(blend, lastColor, nextColor);
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
