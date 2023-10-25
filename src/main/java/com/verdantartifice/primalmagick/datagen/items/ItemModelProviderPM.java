package com.verdantartifice.primalmagick.datagen.items;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.armor.RobeArmorItem;
import com.verdantartifice.primalmagick.common.items.entities.ManaArrowItem;
import com.verdantartifice.primalmagick.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagick.common.items.food.AmbrosiaItem;
import com.verdantartifice.primalmagick.common.items.misc.AttunementShacklesItem;
import com.verdantartifice.primalmagick.common.items.misc.HummingArtifactItem;
import com.verdantartifice.primalmagick.common.items.misc.PixieItem;
import com.verdantartifice.primalmagick.common.items.misc.RuneItem;
import com.verdantartifice.primalmagick.common.items.misc.SanguineCoreItem;

import net.minecraft.client.renderer.block.model.BlockModel.GuiLight;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Data provider for mod item models that aren't associated with a block state.
 * 
 * @author Daedalus4096
 */
public class ItemModelProviderPM extends ModelProvider<ItemModelBuilderPM> {
    @SuppressWarnings("unchecked")
    private static final List<ResourceKey<TrimMaterial>> SUPPORTED_TRIMS = ImmutableList.<ResourceKey<TrimMaterial>>builder().add(
            TrimMaterials.AMETHYST, TrimMaterials.COPPER, TrimMaterials.DIAMOND, TrimMaterials.EMERALD, TrimMaterials.GOLD,
            TrimMaterials.IRON, TrimMaterials.LAPIS, TrimMaterials.NETHERITE, TrimMaterials.QUARTZ, TrimMaterials.REDSTONE).build();
    
    private final CompletableFuture<HolderLookup.Provider> lookupProviderFuture;

