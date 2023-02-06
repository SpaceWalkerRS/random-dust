package random.dust.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;

import random.dust.RandomDustMod;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

	@Inject(
		method = "loadLevel",
		at = @At(
			value = "HEAD"
		)
	)
	private void randomDust$init(CallbackInfo info) {
		RandomDustMod.reset();
	}
}
