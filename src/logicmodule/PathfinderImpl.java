package logicmodule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import shared.City;

/**
 * @author Kerekes Zoltán
 */
class PathfinderImpl implements Pathfinder {
	
	class Node {
		public City representedCity;
		public List<Path> paths;
		public Node(City representedCity){
			this.representedCity = representedCity;
			paths = new ArrayList<>();
		}
	}
	
	class Path {
		public Route representedRoute;
		public List<Node> nodes;
		public Path(Route representedRoute){
			this.representedRoute = representedRoute;
			nodes = new ArrayList<>();
		}
	}
	
	private List<Node> nodes;
	private List<Path> paths;
	private Map<City, Node> nodeMap;
	private Map<Route, Path> pathMap;

	public PathfinderImpl(List<Route> routes) {
		nodes = new ArrayList<>();
		paths = new ArrayList<>();
		nodeMap = new HashMap<>();
		pathMap = new HashMap<>();
		for (City city : City.values()) {
			Node node = new Node(city);
			nodes.add(node);
			nodeMap.put(city, node);
		}
		for (Route route : routes) {
			Path path = new Path(route);
			paths.add(path);
			pathMap.put(route, path);
			for (City city : route.getDestinations()) {
				Node node = nodeMap.get(city);
				path.nodes.add(node);
				node.paths.add(path);
			}
		}
	}

	@Override
	public List<Player> getLongestPathOwners() {
		// TODO [logic] getLongestPathOwners()
		return new ArrayList<>();
	}

	@Override
	public boolean pathExistsBetween(City cityA, City cityB, Player player) {
		List<Node> closedNodes = new ArrayList<>();
		List<Node> openNodes = new ArrayList<>();
		openNodes.add(nodeMap.get(cityA));
		Node goal = nodeMap.get(cityB);
		while(!openNodes.isEmpty()){
			// select one
			Node curr = openNodes.remove(0);
			// found
			if(curr == goal)
				return true;
			// visit every path from the node
			for (Path path : curr.paths) {
				// two rails
				for (Rail rail : path.representedRoute.getRails()) {
					// Is owned by the selected player?
					if(rail.getOwner() == player){
						// two endpoints
						for (City dest : path.representedRoute.getDestinations()) {
							Node destNode = nodeMap.get(dest);
							// Is this the another non-searched node?
							if(destNode != curr && !closedNodes.contains(destNode) && !openNodes.contains(destNode))
								openNodes.add(destNode);
						}
						// skip another rail
						break;
					}
				}
			}
			// it is visited
			closedNodes.add(curr);
		}
		return false;
	}

}