    public ItemModelProviderPM(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper exFileHelper) {
        super(output, PrimalMagick.MODID, ITEM_FOLDER, ItemModelBuilderPM::new, exFileHelper);
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

    @Override
    protected void registerModels() {
        // Not used
    }
    
    protected void registerModels(HolderLookup.Provider lookupProvider) {
        // Generate crop items
        this.basicItem(ItemsPM.HYDROMELON_SEEDS.get());
        this.basicItem(ItemsPM.HYDROMELON_SLICE.get());
        
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
        this.itemWithParent(ItemsPM.ENERGIZED_AMETHYST.get(), Items.AMETHYST_SHARD);
        this.itemWithParent(ItemsPM.ENERGIZED_DIAMOND.get(), Items.DIAMOND);
        this.itemWithParent(ItemsPM.ENERGIZED_EMERALD.get(), Items.EMERALD);
        this.itemWithParent(ItemsPM.ENERGIZED_QUARTZ.get(), Items.QUARTZ);
        
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
        this.handheldItem(ItemsPM.HALLOWSTEEL_SWORD.get());
        this.tridentItem(ItemsPM.HALLOWSTEEL_TRIDENT.get());
        this.bowItem(ItemsPM.HALLOWSTEEL_BOW.get());
        this.handheldItem(ItemsPM.HALLOWSTEEL_SHOVEL.get());
        this.handheldItem(ItemsPM.HALLOWSTEEL_PICKAXE.get());
        this.handheldItem(ItemsPM.HALLOWSTEEL_AXE.get());
        this.handheldItem(ItemsPM.HALLOWSTEEL_HOE.get());
        this.fishingRodItem(ItemsPM.HALLOWSTEEL_FISHING_ROD.get());
        this.shieldItem(ItemsPM.HALLOWSTEEL_SHIELD.get(), this.blockTexture(BlocksPM.HALLOWSTEEL_BLOCK.get()));
        this.handheldItem(ItemsPM.FORBIDDEN_SWORD.get());
        this.tridentItem(ItemsPM.FORBIDDEN_TRIDENT.get());
        this.bowItem(ItemsPM.FORBIDDEN_BOW.get());
        this.handheldItem(ItemsPM.PRIMAL_SHOVEL.get());
        this.handheldItem(ItemsPM.PRIMAL_PICKAXE.get());
        this.handheldItem(ItemsPM.PRIMAL_AXE.get());
        this.handheldItem(ItemsPM.PRIMAL_HOE.get());
        this.fishingRodItem(ItemsPM.PRIMAL_FISHING_ROD.get());
        // Do not generate an item model for the sacred shield
        
        // Generate mana arrow items
        ManaArrowItem.getManaArrows().forEach(item -> this.itemWithParent(item, PrimalMagick.resource("item/template_mana_arrow")));
        
        // Generate armor items
        this.armorItemWithTrims(ItemsPM.IMBUED_WOOL_HEAD.get(), lookupProvider);
        this.armorItemWithTrims(ItemsPM.IMBUED_WOOL_CHEST.get(), lookupProvider);
        this.armorItemWithTrims(ItemsPM.IMBUED_WOOL_LEGS.get(), lookupProvider);
        this.armorItemWithTrims(ItemsPM.IMBUED_WOOL_FEET.get(), lookupProvider);
        this.armorItemWithTrims(ItemsPM.SPELLCLOTH_HEAD.get(), lookupProvider);
        this.armorItemWithTrims(ItemsPM.SPELLCLOTH_CHEST.get(), lookupProvider);
        this.armorItemWithTrims(ItemsPM.SPELLCLOTH_LEGS.get(), lookupProvider);
        this.armorItemWithTrims(ItemsPM.SPELLCLOTH_FEET.get(), lookupProvider);
        this.armorItemWithTrims(ItemsPM.HEXWEAVE_HEAD.get(), lookupProvider);
        this.armorItemWithTrims(ItemsPM.HEXWEAVE_CHEST.get(), lookupProvider);
        this.armorItemWithTrims(ItemsPM.HEXWEAVE_LEGS.get(), lookupProvider);
        this.armorItemWithTrims(ItemsPM.HEXWEAVE_FEET.get(), lookupProvider);
        this.armorItemWithTrims(ItemsPM.SAINTSWOOL_HEAD.get(), lookupProvider);
        this.armorItemWithTrims(ItemsPM.SAINTSWOOL_CHEST.get(), lookupProvider);
        this.armorItemWithTrims(ItemsPM.SAINTSWOOL_LEGS.get(), lookupProvider);
        this.armorItemWithTrims(ItemsPM.SAINTSWOOL_FEET.get(), lookupProvider);
        this.armorItemWithTrims(ItemsPM.PRIMALITE_HEAD.get(), lookupProvider);
        this.armorItemWithTrims(ItemsPM.PRIMALITE_CHEST.get(), lookupProvider);
        this.armorItemWithTrims(ItemsPM.PRIMALITE_LEGS.get(), lookupProvider);
        this.armorItemWithTrims(ItemsPM.PRIMALITE_FEET.get(), lookupProvider);
        this.armorItemWithTrims(ItemsPM.HEXIUM_HEAD.get(), lookupProvider);
        this.armorItemWithTrims(ItemsPM.HEXIUM_CHEST.get(), lookupProvider);
        this.armorItemWithTrims(ItemsPM.HEXIUM_LEGS.get(), lookupProvider);
        this.armorItemWithTrims(ItemsPM.HEXIUM_FEET.get(), lookupProvider);
        this.armorItemWithTrims(ItemsPM.HALLOWSTEEL_HEAD.get(), lookupProvider);
        this.armorItemWithTrims(ItemsPM.HALLOWSTEEL_CHEST.get(), lookupProvider);
        this.armorItemWithTrims(ItemsPM.HALLOWSTEEL_LEGS.get(), lookupProvider);
        this.armorItemWithTrims(ItemsPM.HALLOWSTEEL_FEET.get(), lookupProvider);
        
        // Generate miscellaneous items
        this.basicItem(ItemsPM.GRIMOIRE.get());
        this.basicItem(ItemsPM.CREATIVE_GRIMOIRE.get());
        // TODO Generate arcanometer
        this.basicItem(ItemsPM.MAGNIFYING_GLASS.get());
        this.basicItem(ItemsPM.ALCHEMICAL_WASTE.get());
        this.basicItem(ItemsPM.BLOODY_FLESH.get());
        this.basicItem(ItemsPM.HALLOWED_ORB.get());
        this.basicItem(ItemsPM.HEARTWOOD.get());
        this.basicItem(ItemsPM.ENCHANTED_INK.get());
        this.basicItem(ItemsPM.ENCHANTED_INK_AND_QUILL.get());
        this.basicItem(ItemsPM.SEASCRIBE_PEN.get());
        this.basicItem(ItemsPM.ROCK_SALT.get());
        this.basicItem(ItemsPM.EARTHSHATTER_HAMMER.get());
        this.basicItem(ItemsPM.MANA_PRISM.get());
        this.basicItem(ItemsPM.TALLOW.get());
        this.basicItem(ItemsPM.BEESWAX.get());
        this.basicItem(ItemsPM.MANA_SALTS.get());
        this.basicItem(ItemsPM.MANAFRUIT.get());
        this.basicItem(ItemsPM.INCENSE_STICK.get());
        this.basicItem(ItemsPM.SOUL_GEM.get());
        this.basicItem(ItemsPM.SOUL_GEM_SLIVER.get());
        this.basicItem(ItemsPM.SPELLCLOTH.get());
        this.basicItem(ItemsPM.HEXWEAVE.get());
        this.basicItem(ItemsPM.SAINTSWOOL.get());
        this.basicItem(ItemsPM.MAGITECH_PARTS_BASIC.get());
        this.basicItem(ItemsPM.MAGITECH_PARTS_ENCHANTED.get());
        this.basicItem(ItemsPM.MAGITECH_PARTS_FORBIDDEN.get());
        this.basicItem(ItemsPM.MAGITECH_PARTS_HEAVENLY.get());
        this.dyeableItem(ItemsPM.FLYING_CARPET.get(), DyeColor.WHITE);
        this.basicItem(ItemsPM.DREAM_VISION_TALISMAN.get());
        this.basicItem(ItemsPM.IGNYX.get());
        this.basicItem(ItemsPM.DOWSING_ROD.get());
        this.basicItem(ItemsPM.FOUR_LEAF_CLOVER.get());
        this.basicItem(ItemsPM.RECALL_STONE.get());
        this.basicItem(ItemsPM.RUNIC_ARMOR_TRIM_SMITHING_TEMPLATE.get());
        this.basicItem(ItemsPM.BASIC_WARDING_MODULE.get());
        this.basicItem(ItemsPM.GREATER_WARDING_MODULE.get());
        this.basicItem(ItemsPM.SUPREME_WARDING_MODULE.get());
        
        // Generate knowledge items
        this.basicItem(ItemsPM.OBSERVATION_NOTES.get());
        this.basicItem(ItemsPM.THEORY_NOTES.get());
        this.basicItem(ItemsPM.MYSTICAL_RELIC.get());
        this.basicItem(ItemsPM.MYSTICAL_RELIC_FRAGMENT.get());
        this.basicItem(ItemsPM.BLOOD_NOTES.get());
        this.basicItem(ItemsPM.SHEEP_TOME.get());
        
        // Generate essence items
        EssenceItem.getAllEssences().forEach(this::basicItem);
        
        // Generate rune items
        this.basicItem(ItemsPM.RUNE_UNATTUNED.get());
        RuneItem.getAllRunes().forEach(this::basicItem);
        
        // Generate ambrosia items
        AmbrosiaItem.getAllAmbrosiasOfType(AmbrosiaItem.Type.BASIC).forEach(item -> this.itemWithParent(item, PrimalMagick.resource("item/template_ambrosia")));
        AmbrosiaItem.getAllAmbrosiasOfType(AmbrosiaItem.Type.GREATER).forEach(item -> this.itemWithParent(item, PrimalMagick.resource("item/template_ambrosia_greater")));
        AmbrosiaItem.getAllAmbrosiasOfType(AmbrosiaItem.Type.SUPREME).forEach(item -> this.itemWithParent(item, PrimalMagick.resource("item/template_ambrosia_supreme")));
        
        // Generate attunement shackles items
        AttunementShacklesItem.getAllShackles().forEach(item -> this.itemWithParent(item, PrimalMagick.resource("item/template_attunement_shackles")));
        
        // Generate humming artifact items
        this.basicItem(ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get());
        HummingArtifactItem.getAllHummingArtifacts().forEach(item -> this.itemWithParent(item, PrimalMagick.resource("item/template_humming_artifact")));
        
        // Generate sanguine core items
        this.itemWithParent(ItemsPM.SANGUINE_CORE_BLANK.get(), PrimalMagick.resource("item/template_sanguine_core"));
        SanguineCoreItem.getAllCores().forEach(item -> this.itemWithParent(item, PrimalMagick.resource("item/template_sanguine_core")));
        
        // Generate concoction items
        this.basicItem(ItemsPM.SKYGLASS_FLASK.get());
        this.itemWithOverlay(ItemsPM.CONCOCTION.get());
        this.basicItem(ItemsPM.BOMB_CASING.get());
        this.itemWithOverlay(ItemsPM.ALCHEMICAL_BOMB.get());
        
        // Generate caster items
        this.basicItem(ItemsPM.SPELL_SCROLL_BLANK.get());
        this.basicItem(ItemsPM.SPELL_SCROLL_FILLED.get());
        // TODO Generate mundane wand
        // TODO Generate modular wand
        // TODO Generate modular staff
        
        // Generate spawn items
        this.spawnEggItem(ItemsPM.TREEFOLK_SPAWN_EGG.get());
        this.spawnEggItem(ItemsPM.PRIMALITE_GOLEM_SPAWN_EGG.get());
        this.spawnEggItem(ItemsPM.HEXIUM_GOLEM_SPAWN_EGG.get());
        this.spawnEggItem(ItemsPM.HALLOWSTEEL_GOLEM_SPAWN_EGG.get());
        
        // Generate pixie and drained pixie items
        PixieItem.getAllPixies().forEach(this::pixieItem);
        
        // Generate book items
        this.basicItem(ItemsPM.STATIC_BOOK.get());
        this.itemWithParent(ItemsPM.STATIC_TABLET.get(), ItemsPM.MYSTICAL_RELIC.get());
        this.basicItem(ItemsPM.CODEX.get());
        this.itemWithParent(ItemsPM.CODEX_CREATIVE.get(), ItemsPM.CODEX.get());
        this.itemWithParent(ItemsPM.LORE_TABLET_FRAGMENT.get(), ItemsPM.MYSTICAL_RELIC_FRAGMENT.get());
        this.itemWithParent(ItemsPM.LORE_TABLET_DIRTY.get(), ItemsPM.MYSTICAL_RELIC.get());
        
        // Generate debug items
        this.itemWithParent(ItemsPM.TICK_STICK.get(), Items.STICK);
    }
    
    private ResourceLocation key(Item item) {
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item));
    }
    
    private ResourceLocation key(Block block) {
        return Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block));
    }
    
    private ItemModelBuilderPM builder(Item item) {
        return this.builder(this.key(item));
    }
    
    private ItemModelBuilderPM builder(ResourceLocation loc) {
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
    
    public ItemModelBuilderPM basicItem(Item item) {
        ResourceLocation key = this.key(item);
        return this.builder(key)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", new ResourceLocation(key.getNamespace(), "item/" + key.getPath()));
    }

    private ItemModelBuilderPM itemWithParent(Item item, Item parent) {
        return this.itemWithParent(item, this.defaultModelLoc(parent));
    }
    
    private ItemModelBuilderPM itemWithParent(Item item, ResourceLocation parent) {
        return this.builder(item).parent(this.existingModel(parent));
    }
    
    private ItemModelBuilderPM handheldItem(Item item) {
        return this.builder(item).parent(this.existingModel(new ResourceLocation("item/handheld"))).texture("layer0", this.defaultModelLoc(item));
    }
    
    private ItemModelBuilderPM tridentItem(TridentItem item) {
        ItemModelBuilderPM throwingModel = this.tridentThrowingModel(item);
        ItemModelBuilderPM inHandModel = this.tridentInHandModel(item, throwingModel);
        return this.basicItem(item)
                .override().predicate(new ResourceLocation("throwing"), 0).model(inHandModel).end()
                .override().predicate(new ResourceLocation("throwing"), 1).model(throwingModel).end();
    }
    
    private ItemModelBuilderPM tridentInHandModel(TridentItem item, ModelFile throwingModel) {
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
    
    private ItemModelBuilderPM tridentThrowingModel(TridentItem item) {
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
    
    private ItemModelBuilderPM bowItem(BowItem item) {
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
    
    private ItemModelBuilderPM bowPullingModel(BowItem item, int stage) {
        ResourceLocation stageKey = this.key(item).withSuffix("_pulling_" + stage);
        return this.builder(stageKey).parent(this.uncheckedModel(this.defaultModelLoc(item))).texture("layer0", stageKey.withPrefix(ModelProvider.ITEM_FOLDER + "/"));
    }
    
    private ItemModelBuilderPM fishingRodItem(FishingRodItem item) {
        return this.builder(item)
                .parent(new ModelFile.UncheckedModelFile("item/handheld_rod"))
                .texture("layer0", this.defaultModelLoc(item))
                .override().predicate(new ResourceLocation("cast"), 1).model(this.fishingRodCastModel(item)).end();
    }
    
    private ItemModelBuilderPM fishingRodCastModel(FishingRodItem item) {
        ResourceLocation castKey = this.key(item).withSuffix("_cast");
        return this.builder(castKey).parent(this.uncheckedModel(this.defaultModelLoc(item))).texture("layer0", castKey.withPrefix(ModelProvider.ITEM_FOLDER + "/"));
    }
    
    private ItemModelBuilderPM shieldItem(ShieldItem item, ResourceLocation particleTexture) {
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
    
    private ItemModelBuilderPM shieldBlockingModel(ShieldItem item, ResourceLocation particleTexture) {
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
    
    private ItemModelBuilderPM armorItemWithTrims(ArmorItem item, HolderLookup.Provider lookupProvider) {
        return this.armorItemWithTrims(item, SUPPORTED_TRIMS, lookupProvider);
    }
    
    private ItemModelBuilderPM armorItemWithTrims(ArmorItem item, List<ResourceKey<TrimMaterial>> trimKeys, HolderLookup.Provider lookupProvider) {
        return this.armorItemWithTrims(item, trimKeys.stream().map(key -> this.getTrimMaterial(key, lookupProvider)).toList());
    }
    
    private ItemModelBuilderPM armorItemWithTrims(ArmorItem item, List<Holder<TrimMaterial>> trims) {
        return this.armorItemWithTrims(item, this.getArmorTrimOverlay(item), trims);
    }
    
    private ItemModelBuilderPM armorItemWithTrims(ArmorItem item, ResourceLocation trimOverlayLoc, List<Holder<TrimMaterial>> trims) {
        ResourceLocation trimType = new ResourceLocation("trim_type");
        ItemModelBuilderPM builder = this.basicItem(item);
        
        // It's very important that the overrides be written in ascending order of value, otherwise you may get the wrong texture for any given property value
        List<Holder<TrimMaterial>> sortedTrims = trims.stream().sorted((m1, m2) -> Float.compare(m1.value().itemModelIndex(), m2.value().itemModelIndex())).toList();
        
        for (Holder<TrimMaterial> trim : sortedTrims) {
            builder = builder.override().predicate(trimType, trim.value().itemModelIndex()).model(this.trimmedArmorModel(item, trimOverlayLoc, trim)).end();
        }
        return builder;
    }
    
    private ItemModelBuilderPM trimmedArmorModel(ArmorItem item, ResourceLocation trimOverlayLoc, Holder<TrimMaterial> trim) {
        String palatteSuffix = this.getArmorTrimColorPaletteSuffix(trim, item.getMaterial());
        return this.builder(this.key(item).withSuffix("_" + palatteSuffix + "_trim"))
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", this.defaultModelLoc(item))
                .palattedTexture("layer1", trimOverlayLoc, palatteSuffix);
    }
    
    private ResourceLocation getArmorTrimOverlay(ArmorItem item) {
        if (item instanceof RobeArmorItem) {
            return switch (item.getEquipmentSlot()) {
                case HEAD -> PrimalMagick.resource("trims/items/robe_head_trim");
                case CHEST -> PrimalMagick.resource("trims/items/robe_chest_trim");
                case LEGS -> PrimalMagick.resource("trims/items/robe_legs_trim");
                case FEET -> PrimalMagick.resource("trims/items/robe_feet_trim");
                default -> throw new IllegalArgumentException("Invalid armor type for trim overlay detection");
            };
        } else {
            return switch (item.getEquipmentSlot()) {
                case HEAD -> new ResourceLocation("trims/items/helmet_trim");
                case CHEST -> new ResourceLocation("trims/items/chestplate_trim");
                case LEGS -> new ResourceLocation("trims/items/leggings_trim");
                case FEET -> new ResourceLocation("trims/items/boots_trim");
                default -> throw new IllegalArgumentException("Invalid armor type for trim overlay detection");
            };
        }
    }
    
    private Holder<TrimMaterial> getTrimMaterial(ResourceKey<TrimMaterial> key, HolderLookup.Provider lookupProvider) {
        return lookupProvider.lookupOrThrow(Registries.TRIM_MATERIAL).getOrThrow(key);
    }
    
    private String getArmorTrimColorPaletteSuffix(Holder<TrimMaterial> trimMaterial, ArmorMaterial armorMaterial) {
        Map<ArmorMaterials, String> map = trimMaterial.value().overrideArmorMaterials();
        return armorMaterial instanceof ArmorMaterials && map.containsKey(armorMaterial) ? map.get(armorMaterial) : trimMaterial.value().assetName();
    }
    
    private ItemModelBuilderPM itemWithOverlay(Item item) {
        ResourceLocation texture = this.defaultModelLoc(item);
        return this.builder(item)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", texture.withSuffix("_overlay"))
                .texture("layer1", texture);
    }
    
    private ItemModelBuilderPM spawnEggItem(SpawnEggItem item) {
        return this.itemWithParent(item, new ResourceLocation("item/template_spawn_egg"));
    }
    
    private void pixieItem(PixieItem item) {
        this.itemWithParent(item, PrimalMagick.resource("item/template_pixie"));
        this.getBuilder(this.key(item).withPrefix("drained_").toString()).parent(this.existingModel(PrimalMagick.resource("item/template_drained_pixie")));
    }
    
    private ItemModelBuilderPM dyeableItem(Item item, DyeColor defaultColor) {
        ResourceLocation key = this.key(item);
        ItemModelBuilderPM builder = this.builder(key)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", new ResourceLocation(key.getNamespace(), "item/" + key.getPath() + "_" + defaultColor.getName()));
        
        // It's very important that the overrides be written in ascending order of value, otherwise you may get the wrong texture for any given property value
        List<DyeColor> sortedColors = Stream.of(DyeColor.values()).sorted((c1, c2) -> Integer.compare(c1.getId(), c2.getId())).toList();
        for (DyeColor color : sortedColors) {
            builder = builder.override().predicate(PrimalMagick.resource("color"), color.getId() / 16F).model(this.dyedItemModel(item, color)).end();
        }
        return builder;
    }
    
    private ItemModelBuilderPM dyedItemModel(Item item, DyeColor color) {
        return this.builder(this.key(item).withSuffix("_" + color.getName()))
                .parent(this.existingModel(this.defaultModelLoc(item)))
                .texture("layer0", this.defaultModelLoc(item).withSuffix("_" + color.getName()));
    }
}
