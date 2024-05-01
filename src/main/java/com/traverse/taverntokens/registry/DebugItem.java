package com.traverse.taverntokens.registry;

import com.traverse.taverntokens.References;
import com.traverse.taverntokens.client.screens.WalletScreenHandlerFactory;
import com.traverse.taverntokens.screens.WalletScreenHandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class DebugItem extends Item  {

    public DebugItem(Settings settings) {
        super(settings);
        //TODO Auto-generated constructor stub
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        References.LOGGER.info("ITEM RIGHT CLICKED");
        user.openHandledScreen(new WalletScreenHandlerFactory());
        return super.use(world, user, hand);
    }
    
}
