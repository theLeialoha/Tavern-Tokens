package com.traverse.taverntokens.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.traverse.taverntokens.networking.PacketHandler;

import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;

@Mixin(ServerLoginPacketListenerImpl.class)
public abstract class ServerLoginPacketMixin {

    @Shadow
    private Connection connection;

    @Inject(method = "placeNewPlayer", at = @At("HEAD"))
    private void placeNewPlayer$injectPacketListener(ServerPlayer serverPlayer, CallbackInfo ci) {
        PacketHandler.injectServer(serverPlayer, connection);
    }
}
