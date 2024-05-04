package com.traverse.taverntokens.mixin;

import com.traverse.taverntokens.interfaces.PlayerEntityWithBagInventory;
import com.traverse.taverntokens.wallet.WalletInventory;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements PlayerEntityWithBagInventory {

    @Unique
    public WalletInventory walletInventory = new WalletInventory();

    @Override
    public WalletInventory getWalletInventory() {
        return this.walletInventory;
    }

    @SuppressWarnings("static-access")
    @Inject(at= @At("RETURN"), method = "copyFrom")
    public void onRespawn(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        this.walletInventory.copy(((PlayerEntityWithBagInventory) oldPlayer).walletInventory);
    }

    // // Drop Method for Coins on Death
    // @Inject(at = @At("RETURN"), method = "dropInventory")
    // public void droppedInventory(CallbackInfo ci) {
    //     References.LOGGER.info("Coins lost send HALP");
    // }
}
