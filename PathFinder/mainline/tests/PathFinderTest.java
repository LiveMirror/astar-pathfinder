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

package tests;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import pathfinder.IPathHelper;
import pathfinder.Pathfinder;
import pathfinder.heuristics.DiagonalEqual;
import pathfinder.heuristics.DiagonalNotEqual;
import junit.framework.TestCase;

public class PathFinderTest extends TestCase 
{
	private class PathHelper implements IPathHelper<Point>
	{
		int[][]level;
		PathHelper(int[][]level)
		{
			this.level = level;			
		}
		public float getCost(Point sourcePoint, Point destPoint) 
		{
			return level[destPoint.x][destPoint.y];
		}

		public List<Point> getNeighbours(Point point) 
		{
			LinkedList<Point> points = new LinkedList<Point>();			
			for (int x = Math.max(0, point.x-1); x<=Math.min(level[0].length-1, point.x+1); x++)
			{
				for (int y = Math.max(0, point.y-1); y<=Math.min(level.length-1, point.y+1); y++)
				{
					if (level[x][y]!=NP)
					{
						if ((point.x!=x)||(point.y!=y))
							points.add(new Point(x,y));
					}
				}			
			}		
			return points;
		}
	}
	private static final int NP = 99;
	Pathfinder<Point> finder;
//						-----------------------> y
	static int[][] h1 = {{ 1, 1, 1,NP, 1, 1, 1},	// |
						 {NP,NP, 1,NP, 1,NP, 1},	// |
						 {NP, 1, 1,NP, 1,NP, 1},	// | x
						 { 1,NP,NP,NP, 1,NP, 1},	// |
						 { 1,NP, 1, 1,NP,NP, 1},	// |
						 { 1,NP, 1,NP,NP, 1,NP},	// v
						 { 1, 1, 1,NP, 1, 1, 1}};

	static int[][] h2 = {{ 1, 1, 1, 1, 1, 6, 6},
						 { 1, 1, 1, 1, 6, 6, 1},
						 { 1, 1, 5, 5, 6, 1, 1},
						 { 1, 1, 5, 5, 5, 1, 1},
						 { 1, 6, 5, 5, 1, 1, 1},
						 { 1, 6, 6, 1, 1, 1, 1},
						 { 6, 6, 1, 1, 1, 1, 1}};

	static int[][] h3 = {{ 1, 1, 1, 1, 1, 6,NP},
						 { 1, 1, 1, 1, 6,NP,NP},
						 { 1, 1, 5, 5,NP,NP, 1},
						 { 1, 1, 5,NP,NP, 1, 1},
						 { 1, 6,NP,NP, 1, 1, 1},
						 { 1,NP,NP, 1, 1, 1, 1},
						 {NP,NP, 1, 1, 1, 1, 1}};
		
	static int[][] h4 = {{ 1, 1, 1, 1},
						 { 1, 1, 1, 1},
						 { 1, 1, 1, 1},
						 { 1, 1, 1, 1}}; 						

	static int[][] h5 = {{ 1, 1,NP},
						 { 1,NP,NP},
						 {NP,NP, 1}};
	
	static int[][] h6 = {{ 0, 9, 9},
		 				 { 9, 1, 9},
		 				 { 9, 9, 2}};
	public void setUp()
	{
		finder = new Pathfinder<Point>();		
	}

	private void printPath(int[][] map,List<Point> path)
	{
		for (int i = 0; i<map.length; i++)
		{
			for (int j=0; j<map[0].length;j++)
			{
				if (path.contains(new Point(i,j)))					
					System.out.print('x');
				else
					if (map[i][j]==NP)
					System.out.print('#');
					else
						System.out.print(map[i][j]);
			}
			System.out.println();				
		}
		System.out.println("---------------");
		
	}
	
