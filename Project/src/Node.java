import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Node{
    int color;
    List<Integer> neighbours;

    public Node(int color, List<Integer> neighbours) {
        this.color = color;
        this.neighbours = neighbours;
    }

    public Node() {
        this.color = -1;
        this.neighbours = new ArrayList<>();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public List<Integer> getNeighbours() {
        return neighbours;
    }
}
