package com.barclays.test.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;

import com.barclays.test.dto.Graph;

public class Solution {

	List<String> bagInput = new ArrayList<String>();
	Map<String, Integer> nodes = new HashMap<String, Integer>();
	Map<String, Integer> flights = new HashMap<String, Integer>();
	Stack<String> nodePath = new Stack<String>();
	int dist;

	public Graph initialize(String file) {
		boolean conveyor = false, departures = false, bags = false;
		List<String> lines = new ArrayList<String>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			int j = 0;
			for (String line; (line = br.readLine()) != null;) {

				if (line.startsWith("# Section:")) {
					if (line.startsWith("# Section: Conveyor System")) {
						conveyor = true;
					} else if (line.startsWith("# Section: Departures")) {
						conveyor = false;
						departures = true;
					} else if (line.startsWith("# Section: Bags")) {
						bags = true;
						departures = false;
					}
				} else {
					if (conveyor) {
						lines.add(line);
						String[] cols = line.split(" ");
						if (!nodes.containsKey(cols[0])) {
							nodes.put(cols[0], j);
							j++;
						}
						if (!nodes.containsKey(cols[1])) {
							nodes.put(cols[1], j);
							j++;
						}
					}

					if (departures) {
						String[] cols = line.split(" ");
						flights.put(cols[0], nodes.get(cols[1]));
					}

					if (bags) {
						bagInput.add(line);
					}
				}

			}

			Graph graph = new Graph(nodes.size());
			for (int i = 0; i < lines.size(); i++) {
				String[] cols = lines.get(i).split(" ");
				graph.addEdge(nodes.get(cols[0]), nodes.get(cols[1]), Integer
						.parseInt(cols[2]));
			}

			return graph;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void printMap() {
		Iterator<Entry<String, Integer>> itr = nodes.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, Integer> pair = itr.next();
			System.out.println(pair.getKey() + ":" + pair.getValue());
		}
	}

	public void findPath(int i, int j, Graph graph) {
		int cntr = 1;
		int dest = j;
		dist = 0;
		nodePath.clear();
		nodePath.push(getNode(j));
		List<Integer> visited = new ArrayList<Integer>();
		List<Integer> marked = new ArrayList<Integer>();
		
		while (true) {
			int k = 0;
			cntr = 1;
			for (; k < nodes.size(); k++) {
				if (!visited.contains(k)) {
					if (graph.isEdge(k, j)) {
						visited.add(j);
						dist += graph.getDist(k, j);
						nodePath.push(getNode(k));
						break;
					}
				}
				cntr++;
			}

			if (k == i)
				break;

			if (cntr > 11) {
				marked.add(Integer.valueOf(j));
				visited.clear();
				visited.addAll(marked);
				j = dest;
				dist = 0;
				nodePath.clear();
				nodePath.push(getNode(j));

			} else {
				j = k;
			}
		}
	}

	public void processBags(Graph graph) {
		for (int i = 0; i < bagInput.size(); i++) {
			String[] cols = bagInput.get(i).split(" ");
			Integer destination;
			if (cols[2].equals("ARRIVAL")) {
				destination = nodes.get("BaggageClaim");
			} else {
				destination = flights.get(cols[2]);
			}

			StringBuilder finalPath = new StringBuilder();
			finalPath.append(cols[0]);
			findPath(nodes.get(cols[1]), destination, graph);
			
			while(!nodePath.isEmpty()){
				finalPath.append(" ");
				finalPath.append(nodePath.pop());
			}
			finalPath.append(" : ");
			finalPath.append(dist);
			System.out.println(finalPath.toString());
			
		}
	}

	public String getNode(Integer val) {
		Iterator<Entry<String, Integer>> itr = nodes.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, Integer> pair = itr.next();
			if (pair.getValue().equals(val))
				return pair.getKey();
		}
		return null;
	}

	public static void main(String[] args) {
		Solution sol = new Solution();
		sol.processBags(sol.initialize("resources/input.txt"));
		System.out.println("Success");
	}

}
