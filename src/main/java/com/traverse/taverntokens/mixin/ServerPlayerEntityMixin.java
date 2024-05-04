package com.traverse.taverntokens.mixin;

import com.traverse.taverntokens.interfaces.PlayerEntityWithBagInventory;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin extends PlayerEntityMixin {

    @SuppressWarnings("static-access")
    @Inject(at = @At("RETURN"), method = "copyFrom")
    public void onRespawn(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        this.walletInventory.copy(((PlayerEntityWithBagInventory) oldPlayer).getWalletInventory());
    }

}
