package dev.micalobia.micalibria.client.particle;

import net.minecraft.block.BlockState;
import net.minecraft.client.particle.BlockDustParticle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

import java.util.Random;

public class ParticleManagerUtil {
	private final ParticleManager manager;

	public ParticleManagerUtil(ParticleManager manager) {
		this.manager = manager;
	}

	public static void addBreakingParticles(ParticleManager manager, BlockState state, BlockPos pos, ClientWorld world, Direction direction, Random random) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		Box box = state.getOutlineShape(world, pos).getBoundingBox();
		double dx = x + random.nextDouble() * (box.getXLength() - .2d) + .1d + box.minX;
		double dy = y + random.nextDouble() * (box.getYLength() - .2d) + .1d + box.minY;
		double dz = z + random.nextDouble() * (box.getZLength() - .2d) + .1d + box.minZ;
		switch(direction) {
			case DOWN:
				dy = y + box.minY - .1d;
				break;
			case UP:
				dy = y + box.maxY + .1d;
				break;
			case NORTH:
				dz = z + box.minZ - .1d;
				break;
			case SOUTH:
				dz = z + box.maxZ + .1d;
				break;
			case WEST:
				dx = x + box.minX - .1d;
				break;
			case EAST:
				dx = x + box.maxX + .1d;
				break;
		}
		manager.addParticle((new BlockDustParticle(world, dx, dy, dz, 0.0D, 0.0D, 0.0D, state)).setBlockPos(pos).move(0.2F).scale(0.6F));
	}

	public void addBreakingParticles(BlockState state, BlockPos pos, ClientWorld world, Direction direction, Random random) {
		addBreakingParticles(manager, state, pos, world, direction, random);
	}
}