	public void testFindPathInMaze() 
	{
		finder.setHeuristic(new DiagonalNotEqual<Point>());
		List<Point> points = finder.findPath(new PathHelper(h1), new Point(0,0), new Point(6,6));
		
		assertTrue(points.size()==19);
		assertTrue(points.get(0).equals(new Point(0,1)));
		assertTrue(points.get(1).equals(new Point(1,2)));
		assertTrue(points.get(2).equals(new Point(2,1)));
		assertTrue(points.get(3).equals(new Point(3,0)));
		assertTrue(points.get(4).equals(new Point(4,0)));
		assertTrue(points.get(5).equals(new Point(5,0)));
		assertTrue(points.get(6).equals(new Point(6,1)));
		assertTrue(points.get(7).equals(new Point(5,2)));
		assertTrue(points.get(8).equals(new Point(4,3)));
		assertTrue(points.get(9).equals(new Point(3,4)));
		assertTrue(points.get(10).equals(new Point(2,4)));
		assertTrue(points.get(11).equals(new Point(1,4)));
		assertTrue(points.get(12).equals(new Point(0,5)));
		assertTrue(points.get(13).equals(new Point(1,6)));
		assertTrue(points.get(14).equals(new Point(2,6)));
		assertTrue(points.get(15).equals(new Point(3,6)));
		assertTrue(points.get(16).equals(new Point(4,6)));
		assertTrue(points.get(17).equals(new Point(5,5)));
		assertTrue(points.get(18).equals(new Point(6,6)));		
		printPath(h1, points);
	}
	
	public void testFindPathDifferentCost() 
	{
		finder.setHeuristic(new DiagonalNotEqual<Point>());
		List<Point> points = finder.findPath(new PathHelper(h2), new Point(0,0), new Point(6,6));
		assertTrue(points.size()==8);
		
		//Check that algorithm crosses expensive part in the best place
		assertTrue(points.get(2).equals(new Point(3,1)));
		assertTrue(points.get(3).equals(new Point(4,2)));
		assertTrue(points.get(4).equals(new Point(5,3)));
		
		assertTrue(points.get(7).equals(new Point(6,6)));
		printPath(h2, points);	
	}
	
	public void testFailedSearch1()
	{
		finder.setHeuristic(new DiagonalNotEqual<Point>());
		List<Point> points = finder.findPath(new PathHelper(h3), new Point(0,0), new Point(6,6));
		assertTrue(points==null);
		//See that finder tried all possible tiles
		assertTrue(finder.getSteps()==21);
		assertTrue(finder.getVisitedPoints().size()==21);
		
	}

	public void testGetSteps() 
	{
		finder.setHeuristic(new DiagonalNotEqual<Point>());
		List<Point> points = finder.findPath(new PathHelper(h4), new Point(0,0), new Point(3,3));
		assertTrue(finder.getSteps()==4);
		assertTrue(points.size()==3);		
		assertTrue(points.get(0).equals(new Point(1,1)));
		assertTrue(points.get(1).equals(new Point(2,2)));
		assertTrue(points.get(2).equals(new Point(3,3)));
		printPath(h4, points);
	}
	
	public void testFailedSearch2()
	{
		finder.setHeuristic(new DiagonalNotEqual<Point>());
		List<Point> points = finder.findPath(new PathHelper(h5), new Point(0,0), new Point(2,2));
		assertTrue(points==null);
		//See that finder tried all possible tiles
		assertTrue(finder.getSteps()==3);
		assertTrue(finder.getVisitedPoints().size()==3);
	}

	
	public void testGetGVal()
	{
		finder.setHeuristic(new DiagonalNotEqual<Point>());
		finder.findPath(new PathHelper(h6), new Point(0,0), new Point(2,2));
		
		List<Point> closed = finder.getVisitedPoints();
		assertTrue(closed.size()!=0);
		
		double gVal = finder.getGVal(new Point(1,1)); 
		assertTrue(gVal==h6[1][1]);
		gVal = finder.getGVal(new Point(2,2));
		assertTrue(gVal==(h6[2][2]+h6[1][1]));		
	}
	
	public void testGetHVal()
	{
		finder.setHeuristic(new DiagonalEqual<Point>());
		finder.findPath(new PathHelper(h6), new Point(0,0), new Point(2,2));
		
		double breaker = finder.getTieBreaker();
		
		List<Point> closed = finder.getVisitedPoints();
		assertTrue(closed.size()!=0);
		
		double hVal = finder.getHVal(new Point(0,0)); 
		assertTrue(hVal==2.0*breaker);				
		hVal = finder.getHVal(new Point(1,1)); 
		assertTrue(hVal==1.0*breaker);
		hVal = finder.getHVal(new Point(2,2)); 
		assertTrue(hVal==0.0);
	}
}
