package com.barclays.test.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
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
	//StringBuilder finalPath;
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
						// System.out.println("In conveyor");
						lines.add(line);
						String[] cols = line.split(" ");
						if (!nodes.containsKey(cols[0])) {
							nodes.put(cols[0], j);
							j++;
							// System.out.println(j);
						}
						if (!nodes.containsKey(cols[1])) {
							nodes.put(cols[1], j);
							j++;
							// System.out.println(j);
						}
					}

					if (departures) {
						// System.out.println("In departure");
						String[] cols = line.split(" ");
						flights.put(cols[0], nodes.get(cols[1]));
					}

					if (bags) {
						// System.out.println("In bag");
						bagInput.add(line);
					}
				}

			}

			//System.out.println("Nodes Length: " + nodes.size());
			//printMap();

			Graph graph = new Graph(nodes.size());
			for (int i = 0; i < lines.size(); i++) {
				String[] cols = lines.get(i).split(" ");
				graph.addEdge(nodes.get(cols[0]), nodes.get(cols[1]), Integer
						.parseInt(cols[2]));
			}

			//printMatrix(graph);
			return graph;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void printMatrix(Graph graph) {
		int[][] matrix = graph.adjMatrix;
		for (int i = 0; i < nodes.size(); i++) {
			for (int m = 0; m < nodes.size(); m++) {
				System.out.print(matrix[i][m] + " ");
			}
			System.out.print("\n");
		}
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
		
		//finalPath = new StringBuilder();
		//finalPath.append(" ");
		//finalPath.append(getNode(j));
		
		//System.out.println(getNode(j));
		while (true) {
			int k = 0;
			cntr = 1;
			for (; k < nodes.size(); k++) {
				if (!visited.contains(k)) {
					if (graph.isEdge(k, j)) {
						//System.out.println(getNode(k));
						visited.add(j);
						dist += graph.getDist(k, j);
						nodePath.push(getNode(k));
						//finalPath.append(" ");
						//finalPath.append(getNode(k));
						break;
					}
				}
				cntr++;
			}
			// j = k;
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
				//finalPath = new StringBuilder();
				//finalPath.append(getNode(j));
				//finalPath.append(" ");
				// visited.remove(Integer.valueOf(j));
				//System.out.println("Alternate Path");

			} else {
				j = k;
			}

		}
		
		//System.out.println("Distance:" + dist);
	}

	public void processBags(Graph graph) {
		//System.out.println("No of Inputs:" + bagInput.size());
		for (int i = 0; i < bagInput.size(); i++) {
			String[] cols = bagInput.get(i).split(" ");
			Integer destination;
			if (cols[2].equals("ARRIVAL")) {
				destination = nodes.get("BaggageClaim");
			} else {
				destination = flights.get(cols[2]);
			}

			//System.out.println("---------------");
			//System.out.println(cols[0]);
			
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
	}

}
