package com.verdantartifice.primalmagick.client.events;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import com.verdantartifice.primalmagick.client.config.KeyBindings;
import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.affinities.AffinityManager;
import com.verdantartifice.primalmagick.common.affinities.AffinityTooltipComponent;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.armor.IManaDiscountGear;
import com.verdantartifice.primalmagick.common.items.armor.WardingModuleItem;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.runes.RuneManager;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.wands.IWand;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;

import java.util.List;
import java.util.Optional;

/**
 * Respond to client-only rendering events.
 * 
 * @author Daedalus4096
 */
public class ClientRenderEvents {
    public static void renderTooltip(ItemStack stack, Player player, List<Component> tooltip) {
        // Show a tooltip entry if the item stack grants a mana discount
        if (stack.getItem() instanceof IManaDiscountGear discountItem) {
            int discount = discountItem.getBestManaDiscount(stack, player);
            Optional<Source> attunedSource = discountItem.getAttunedSource(stack, player);
            attunedSource.ifPresentOrElse(
                    source -> tooltip.add(Component.translatable("tooltip.primalmagick.mana_discount_attuned", discount, source.getNameText(ChatFormatting.DARK_AQUA)).withStyle(ChatFormatting.DARK_AQUA)),
                    () -> tooltip.add(Component.translatable("tooltip.primalmagick.mana_discount", discount).withStyle(ChatFormatting.DARK_AQUA)));
        }
        
        // Show a tooltip entry if the item stack is runescribed
        if (RuneManager.hasRunes(stack)) {
            tooltip.add(Component.translatable("tooltip.primalmagick.runescribed").withStyle(ChatFormatting.DARK_AQUA));
        }
        
        // Show a tooltip entry if the item is a glamoured wand (this code is here instead of in AbstractWandItem for tooltip ordering reasons)
        if (stack.getItem() instanceof IWand wand && wand.isGlamoured(stack)) {
            tooltip.add(Component.translatable("tooltip.primalmagick.glamoured").withStyle(ChatFormatting.DARK_AQUA));
        }
        
        // Show a tooltip entry if the item has a warding module attached
        if (WardingModuleItem.hasWardAttached(stack)) {
            Component levelComponent = Component.translatable("enchantment.level." + WardingModuleItem.getAttachedWardLevel(stack));
            tooltip.add(Component.translatable("tooltip.primalmagick.warded").append(CommonComponents.SPACE).append(levelComponent).withStyle(ChatFormatting.DARK_AQUA));
        }
        
        // Show a tooltip entry if the item has attached mana storage
        ManaStorage manaStorage = stack.get(DataComponentsPM.CAPABILITY_MANA_STORAGE.get());
        if (manaStorage != null) {
            Sources.getAllSorted().stream().filter(source -> source.isDiscovered(player) && manaStorage.canStore(source)).forEach(source ->
                tooltip.add(Component.translatable("tooltip.primalmagick.source.mana_container", source.getNameText(), (manaStorage.getManaStored(source) / 100.0D))));
        }
    }
    
    public static void onRenderTooltipGatherComponents(ItemStack stack, List<Either<FormattedText, TooltipComponent>> elements) {
        Minecraft mc = Minecraft.getInstance();
        Screen gui = mc.screen;

        // Assemble the tooltip components for showing primal affinities on an item stack
        if (gui instanceof AbstractContainerScreen && (Services.INPUT.isKeyDown(KeyBindings.VIEW_AFFINITY_KEY) != Services.CONFIG.showAffinities()) && !mc.mouseHandler.isMouseGrabbed() && stack != null && !stack.isEmpty()) {
            AffinityManager.getInstance().getAffinityValues(stack, mc.level).ifPresentOrElse(sources -> {
                if (sources.isEmpty()) {
                    elements.add(Either.left(Component.translatable("tooltip.primalmagick.affinities.none")));
                } else if (!ResearchManager.isScanned(stack, mc.player) && !Services.CONFIG.showUnscannedAffinities()) {
                    elements.add(Either.left(Component.translatable("tooltip.primalmagick.affinities.unknown")));
                } else {
                    elements.add(Either.left(Component.translatable("tooltip.primalmagick.affinities.label")));
                    elements.add(Either.right(new AffinityTooltipComponent(sources)));
                }
            }, () -> {
                // If the optional is empty, that means the asynchronous calculation is still in progress
                elements.add(Either.left(Component.translatable("tooltip.primalmagick.affinities.calculating")));
            });
        }
    }
    
    public static void onHighlightEntity(EntityHitResult target, PoseStack poseStack, MultiBufferSource multiBufferSource, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player.getMainHandItem().getItem() == ItemsPM.ARCANOMETER.get() || mc.player.getOffhandItem().getItem() == ItemsPM.ARCANOMETER.get()) {
            Entity entity = target.getEntity();
            AffinityManager.getInstance().getAffinityValues(entity.getType(), entity.level().registryAccess()).ifPresent(affinities -> {
                boolean isScanned = ResearchManager.isScanned(entity.getType(), mc.player);
                if (isScanned && !affinities.isEmpty()) {
                    double interpolatedEntityX = entity.xo + (partialTicks * (entity.getX() - entity.xo));
                    double interpolatedEntityY = entity.yo + (partialTicks * (entity.getY() - entity.yo));
                    double interpolatedEntityZ = entity.zo + (partialTicks * (entity.getZ() - entity.zo));
                    GuiUtils.renderSourcesBillboard(poseStack, multiBufferSource, interpolatedEntityX, interpolatedEntityY + entity.getBbHeight(), interpolatedEntityZ, affinities, partialTicks);
                }
            });
        }
    }
}
