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
package alignmentplugintest;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 *
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class Alignmentplugintest {

        /**
         * @param args the command line arguments
         * @throws java.io.FileNotFoundException
         */
        public static void main(String[] args) throws FileNotFoundException, Exception {
                ArrayList<research.Test> test_list = new ArrayList<>();
                //test_list.add(new research.TestGWLoader("yeast.gw"));
                test_list.add(new research.TestCustomDialog());

                for (research.Test test : test_list) {
                        if (!test.test()) {
                                System.out.println("Test case: " + test.name() + " doesn't pass, with reasons: " + test.failure_reason());
                        }
                }
        }

}
