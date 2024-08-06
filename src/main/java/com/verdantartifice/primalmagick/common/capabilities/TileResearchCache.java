package com.verdantartifice.primalmagick.common.capabilities;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.common.research.keys.AbstractResearchKey;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;

/**
 * Default implementation of the tile research cache capability.
 * 
 * @author Daedalus4096
 */
public class TileResearchCache implements ITileResearchCache {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Set<AbstractResearchKey<?>> research = ConcurrentHashMap.newKeySet();

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider registries) {
        CompoundTag rootTag = new CompoundTag();
        
        // Serialize cached completed research
        ListTag researchList = new ListTag();
        this.research.forEach(k -> {
            AbstractResearchKey.dispatchCodec().encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), k)
                .resultOrPartial(msg -> LOGGER.error("Failed to encode research key in tile research cache capability: {}", msg))
                .ifPresent(encodedTag -> researchList.add(encodedTag));
        });
        rootTag.put("research", researchList);
        
        return rootTag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider registries, CompoundTag nbt) {
        if (nbt == null) {
            return;
        }
        
        this.clear();
        
        // Deserialize completed research
        ListTag researchList = nbt.getList("research", Tag.TAG_COMPOUND);
        for (int index = 0; index < researchList.size(); index++) {
            CompoundTag innerTag = researchList.getCompound(index);
            AbstractResearchKey.dispatchCodec().parse(registries.createSerializationContext(NbtOps.INSTANCE), innerTag)
                .resultOrPartial(msg -> LOGGER.error("Failed to decode research key in tile research cache capability: {}", msg))
                .ifPresent(parsedKey -> this.research.add(parsedKey));
        }
    }

    @Override
    public void clear() {
        this.research.clear();
    }

    @Override
    public boolean isResearchComplete(AbstractResearchKey<?> key) {
        if (key == null) {
            return false;
        } else {
            return this.research.contains(key);
        }
    }

    @Override
    public boolean isResearchComplete(List<AbstractResearchKey<?>> keys) {
        return keys.stream().allMatch(k -> this.isResearchComplete(k));
    }
    
    @Override
    public void update(Player player, Predicate<AbstractResearchKey<?>> researchFilter) {
        PrimalMagickCapabilities.getKnowledge(player).ifPresent(knowledge -> {
            this.clear();
            RegistryAccess registryAccess = player.level().registryAccess();
            for (AbstractResearchKey<?> key : knowledge.getResearchSet()) {
                if (knowledge.isResearchComplete(registryAccess, key) && (researchFilter == null || researchFilter.test(key))) {
                    this.research.add(key);
                }
            }
        });
    }
}
