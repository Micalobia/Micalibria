package dev.micalobia.micalibria.mixin.block;

import dev.micalobia.micalibria.event.MorphBlockDropStacksEvent;
import dev.micalobia.micalibria.event.enums.EventReaction;
import dev.micalobia.micalibria.event.enums.TypedEventReaction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.List;

@Mixin(Block.class)
public class BlockMixin {
	@Inject(at = @At("RETURN"), cancellable = true, method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;")
	private static void morphBlockDropStacksHook(BlockState state, ServerWorld world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack stack, CallbackInfoReturnable<List<ItemStack>> cir) {
		morphBlockDropStacksCallback(state, world, pos, blockEntity, entity, stack, cir);
	}

	@Inject(at = @At("RETURN"), cancellable = true, method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;)Ljava/util/List;")
	private static void morphBlockDropStacksHook(BlockState state, ServerWorld world, BlockPos pos, BlockEntity blockEntity, CallbackInfoReturnable<List<ItemStack>> cir) {
		morphBlockDropStacksCallback(state, world, pos, blockEntity, null, null, cir);
	}

	private static void morphBlockDropStacksCallback(BlockState state, ServerWorld world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack stack, CallbackInfoReturnable<List<ItemStack>> cir) {
		TypedEventReaction<List<ItemStack>> ret = MorphBlockDropStacksEvent.EVENT.invoker().morph(state, world, pos, blockEntity, entity, stack, cir.getReturnValue());
		EventReaction reaction = ret.getReaction();
		if(reaction == EventReaction.CANCEL) cir.setReturnValue(Collections.emptyList());
		else if(reaction == EventReaction.COMPLETE) cir.setReturnValue(ret.getValue());
	}
}
