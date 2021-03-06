package dev.micalobia.micalibria.mixin.server.world;

import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {
	protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DimensionType dimensionType, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed) {
		super(properties, registryRef, dimensionType, profiler, isClient, debugWorld, seed);
	}

	@Inject(method = "onBlockChanged", at = @At("HEAD"))
	public void onBlockChangedSuper(BlockPos pos, BlockState oldBlock, BlockState newBlock, CallbackInfo ci) {
		super.onBlockChanged(pos, oldBlock, newBlock);
	}

	@Inject(method = "spawnParticles(Lnet/minecraft/particle/ParticleEffect;DDDIDDDD)I", at = @At("HEAD"), cancellable = true)
	public <T extends ParticleEffect> void cancelIfNull(T particle, double x, double y, double z, int count, double deltaX, double deltaY, double deltaZ, double speed, CallbackInfoReturnable<Integer> cir) {
		if(particle == null) cir.setReturnValue(0);
	}
}
