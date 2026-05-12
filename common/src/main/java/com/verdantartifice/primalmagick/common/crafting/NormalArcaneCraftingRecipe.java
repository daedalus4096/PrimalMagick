package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.crafting.recipe_book.ArcaneCraftingBookCategory;
import com.verdantartifice.primalmagick.common.research.ResearchTier;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class NormalArcaneCraftingRecipe implements IArcaneRecipe {
    protected final Recipe.CommonInfo commonInfo;
    protected final IArcaneRecipe.ArcaneCraftingBookInfo bookInfo;
    protected final Optional<AbstractRequirement<?>> requirement;
    protected final SourceList manaCosts;
    protected final Optional<Integer> baseExpertiseOverride;
    protected final Optional<Integer> bonusExpertiseOverride;
    protected final Optional<Identifier> expertiseGroup;
    protected final Optional<ResearchDisciplineKey> disciplineOverride;
    @Nullable private PlacementInfo placementInfo;

    protected NormalArcaneCraftingRecipe(Recipe.CommonInfo commonInfo, IArcaneRecipe.ArcaneCraftingBookInfo bookInfo,
                                         Optional<AbstractRequirement<?>> requirement, SourceList manaCosts,
                                         Optional<Integer> baseExpertiseOverride, Optional<Integer> bonusExpertiseOverride,
                                         Optional<Identifier> expertiseGroup, Optional<ResearchDisciplineKey> disciplineOverride) {
        this.commonInfo = commonInfo;
        this.bookInfo = bookInfo;
        this.requirement = requirement;
        this.manaCosts = manaCosts;
        this.baseExpertiseOverride = baseExpertiseOverride;
        this.bonusExpertiseOverride = bonusExpertiseOverride;
        this.expertiseGroup = expertiseGroup;
        this.disciplineOverride = disciplineOverride;
    }

    @NotNull
    public abstract RecipeSerializer<? extends NormalArcaneCraftingRecipe> getSerializer();

    @NotNull
    public final String group() {
        return this.bookInfo.group();
    }

    @NotNull
    public final ArcaneCraftingBookCategory category() {
        return this.bookInfo.category();
    }

    public final boolean showNotification() {
        return this.commonInfo.showNotification();
    }

    @NotNull
    protected abstract PlacementInfo createPlacementInfo();

    @NotNull
    public final PlacementInfo placementInfo() {
        if (this.placementInfo == null) {
            this.placementInfo = this.createPlacementInfo();
        }
        return this.placementInfo;
    }

    @Override
    @NotNull
    public SourceList getManaCosts() {
        return this.manaCosts;
    }

    @Override
    @NotNull
    public Optional<AbstractRequirement<?>> getRequirement() {
        return this.requirement;
    }

    @Override
    public int getExpertiseReward(RegistryAccess registryAccess) {
        return this.baseExpertiseOverride.orElseGet(() -> this.getResearchTier(registryAccess).map(ResearchTier::getDefaultExpertise).orElse(0));
    }

    @Override
    public int getBonusExpertiseReward(RegistryAccess registryAccess) {
        return this.bonusExpertiseOverride.orElseGet(() -> this.getResearchTier(registryAccess).map(ResearchTier::getDefaultBonusExpertise).orElse(0));
    }

    @Override
    @NotNull
    public Optional<Identifier> getExpertiseGroup() {
        return this.expertiseGroup;
    }

    @Override
    @NotNull
    public Optional<ResearchDisciplineKey> getResearchDisciplineOverride() {
        return this.disciplineOverride;
    }
}
