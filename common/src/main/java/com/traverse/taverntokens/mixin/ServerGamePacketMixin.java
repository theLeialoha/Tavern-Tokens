package com.traverse.taverntokens.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.traverse.taverntokens.networking.PacketHandler;

import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketMixin {

    @Shadow
    private Connection connection;

    @Inject(method = "disconnect", at = @At("HEAD"))
    private void disconnect$uninjectPacketListener(Component reason, CallbackInfo ci) {
        PacketHandler.uninjectServer(connection);
    }
}
