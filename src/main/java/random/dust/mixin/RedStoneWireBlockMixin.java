package random.dust.mixin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;

import random.dust.NotifierPos;
import random.dust.RandomDustMod;

@Mixin(RedStoneWireBlock.class)
public class RedStoneWireBlockMixin {

	private static final Direction[] DIRECTIONS = Direction.values();

	@Inject(
		method = "updatePowerStrength",
		cancellable = true,
		at = @At(
			remap = false,
			value = "INVOKE",
			target = "Lcom/google/common/collect/Sets;newHashSet()Ljava/util/HashSet;"
		)
	)
	private void randomDust$updateNeighbors(Level level, BlockPos pos, BlockState state, CallbackInfo ci) {
		if (RandomDustMod.interval >= 0) {
			if (RandomDustMod.interval == 0) {
				updateNeighborsRandomly(level, pos);
			} else {
				updateNeighborsOffset(level, pos);
			}

			ci.cancel();
		}
	}

	private void updateNeighborsRandomly(Level level, BlockPos pos) {
		List<BlockPos> notifiers = new ArrayList<>();

		notifiers.add(pos);
		for (Direction dir : DIRECTIONS) {
			notifiers.add(pos.relative(dir));
		}

		Collections.shuffle(notifiers);

		for (int i = 0; i < notifiers.size(); i++) {
			level.updateNeighborsAt(notifiers.get(i), (Block)(Object)this);
		}
	}

	private void updateNeighborsOffset(Level level, BlockPos pos) {
		BlockPos offset = level.randomDust$getOffset();
		Set<NotifierPos> notifiers = new HashSet<>();

		notifiers.add(new NotifierPos(offset, pos));
		for (Direction dir : DIRECTIONS) {
			notifiers.add(new NotifierPos(offset, pos.relative(dir)));
		}

		for (NotifierPos notifier : notifiers) {
			level.updateNeighborsAt(notifier.pos, (Block)(Object)this);
		}
	}
}
