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
import org.cytoscape.app.swing.CySwingAppAdapter;


/**
 * Menu Item for importing alignment file
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class MenuImportAlignmentFile implements MenuProtocol{
        private final String            c_MenuName = "Import SANA Alignment File";
        private final String            c_ParentMenuName = "File.Import";
        
        private CytoscapeMenuService    m_service = null;
        private CySwingAppAdapter       m_adapter = null;

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
        }

        @Override
        public void set_menu_service(CytoscapeMenuService service) {
                m_service = service;
                m_adapter = m_service.get_adapter();
        }
        
}
