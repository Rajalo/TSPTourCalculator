/**
 *  This file is part of the TSP Tour Calculator
    Copyright (C) 2021  Reilly Browne

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The purpose of this class is to implement the branch and bound method for
 * finding the optimal TSP tour
 */

public class TSP {
    /**
     * a node in the binary tree used for branching
     */
    static class TSPNode {
        double lowerbound;
        double[][] matrix;
        ArrayList<int[]> edges;
        ArrayList<int[]> graph;
        TSPNode left;
        TSPNode right;
        TSPNode parent;

        /**
         * Constructs a node in the binary tree used for branching
         * @param parent
         * @param lowerbound
         * @param matrix
         * @param edges
         * @param graph
         */
        public TSPNode(TSPNode parent, double lowerbound, double[][] matrix, ArrayList<int[]> edges, ArrayList<int[]> graph)
        {
            this.lowerbound = lowerbound + reduce(matrix);
            this.matrix = matrix;
            this.edges = edges;
            this.graph = graph;
            left = null;
            right = null;
            this.parent = parent;
        }

        /**
         * Adds in an edge to the component its a part of. Checks for cycles.
         * @param edge coordinates of edge being added
         * @return the current instance of TSPNode
         */
        public TSPNode appendEdge(int[] edge)
        {
            edges.add(edge);
            int[] start = {edge[0]};
            int[] end = {edge[1]};
            for (int i = 0; i < graph.size(); i++)
            {
                int[] path = graph.get(i);
                boolean remove = false;
                if (path[0]==edge[1])
                {
                    end = path;
                    remove = true;
                }
                if (path[path.length-1]==edge[0])
                {
                    start = path;
                    remove = true;
                }
                if (path[0] == edge[0])
                {
                    start = new int[path.length];
                    for (int j = 0; j < path.length;j++)
                    {
                        start[j] = path[path.length-j-1];
                    }
                    remove = true;
                }
                if (path[path.length-1] == edge[1])
                {
                    end = new int[path.length];
                    for (int j = 0; j < path.length;j++)
                    {
                        end[j] = path[path.length-j-1];
                    }
                    remove = true;
                }
                if (remove) {
                    graph.remove(path);
                    i--;
                }
            }
            if (Arrays.equals(start,end)&&(edges.size()< matrix.length||graph.size()>2))
            {
                lowerbound = Integer.MAX_VALUE;
            }
            int[] newPath = new int[start.length+ end.length];
            System.arraycopy(start, 0, newPath, 0, start.length);
            System.arraycopy(end, 0, newPath, start.length, end.length);
            graph.add(newPath);
            if (matrix.length> edges.size()) {
                for (int[] path1 : graph) {
                    if (path1[0] == path1[path1.length - 1]) {
                        lowerbound = Integer.MAX_VALUE;
                        break;
                    }
                    for (int[] path2 : graph) {
                        if (path1[0] == path2[path2.length - 1] && path2[0] == path1[path1.length - 1]) {
                            lowerbound = Integer.MAX_VALUE;
                            break;
                        }
                    }
                }
            }
            return this;
        }

        /**
         * Returns a string representation of the TSPNode
         * @return a string representation of the TSPNode
         */
        @Override
        public String toString() {
            return "TSPNode{" +
                    "lowerbound=" + lowerbound +
                    '}';
        }
    }

    /**
     * A tree that stores all our decisions on branching
     */
    static class TSPTree {
        TSPNode root;

