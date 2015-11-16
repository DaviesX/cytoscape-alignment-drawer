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
import java.util.HashMap;
import java.util.List;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyTableFactory;

/**
 * Aligning networks from given data
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class NetworkAligner {
        private CyTable                 m_table = null;         // This table stores majority of the alignment data
        private final String            c_PrimaryKeySlot = "AlignPrimaryKey";
        private final String            c_NodesSignatureSlot = "AlignNodesSig";
        private final String            c_DataTitle = "AlignmentData";
        private Long                    m_size;
        
        private void build_attributes(CyTable table) {
                m_table = table;
                m_size = new Long(0);
                if (null == m_table.getColumn(c_PrimaryKeySlot)) {
                        m_table.createColumn(c_PrimaryKeySlot, Long.class, true);
                }
                if (null == m_table.getColumn(c_NodesSignatureSlot)) {
                        m_table.createListColumn(c_NodesSignatureSlot, String.class, true);
                }
        }
        
        public NetworkAligner(CyTableFactory fact) {
                build_attributes(fact.createTable(c_DataTitle, c_PrimaryKeySlot, Long.class, true, true));
        }
        
        public NetworkAligner(CyTable table) {
                build_attributes(table);
        }
        
        CyTable get_data() {
                return m_table;
        }
        
        void set_data_name(String name) {
                m_table.setTitle(name);
        }
        
        public void add_data_aligned_node_pair(String sig0, String sig1) {
                CyRow row = m_table.getRow(m_size);
                row.set(c_PrimaryKeySlot, m_size);
                
                ArrayList<String> list = new ArrayList<>(2);
                list.add(sig0); list.add(sig1);
                
                row.set(c_NodesSignatureSlot, list);
                m_size ++;
        }
        
        public void align_networks_from_data(AlignmentNetwork network0, AlignmentNetwork network1,
                                                AlignmentNetwork aligned) {
                aligned.set_suggested_name(network0.get_suggested_name() + "_" + network1.get_suggested_name());
                
                // Create the bidirectional maps for lookup
                HashMap<String, String> network0_1 = new HashMap<>();
                HashMap<String, String> network1_0 = new HashMap<>();
                List<CyRow> rows = m_table.getAllRows();
                for (CyRow row : rows) {
                        List<String> sigs = row.getList(c_NodesSignatureSlot, String.class);
                        network0_1.put(sigs.get(0), sigs.get(1));
                        network1_0.put(sigs.get(1), sigs.get(0));
                }
                // Add all the data from the first network, and add belongings according to alignment data
                // Add nodes 0
                for (AlignmentNetwork.NodeIterator i = network0.NodeIterator(); i.hasNext(); ) {
                        String sig = i.next();
                        CyNode node = aligned.make_node(sig);
                        aligned.add_node_belongings(node, network0.get_network());
                        if (network0_1.containsKey(sig)) {
                                // This node can be aligned
                                aligned.add_node_belongings(node, network1.get_network());
                        }
                }
                // Add edges 0
                for (AlignmentNetwork.EdgeIterator i = network0.EdgeIterator(); i.hasNext(); ) {
                        AlignmentNetwork.Edge edge_sig = i.next();
                        CyNode node0 = aligned.get_node_from_signature(edge_sig.m_e0);
                        CyNode node1 = aligned.get_node_from_signature(edge_sig.m_e1);
                        CyEdge edge = aligned.make_edge(node0, node1);
                        aligned.add_edge_belongings(edge, network0.get_network());
                        if (network0_1.containsKey(edge_sig.m_e0) && network0_1.containsKey(edge_sig.m_e1)) {
                                // This edge can be aligned
                                aligned.add_edge_belongings(edge, network1.get_network());
                        }
                }
                // Add second network but exclude nodes and edges that are already in the first network
                // Add nodes 1
                for (AlignmentNetwork.NodeIterator i = network1.NodeIterator(); i.hasNext(); ) {
                        String sig = i.next();
                        if (!network1_0.containsKey(sig)) {
                                // This node is not aligned
                                CyNode node = aligned.make_node(sig);
                                aligned.add_node_belongings(node, network1.get_network());
                        }
                }
                // Add edges 1
                for (AlignmentNetwork.EdgeIterator i = network1.EdgeIterator(); i.hasNext(); ) {
                        AlignmentNetwork.Edge edge_sig = i.next();
                        CyNode node0, node1;
                        if (network1_0.containsKey(edge_sig.m_e0)) {
                                String translated = network1_0.get(edge_sig.m_e0);
                                node0 = aligned.get_node_from_signature(translated);
                        } else {
                                node0 = aligned.get_node_from_signature(edge_sig.m_e0);
                        }
                        if (network1_0.containsKey(edge_sig.m_e1)) {
                                String translated = network1_0.get(edge_sig.m_e1);
                                node1 = aligned.get_node_from_signature(translated);
                        } else {
                                node1 = aligned.get_node_from_signature(edge_sig.m_e1);
                        }
                        if (node0 == null || node1 == null) continue;
                        if (!network1_0.containsKey(edge_sig.m_e0) || !network0_1.containsKey(edge_sig.m_e1)) {
                                // This edge is not aligned
                                CyEdge edge = aligned.make_edge(node0, node1);
                                aligned.add_edge_belongings(edge, network1.get_network());
                        }
                }
        }
}
