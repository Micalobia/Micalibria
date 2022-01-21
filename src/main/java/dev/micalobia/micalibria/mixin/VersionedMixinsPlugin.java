package dev.micalobia.micalibria.mixin;

import net.minecraft.SharedConstants;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Will check the full classname of a mixin and only allow it if it passes the version check
 * <br/>
 * <code>.1_16_3__. => After 1.16.3 (inclusive)<br/>
 * .__1_15_2. => Before 1.15.2 (inclusive)<br/>
 * .1_13__1_18_1. => Between 1.13.x and 1.18.1 (inclusive)</code>
 */
public class VersionedMixinsPlugin implements IMixinConfigPlugin {
	private static final Pattern versionPattern =
			Pattern.compile("^(\\d+)(?:\\.(\\d+)(?:\\.(\\d+))?)?$");
	private static final Pattern versionRangePattern =
			Pattern.compile("\\.(?!__\\.)(?<min>\\d+(?:(?:_\\d+)?_\\d+)?)?__(?<max>\\d+(?:(?:_\\d+)?_\\d+)?)?\\.");

	private static boolean between_version(@Nullable String minimum, @Nullable String maximum) {
		//noinspection deprecation
		String version = SharedConstants.VERSION_NAME;
		var matcher = versionPattern.matcher(version);
		if(!matcher.matches()) return false;
		var minMatcher = versionPattern.matcher(Objects.requireNonNullElse(minimum, "0"));
		if(!minMatcher.matches()) throw new IllegalArgumentException("minimum was not valid!");
		var maxMatcher = versionPattern.matcher(Objects.requireNonNullElse(maximum, "2147483647"));
		if(!maxMatcher.matches()) throw new IllegalArgumentException("maximum was not valid!");
		for(int i = 1; i <= 3; ++i) {
			var group = matcher.group(i);
			if(group == null) return true;
			var val = Integer.parseInt(group);
			var minGroup = minMatcher.group(i);
			if(minGroup != null && val < Integer.parseInt(minGroup)) return false;
			var maxGroup = maxMatcher.group(i);
			if(maxGroup != null && val > Integer.parseInt(maxGroup)) return false;
			if(maxGroup == null && minGroup == null) return true;
		}
		return true;
	}

	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		var matcher = versionRangePattern.matcher(mixinClassName);
		while(matcher.find()) {
			String min = matcher.group("min");
			if(min != null) min = min.replace('_', '.');
			String max = matcher.group("max");
			if(max != null) max = max.replace('_', '.');
			if(!between_version(min, max)) return false;
		}
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}
}
