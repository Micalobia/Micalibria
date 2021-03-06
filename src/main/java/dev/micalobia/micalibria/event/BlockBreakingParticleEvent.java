package dev.micalobia.micalibria.event;

import dev.micalobia.micalibria.event.enums.TypedEventReaction;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Random;

public interface BlockBreakingParticleEvent {
	Event<BlockBreakingParticleEvent> EVENT = EventFactory.createArrayBacked(
			BlockBreakingParticleEvent.class,
			(listeners) -> (state, pos, world, direction, random) -> {
				BlockState passing = state;
				for(BlockBreakingParticleEvent callback : listeners) {
					TypedEventReaction<BlockState> ret = callback.getState(passing, pos, world, direction, random);
					switch(ret.getReaction()) {
						case CANCEL:
							return TypedEventReaction.cancel();
						case COMPLETE:
							return ret;
						case PROCESS:
							passing = ret.getValue();
					}
				}
				return TypedEventReaction.completeIfChanged(state, passing);
			}
	);

	TypedEventReaction<BlockState> getState(BlockState state, BlockPos pos, ClientWorld world, Direction direction, Random random);
}
