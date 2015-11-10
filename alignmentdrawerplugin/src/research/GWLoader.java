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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.cytoscape.io.DataCategory;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.work.TaskMonitor;

/**
 * Actually implementation of GW file loader
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class GWLoader implements FileLoaderProtocol {
        private final String            c_GWFileExtension = "gw";
        private final String            c_GWFileContent = "txt";
        private final DataCategory      c_GWFileCategory = DataCategory.NETWORK;
        private final String            c_GWFileDesc = "GW LEDA network file";
        private final String            c_GWFileLoaderID = "GWLoader";

        private HashSet<String>         m_extenstion = null;
        private HashSet<String>         m_content = null;
        
        private final int               c_MaxBuffering = 16384;
        private CyNetworkViewFactory    m_view_fact = null;
        private CyNetworkFactory        m_network_fact = null;
        private InputStream             m_istream = null;
        private CyNetwork               m_network = null;
        private boolean                 m_is_canceled = false;
        
        public GWLoader() {
                m_extenstion    = new HashSet<>();
                m_content       = new HashSet<>();
                m_extenstion.add(c_GWFileExtension);
                m_content.add(c_GWFileContent);
        }

        @Override
        public CyNetwork[] getNetworks() {
                return new CyNetwork[] {m_network};
        }

        @Override
        public CyNetworkView buildCyNetworkView(CyNetwork cn) {
                if (m_view_fact == null) {
                        System.out.println("Have not given a network view factory");
                        return null;
                } else {
                        return m_view_fact.createNetworkView(cn);
                }
        }

        @Override
        public void run(TaskMonitor tm) throws Exception {
                System.out.println("Running GW Loader...");
                
                m_is_canceled = false;

                String text = extract_string_from_stream(m_istream);
                String[] lines = text.split("\n");

                // GW file header:
                // LEDA.GRAPH
                // string
                // short
                // -2
                final int c_MinLines = 5;
                final int c_StartingLine = 4;
                if (lines.length < c_MinLines || !lines[0].equals("LEDA.GRAPH")) {
                        System.out.println("Invalid LEDA.GRAPH");
                        return;
                }
                
                if (m_network_fact == null) {
                        System.out.println("Have not given a network factory");
                        return ;
                }
                m_network = m_network_fact.createNetwork();

                // load in nodes
                int num_nodes = Integer.decode(lines[c_StartingLine]);
                ArrayList<CyNode> node_list = new ArrayList<>();
                String current_line;
                Pattern regex_pattern = Pattern.compile("\\|\\{([[a-zA-Z][0-9][-][@][,][\"]]*)\\}\\|");
                int i;
                for (i = c_StartingLine + 1; i < lines.length; i++) {
                        if (m_is_canceled) {
                                System.out.println("Tasks canceled: " + this.getClass());
                                return ;
                        }
                        current_line = lines[i];
                        // pattern of a line containing a node: |{A1BG}|
                        Matcher matcher = regex_pattern.matcher(current_line);
                        if (!matcher.matches() ) {
                                // finished reading node list
                                break;
                        }
                        String signature = matcher.group(1);
                        CyNode node = m_network.addNode();
                        CyRow attri = m_network.getRow(node);
                        // customize name attribute
                        attri.set("name", signature);
//                        AlignmentNode node = new AlignmentNode(signature,
//                                                               m_network,
//                                                               m_network.alloc_network_suid());
                        node_list.add(node);
                }
                System.out.println("finished reading node list...");
                // load in edges
                int num_edges = Integer.decode(lines[i++]);
                Pattern regex_pattern2 = Pattern.compile("([0-9]*) ([0-9]*) [0-9]* \\|\\{\\}\\|");
                for (; i < lines.length; i++) {
                        if (m_is_canceled) {
                                System.out.println("Tasks canceled: " + this.getClass());
                                return ;
                        }
                        current_line = lines[i];
                        Matcher matcher = regex_pattern2.matcher(current_line);
                        if (!matcher.matches()) {
                                // finished reading edge list
                                break;
                        }
                        // pattern of a line containing an edge: 649 757 0 |{}|
                        String sn0 = matcher.group(1);
                        String sn1 = matcher.group(2);
                        int n0 = Integer.decode(sn0);
                        int n1 = Integer.decode(sn1);
//                        AlignmentNode node0 = node_list.get(n0 - 1);
//                        AlignmentNode node1 = node_list.get(n1 - 1);
                        CyNode node0 = node_list.get(n0 - 1);
                        CyNode node1 = node_list.get(n1 - 1);
                        // Create an edge between node1 and node2
//                        m_network.make_edge(node0, node1);
                        m_network.addEdge(node0, node1, true);
                }
                System.out.println("finished reading edge list...");
                if (m_network.getNodeCount() != num_nodes) {
                        System.out.println("node count stated in file doesn't match that of loaded");
                }
                if (m_network.getEdgeCount() != num_edges) {
                        System.out.println("edge count stated in file doesn't match that have been made");
                }
                System.out.println("Everything has been loaded.");
        }

        @Override
        public void cancel() {
                m_is_canceled = true;
        }
        
        private String extract_string_from_stream(InputStream source) throws IOException {
		StringWriter writer = new StringWriter();
		try (   BufferedReader reader = new BufferedReader(new InputStreamReader(source))) {
			char[] buffer = new char[c_MaxBuffering];
			int charactersRead = reader.read(buffer, 0, buffer.length);
			while (charactersRead != -1) {
				writer.write(buffer, 0, charactersRead);
				charactersRead = reader.read(buffer, 0, buffer.length);
			}
		}
		return writer.toString();
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
                return c_GWFileCategory;
        }

        @Override
        public String get_file_description() {
                return c_GWFileDesc;
        }

        @Override
        public String get_file_loader_id() {
                return c_GWFileLoaderID;
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
