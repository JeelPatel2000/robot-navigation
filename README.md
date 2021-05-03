# Robot Navigation
Jeel Patel - 102220664
Solution for Robot Navigation Problem for COS30019 (Assignment 1)

## Introduction

This problem is for the Robot Navigation. In this problem an environment of N x M where both N, M are > 1 with several walls occupying some cells (grey cells also known as walls). The robot is initially at the Red Node (StartNode), it needs to find the path to the Destination node (Green Node). The data required to initialize the grid is given in a text file. 
In this project, I have implemented several search algorithms (informed and uninformed) to find the path.

## Instructions 
To run the program, navigate to the directory where search.exe file is location.
Then open command prompt from that directory.
#### Input 
```bash
Type the command: search.exe <path_to_file> <search_strategy> 
```
Search strategies: 
* BFS
* DFS
* ASTAR
* GBFS
* CUS1

#### Output 
Example Output
```
TestInput.txt 

ASTAR 23 
right down right right right right
```

## Search Algorithms
This program implements the following Search Algorithms:

* Depth-first Search: Depth-first search is an algorithm for traversing or searching tree or graph data structures. The algorithm starts at the root node and explores as far as possible along each branch before backtracking.
The time complexity of  `DFS`  if the entire tree is traversed is  O(V)  where  V   is the number of nodes. In the case of a graph, the time complexity is  O(V+E)  where  _V_  is the number of vertexes and  _E_  is the number of edges. 
Depth First Search does not yield the shortest path. So it is inefficient.

* Breadth-first Search: Breadth-first search is an algorithm for traversing or searching tree or graph data structures. It starts at the tree root, and explores all of the neighbor nodes at the present depth prior to moving on to the nodes at the next depth level.
The time complexity of `BFS` if the entire tree is traversed is O(V) where _V_ is the number of nodes. In the case of a graph, the time complexity is O(V+E) where _V_ is the number of vertices and _E_ is the number of edges.
Best First search is better algorithm than Depth First Search as it guarantees the shortest path in a tree.


* Greedy Best-first: Greedy best-first search algorithm always selects the path which appears best at that moment. It uses only the heuristic cost to reach the goal from the current node to evaluate the node. In the best first search algorithm, we expand the node which is closest to the goal node and the closest cost is estimated by heuristic function. GBFS does not guarantee a shortest path.

* A* ("A Star"): A algorithm is a searching algorithm that searches for the shortest path between the _initial and the final state._ Uses both the cost to reach the goal from the current node and the cost to reach this node to evaluate the node.
**g :** the cost of moving from the initial cell to the current cell.
**h :** also known as the _heuristic value,_ it is the **estimated** cost of moving from the current cell to the final cell.
**f :**  Sum of g and h. So,  **f = g + h**
 
  It makes decisions by taking the f-value into account. The algorithm selects the  _smallest f-valued cell_  and moves to that cell. This process continues until the algorithm reaches its goal cell.
  
  **A Star** guarantees a shortest path to destination. And also it is very much efficient as it uses heuristic to find where to go next. 

* Custom Search Strategy 1: An uninformed method to find a path to reach the goal.



## Implementation
The implementation of this program includes search algorithms, using console app and GUI.

### Domain Classes used
#### Point
This is the most fundament class of my program. It is use to store the coordinates. It has two properties, x and y. It also has getters and setters methods.
```java
class Point{
	int x,y;
	void getX();
	void getY();
	void setX();
	void setY();
}
```
#### Cell
This class inherits from Point class, and add few more properties to it. Cell class is used to represent Cell id, isWall, parentNodeId, gCost, hCost properties.  
```java
class Cell extends Point{
	int id, parentNodeId, gCost, hCost;
	boolean isWall;
}
```

### Other Variables and Classes

#### Grid
 
The primary environment is stored in an object called Grid, the term grid was chosen over map due to the design of the environment been based on a grid of cells connected in four directions, North, West, South and East.

This implementation can simply adapted into a traditional adjacency list through simple alterations to the class.

The grid is 2d Array of `Cell`, given rows and columns. Grid can be compared with the map given in the project. 


