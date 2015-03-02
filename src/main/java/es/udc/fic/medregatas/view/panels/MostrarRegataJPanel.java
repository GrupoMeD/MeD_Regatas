/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.view.panels;

import es.udc.fic.medregatas.view.dialogs.ConfirmJDialog;
import es.udc.fic.medregatas.model.inscripcion.Inscripcion;
import es.udc.fic.medregatas.model.regata.Regata;
import es.udc.fic.medregatas.model.service.InscripcionService;
import es.udc.fic.medregatas.view.MainAppjFrame;
import es.udc.fic.medregatas.view.dialogs.RegistrarDatosRegataJDialog;
import java.text.SimpleDateFormat;
import javax.swing.JDialog;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author iago
 */
public class MostrarRegataJPanel extends javax.swing.JPanel {

    private MainAppjFrame parent;

    private InscripcionService inscService;

    private Regata regata;

    /**
     * Creates new form NewJPanel
     */
    public MostrarRegataJPanel(MainAppjFrame parent) {
        initComponents();

        this.parent = parent;

        this.regata = parent.getSelectedRegata();
        this.inscService = parent.getAppContext().getBean(InscripcionService.class);
        
        DescripcionJTextArea.setText(regata.getDescripcion());

        if (regata.getMangas().isEmpty()) {
            diasjLabel.setText("La regata no tiene mangas asociadas");
            editarjButton.setVisible(true);
        } else {
            SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
            diasjLabel.setText(dateFormater.format(regata.getDiaIni().getTime())
                    + " - " + dateFormater.format(regata.getDiaFin().getTime()));
            editarjButton.setVisible(false);

        }
        rellenarTablaBarcos();

    }

    private void rellenarTablaBarcos() {

        DefaultTableModel barcosModel = (DefaultTableModel) jTable2.getModel();
        
        barcosModel.setRowCount(0);

        for (Inscripcion i : inscService.getInscripciones(regata)) {
            Object[] o = {i.getBarco().getNombre(), i.getBarco().getVela(), i.getPatron()};
            barcosModel.addRow(o);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        diasjLabel = new javax.swing.JLabel();
        editarjButton = new javax.swing.JButton();
        jButtonRemove = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        BarcosInscritosJLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        DescripcionJTextArea = new javax.swing.JTextArea();

        jLabel1.setText("Descripción:");

        jLabel3.setText("Dias:");

        diasjLabel.setText("diasjLabel");
        diasjLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        editarjButton.setText("Editar");
        editarjButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarjButtonActionPerformed(evt);
            }
        });

        jButtonRemove.setText("Eliminar");
        jButtonRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveActionPerformed(evt);
            }
        });

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Num. Vela", "Patrón"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable2);

        BarcosInscritosJLabel.setText("Barcos Inscritos:");

        DescripcionJTextArea.setEditable(false);
        DescripcionJTextArea.setColumns(20);
        DescripcionJTextArea.setLineWrap(true);
        DescripcionJTextArea.setRows(5);
        DescripcionJTextArea.setWrapStyleWord(true);
        DescripcionJTextArea.setCursor(null);
        DescripcionJTextArea.setFocusable(false);
        DescripcionJTextArea.setOpaque(false);
        jScrollPane2.setViewportView(DescripcionJTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(editarjButton)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonRemove)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(diasjLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(59, 59, 59)
                                .addComponent(BarcosInscritosJLabel)
                                .addContainerGap(68, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(BarcosInscritosJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(diasjLabel)
                        .addGap(0, 73, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editarjButton)
                    .addComponent(jButtonRemove))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void editarjButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarjButtonActionPerformed

        JDialog dialog = new RegistrarDatosRegataJDialog(parent, regata);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }//GEN-LAST:event_editarjButtonActionPerformed

    private void jButtonRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveActionPerformed
        // TODO add your handling code here:
        //borar regata,inscripciones y mangas asociadas.
        JDialog dialog = new ConfirmJDialog(parent, true, regata);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

    }//GEN-LAST:event_jButtonRemoveActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BarcosInscritosJLabel;
    private javax.swing.JTextArea DescripcionJTextArea;
    private javax.swing.JLabel diasjLabel;
    private javax.swing.JButton editarjButton;
    private javax.swing.JButton jButtonRemove;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables
}