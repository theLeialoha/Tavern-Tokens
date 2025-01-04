package com.traverse.taverntokens.mixin;

import com.traverse.taverntokens.TavernTokens;
import com.traverse.taverntokens.interfaces.PlayerWithBagInventory;
import com.traverse.taverntokens.wallet.WalletInventory;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;

@Mixin(Player.class)
public abstract class PlayerMixin implements PlayerWithBagInventory {

    @Unique
    public WalletInventory walletInventory = new WalletInventory();

    @Override
    public WalletInventory getWalletInventory() {
        return this.walletInventory;
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    public void addAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        nbt.put("WalletItems", this.walletInventory.toNbtList());
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    public void readAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains("WalletItems", 9)) {
            this.walletInventory.readNbtList(nbt.getList("WalletItems", 10));
        }
    }

    // Drop Method for Coins on Death
    @Inject(at = @At("RETURN"), method = "dropEquipment")
    public void dropEquipment(CallbackInfo ci) {
        TavernTokens.LOGGER.info("Coins lost send HALP");
        // TODO: Find out where, death is causing the items in the player's inventory
        // TODO: to vanish after death when opening gui

        // if (!TavernTokens.CONFIG.allowRollover.get())
        // this.getWalletInventory().clearContent();
    }
}
