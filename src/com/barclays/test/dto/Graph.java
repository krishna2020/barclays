package com.barclays.test.dto;

public class Graph {

	public int adjMatrix[][];
	private int vertexCount;

	public Graph(int vertexCount) {
		this.vertexCount = vertexCount;
		adjMatrix = new int[vertexCount][vertexCount];
	}

	public void addEdge(int i, int j, int distance) {
		if (i >= 0 && i < vertexCount && j >= 0 && j < vertexCount) {
			adjMatrix[i][j] = distance;
			adjMatrix[j][i] = distance;
		}
	}

	public void removeEdge(int i, int j) {
		if (i >= 0 && i < vertexCount && j >= 0 && j < vertexCount) {
			adjMatrix[i][j] = 0;
			adjMatrix[j][i] = 0;
		}
	}

	public boolean isEdge(int i, int j) {
		if (i >= 0 && i < vertexCount && j >= 0 && j < vertexCount) {
			if (adjMatrix[i][j] != 0)
				return true;
		}
		return false;
	}

	public int getDist(int i, int j) {
		if (i >= 0 && i < vertexCount && j >= 0 && j < vertexCount)
			return adjMatrix[i][j];
		else
			return 0;
	}

}
