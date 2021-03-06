package dev.micalobia.micalibria.event;

import dev.micalobia.micalibria.event.enums.TypedEventReaction;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;

public interface HitGroundParticlesEvent {
	Event<HitGroundParticlesEvent> EVENT = EventFactory.createArrayBacked(
			HitGroundParticlesEvent.class,
			(listeners) -> (state, entity) -> {
				BlockState passing = state;
				for(HitGroundParticlesEvent callback : listeners) {
					TypedEventReaction<BlockState> ret = callback.getState(state, entity);
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

	TypedEventReaction<BlockState> getState(BlockState state, LivingEntity entity);
}
