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

/**
 * 
 */
package pathfinder.heuristics;

import java.awt.Point;

/**
 * This heuristic function calculates minimal cost for maps that don't contain
 * square tiles, but instead support movement in any direction. It calculates 
 * the length of the straight line between <b>start</b> and <b>end</b>
 * @author Egor Tsinko
 */
public class EuclidianHeuristic implements IHeuristic 
{

	/* (non-Javadoc)
	 * @see pathfinder.heuristics.IHeuristic#calculateHeuristic(java.awt.Point, java.awt.Point)
	 */
	public float calculateHeuristic(Point start, Point goal) 
	{
		return (float)Math.sqrt(Math.pow(start.x-goal.x, 2)+Math.pow(start.y-goal.y, 2));
	}

}
