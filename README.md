# Dijkstra-s-Algorithm-Demonstrator

---
# Dijkstra's Algorithm - Shortest Path Finder


## Overview

This project provides a graphical user interface (GUI) implementation of Dijkstra's Algorithm to find the shortest path between nodes in a graph. The application is built using Java Swing and provides a visual representation of the graph, allowing users to interactively generate graphs, specify start and end nodes, and find the shortest path.

## Features

- **Graph Visualization**: Nodes and edges are visually represented on a panel, with node labels and edge weights displayed clearly.
- **Graph Generation**: Users can generate a random graph with a specified number of nodes.
- **Shortest Path Finding**: After specifying the start and end nodes, the shortest path is calculated and highlighted.
- **Reset Functionality**: The graph can be reset to its initial state after modifications.
- **Input Validation**: Ensures valid inputs for node numbers and graph generation parameters.

## Dijkstra's Algorithm

Dijkstra's Algorithm is a greedy algorithm used to find the shortest path from a starting node to all other nodes in a graph. The graph can have non-negative weights on its edges, and the algorithm efficiently calculates the minimum cost to reach each node.

### Pseudocode

```text
1. Initialize distances from the start node to all nodes as infinity, except the start node itself (distance 0).
2. Set the predecessor of each node to null.
3. Create a priority queue and add the start node with a distance of 0.
4. While the priority queue is not empty:
   a. Extract the node with the smallest distance (let's call it `current`).
   b. For each neighboring node of `current` (let's call it `neighbor`):
      i. Calculate the tentative distance to `neighbor` as the sum of the distance to `current` and the edge weight from `current` to `neighbor`.
      ii. If the tentative distance is smaller than the current known distance to `neighbor`, update the distance and set `current` as the predecessor of `neighbor`.
      iii. If the distance is updated, add `neighbor` to the priority queue with the updated distance.
5. Once the priority queue is empty, the shortest paths to all nodes have been determined.
6. To reconstruct the shortest path to any specific node, trace back from the node to the start node using the predecessors.
```

## How to Run

1. Ensure you have Java installed on your machine.
2. Compile the Java code using a command like:
   ```bash
   javac DijkstraShortestPathGUI.java
   ```
3. Run the compiled class:
   ```bash
   java DijkstraShortestPathGUI
   ```
4. The GUI will open, allowing you to generate graphs, specify start and end nodes, and visualize the shortest path.

## Project Structure

- **DijkstraShortestPathGUI.java**: The main class that handles the GUI and Dijkstra's algorithm logic.
- **Node**: Represents a node in the graph with an ID and position (x, y) on the panel.
- **NodeDistance**: A helper class used in the priority queue to keep track of node distances.

## Controls

- **Start Node**: Enter the ID of the start node.
- **End Node**: Enter the ID of the end node.
- **Find Shortest Path**: Calculates and highlights the shortest path.
- **Number of Nodes**: Specify the number of nodes for graph generation.
- **Generate Graph**: Generates a random graph with the specified number of nodes.
- **Reset**: Resets the graph to its initial state.

## Visualization

- **Nodes**: Represented as blue circles with labels.
- **Edges**: Black lines connecting nodes, with labels indicating weights.
- **Shortest Path**: Highlighted in blue after calculation.

## Future Improvements

- **Dynamic Edge Weight Adjustment**: Allow users to manually change edge weights.
- **More Algorithms**: Implement other pathfinding algorithms like A*.
- **Graph Customization**: Enable users to add or remove nodes and edges manually.


## Dijkstra's Algorithm

Dijkstra's algorithm is a greedy algorithm used to find the shortest path from a source node to all other nodes in a weighted graph. The algorithm works as follows:

### Pseudocode

1. **Initialization:**
   - Set the distance to the source node as `0` and to all other nodes as `∞`.
   - Mark all nodes as unvisited. Create a set of all unvisited nodes.

