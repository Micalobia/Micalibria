package dev.micalobia.micalibria.event;

import dev.micalobia.micalibria.event.enums.PairedEventReaction;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public interface SetBlockStateEvent {
	Event<SetBlockStateEvent> EVENT = EventFactory.createArrayBacked(
			SetBlockStateEvent.class,
			(listeners) -> (world, pos, state, moved) -> {
				BlockState passingState = state;
				BlockEntity passingEntity = null;
				for(SetBlockStateEvent callback : listeners) {
					PairedEventReaction<BlockState, Optional<BlockEntity>> ret = callback.getState(world, pos, state, moved);
					switch(ret.getReaction()) {
						case CANCEL:
							return PairedEventReaction.cancel();
						case COMPLETE:
							return ret;
						case PROCESS:
							passingState = ret.getLeft();
							passingEntity = ret.getRight().orElse(passingEntity);
					}
				}
				if(passingState == state && passingEntity == null)
					return PairedEventReaction.ignore(state, Optional.empty());
				else
					return PairedEventReaction.complete(passingState, Optional.ofNullable(passingEntity));
			}
	);

	PairedEventReaction<BlockState, Optional<BlockEntity>> getState(World world, BlockPos pos, BlockState state, boolean moved);
}
