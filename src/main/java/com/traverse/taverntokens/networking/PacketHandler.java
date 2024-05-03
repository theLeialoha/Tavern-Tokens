package com.traverse.taverntokens.networking;

import com.traverse.taverntokens.client.screens.WalletScreenHandlerFactory;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class PacketHandler {

    public static void registerClient() { }

    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(PacketConstants.OPEN_WALLET_ID, (server, player, handler, buf, responseSender) -> {
            player.openHandledScreen(new WalletScreenHandlerFactory());
        });
    }

    @Environment(EnvType.CLIENT)
    public static void requestWallet() {
        ClientPlayNetworking.send(PacketConstants.OPEN_WALLET_ID, PacketByteBufs.empty());
    }

}
