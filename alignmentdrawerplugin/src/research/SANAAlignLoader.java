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

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import org.cytoscape.io.DataCategory;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.work.TaskMonitor;


/**
 * Actual implementation of a SANA alignment file loader.
 * 
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class SANAAlignLoader implements FileLoaderProtocol {
        private final String            c_AlignmentFileExtension = "txt";
        private final String            c_AlignmentFileContent = "txt";
        private final DataCategory      c_AlignmentFileCategory = DataCategory.UNSPECIFIED;
        private final String            c_AlignmentFileDesc = "SANA Network Alignment File";
        private final String            c_AlignmentFileLoaderID = "SANAAlignmentLoader";

        private HashSet<String>   m_extenstion = null;
        private HashSet<String>   m_content = null;
        
        private CyNetworkFactory        m_network_fact = null;
        private CyNetworkViewFactory    m_view_fact = null;
        private InputStream             m_istream = null;

        public SANAAlignLoader() {
                m_extenstion    = new HashSet<>();
                m_content       = new HashSet<>();
                m_extenstion.add(c_AlignmentFileExtension);
                m_content.add(c_AlignmentFileContent);
        }

        @Override
        public CyNetwork[] getNetworks() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public CyNetworkView buildCyNetworkView(CyNetwork cn) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void run(TaskMonitor tm) throws Exception {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void cancel() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Set<String> get_file_extension() {
                return m_extenstion;
        }

        @Override
        public Set<String> get_file_content_type() {
                return m_content;
        }

        @Override
        public DataCategory get_file_category() {
                return c_AlignmentFileCategory;
        }

        @Override
        public String get_file_description() {
                return c_AlignmentFileDesc;
        }

        @Override
        public String get_file_loader_id() {
                return c_AlignmentFileLoaderID;
        }
        
        @Override
        public void set_input_stream(InputStream s) {
                m_istream = s;
        }

        @Override
        public void set_view_factory(CyNetworkViewFactory fact) {
                m_view_fact = fact;
        }

        @Override
        public void set_network_factory(CyNetworkFactory fact) {
                m_network_fact = fact;
        }
}
