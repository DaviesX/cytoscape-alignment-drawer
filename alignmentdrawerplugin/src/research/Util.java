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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.cytoscape.work.TaskMonitor;

/**
 * Utility class
 *
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class Util {

        private static final int MAX_BUFFERING = 16384;

        public static String extract_string_from_stream(InputStream source) throws IOException {
                StringWriter writer = new StringWriter();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(source))) {
                        char[] buffer = new char[MAX_BUFFERING];
                        int n_bytes = reader.read(buffer, 0, buffer.length);
                        while (n_bytes != -1) {
                                writer.write(buffer, 0, n_bytes);
                                n_bytes = reader.read(buffer, 0, buffer.length);
                        }
                }
                return writer.toString();
        }

        public static String[] extract_lines_from_stream(InputStream source) throws IOException {
                return extract_string_from_stream(source).split("\n");
        }

        public static String run_selection_dialog(String[] list_of_data, String title, String prompt) {
                String default_string = "";
                if (list_of_data.length != 0) {
                        default_string = list_of_data[0];
                }
                return (String) JOptionPane.showInputDialog(null, title, prompt,
                                                            JOptionPane.PLAIN_MESSAGE, null,
                                                            list_of_data, default_string);
        }

        public abstract class Dialog extends JFrame {

                public abstract void set_data(Object data);

                public abstract Object get_data();

                public abstract String get_name();
        }

        private final static Object LOCK = new Object();

        public static Object run_customized_dialog(final Dialog dialog, Object data) {
                dialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                dialog.setVisible(true);

                Thread t = new Thread() {
                        @Override
                        public void run() {
                                synchronized (LOCK) {
                                        while (dialog.isVisible()) {
                                                try {
                                                        LOCK.wait();
                                                } catch (InterruptedException e) {
                                                }
                                        }
                                        System.out.println("run_customized_dialog - running "
                                                           + dialog.get_name());
                                }
                        }
                };
                t.start();

                dialog.addWindowListener(new WindowAdapter() {

                        @Override
                        public void windowClosing(WindowEvent arg0) {
                                synchronized (LOCK) {
                                        dialog.setVisible(false);
                                        dialog.notify();
                                }
                        }

                });

                try {
                        t.join();
                } catch (InterruptedException ex) {
                        Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
                }
                return dialog.get_data();
        }

        public static void prompt_critical_error(String title, String prompt) {
                JOptionPane.showMessageDialog(null, prompt, title, JOptionPane.ERROR_MESSAGE);
        }

        public static File run_file_chooser(String file_desc, String extension) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(file_desc, extension);
                chooser.setFileFilter(filter);
                int ret_val = chooser.showOpenDialog(null);
                if (ret_val == JFileChooser.APPROVE_OPTION) {
                        return chooser.getSelectedFile();
                } else {
                        return null;
                }
        }

        public static <T> Set<T> list_to_set(List<T> l) {
                HashSet<T> set = new HashSet<>(l.size());
                for (T e : l) {
                        set.add(e);
                }
                return set;
        }

        public static void advance_progress(TaskMonitor tm, Integer current, Integer total) {
                if (tm != null) {
                        tm.setProgress((double) current / total);
                }
                current++;
        }
}
