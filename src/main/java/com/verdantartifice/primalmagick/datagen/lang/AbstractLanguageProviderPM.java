package com.verdantartifice.primalmagick.datagen.lang;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.lang3.mutable.MutableInt;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.datagen.lang.builders.BlockLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.ILanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.ItemLanguageBuilder;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;

/**
 * Language provider with mod-specific helper functions.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractLanguageProviderPM extends LanguageProvider {
    protected final Map<ResourceKey<?>, ILanguageBuilder> tracking = new HashMap<>();
    
    public AbstractLanguageProviderPM(PackOutput output, String locale) {
        super(output, PrimalMagick.MODID, locale);
    }

    private void track(ILanguageBuilder builder) {
        ResourceKey<?> key = builder.getKey();
        if (this.tracking.containsKey(key)) {
            throw new IllegalStateException("Already tracking language builder for resource key " + key);
        } else {
            this.tracking.put(key, builder);
        }
    }
    
    private void untrack(ILanguageBuilder builder) {
        this.tracking.remove(builder.getKey());
    }
    
    protected abstract void addLocalizations();

    @Override
    protected void addTranslations() {
        this.addLocalizations();
        this.validate();
    }
    
    protected void validate() {
        MutableInt count = new MutableInt(0);
        this.tracking.forEach((key, builder) -> {
            if (builder.isEmpty()) {
                LOGGER.warn("Empty untracked language builder left over for " + key.toString());
            } else {
                LOGGER.error("Unbuilt language builder left over for " + key.toString());
                count.increment();
            }
        });
        if (count.intValue() > 0) {
            throw new IllegalStateException("Found " + count.intValue() + " unbuilt language builders for " + this.getName());
        }
    }
    
    public BlockLanguageBuilder block(Supplier<? extends Block> block) {
        return this.block(block.get());
    }
    
    public BlockLanguageBuilder block(Block block) {
        BlockLanguageBuilder builder = new BlockLanguageBuilder(block, this::untrack, this::add);
        this.track(builder);
        return builder;
    }
    
    public ItemLanguageBuilder item(Supplier<? extends Item> item) {
        return this.item(item.get());
    }
    
    public ItemLanguageBuilder item(Item item) {
        ItemLanguageBuilder builder = new ItemLanguageBuilder(item, this::untrack, this::add);
        this.track(builder);
        return builder;
    }
}
