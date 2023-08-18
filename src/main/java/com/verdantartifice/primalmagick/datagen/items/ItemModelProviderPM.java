package com.verdantartifice.primalmagick.datagen.items;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.items.ItemsPM;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Data provider for mod item models that aren't associated with a block state.
 * 
 * @author Daedalus4096
 */
public class ItemModelProviderPM extends ItemModelProvider {
    public ItemModelProviderPM(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, PrimalMagick.MODID, exFileHelper);
    }

    @Override
    protected void registerModels() {
        // Generate salted food items
        this.itemWithParent(ItemsPM.SALTED_BAKED_POTATO.get(), Items.BAKED_POTATO);
    }
    
    private ResourceLocation key(Item item) {
        return ForgeRegistries.ITEMS.getKey(item);
    }
    
    private ResourceLocation defaultModel(Item item) {
        return this.key(item).withPrefix(ModelProvider.ITEM_FOLDER + "/");
    }
    
    private ItemModelBuilder itemWithParent(Item item, Item parent) {
        return this.getBuilder(this.key(item).toString()).parent(new ModelFile.ExistingModelFile(this.defaultModel(parent), this.existingFileHelper));
    }
}
