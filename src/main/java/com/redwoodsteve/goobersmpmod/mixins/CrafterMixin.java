package com.redwoodsteve.goobersmpmod.mixins;

import net.minecraft.block.BlockState;
import net.minecraft.block.CrafterBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.CrafterBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(CrafterBlock.class)
public abstract class CrafterMixin {
    @Shadow
    public static Optional<RecipeEntry<CraftingRecipe>> getCraftingRecipe(ServerWorld world, CraftingRecipeInput input) {
        return Optional.empty();
    }

    @Inject(method = "craft", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/CraftingRecipe;craft(Lnet/minecraft/recipe/input/RecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;",
            shift = At.Shift.AFTER
    ), cancellable = true)
    private void onCraft(BlockState state, ServerWorld world, BlockPos pos, CallbackInfo ci) {
        CrafterBlockEntity crafterEntity = (CrafterBlockEntity) world.getBlockEntity(pos);
        CraftingRecipeInput input = null;
        if (crafterEntity != null) {
            input = crafterEntity.createRecipeInput();
        }

        Optional<RecipeEntry<CraftingRecipe>> recipe = getCraftingRecipe(world, input);

        if (recipe.isPresent()) {
            RecipeEntry<CraftingRecipe> entry = recipe.get();
            ItemStack result = entry.value().craft(input, world.getRegistryManager());

            if (result.isOf(Items.MACE)) {
                world.syncWorldEvent(WorldEvents.CRAFTER_FAILS, pos, 0);
                ci.cancel();
            }
        }
    }
}
