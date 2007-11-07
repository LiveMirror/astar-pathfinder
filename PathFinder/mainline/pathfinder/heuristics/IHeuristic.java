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
 * General interface for Heuristic funtion
 * @author Egor Tsinko
 * @param <T> type of the point. Must extend java.awt.Point
 */
public interface IHeuristic<T extends Point> 
{
	/** 
	 * Calculates heuristic estimate of the minimal cost to reach the <b>goal</b>
	 * from <b>start</b> assuming that cost of move between adjacent points equals to 1.0
	 * which means it has to be scaled.
	 * @param start start point
	 * @param goal goal point
	 * @return calculated minimal cost to reach goal
	 */
	public float calculateHeuristic(T start, T goal);
}
