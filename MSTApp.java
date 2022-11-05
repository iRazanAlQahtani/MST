package mst;
import java.util.*;

class Edge {

    String startVertex;
    String endVertex;

    int value;
}
class MinHeap {

    Map<String, Integer> vertexVal;
    String[] verticesKeyArray;
    MinHeap(Map<String, Integer> vertexVal){
        this.vertexVal = vertexVal;
    }
    public void heapify(String[] verticesArray,int root, int length) {

        int left = (2*root)+1;
        int right = (2*root)+2;
        int smallest = root;

        if (left < length && right <= length && 
        		vertexVal.get(verticesArray[right]) < vertexVal.get(verticesArray[left])) {
            smallest = right;
        }
        else if (left <= length){

            smallest = left;
        }
        if (vertexVal.get(verticesArray[root]) > vertexVal.get(verticesArray[smallest])) {
            String temp = verticesArray[root];
            verticesArray[root] = verticesArray[smallest];
            verticesArray[smallest] = temp;
            heapify(verticesArray, smallest, length);
        }
    }
    public void buildHeap() {

        Set<String> verticesSet = vertexVal.keySet();

        // Now convert the above keys to an Array.
        String[] verticesArray = new String[verticesSet.size()];
        verticesSet.toArray(verticesArray);

        int len = verticesArray.length-1;

        for (int parent = (len-1)/ 2; parent >= 0; parent--)
            heapify(verticesArray, parent, len);

        verticesKeyArray = verticesArray;

    }

    public void updateHeap(String vertex, int length) {

        vertexVal.put(vertex, length);

        // Get all the keys (i.e. Vertices ) for the Map.
        Set<String> verticesSet = vertexVal.keySet();

        // Now convert the above keys to an Array.
        String[] verticesArray = new String[verticesSet.size()];
        verticesSet.toArray(verticesArray);

        int len = verticesArray.length-1;

        for (int parent = (len-1)/ 2; parent >= 0; parent--)
            heapify(verticesArray, parent, len);

        verticesKeyArray = verticesArray;
    }

    boolean containsVertex(String vertex){

        if (vertexVal.containsKey(vertex))
            return true;
        else
            return false;
    }

    public String deleteMin() {

        String temp = verticesKeyArray[0];

        int len = verticesKeyArray.length-1;

        verticesKeyArray[0] = verticesKeyArray[len];

        vertexVal.remove(temp);

        verticesKeyArray = Arrays.copyOf(verticesKeyArray, len);

        if (len>0)
            heapify(verticesKeyArray, 0, len-1);

        return temp;
    }

    int getWeight(String vertex){

        return vertexVal.get(vertex);
    }

    public boolean empty() {

        if (verticesKeyArray.length>0)
            return false;
        else
            return true;

    }
}



public class MSTApp {

    LinkedList<String> vertices;
    LinkedList<LinkedList<Edge>>  adjcList;
    Map<String,Integer> vertexVal;

    // Stores the Minimum spanning Tree
    List<Edge> result;

    MSTApp(){

        adjcList = new LinkedList<>();
        vertices = new LinkedList<>();
        vertexVal = new LinkedHashMap<>();

        // Stores the Minimum spanning Tree
        result = new ArrayList<>();
    }

    public void primMST(){

        vertexVal = new LinkedHashMap<>();

        // Vertex to Edge Map
        Map<String, Edge> vertexToEdge = new HashMap<>();

        // Assign all the initial values as infinity for all the Vertices.
        for(String v : vertices) {
            vertexVal.put(v,Integer.MAX_VALUE);
        }

        MinHeap minHeap = new MinHeap(vertexVal);

        // Call buildHeap() to create the MinHeap
        minHeap.buildHeap();

        // Replace the value of start vertex to 0.
        minHeap.updateHeap("a",0);

        // Continue until the Min-Heap is not empty.
        while(!minHeap.empty()){
            // Extract minimum value vertex from Map in Heap
            String currentVertex = minHeap.deleteMin();

            // Need to get the edge for the vertex and add it to the Minimum Spanning Tree..
            // Just note, the edge for the source vertex will not be added.
            Edge spanningTreeEdge = vertexToEdge.get(currentVertex);
            if(spanningTreeEdge != null) {
                result.add(spanningTreeEdge);
            }

            // Get all the adjacent vertices and iterate through them.
            for(Edge edge : getEdges(currentVertex)){
            
                String adjacentVertex = edge.endVertex;
                
                // We check if adjacent vertex exist in 'Map in Heap' and length of the edge is with this vertex
                // is greater than this edge length.
                if(minHeap.containsVertex(adjacentVertex) && minHeap.getWeight(adjacentVertex) > edge.value){
                
                    // Replace the edge length with this edge weight.
                    minHeap.updateHeap(adjacentVertex, edge.value);
                    
                    vertexToEdge.put(adjacentVertex, edge);
                }
            }
        }
    }

    List<Edge> getEdges(String vertex){

        List<Edge> edgeList = new LinkedList<>();

        int i = vertices.indexOf(vertex);

        for (Iterator iter = adjcList.get(i).iterator() ; iter.hasNext(); ) {

            edgeList.add((Edge) iter.next());
        }

        return edgeList;
    }

    void constructAdjacencyList(String vertex1, String vertex2, int edgeVal) {

        Edge edge = new Edge();

        edge.startVertex = vertex1;
        edge.endVertex = vertex2;
        edge.value = edgeVal;

        adjcList.add(new LinkedList<Edge>());
        adjcList.get(vertices.indexOf(vertex1)).add(edge);
    }

    void insertVertex(String vertex) {

        vertices.add(vertex);
    }



    void printEdgeList() {

        for (Edge edge : result) {

            System.out.println(""+edge.startVertex+" - "+edge.endVertex+" is "+edge.value );
        }
    }

    public static void main(String[] args) {

        MSTApp primMST = new MSTApp();

        // Insert Vertices
        primMST.insertVertex("a");
        primMST.insertVertex("b");
        primMST.insertVertex("c");
        primMST.insertVertex("d");
        

        // Create Adjacency List with Edges.
        primMST.constructAdjacencyList("a", "b",2);
        primMST.constructAdjacencyList("a", "c",4);	

        primMST.constructAdjacencyList("b", "a",2);
        primMST.constructAdjacencyList("b" ,"c",6);
        primMST.constructAdjacencyList("b" ,"d",3);

        primMST.constructAdjacencyList("c", "a",4);
        primMST.constructAdjacencyList("c", "b",6);
        primMST.constructAdjacencyList("c", "d",1);


        primMST.constructAdjacencyList("d", "c", 1);
        primMST.constructAdjacencyList("d", "b", 3);


        primMST.primMST();


        primMST.printEdgeList();
    }
}