package dev.micalobia.micalibria.mixin.world;

import dev.micalobia.micalibria.event.OnBlockChangedEvent;
import dev.micalobia.micalibria.event.SetBlockStateEvent;
import dev.micalobia.micalibria.event.enums.PairedEventReaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(World.class)
public class WorldMixin {
	private World self() {
		return (World) (Object) this;
	}

	@Inject(method = "onBlockChanged", at = @At("HEAD"))
	public void onBlockChangedHook(BlockPos pos, BlockState oldBlock, BlockState newBlock, CallbackInfo ci) {
		OnBlockChangedEvent.EVENT.invoker().onBlockChanged(self(), pos, oldBlock, newBlock);
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/WorldChunk;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Z)Lnet/minecraft/block/BlockState;"), method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z")
	public BlockState setBlockStateHook(WorldChunk worldChunk, BlockPos pos, BlockState state, boolean moved) {
		PairedEventReaction<BlockState, Optional<BlockEntity>> value =
				SetBlockStateEvent.EVENT.invoker().getState(self(), pos, state, null, moved);
		switch(value.getReaction()) {
			case IGNORE:
				return worldChunk.setBlockState(pos, state, moved);
			case CANCEL:
				return null;
			default:
				Optional<BlockEntity> entity = value.getRight();
				BlockState ret = worldChunk.setBlockState(pos, value.getLeft(), moved);
				entity.ifPresent(blockEntity -> worldChunk.setBlockEntity(pos, blockEntity));
				return ret;
		}
	}
}
