package com.verdantartifice.primalmagick.datagen.lang;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.apache.commons.lang3.mutable.MutableInt;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagick.common.attunements.AttunementType;
import com.verdantartifice.primalmagick.common.books.BookDefinition;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.stats.Stat;
import com.verdantartifice.primalmagick.common.wands.IWandComponent;
import com.verdantartifice.primalmagick.datagen.lang.builders.AdvancementLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.AttunementThresholdLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.AttunementTypeLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.BlockLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.CommandLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.ConcoctionLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.DamageTypeLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.EnchantmentLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.EntityTypeLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.EventLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.ExpertiseGroupLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.GrimoireLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.ILanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.ItemLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.JeiLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.KeyMappingLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.KnowledgeTypeLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.LabelLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.MobEffectLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.ResearchDisciplineLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.ResearchEntryLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.ResearchProjectLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.RitualLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.SourceLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.SpellModLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.SpellPayloadLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.SpellPropertyLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.SpellVehicleLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.StatLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.TipLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.TooltipLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.TrimMaterialLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.TrimMaterialSourceMultipliedLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.TrimPatternLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.WandComponentLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.WrittenBookLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.WrittenLanguageLanguageBuilder;

import net.minecraft.client.KeyMapping;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;

/**
 * Language provider with mod-specific helper functions.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractLanguageProviderPM implements DataProvider {
    protected final Map<String, ILanguageBuilder> tracking = new TreeMap<>();
    protected final Map<Source, String> sourceNames = new HashMap<>();
    private final Map<String, String> data = new TreeMap<>();
    private final PackOutput output;
    private final String modid;
    private final String locale;
    private final CompletableFuture<HolderLookup.Provider> lookupProviderFuture;

    public AbstractLanguageProviderPM(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProviderFuture, String locale) {
        this.output = output;
        this.modid = PrimalMagick.MODID;
        this.locale = locale;
        this.lookupProviderFuture = lookupProviderFuture;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {
        return this.lookupProviderFuture.thenCompose(p -> {
            this.addLocalizations(p);
            this.validate();
            return save(pOutput, this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(this.modid).resolve("lang").resolve(this.locale + ".json"));
        });
    }

    @Override
    public String getName() {
        return "Languages: " + this.locale;
    }
    
    private CompletableFuture<?> save(CachedOutput cache, Path target) {
        JsonObject json = new JsonObject();
        this.data.forEach(json::addProperty);
        return DataProvider.saveStable(cache, json, target);
    }

    private void track(ILanguageBuilder builder) {
        String key = builder.getBuilderKey();
        if (this.tracking.containsKey(key)) {
            throw new IllegalStateException("Already tracking language builder for resource key " + key);
        } else {
            this.tracking.put(key, builder);
        }
    }
    
    private void untrack(ILanguageBuilder builder) {
        this.tracking.remove(builder.getBuilderKey());
    }
    
    protected abstract void addLocalizations(HolderLookup.Provider lookupProvider);

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
    
    protected void saveSourceName(Source source, String name) {
        this.sourceNames.put(source, name);
    }
    
    protected String getSourceName(Source source) {
        if (this.sourceNames.containsKey(source)) {
            return this.sourceNames.get(source);
        } else {
            throw new IllegalStateException("No source name found for " + source.getId().toString());
        }
    }
    
    protected void add(String key, String value) {
        if (this.data.put(key, value) != null) {
            throw new IllegalStateException("Duplicate translation key " + key);
        }
    }
    
    private <T extends ILanguageBuilder> T createBuilder(Supplier<T> builderFactory) {
        T builder = builderFactory.get();
        this.track(builder);
        return builder;
    }
    
    public BlockLanguageBuilder block(Supplier<? extends Block> block) {
        return this.block(block.get());
    }
    
    public BlockLanguageBuilder block(Block block) {
        return this.createBuilder(() -> new BlockLanguageBuilder(block, this::untrack, this::add));
    }
    
    public ItemLanguageBuilder item(Supplier<? extends Item> item) {
        return this.item(item.get());
    }
    
    public ItemLanguageBuilder item(Item item) {
        return this.createBuilder(() -> new ItemLanguageBuilder(item, this::untrack, this::add));
    }
    
    public TooltipLanguageBuilder tooltip(String id) {
        return this.createBuilder(() -> new TooltipLanguageBuilder(id, this::untrack, this::add));
    }
    
    public EntityTypeLanguageBuilder entity(Supplier<? extends EntityType<?>> entity) {
        return this.entity(entity.get());
    }
    
    public EntityTypeLanguageBuilder entity(EntityType<?> entity) {
        return this.createBuilder(() -> new EntityTypeLanguageBuilder(entity, this::untrack, this::add));
    }
    
    public MobEffectLanguageBuilder mobEffect(Supplier<? extends MobEffect> effect) {
        return this.mobEffect(effect.get());
    }
    
    public MobEffectLanguageBuilder mobEffect(MobEffect effect) {
        return this.createBuilder(() -> new MobEffectLanguageBuilder(effect, this::untrack, this::add));
    }
    
    public EnchantmentLanguageBuilder enchantment(ResourceKey<Enchantment> ench) {
        return this.createBuilder(() -> new EnchantmentLanguageBuilder(ench, this::untrack, this::add));
    }
    
    public SourceLanguageBuilder source(Source source) {
        return this.createBuilder(() -> new SourceLanguageBuilder(source, this::untrack, this::add, this::saveSourceName));
    }
    
    public WandComponentLanguageBuilder wandComponent(IWandComponent component) {
        return this.createBuilder(() -> new WandComponentLanguageBuilder(component, this::untrack, this::add));
    }
    
    public KnowledgeTypeLanguageBuilder knowledgeType(KnowledgeType type) {
        return this.createBuilder(() -> new KnowledgeTypeLanguageBuilder(type, this::untrack, this::add));
    }
    
    public AttunementTypeLanguageBuilder attunementType(AttunementType type) {
        return this.createBuilder(() -> new AttunementTypeLanguageBuilder(type, this::untrack, this::add));
    }
    
    public AttunementThresholdLanguageBuilder attunementThreshold(AttunementThreshold threshold) {
        return this.createBuilder(() -> new AttunementThresholdLanguageBuilder(threshold, this::untrack, this::add));
    }
    
    public ResearchDisciplineLanguageBuilder researchDiscipline(ResourceKey<ResearchDiscipline> discKey, HolderLookup.Provider lookupProvider) {
        Holder.Reference<ResearchDiscipline> discHolder = lookupProvider.lookupOrThrow(RegistryKeysPM.RESEARCH_DISCIPLINES).getOrThrow(discKey);
        return this.createBuilder(() -> new ResearchDisciplineLanguageBuilder(discHolder.get(), this::untrack, this::add));
    }
    
    public ResearchEntryLanguageBuilder researchEntry(ResourceKey<ResearchEntry> entryKey, HolderLookup.Provider lookupProvider) {
        Holder.Reference<ResearchEntry> entryHolder = lookupProvider.lookupOrThrow(RegistryKeysPM.RESEARCH_ENTRIES).getOrThrow(entryKey);
        return this.createBuilder(() -> new ResearchEntryLanguageBuilder(entryHolder.get(), this::untrack, this::add));
    }
    
    public ResearchProjectLanguageBuilder researchProject(String id) {
        return this.createBuilder(() -> new ResearchProjectLanguageBuilder(id, this::untrack, this::add));
    }
    
    public CommandLanguageBuilder command(String token) {
        return this.createBuilder(() -> new CommandLanguageBuilder(token, this::untrack, this::add));
    }
    
    public EventLanguageBuilder event(String token) {
        return this.createBuilder(() -> new EventLanguageBuilder(token, this::untrack, this::add));
    }
    
    public LabelLanguageBuilder label(String token) {
        return this.createBuilder(() -> new LabelLanguageBuilder(token, this::untrack, this::add));
    }
    
    public WrittenBookLanguageBuilder book(ResourceKey<BookDefinition> bookKey) {
        return this.createBuilder(() -> new WrittenBookLanguageBuilder(bookKey, this::untrack, this::add));
    }
    
    public WrittenLanguageLanguageBuilder language(ResourceKey<BookLanguage> lang) {
        return this.createBuilder(() -> new WrittenLanguageLanguageBuilder(lang, this::untrack, this::add));
    }
    
    public SpellVehicleLanguageBuilder spellVehicle(String id) {
        return this.createBuilder(() -> new SpellVehicleLanguageBuilder(id, this::untrack, this::add));
    }
    
    public SpellPayloadLanguageBuilder spellPayload(String id) {
        return this.createBuilder(() -> new SpellPayloadLanguageBuilder(id, this::untrack, this::add));
    }
    
    public SpellModLanguageBuilder spellMod(String id) {
        return this.createBuilder(() -> new SpellModLanguageBuilder(id, this::untrack, this::add));
    }
    
    public SpellPropertyLanguageBuilder spellProperty(String id) {
        return this.createBuilder(() -> new SpellPropertyLanguageBuilder(id, this::untrack, this::add));
    }
    
    public GrimoireLanguageBuilder grimoire(String id) {
        return this.createBuilder(() -> new GrimoireLanguageBuilder(id, this::untrack, this::add));
    }
    
    public KeyMappingLanguageBuilder keyMapping(KeyMapping mapping, String regName) {
        return this.createBuilder(() -> new KeyMappingLanguageBuilder(mapping, regName, this::untrack, this::add));
    }
    
    public StatLanguageBuilder stat(Stat stat) {
        return this.createBuilder(() -> new StatLanguageBuilder(stat, this::untrack, this::add));
    }
    
    public ExpertiseGroupLanguageBuilder expertiseGroup(String group) {
        return this.createBuilder(() -> new ExpertiseGroupLanguageBuilder(group, this::untrack, this::add));
    }
    
    public RitualLanguageBuilder ritual(String id) {
        return this.createBuilder(() -> new RitualLanguageBuilder(id, this::untrack, this::add));
    }
    
    public ConcoctionLanguageBuilder concoction(String id) {
        return this.createBuilder(() -> new ConcoctionLanguageBuilder(id, this::untrack, this::add));
    }
    
    public TrimMaterialLanguageBuilder trimMaterial(ResourceKey<TrimMaterial> key) {
        return this.createBuilder(() -> new TrimMaterialLanguageBuilder(key, this::untrack, this::add));
    }
    
    public TrimMaterialSourceMultipliedLanguageBuilder trimMaterial(String name, List<ResourceKey<TrimMaterial>> keys) {
        return this.createBuilder(() -> new TrimMaterialSourceMultipliedLanguageBuilder(name, keys, this::getSourceName, this::untrack, this::add));
    }
    
    public TrimPatternLanguageBuilder trimPattern(ResourceKey<TrimPattern> key) {
        return this.createBuilder(() -> new TrimPatternLanguageBuilder(key, this::untrack, this::add));
    }
    
    public DamageTypeLanguageBuilder deathMessage(ResourceKey<DamageType> key) {
        return this.createBuilder(() -> new DamageTypeLanguageBuilder(key, this::untrack, this::add));
    }
    
    public JeiLanguageBuilder jei(String id) {
        return this.createBuilder(() -> new JeiLanguageBuilder(id, this::untrack, this::add));
    }
    
    public TipLanguageBuilder tip(String id) {
        return this.createBuilder(() -> new TipLanguageBuilder(id, this::untrack, this::add));
    }
    
    public AdvancementLanguageBuilder advancement(String id) {
        return this.createBuilder(() -> new AdvancementLanguageBuilder(id, this::untrack, this::add));
    }
}
