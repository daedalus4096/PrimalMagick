package com.verdantartifice.primalmagick.common.research.topics;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

/**
 * Research topic that points to the main index of the Grimoire.
 * 
 * @author Daedalus4096
 */
public class MainIndexResearchTopic extends AbstractResearchTopic<MainIndexResearchTopic> {
    public static final MainIndexResearchTopic INSTANCE = new MainIndexResearchTopic();
    
    public static final MapCodec<MainIndexResearchTopic> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<ByteBuf, MainIndexResearchTopic> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    
    protected MainIndexResearchTopic() {
        super(0);
    }

    @Override
    public ResearchTopicType<MainIndexResearchTopic> getType() {
        return ResearchTopicTypesPM.MAIN_INDEX.get();
    }

    @Override
    public MainIndexResearchTopic withPage(int newPage) {
        return INSTANCE;
    }

    @Override
    public boolean isUnread(Player player) {
        return ResearchDisciplines.stream(player.registryAccess()).anyMatch(d -> d.isUnread(player));
    }

    @Override
    public Optional<Component> getUnreadTooltip(Player player) {
        int count = ResearchDisciplines.stream(player.registryAccess()).mapToInt(d -> d.isUnread(player) ? 1 : 0).sum();
        if (count == 1) {
            return Optional.of(Component.translatable("tooltip.primalmagick.unread_count.discipline.single"));
        } else if (count > 0) {
            return Optional.of(Component.translatable("tooltip.primalmagick.unread_count.discipline.multiple", count));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MainIndexResearchTopic)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
