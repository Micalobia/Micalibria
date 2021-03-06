package dev.micalobia.micalibria.mixin.client.particle;

import dev.micalobia.micalibria.client.particle.ParticleManagerUtil;
import dev.micalobia.micalibria.event.BlockBreakingParticleEvent;
import dev.micalobia.micalibria.event.enums.TypedEventReaction;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
	@Shadow
	protected ClientWorld world;

	@Shadow
	@Final
	private Random random;

	@Inject(method = "addBlockBreakingParticles", cancellable = true, at = @At("HEAD"))
	public void blockBreakingParticleHook(BlockPos pos, Direction direction, CallbackInfo ci) {
		TypedEventReaction<BlockState> ret = BlockBreakingParticleEvent.EVENT.invoker().getState(this.world.getBlockState(pos), pos, this.world, direction, this.random);
		switch(ret.getReaction()) {
			case IGNORE:
				return;
			case COMPLETE:
				ParticleManagerUtil.addBreakingParticles(self(), ret.getValue(), pos, this.world, direction, random);
			default: //CANCEL
				ci.cancel();
		}
	}

	private ParticleManager self() {
		return (ParticleManager) (Object) this;
	}
}
