package dev.micalobia.micalibria.event;

import dev.micalobia.micalibria.event.enums.TypedEventReaction;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

public interface RenderDamageEvent {
	Event<RenderDamageEvent> EVENT = EventFactory.createArrayBacked(
			RenderDamageEvent.class,
			(listeners) -> (state, pos, world, matrix, vertexConsumer) -> {
				BlockState passing = state;
				for(RenderDamageEvent callback : listeners) {
					TypedEventReaction<BlockState> ret = callback.renderSlabDamage(passing, pos, world, matrix, vertexConsumer);
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

	TypedEventReaction<BlockState> renderSlabDamage(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrix, VertexConsumer vertexConsumer);
}
