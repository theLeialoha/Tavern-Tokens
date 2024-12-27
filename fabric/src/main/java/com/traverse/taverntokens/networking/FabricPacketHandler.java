package com.traverse.taverntokens.networking;

import com.traverse.taverntokens.interfaces.PlayerWithBagInventory;
import com.traverse.taverntokens.wallet.WalletInventory;
import com.traverse.taverntokens.wallet.WalletItemStack;
import com.traverse.taverntokens.wallet.WalletScreenMenuProvider;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class FabricPacketHandler extends PacketHandler {

    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(PacketConstants.UPDATE_WALLET_ID,
                (client, handler, buf, responseSender) -> {
                    WalletInventory walletInventory = ((PlayerWithBagInventory) client.player)
                            .getWalletInventory();

                    int slot = buf.readInt();
                    WalletItemStack itemStack = WalletItemStack.fromTag(buf.readNbt());
                    walletInventory.updateSlot(slot, itemStack);
                });
    }

    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(PacketConstants.OPEN_WALLET_ID,
                (server, player, handler, buf, responseSender) -> {
                    player.openMenu(new WalletScreenMenuProvider());
                });
    }

}
