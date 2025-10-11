package random.dust.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import random.dust.RandomDustMod;
import random.dust.access.ILevel;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level implements ILevel {

	private ServerLevelMixin() {
		super(null, null, null, null, false, false, 0L, 0);
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
