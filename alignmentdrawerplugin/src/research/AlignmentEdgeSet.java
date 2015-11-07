/* 
 * Copyright (C) 2015 Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package research;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import org.cytoscape.model.CyEdge;

class AlignmentEdgeSetComparator implements Comparator<AlignmentEdge> {

        @Override
        public int compare(AlignmentEdge arg0, AlignmentEdge arg1) {
                return arg0.getSUID().compareTo(arg1.getSUID());
        }
        
}

/**
 * A mutable set for alignment edge
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class AlignmentEdgeSet {
        TreeSet<AlignmentEdge>  m_edgeset;
        
        public AlignmentEdgeSet() {
                m_edgeset = new TreeSet<>(new AlignmentEdgeSetComparator());
        }
        
        public List<AlignmentEdge> to_alignment_edge_list() {
                ArrayList<AlignmentEdge> list = new ArrayList<>();
                for (AlignmentEdge e : m_edgeset) {
                        list.add(e);
                }
                return list;
        }
        
        public List<CyEdge> to_cyedge_list() {
                ArrayList<CyEdge> list = new ArrayList<>();
                for (AlignmentEdge e : m_edgeset) {
                        list.add(e);
                }
                return list;
        }
        
        public int add_edge(AlignmentEdge edge) {
                if (m_edgeset.add(edge)) {
                        return 1;
                } else {
                        return 0;
                }
        }
        
        public AlignmentEdge find_edge(AlignmentEdge edge) {
                for (AlignmentEdge e : m_edgeset) {
                        if (e.equals(edge)) {
                                return e;
                        }
                }
                return null;
        }
        
        public int size() {
                return m_edgeset.size();
        }
        
}
