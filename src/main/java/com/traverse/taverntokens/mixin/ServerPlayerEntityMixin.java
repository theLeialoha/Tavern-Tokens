package com.traverse.taverntokens.mixin;

import com.traverse.taverntokens.interfaces.PlayerEntityWithBagInventory;
import com.traverse.taverntokens.wallet.WalletScreenHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin extends PlayerEntityMixin {

    @SuppressWarnings("static-access")
    @Inject(at = @At("RETURN"), method = "copyFrom")
    public void onRespawn(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        this.walletInventory.copy(((PlayerEntityWithBagInventory) oldPlayer).getWalletInventory());
    }

    @Redirect(
        // the method this function is called in
        method = "onScreenHandlerOpened",
        // target the invocation of System.out.println
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/screen/ScreenHandler;addListener(Lnet/minecraft/screen/ScreenHandlerListener;)V"
        )
    )
    public void onScreenHandlerOpened(ScreenHandler screenHandler, ScreenHandlerListener screenHandlerListener) {
        if (!(screenHandler instanceof WalletScreenHandler)) {
            screenHandler.addListener(screenHandlerListener);
        }
    }

}
