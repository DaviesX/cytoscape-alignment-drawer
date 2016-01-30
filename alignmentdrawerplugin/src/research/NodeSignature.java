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

import java.util.Set;
import java.util.TreeSet;

/**
 * Manage signature identification.
 *
 * @author davis
 */
public class NodeSignature {

        private final Set<String> m_namespaces = new TreeSet<>();
        private final Set<String> m_ids = new TreeSet<>();

        private final String COMMA = "#";
        private final String SEPARATOR = "/";

        private void arrange_id_namespace(String ouput) {
                String[] parts = ouput.split(SEPARATOR);
                String[] namespaces = parts[0].split(COMMA);
                String[] ids = parts[1].split(COMMA);
                for (String namespace : namespaces) {
                        if (!namespace.equals(""))
                                m_namespaces.add(namespace);
                }
                for (String id : ids) {
                        if (!id.equals(""))
                                m_ids.add(id);
                }
        }

        public NodeSignature() {
        }
        
        public NodeSignature(String output) {
                try {
                        arrange_id_namespace(output);
                } catch (Exception ex) {
                        System.out.println(getClass() + " - Error: " + output);
                        throw ex;
                }
        }
        
        public void import_signature(String output) {
                m_namespaces.clear();
                m_ids.clear();
                arrange_id_namespace(output);
        }
        
        public void add_id(String id) {
                m_ids.add(id);
        }

        public void add_namespace(String namespace) {
                m_namespaces.add(namespace);
        }
        
        public Set<String> get_all_ids() {
                return m_ids;
        }
        
        public Set<String> get_all_namespaces() {
                return m_namespaces;
        }

        @Override
        public boolean equals(Object o) {
                if (!(o instanceof NodeSignature)) {
                        return false;
                }
                NodeSignature other = (NodeSignature) o;
                return m_ids.containsAll(other.m_ids) && m_namespaces.containsAll(other.m_namespaces);
        }

        @Override
        public int hashCode() {
                return 0;
        }

        @Override
        public String toString() {
                StringBuilder s = new StringBuilder();
                for (String ns : m_namespaces) {
                        s.append(ns).append(COMMA);
                }
                s.append(SEPARATOR);
                for (String id : m_ids) {
                        s.append(id).append(COMMA);
                }
                return s.toString();
        }
}
