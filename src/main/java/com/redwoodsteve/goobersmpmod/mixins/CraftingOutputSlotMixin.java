package com.redwoodsteve.goobersmpmod.mixins;

import com.mojang.authlib.GameProfile;
import com.redwoodsteve.goobersmpmod.EventListeners;
import com.redwoodsteve.goobersmpmod.config.Config;
import com.redwoodsteve.goobersmpmod.config.ConfigField;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.message.ChatVisibility;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.particle.ParticlesMode;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;
import java.util.function.Consumer;

import static com.ibm.icu.text.PluralRules.Operand.f;

@Mixin(CraftingResultSlot.class)
public class CraftingOutputSlotMixin {
    @Inject(method = "onTakeItem", at = @At("HEAD"), cancellable = true)
    private void onCraft(PlayerEntity playerf, ItemStack stack, CallbackInfo ci) {
        if (stack.isOf(Items.MACE) && !Config.getConfig().maceCraftable()) {
            ci.cancel();
        } else {
            PlayerManager playerManager = EventListeners.server.getPlayerManager();
            Config.changeConfig(ConfigField.maceCraftable, false);

            playerManager.getPlayerList().forEach(player -> {
                player.sendMessage(Text.literal(playerf.getGameProfile().getName()).formatted(Formatting.DARK_RED, Formatting.BOLD)
                        .append(Text.literal(" has crafted the only mace!").formatted(Formatting.RED)));
            });
        }
    }
}
