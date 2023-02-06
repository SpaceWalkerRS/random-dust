package random.dust.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import random.dust.access.ILevel;

@Mixin(Level.class)
public class LevelMixin implements ILevel {

	@Override
	public BlockPos randomDust$getOffset() {
		return BlockPos.ZERO;
	}
}
