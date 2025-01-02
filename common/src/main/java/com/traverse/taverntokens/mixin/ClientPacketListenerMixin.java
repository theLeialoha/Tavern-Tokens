package com.traverse.taverntokens.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.traverse.taverntokens.networking.PacketHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {

    @Shadow
    public Minecraft minecraft;

    @Shadow
    public Connection connection;

    @Inject(method = "handleLogin", at = @At(value = "TAIL"))
    private void handleGameProfile$injectPacketListener(ClientboundLoginPacket clientboundLoginPacket,
            CallbackInfo ci) {

        PacketHandler.injectClient(minecraft.player, connection);
    }

    // @Inject(method = "clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V", at
    // = @At("HEAD"))
    // private void clearLevel$uninjectPacketListener(Screen screen, CallbackInfo
    // ci) {
    // Connection connection = getConnection().getConnection();
    // PacketHandler.uninjectClient(connection);
    // }

}
