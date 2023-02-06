package random.dust;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public class RandomizeRedstoneDustCommand {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.
			literal("randomizeredstonedust").
			requires(source -> source.hasPermission(2)).
			executes(context -> query(context.getSource())).
			then(Commands.
				argument("interval", IntegerArgumentType.integer(-1, Integer.MAX_VALUE)).
				executes(context -> set(context.getSource(), IntegerArgumentType.getInteger(context, "interval")))).
			then(Commands.
				literal("pause").
				executes(context -> paused(context.getSource(), true))).
			then(Commands.
				literal("resume").
				executes(context -> paused(context.getSource(), false)));

		dispatcher.register(builder);
	}

	private static int query(CommandSourceStack source) {
		Component message;

		if (RandomDustMod.interval < 0) {
			message = Component.literal("randomized redstone dust is disabled");
		} else if (RandomDustMod.interval == 0) {
			message = Component.literal("redstone dust update order randomizes for each individual wire");
		} else {
			message = Component.literal("redstone dust update order randomizes every " + RandomDustMod.interval + " ticks");
		}

		source.sendSuccess(message, false);

		return Command.SINGLE_SUCCESS;
	}

	private static int set(CommandSourceStack source, int interval) {
		RandomDustMod.setInterval(interval);

		Component message;

		if (interval < 0) {
			message = Component.literal("disabled randomized redstone dust");
		} else if (interval == 0) {
			message = Component.literal("redstone dust update order will randomize for each individual wire");
		} else {
			message = Component.literal("redstone dust update order will randomize every " + interval + " ticks");
		}

		source.sendSuccess(message, true);

		return Command.SINGLE_SUCCESS;
	}

	private static int paused(CommandSourceStack source, boolean paused) {
		if (RandomDustMod.paused == paused) {
			source.sendFailure(Component.literal("redstone dust randomizing is " + (paused ? "already" : "not") + " paused!"));
			return 0;
		}
		if (paused) {
			if (RandomDustMod.interval < 0) {
				source.sendFailure(Component.literal("redstone dust randomizing is not enabled!"));
				return 0;
			}
			if (RandomDustMod.interval == 0) {
				source.sendFailure(Component.literal("can only pause redstone dust randomizing if interval is greater than 0!"));
				return 0;
			}
		}

		RandomDustMod.paused = paused;

		if (paused) {
			Level level = source.getLevel();
			source.sendSuccess(Component.literal("paused redstone dust randomizing at offset ").
				append(Component.literal(level.randomDust$getOffset().toShortString())), true);
		} else {
			source.sendSuccess(Component.literal("resumed redstone dust randomizing"), true);
		}

		return Command.SINGLE_SUCCESS;
	}
}
