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

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.presentation.property.NodeShapeVisualProperty;
import org.cytoscape.view.presentation.property.values.NodeShape;

/**
 * Put customize styling for the aligned network.
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class AlignmentDecorated extends AlignmentNetwork {
        private final CyNetwork                         m_decorated;
        private final HashMap<Set<Long>, Color>         m_node_constraints;
        private final HashMap<Set<Long>, Color>         m_edge_constraints;
        private final Double                            c_NodeWidth = 12.0;
        private final Double                            c_NodeHeight = 12.0;
        private final NodeShape                         c_NodeShape = NodeShapeVisualProperty.ELLIPSE;
        
        private final String                            m_style_name;
        
        AlignmentDecorated(AlignmentNetwork network) {
                super(network);
                m_decorated             = super.get_network();
                m_node_constraints      = new HashMap<>();
                m_edge_constraints      = new HashMap<>();
                m_style_name            = super.get_suggested_name() + "_visual_style";
        }
        
        
        void set_node_coloring_constraint(List<AlignmentNetwork> networks, Color color) {
                HashSet<Long> network_ids = new HashSet<>();
                for (AlignmentNetwork network : networks) {
                        network_ids.add(network.get_suid());
                }
                m_node_constraints.put(network_ids, color);
        }
        
        void set_edge_coloring_constraint(List<AlignmentNetwork> networks, Color color) {
                HashSet<Long> network_ids = new HashSet<>();
                for (AlignmentNetwork network : networks) {
                        network_ids.add(network.get_suid());
                }
                m_edge_constraints.put(network_ids, color);
        }
        
        void decorate(CyNetworkView view) throws Exception {
                // Decorate nodes
                for (AlignmentNetwork.NodeIterator i = super.NodeIterator(); i.hasNext(); ) {
                        String sig              = i.next();
                        CyNode node             = super.get_node_from_signature(sig);
                        Set<Long> belongings    = Util.list_to_set(super.get_node_belongings(node));
                        Color color             = m_node_constraints.get(belongings);
                        
                        View<CyNode> node_view  = view.getNodeView(node);
                        if (color != null) {
                                node_view.setVisualProperty(BasicVisualLexicon.NODE_FILL_COLOR, color);
                        }
                        node_view.setVisualProperty(BasicVisualLexicon.NODE_WIDTH, c_NodeWidth);
                        node_view.setVisualProperty(BasicVisualLexicon.NODE_HEIGHT, c_NodeHeight);
                        node_view.setVisualProperty(BasicVisualLexicon.NODE_BORDER_WIDTH, 0.0);
                        node_view.setVisualProperty(BasicVisualLexicon.NODE_SHAPE, c_NodeShape);
                        // node_view.setVisualProperty(BasicVisualLexicon.NODE_LABEL, sig);
                }
                // Decorate edges
                for (AlignmentNetwork.EdgeIterator i = super.EdgeIterator(); i.hasNext(); ) {
                        Long suid               = i.next2();
                        CyEdge edge             = super.get_edge_from_suid(suid);
                        Set<Long> belongings    = Util.list_to_set(super.get_edge_belongings(edge));
                        Color color             = m_node_constraints.get(belongings);
                        
                        View<CyEdge> edge_view  = view.getEdgeView(edge);
                        if (color != null) {
                                edge_view.setVisualProperty(BasicVisualLexicon.EDGE_PAINT, color);
                                edge_view.setVisualProperty(BasicVisualLexicon.EDGE_UNSELECTED_PAINT, color);
                        }
                }
        }
        
        CyNetwork export_cy_network() {
                return m_decorated;
        }
}
