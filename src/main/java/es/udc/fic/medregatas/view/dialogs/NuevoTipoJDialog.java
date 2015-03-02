/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.view.dialogs;

import es.udc.fic.medregatas.model.service.RegataService;
import es.udc.fic.medregatas.model.tipo.Tipo;
import es.udc.fic.medregatas.util.exceptions.DuplicateInstanceException;
import es.udc.fic.medregatas.util.exceptions.EmptyTextException;
import es.udc.fic.medregatas.view.MainAppjFrame;
import es.udc.fic.medregatas.view.panels.PanelEnum;
import java.awt.Component;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Abosan
 */
public class NuevoTipoJDialog extends javax.swing.JDialog {

    private MainAppjFrame parent;

    private RegataService regataService;
    private Component frame;
    private Tipo tipo;

    /**
     * Creates new form RegistrarTipoJDialog
     *
     * @param parent
     * @param modal
     * @param tipo
     */
    public NuevoTipoJDialog(MainAppjFrame parent, boolean modal, Tipo tipo) {
        super(parent, modal);
        initComponents();
        this.parent = parent;
        this.regataService = parent.getAppContext().getBean(RegataService.class);
        this.tipo = tipo;
        if (tipo != null) {
            nombreTipoTextField.setText(tipo.getNombre());
            DescTextArea.setText(tipo.getDescripcion());
            CTRCheckBox.setSelected(tipo.getCompiteTmpReal());
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

        nombreTipoLabel = new javax.swing.JLabel();
        descTipoLabel = new javax.swing.JLabel();
        CompTRealLabel = new javax.swing.JLabel();
        nombreTipoTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        DescTextArea = new javax.swing.JTextArea();
        CrearButton = new javax.swing.JButton();
        CancelarButton = new javax.swing.JButton();
        CTRCheckBox = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Crear clase");

        nombreTipoLabel.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        nombreTipoLabel.setText("Nombre de la Clase:");

        descTipoLabel.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        descTipoLabel.setText("Descripción:");

        CompTRealLabel.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        CompTRealLabel.setText("Compite en tiempo real :");

        nombreTipoTextField.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        DescTextArea.setColumns(20);
        DescTextArea.setRows(5);
        DescTextArea.setAutoscrolls(false);
        jScrollPane1.setViewportView(DescTextArea);

        CrearButton.setText("Guardar");
        CrearButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CrearButtonMouseClicked(evt);
            }
        });

        CancelarButton.setText("Cancelar");
        CancelarButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CancelarButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(CancelarButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(CrearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(descTipoLabel)
                            .addComponent(nombreTipoLabel))
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nombreTipoTextField)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(CompTRealLabel)
                        .addGap(18, 18, 18)
                        .addComponent(CTRCheckBox)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nombreTipoLabel)
                    .addComponent(nombreTipoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(descTipoLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CTRCheckBox)
                    .addComponent(CompTRealLabel))
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CrearButton)
                    .addComponent(CancelarButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CrearButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CrearButtonMouseClicked

        try {
            List<Tipo> tipos = regataService.getTipos();

            String nombreTipo = nombreTipoTextField.getText();
            boolean existe = false;

            if (nombreTipo.isEmpty()) {
                throw new EmptyTextException(nombreTipo);
            }

            /**
             * Si es para editar un tipo se actualiza
             */
            if (tipo != null) {
                Tipo nuevo = new Tipo(nombreTipoTextField.getText(), DescTextArea.getText(), CTRCheckBox.isSelected());
                regataService.updateTipo(tipo.getIdTipo(), nuevo);
                setVisible(false);
                dispose();
            } else {
                /**
                 * En otro caso, se comprueba que no exista ya el nombre y se
                 * crea
                 */
                for (Tipo t : tipos) {
                    if (t.getNombre().equalsIgnoreCase(nombreTipo)) {
                        existe = true;
                        break;
                    }
                }

                if (!existe) {
                    regataService.añadirTipo(nombreTipoTextField.getText(), DescTextArea.getText(), CTRCheckBox.isSelected());
                    setVisible(false);
                    dispose();
                } else {
                    throw new DuplicateInstanceException(evt, nombreTipo);
                }
            }
            parent.refresh(PanelEnum.PANNEL_CLASES);

        } catch (EmptyTextException e) {
            JOptionPane.showMessageDialog(frame,
                    "El nombre de la clase no puede estar vacía",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (DuplicateInstanceException e) {
            JOptionPane.showMessageDialog(frame,
                    "El nombre de la clase ya existe",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_CrearButtonMouseClicked

    private void CancelarButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CancelarButtonMouseClicked

        setVisible(false);
        dispose();
    }//GEN-LAST:event_CancelarButtonMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox CTRCheckBox;
    private javax.swing.JButton CancelarButton;
    private javax.swing.JLabel CompTRealLabel;
    private javax.swing.JButton CrearButton;
    private javax.swing.JTextArea DescTextArea;
    private javax.swing.JLabel descTipoLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel nombreTipoLabel;
    private javax.swing.JTextField nombreTipoTextField;
    // End of variables declaration//GEN-END:variables
}
