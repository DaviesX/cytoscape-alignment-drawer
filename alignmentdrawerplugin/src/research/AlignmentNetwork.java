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
import java.util.Collection;
import java.util.List;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyIdentifiable;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;

/**
 * Definition of an Alignment Network
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class AlignmentNetwork {
        private final String            c_NodeSignatureSlot = "AlignNodeSig";
        private final String            c_BelongingSlot = "AlignNodeBelonging";
        private final String            c_SUIDReferenceSlot = ".SUID";
        private CyNetwork               m_network = null;
        
        private boolean build_node_attributes(CyNetwork network) {
                boolean is_compatible = true;
                
                m_network = network;
                // Configure nodes
                CyTable node_table = m_network.getDefaultNodeTable();
                if (node_table.getColumn(c_NodeSignatureSlot) == null) {
                        is_compatible = false;
                        node_table.createColumn(c_NodeSignatureSlot, String.class, false);
                }
                if (node_table.getColumn(c_BelongingSlot) == null) {
                        is_compatible = false;
                        node_table.createListColumn(c_BelongingSlot, Long.class, false);
                }
                if (node_table.getColumn(c_SUIDReferenceSlot) == null) {
                        is_compatible = false;
                        node_table.createColumn(c_SUIDReferenceSlot, Long.class, false);
                }
                // Configure edges
                CyTable edge_table = m_network.getDefaultEdgeTable();
                if (edge_table.getColumn(c_BelongingSlot) == null) {
                        is_compatible = false;
                        edge_table.createListColumn(c_BelongingSlot, Long.class, false);
                }
                if (edge_table.getColumn(c_SUIDReferenceSlot) == null) {
                        is_compatible = false;
                        edge_table.createColumn(c_SUIDReferenceSlot, Long.class, false);
                }
                return is_compatible;
        }
        
        /**
         *  This will import the <CyNetwork> and absorb all the network data.
         * @param network network to be imported
         */
        AlignmentNetwork(CyNetwork network) throws Exception {
                if (build_node_attributes(network) != true) {
                        throw new Exception(this.getClass() + ": This network is not a compatible alignment network");
                }
        }
        
        /**
         * This will create a new <CyNetwork> with building using attributes for the data.
         * @param fact network factory used to create new network
         */
        AlignmentNetwork(CyNetworkFactory fact) {
                build_node_attributes(fact.createNetwork());
        }
        
        CyNetwork get_network() {
                return m_network;
        }
        
        public CyNode make_node(String signature) {
                CyNode node = m_network.addNode();
                CyRow attri = m_network.getRow(node);
                attri.set(c_NodeSignatureSlot, signature);
                attri.set(c_SUIDReferenceSlot, node.getSUID());
                return node;
        }
        
        private int add_object_belongings(CyIdentifiable object, CyNetwork network) {
                if (network == null) network = m_network;
                CyRow attri = m_network.getRow(object);
                List<Long> belonging_list = attri.getList(c_BelongingSlot, Long.class);

                if (belonging_list == null) belonging_list = new ArrayList<>();
                if (!belonging_list.contains(network.getSUID())) {
                        // add belonging if node hasn't yet belong to the network
                        belonging_list.add(network.getSUID());
                        attri.set(c_BelongingSlot, belonging_list);
                        return 1;
                } else {
                        return 0;
                }
        }
        
        public int add_node_belongings(CyNode node, CyNetwork network) {
                return add_object_belongings(node, network);
        }
        
        public void set_node_selected(CyNode node, Boolean is_selected) {
                CyRow attri = m_network.getRow(node);
                attri.set(CyNetwork.SELECTED, is_selected);
        }
        
        public CyNode get_node_from_signature(String signature) {
                CyTable table = m_network.getDefaultNodeTable();
                Collection<CyRow> rows = table.getMatchingRows(c_NodeSignatureSlot, signature);
//                List<CyRow> rows = table.getAllRows();
//                for (CyRow row : rows) {
//                        String sig = row.get(c_NodeSignatureSlot, String.class);
//                        if (sig.equals(signature)) {
//                                Long ref = row.get(c_SUIDReferenceSlot, Long.class);
//                                return m_network.getNode(ref);
//                        }
//                }
                CyRow row = rows.iterator().next();
                Long ref  = row.get(c_SUIDReferenceSlot, Long.class);
                return m_network.getNode(ref);
        }
        
        public CyEdge make_edge(CyNode node0, CyNode node1) {
                CyEdge edge = m_network.addEdge(node0, node1, true);
                m_network.getRow(edge).set(c_SUIDReferenceSlot, edge.getSUID());
                return edge;
        }
        
        public int add_edge_belongings(CyEdge edge, CyNetwork network) {
                return add_object_belongings(edge, network);
        }
}
