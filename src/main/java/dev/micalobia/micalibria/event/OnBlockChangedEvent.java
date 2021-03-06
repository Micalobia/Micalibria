package dev.micalobia.micalibria.event;

import dev.micalobia.micalibria.event.enums.EventReaction;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface OnBlockChangedEvent {
	Event<OnBlockChangedEvent> EVENT = EventFactory.createArrayBacked(
			OnBlockChangedEvent.class,
			(listeners) -> (world, pos, oldState, newState) -> {
				EventReaction reaction = EventReaction.IGNORE;
				for(OnBlockChangedEvent callback : listeners) {
					reaction = callback.onBlockChanged(world, pos, oldState, newState);
					if(!reaction.continueProcessing()) return reaction;
				}
				return reaction;
			}
	);

	EventReaction onBlockChanged(World world, BlockPos pos, BlockState oldState, BlockState newState);
}
