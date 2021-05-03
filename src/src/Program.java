package src;

/**
 *
 * @author jeelp
 */
public class Program {

    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            System.out.print("Missing Arguements! ex. search <filename> <searchStrategy>");
            System.exit(0);
        }
        String filename = args[0];

        ResourceInitializer ri = new ResourceInitializer();
        ri.Initialize(filename);

        ri.getGrid();

        String strategy = args[1];

        System.out.println(filename);
        System.out.println(strategy);

        switch (strategy.toLowerCase()) {
            case "bfs":
                BFS.BFSSearch(ri.getStartPos(), ri.getEndPos(), ri.getGrid(), ri.getRows(), ri.getColumns());
                break;
            case "dfs":
                DFS dfs = new DFS(ri.getGrid(), ri.getRows(), ri.getColumns(), ri.getStartPos(), ri.getEndPos());
                dfs.DFSSearch();
                break;
            case "astar":
                AStar.astar(ri.getGrid(), ri.getRows(), ri.getColumns(), ri.getStartPos(), ri.getEndPos());
                break;
            case "gbfs":
                GBFS.gbfs(ri.getGrid(), ri.getRows(), ri.getColumns(), ri.getStartPos(), ri.getEndPos());
                break;
            case "cus1":
                CUS1 cus = new CUS1(ri.getGrid(), ri.getRows(), ri.getColumns(), ri.getStartPos(), ri.getEndPos());
                cus.cusSearch();
                break;
            default: {
                System.out.print("Wrong strategy entered! Options: (bfs, dfs, astar, gbfs, cus1)");
                System.exit(0);
            }

        }
    }

}
