package dev.micalobia.micalibria.mixin.server.network;

import dev.micalobia.micalibria.event.ServerPlayerBrokeBlockEvent;
import dev.micalobia.micalibria.event.enums.EventReaction;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
	@Shadow
	public ServerWorld world;

	@Shadow
	public ServerPlayerEntity player;

	@Inject(method = "tryBreakBlock", cancellable = true, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onBreak(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)V"))
	public void serverPlayerBrokeBlockHook(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		EventReaction ret = ServerPlayerBrokeBlockEvent.EVENT.invoker().breakBlock(this.world, this.player, pos);
		if(ret == EventReaction.COMPLETE) cir.setReturnValue(true);
		else if(ret == EventReaction.CANCEL) cir.setReturnValue(false);
	}
}
