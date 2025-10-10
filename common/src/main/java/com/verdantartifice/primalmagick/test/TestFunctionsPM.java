package com.verdantartifice.primalmagick.test;

import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.platform.Services;
import com.verdantartifice.primalmagick.test.attunements.AbstractAttunementTest;
import net.minecraft.gametest.framework.GameTestHelper;

import java.util.function.Consumer;

public class TestFunctionsPM {
    public static void init() {
        // Pass the service initialization through this class so it gets class loaded and fields registered
        Services.TEST_FUNCTIONS_REGISTRY.init();
    }

    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> CANARY = Services.TEST_FUNCTIONS_REGISTRY.register("canary", () -> CanaryTest::canary);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MINOR_ATTUNEMENT_DISCOUNT_EARTH = Services.TEST_FUNCTIONS_REGISTRY.register("minor_attunement_gives_mana_discount_earth", () -> (helper) -> AbstractAttunementTest.minor_attunement_gives_mana_discount(helper, Sources.EARTH));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MINOR_ATTUNEMENT_DISCOUNT_SEA = Services.TEST_FUNCTIONS_REGISTRY.register("minor_attunement_gives_mana_discount_sea", () -> (helper) -> AbstractAttunementTest.minor_attunement_gives_mana_discount(helper, Sources.SEA));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MINOR_ATTUNEMENT_DISCOUNT_SKY = Services.TEST_FUNCTIONS_REGISTRY.register("minor_attunement_gives_mana_discount_sky", () -> (helper) -> AbstractAttunementTest.minor_attunement_gives_mana_discount(helper, Sources.SKY));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MINOR_ATTUNEMENT_DISCOUNT_SUN = Services.TEST_FUNCTIONS_REGISTRY.register("minor_attunement_gives_mana_discount_sun", () -> (helper) -> AbstractAttunementTest.minor_attunement_gives_mana_discount(helper, Sources.SUN));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MINOR_ATTUNEMENT_DISCOUNT_MOON = Services.TEST_FUNCTIONS_REGISTRY.register("minor_attunement_gives_mana_discount_moon", () -> (helper) -> AbstractAttunementTest.minor_attunement_gives_mana_discount(helper, Sources.MOON));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MINOR_ATTUNEMENT_DISCOUNT_BLOOD = Services.TEST_FUNCTIONS_REGISTRY.register("minor_attunement_gives_mana_discount_blood", () -> (helper) -> AbstractAttunementTest.minor_attunement_gives_mana_discount(helper, Sources.BLOOD));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MINOR_ATTUNEMENT_DISCOUNT_INFERNAL = Services.TEST_FUNCTIONS_REGISTRY.register("minor_attunement_gives_mana_discount_infernal", () -> (helper) -> AbstractAttunementTest.minor_attunement_gives_mana_discount(helper, Sources.INFERNAL));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MINOR_ATTUNEMENT_DISCOUNT_VOID = Services.TEST_FUNCTIONS_REGISTRY.register("minor_attunement_gives_mana_discount_void", () -> (helper) -> AbstractAttunementTest.minor_attunement_gives_mana_discount(helper, Sources.VOID));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MINOR_ATTUNEMENT_DISCOUNT_HALLOWED = Services.TEST_FUNCTIONS_REGISTRY.register("minor_attunement_gives_mana_discount_hallowed", () -> (helper) -> AbstractAttunementTest.minor_attunement_gives_mana_discount(helper, Sources.HALLOWED));
}
