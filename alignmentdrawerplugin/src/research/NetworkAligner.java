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
import java.util.List;
import java.util.Set;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyTableFactory;
import org.cytoscape.work.TaskMonitor;

/**
 * Aligning networks from given data
 *
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class NetworkAligner {

        private CyTable m_table = null;         // This table stores majority of the alignment data
        private final String c_PrimaryKeySlot = "AlignPrimaryKey";
        private final String c_NodesSignatureSlot = "AlignNodesSig";
        private final String c_DataTitle = "AlignmentData";
        private Long m_size;

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
                list.add(sig0);
                list.add(sig1);

                row.set(c_NodesSignatureSlot, list);
                m_size++;
        }
        
        public String the_only_element_in(Set<String> set) {
                return set.iterator().next();
        }
        
        public void align_networks_from_data(AlignmentNetwork network0, AlignmentNetwork network1,
                                             AlignmentNetwork aligned, TaskMonitor tm) throws Exception {
                String name = network0.get_suggested_name() + "_" + network1.get_suggested_name();
                aligned.set_suggested_name(name);
                if (tm != null) {
                        tm.setStatusMessage("aligning " + name + "...");
                }

                List<CyRow> rows = m_table.getAllRows();

                // keep track of progress
                int j = 0;
                int total = rows.size() + network0.get_node_count() + network0.get_edge_count()
                            + network1.get_node_count() + network1.get_edge_count();

                // Create the bidirectional maps for lookup
                BidiMap<String, String> network0_1 = new DualHashBidiMap<>();
                for (CyRow row : rows) {
                        List<String> sigs = row.getList(c_NodesSignatureSlot, String.class);
                        network0_1.put(sigs.get(0), sigs.get(1));
                        Util.advance_progress(tm, j, total);
                }
                // Update the signatures with the one in the real network
                for (AlignmentNetwork.NodeIterator i = network0.NodeIterator(); i.hasNext(); ) {
                        String real_sig = i.next();
                        String plain_sig = the_only_element_in(new NodeSignature(real_sig).get_all_ids());
                        String val = network0_1.get(plain_sig);
                        if (val != null) {
                                network0_1.remove(plain_sig);
                                network0_1.put(real_sig, val);
                        }
                }
                for (AlignmentNetwork.NodeIterator i = network1.NodeIterator(); i.hasNext(); ) {
                        String real_sig = i.next();
                        String plain_sig = the_only_element_in(new NodeSignature(real_sig).get_all_ids());
                        String key = network0_1.getKey(plain_sig);
                        if (key != null) {
                                network0_1.remove(plain_sig);
                                network0_1.put(key, real_sig);
                        }
                }
                
                // Add all the data from the first network, and add belongings according to alignment data
                // Add nodes 0
                for (AlignmentNetwork.NodeIterator i = network0.NodeIterator(); i.hasNext();) {
                        String sig = i.next();
                        if (network0_1.containsKey(sig)) {
                                // This node can be aligned
                                CyNode node = aligned.make_node(new NodeSignature(sig));
                                aligned.add_node_belongings(node, network0.get_network());
                                aligned.add_node_belongings(node, network1.get_network());
                        } else {
                                // This node cannot be aligned
                                CyNode node = aligned.make_node(new NodeSignature(sig));
                                aligned.add_node_belongings(node, network0.get_network());
                        }
                        Util.advance_progress(tm, j, total);
                }
                // Add edges 0
                for (AlignmentNetwork.EdgeIterator i = network0.EdgeIterator(); i.hasNext();) {
                        AlignmentNetwork.Edge edge_sig = i.next();
                        CyNode node0, node1;
                        node0 = aligned.get_node_from_signature(new NodeSignature(edge_sig.m_e0));
                        node1 = aligned.get_node_from_signature(new NodeSignature(edge_sig.m_e1));
//                        if (network0_1.containsKey(edge_sig.m_e0)) {
//                                node0 = aligned.get_node_from_signature(new NodeSignature(edge_sig.m_e0));
//                        } else {
//                                node0 = aligned.get_node_from_signature(new NodeSignature(edge_sig.m_e0));
//                        }
//                        if (network0_1.containsKey(edge_sig.m_e1)) {
//                                node1 = aligned.get_node_from_signature(new NodeSignature(edge_sig.m_e1));
//                        } else {
//                                node1 = aligned.get_node_from_signature(new NodeSignature(edge_sig.m_e1));
//                        }
                        if (node0 == null || node1 == null) {
                                System.out.println(getClass() + " - node0:" + edge_sig.m_e0 + "node1:" + edge_sig.m_e1);
                        }
                        CyEdge edge = aligned.make_edge(node0, node1);
                        aligned.add_edge_belongings(edge, network0.get_network());
                        if (network0_1.containsKey(edge_sig.m_e0) && network0_1.containsKey(edge_sig.m_e1)) {
                                // This edge can be aligned
                                aligned.add_edge_belongings(edge, network1.get_network());
                        }
                        Util.advance_progress(tm, j, total);
                }
                // Add second network but exclude nodes and edges that are already in the first network
                // Add nodes 1
                for (AlignmentNetwork.NodeIterator i = network1.NodeIterator(); i.hasNext();) {
                        String sig = i.next();
                        if (!network0_1.containsValue(sig)) {
                                // This node is not aligned
                                CyNode node = aligned.make_node(new NodeSignature(sig));
                                aligned.add_node_belongings(node, network1.get_network());
                        } else {
                                // This node is aligned but we shall change the signature
                                // to reflect the relation of the aligned node
                                String translated = network0_1.getKey(sig);
                                CyNode node = aligned.get_node_from_signature(new NodeSignature(translated));
                                NodeSignature nodesig = new NodeSignature(translated);
                                nodesig.add_id(sig);
                                aligned.mutate_node_signature(node, nodesig);
                        }
                        Util.advance_progress(tm, j, total);
                }
                // Add edges 1
                for (AlignmentNetwork.EdgeIterator i = network1.EdgeIterator(); i.hasNext();) {
                        AlignmentNetwork.Edge edge_sig = i.next();
                        CyNode node0, node1;
                        if (network0_1.containsValue(edge_sig.m_e0)) {
                                String translated = network0_1.getKey(edge_sig.m_e0);
                                node0 = aligned.get_node_from_signature(new NodeSignature(translated));
                        } else {
                                node0 = aligned.get_node_from_signature(new NodeSignature(edge_sig.m_e0));
                        }
                        if (network0_1.containsValue(edge_sig.m_e1)) {
                                String translated = network0_1.getKey(edge_sig.m_e1);
                                node1 = aligned.get_node_from_signature(new NodeSignature(translated));
                        } else {
                                node1 = aligned.get_node_from_signature(new NodeSignature(edge_sig.m_e1));
                        }
                        if (node0 == null) {
                                if (network0_1.containsValue(edge_sig.m_e0)) {
                                        throw new Exception(" - node0 not found from the g0 network: "
                                                            + edge_sig.m_e0 + "->" + network0_1.get(edge_sig.m_e0));
                                } else {
                                        throw new Exception(" - node0 not found from the g1 network: "
                                                            + edge_sig.m_e0);
                                }
                        }
                        if (node1 == null) {
                                if (network0_1.containsValue(edge_sig.m_e1)) {
                                        throw new Exception(" - node1 not found from the g0 network: "
                                                            + edge_sig.m_e1 + "->" + network0_1.get(edge_sig.m_e1));
                                } else {
                                        throw new Exception(" - node1 not found from the g1 network: "
                                                            + edge_sig.m_e1);
                                }
                        }
                        if (!network0_1.containsValue(edge_sig.m_e0) || !network0_1.containsValue(edge_sig.m_e1)) {
                                // This edge is not aligned
                                CyEdge edge = aligned.make_edge(node0, node1);
                                aligned.add_edge_belongings(edge, network1.get_network());
                        }
                        Util.advance_progress(tm, j, total);
                }
        }

        public void align_networks_from_data(AlignmentNetwork network0, AlignmentNetwork network1,
                                             AlignmentNetwork aligned) throws Exception {
                align_networks_from_data(network0, network1, aligned, null);
        }
}
