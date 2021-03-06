package dev.micalobia.micalibria.mixin.item;

import dev.micalobia.micalibria.event.PlaceBlockItemEvent;
import dev.micalobia.micalibria.event.enums.TypedEventReaction;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {
	@Inject(at = @At("HEAD"), cancellable = true, method = "place(Lnet/minecraft/item/ItemPlacementContext;Lnet/minecraft/block/BlockState;)Z")
	public void placeBlockItemHook(ItemPlacementContext context, BlockState state, CallbackInfoReturnable<Boolean> cir) {
		TypedEventReaction<BlockState> result = PlaceBlockItemEvent.EVENT.invoker().getState(context, state);
		switch(result.getReaction()) {
			case CANCEL:
				cir.setReturnValue(false);
			case IGNORE:
				return;
			default: // COMPLETE
				World world = context.getWorld();
				BlockPos pos = context.getBlockPos();
				BlockState placedState = result.getValue();
				boolean placed = world.setBlockState(pos, placedState, 11);
				cir.setReturnValue(placed);
		}
	}
}
