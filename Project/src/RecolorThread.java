import java.util.ArrayList;
import java.util.List;

public class RecolorThread implements Runnable {
    private Graph graph;
    private int start;
    private int end;
    private List<Integer> r;

    public RecolorThread(Graph graph, List<Integer> r, int start, int end) {
        this.graph = graph;
        this.start = start;
        this.end = end;
        this.r = r;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            ArrayList<Integer> adjColorList = new ArrayList<>();
            Node node = graph.getNode(i);
            List<Integer> adjList = node.getNeighbours();

            for (int adjNode : adjList) {
                if (graph.getNode(adjNode).getColor() == node.getColor())
                    //conflict, new list
                    r.add(Math.max(adjNode, i));
                adjColorList.add(graph.getNode(adjNode).getColor());
            }
        }

    }
}
