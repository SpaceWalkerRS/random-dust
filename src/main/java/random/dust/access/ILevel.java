package random.dust.access;

import net.minecraft.core.BlockPos;

public interface ILevel {

	default BlockPos randomDust$getOffset() { throw new UnsupportedOperationException(); }

}
