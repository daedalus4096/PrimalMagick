package com.verdantartifice.primalmagick.common.items.books;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

/**
 * Item which is transformed into a randomized item from the given loot table upon use.  Exists so
 * that NBT attachement doesn't have to happen as part of a crafting recipe.
 * 
 * @author Daedalus4096
 */
public class LootGeneratorItem extends Item {
    protected final ResourceKey<LootTable> lootTableLoc;
    
    public LootGeneratorItem(ResourceKey<LootTable> lootTableLoc, Item.Properties properties) {
        super(properties);
        this.lootTableLoc = lootTableLoc;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide && pPlayer instanceof ServerPlayer serverPlayer && pLevel instanceof ServerLevel serverLevel) {
            LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(this.lootTableLoc);
            LootParams lootParams = new LootParams.Builder(serverLevel).withParameter(LootContextParams.ORIGIN, pPlayer.getEyePosition()).create(LootContextParamSets.CHEST);   // Origin is irrelevant, but expected
            List<ItemStack> generatedStacks = lootTable.getRandomItems(lootParams);
            ItemStack generatedStack = generatedStacks.stream().findFirst().orElse(ItemStack.EMPTY);
            
            serverPlayer.level().playSound((Player)null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((serverPlayer.getRandom().nextFloat() - serverPlayer.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            serverPlayer.getItemInHand(pUsedHand).shrink(1);
            if (!generatedStack.isEmpty() && !serverPlayer.getInventory().add(generatedStack)) {
                serverPlayer.drop(generatedStack, false);
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable(this.getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }
}