2. **Loop:**
   - While there are unvisited nodes:
     - Select the unvisited node with the smallest known distance (let's call it `currentNode`).
     - For each unvisited neighbor of the `currentNode`:
       - Calculate the distance from the source to the neighbor through the `currentNode`.
       - If this distance is smaller than the previously known distance to the neighbor, update the distance.
     - Mark the `currentNode` as visited (it will not be checked again).

3. **Termination:**
   - When all nodes have been visited, the algorithm ends. The smallest distance to each node is now known, and for any node, the path with the smallest distance can be reconstructed by tracing back from the node to the source node.

### Example

Given a graph with nodes connected by weighted edges, Dijkstra's algorithm finds the shortest path from a start node to all other nodes. The path is represented as a sequence of nodes, with the total distance being minimized.

## Code Skeleton

### 1. **Main Class (`DijkstraShortestPathGUI`)**

- **Purpose:**  
  This class is the main entry point for the application. It sets up the GUI and handles user interactions such as generating a graph, finding the shortest path, and resetting the graph.

- **Key Components:**
  - **Graph Representation:**
    - `Map<Integer, Node> nodes`: Stores the nodes with their positions.
    - `Map<Integer, Map<Integer, Integer>> graph`: Represents the adjacency list of the graph, where each node is connected to its neighbors with a certain weight.

  - **GUI Components:**
    - `JPanel graphPanel`: The panel where the graph and paths are drawn.
    - `JTextField numNodesField`: Input field to specify the number of nodes in the graph.
    - `JTextField startField, endField`: Input fields for specifying the start and end nodes.

  - **Buttons:**
    - `findPathButton`: Finds and draws the shortest path.
    - `generateGraphButton`: Generates a new random graph.
    - `resetButton`: Resets the graph to its initial state.

### 2. **Node Class (`Node`)**

- **Purpose:**  
  Represents a node in the graph. Each node has an ID and coordinates (x, y) for its position on the graph panel.

- **Key Components:**
  - `int id`: The unique identifier of the node.
  - `int x, y`: The coordinates of the node on the graph panel.

### 3. **NodeDistance Class (`NodeDistance`)**

- **Purpose:**  
  A helper class used in Dijkstra's algorithm to manage the distances of nodes in a priority queue. It stores a node ID and its current known shortest distance from the start node.

- **Key Components:**
  - `Integer node`: The node ID.
  - `int distance`: The current shortest known distance to this node.

### 4. **Dijkstra Algorithm Implementation (`findAndDrawShortestPath()`)**

- **Purpose:**  
  Implements Dijkstra's algorithm to find the shortest path from the start node to the end node. It updates the `predecessors` map to store the path and then triggers the drawing of the path on the GUI.

- **Key Steps:**
  - **Initialization:**  
    Initialize distances to all nodes as `∞` except for the start node, which is set to `0`. Initialize the priority queue with the start node.

  - **Main Loop:**  
    While the priority queue is not empty, extract the node with the smallest distance, update the distances of its neighbors, and update the queue.

  - **Path Construction:**  
    After processing, reconstruct the path from the end node back to the start node using the `predecessors` map.

### 5. **Graph and Path Drawing (`drawNodes()`, `drawEdges()`, `drawShortestPath()`)**

- **Purpose:**  
  These methods handle the graphical rendering of the nodes, edges, and the shortest path on the `graphPanel`. The nodes are drawn as circles with their IDs, edges as lines with weights, and the shortest path as a highlighted path.

- **Key Components:**
  - **`drawNodes(Graphics g, Map<Integer, Node> nodes, Color color)`**: Draws all nodes.
  - **`drawEdges(Graphics g, Map<Integer, Map<Integer, Integer>> graph, Color color)`**: Draws all edges with weights.
  - **`drawShortestPath(Graphics g, Integer startNode, Integer endNode)`**: Draws the shortest path using a thicker, colored line.

## How to Use

1. **Run the Application:**  
   Execute the `DijkstraShortestPathGUI` class. The GUI window will open.

2. **Generate a Graph:**  
   Enter the number of nodes you want in the graph and click the "Generate Graph" button. A random graph will be generated and displayed.

3. **Find Shortest Path:**  
   Enter the start and end nodes in the respective fields and click "Find Shortest Path". The shortest path will be highlighted on the graph.

4. **Reset the Graph:**  
   Click the "Reset" button to restore the graph to its initial state.

   ## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Conclusion

This project provides a visual and interactive way to understand Dijkstra's algorithm. The GUI allows users to experiment with different graph structures and observe how the algorithm finds the shortest path in real-time.

---
