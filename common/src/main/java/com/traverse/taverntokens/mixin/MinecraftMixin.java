package com.traverse.taverntokens.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.traverse.taverntokens.networking.PacketHandler;
import com.traverse.taverntokens.registry.ModKeybinds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.tutorial.Tutorial;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow
    public MultiPlayerGameMode gameMode;

    @Shadow
    public LocalPlayer player;

    @Final
    @Shadow
    private Tutorial tutorial;

    @Shadow
    protected abstract void setScreen(@Nullable Screen screen);

    @Inject(method = "handleKeybinds", at = @At(value = "TAIL"))
    private void handleKeybinds$injectModKeybinds(CallbackInfo ci) {
        while (ModKeybinds.OPEN_WALLET.consumeClick()) {
            PacketHandler.requestWallet();
        }
    }

}
