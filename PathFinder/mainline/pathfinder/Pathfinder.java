/*
 * Copyright (c) 2007 Egor Tsinko
 * 
 * This file is part of A* Pathfinder.
 *
 * A* Pathfinder is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * A* Pathfinder is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with A* Pathfinder.  If not, see <http://www.gnu.org/licenses/>.
 */

package pathfinder;

import java.awt.Point;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import pathfinder.heuristics.DiagonalNotEqual;
import pathfinder.heuristics.IHeuristic;

//TODO implement fillRadius, a function that gets all accessible points in some radius.
/** 
 * Implementation of A* path finding algorithm for maps where coordinates are represented by
 * jawa.awt.Point
 * The implementation details and heuristic functions were taken from
 * <a href="http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html">
 * http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html</a>
 * 
 * @author Egor Tsinko
 * @version 1.0.0
 *
 */
public class Pathfinder 
{	
	/** reference to the helper class */
	private IPathHelper helper;
	
	/** total time it took algorithm to calculate the path */
	private long elapsedTime = 0;
	
	private IHeuristic heuristic = new DiagonalNotEqual();	

	private double scale = 1.0;

	private double tieBreaker = 1.0;

	private Point start;

	private Point goal;

	private Hashtable<Point,Node> closedNodes;

	private PriorityQueue<Node> openNodes;

	private int steps = 0;

	/**
	 * Inner node class, stores information for a priority queue.
	 * Total value of the node is <code>gVal + hVal<code> where:<br>
	 * <code>gVal</code> is a total cost of movements from <b>start</b> to this point<br>
	 * <code>hVal</code> is an estimate of cost of movements from this point to the <b>goal</b> 
	 */
	private class Node implements Comparable<Node>
	{
		private static final double EPSILON = 0.000001;

		Node prev = null;

		Point coord;

		double gVal = 0.0;

		double hVal = 0.0;

		Node(Point p) 
		{
			this.coord = p;
		}

		/**
		 * Compares the combined value of this node, to the other node.
		 * @param o - node to compare to
		 * @return 1 if (this.gVal+this.hVal)>(o.gVal+o.hVal)<br>
		 * 0 if (this.gVal+this.hVal)==(o.gVal+o.hVal)<br>
		 * -1 if (this.gVal+this.hVal)<(o.gVal+o.hVal)
		 * @see equals
		 */
		public int compareTo(Node o) 
		{
			double difference = this.gVal + this.hVal - o.gVal - o.hVal;
			difference = (Math.abs(difference) < EPSILON) ? 0.0f : difference;
			return (int) Math.signum(difference);
		}

		/**
		 * @return true if other nodes coordinates are the same as this node's coordinates
		 * @see compareTo
		 */
		public boolean equals(Object obj)
		{
			if (coord.equals(((Node)obj).coord))
			{
				return true;
			}
			return false;
		}

	}

	/**
	 * Sets new scale for results of the heuristics. This parameter is neccessary if average
	 * cost of travel between two points is not 1. The result of the heuristic function is multiplied
	 * by the scale. <br>
	 * The default value is 1.0
	 * @param scale new scale
	 */
	public void setScale(double scale) 
	{
		this.scale = scale;
	}

	/**
	 * Sets the heuristic function. The default is <code>DiagonalNotEqual</code>
	 * @param heuristic
	 */
	public void setHeuristic(IHeuristic heuristic) 
	{
		this.heuristic = heuristic;
	}

	/**
	 * This function back tracks the path from the <code>node</node> and creates the list of
	 * points which are conected and create the most optimal path from <b>start</b> to the <b>goal</b> 
	 * @param node end node that has to be backtracked
	 * @return list of the points
	 */
	private List<Point> backTrackPath(Node node) 
	{
		Node traversalNode = node;
		LinkedList<Point> path = new LinkedList<Point>();

		while (traversalNode != null) 
		{
			path.addFirst(traversalNode.coord);
			traversalNode = traversalNode.prev;
		}
		//Remove the starting point, we don't need it
		path.remove();
		
		return path;
	}

	/**
	 * Analysis function, must be called after <code>findPath</code><br>
	 * Gets the list of the points that have been added to the <b>closed</b> list
	 * @return list of closed points
	 */
	public List<Point> getVisitedPoints() 
	{
		if (closedNodes!=null)
		{
			List<Point> list = new LinkedList<Point>();
			Enumeration<Point> points = closedNodes.keys();
			while(points.hasMoreElements())
			{
				Point p = points.nextElement();
				list.add(p);
			}
			return list;
		}
		return null;
	}
	
	/**
	 * Analysis function, must be called after <code>findPath</code><br>
	 * Gets <b>g</b> value for the closed node with coordinates p, if there is no node with
	 * these coordinates in the list of closed nodes, 0 is returned
	 * @param p coordinates of the closed node
	 * @return g value
	 */
	public double getGVal (Point p)
	{
		if (closedNodes!=null)
		{
			Node n = closedNodes.get(p);
			if (n!=null)
			{
				return n.gVal;
			}
		}		
		return 0;
	}
	
	/**
	 * Analysis function, must be called after <code>findPath</code><br>
	 * Gets <b>h</b> value for the closed node with coordinates p, if there is no node with
	 * these coordinates in the list of closed nodes, 0 is returned
	 * @param p coordinates of the closed node
	 * @return h value
	 */
	public double getHVal (Point p)
	{
		if (closedNodes!=null)
		{
			Node n = closedNodes.get(p);
			if (n!=null)
			{
				return n.hVal;
			}
		}		
		return 0;
	}

