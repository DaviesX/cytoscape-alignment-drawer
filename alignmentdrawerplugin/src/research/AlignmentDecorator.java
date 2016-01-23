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
import java.util.Objects;
import java.util.Set;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.presentation.property.NodeShapeVisualProperty;
import org.cytoscape.view.presentation.property.values.NodeShape;
import org.cytoscape.work.TaskMonitor;

/**
 * Put customize styling for the aligned network.
 *
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class AlignmentDecorator extends AlignmentNetwork {

        class ConstraintValue {

                public Color color;
                public Integer transparency;

                ConstraintValue(Color c, Integer t) {
                        color = c;
                        transparency = t;
                }
        }

        class Constraint {

                private final Set<Long> m_network_ids;
                private final Set<String> m_node_ids;

                Constraint(Set<Long> network_ids, Set<String> node_ids) {
                        m_network_ids = network_ids;
                        m_node_ids = node_ids;
                }

                Constraint(Set<Long> network_ids, String node_id) {
                        m_network_ids = network_ids;
                        m_node_ids = new HashSet<>();
                        m_node_ids.add(node_id);
                }

                @Override
                public boolean equals(Object o) {
                        if (!(o instanceof Constraint)) {
                                return false;
                        }
                        Constraint other = (Constraint) o;
                        return (other.m_network_ids == null || m_network_ids == null
                                || other.m_network_ids.equals(m_network_ids))
                               && (other.m_node_ids == null || m_node_ids == null
                                   || m_node_ids.containsAll(other.m_node_ids));
                }

                @Override
                public int hashCode() {
                        int hash = 7;
                        hash = 23 * hash + Objects.hashCode(this.m_network_ids);
                        hash = 23 * hash + Objects.hashCode(this.m_node_ids);
                        return hash;
                }
        }

        private final CyNetwork m_decorated;
        private final HashMap<Constraint, ConstraintValue> m_node_constraints;
        private final HashMap<Constraint, ConstraintValue> m_edge_constraints;
        private final Double c_NodeWidth = 12.0;
        private final Double c_NodeHeight = 12.0;
        private final NodeShape c_NodeShape = NodeShapeVisualProperty.ELLIPSE;

        private final String m_style_name;

        static public final String c_DecoratedBindableId = "AlignmentDecoratedBindable";

        AlignmentDecorator(AlignmentNetwork network) {
                super(network);
                m_decorated = super.get_network();
                m_node_constraints = new HashMap<>();
                m_edge_constraints = new HashMap<>();
                m_style_name = super.get_suggested_name() + "_visual_style";
        }

        private HashSet<Long> make_network_id(List<AlignmentNetwork> networks) {
                HashSet<Long> network_ids = new HashSet<>();
                for (AlignmentNetwork network : networks) {
                        network_ids.add(network.get_suid());
                }
                return network_ids;
        }

        public void set_network_edge_constraint(List<AlignmentNetwork> networks,
                                                Color color,
                                                Integer transparency) {
                m_edge_constraints.put(new Constraint(make_network_id(networks), (Set<String>) null),
                                       new ConstraintValue(color, transparency));
        }

        public void set_network_edge_constraint(List<AlignmentNetwork> networks, Color color) {
                Constraint constraint = new Constraint(make_network_id(networks), (Set<String>) null);
                ConstraintValue value = m_edge_constraints.get(constraint);
                if (value != null) {
                        value.color = color;
                } else {
                        m_edge_constraints.put(constraint, new ConstraintValue(color, 255));
                }
        }

        public void set_network_edge_constraint(List<AlignmentNetwork> networks, Integer transparency) {
                Constraint constraint = new Constraint(make_network_id(networks), (Set<String>) null);
                ConstraintValue value = m_edge_constraints.get(constraint);
                if (value != null) {
                        value.transparency = transparency;
                } else {
                        m_edge_constraints.put(constraint, new ConstraintValue(Color.BLACK, transparency));
                }
        }

        public void set_network_node_constraint(List<AlignmentNetwork> networks,
                                                Color color,
                                                Integer transparency) {
                m_node_constraints.put(new Constraint(make_network_id(networks), (Set<String>) null),
                                       new ConstraintValue(color, transparency));
        }

        public void set_network_node_constraint(List<AlignmentNetwork> networks, Integer transparency) {
                Constraint constraint = new Constraint(make_network_id(networks), (Set<String>) null);
                ConstraintValue value = m_node_constraints.get(constraint);
                if (value != null) {
                        value.transparency = transparency;
                } else {
                        m_node_constraints.put(constraint, new ConstraintValue(Color.BLACK, transparency));
                }
        }

        public void set_network_node_constraint(List<AlignmentNetwork> networks, Color color) {
                Constraint constraint = new Constraint(make_network_id(networks), (Set<String>) null);
                ConstraintValue value = m_node_constraints.get(constraint);
                if (value != null) {
                        value.color = color;
                } else {
                        m_node_constraints.put(constraint, new ConstraintValue(color, 255));
                }
        }

        public void set_node_signature_constraint(Set<String> signatures,
                                                  Color color,
                                                  Integer transparency) {
                m_node_constraints.put(new Constraint(null, signatures),
                                       new ConstraintValue(color, transparency));
        }

        public void set_edge_signature_constraint(Set<String> signatures,
                                                  Color color,
                                                  Integer transparency) {
                m_edge_constraints.put(new Constraint(null, signatures),
                                       new ConstraintValue(color, transparency));
        }

        public void decorate(CyNetworkView view, TaskMonitor tm) {
                // To keep track of progress
                int j = 0;
                int total = super.get_node_count() + super.get_edge_count();

                // Decorate nodes
                for (AlignmentNetwork.NodeIterator i = super.NodeIterator(); i.hasNext();) {
                        String sig = i.next();
                        CyNode node = super.get_node_from_signature(sig);
                        Set<Long> belongings = Util.list_to_set(super.get_node_belongings(node));
                        Constraint constraint = new Constraint(belongings, sig);
                        ConstraintValue value = m_node_constraints.get(constraint);

                        View<CyNode> node_view = view.getNodeView(node);
                        if (value != null) {
                                node_view.setVisualProperty(BasicVisualLexicon.NODE_FILL_COLOR, value.color);
                                node_view.setVisualProperty(BasicVisualLexicon.NODE_TRANSPARENCY, value.transparency);
                        }
                        node_view.setVisualProperty(BasicVisualLexicon.NODE_WIDTH, c_NodeWidth);
                        node_view.setVisualProperty(BasicVisualLexicon.NODE_HEIGHT, c_NodeHeight);
                        node_view.setVisualProperty(BasicVisualLexicon.NODE_BORDER_WIDTH, 0.0);
                        node_view.setVisualProperty(BasicVisualLexicon.NODE_SHAPE, c_NodeShape);
                        node_view.setVisualProperty(BasicVisualLexicon.NODE_TOOLTIP, sig);
                        Util.advance_progress(tm, j, total);
                }
                // Decorate edges
                for (AlignmentNetwork.EdgeIterator i = super.EdgeIterator(); i.hasNext();) {
                        Edge euid = i.next();
                        CyEdge edge = super.get_edge_from_suid(euid.m_eid);
                        Set<Long> belongings = Util.list_to_set(super.get_edge_belongings(edge));
                        Set<String> node_sigs = new HashSet<>();
                        node_sigs.add(euid.m_e0);
                        node_sigs.add(euid.m_e1);
                        Constraint constraint = new Constraint(belongings, node_sigs);
                        ConstraintValue value = m_edge_constraints.get(constraint);

                        View<CyEdge> edge_view = view.getEdgeView(edge);
                        if (value != null) {
                                edge_view.setVisualProperty(BasicVisualLexicon.EDGE_STROKE_UNSELECTED_PAINT,
                                                            value.color);
                                edge_view.setVisualProperty(BasicVisualLexicon.EDGE_TRANSPARENCY,
                                                            value.transparency);
                        }
                        Util.advance_progress(tm, j, total);
                }
        }

        public void decorate(CyNetworkView view) {
                decorate(view, null);
        }

        public CyNetwork export_cy_network() {
                return m_decorated;
        }
}
