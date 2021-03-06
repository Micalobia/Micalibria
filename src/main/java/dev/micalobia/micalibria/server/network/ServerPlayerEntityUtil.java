package dev.micalobia.micalibria.server.network;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.HitResult;

public class ServerPlayerEntityUtil {
	private final ServerPlayerEntity player;

	public ServerPlayerEntityUtil(ServerPlayerEntity player) {
		this.player = player;
	}

	public static HitResult crosshair(ServerPlayerEntity player) {
		return crosshair(player, 0f);
	}

	public static HitResult crosshair(ServerPlayerEntity player, float tickDelta) {
		return crosshair(player, tickDelta, player.isCreative() ? 4.5f : 3.0f);
	}

	public static HitResult crosshair(ServerPlayerEntity player, float tickDelta, float reachDistance) {
		return player.raycast(reachDistance, tickDelta, false);
	}

	public HitResult crosshair() {
		return crosshair(player);
	}

	public HitResult crosshair(float tickDelta) {
		return crosshair(player, tickDelta);
	}

	public HitResult crosshair(float tickDelta, float reachDistance) {
		return crosshair(player, tickDelta, reachDistance);
	}
}
