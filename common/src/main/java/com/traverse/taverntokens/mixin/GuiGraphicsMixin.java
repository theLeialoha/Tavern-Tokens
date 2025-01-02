package com.traverse.taverntokens.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.traverse.taverntokens.wallet.WalletItemStack;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {

    @Shadow
    @Final
    private PoseStack pose;

    @Shadow
    public abstract int drawString(Font font, String text, int x, int y, int color, boolean shadow);

    @ModifyVariable(at = @At("HEAD"), method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V")
    private String renderItemDecorations$ModifyVariable(String countOverride, Font font, ItemStack stack,
            int x, int y) {
        return (stack instanceof WalletItemStack) ? "" : countOverride;
    }

    @Inject(at = @At("HEAD"), method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V")
    private void renderItemDecorations$Inject(Font font, ItemStack stack, int x, int y, String countOverride,
            CallbackInfo info) {
        if (!stack.isEmpty()) {
            if (stack instanceof WalletItemStack walletItemStack) {
                if (walletItemStack.getLongCount() != 1) {
                    float scale = 0.75f;

                    this.pose.pushPose();
                    this.pose.scale(scale, scale, 1.0F);
                    this.pose.translate(0.0f, 0.0f, 200.0f);

                    String string = walletItemStack.getItemCountShort();

                    this.drawString(font, string, (int) ((x + 19 - 2) / scale - font.width(string)),
                            (int) ((y + 3 + (6 / scale)) / scale), 0xFFFFFF, true);
                    this.pose.popPose();
                }
            }
        }
    }
}
