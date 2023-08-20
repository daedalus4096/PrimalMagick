package com.verdantartifice.primalmagick.datagen.blocks;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.wands.StaffCoreItem;
import com.verdantartifice.primalmagick.common.items.wands.WandCapItem;
import com.verdantartifice.primalmagick.common.items.wands.WandCoreItem;
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
        this.wandCoreWithItem(ItemsPM.HEARTWOOD_WAND_CORE_ITEM.get());
        this.staffCoreWithItem(ItemsPM.HEARTWOOD_STAFF_CORE_ITEM.get());
        this.wandCapWithItem(ItemsPM.IRON_WAND_CAP_ITEM.get());
    }
    
    private ResourceLocation modelLoc(ResourceLocation key) {
        return key.withPrefix(ModelProvider.ITEM_FOLDER + "/");
    }
    
    private void wandCoreWithItem(WandCoreItem coreItem) {
        this.componentWithItem(coreItem, coreItem.getWandCore(), "wand_core", "core");
    }
    
    private void staffCoreWithItem(StaffCoreItem coreItem) {
        this.componentWithItem(coreItem, coreItem.getWandCore(), "staff_core", "core", "wand_core");    // Staff cores use the same model texture as wand cores
    }
    
    private void wandCapWithItem(WandCapItem capItem) {
        this.componentWithItem(capItem, capItem.getWandCap(), "wand_cap", "cap");
        this.component(capItem.getWandCap(), "staff_cap", "cap", "wand_cap");   // Staff caps use the same model texture as wand caps and have no corresponding item
    }
    
    private void componentWithItem(Item item, IWandComponent component, String componentSuffix, String textureComponentName) {
        this.componentWithItem(item, component, componentSuffix, textureComponentName, componentSuffix);
    }
    
    private void component(IWandComponent component, String modelComponentSuffix, String textureComponentName, String textureComponentSuffix) {
        ResourceLocation modelParent = PrimalMagick.resource("item/" + modelComponentSuffix);
        ResourceLocation modelKey = PrimalMagick.resource(component.getTag() + "_" + modelComponentSuffix);
        ResourceLocation textureKey = PrimalMagick.resource(component.getTag() + "_" + textureComponentSuffix);
        String modelName = modelKey.getPath();
        ResourceLocation textureLoc = this.modelLoc(textureKey);
        ModelFile model = this.models().withExistingParent(modelName, modelParent).texture(textureComponentName, textureLoc);
        this.getSpecialBuilder(modelKey).setModels(new ConfiguredModel(model));
    }
    
    private void componentWithItem(Item item, IWandComponent component, String modelComponentSuffix, String textureComponentName, String textureComponentSuffix) {
        this.component(component, modelComponentSuffix, textureComponentName, textureComponentSuffix);
        this.itemModels().basicItem(item);
    }
}
