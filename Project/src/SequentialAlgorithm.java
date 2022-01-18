import java.util.ArrayList;
import java.util.List;

public class SequentialAlgorithm {
    public static void colorVertices(Graph graph) {
        for (int i = 0; i < graph.getNrNodes(); i++) {
            ArrayList<Integer> adjColorList = new ArrayList<>();
            Node node = graph.getNode(i);
            List<Integer> adjList = node.getNeighbours();

            for (int adjNode : adjList) {
                adjColorList.add(graph.getNode(adjNode).getColor());
            }

            int possibleColor = 0;
            while (adjColorList.contains(possibleColor)) {
                possibleColor++;
            }
            node.setColor(possibleColor);
        }
    }
}
