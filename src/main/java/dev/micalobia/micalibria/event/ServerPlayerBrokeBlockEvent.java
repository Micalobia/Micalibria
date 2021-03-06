package dev.micalobia.micalibria.event;

import dev.micalobia.micalibria.event.enums.EventReaction;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public interface ServerPlayerBrokeBlockEvent {
	Event<ServerPlayerBrokeBlockEvent> EVENT = EventFactory.createArrayBacked(
			ServerPlayerBrokeBlockEvent.class,
			(listeners) -> (world, player, pos) -> {
				EventReaction reaction = EventReaction.IGNORE;
				for(ServerPlayerBrokeBlockEvent callback : listeners) {
					reaction = callback.breakBlock(world, player, pos);
					if(!reaction.continueProcessing()) return reaction;
				}
				return reaction;
			}
	);

	EventReaction breakBlock(ServerWorld world, ServerPlayerEntity player, BlockPos pos);
}
