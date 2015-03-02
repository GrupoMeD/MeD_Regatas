/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.view.dialogs;

import es.udc.fic.medregatas.model.barco.Barco;
import es.udc.fic.medregatas.model.inscripcion.Inscripcion;
import es.udc.fic.medregatas.model.regata.Regata;
import es.udc.fic.medregatas.model.service.InscripcionService;
import es.udc.fic.medregatas.model.service.RegataService;
import es.udc.fic.medregatas.model.tipo.Tipo;
import es.udc.fic.medregatas.util.exceptions.EmptyTextException;
import es.udc.fic.medregatas.util.exceptions.InstanceNotFoundException;
import es.udc.fic.medregatas.view.MainAppjFrame;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Adrián
 */
public class RegistrarBarcosRegataJDialog extends javax.swing.JDialog {

    private static String TODOS_TIPOS = "Todos";

    private MainAppjFrame parent;
    private Component frame;

    private RegataService regataService;
    private InscripcionService inscripcionService;
    private Regata regata;

    private Map<Tipo, List<Barco>> barcosDisponibles;
    private List<Inscripcion> inscripciones = new ArrayList<>();
    private DefaultTableModel barcosModel;
    private final List<Tipo> tiposAsociados;

    /**
     * Creates new form CrearRegataJDialog
     */
    public RegistrarBarcosRegataJDialog(MainAppjFrame parent, Regata regata,
            List<Tipo> tiposAsociados) {
        super(parent, true);
        initComponents();

        this.parent = parent;
        this.regata = regata;
        this.tiposAsociados = tiposAsociados;

        this.regataService = parent.getAppContext().getBean(RegataService.class);
        this.inscripcionService = parent.getAppContext().getBean(InscripcionService.class);

        //Solo en caso de que se esté editando
        //Borramos las inscripciones de tipos no Asociados
        inscripciones = inscripcionService.getInscripciones(regata);
        for (Inscripcion i : inscripcionService.getInscripciones(regata)) {
            if (!tiposAsociados.contains(i.getBarco().getTipo())) {
                inscripciones.remove(i);
            }
        }

        cargarBarcosDisponibles();

        rellenarTiposComboBox();
        tiposComboBox1.setSelectedIndex(0);

        barcosModel = (DefaultTableModel) BarcosTable.getModel();
        BarcosTable.setModel(rellenarBarcosTable());

        rellenarBarcosComboBox();
        rellenarBarcosTable();
    }

    private void cargarBarcosDisponibles() {

        //Guardamos todos los barcos del sistema de todos los tipos en un Hasmap
        // según su tipo
        List<Barco> barcosSistema = inscripcionService.findAllBarcos();
        barcosDisponibles = new HashMap<Tipo, List<Barco>>();

        for (Barco b : barcosSistema) {
            //Si el tipo es uno de los que deseamos emplear
            if (tiposAsociados.contains(b.getTipo())) {
                // En caso de que no hayamos creado su entrada en el hashmap la creamos
                if (!barcosDisponibles.containsKey(b.getTipo())) {
                    barcosDisponibles.put(b.getTipo(), new ArrayList<Barco>());
                }
                // Lo insertamos en el hashmap
                barcosDisponibles.get(b.getTipo()).add(b);
            }
        }

        // borramos los barcos ya inscritos, y obtenemos los datos de las
        // inscripciones ya persistidas
        for (Barco b : inscripcionService.findBarcosByRegata(regata)) {
            if (barcosDisponibles.containsKey(b.getTipo())) {
                List<Barco> barcosDelTipo = barcosDisponibles.get(b.getTipo());
                if (barcosDelTipo.contains(b)) {
                    barcosDelTipo.remove(b);
                }
            }
        }

    }

    private void rellenarTiposComboBox() {

        tiposComboBox1.addItem(TODOS_TIPOS);
        for (Tipo t : barcosDisponibles.keySet()) {
            tiposComboBox1.addItem(t);
        }
    }

    private void rellenarBarcosComboBox() {

        if (tiposComboBox1.getSelectedIndex() == -1) {
            return;
        }
        BarcosComboBox.removeAllItems();
        
        if (tiposComboBox1.getSelectedItem().equals(TODOS_TIPOS)) {
            for (List<Barco> barcos : barcosDisponibles.values()) {
                for (Barco b : barcos) {
                    BarcosComboBox.addItem(b);
                }
            }
        } else {
            Tipo tipoSeleccionado = (Tipo) tiposComboBox1.getSelectedItem();

            for (Barco b : barcosDisponibles.get(tipoSeleccionado)) {
                BarcosComboBox.addItem(b);
            }
        }

    }

    private DefaultTableModel rellenarBarcosTable() {

        barcosModel.setRowCount(0);

        for (Inscripcion i : inscripciones) {
            Object[] o = {i.getBarco().getVela(), i.getBarco().getNombre(), i.getPatron()};
            barcosModel.addRow(o);

        }

        return barcosModel;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        AcceptButton = new javax.swing.JButton();
        CancelButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        BarcosTable = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        BarcosComboBox = new javax.swing.JComboBox();
        nuevoBarcoButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        InscribirButton = new javax.swing.JButton();
        DeleteButton = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldPatron = new javax.swing.JTextField();
        AtrasjButton = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        tiposComboBox1 = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Crear Regata");
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(600, 300));

