package com.scheng;

import java.util.*;

public class Graph {

    List<Node> nodes = new ArrayList<Node>();


    public void addNode(Node node){
        nodes.add(node);
    }

    public void addNodes(List<Node> nodes){
        this.nodes.addAll(nodes);
    }



    public void fromGrid(String input, int dim, boolean allowTransitive){
        int[][] grid = new int[dim][dim];
        int row = 0, col =0;
        for(String line : input.split("\\n")){
            col = 0;
            //System.out.println(line);
            for(String d : line.split("\\s+")){
                int v = Integer.valueOf(d.trim(), 10);
                grid[row][col++]= v;
            }

            row++;
        }



        Node[][] nodes = new Node[dim][dim];

        row = 0;
        col = 0;
        for(int[] r : grid){

            col =0;
            for(int c : r){
                //System.out.print(c + "\t");
                Node node = new Node(c);
                this.addNode(node);
                nodes[row][col++] = node;


            }

            row++;

            //System.out.println();
        }




        row = 0;
        col = 0;
        for(Node[] r : nodes){
            col =0;
            for(Node n : r){
                System.out.print(n.name + "\t");
                if(row -1 >=0){
                    Node other = nodes[row-1][col];
                    if(other.name > n.name){
                        n.addEdge(other);
                    }else if(allowTransitive && other.name == n.name){
                        n.addEdge(other);
                    }

                    if(col - 1 >=0) {
                        other = nodes[row-1][col-1];
                        if(other.name > n.name){
                            n.addEdge(other);
                        }else if(allowTransitive && other.name == n.name){
                            n.addEdge(other);
                        }
                    }

                    if(col +1 < dim) {
                        other = nodes[row-1][col+1];
                        if(other.name > n.name){
                            n.addEdge(other);
                        }else if(allowTransitive && other.name == n.name){
                            n.addEdge(other);
                        }
                    }

                }

                if(row + 1 < dim){
                    Node other = nodes[row+1][col];
                    if(other.name > n.name){
                        n.addEdge(other);
                    }else if(allowTransitive && other.name == n.name){
                        n.addEdge(other);
                    }

                    if(col - 1 >=0) {
                        other = nodes[row+1][col-1];
                        if(other.name > n.name){
                            n.addEdge(other);
                        }else if(allowTransitive && other.name == n.name){
                            n.addEdge(other);
                        }
                    }

                    if(col +1 < dim) {
                        other = nodes[row+1][col+1];
                        if(other.name > n.name){
                            n.addEdge(other);
                        }else if(allowTransitive && other.name == n.name){
                            n.addEdge(other);
                        }
                    }
                }

                if(col - 1 >=0) {
                    Node other = nodes[row][col-1];
                    if(other.name > n.name){
                        n.addEdge(other);
                    }else if(allowTransitive && other.name == n.name){
                        n.addEdge(other);
                    }

                }

                if(col + 1 < dim){
                    Node other = nodes[row][col+1];
                    if(other.name > n.name){
                        n.addEdge(other);
                    }else if(allowTransitive && other.name == n.name){
                        n.addEdge(other);
                    }


                }
                col++;

            }
            System.out.println();
            row++;

        }
    }

    List<Node> getSorted(){
        List<Node> sorted = this.topologicalSort();

        for(Node node : sorted){
            if(node.getInComingEdges().size() == 0){
                continue;
            }
            int longest = 0;
            Node from = null;
            List<Node> previous = new ArrayList<Node>();
            for(Edge edge : node.getInComingEdges()){
                Node other = edge.from;
                if(other.longesetPath > longest){
                    longest = other.longesetPath;
                    from = other;
                    previous.clear();
                    previous.add(other);
                }else if(other.longesetPath == longest){
                    longest = other.longesetPath;
                    from = other;
                    previous.add(other);
                }
            }
            node.longesetPath = from.longesetPath + 1;
            node.path.addAll(from.path);
            node.path.add(from);
            node.previousNodes.addAll(previous);
        }

        return sorted;
    }

    public void printLongestPath(){


        System.out.println("...............................");

        for(Node node : getSorted()){
            node.printPath();
        }
    }

    public void printAllPossibleLongestPath(){


        System.out.println("...............................");

        for(Node node : getSorted()){
            System.out.println(node);
            Node.PrintAllPossiblePath(node, new ArrayList<Node>());
        }
    }


    public void printAllPossibleLongestPathBruteForce(){


        System.out.println("...............................");

        for(Node node : this.nodes){
            System.out.println(node);
            Node.getAllPossiblePathBruteForce(node, new ArrayList<Node>(), node);
            node.printAllPossiblePath();
        }
    }

    static class Path {
        public List<Node>  path = new ArrayList<Node>();
        public int size;
    }


    static class Node{

        public List<Node> path = new ArrayList<Node>();
        public final int name;
        public final HashSet<Edge> inEdges;
        public final HashSet<Edge> outEdges;
        private final HashSet<Edge> _inEdges;
        public int longesetPath = 0;

        public List<Path> allPossiblePath = new ArrayList<Path>();

        List<Node> previousNodes = new ArrayList<Node>();


        public void printPath(){
            for(Node node : path){
                System.out.print(node.name + "=>");
            }
            System.out.println(name);

        }

        public void printAllPossiblePath(){
            Collections.sort(allPossiblePath, new Comparator<Path>(){
                public int compare(Path a1, Path a2) {
                    return a2.path.size() - a1.path.size(); // assumes you want biggest to smallest
                }
            });

            for(Path path : allPossiblePath) {

                for(Node node : path.path){
                    System.out.print(node.name + "=>");
                }
                System.out.println();
            }
        }


