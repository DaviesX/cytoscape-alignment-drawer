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

import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyTable;
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
                
                tm.setProgress(0.1);
                
                // Align the networks
                NetworkAligner aligner= new NetworkAligner(m_data);
                AlignmentNetwork g0 = new AlignmentNetwork(m_g0);
                AlignmentNetwork g1 = new AlignmentNetwork(m_g1);
                CyNetwork cyaligned = m_adapter.getCyNetworkFactory().createNetwork();
                AlignmentNetwork aligned = new AlignmentNetwork(cyaligned);
                
                aligner.align_networks_from_data(g0, g1, aligned);
                // Configure the style
                NetworkConfigurator config = new NetworkConfigurator(aligned);
                config.configure();
                CyNetwork final_aligned = config.export_cy_network();
                // Put new network to the manager
                m_adapter.getCyNetworkManager().addNetwork(final_aligned);
                
                tm.setProgress(1.0);
                
                System.out.println(getClass() + " - Finished aligning networks...");
        }

        @Override
        public void cancel() {
        }
}
