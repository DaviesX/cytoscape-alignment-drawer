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
import java.util.Properties;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.io.BasicCyFileFilter;
import org.cytoscape.io.CyFileFilter;
import org.cytoscape.io.read.InputStreamTaskFactory;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.work.TaskIterator;


/**
 * Direct the InputStream to our FileLoaderProtocol in order to create a network.
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
class GWLoaderInputStreamFactory implements InputStreamTaskFactory {
        private final CyNetworkFactory        m_network_fact;
        private final CyNetworkViewFactory    m_view_fact;
        private final BasicCyFileFilter       m_filter;
        private final FileLoaderProtocol      m_protocol;

        public GWLoaderInputStreamFactory(FileLoaderProtocol protocol,
                                          BasicCyFileFilter filter,
                                          CyNetworkFactory network_fact,
                                          CyNetworkViewFactory view_fact) {
                m_filter        = filter;
                m_network_fact  = network_fact;
                m_view_fact     = view_fact;
                m_protocol      = protocol;
        }

        @Override
        public TaskIterator createTaskIterator(InputStream in, String string) {
                m_protocol.set_input_stream(in);
                m_protocol.set_view_factory(m_view_fact);
                m_protocol.set_network_factory(m_network_fact);
                return new TaskIterator(m_protocol);
        }

        @Override
        public boolean isReady(InputStream in, String string) {
                return true;
        }

        @Override
        public CyFileFilter getFileFilter() {
                return m_filter;
        }
}

/**
 * Create a loader service with given FileLoaderProtocol.
 *
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class CytoscapeLoaderService {
        private final CySwingAppAdapter m_adapter;
        
        public CytoscapeLoaderService(CySwingAppAdapter adapter) {
                m_adapter = adapter;
        }
        
        public boolean install_protocol(FileLoaderProtocol protocol) {
                BasicCyFileFilter filter = new BasicCyFileFilter(protocol.get_file_extension(), 
                                                                 protocol.get_file_content_type(),
                                                                 protocol.get_file_description(), 
                                                                 protocol.get_file_category(),
                                                                 m_adapter.getStreamUtil());
                GWLoaderInputStreamFactory factory = 
                        new GWLoaderInputStreamFactory(protocol,
                                                       filter,
                                                       m_adapter.getCyNetworkFactory(),
                                                       m_adapter.getCyNetworkViewFactory());
                Properties props = new Properties();
                props.setProperty("readerDescription", protocol.get_file_description());
                props.setProperty("readerId", protocol.get_file_loader_id());
                m_adapter.getCyServiceRegistrar().registerService(factory, InputStreamTaskFactory.class, props);
                return true;
        }
}
