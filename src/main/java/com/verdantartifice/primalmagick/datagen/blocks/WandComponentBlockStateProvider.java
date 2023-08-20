package com.verdantartifice.primalmagick.datagen.blocks;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.items.wands.StaffCoreItem;
import com.verdantartifice.primalmagick.common.items.wands.WandCapItem;
import com.verdantartifice.primalmagick.common.items.wands.WandCoreItem;
import com.verdantartifice.primalmagick.common.items.wands.WandGemItem;
import com.verdantartifice.primalmagick.common.wands.IWandComponent;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
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
        WandCoreItem.getAllCores().forEach(this::wandCoreWithItem);
        StaffCoreItem.getAllCores().forEach(this::staffCoreWithItem);
        WandCapItem.getAllCaps().forEach(this::wandCapWithItem);
        WandGemItem.getAllGems().forEach(this::wandGemWithItem);
    }
    
    private ResourceLocation itemTextureLoc(ResourceLocation key) {
        return key.withPrefix(ModelProvider.ITEM_FOLDER + "/");
    }
    
    private void wandCoreWithItem(WandCoreItem coreItem) {
        this.generatedComponentWithItem(coreItem, coreItem.getWandCore(), "wand_core", "core");
    }
    
    private void staffCoreWithItem(StaffCoreItem coreItem) {
        this.generatedComponentWithItem(coreItem, coreItem.getWandCore(), "staff_core", "core", "wand_core");    // Staff cores use the same model texture as wand cores
    }
    
    private void wandCapWithItem(WandCapItem capItem) {
        this.generatedComponentWithItem(capItem, capItem.getWandCap(), "wand_cap", "cap");
        this.generatedComponent(capItem.getWandCap(), "staff_cap", "cap", "wand_cap");   // Staff caps use the same model texture as wand caps and have no corresponding item
    }
    
    private void wandGemWithItem(WandGemItem gemItem) {
        this.existingComponentWithItem(gemItem, gemItem.getWandGem(), "wand_gem");
    }
    
    private void generatedComponentWithItem(Item item, IWandComponent component, String componentSuffix, String textureComponentName) {
        this.generatedComponentWithItem(item, component, componentSuffix, textureComponentName, componentSuffix);
    }
    
    private void generatedComponentWithItem(Item item, IWandComponent component, String modelComponentSuffix, String textureComponentName, String textureComponentSuffix) {
        this.generatedComponent(component, modelComponentSuffix, textureComponentName, textureComponentSuffix);
        this.itemModels().basicItem(item);
    }
    
    private void generatedComponent(IWandComponent component, String modelComponentSuffix, String textureComponentName, String textureComponentSuffix) {
        ResourceLocation modelParent = PrimalMagick.resource(ModelProvider.ITEM_FOLDER + "/" + modelComponentSuffix);
        ResourceLocation modelKey = PrimalMagick.resource(component.getTag() + "_" + modelComponentSuffix);
        ResourceLocation textureKey = PrimalMagick.resource(component.getTag() + "_" + textureComponentSuffix);
        String modelName = modelKey.getPath();
        ResourceLocation textureLoc = this.itemTextureLoc(textureKey);
        ModelFile model = this.models().withExistingParent(modelName, modelParent).texture(textureComponentName, textureLoc);
        this.getSpecialBuilder(modelKey).setModels(new ConfiguredModel(model));
    }
    
    private void existingComponentWithItem(Item item, IWandComponent component, String modelComponentSuffix) {
        ResourceLocation modelKey = PrimalMagick.resource(component.getTag() + "_" + modelComponentSuffix);
        String modelName = modelKey.getPath();
        ModelFile model = new ModelFile.ExistingModelFile(PrimalMagick.resource(ModelProvider.BLOCK_FOLDER + "/" + modelName), this.existingFileHelper);
        this.getSpecialBuilder(modelKey).setModels(new ConfiguredModel(model));
        this.itemModels().basicItem(item);
    }
}
