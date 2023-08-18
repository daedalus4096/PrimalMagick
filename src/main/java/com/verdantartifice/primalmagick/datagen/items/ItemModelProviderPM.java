package com.verdantartifice.primalmagick.datagen.items;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;

import net.minecraft.client.renderer.block.model.BlockModel.GuiLight;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.block.Block;
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
        this.itemWithParent(ItemsPM.SALTED_COOKED_BEEF.get(), Items.COOKED_BEEF);
        this.itemWithParent(ItemsPM.SALTED_COOKED_CHICKEN.get(), Items.COOKED_CHICKEN);
        this.itemWithParent(ItemsPM.SALTED_COOKED_COD.get(), Items.COOKED_COD);
        this.itemWithParent(ItemsPM.SALTED_COOKED_MUTTON.get(), Items.COOKED_MUTTON);
        this.itemWithParent(ItemsPM.SALTED_COOKED_PORKCHOP.get(), Items.COOKED_PORKCHOP);
        this.itemWithParent(ItemsPM.SALTED_COOKED_RABBIT.get(), Items.COOKED_RABBIT);
        this.itemWithParent(ItemsPM.SALTED_COOKED_SALMON.get(), Items.COOKED_SALMON);
        this.itemWithParent(ItemsPM.SALTED_BEETROOT_SOUP.get(), Items.BEETROOT_SOUP);
        this.itemWithParent(ItemsPM.SALTED_MUSHROOM_STEW.get(), Items.MUSHROOM_STEW);
        this.itemWithParent(ItemsPM.SALTED_RABBIT_STEW.get(), Items.RABBIT_STEW);
        
        // Generate mineral items
        this.basicItem(ItemsPM.IRON_GRIT.get());
        this.basicItem(ItemsPM.GOLD_GRIT.get());
        this.basicItem(ItemsPM.COPPER_GRIT.get());
        this.basicItem(ItemsPM.PRIMALITE_INGOT.get());
        this.basicItem(ItemsPM.HEXIUM_INGOT.get());
        this.basicItem(ItemsPM.HALLOWSTEEL_INGOT.get());
        this.basicItem(ItemsPM.PRIMALITE_NUGGET.get());
        this.basicItem(ItemsPM.HEXIUM_NUGGET.get());
        this.basicItem(ItemsPM.HALLOWSTEEL_NUGGET.get());
        this.basicItem(ItemsPM.QUARTZ_NUGGET.get());
        
        // Generate tool items
        this.handheldItem(ItemsPM.PRIMALITE_SWORD.get());
        this.tridentItem(ItemsPM.PRIMALITE_TRIDENT.get());
        this.bowItem(ItemsPM.PRIMALITE_BOW.get());
        this.handheldItem(ItemsPM.PRIMALITE_SHOVEL.get());
        this.handheldItem(ItemsPM.PRIMALITE_PICKAXE.get());
        this.handheldItem(ItemsPM.PRIMALITE_AXE.get());
        this.handheldItem(ItemsPM.PRIMALITE_HOE.get());
        this.fishingRodItem(ItemsPM.PRIMALITE_FISHING_ROD.get());
        this.shieldItem(ItemsPM.PRIMALITE_SHIELD.get(), this.blockTexture(BlocksPM.PRIMALITE_BLOCK.get()));
        this.handheldItem(ItemsPM.HEXIUM_SWORD.get());
        this.tridentItem(ItemsPM.HEXIUM_TRIDENT.get());
        this.bowItem(ItemsPM.HEXIUM_BOW.get());
        this.handheldItem(ItemsPM.HEXIUM_SHOVEL.get());
        this.handheldItem(ItemsPM.HEXIUM_PICKAXE.get());
        this.handheldItem(ItemsPM.HEXIUM_AXE.get());
        this.handheldItem(ItemsPM.HEXIUM_HOE.get());
        this.fishingRodItem(ItemsPM.HEXIUM_FISHING_ROD.get());
        this.shieldItem(ItemsPM.HEXIUM_SHIELD.get(), this.blockTexture(BlocksPM.HEXIUM_BLOCK.get()));
    }
    
    private ResourceLocation key(Item item) {
        return ForgeRegistries.ITEMS.getKey(item);
    }
    
    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }
    
    private ItemModelBuilder builder(Item item) {
        return this.builder(this.key(item));
    }
    
    private ItemModelBuilder builder(ResourceLocation loc) {
        return this.getBuilder(loc.toString());
    }
    
    private ResourceLocation defaultModelLoc(Item item) {
        return this.key(item).withPrefix(ModelProvider.ITEM_FOLDER + "/");
    }
    
    private ModelFile existingModel(ResourceLocation modelLoc) {
        return new ModelFile.ExistingModelFile(modelLoc, this.existingFileHelper);
    }
    
    private ModelFile uncheckedModel(ResourceLocation modelLoc) {
        return new ModelFile.UncheckedModelFile(modelLoc);
    }
    
    private ResourceLocation blockTexture(Block block) {
        return this.key(block).withPrefix(ModelProvider.BLOCK_FOLDER + "/");
    }
    
    private ItemModelBuilder itemWithParent(Item item, Item parent) {
        return this.itemWithParent(item, this.defaultModelLoc(parent));
    }
    
    private ItemModelBuilder itemWithParent(Item item, ResourceLocation parent) {
        return this.builder(item).parent(this.existingModel(parent));
    }
    
    private ItemModelBuilder handheldItem(Item item) {
        return this.builder(item).parent(this.existingModel(new ResourceLocation("item/handheld"))).texture("layer0", this.defaultModelLoc(item));
    }
    
    private ItemModelBuilder tridentItem(TridentItem item) {
        ItemModelBuilder throwingModel = this.tridentThrowingModel(item);
        ItemModelBuilder inHandModel = this.tridentInHandModel(item, throwingModel);
        return this.basicItem(item)
                .override().predicate(new ResourceLocation("throwing"), 0).model(inHandModel).end()
                .override().predicate(new ResourceLocation("throwing"), 1).model(throwingModel).end();
    }
    
    private ItemModelBuilder tridentInHandModel(TridentItem item, ModelFile throwingModel) {
        return this.builder(this.key(item).withSuffix("_in_hand"))
                .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                .guiLight(GuiLight.FRONT)
                .texture("particle", this.defaultModelLoc(item))
                .override().predicate(new ResourceLocation("throwing"), 1).model(throwingModel).end()
                .transforms()
                        .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(0, 60, 0).translation(11, 17, -2).scale(1).end()
                        .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(0, 60, 0).translation(3, 17, 12).scale(1).end()
                        .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0, -90, 25).translation(-3, 17, 1).scale(1).end()
                        .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(0, 90, -25).translation(13, 17, 1).scale(1).end()
                        .transform(ItemDisplayContext.GUI).rotation(0, 0, 0).translation(8, 8, 8).scale(1).end()
                        .transform(ItemDisplayContext.FIXED).rotation(0, 0, 0).translation(8, 8, 8).scale(1).end()
                        .transform(ItemDisplayContext.GROUND).rotation(0, 0, 0).translation(8, 8, 8).scale(1).end()
                        .end();
    }
    
    private ItemModelBuilder tridentThrowingModel(TridentItem item) {
        return this.builder(this.key(item).withSuffix("_throwing"))
                .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                .guiLight(GuiLight.FRONT)
                .texture("particle", this.defaultModelLoc(item))
                .transforms()
                        .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(0, 90, 180).translation(8, -17, 9).scale(1).end()
                        .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(0, 90, 180).translation(8, -17, -7).scale(1).end()
                        .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0, -90, 25).translation(-3, 17, 1).scale(1).end()
                        .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(0, 90, -25).translation(13, 17, 1).scale(1).end()
                        .transform(ItemDisplayContext.GUI).rotation(0, 0, 0).translation(8, 8, 8).scale(1).end()
                        .transform(ItemDisplayContext.FIXED).rotation(0, 0, 0).translation(8, 8, 8).scale(1).end()
                        .transform(ItemDisplayContext.GROUND).rotation(0, 0, 0).translation(8, 8, 8).scale(1).end()
                        .end();
    }
    
    private ItemModelBuilder bowItem(BowItem item) {
        ResourceLocation pulling = new ResourceLocation("pulling");
        ResourceLocation pull = new ResourceLocation("pull");
        return this.builder(item)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", this.defaultModelLoc(item))
                .override().predicate(pulling, 1).model(this.bowPullingModel(item, 0)).end()
                .override().predicate(pulling, 1).predicate(pull, 0.65F).model(this.bowPullingModel(item, 1)).end()
                .override().predicate(pulling, 1).predicate(pull, 0.9F).model(this.bowPullingModel(item, 2)).end()
                .transforms()
                        .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(-80, 260, -40).translation(-1, -2, 2.5F).scale(0.9F).end()
                        .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(-80, -280, 40).translation(-1, -2, 2.5F).scale(0.9F).end()
                        .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0, -90, 25).translation(1.13F, 3.2F, 1.13F).scale(0.68F).end()
                        .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(0, 90, -25).translation(1.13F, 3.2F, 1.13F).scale(0.68F).end()
                        .end();
    }
    
    private ItemModelBuilder bowPullingModel(BowItem item, int stage) {
        ResourceLocation stageKey = this.key(item).withSuffix("_pulling_" + stage);
        return this.builder(stageKey).parent(this.uncheckedModel(this.defaultModelLoc(item))).texture("layer0", stageKey.withPrefix(ModelProvider.ITEM_FOLDER + "/"));
    }
    
    private ItemModelBuilder fishingRodItem(FishingRodItem item) {
        return this.builder(item)
                .parent(new ModelFile.UncheckedModelFile("item/handheld_rod"))
                .texture("layer0", this.defaultModelLoc(item))
                .override().predicate(new ResourceLocation("cast"), 1).model(this.fishingRodCastModel(item)).end();
    }
    
    private ItemModelBuilder fishingRodCastModel(FishingRodItem item) {
        ResourceLocation castKey = this.key(item).withSuffix("_cast");
        return this.builder(castKey).parent(this.uncheckedModel(this.defaultModelLoc(item))).texture("layer0", castKey.withPrefix(ModelProvider.ITEM_FOLDER + "/"));
    }
    
    private ItemModelBuilder shieldItem(ShieldItem item, ResourceLocation particleTexture) {
        return this.builder(item)
                .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                .guiLight(GuiLight.FRONT)
                .texture("particle", particleTexture)
                .override().predicate(new ResourceLocation("blocking"), 1).model(this.shieldBlockingModel(item, particleTexture)).end()
                .transforms()
                        .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(0, 90, 0).translation(10, 6, -4).scale(1).end()
                        .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(0, 90, 0).translation(10, 6, 12).scale(1).end()
                        .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0, 180, 5).translation(-10, 2, -10).scale(1.25F).end()
                        .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(0, 180, 5).translation(10, 0, -10).scale(1.25F).end()
                        .transform(ItemDisplayContext.GUI).rotation(15, -25, -5).translation(2, 3, 0).scale(0.65F).end()
                        .transform(ItemDisplayContext.FIXED).rotation(0, 180, 0).translation(-2, 4, -5).scale(0.5F).end()
                        .transform(ItemDisplayContext.GROUND).rotation(0, 0, 0).translation(4, 4, 2).scale(0.25F).end()
                        .end();
    }
    
    private ItemModelBuilder shieldBlockingModel(ShieldItem item, ResourceLocation particleTexture) {
        return this.builder(this.key(item).withSuffix("_blocking"))
                .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                .guiLight(GuiLight.FRONT)
                .texture("particle", particleTexture)
                .transforms()
                        .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(45, 135, 0).translation(3.51F, 11, -2).scale(1).end()
                        .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(45, 135, 0).translation(13.51F, 3, 5).scale(1).end()
                        .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0, 180, -5).translation(-15, 5, -11).scale(1.25F).end()
                        .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(0, 180, -5).translation(5, 5, -11).scale(1.25F).end()
                        .transform(ItemDisplayContext.GUI).rotation(15, -25, -5).translation(2, 3, 0).scale(0.65F).end()
                        .end();
    }
}
