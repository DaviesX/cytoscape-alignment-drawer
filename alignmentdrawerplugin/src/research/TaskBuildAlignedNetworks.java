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
import java.util.ArrayList;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.Task;
import org.cytoscape.work.TaskMonitor;

/**
 * A task to compute and build an aligned network.
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class TaskBuildAlignedNetworks implements Task {
        
        private final CySwingAppAdapter m_adapter; 
        private final CyTable           m_data;
        private final CyNetwork         m_g0;
        private final CyNetwork         m_g1;
        
        TaskBuildAlignedNetworks(CyTable align_data, CyNetwork g0, CyNetwork g1, 
                                    CySwingAppAdapter adapter) {
                m_adapter = adapter;
                m_data = align_data;
                m_g0 = g0;
                m_g1 = g1;
        }

        @Override
        public void run(TaskMonitor tm) throws Exception {
                System.out.println(getClass() + " - Start aligning networks...");
                
                tm.setTitle("Aligning networks...");
                tm.setStatusMessage("Aligning g0 with g1...");
                tm.setProgress(0.0);
                
                // Align the networks
                NetworkAligner aligner= new NetworkAligner(m_data);
                AlignmentNetwork g0 = new AlignmentNetwork(m_g0);
                AlignmentNetwork g1 = new AlignmentNetwork(m_g1);
                CyNetwork cyaligned = m_adapter.getCyNetworkFactory().createNetwork();
                AlignmentNetwork aligned = new AlignmentNetwork(cyaligned);
                aligner.align_networks_from_data(g0, g1, aligned);
                
                // Configure the style
                tm.setStatusMessage("Styling the aligned network...");
                // @todo: coloring scheme is hardcoded, maybe better find a way to let user choose the scheme
                AlignmentDecorated decorated = new AlignmentDecorated(aligned);
                ArrayList<AlignmentNetwork> aligned_list = new ArrayList<>();
                ArrayList<AlignmentNetwork> g0_list = new ArrayList<>();
                ArrayList<AlignmentNetwork> g1_list = new ArrayList<>();
                aligned_list.add(g0);
                aligned_list.add(g1);
                g0_list.add(g0);
                g1_list.add(g1);
                decorated.set_node_coloring_constraint(aligned_list, Color.GREEN);
                decorated.set_node_coloring_constraint(g0_list, Color.RED);
                decorated.set_node_coloring_constraint(g1_list, Color.BLACK);
                decorated.set_edge_coloring_constraint(aligned_list, Color.GREEN, 255);
                decorated.set_edge_coloring_constraint(g0_list, Color.RED, 127);
                decorated.set_edge_coloring_constraint(g1_list, Color.BLACK, 127);
                // Apply visual style to the network
                // Then put new network and its view to the manager
                CyNetworkView view = 
                        m_adapter.getCyNetworkViewFactory().createNetworkView(decorated.export_cy_network());
                decorated.decorate(view, tm);
                view.updateView();
                m_adapter.getCyNetworkManager().addNetwork(decorated.export_cy_network());
                m_adapter.getCyNetworkViewManager().addNetworkView(view);
                
                tm.setProgress(1.0);
                tm.setStatusMessage("Finished aligning networks...");
                System.out.println(getClass() + " - Finished aligning networks...");
        }

        @Override
        public void cancel() {
        }
}
