package dev.micalobia.micalibria.mixin.entity;

import dev.micalobia.micalibria.event.HitGroundParticlesEvent;
import dev.micalobia.micalibria.event.enums.TypedEventReaction;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	private LivingEntity self() {
		return (LivingEntity) (Object) this;
	}

	@Redirect(method = "fall", at = @At(value = "NEW", target = "net/minecraft/particle/BlockStateParticleEffect"))
	public BlockStateParticleEffect hitGroundParticlesHook(ParticleType<BlockStateParticleEffect> type, BlockState state) {
		TypedEventReaction<BlockState> ret = HitGroundParticlesEvent.EVENT.invoker().getState(state, self());
		switch(ret.getReaction()) {
			case CANCEL:
				return null;
			case IGNORE:
				return new BlockStateParticleEffect(type, state);
			default:
				return new BlockStateParticleEffect(type, ret.getValue());
		}
	}
}
