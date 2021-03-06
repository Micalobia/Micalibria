package dev.micalobia.micalibria.event;

import dev.micalobia.micalibria.event.enums.TypedEventReaction;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;

public interface PlaceBlockItemEvent {
	Event<PlaceBlockItemEvent> EVENT = EventFactory.createArrayBacked(
			PlaceBlockItemEvent.class,
			(listeners) -> (context, state) -> {
				BlockState passing = state;
				for(PlaceBlockItemEvent callback : listeners) {
					TypedEventReaction<BlockState> ret = callback.getState(context, passing);
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

	TypedEventReaction<BlockState> getState(ItemPlacementContext context, BlockState state);
}
