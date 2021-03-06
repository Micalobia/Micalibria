package dev.micalobia.micalibria.mixin.client.render.block;

import dev.micalobia.micalibria.event.RenderDamageEvent;
import dev.micalobia.micalibria.event.enums.TypedEventReaction;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(BlockRenderManager.class)
public class BlockRenderManagerMixin {
	@Shadow
	@Final
	private BlockModels models;

	@Shadow
	@Final
	private BlockModelRenderer blockModelRenderer;

	@Shadow
	@Final
	private Random random;

	@Inject(method = "renderDamage", cancellable = true, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/BlockModels;getModel(Lnet/minecraft/block/BlockState;)Lnet/minecraft/client/render/model/BakedModel;"))
	public void renderDamageHook(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrix, VertexConsumer vertexConsumer, CallbackInfo ci) {
		TypedEventReaction<BlockState> ret = RenderDamageEvent.EVENT.invoker().renderSlabDamage(state, pos, world, matrix, vertexConsumer);
		switch(ret.getReaction()) {
			case CANCEL:
				ci.cancel();
			case IGNORE:
				return;
			default:
				BlockState trueState = ret.getValue();
				BakedModel model = this.models.getModel(trueState);
				long l = trueState.getRenderingSeed(pos);
				this.blockModelRenderer.render(world, model, state, pos, matrix, vertexConsumer, true, this.random, l, OverlayTexture.DEFAULT_UV);
		}
	}
}
