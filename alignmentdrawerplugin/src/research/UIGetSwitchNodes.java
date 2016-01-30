/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package research;

/**
 * GUI to prompt for customizing alignment switch.
 * @author davis
 */
public class UIGetSwitchNodes extends javax.swing.JPanel {

        /**
         * Creates new form UIGetSwitchNodes
         */
        public UIGetSwitchNodes() {
                initComponents();
                
        }

        /**
         * This method is called from within the constructor to initialize the
         * form. WARNING: Do NOT modify this code. The content of this method is
         * always regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                buttonGroup1 = new javax.swing.ButtonGroup();
                btn_confirm = new javax.swing.JButton();
                cb_is_2switch = new javax.swing.JCheckBox();
                jScrollPane1 = new javax.swing.JScrollPane();
                txt_g0_input = new javax.swing.JTextArea();
                jScrollPane2 = new javax.swing.JScrollPane();
                txt_g1_input = new javax.swing.JTextArea();
                jLabel2 = new javax.swing.JLabel();
                jLabel1 = new javax.swing.JLabel();
                rb_switch_customized = new javax.swing.JRadioButton();
                rb_switch_aligned = new javax.swing.JRadioButton();

                btn_confirm.setText("btn_confirm");

                cb_is_2switch.setSelected(true);
                cb_is_2switch.setText("cb_is_2switch");

                txt_g0_input.setColumns(20);
                txt_g0_input.setRows(5);
                jScrollPane1.setViewportView(txt_g0_input);

                txt_g1_input.setColumns(20);
                txt_g1_input.setRows(5);
                jScrollPane2.setViewportView(txt_g1_input);

                jLabel2.setText("g1_nodes");

                jLabel1.setText("g0_nodes");

                buttonGroup1.add(rb_switch_customized);
                rb_switch_customized.setText("rb_switch_customized");

                buttonGroup1.add(rb_switch_aligned);
                rb_switch_aligned.setSelected(true);
                rb_switch_aligned.setText("rb_switch_aligned");
                rb_switch_aligned.setToolTipText("");

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel1)
                                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel2)
                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                        .addComponent(jScrollPane2)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(rb_switch_aligned)
                                                        .addComponent(rb_switch_customized))
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(cb_is_2switch)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btn_confirm, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(rb_switch_aligned)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rb_switch_customized)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
                                        .addComponent(jScrollPane1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cb_is_2switch)
                                        .addComponent(btn_confirm))
                                .addContainerGap())
                );
        }// </editor-fold>//GEN-END:initComponents


        // Variables declaration - do not modify//GEN-BEGIN:variables
        public javax.swing.JButton btn_confirm;
        private javax.swing.ButtonGroup buttonGroup1;
        public javax.swing.JCheckBox cb_is_2switch;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane2;
        public javax.swing.JRadioButton rb_switch_aligned;
        public javax.swing.JRadioButton rb_switch_customized;
        public javax.swing.JTextArea txt_g0_input;
        public javax.swing.JTextArea txt_g1_input;
        // End of variables declaration//GEN-END:variables
}
