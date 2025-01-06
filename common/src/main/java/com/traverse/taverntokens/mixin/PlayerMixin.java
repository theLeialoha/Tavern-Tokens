package com.traverse.taverntokens.mixin;

import com.traverse.taverntokens.TavernTokens;
import com.traverse.taverntokens.interfaces.PlayerWithBagInventory;
import com.traverse.taverntokens.item.BagItem;
import com.traverse.taverntokens.registry.ModItems;
import com.traverse.taverntokens.wallet.WalletInventory;
import com.traverse.taverntokens.wallet.WalletItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

@Mixin(Player.class)
public abstract class PlayerMixin implements PlayerWithBagInventory {

    @Shadow
    public abstract ItemEntity drop(ItemStack itemStack, boolean bl, boolean bl2);

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

        WalletItemStack[] drops = walletInventory.takePercent(TavernTokens.CONFIG.lossPercentage.get() / 100f);
        if (drops.length > 0 && TavernTokens.CONFIG.allowDrops.get()) {
            ItemStack bag = new ItemStack(ModItems.MONEY_BAG.get());
            BagItem.makeShadowed(bag, true);

            for (WalletItemStack drop : drops) {
                BagItem.addWalletItemStack(bag, drop);
            }

            this.drop(bag, true, false);
        }

        if (!TavernTokens.CONFIG.allowRollover.get())
            this.walletInventory.clearContent();
    }
}
