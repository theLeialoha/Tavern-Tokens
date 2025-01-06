package com.traverse.taverntokens.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.traverse.taverntokens.TavernTokens;
import com.traverse.taverntokens.item.BagItem;

import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

@Mixin(ItemProperties.class)
public abstract class ItemPropertiesMixin {

    @Shadow
    public static ClampedItemPropertyFunction registerGeneric(ResourceLocation resourceLocation,
            ClampedItemPropertyFunction clampedItemPropertyFunction) {
        return null;
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void clinit(CallbackInfo ci) {
        registerGeneric(new ResourceLocation(TavernTokens.MODID, "shadowed"),
                (itemStack, clientLevel, livingEntity, i) -> {
                    return itemStack.getItem() instanceof BagItem ? BagItem.isShadowed(itemStack) : 0.0F;
                });
        registerGeneric(new ResourceLocation(TavernTokens.MODID, "filled"),
                (itemStack, clientLevel, livingEntity, i) -> {
                    if (itemStack.getItem() instanceof BagItem) {
                        if (BagItem.hasContents(itemStack)) {
                            return 1f;
                        }
                    }
                    return 0.0F;
                });
    }

}