	/**
	 * This function calculates tie breaker - a small scale for the heuristic which
	 * makes A* prefer to expand search closer to the goal.
	 * 
	 * @param start start point of the search
	 * @param goal goal point of the search
	 */
	private double calculateTieBreaker(Point start, Point goal) 
	{
		//Calculating expected maximum number of moves
		int maxNumberOfMoves = 4 * (Math.abs(start.x - goal.x) + Math
				.abs(start.y - goal.y));
		//Calculating tiebreaker multiplier
		return (1.0 + 1.0 / maxNumberOfMoves);
	}

	/**
	 * This function finds optimal path from <b>start</b> to <b>goal</b> with the <b>helper</b>
	 * @param helper
	 * @param start
	 * @param goal
	 * @return list of points that actor must sequentially walk through to reach goal or 
	 * <b>null</b> if path doesn't exist.
	 */
	public List<Point> findPath(IPathHelper helper, Point start, Point goal) 
	{
		closedNodes = new Hashtable<Point,Node>();
		this.helper = helper;
		long startTime= System.nanoTime();
		this.steps = 0;
		
		//If points are valid
		if (!start.equals(goal)) 
		{
			//Set up variables
			this.start = start;
			this.goal = goal;
			openNodes = new PriorityQueue<Node>();			 
			tieBreaker = calculateTieBreaker(start, goal);
			//Calculating path
			List<Point> path = calculatePath();
			
			elapsedTime = System.nanoTime() - startTime;
			
			return path;
		}
		else
		{
			elapsedTime = System.nanoTime() - startTime;
			// returning empty list
			return new LinkedList<Point>();
		}
	}

	/**
	 * This function calculates heuristic cost of travel between <b>start</b> and <b>goal</b> <br>
	 * The result is adjusted by <b>scale</b>
	 * @param start start point
	 * @param goal goal point
	 * @return calculated cost to travel from start to goal
	 */
	private double calculateHVal(Point start, Point goal) 
	{
		return scale * tieBreaker * heuristic.calculateHeuristic(start, goal);
	}

	/**
	 * This function calculates the path 
	 * @return path
	 */
	private List<Point> calculatePath() 
	{		
		List<Point> path = null;
		Node startNode = new Node(start);
		startNode.hVal = calculateHVal(start, goal);
		openNodes.add(startNode);

		//While there are nodes in the open list
		while (!openNodes.isEmpty()) 
		{

			this.steps++;
			//Get the node with the lowest gVal+hVal
			Node node = openNodes.poll();
			//Add it to the closed list
			closedNodes.put(node.coord,node);

			//If it is not the goal node
			if (!node.coord.equals(goal)) 
			{
				//Get all the neighbours
				List<Point> neighbours = helper.getNeighbours(node.coord);
				
				//For each neighbour
				for (Point p : neighbours) 
				{
					//Create neighbour node
					Node neighbourNode = createNeighbourNode(node, p);
					
					//If coordinates of the neighbour node are the same as the coordinates of the parent node, discard it
					if (!neighbourNode.coord.equals(node.coord)) 
					{
						//Check if the neighbour is in the list of open nodes
						boolean isInOpen = checkOpenNodes(neighbourNode);
						//If it is not in the open nodes and not in the closed nodes
						if ((!closedNodes.containsKey(neighbourNode.coord))&& (!isInOpen)) 
						{
							//Add it to the list of open nodes
							openNodes.add(neighbourNode);
						}
					}
				}
			} else 
			{
				// We found the path
				path = backTrackPath(node);
				break;				
			}
		}
		return path;
	}

	/**
	 * Creates neighbour node for the <b>parent</b> with coordinates <b>neighbourPos</b>
	 * @param parent parent node 
	 * @param neighbourPos position of the neighbour
	 * @return node
	 */
	private Node createNeighbourNode(Node parent, Point neighbourPos) 
	{
		Node neighbourNode = new Node(neighbourPos);
		neighbourNode.prev = parent;
		neighbourNode.gVal = parent.gVal + helper.getCost(parent.coord, neighbourNode.coord);
		neighbourNode.hVal = calculateHVal(neighbourNode.coord, goal);
		return neighbourNode;
	}

	/**
	 * Checks if there is a node with the same coordinates as the <b>node<b> in the list of open nodes.
	 * If there is, then it checks if its gValue is higher, and replaces it.
	 * @param node node
	 * @return true if node is already in the open list
	 */
	private boolean checkOpenNodes(Node node) 
	{
		boolean ret = false;
		for (Node oldNode : openNodes) 
		{
			if (oldNode.coord.equals(node.coord)) 
			{
				ret = true;
				if (oldNode.gVal >= node.gVal) 
				{
					//Since compareTo and equal compare different fields 
					//we cannot use remove() method from the PriorityQueue
					LinkedList<Node> list = new LinkedList<Node>();
					list.add(oldNode);
					openNodes.removeAll(list);
					openNodes.add(node);
				}
				break;
			}
		}
		return ret;
	}

	/**
	 * Analysis function, must be called after <code>findPath</code><br> 
	 * @return number of steps it took to find the optimal path
	 */
	public int getSteps() 
	{
		return steps;
	}

	/**
	 * Analysis function, must be called after <code>findPath</code><br>
	 * @return time of the last run in nanoseconds 
	 */
	public long getElapsedTime()
	{
		return elapsedTime;
	}
	
	/**
	 * Analysis function, must be called after <code>findPath</code><br>
	 * @return the tiebreaker multiplier used to adjust the result of the heuristic function 
	 */
	public double getTieBreaker()
	{
		return tieBreaker;
	}
}
