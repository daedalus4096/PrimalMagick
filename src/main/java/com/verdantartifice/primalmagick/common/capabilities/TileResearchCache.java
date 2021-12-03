package com.verdantartifice.primalmagick.common.capabilities;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;

/**
 * Default implementation of the tile research cache capability.
 * 
 * @author Daedalus4096
 */
public class TileResearchCache implements ITileResearchCache {
    private final Set<String> research = ConcurrentHashMap.newKeySet();

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag rootTag = new CompoundTag();
        
        // Serialize cached completed research
        ListTag researchList = new ListTag();
        for (String res : this.research) {
            researchList.add(StringTag.valueOf(res));
        }
        rootTag.put("research", researchList);
        
        return rootTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (nbt == null) {
            return;
        }
        
        this.clear();
        
        // Deserialize completed research
        ListTag researchList = nbt.getList("research", Tag.TAG_STRING);
        for (int index = 0; index < researchList.size(); index++) {
            String str = researchList.getString(index);
            if (str != null && !str.isEmpty()) {
                this.research.add(str);
            }
        }
    }

    @Override
    public void clear() {
        this.research.clear();
    }

    @Override
    public boolean isResearchComplete(SimpleResearchKey key) {
        if (key == null) {
            return false;
        } else if ("".equals(key.getRootKey())) {
            return true;
        } else {
            return this.research.contains(key.getRootKey());
        }
    }

    @Override
    public boolean isResearchComplete(CompoundResearchKey key) {
        if (key == null) {
            return false;
        } else {
            return key.getRequireAll() ? this.isAllResearchComplete(key.getKeys()) : this.isAnyResearchComplete(key.getKeys());
        }
    }
    
    protected boolean isAnyResearchComplete(@Nonnull List<SimpleResearchKey> keys) {
        for (SimpleResearchKey key : keys) {
            if (this.isResearchComplete(key)) {
                return true;
            }
        }
        return false;
    }
    
    protected boolean isAllResearchComplete(@Nonnull List<SimpleResearchKey> keys) {
        for (SimpleResearchKey key : keys) {
            if (!this.isResearchComplete(key)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void update(Player player, Predicate<SimpleResearchKey> researchFilter) {
        PrimalMagickCapabilities.getKnowledge(player).ifPresent(knowledge -> {
            this.clear();
            for (SimpleResearchKey key : knowledge.getResearchSet()) {
                if (knowledge.isResearchComplete(key) && (researchFilter == null || researchFilter.test(key))) {
                    this.research.add(key.getRootKey());
                }
            }
        });
    }
}
