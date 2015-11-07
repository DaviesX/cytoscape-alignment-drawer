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

import java.util.Collection;
import java.util.List;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyIdentifiable;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.SavePolicy;


    
/**
 *  Definition of an AlignmentNetwork
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class AlignmentNetwork implements CyNetwork {
        private AlignmentNodeSet        m_node_set = null;
        private AlignmentEdgeSet        m_edge_set = null;
        private long                    m_suid = 0;
        
        public AlignmentNetwork() {
                m_node_set = new AlignmentNodeSet();
                m_edge_set = new AlignmentEdgeSet();
        }
        
        public long alloc_network_suid() {
                return ++ m_suid;
        }
        
        public AlignmentEdge make_edge(AlignmentNode node0, AlignmentNode node1) {
                AlignmentNode n0 = m_node_set.find_node(node0);
                if (n0 == null) {
                        m_node_set.add_node(node0);
                        n0 = node0;
                }
                AlignmentNode n1 = m_node_set.find_node(node1);
                if (n1 == null) {
                        m_node_set.add_node(node1);
                        n1 = node1;
                }
                if (!n0.equals(n1)) {
                        // never allow cyclical network
                        n0.add_relation_to(n1);
                        n1.add_relation_to(n0);
                        AlignmentEdge existing = m_edge_set.find_edge(new AlignmentEdge(n0, n1));
                        if (existing == null) {
                                existing = new AlignmentEdge(n0, n1, alloc_network_suid());
                                m_edge_set.add_edge(existing);
                        }
                        return existing;
                }
                return null;
        }

        @Override
        public CyNode addNode() {
                return new AlignmentNode("", this, alloc_network_suid());
        }

        @Override
        public boolean removeNodes(Collection<CyNode> clctn) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public CyEdge addEdge(CyNode cynode, CyNode cynode1, boolean bln) {
                if (cynode.getClass() != AlignmentNode.class ||
                    cynode1.getClass() != AlignmentNode.class) {
                        return null;
                }
                return make_edge((AlignmentNode) cynode, (AlignmentNode) cynode1);
        }

        @Override
        public boolean removeEdges(Collection<CyEdge> clctn) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getNodeCount() {
                return m_node_set.size();
        }

        @Override
        public int getEdgeCount() {
                return m_edge_set.size();
        }

        @Override
        public List<CyNode> getNodeList() {
                return m_node_set.to_cynode_list();
        }

        @Override
        public List<CyEdge> getEdgeList() {
                return m_edge_set.to_cyedge_list();
        }

        @Override
        public boolean containsNode(CyNode cynode) {
                if (cynode.getClass() != AlignmentNode.class) {
                        return false;
                }
                return m_node_set.is_contain_node((AlignmentNode) cynode);
        }

        @Override
        public boolean containsEdge(CyEdge cyedge) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean containsEdge(CyNode cynode, CyNode cynode1) {
                if (cynode.getClass() != AlignmentNode.class || cynode1.getClass() != AlignmentNode.class) {
                        return false;
                }
                AlignmentNode n0 = m_node_set.find_node((AlignmentNode) cynode);
                if (n0 != null) {
                        return n0.is_related_to((AlignmentNode) cynode1);
                } else {
                        return false;
                }
        }

        @Override
        public CyNode getNode(long l) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public CyEdge getEdge(long l) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public List<CyNode> getNeighborList(CyNode cynode, CyEdge.Type type) {
                if (cynode.getClass() != AlignmentNode.class) {
                        return null;
                }
                AlignmentNode node = (AlignmentNode) cynode;
                return node.get_neigbours().to_cynode_list();
        }

        @Override
        public List<CyEdge> getAdjacentEdgeList(CyNode cynode, CyEdge.Type type) {
                if (cynode.getClass() != AlignmentNode.class) {
                        return null;
                }
                AlignmentNode node = (AlignmentNode) cynode;
                return node.construct_adjacent_edges().to_cyedge_list();
        }

        @Override
        public Iterable<CyEdge> getAdjacentEdgeIterable(CyNode cynode, CyEdge.Type type) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public List<CyEdge> getConnectingEdgeList(CyNode cynode, CyNode cynode1, CyEdge.Type type) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public CyTable getDefaultNetworkTable() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public CyTable getDefaultNodeTable() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public CyTable getDefaultEdgeTable() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public CyTable getTable(Class<? extends CyIdentifiable> type, String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public CyRow getRow(CyIdentifiable ci, String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public CyRow getRow(CyIdentifiable ci) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public SavePolicy getSavePolicy() {
                return SavePolicy.SESSION_FILE;
        }

        @Override
        public Long getSUID() {
                return (long) hashCode();
        }

        @Override
        public void dispose() {
                m_node_set.clear();
        }
}
