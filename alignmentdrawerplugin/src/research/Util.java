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

/**
 * Utility class
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class Util {
        private static final int               c_MaxBuffering = 16384;
        
        public static String extract_string_from_stream(InputStream source) throws IOException {
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
        
        public static String[] extract_lines_from_stream(InputStream source) throws IOException {
                String text = extract_string_from_stream(source);
                return text.split("\n");
        }
}
