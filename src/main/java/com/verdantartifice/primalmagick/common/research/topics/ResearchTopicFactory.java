package com.verdantartifice.primalmagick.common.research.topics;

import java.util.LinkedList;
import java.util.List;

import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Provides functionality to decode network bytes into a usable research topic.
 *  
 * @author Daedalus4096
 */
public class ResearchTopicFactory {
    public static AbstractResearchTopic decode(FriendlyByteBuf buf) {
        AbstractResearchTopic.Type type = buf.readEnum(AbstractResearchTopic.Type.class);
        String data = buf.readUtf();
        int page = buf.readVarInt();
        return create(type, data, page);
    }
    
    public static List<AbstractResearchTopic> decodeHistory(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        List<AbstractResearchTopic> retVal = new LinkedList<>();
        for (int index = 0; index < size; index++) {
            retVal.add(decode(buf));
        }
        return retVal;
    }
    
    public static AbstractResearchTopic deserializeNBT(CompoundTag tag) {
        AbstractResearchTopic.Type type = AbstractResearchTopic.Type.fromName(tag.getString("Type"));
        String data = tag.getString("Data");
        int page = tag.getInt("Page");
        return create(type, data, page);
    }
    
    public static AbstractResearchTopic create(AbstractResearchTopic.Type type, String data, int page) {
        switch (type) {
        case MAIN_INDEX:
            return MainIndexResearchTopic.INSTANCE;
        case RESEARCH_DISCIPLINE:
            ResearchDiscipline disc = ResearchDisciplines.getDiscipline(data);
            return disc == null ? MainIndexResearchTopic.INSTANCE : new DisciplineResearchTopic(disc, page);
        case RESEARCH_ENTRY:
            ResearchEntry entry = ResearchEntries.getEntry(SimpleResearchKey.parse(data));
            return entry == null ? MainIndexResearchTopic.INSTANCE : new EntryResearchTopic(entry, page);
        case SOURCE:
            Source source = Source.getSource(data);
            return source == null ? MainIndexResearchTopic.INSTANCE : new SourceResearchTopic(source, page);
        case ENCHANTMENT:
            ResourceLocation loc = ResourceLocation.tryParse(data);
            Enchantment ench = ForgeRegistries.ENCHANTMENTS.getValue(loc);
            return ench == null ? MainIndexResearchTopic.INSTANCE : new EnchantmentResearchTopic(ench, page);
        case OTHER:
            return new OtherResearchTopic(data, page);
        default:
            throw new IllegalArgumentException("Unknown research topic type");
        }
    }
}
