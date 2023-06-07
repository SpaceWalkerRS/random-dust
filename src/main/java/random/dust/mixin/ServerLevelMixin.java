package random.dust.mixin;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;

import random.dust.RandomDustMod;
import random.dust.access.ILevel;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level implements ILevel {

	private ServerLevelMixin(WritableLevelData data, ResourceKey<Level> key, RegistryAccess registryAccess, Holder<DimensionType> dimension, Supplier<ProfilerFiller> profiler, boolean isClientSide, boolean isDebug, long seed, int maxChainedNeighborUpdates) {
		super(data, key, registryAccess, dimension, profiler, isClientSide, isDebug, seed, maxChainedNeighborUpdates);
	}

	private int ticks = 0;
	private BlockPos offset;

	@Inject(
		method = "tick",
		at = @At(
			value = "HEAD"
		)
	)
	private void randomDust$tickOffset(CallbackInfo ci) {
		if (RandomDustMod.interval > 0) {
			if (!RandomDustMod.paused && ticks++ % RandomDustMod.interval == 0) {
				int width = (1 << 16) - 1;
				int height = getHeight() / 2;

				int offsetX = (int)Math.round(width * random.nextGaussian());
				int offsetY = random.nextInt(2 * height) - height;
				int offsetZ = (int)Math.round(width * random.nextGaussian());

				offset = new BlockPos(offsetX, offsetY, offsetZ);
			}
		} else {
			ticks = 0;
		}
	}

	@Override
	public BlockPos randomDust$getOffset() {
		return offset;
	}
}
