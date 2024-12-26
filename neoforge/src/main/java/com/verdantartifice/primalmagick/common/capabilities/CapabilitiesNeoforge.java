package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class CapabilitiesNeoforge {
    private static final DeferredRegister<AttachmentType<?>> CAPABILITIES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Constants.MOD_ID);

    public static void init() {
        CAPABILITIES.register(PrimalMagick.getEventBus());
    }

    public static final Supplier<AttachmentType<PlayerKnowledgeNeoforge>> KNOWLEDGE = CAPABILITIES.register("knowledge",
            () -> AttachmentType.serializable(PlayerKnowledgeNeoforge::new).copyOnDeath().build());
    public static final Supplier<AttachmentType<PlayerCooldownsNeoforge>> COOLDOWNS = CAPABILITIES.register("cooldowns",
            () -> AttachmentType.serializable(PlayerCooldownsNeoforge::new).copyOnDeath().build());
    public static final Supplier<AttachmentType<PlayerStatsNeoforge>> STATS = CAPABILITIES.register("stats",
            () -> AttachmentType.serializable(PlayerStatsNeoforge::new).copyOnDeath().build());
    public static final Supplier<AttachmentType<PlayerAttunementsNeoforge>> ATTUNEMENTS = CAPABILITIES.register("attunements",
            () -> AttachmentType.serializable(PlayerAttunementsNeoforge::new).copyOnDeath().build());
    public static final Supplier<AttachmentType<PlayerCompanionsNeoforge>> COMPANIONS = CAPABILITIES.register("companions",
            () -> AttachmentType.serializable(PlayerCompanionsNeoforge::new).copyOnDeath().build());
    public static final Supplier<AttachmentType<PlayerWardNeoforge>> WARD = CAPABILITIES.register("ward",
            () -> AttachmentType.serializable(PlayerWardNeoforge::new).copyOnDeath().build());
    public static final Supplier<AttachmentType<PlayerLinguisticsNeoforge>> LINGUISTICS = CAPABILITIES.register("linguistics",
            () -> AttachmentType.serializable(PlayerLinguisticsNeoforge::new).copyOnDeath().build());

    public static final BlockCapability<ITileResearchCache, Void> RESEARCH_CACHE =
            BlockCapability.createVoid(ResourceUtils.loc("research_cache"), ITileResearchCache.class);
}