#### ResourceInitializer
ResourceInitializer is used to generate the environment, in this case the File Path is all that is provided to the Initialize function.
The file needs to be formatted correctly for reading, and consists of N lines, at least 3.
Lines 0 - 3 contain crucial information for producing the environment. Descriptions and formatting of the file is as follows:
```java
[w,h]               // Width and height of the enviroment
(x,y)               // Starting location of the agent
(x1,y1) | (x2,y2)   // Goal states for the agent - coordinates are 
// seperated by the character |
(x,y,w,h)           // Refernce to a rectangular block of walls the agent 
//cannot pass through, the walls leftmost top corner occupies cell(x,y)
// and is w cells wide, and h cess tall
...
...
```
#### Un-Informed Algorithms
In this program three un-informed Algorithms have been implemented. 
* Depth-first Search 
* Breadth-first Search
* Custom un-informed algorithm based on the sorting method known as "pogo-sort"

##### Depth-first Search (DFS)
The depth-first search algorithm is built using recursion - tree builder, requiring one overload function to fit within a standard call format. initially the DFS function is called and a reference to the grid provided along with startNode, destinationNodes. The function generates a hashtable to store the Id of visited nodes, and calls the retracePath method to process the recessive pattern of the algorithm, this works on the following pseudo-code

```java
function dfs(currentNode, endNodes)
  if(currentNode == endNodes)
	found = true;
    return true;
    
  foreach(valid connection)
    if(!visited)
	  validNode.parentNodeId = currentNode.parentNodeId 
      dfs(validNode, endNodes);
      
  goback();
```

##### Breadth-first Search (BFS)

Here are the steps for the  `BFS`  algorithm:

-   Pick a node and enqueue all its adjacent nodes into a queue.
    
-   Dequeue a node from the queue, mark it as visited and enqueue all its adjacent nodes into a queue.
    
-   Repeat this process until the queue is empty or you meet a goal.

The program can be stuck in an infinite loop if a node is revisited and was not marked as  `visited`  before. Hence, prevent exploring nodes that are visited by marking them as visited.
The algorithm is designed around the following pseudo-code

```java
  do
  {
    foreach(valid connection)
      enqueue.(path to valid connection);
    
    move to origin node;
    move to next node in queue;
    
    if(currenNode.atDestinationNode())
      return path;
      
  } while (!queue.empty())
```

##### Custom Un-informed Search Algorithm (CUS1)
The custom un-informed search algorithm is based on the array sorting method known as pogo-sort, it is a ineffective search algorithm. The design of the algorithm uses an agent lifespan and is based on the following pseudo-code

```java
while(Agent.isAlive())
{
  Move in a random direction();
  currentNode = random unvisited neighbour
  if(currentNode.atGoal())
      return path;
}
```
In this implementation the life of the agent is limited at (no. of rows x no. of columns) movements.

This method is very inefficient because there is no guarantee that,  agent will reach the destination node. It might even stuck inside a loop, where all its neighbouring nodes have been visited. 

#### Informed Algorithms
Two informed Algorithms have been defined, Greedy Best-first Search, A* (A Star).

##### Distance Cost and Heuristic Cost Estimate
The greedy best-first search and A* search algorithms require a extra step for cost calculation method to work correctly. Calculating the cost based on the positioning of a to and from node on the grid.

```java
int HeuristicCostEstimate(Grid::Cell & aFrom, Grid::Cell & aTo);
int CalculateDistance(Coordinate & aFrom, Coordinate & aTo);
```

Both functions (A* and GBFS) use a well known distance method known as Manhattan Distance, the distance required to travel along the x and y paths.
```
p1 at (x1, y1) and p2 at (x2, y2), D=|x1 - x2| + |y1 - y2|.
```
Along with Manhattan distance there is another measurement method i.e. Euclidian Distance.

the final heuristic cost estimate used is the sum of path cost and manhattan distance.

##### Greedy Best-first Search (GBFS)

The Greedy Best first Search also uses Manhattan distance to calculate heuristic, but unlike A star algorithm, it does not takes cost to reach the destination node (g cost) into consideration. So this algorithm might lead to a path which is not the shortest.
Hence, GBFS is not very efficient.

The implementation of GBFS is same as of A star.

##### A* ("AStar") (AS)
The A* search algorithm uses both the cost to travel from the starting point to the node, as well as the cost to travel from the node to the goal to assess the node to determine the best path. the function is built on the following pseudo-code

