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

import java.util.Objects;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

/**
 * Definition of our alignment node
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class AlignmentNode extends Object implements CyNode {
        String                          m_signature = null;
        int                             m_verifier = 0;
        
        Long                            m_suid;
        CyNetwork                       m_network = null;

        private AlignmentNodeSet        m_nodes_list = null;
        
        public AlignmentNode(String sig, AlignmentNetwork network, Long suid) {
                m_signature     = sig;
                m_verifier      = m_signature.hashCode();
                m_network       = network;
                m_suid          = suid;
                m_nodes_list    = new AlignmentNodeSet();
        }
        
        public int add_relation_to(AlignmentNode node) {
                return m_nodes_list.add_node(node);
        }
        
        public boolean is_related_to(AlignmentNode node) {
                return m_nodes_list.is_contain_node(node);
        }
        
        public AlignmentNodeSet get_neigbours() {
                return m_nodes_list;
        }
        
        public AlignmentEdgeSet construct_adjacent_edges() {
                AlignmentEdgeSet edges = new AlignmentEdgeSet();
                AlignmentNode[] nodes = m_nodes_list.to_alignment_node_array();
                for (AlignmentNode n : nodes) {
                        AlignmentEdge e = new AlignmentEdge(this, n, n.getSUID());
                        edges.add_edge(e);
                }
                return edges;
        }
        
        public String get_signature() {
                return m_signature;
        }
        
        /**
         * Two nodes are equal iff their suid is equal and they are in the same network
         * @param o AlignmentNode, only AlignmentNode is acceptable.
         * @return true if equal.
         */
        @Override
        public boolean equals(Object o) {
                if (o.getClass() != AlignmentNode.class) {
                        return false;
                }
                AlignmentNode node = (AlignmentNode) o;
                return Objects.equals(m_suid, node.m_suid) && m_network == node.m_network;
 
        }

        @Override
        public int hashCode() {
                return m_verifier;
        }
        
        @Override
        public CyNetwork getNetworkPointer() {
                return m_network;
        }

        @Override
        public void setNetworkPointer(CyNetwork cn) {
                m_network = cn;
        }

        @Override
        public Long getSUID() {
                return (long) m_suid;
        }
        
}
