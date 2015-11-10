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
import java.util.Objects;
import java.util.TreeSet;
import org.cytoscape.model.CyNode;

class AlignmentNodeSetComparator implements Comparator<AlignmentNode> {

        @Override
        public int compare(AlignmentNode arg0, AlignmentNode arg1) {
                return arg0.getSUID().compareTo(arg1.getSUID());
        }
        
}

/**
 * A mutable set for alignment node
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class AlignmentNodeSet {
        TreeSet<AlignmentNode>  m_nodeset;
        
        public AlignmentNodeSet() {
                m_nodeset = new TreeSet<>(new AlignmentNodeSetComparator());
        }
        
        public AlignmentNodeSet(AlignmentNode[] nodes) {
                for (AlignmentNode n : nodes) {
                        m_nodeset.add(n);
                }
        }
        
        public List<AlignmentNode> to_alignment_node_list() {
                ArrayList<AlignmentNode> list = new ArrayList<>();
                for (AlignmentNode n : m_nodeset) {
                        list.add(n);
                }
                return list;
        }
        
        public AlignmentNode[] to_alignment_node_array() {
                return (AlignmentNode[]) m_nodeset.toArray();
        }
        
        public List<CyNode> to_cynode_list() {
                ArrayList<CyNode> list = new ArrayList<>();
                for (AlignmentNode n : m_nodeset) {
                        list.add(n);
                }
                return list;
        }
        
        public int add_node(AlignmentNode node) {
                if (m_nodeset.add(node)) {
                        return 1;
                } else {
                        return 0;
                }
        }
        
        public AlignmentNode find_node(AlignmentNode node) {
                for (AlignmentNode n : m_nodeset) {
                        if (node.equals(n)) {
                                return n;
                        }
                }
                return null;
        }
        
        public AlignmentNode find_node(String signature) {
                for (AlignmentNode n : m_nodeset) {
                        if (n.hashCode() == signature.hashCode() && n.get_signature().equals(signature)) {
                                return n;
                        }
                }
                return null;
        }
        
        public AlignmentNode find_node(Long suid) {
                for (AlignmentNode n : m_nodeset) {
                        if (Objects.equals(n.getSUID(), suid)) {
                                return n;
                        }
                }
                return null;
        }
        
        public boolean is_contain_node(AlignmentNode node) {
                return m_nodeset.contains(node);
        }
        
        public boolean is_contain_node(String signature) {
                return find_node(signature) != null;
        }

        public void clear() {
                m_nodeset.clear();
        }

        public int size() {
                return m_nodeset.size();
        }
}
