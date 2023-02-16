package dev.micalobia.micalibria.mixin.item;

import net.minecraft.item.Item;
import net.minecraft.util.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Item.class)
public interface ItemAccessor {
	@Mutable
	@Accessor
	void setMaxCount(int value);

	@Mutable
	@Accessor
	void setMaxDamage(int value);

	@Mutable
	@Accessor
	void setFireproof(boolean value);

	@Mutable
	@Accessor
	void setRarity(Rarity rarity);
}