```java
initalise a grid to store the fScores and gScores for nodes, as well as a 
grid to contain direction of the best node to travel to this node.
the gScore of moving from stat to start is 0

initalise openlist containing starting node.

while(!openList.empty())
{
  get next node from open list with lowest fScore;
  
  if(node is at goal)
    construct path;
    
  move current node to closedList;
  
  foreach(validNeighbour)
  {
    if(validNeighbour is in closedList)
      continue;

	calculate new_fScore = gCost + hCost
	//if nighbour in not openListIf OR better path to currentNode 
	//found record it
    if(validNeighbour not in openList OR new_fScore < old_fScore)
    {
	  node.setGCost = new_fScore
	  node.setHCost = ManhattanDistance(node, destinationNode)
      if(validDirection isnt in openList)
        add validDirection to openList;
    }  
    
  }
}
```

## Features/Bugs/Missing
### Notable Features
Visualizer: Visualizer helps to visualize the current state of the search algorithm. In other words, it helps up to visualize how the algorithm expands the graph tree. Upon reaching the destination node, it also animates the path.

Search Algorithms
* Breadth First Search
* Depth First Search
* Greedy Best First Search
* A star
* Custom uninformed search

### Bugs & Missing Features
Bugs - No bugs in the program.

Missing Features
* Program does not include CUS2 (Informed search)


## Research
####  GUI visualizer
I have implemented the GUI visualizer to visualize the search algorithm. It helps us to understand how an algorithm expands the nodes in a graph. 
##### Implementation 
I have created a separate class to deal with all graphics. The class takes, grid, visited nodes, startNode, endNodes, path as the parameter.
```java
class Visualizer{
	Cell[][] grid;
	Cell startNode;
	List<Cell> endNodes;
	Hashtable<Integer, Boolean> visitedNodes;
	List<Cell> path;
}
```
I have used the ``java.awt`` to draw the grid. 
##### Challenges faced
As java is single threaded by default, my program was not able to visualize the grid in real time, as the algorithm would finish first, then the visualizer would execute. 
To overcome this problem, I used multithreading, to run two simultaneous threads, one for visualizing and second for running the algorithm.   


## Conclusion
For similar cases, I would like to use A* algorithm, as it is very much efficient and quick.
As it yields the shortest path to the destination.
Performance could be improved by improving overestimation-function (heuristic) for the cost of the remaining path to reduce the number of explored nodes.

## Acknowledgements/Resources

I would like to thank my convenor A/Prof Bao Vo, who gave me this opportunity to work on this amazing project. It helped me a lot, I got to learn new techniques in AI. 

Apart from that, Breadth-first search and Depth-first search algorithms were taught in the Data Structures and Patterns unit, the algorithms were developed based on knowledge learned in that unit.

The custom algorithms were developed as an experiment into effective uses of a agent lifespan (CUS1).

I would also acknowledge the great resources available on internet. (Youtube)

## References


Educative: Interactive Courses for Software Developers. 2021. What is Breadth First Search?. [ONLINE] Available at: [https://www.educative.io/edpresso/what-is-breadth-first-search](https://www.educative.io/edpresso/what-is-breadth-first-search). [Accessed 20 April 2021].

GitHub. 2021. Robonav2019/Robonav2019 at master · JeelPatel2000/Robonav2019 · GitHub. [ONLINE] Available at: [https://github.com/JeelPatel2000/Robonav2019/tree/master/Robonav2019](https://github.com/JeelPatel2000/Robonav2019/tree/master/Robonav2019). [Accessed 21 April 2021].

Educative: Interactive Courses for Software Developers. 2021. What is the A* algorithm?. [ONLINE] Available at:  [https://www.educative.io/edpresso/what-is-the-a-star-algorithm](https://www.educative.io/edpresso/what-is-the-a-star-algorithm). [Accessed 21 April 2021].

Educative: Interactive Courses for Software Developers. 2021. What is Depth First Search?. [ONLINE] Available at:  [https://www.educative.io/edpresso/what-is-depth-first-search](https://www.educative.io/edpresso/what-is-depth-first-search). [Accessed 21 April 2021].

www.javatpoint.com. 2021. Informed Search Algorithms in AI - Javatpoint. [ONLINE] Available at:  [https://www.javatpoint.com/ai-informed-search-algorithms](https://www.javatpoint.com/ai-informed-search-algorithms). [Accessed 23 April 2021].



