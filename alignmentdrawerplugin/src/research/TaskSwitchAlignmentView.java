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
import java.util.Set;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.Task;
import org.cytoscape.work.TaskMonitor;

/**
 * Switch the view of alignment network between show/hidden
 *
 * @author davis
 */
public class TaskSwitchAlignmentView implements Task {

        enum SwitchMode {
                ShowOrHideAlignedNetwork,
                ShowOrHideListedNodes
        }

        private final CySwingAppAdapter m_adapter;
        private final boolean m_is_showing;
        private SwitchMode m_mode;

        TaskSwitchAlignmentView(CySwingAppAdapter adapter, boolean is_to_show) {
                m_adapter = adapter;
                m_is_showing = is_to_show;
        }

        private void show_or_hide_aligned_network(TaskMonitor tm) throws Exception {
                System.out.println(getClass() + " - Begin switching alignment view...");
                CyNetworkManager mgr = m_adapter.getCyNetworkManager();
                Set<CyNetwork> networks = mgr.getNetworkSet();
                NetworkDatabase db = NetworkDatabaseSingleton.get_instance();
                for (CyNetwork network : networks) {
                        AlignmentNetwork align_net = new AlignmentNetwork(network);
                        if (!db.has_network(align_net)) {
                                // Doesn't have record of this network, skip
                                System.out.println(getClass() + " - doesn't have record of: " + network.getSUID());
                                continue;
                        }
                        // Obtain data from database
                        System.out.println(getClass() + " - network: " + network.getSUID()
                                           + " is in the database, will modify this network");
                        Bindable b_decorated = db.get_network_binding(align_net,
                                                                      AlignmentDecorator.c_DecoratedBindableId);
                        Bindable b_g0_network = db.get_network_binding(align_net,
                                                                       AlignmentNetwork.c_AlignmentBindableId + "_g0");
                        Bindable b_g1_network = db.get_network_binding(align_net,
                                                                       AlignmentNetwork.c_AlignmentBindableId + "_g1");
                        Bindable b_network_view = db.get_network_binding(align_net,
                                                                         CyNetworkView.class.getName());
                        AlignmentDecorator decorator = (AlignmentDecorator) b_decorated.get_binded();
                        AlignmentNetwork g0 = (AlignmentNetwork) b_g0_network.get_binded();
                        AlignmentNetwork g1 = (AlignmentNetwork) b_g1_network.get_binded();
                        CyNetworkView view = (CyNetworkView) b_network_view.get_binded();
                        // Configurate and decorate the view
                        ArrayList<AlignmentNetwork> g0_list = new ArrayList<>();
                        ArrayList<AlignmentNetwork> g1_list = new ArrayList<>();
                        g0_list.add(g0);
                        g1_list.add(g1);
                        if (m_is_showing == false) {
                                // Hide unaligned nodes/edges
                                decorator.set_network_node_constraint(g0_list, 0);
                                decorator.set_network_node_constraint(g1_list, 0);
                                decorator.set_network_edge_constraint(g0_list, 0);
                                decorator.set_network_edge_constraint(g1_list, 0);
                        } else {
                                // Show everything
                                decorator.set_network_node_constraint(g0_list, 127);
                                decorator.set_network_node_constraint(g1_list, 127);
                                decorator.set_network_edge_constraint(g0_list, 127);
                                decorator.set_network_edge_constraint(g1_list, 127);
                        }
                        decorator.decorate(view, tm);
                        view.updateView();
                        System.out.println(getClass() + " - Finished switching alignment view...");
                }
        }

        @Override
        public void run(TaskMonitor tm) throws Exception {
                show_or_hide_aligned_network(tm);
        }

        @Override
        public void cancel() {
                // do nothing
        }
}
