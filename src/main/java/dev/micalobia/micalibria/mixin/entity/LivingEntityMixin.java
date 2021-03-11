package dev.micalobia.micalibria.mixin.entity;

import dev.micalobia.micalibria.event.HitGroundParticlesEvent;
import dev.micalobia.micalibria.event.enums.EventReaction;
import dev.micalobia.micalibria.event.enums.TypedEventReaction;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	private LivingEntity self() {
		return (LivingEntity) (Object) this;
	}

	//@Redirect(method = "fall", at = @At(value = "NEW", target = "net/minecraft/particle/BlockStateParticleEffect"))
	@Inject(method = "fall", cancellable = true, at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnParticles(Lnet/minecraft/particle/ParticleEffect;DDDIDDDD)I"), locals = LocalCapture.CAPTURE_FAILSOFT)
	public void hitGroundParticlesHook(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition, CallbackInfo ci, float f, double d, int i) {
		TypedEventReaction<BlockState> ret = HitGroundParticlesEvent.EVENT.invoker().getState(state, self());
		if(ret.getReaction() != EventReaction.IGNORE) {
			ci.cancel();
			if(ret.getReaction() == EventReaction.CANCEL) return;
			((ServerWorld) this.world).spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, ret.getValue()), this.getX(), this.getY(), this.getZ(), i, 0.0d, 0.0d, 0.0d, 0.15000000596046448);
			super.fall(heightDifference, onGround, state, landedPosition);
		}
	}
}
