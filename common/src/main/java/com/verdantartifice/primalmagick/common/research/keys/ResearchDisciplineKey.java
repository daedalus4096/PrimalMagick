package com.verdantartifice.primalmagick.common.research.keys;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.mutable.MutableBoolean;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.IconDefinition;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class ResearchDisciplineKey extends AbstractResearchKey<ResearchDisciplineKey> {
    public static final MapCodec<ResearchDisciplineKey> CODEC = ResourceKey.codec(RegistryKeysPM.RESEARCH_DISCIPLINES).fieldOf("rootKey").xmap(ResearchDisciplineKey::new, key -> key.rootKey);
    public static final StreamCodec<ByteBuf, ResearchDisciplineKey> STREAM_CODEC = ResourceKey.streamCodec(RegistryKeysPM.RESEARCH_DISCIPLINES).map(ResearchDisciplineKey::new, key -> key.rootKey);
    
    private static final ResourceLocation ICON_UNKNOWN = PrimalMagick.resource("textures/research/research_unknown.png");

    protected final ResourceKey<ResearchDiscipline> rootKey;
    
    public ResearchDisciplineKey(ResourceKey<ResearchDiscipline> rootKey) {
        this.rootKey = rootKey;
    }
    
    public ResourceKey<ResearchDiscipline> getRootKey() {
        return this.rootKey;
    }

    @Override
    public String toString() {
        return this.rootKey.location().toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.rootKey.registry(), this.rootKey.location());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ResearchDisciplineKey other = (ResearchDisciplineKey) obj;
        return Objects.equals(rootKey, other.rootKey);
    }

    @Override
    public RequirementCategory getRequirementCategory() {
        return RequirementCategory.RESEARCH;
    }

    @Override
    protected ResearchKeyType<ResearchDisciplineKey> getType() {
        return ResearchKeyTypesPM.RESEARCH_DISCIPLINE.get();
    }

    @Override
    public IconDefinition getIcon(RegistryAccess registryAccess) {
        return IconDefinition.of(registryAccess.registryOrThrow(RegistryKeysPM.RESEARCH_DISCIPLINES).getHolder(this.rootKey).map(ref -> ref.get().iconLocation()).orElse(ICON_UNKNOWN));
    }

    @Override
    public boolean isKnownBy(Player player) {
        if (player == null) {
            return false;
        }
        RegistryAccess registryAccess = player.level().registryAccess();
        Holder.Reference<ResearchDiscipline> discipline = registryAccess.registryOrThrow(RegistryKeysPM.RESEARCH_DISCIPLINES).getHolderOrThrow(this.rootKey);
        MutableBoolean retVal = new MutableBoolean(false);
        discipline.get().unlockRequirementOpt().ifPresentOrElse(req -> {
            // If the discipline does have an unlock requirement, then the discipline is only known if that requirement is met
            retVal.setValue(req.isMetBy(player));
        }, () -> {
            // If the discipline has no unlock requirement, then it's known to the player
            retVal.setTrue();
        });
        return retVal.booleanValue();
    }
    
    @Nonnull
    public static ResearchDisciplineKey fromNetwork(RegistryFriendlyByteBuf buf) {
        return (ResearchDisciplineKey)AbstractResearchKey.fromNetwork(buf);
    }
}
