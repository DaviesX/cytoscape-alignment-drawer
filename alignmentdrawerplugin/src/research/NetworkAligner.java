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
        }
}