        /**
         * A tree that stores all our decisions on branching
         * @param matrix adjacency matrix used in root TSPNode
         */
        public TSPTree(double[][] matrix)
        {
            root = new TSPNode(null,0,matrix,new ArrayList<int[]>(),new ArrayList<int[]>());
        }
        /**
         * Executes the branch and bound technique starting from root.
         * @return the TSPNode of the optimal TSP tour
         */
        public TSPNode branchAndBound()
        {
            TSPNode nodePtr = root;
            while (nodePtr.edges.size() < root.matrix.length) {
                lowerBoundUpdate(root);
                nodePtr = findMinimumLeaf(root);
                nodePtr = branch(nodePtr);
            }
            nodePtr = findMinimumLeaf(root);
            return nodePtr;
        }
        /**
         * Takes a node and finds optimal edge for branching and creates 2 child nodes
         * @param node the node being branched
         * @return the node being branched to
         */
        public TSPNode branch(TSPNode node)
        {
            int[] edge = optimalEdge(node.matrix);
            double[][] lmatrix = deepCopy(node.matrix);
            double[][] rmatrix = deepCopy(node.matrix);
            rmatrix[edge[0]][edge[1]] = -1;
            lmatrix[edge[1]][edge[0]] = -1;
            removeEdge(edge, lmatrix);
            ArrayList<int[]> ledges = new ArrayList<>(node.edges);
            ArrayList<int[]> redges = new ArrayList<>(node.edges);
            ArrayList<int[]> lgraph = new ArrayList<>(node.graph);
            ArrayList<int[]> rgraph = new ArrayList<>(node.graph);
            node.left = new TSPNode(node, node.lowerbound, lmatrix,ledges,lgraph);
            node.left.appendEdge(edge);
            node.right = new TSPNode(node, node.lowerbound, rmatrix,redges,rgraph);
            return node.left;
        }
        /**
         * Finds the lowest bound in tree so we’re always working with the optimal set of edges
         * @param node the root node to start from
         * @return the leaf node with the lowest lowerbound
         */
        public TSPNode findMinimumLeaf(TSPNode node)
        {
            if (node.left==null&&node.right==null)
            {
                return node;
            }
            else if (node.left != null && node.left.lowerbound== node.lowerbound)
            {
                return findMinimumLeaf(node.left);
            }
            else if (node.right != null && node.right.lowerbound== node.lowerbound)
            {
                return findMinimumLeaf(node.right);
            }
            return node;
        }
        /**
         * Updates up the tree the smallest lowerbound achievable descending from the given node
         * @param node the root node to start from
         */
        public void lowerBoundUpdate(TSPNode node)
        {
            double left, right;
            left = right = node.lowerbound;
            if (node.left!=null)
            {
                lowerBoundUpdate(node.left);
                left = node.left.lowerbound;
            }
            if (node.right != null)
            {
                lowerBoundUpdate(node.right);
                right = node.right.lowerbound;
            }
            node.lowerbound = Math.min(left, right);
        }
    }
    /**
     * Given a reduced adjacency matrix, finds the optimal edge for branching
     * @param matrix the reduced adjacency matrix
     * @return the optimal edge for branching
     */
    public static int[] optimalEdge(double[][] matrix)
    {
        int[] edge = {0,0};
        double max = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (i==j||matrix[i][j] != 0)
                    continue;
                double localminRow = Integer.MAX_VALUE;
                for (int k = 0; k < matrix[0].length; k++) {
                    if (k == j || matrix[i][k] <= -1)
                        continue;
                    localminRow = Math.min(localminRow, matrix[i][k]);
                }
                double localminCol = Integer.MAX_VALUE;
                for (int k = 0; k < matrix.length; k++) {
                    if (k == i || matrix[k][j] <= -1)
                        continue;
                    localminCol = Math.min(localminCol, matrix[k][j]);
                }
                if (max <= localminCol + localminRow) {
                    max = localminCol + localminRow;
                    edge[0] = i;
                    edge[1] = j;
                }
            }
        }
        return edge;
    }
    /**
     * Simulates deleting a row and column from the matrix by setting them to -2
     * @param edge pair of vertices whose edge is being removed
     * @param matrix the adjacency matrix
     */
    public static void removeEdge(int[] edge, double[][] matrix)
    {
        for (int i = 0; i < matrix.length; i++)
        {
            matrix[i][edge[1]]=-2;
        }
        for (int i = 0; i < matrix[0].length; i++)
        {
            matrix[edge[0]][i]=-2;
        }
    }
    /**
     * Creates a deep copy of a 2D array
     * @param matrix the 2D array
     * @return deep copy of the array
     */
    public static double[][] deepCopy(double[][] matrix)
    {
        double[][] clone = new double[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length;i++)
        {
            System.arraycopy(matrix[i], 0, clone[i], 0, matrix[0].length);
        }
        return clone;
    }
    /**
     * Creates the adjacency matrix of the complete graph of points in 2D Euclidean space
     * @param vertices 2D array of the coordinates of the vertices
     * @return the adjacency matrix of the complete graph of points in 2D Euclidean space
     */
    public static double[][] adjacency(int[][] vertices)
    {
        int length = vertices.length;
        double[][] matrix = new double[length][length];
        for (int i = 0; i< length; i++)
        {
            for (int j = 0; j < length; j++)
            {
                if (i == j) {
                    matrix[i][j] = -1;
                    continue;
                }
                matrix[i][j] = Math.sqrt(Math.pow(vertices[i][0]-vertices[j][0],2)+Math.pow(vertices[i][1]-vertices[j][1],2));
            }
        }
        return matrix;
    }
    /**
     * Reduces a matrix (as done in branch and bound) and gives a lowerbound for the TSP based on this reduction
     * @param matrix the matrix being reduced
     * @return the amount the matrix was reduced by, basically the lowerbound.
     */
    public static double reduce(double[][] matrix)
    {
        double lowerbound = 0;
        for (int i = 0; i < matrix.length; i++)
        {
            double min = matrix[i][0];
            for (int j = 1; j < matrix[0].length; j++) {
                if (matrix[i][j]<=-1)
                    continue;
                min = (min > matrix[i][j]||min<0) ? matrix[i][j] : min;
            }
            for (int j = 0; j< matrix[0].length;j++)
            {
                if (matrix[i][j]<=-1)
                    continue;
                matrix[i][j] -= min;
            }
            lowerbound += (min<=-1)?0:min;
        }
        for (int i = 0; i < matrix[0].length; i++)
        {
            double min = matrix[0][i];
            for (double[] doubles : matrix) {
                if (doubles[i] <= -1)
                    continue;
                min = (min > doubles[i] || min < 0) ? doubles[i] : min;
            }
            for (int j = 0; j< matrix.length;j++)
            {
                if (matrix[j][i]<=-1)
                    continue;
                matrix[j][i]-= min;
            }
            lowerbound += (min<=-1)?0:min;
        }
        return lowerbound;
    }
    /**
     * Executes branch and bound on the adjacency matrix of the given set of vertices
     * @param vertices the vertices being analyzed
     * @return the TSPNode containing the TSP tour.
     */
    public static TSPNode branchAndBoundOfVertices(int[][] vertices)
    {
        return (new TSPTree(adjacency(vertices))).branchAndBound();
    }
}