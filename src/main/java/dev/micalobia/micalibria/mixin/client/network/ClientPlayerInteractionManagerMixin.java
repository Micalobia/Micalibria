package dev.micalobia.micalibria.mixin.client.network;

import dev.micalobia.micalibria.event.ClientPlayerBrokeBlockEvent;
import dev.micalobia.micalibria.event.enums.EventReaction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
	@Shadow
	@Final
	private MinecraftClient client;

	@Inject(method = "breakBlock", cancellable = true, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onBreak(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)V"))
	public void clientPlayerBrokeBlockHook(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		EventReaction reaction = ClientPlayerBrokeBlockEvent.EVENT.invoker().breakBlock(this.client, pos);
		if(reaction == EventReaction.COMPLETE) cir.setReturnValue(true);
		else if(reaction == EventReaction.CANCEL) cir.setReturnValue(false);
	}
}
