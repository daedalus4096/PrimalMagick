package com.verdantartifice.primalmagick.common.research.topics;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.StringRepresentable;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Base research topic that points to a specific page in the Grimoire.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractResearchTopic implements INBTSerializable<CompoundTag> {
    protected final AbstractResearchTopic.Type type;
    protected String data;
    protected int page;
    
    protected AbstractResearchTopic(AbstractResearchTopic.Type type, String data, int page) {
        this.type = type;
        this.data = data;
        this.page = page;
    }
    
    public Type getType() {
        return this.type;
    }
    
    public int getPage() {
        return this.page;
    }
    
    public AbstractResearchTopic withPage(int newPage) {
        return ResearchTopicFactory.create(this.type, this.data, newPage);
    }
    
    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(this.type);
        buf.writeUtf(this.data);
        buf.writeVarInt(this.page);
    }
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag retVal = new CompoundTag();
        retVal.putString("Type", this.type.getSerializedName());
        retVal.putString("Data", this.data);
        retVal.putInt("Page", this.page);
        return retVal;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        // Type is set by the constructor
        this.data = nbt.getString("Data");
        this.page = nbt.getInt("Page");
    }

    public static enum Type implements StringRepresentable {
        MAIN_INDEX("main_index"),
        RESEARCH_DISCIPLINE("research_discipline"),
        RESEARCH_ENTRY("research_entry"),
        SOURCE("source"),
        ENCHANTMENT("enchantment"),
        OTHER("other");
        
        private final String name;
        
        private Type(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        @Override
        public String toString() {
            return this.getSerializedName();
        }
        
        @Nullable
        public static Type fromName(@Nullable String name) {
            for (Type type : values()) {
                if (type.getSerializedName().equals(name)) {
                    return type;
                }
            }
            return null;
        }
    }
}
