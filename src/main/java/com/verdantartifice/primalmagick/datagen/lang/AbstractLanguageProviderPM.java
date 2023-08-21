package com.verdantartifice.primalmagick.datagen.lang;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

import org.apache.commons.lang3.mutable.MutableInt;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagick.common.attunements.AttunementType;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.stats.Stat;
import com.verdantartifice.primalmagick.common.wands.IWandComponent;
import com.verdantartifice.primalmagick.datagen.lang.builders.AttunementThresholdLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.AttunementTypeLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.BlockLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.CommandLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.EnchantmentLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.EntityTypeLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.EventLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.GrimoireLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.ILanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.ItemLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.KeyMappingLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.KnowledgeTypeLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.ResearchDisciplineLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.ResearchEntryLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.ResearchRequirementLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.SourceLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.SpellModLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.SpellPayloadLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.SpellPropertyLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.SpellVehicleLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.StatLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.WandComponentLanguageBuilder;
import com.verdantartifice.primalmagick.datagen.lang.builders.WrittenBookLanguageBuilder;

import net.minecraft.client.KeyMapping;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;

/**
 * Language provider with mod-specific helper functions.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractLanguageProviderPM extends LanguageProvider {
    protected final Map<String, ILanguageBuilder> tracking = new TreeMap<>();
    
    public AbstractLanguageProviderPM(PackOutput output, String locale) {
        super(output, PrimalMagick.MODID, locale);
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
    
    public EntityTypeLanguageBuilder entity(Supplier<? extends EntityType<?>> entity) {
        return this.entity(entity.get());
    }
    
    public EntityTypeLanguageBuilder entity(EntityType<?> entity) {
        return this.createBuilder(() -> new EntityTypeLanguageBuilder(entity, this::untrack, this::add));
    }
    
    public EnchantmentLanguageBuilder enchantment(Supplier<? extends Enchantment> ench) {
        return this.enchantment(ench.get());
    }
    
    public EnchantmentLanguageBuilder enchantment(Enchantment ench) {
        return this.createBuilder(() -> new EnchantmentLanguageBuilder(ench, this::untrack, this::add));
    }
    
    public SourceLanguageBuilder source(Source source) {
        return this.createBuilder(() -> new SourceLanguageBuilder(source, this::untrack, this::add));
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
    
    public ResearchDisciplineLanguageBuilder researchDiscipline(ResearchDiscipline disc) {
        return this.createBuilder(() -> new ResearchDisciplineLanguageBuilder(disc, this::untrack, this::add));
    }
    
    public ResearchEntryLanguageBuilder researchEntry(String keyStr) {
        return this.researchEntry(SimpleResearchKey.parse(keyStr));
    }
    
    public ResearchEntryLanguageBuilder researchEntry(SimpleResearchKey key) {
        return this.createBuilder(() -> new ResearchEntryLanguageBuilder(key, this::untrack, this::add));
    }
    
    public ResearchRequirementLanguageBuilder researchRequirement(String keyStr) {
        return this.researchRequirement(SimpleResearchKey.parse(keyStr));
    }
    
    public ResearchRequirementLanguageBuilder researchRequirement(SimpleResearchKey key) {
        return this.createBuilder(() -> new ResearchRequirementLanguageBuilder(key, this::untrack, this::add));
    }
    
    public CommandLanguageBuilder command(String token) {
        return this.createBuilder(() -> new CommandLanguageBuilder(token, this::untrack, this::add));
    }
    
    public EventLanguageBuilder event(String token) {
        return this.createBuilder(() -> new EventLanguageBuilder(token, this::untrack, this::add));
    }
    
    public WrittenBookLanguageBuilder book(String id) {
        return this.createBuilder(() -> new WrittenBookLanguageBuilder(id, this::untrack, this::add));
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
}
