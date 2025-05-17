package com.verdantartifice.primalmagick.common.items.misc;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.verdantartifice.primalmagick.common.entities.pixies.PixieRank;
import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

public class DrainedPixieItem extends Item {
    protected static final Table<PixieRank, Source, DrainedPixieItem> PIXIES = HashBasedTable.create();

    private final PixieRank rank;
    private final Source source;

    public DrainedPixieItem(@NotNull PixieRank rank, @NotNull Source source, Item.Properties pProperties) {
        super(pProperties);
        this.rank = rank;
        this.source = source;
        register(rank, source, this);
    }

    @NotNull
    public PixieRank getPixieRank() {
        return this.rank;
    }

    @NotNull
    public Source getPixieSource() {
        return this.source;
    }

    protected static void register(@NotNull PixieRank rank, @NotNull Source source, @NotNull DrainedPixieItem item) {
        PIXIES.put(rank, source, item);
    }

    @NotNull
    public static ItemStack getDrainedPixie(@Nullable PixieRank rank, @Nullable Source source) {
        return getDrainedPixie(rank, source, 1);
    }

    @NotNull
    public static ItemStack getDrainedPixie(@Nullable PixieRank rank, @Nullable Source source, int count) {
        Item item = PIXIES.get(rank, source);
        return (item == null) ? ItemStack.EMPTY : new ItemStack(item, count);
    }

    public static Collection<DrainedPixieItem> getAllDrainedPixies() {
        return Collections.unmodifiableCollection(PIXIES.values());
    }
}
