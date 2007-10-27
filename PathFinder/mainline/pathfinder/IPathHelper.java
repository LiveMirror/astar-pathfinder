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
import java.util.List;

/**
 * Interface for helper functions. The user of the library has to implement these functions.
 * The helper may also contain information about the level and the actor that initiated 
 * search.
 * @author Egor Tsinko
 */
public interface IPathHelper 
{
	/**
	 * Calculates cost of movement from point <b>sourcePoint</b> to point <b>destPoint</b>
	 * @param sourcePoint source point for calculating cost
	 * @param destPoint destination point
	 * @return the cost 
	 */
	public float getCost(Point sourcePoint, Point destPoint);	
	
	/**
	 * Gets the list of valid points that are possible to travel to from <b>point</b>
	 * @param point point to get neighbours for
	 * @return list of neighbour points
	 */
	public List<Point> getNeighbours(Point point);
}
