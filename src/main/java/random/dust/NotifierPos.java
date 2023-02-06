package random.dust;

import net.minecraft.core.BlockPos;

public class NotifierPos {

	public final BlockPos offset;
	public final BlockPos pos;

	private Integer hash;

	public NotifierPos(BlockPos offset, BlockPos pos) {
		this.offset = offset;
		this.pos = pos;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof NotifierPos)) {
			return false;
		}

		return pos.equals(((NotifierPos)obj).pos);
	}

	@Override
	public int hashCode() {
		if (hash == null) {
			hash = pos.offset(offset).hashCode();
		}

		return hash;
	}
}
