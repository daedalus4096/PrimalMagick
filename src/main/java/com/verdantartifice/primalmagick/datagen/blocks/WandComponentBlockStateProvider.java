package com.verdantartifice.primalmagick.datagen.blocks;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.wands.WandCoreItem;
import com.verdantartifice.primalmagick.common.wands.WandCore;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * Data provider dedicated to generating block state and item model asset files for wand
 * components.
 * 
 * @author Daedalus4096
 */
public class WandComponentBlockStateProvider extends AbstractSpecialBlockStateProvider {
    public WandComponentBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, PrimalMagick.MODID, exFileHelper);
    }

    @Override
    public String getName() {
        return "Wand Component Block States: " + this.modid;
    }

    @Override
    protected void registerStatesAndModels() {
        // Generate wand component blockstates and items
        this.wandCoreWithItem(ItemsPM.HEARTWOOD_WAND_CORE_ITEM.get());
    }
    
    private ResourceLocation modelLoc(ResourceLocation key) {
        return key.withPrefix(ModelProvider.ITEM_FOLDER + "/");
    }
    
    private void wandCoreWithItem(WandCoreItem coreItem) {
        WandCore core = coreItem.getWandCore();
        ResourceLocation key = PrimalMagick.resource(core.getTag() + "_wand_core");
        String name = key.getPath();
        ResourceLocation modelLoc = this.modelLoc(key);
        ModelFile model = this.models().withExistingParent(name, PrimalMagick.resource("item/wand_core")).texture("core", modelLoc);
        this.getSpecialBuilder(key).setModels(new ConfiguredModel(model));
        this.itemModels().basicItem(coreItem);
    }
}