        public static void PrintAllPossiblePath(Node node,List<Node> nodelist)
        {
            if(node != null)
            {
                nodelist.add(node);

                if(node.previousNodes.size() == 0){
                    for(int i=0;i<nodelist.size();i++)
                    {
                        System.out.print(nodelist.get(i).name + "=>");
                    }
                    System.out.println();
                }else{
                    for(Node previous : node.previousNodes){
                        PrintAllPossiblePath(previous,nodelist);
                    }
                }
                nodelist.remove(node);
            }
        }


        public static void PrintAllPossiblePathViaIncomingEdges(Node node,List<Node> nodelist)
        {
            if(node != null)
            {
                nodelist.add(node);

                if(node.inEdges.size() == 0){
                    for(int i=0;i<nodelist.size();i++)
                    {
                        System.out.print(nodelist.get(i).name + "=>");
                    }
                    System.out.println();
                }else{
                    for(Edge edge : node.inEdges){
                        Node previous = edge.from;
                        PrintAllPossiblePathViaIncomingEdges(previous, nodelist);
                    }
                }
                nodelist.remove(node);
            }
        }


        public static boolean hasCycle(ArrayList<Node> path, Node source){
            //if(path.size() == 1) return false;
            for(Node node : path){
                if(node == source){
                    return true;
                }
            }
            return false;
        }

        public static void getAllPossiblePathBruteForce(Node node,ArrayList<Node> nodelist, Node source)
        {
            if(node != null)
            {
                if(hasCycle(nodelist, node)){
                    Path path = new Path();

                    for(int i=0;i<nodelist.size();i++)
                    {
                        //System.out.print(nodelist.get(i).name + "=>");
                        path.path.add(nodelist.get(i));
                    }
                    //System.out.println();


                    source.allPossiblePath.add(path);

                    return;
                }

                // cycle detection
                nodelist.add(node);

                if(node.inEdges.size() == 0){

                    // find a possible path
                    Path path = new Path();

                   for(int i=0;i<nodelist.size();i++)
                    {
                        //System.out.print(nodelist.get(i).name + "=>");
                        path.path.add(nodelist.get(i));
                    }
                    //System.out.println();


                    source.allPossiblePath.add(path);
                }else{
                    for(Edge edge : node.inEdges){
                        Node previous = edge.from;
                        getAllPossiblePathBruteForce(previous,nodelist, source);
                    }
                }
                nodelist.remove(node);
            }
        }



        public HashSet<Edge> getInComingEdges(){
            return _inEdges;
        }
        public Node(int name) {
            this.name = name;
            inEdges = new HashSet<Edge>();
            outEdges = new HashSet<Edge>();
            _inEdges = new HashSet<Edge>();
        }
        public Node addEdge(Node node){
            Edge e = new Edge(this, node);
            outEdges.add(e);
            node.inEdges.add(e);
            node._inEdges.add(e);
            return this;
        }
        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("\n"+name + " incoming edges: ");
            for(Edge edge : this._inEdges){
                sb.append(edge).append("\t");
            }
            sb.append("\n");
            return sb.toString();
        }
    }

    static class Edge{
        public final Node from;
        public final Node to;
        public Edge(Node from, Node to) {
            this.from = from;
            this.to = to;
        }
/*        @Override
        public boolean equals(Object obj) {
            Edge e = (Edge)obj;
            return e.from == from && e.to == to;
        }*/

        @Override
        public String toString(){
            return from.name + "=>" + to.name;
        }
    }

    public List<Node> topologicalSort(){
        List<Node> sorted = new ArrayList<Node>();
        //S <- Set of all nodes with no incoming edges
        HashSet<Node> S = new HashSet<Node>();
        for(Node n : this.nodes){
            if(n.inEdges.size() == 0){
                S.add(n);
            }
        }

        //while S is non-empty do
        while(!S.isEmpty()){
            //remove a node n from S
            Node n = S.iterator().next();
            S.remove(n);

            //insert n into sorted
            sorted.add(n);

            //for each node m with an edge e from n to m do
            for(Iterator<Edge> it = n.outEdges.iterator();it.hasNext();){
                //remove edge e from the graph
                Edge e = it.next();
                Node m = e.to;
                it.remove();//Remove edge from n
                m.inEdges.remove(e);//Remove edge from m

                //if m has no other incoming edges then insert m into S
                if(m.inEdges.isEmpty()){
                    S.add(m);
                }
            }
        }
        //Check to see if all edges are removed
        boolean cycle = false;
        for(Node n : this.nodes){
            if(!n.inEdges.isEmpty()){
                cycle = true;
                break;
            }
        }
        if(cycle){
            System.out.println("Cycle present, topological sort not possible");
        }else{
            System.out.println("Topological Sort: "+Arrays.toString(sorted.toArray()));
        }

        return sorted;
    }

    public boolean hasCycle(){
        return false;
    }

    public static void main(String[] args) {
        Node seven = new Node(7);
        Node five = new Node(5);
        Node three = new Node(3);
        Node eleven = new Node(11);
        Node eight = new Node(8);
        Node two = new Node(2);
        Node nine = new Node(9);
        Node ten = new Node(10);
        seven.addEdge(eleven).addEdge(eight);
        five.addEdge(eleven);
        three.addEdge(eight).addEdge(ten);
        eleven.addEdge(two).addEdge(nine).addEdge(ten);
        eight.addEdge(nine).addEdge(ten);

        Node[] allNodes = {seven, five, three, eleven, eight, two, nine, ten};

        Graph graph = new Graph();
        graph.addNodes(Arrays.asList(allNodes));
        graph.topologicalSort();

    }
}