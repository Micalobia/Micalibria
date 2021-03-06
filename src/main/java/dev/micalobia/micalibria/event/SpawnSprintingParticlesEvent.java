package dev.micalobia.micalibria.event;


import dev.micalobia.micalibria.event.enums.TypedEventReaction;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;


public interface SpawnSprintingParticlesEvent {
	Event<SpawnSprintingParticlesEvent> EVENT = EventFactory.createArrayBacked(
			SpawnSprintingParticlesEvent.class,
			(listeners) -> (state, pos, entity) -> {
				BlockState passing = state;
				for(SpawnSprintingParticlesEvent callback : listeners) {
					TypedEventReaction<BlockState> ret = callback.getState(passing, pos, entity);
					switch(ret.getReaction()) {
						case CANCEL:
							return TypedEventReaction.cancel();
						case COMPLETE:
							return TypedEventReaction.complete(passing);
						case PROCESS:
							passing = ret.getValue();
					}
				}
				return TypedEventReaction.completeIfChanged(state, passing);
			}
	);

	TypedEventReaction<BlockState> getState(BlockState state, BlockPos pos, Entity entity);
}
