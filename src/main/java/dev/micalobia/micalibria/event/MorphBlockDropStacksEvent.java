package dev.micalobia.micalibria.event;

import dev.micalobia.micalibria.event.enums.TypedEventReaction;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface MorphBlockDropStacksEvent {
	Event<MorphBlockDropStacksEvent> EVENT = EventFactory.createArrayBacked(
			MorphBlockDropStacksEvent.class,
			(listeners) -> (state, world, pos, blockEntity, entity, stack, dropped) ->
			{
				List<ItemStack> passing = dropped;
				for(MorphBlockDropStacksEvent callback : listeners) {
					TypedEventReaction<List<ItemStack>> ret = callback.morph(state, world, pos, blockEntity, entity, stack, passing);
					switch(ret.getReaction()) {
						case COMPLETE:
							return ret;
						case CANCEL:
							return TypedEventReaction.cancel();
						case PROCESS:
							passing = ret.getValue();
					}
				}
				return TypedEventReaction.completeIfChanged(dropped, passing);
			}
	);

	TypedEventReaction<List<ItemStack>> morph(BlockState state, ServerWorld world, BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity entity, ItemStack stack, List<ItemStack> dropped);
}
