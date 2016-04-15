/* 
 * Copyright (C) 2016 Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.Task;
import org.cytoscape.work.TaskMonitor;

/**
 * @author davis
 */
public class TaskHighlightNode implements Task {
        private final String m_sig;
        private final CySwingAppAdapter m_adapter;
        
        public TaskHighlightNode(String sig, CySwingAppAdapter adapter) {
                m_sig = sig;
                m_adapter = adapter;
        }

        @Override
        public void run(TaskMonitor tm) throws Exception {
                NetworkDatabase db = NetworkDatabaseSingleton.get_instance();
                CyNetwork curr_net = m_adapter.getCyApplicationManager().getCurrentNetwork();
                
                AlignmentNetwork aligned_net = new AlignmentNetwork(curr_net);
                db.get_network_binding(aligned_net, AlignmentNetwork.c_AlignmentBindableId);
                
                NetworkDescriptor desc = new NetworkDescriptor(aligned_net);
                Set highlight_set = new HashSet();
                highlight_set.add(m_sig);
                desc.select_by_signatures(highlight_set);
                
                NetworkRenderer rend = new NetworkRenderer();
                NetworkRenderer.Shader highlight_sha = rend.create_shader(Color.MAGENTA, 255);
                List<NetworkRenderer.Batch> batches = new ArrayList<>();
                batches.add(rend.create_batch(desc, highlight_sha));
                rend.render(batches);
        }

        @Override
        public void cancel() {
        }
}
