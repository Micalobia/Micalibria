package dev.micalobia.micalibria.event;

import dev.micalobia.micalibria.event.enums.EventReaction;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public interface ClientPlayerBrokeBlockEvent {
	Event<ClientPlayerBrokeBlockEvent> EVENT = EventFactory.createArrayBacked(
			ClientPlayerBrokeBlockEvent.class,
			(listeners) -> (client, pos) -> {
				EventReaction reaction = EventReaction.IGNORE;
				for(ClientPlayerBrokeBlockEvent callback : listeners) {
					reaction = callback.breakBlock(client, pos);
					if(!reaction.continueProcessing()) return reaction;
				}
				return reaction;
			}
	);

	EventReaction breakBlock(MinecraftClient client, BlockPos pos);
}
