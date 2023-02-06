package random.dust;

public class RandomDustMod {

	public static boolean paused = false;
	public static int interval = -1;

	public static void reset() {
		setInterval(-1);
	}

	public static void setInterval(int newInterval) {
		if (newInterval < 0) {
			newInterval = -1;
		}

		paused = false;
		interval = newInterval;
	}
}