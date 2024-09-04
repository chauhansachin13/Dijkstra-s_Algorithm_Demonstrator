import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class DijkstraShortestPathGUI extends JFrame {
    private static final int NODE_RADIUS = 10;
    private static final int EDGE_THICKNESS = 2;
    private static final Color NODE_COLOR = Color.BLUE;
    private static final Color EDGE_COLOR = Color.BLACK;
    private static final Color PATH_COLOR = Color.BLUE; // Highlighted path color
    private static final int DEFAULT_NUM_NODES = 6;
    private Map<Integer, Node> nodes; // Changed from String to Integer
    private Map<Integer, Map<Integer, Integer>> graph; // Changed from String to Integer
    private Integer startNode;
    private Integer endNode;
    private Map<Integer, Integer> predecessors; // Changed from String to Integer
    private JPanel graphPanel;
    private JTextField numNodesField;

    // Variables to store initial graph state
    private Map<Integer, Node> initialNodes;
    private Map<Integer, Map<Integer, Integer>> initialGraph;

    public DijkstraShortestPathGUI() {
        nodes = new HashMap<>();
        graph = new HashMap<>();
        predecessors = new HashMap<>();
        initialNodes = new HashMap<>();
        initialGraph = new HashMap<>();
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Dijkstra's Algorithm - Shortest Path Finder");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel controlPanel = new JPanel();
        JButton findPathButton = new JButton("Find Shortest Path");
        JButton generateGraphButton = new JButton("Generate Graph");
        JButton resetButton = new JButton("Reset"); // Added Reset button
        JTextField startField = new JTextField(10);
        JTextField endField = new JTextField(10);
        numNodesField = new JTextField(5);
        numNodesField.setText(String.valueOf(DEFAULT_NUM_NODES)); // Default value

        findPathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    startNode = Integer.parseInt(startField.getText().trim());
                    endNode = Integer.parseInt(endField.getText().trim());
                    if (graph.containsKey(startNode) && graph.containsKey(endNode)) {
                        findAndDrawShortestPath();
                        graphPanel.repaint(); // Repaint the graph to show the path
                    } else {
                        JOptionPane.showMessageDialog(DijkstraShortestPathGUI.this,
                                "Invalid start or end node.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(DijkstraShortestPathGUI.this,
                            "Start and End Node must be valid integers.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        generateGraphButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int numNodes = Integer.parseInt(numNodesField.getText().trim());
                    if (numNodes < 2) {
                        JOptionPane.showMessageDialog(DijkstraShortestPathGUI.this,
                                "Number of nodes must be at least 2.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    generateLargeGraph(numNodes);
                    graphPanel.repaint();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(DijkstraShortestPathGUI.this,
                            "Invalid number format.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ActionListener for Reset button
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Restore the graph to its initial state
                nodes.clear();
                graph.clear();
                nodes.putAll(initialNodes);
                graph.putAll(initialGraph);
                predecessors.clear();
                startNode = null;
                endNode = null;
                graphPanel.repaint();
            }
        });

        controlPanel.add(new JLabel("Start Node:"));
        controlPanel.add(startField);
        controlPanel.add(new JLabel("End Node:"));
        controlPanel.add(endField);
        controlPanel.add(findPathButton);
        controlPanel.add(new JLabel("Number of Nodes:"));
        controlPanel.add(numNodesField);
        controlPanel.add(generateGraphButton);
        controlPanel.add(resetButton); // Added Reset button to control panel

        add(controlPanel, BorderLayout.NORTH);

        graphPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawEdges(g, graph, EDGE_COLOR);
                drawNodes(g, nodes, NODE_COLOR);
                if (startNode != null && endNode != null) {
                    drawShortestPath(g, startNode, endNode);
                }
            }
        };
        graphPanel.setPreferredSize(new Dimension(600, 600)); // Adjust size as needed
        add(graphPanel, BorderLayout.CENTER);

        // Generate default graph with 6 nodes
        generateLargeGraph(DEFAULT_NUM_NODES);
    }

    private void generateLargeGraph(int numNodes) {
        Random random = new Random();
        int panelWidth = graphPanel.getPreferredSize().width;
        int panelHeight = graphPanel.getPreferredSize().height;

        // Define margins from the edges of the panel
        int margin = 2 * NODE_RADIUS;
        int usableWidth = panelWidth - 2 * margin;
        int usableHeight = panelHeight - 2 * margin;

        // Center point of the panel, slightly shifted right
        int centerX = panelWidth / 2 + usableWidth / 8; // Adjust right shift here
        int centerY = panelHeight / 2;

        // Calculate radius to distribute nodes around the center
        int radius = Math.min(usableWidth, usableHeight) / 2 - NODE_RADIUS;

        // Position Node 0 specifically to the right
        int node0X = centerX + (radius + 50); // Position Node 0 further to the right
        int node0Y = centerY;

        addNode(0, node0X, node0Y);

        // Calculate angles for the remaining nodes
        double angleOffset = Math.PI / 6; // Offset to spread out nodes around Node 0

        for (int i = 1; i < numNodes; i++) {
            double angle = 2 * Math.PI * (i - 1) / (numNodes - 1) + angleOffset;
            int x = centerX + (int) (radius * Math.cos(angle));
            int y = centerY + (int) (radius * Math.sin(angle));
            addNode(i, x, y);
        }

        // Connect nodes with random edges
        for (int i = 0; i < numNodes; i++) {
            int fromNode = i;
            int numEdges = random.nextInt(5) + 1; // Each node will have 1 to 5 edges

            for (int j = 0; j < numEdges; j++) {
                int toNode = random.nextInt(numNodes);
                if (toNode != fromNode) { // Avoid self-loops
                    int weight = random.nextInt(100) + 1;
                    addEdge(fromNode, toNode, weight);
                }
            }
        }

        // Save the initial state of the graph
        initialNodes.clear();
        initialNodes.putAll(nodes);
        initialGraph.clear();
        for (Map.Entry<Integer, Map<Integer, Integer>> entry : graph.entrySet()) {
            initialGraph.put(entry.getKey(), new HashMap<>(entry.getValue()));
        }
    }

    private void addNode(int id, int x, int y) {
        nodes.put(id, new Node(id, x, y));
        graph.putIfAbsent(id, new HashMap<>());
    }

    private void addEdge(int from, int to, int weight) {
        graph.get(from).put(to, weight);
        graph.putIfAbsent(to, new HashMap<>());
    }

    private void findAndDrawShortestPath() {
        Map<Integer, Integer> distances = new HashMap<>();
        predecessors.clear();
        PriorityQueue<NodeDistance> queue = new PriorityQueue<>(Comparator.comparingInt(NodeDistance::getDistance));

        for (Integer node : nodes.keySet()) {
            distances.put(node, Integer.MAX_VALUE);
            predecessors.put(node, null);
        }

        distances.put(startNode, 0);
        queue.add(new NodeDistance(startNode, 0));

        while (!queue.isEmpty()) {
            NodeDistance current = queue.poll();
            Integer currentId = current.getNode();

            if (current.getDistance() > distances.get(currentId)) {
                continue;
            }

            for (Map.Entry<Integer, Integer> neighborEntry : graph.get(currentId).entrySet()) {
                Integer neighbor = neighborEntry.getKey();
                int weight = neighborEntry.getValue();
                int newDist = distances.get(currentId) + weight;

                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    predecessors.put(neighbor, currentId);
                    queue.add(new NodeDistance(neighbor, newDist));
                }
            }
        }
    }

    private void drawNodes(Graphics g, Map<Integer, Node> nodes, Color color) {
        g.setColor(color);
        g.setFont(new Font("Arial", Font.BOLD, 12)); // Set a larger and bold font

        for (Node node : nodes.values()) {
            // Draw the node as a filled circle
            g.fillOval(node.getX() - NODE_RADIUS, node.getY() - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);

            // Set the font color to black for visibility
            g.setColor(Color.BLACK);

            // Draw a small background rectangle for the text for better contrast
            String nodeName = String.valueOf(node.getId());
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(nodeName);
            int textHeight = fm.getHeight();

            g.setColor(Color.WHITE); // Background color for text
            g.fillRect(node.getX() - textWidth / 2 - 2, node.getY() - textHeight / 2, textWidth + 4, textHeight);

            // Draw the node name on top of the background rectangle
            g.setColor(Color.BLACK); // Text color
            g.drawString(nodeName, node.getX() - textWidth / 2, node.getY() + textHeight / 4);

            // Reset the color for the next node
            g.setColor(color);
        }
    }

    private void drawEdges(Graphics g, Map<Integer, Map<Integer, Integer>> graph, Color color) {
        g.setColor(color);
        g.setFont(new Font("Arial", Font.PLAIN, 12)); // Font for edge labels

        for (Integer from : graph.keySet()) {
            Node fromNode = nodes.get(from);
            for (Map.Entry<Integer, Integer> entry : graph.get(from).entrySet()) {
                Integer to = entry.getKey();
                Node toNode = nodes.get(to);
                g.drawLine(fromNode.getX(), fromNode.getY(), toNode.getX(), toNode.getY());

                // Calculate position for distance label
                int middleX = (fromNode.getX() + toNode.getX()) / 2;
                int middleY = (fromNode.getY() + toNode.getY()) / 2;
                String distance = String.valueOf(entry.getValue());

                // Draw background rectangle for better contrast
                FontMetrics fm = g.getFontMetrics();
                int textWidth = fm.stringWidth(distance);
                int textHeight = fm.getHeight();
                g.setColor(Color.WHITE); // Background color for text
                g.fillRect(middleX - textWidth / 2 - 2, middleY - textHeight / 2 - 2, textWidth + 4, textHeight + 4);

                // Draw the distance label
                g.setColor(Color.BLACK); // Text color
                g.drawString(distance, middleX - textWidth / 2, middleY + textHeight / 4);
            }
        }
    }

    private void drawShortestPath(Graphics g, Integer startNode, Integer endNode) {
        if (nodes.isEmpty() || !nodes.containsKey(startNode) || !nodes.containsKey(endNode)) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(PATH_COLOR);

        // Set the thickness of the path
        float strokeWidth = 4.0f; // Adjust the thickness as needed
        g2d.setStroke(new BasicStroke(strokeWidth));

        Stack<Integer> path = new Stack<>();
        for (Integer at = endNode; at != null; at = predecessors.get(at)) {
            path.push(at);
        }

        Node previousNode = null;
        while (!path.isEmpty()) {
            Integer nodeId = path.pop();
            Node currentNode = nodes.get(nodeId);
            if (previousNode != null) {
                g2d.setColor(PATH_COLOR);
                g2d.drawLine(previousNode.getX(), previousNode.getY(), currentNode.getX(), currentNode.getY());
            }
            previousNode = currentNode;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DijkstraShortestPathGUI frame = new DijkstraShortestPathGUI();
            frame.setVisible(true);
        });
    }

    private static class Node {
        private final int id;
        private final int x, y;

        public Node(int id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }

        public int getId() {
            return id;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    private static class NodeDistance {
        private final Integer node;
        private final int distance;

        public NodeDistance(Integer node, int distance) {
            this.node = node;
            this.distance = distance;
        }

        public Integer getNode() {
            return node;
        }

        public int getDistance() {
            return distance;
        }
    }
}
