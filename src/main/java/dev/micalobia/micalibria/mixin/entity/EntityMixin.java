package dev.micalobia.micalibria.mixin.entity;

import dev.micalobia.micalibria.event.SpawnSprintingParticlesEvent;
import dev.micalobia.micalibria.event.enums.TypedEventReaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public class EntityMixin {
	@Shadow
	public World world;

	@Redirect(method = "spawnSprintingParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
	public BlockState spawnSprintingParticlesHook(World world, BlockPos pos) {
		BlockState state = this.world.getBlockState(pos);
		TypedEventReaction<BlockState> ret = SpawnSprintingParticlesEvent.EVENT.invoker().getState(state, pos, self());
		switch(ret.getReaction()) {
			case CANCEL:
				// Has BlockRenderType.INVISIBLE, so the if statement fails and it's essentially a ci.cancel
				return Blocks.AIR.getDefaultState();
			case IGNORE:
				return state;
			default:
				return ret.getValue();
		}
	}

	private Entity self() {
		return (Entity) (Object) this;
	}
}
