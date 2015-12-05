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

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Set;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.Task;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskMonitor;


/**
 * Show/hide alignment menu
 * @author davis
 */
public class MenuShowAlignmentSwitch implements MenuProtocol {
        private final String            c_MenuName = "Show/Hide Computed Alignment";
        private final String            c_ParentMenuName = "Tools";
        private CytoscapeMenuService    m_service;
        private boolean                 m_is_showing = true;
        
        @Override
        public String get_menu_name() {
                return c_MenuName;
        }

        @Override
        public String get_parent_menu_name() {
                return c_ParentMenuName;
        }

        @Override
        public void action_performed(ActionEvent e) {
                CySwingAppAdapter adapter = m_service.get_adapter();
                if (m_is_showing) {
                        m_is_showing = false;
                } else {
                        m_is_showing = true;
                }
                Task task = new TaskSwitchAlignmentView(adapter, m_is_showing);
                adapter.getTaskManager().execute(new TaskIterator(task));
        }

        @Override
        public void set_menu_service(CytoscapeMenuService service) {
                m_service = service;
        }
    
}
