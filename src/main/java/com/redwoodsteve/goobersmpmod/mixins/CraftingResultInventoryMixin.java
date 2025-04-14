package com.redwoodsteve.goobersmpmod.mixins;

import com.redwoodsteve.goobersmpmod.config.Config;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(CraftingResultInventory.class)
public class CraftingResultInventoryMixin {
    @Inject(method = "getStack", at = @At("RETURN"), cancellable = true)
    private void getStack(int slot, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack getStack = cir.getReturnValue();

        if (getStack.isOf(Items.MACE) && !Objects.requireNonNull(Config.getConfig()).maceCraftable()) {
            cir.setReturnValue(new ItemStack(Items.AIR));
        }
    }
}
