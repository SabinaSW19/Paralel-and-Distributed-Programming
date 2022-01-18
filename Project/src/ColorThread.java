import java.util.ArrayList;
import java.util.List;

public class ColorThread implements Runnable {
    private Graph graph;
    private int start;
    private int end;

    public ColorThread(Graph graph, int start, int end) {
        this.graph = graph;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            ArrayList<Integer> adjColorList = new ArrayList<>();
            Node node = graph.getNode(i);
            List<Integer> adjList = node.getNeighbours();

            for (int adjNode : adjList)
                adjColorList.add(graph.getNode(adjNode).getColor());

            int possibleColor = 0;
            while (adjColorList.contains(possibleColor))
                ++possibleColor;
            node.setColor(possibleColor);
        }

    }
}