        AcceptButton.setText("Finalizar");
        AcceptButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AcceptButtonMouseClicked(evt);
            }
        });

        CancelButton.setText("Cancelar");
        CancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CancelButtonMouseClicked(evt);
            }
        });

        BarcosTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Vela", "Nombre", "Patron"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
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
        jScrollPane2.setViewportView(BarcosTable);

        jLabel3.setText("Barcos");

        nuevoBarcoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icon-plus-small.png"))); // NOI18N
        nuevoBarcoButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                nuevoBarcoButtonMouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel4.setText("Número de Vela");

        InscribirButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/ok.png"))); // NOI18N
        InscribirButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                InscribirButtonMouseClicked(evt);
            }
        });
        InscribirButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InscribirButtonActionPerformed(evt);
            }
        });

        DeleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/remove.png"))); // NOI18N
        DeleteButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DeleteButtonMouseClicked(evt);
            }
        });

        jLabel7.setText("Borrar barco selecc.");

        jLabel8.setText("Crear barco");

        jLabel9.setText("Inscribir barco");

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel5.setText("Patrón");

        jTextFieldPatron.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N

        AtrasjButton.setText("Atrás");
        AtrasjButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AtrasjButtonActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel6.setText("Clase de Barco");

        tiposComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tiposComboBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(CancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(AtrasjButton)
                        .addGap(18, 18, 18)
                        .addComponent(AcceptButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addGap(0, 41, Short.MAX_VALUE))
                                    .addComponent(tiposComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(BarcosComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(0, 16, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(68, 68, 68))
                                    .addComponent(jTextFieldPatron)))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(62, 62, 62)
                                .addComponent(InscribirButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(DeleteButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addGap(19, 19, 19))
                                    .addComponent(nuevoBarcoButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING))))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nuevoBarcoButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(DeleteButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(5, 5, 5)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldPatron)
                        .addComponent(BarcosComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tiposComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(InscribirButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AcceptButton)
                    .addComponent(CancelButton)
                    .addComponent(AtrasjButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CancelButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CancelButtonMouseClicked
        setVisible(false);
        dispose();
    }//GEN-LAST:event_CancelButtonMouseClicked

    private void AcceptButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AcceptButtonMouseClicked
        try {
            if (inscripciones.isEmpty()) {
                throw new EmptyTextException(regata);
            }

            try {
                //Si la regata no existe en BD
                regata = regataService.editarInfoRegata(regata);
            } catch (InstanceNotFoundException ex) {
                regata = regataService.crearRegata(regata.getNombre(),
                        regata.getDescripcion());
            }

            for (Inscripcion i : inscripcionService.getInscripciones(regata)) {
                try {
                    inscripcionService.borrarInscripcion(i);
                } catch (InstanceNotFoundException ex) {
                    Logger.getLogger(RegistrarBarcosRegataJDialog.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            }

            for (Inscripcion i : inscripciones) {
                inscripcionService.inscribir(regata, i.getBarco(), i.getPatron());
            }

            setVisible(false);
            dispose();
            parent.refresh();
        } catch (EmptyTextException e) {
            JOptionPane.showMessageDialog(frame,
                    "Debe haber al menos un barco inscrito en la regata",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_AcceptButtonMouseClicked

    private void nuevoBarcoButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nuevoBarcoButtonMouseClicked
        JDialog dialog = new NuevoBarcoJDialog(parent, true); //GESTIONAR DESPUES CON SPRING
        dialog.setVisible(true);
        cargarBarcosDisponibles();
        rellenarBarcosComboBox();

    }//GEN-LAST:event_nuevoBarcoButtonMouseClicked

    private void InscribirButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_InscribirButtonMouseClicked

        if (BarcosComboBox.getSelectedItem() == null) {
            return;
        }

        // Creamos las incripciones de forma parcial
        String patron = jTextFieldPatron.getText();
        Barco b = (Barco) BarcosComboBox.getSelectedItem();
        inscripciones.add(new Inscripcion(null, b, patron));

        //Borramos el barco de entre los disponibles
        barcosDisponibles.get(b.getTipo()).remove(b);

        rellenarBarcosComboBox();
        jTextFieldPatron.setText("");
        BarcosTable.setModel(rellenarBarcosTable());

    }//GEN-LAST:event_InscribirButtonMouseClicked

    private void DeleteButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DeleteButtonMouseClicked
        if (BarcosTable.getSelectedRow() == -1) {
            return;
        }

        Inscripcion i = inscripciones.get(BarcosTable.getSelectedRow());
        //Añadimos la inscripcion a los barcos disponibles
        barcosDisponibles.get(i.getBarco().getTipo()).add(i.getBarco());
        //La borramos de entre las inscritas
        inscripciones.remove(i);

        rellenarBarcosTable();
        rellenarBarcosComboBox();

    }//GEN-LAST:event_DeleteButtonMouseClicked

    private void InscribirButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InscribirButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_InscribirButtonActionPerformed

    private void AtrasjButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AtrasjButtonActionPerformed

        setVisible(false);
        dispose();

        JDialog dialog = new RegistrarTiposRegataJDialog(parent, regata,
                tiposAsociados);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

    }//GEN-LAST:event_AtrasjButtonActionPerformed

    private void tiposComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tiposComboBox1ActionPerformed
        rellenarBarcosComboBox();
    }//GEN-LAST:event_tiposComboBox1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AcceptButton;
    private javax.swing.JButton AtrasjButton;
    private javax.swing.JComboBox BarcosComboBox;
    private javax.swing.JTable BarcosTable;
    private javax.swing.JButton CancelButton;
    private javax.swing.JButton DeleteButton;
    private javax.swing.JButton InscribirButton;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextFieldPatron;
    private javax.swing.JButton nuevoBarcoButton;
    private javax.swing.JComboBox tiposComboBox1;
    // End of variables declaration//GEN-END:variables
}
