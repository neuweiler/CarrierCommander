package net.carriercommander.objects;

import com.jme3.math.Vector3f;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class IslandMap {

	private final List<Island> islands = new ArrayList<>();
	private Island stockpileIsland = null;
	private static final IslandMap instance = new IslandMap();

	public static IslandMap getInstance() {
		return instance;
	}

	private IslandMap() {
		islands.add(new Island("Vulcan", new Vector3f(-4432.0f, -0.1f, -4276.0f), 12.9f, Island.IslandType.free));
		islands.add(new Island("Elwood", new Vector3f(-4198.0f, -0.1f, -3651.0f), 5.2f, Island.IslandType.free));
		islands.add(new Island("Byrne", new Vector3f(-3886.0f, -0.1f, -3886.0f), 4.6f, Island.IslandType.free));
		islands.add(new Island("Cerebus", new Vector3f(-3651.0f, -0.1f, -4471.0f), 3.6f, Island.IslandType.free));
		islands.add(new Island("Charissa", new Vector3f(-3183.0f, -0.1f, -4042.0f), 3.0f, Island.IslandType.free));
		islands.add(new Island("Magma", new Vector3f(-2597.0f, -0.1f, -4042.0f), 2.8f, Island.IslandType.free));
		islands.add(new Island("Duessa", new Vector3f(-3573.0f, -0.1f, -3339.0f), 5.7f, Island.IslandType.free));
		islands.add(new Island("Tokamak", new Vector3f(-2831.0f, -0.1f, -3339.0f) ,7.8f, Island.IslandType.free));
		islands.add(new Island("Edgeley", new Vector3f(-2948.0f, -0.1f, -2948.0f), 6.4f, Island.IslandType.free));
		islands.add(new Island("Avernus", new Vector3f(-3026.0f, -0.1f, -2284.0f), 7.2f, Island.IslandType.free));
		islands.add(new Island("Acheron", new Vector3f(-2401.0f, -0.1f, -2597.0f), 8.9f, Island.IslandType.free));
		islands.add(new Island("Beacon", new Vector3f(-1659.0f, -0.1f, -27530.0f), 3.6f, Island.IslandType.free));
		islands.add(new Island("Naiades", new Vector3f(-2011.0f, -0.1f, -20890.0f), 5.7f, Island.IslandType.free));
		islands.add(new Island("Igneous", new Vector3f(-1933.0f, -0.1f, -4315.0f), 2.6f, Island.IslandType.free));
		islands.add(new Island("Genetix", new Vector3f(-3651.0f, -0.1f, -2480.0f), 4.0f, Island.IslandType.free));
		islands.add(new Island("Socrates", new Vector3f(-4237.0f, -0.1f, -2480.0f), 3.6f, Island.IslandType.free));
		islands.add(new Island("Acheron", new Vector3f(-2401.0f, -0.1f, -2597.0f), 8.9f, Island.IslandType.free));
		islands.add(new Island("Bacchus", new Vector3f(-2011.0f, -0.1f, -1347.0f), 7.9f, Island.IslandType.free));
		islands.add(new Island("Beltempest", new Vector3f(-2323.0f, -0.1f, -0683.0f), 4.3f, Island.IslandType.free));
		islands.add(new Island("Bardland", new Vector3f(-2909.0f, -0.1f, -0214.0f), 3.6f, Island.IslandType.free));
		islands.add(new Island("Bedrock", new Vector3f(-2987.0f, -0.1f, 0410.0f), 4.3f, Island.IslandType.free));
		islands.add(new Island("Bountybar", new Vector3f(-2089.0f, -0.1f, 0331.0f), 12.9f, Island.IslandType.free));
		islands.add(new Island("Granite", new Vector3f(-3651.0f, -0.1f, 1034.0f), 4.3f, Island.IslandType.free));
		islands.add(new Island("Hytac", new Vector3f(-2597.0f, -0.1f, 1191.0f), 7.2f, Island.IslandType.free));
		islands.add(new Island("Mnemonic", new Vector3f(-1737.0f, -0.1f, 1464.0f), 4.3f, Island.IslandType.free));
		islands.add(new Island("Outcrop", new Vector3f(-1152.0f, -0.1f, 0839.0f), 5.2f, Island.IslandType.free));
		islands.add(new Island("Endymion", new Vector3f(-0566.0f, -0.1f, 1347.0f), 3.2f, Island.IslandType.free));
		islands.add(new Island("Somnus", new Vector3f(-0097.0f, -0.1f, 2050.0f), 3.3f, Island.IslandType.free));
		islands.add(new Island("Splinter", new Vector3f(-1230.0f, -0.1f, 3964.0f), 12.9f, Island.IslandType.free));
		islands.add(new Island("Outpost", new Vector3f(0683.0f, -0.1f, 2519.0f), 3.9f, Island.IslandType.free));
		islands.add(new Island("Frontier", new Vector3f(0917.0f, -0.1f, 1503.0f), 7.6f, Island.IslandType.free));
		islands.add(new Island("Fornax", new Vector3f(0253.0f, -0.1f, 1269.0f), 7.6f, Island.IslandType.free));
		islands.add(new Island("Cherenkov", new Vector3f(-0097.0f, -0.1f, 0527.0f), 8.6f, Island.IslandType.free));
		islands.add(new Island("Steadfast", new Vector3f(0839.0f, -0.1f, 0722.0f), 5.7f, Island.IslandType.free));
		islands.add(new Island("Judgement", new Vector3f(0410.0f, -0.1f, 0331.0f), 6.0f, Island.IslandType.free));
		islands.add(new Island("Evergreen", new Vector3f(-0488.0f, -0.1f, 0214.0f), 12.2f, Island.IslandType.free));
		islands.add(new Island("Fulcrum", new Vector3f(0019.0f, -0.1f, 0019.0f), 12.9f, Island.IslandType.free));
		islands.add(new Island("Vattland", new Vector3f(0253.0f, -0.1f, -0292.0f), 6.2f, Island.IslandType.free));
		islands.add(new Island("Taksaven", new Vector3f(0917.0f, -0.1f, -0292.0f), 6.8f, Island.IslandType.free));
		islands.add(new Island("Lingard", new Vector3f(-0097.0f, -0.1f, -0410.0f), 7.2f, Island.IslandType.free));
		islands.add(new Island("Stavros", new Vector3f(-0566.0f, -0.1f, -0292.0f), 10.8f, Island.IslandType.free));
		islands.add(new Island("Thermopylae", new Vector3f(-0995.0f, -0.1f, -0917.0f), 9.6f, Island.IslandType.free));
		islands.add(new Island("Storm", new Vector3f(-1503.0f, -0.1f, -1152.0f), 7.2f, Island.IslandType.free));
		islands.add(new Island("Terminus", new Vector3f(-1152.0f, -0.1f, -1464.0f), 3.2f, Island.IslandType.free));
		islands.add(new Island("Dyonysius", new Vector3f(-1152.0f, -0.1f, -2167.0f), 3.6f, Island.IslandType.free));
		islands.add(new Island("Medusa", new Vector3f(-0175.0f, -0.1f, -2011.0f), 9.0f, Island.IslandType.free));
		islands.add(new Island("Serrano", new Vector3f(0566.0f, -0.1f, -2480.0f), 8.4f, Island.IslandType.free));
		islands.add(new Island("Charibdis", new Vector3f(0253.0f, -0.1f, -3339.0f), 8.1f, Island.IslandType.free));
		islands.add(new Island("Isolus", new Vector3f(0566.0f, -0.1f, -1230.0f), 10.0f, Island.IslandType.free));
		islands.add(new Island("Obsidian", new Vector3f(1581.0f, -0.1f, 1503.0f), 7.0f, Island.IslandType.free));
		islands.add(new Island("Twilight", new Vector3f(2167.0f, -0.1f, 1191.0f), 10.2f, Island.IslandType.free));
		islands.add(new Island("Arachnid", new Vector3f(2089.0f, -0.1f, 0527.0f), 6.7f, Island.IslandType.free));
		islands.add(new Island("Deadlock", new Vector3f(1425.0f, -0.1f, 0956.0f), 2.8f, Island.IslandType.free));
		islands.add(new Island("Ursula", new Vector3f(1425.0f, -0.1f, 0253.0f), 4.7f, Island.IslandType.free));
		islands.add(new Island("Sanctuary", new Vector3f(1581.0f, -0.1f, -0253.0f), 7.0f, Island.IslandType.free));
		islands.add(new Island("Treasure", new Vector3f(2675.0f, -0.1f, -0371.0f), 7.0f, Island.IslandType.free));
		islands.add(new Island("Fears Edge", new Vector3f(2753.0f, -0.1f, 0956.0f), 7.2f, Island.IslandType.free));
		islands.add(new Island("Traffic", new Vector3f(3417.0f, -0.1f, 1034.0f), 6.1f, Island.IslandType.free));
		islands.add(new Island("Milestone", new Vector3f(2753.0f, -0.1f, 1503.0f), 10.8f, Island.IslandType.free));
		islands.add(new Island("Hades", new Vector3f(3339.0f, -0.1f, 1894.0f), 74.f, Island.IslandType.free));
		islands.add(new Island("Inferno", new Vector3f(2597.0f, -0.1f, 2206.0f), 5.2f, Island.IslandType.free));
		islands.add(new Island("Styx", new Vector3f(3261.0f, -0.1f, 2362.0f), 10.2f, Island.IslandType.free));
		islands.add(new Island("Kouyate", new Vector3f(3847.0f, -0.1f, 1737.0f), 7.9f, Island.IslandType.free));
		islands.add(new Island("Nemesis", new Vector3f(4159.0f, -0.1f, 2519.0f), 12.9f, Island.IslandType.free));
	}

	public List<Island> getIslands() {
		return islands;
	}

	public Optional<Island> getClosestIsland(Vector3f position) {
		Comparator<Island> minComparator = (n1, n2) ->
				Float.valueOf(n1.getPosition().distance(position))
						.compareTo(n2.getPosition().distance(position));
		return islands.stream().min(minComparator);
	}
}
