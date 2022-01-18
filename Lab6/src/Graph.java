import java.util.*;

public class Graph {
    List<Integer> vertices;
    HashMap<Integer,List<Integer>> edges;


    public Graph(int numberVertices) {
        this.vertices = new ArrayList<>();
        this.edges = new HashMap<>();
        for(int i=1;i<=numberVertices;i++) {
            vertices.add(i);
            edges.put(i,new ArrayList<>());
        }
    }

    public void addEdge(int first,int second)
    {
        if(!edges.get(first).contains(second)) {
            edges.get(first).add(second);
        }
    }

    public void generateRandomGraph(int numberEdges)
    {
        Random random=new Random();
        int numberVertices=vertices.size();
        for(int i=1;i<=numberVertices;i++)
        {
            int nr=random.nextInt(numberEdges)+1;
            for(int j=0;j<nr;j++)
                addEdge(i,random.nextInt(numberVertices)+1);
        }
        List<Integer> verticesList = this.vertices;
        Collections.shuffle(verticesList);
        for (int i = 1; i <verticesList.size(); i++)
            addEdge(verticesList.get(i - 1),  verticesList.get(i));
        addEdge(verticesList.get(verticesList.size() -1), verticesList.get(0));
    }

    public List<Integer> getNeighbours(int vertex)
    {
        return this.edges.get(vertex);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i <vertices.size(); i++)
            s.append("vertex ").append(i + 1).append(": ").append(edges.get(i + 1)).append("\n");
        return s.toString();
    }
}
