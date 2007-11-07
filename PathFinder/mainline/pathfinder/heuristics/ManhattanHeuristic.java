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

package pathfinder.heuristics;

import java.awt.Point;

/**
 * This heuristic function calculates minimal cost for maps that
 * <b>only</b> support horizontal and vertical movements (no diagonal movements).
 * It returns the sum of horizontal and vertical movements required to get from
 * the start point to the goal point.
 * @author Egor Tsinko
 * @param <T> type of the point. Must extend java.awt.Point
 */
public class ManhattanHeuristic<T extends Point> implements IHeuristic<T> 
{

	public float calculateHeuristic(T start, T goal) 
	{	
		return Math.abs(start.x-goal.x)+Math.abs(start.y-goal.y);
	}

}
