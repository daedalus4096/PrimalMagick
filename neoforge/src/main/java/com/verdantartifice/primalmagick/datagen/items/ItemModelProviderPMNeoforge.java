package com.verdantartifice.primalmagick.datagen.items;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.entities.PixieHouseItem;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.renderer.block.model.BlockModel.GuiLight;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Data provider for mod item models that aren't associated with a block state.
 * 
 * @author Daedalus4096
 */
public class ItemModelProviderPMNeoforge extends ModelProvider {
    private final CompletableFuture<HolderLookup.Provider> lookupProviderFuture;

    public ItemModelProviderPMNeoforge(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper exFileHelper) {
        super(output, Constants.MOD_ID, ITEM_FOLDER, ItemModelBuilderPMNeoforge::new, exFileHelper);
        this.lookupProviderFuture = lookupProvider;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return this.lookupProviderFuture.thenApply(p -> {
            this.clear();
            this.registerModels(p);
            return p;
        }).thenCompose(p -> {
            return this.generateAll(cache);
        });
    }

    @Override
    public void clear() {
        // Expose publicly is all
        super.clear();
    }

    @Override
    public CompletableFuture<?> generateAll(CachedOutput cache) {
        // Expose publicly is all
        return super.generateAll(cache);
    }

    @Override
    public String getName() {
        return "Item Models: " + this.modid;
    }

    protected void registerModels(HolderLookup.Provider lookupProvider) {
        // Generate miscellaneous items
        this.pixieHouseItem(ItemsPM.PIXIE_HOUSE.get(), this.blockTexture(Blocks.OAK_LOG));
    }
    
    private Identifier key(Item item) {
        return Objects.requireNonNull(Services.ITEMS_REGISTRY.getKey(item));
    }
    
    private Identifier key(Block block) {
        return Objects.requireNonNull(Services.BLOCKS_REGISTRY.getKey(block));
    }
    
    private ItemModelBuilderPMNeoforge builder(Item item) {
        return this.builder(this.key(item));
    }
    
    private ItemModelBuilderPMNeoforge builder(Identifier loc) {
        return this.getBuilder(loc.toString());
    }
    
    private Identifier blockTexture(Block block) {
        return this.key(block).withPrefix(BLOCK_FOLDER + "/");
    }
    
    private ItemModelBuilderPMNeoforge pixieHouseItem(PixieHouseItem item, Identifier particleTexture) {
        return this.builder(item)
                .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                .guiLight(GuiLight.FRONT)
                .texture("particle", particleTexture)
                .transforms()
                        .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(0, 90, 0).translation(6F, 12, -3.75F).scale(0.7F).end()
                        .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(0, 90, 0).translation(6F, 12, 5.75F).scale(0.7F).end()
                        .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0, 0, 5).translation(7, 7, 0).scale(0.7F).end()
                        .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(0, 0, 5).translation(-2, 7, 0).scale(0.7F).end()
                        .transform(ItemDisplayContext.GUI).rotation(15, -25, -5).translation(2, 3, 0).scale(0.65F).end()
                        .transform(ItemDisplayContext.FIXED).rotation(0, 180, 0).translation(-2, 4, -5).scale(0.5F).end()
                        .transform(ItemDisplayContext.GROUND).rotation(0, 0, 0).translation(4, 4, 2).scale(0.25F).end()
                        .end();
    }
}
