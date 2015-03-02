/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.view.panels;

import es.udc.fic.medregatas.model.barco.Barco;
import es.udc.fic.medregatas.model.inscripcion.Inscripcion;
import es.udc.fic.medregatas.model.posicion.Posicion;
import es.udc.fic.medregatas.model.regata.Regata;
import es.udc.fic.medregatas.model.service.InscripcionService;
import es.udc.fic.medregatas.model.service.RegataService;
import es.udc.fic.medregatas.model.tipo.Tipo;
import es.udc.fic.medregatas.util.PDFUtils;
import es.udc.fic.medregatas.view.MainAppjFrame;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import org.apache.pdfbox.exceptions.COSVisitorException;

/**
 *
 * @author iago
 */
public class MostrarClasfJPanel extends javax.swing.JPanel {

    private MainAppjFrame parent;

    private final RegataService regataService;
    private final InscripcionService inscripcionService;

    private Regata regata;
    private List<Tipo> tipos;
    DefaultTableModel model;
    private final String DIA_FINAL = "Final";
    private final String TIPO_NO_SELECCIONADO = "Tipo No Seleccionado";

    /**
     * Creates new form NewJPanel
     *
     * @param regata
     */
    public MostrarClasfJPanel(MainAppjFrame parent) {
        initComponents();

        this.parent = parent;
        this.regataService = parent.getAppContext().getBean(RegataService.class);
        this.inscripcionService = parent.getAppContext().getBean(InscripcionService.class);
        this.regata = parent.getSelectedRegata();

        this.model = (DefaultTableModel) jTable1.getModel();
        //Obtenemos los datos del modelo
        this.tipos = regataService.getTiposAsociadosByRegata(regata);

        //Calculamos los dias a mostrar y los insertamos en el ComboBox de Dia
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        for (Calendar c : regata.getDiasManga()) {
            Dia.addItem(dateFormater.format(c.getTime()));
        }

        Dia.addItem(DIA_FINAL);
        Dia.setSelectedItem(DIA_FINAL);

        //Insertamos los tipos en el comboBox de Categoria
        for (Tipo tipo : tipos) {
            Categoria.addItem(tipo.getNombre());
        }
        Categoria.addItem(TIPO_NO_SELECCIONADO);
        Categoria.setSelectedItem(TIPO_NO_SELECCIONADO);

        jTable1.setModel(rellenarTabla());
    }

    private DefaultTableModel rellenarTabla() {
        List<List<Posicion>> listaBarcosByManga = new ArrayList<>();
        DefaultTableModel myModel = new DefaultTableModel();

        int offsetDia = Dia.getSelectedIndex();
        int offsetTipo = Categoria.getSelectedIndex();

        if (Categoria.getSelectedItem() != TIPO_NO_SELECCIONADO) {

            Calendar diaSeleccionado;
            Tipo tipoSeleccionado = tipos.get(offsetTipo);

            boolean todosDias = (offsetDia == -1 || offsetDia == Dia.getModel().getSize() - 1);

            //TODO Adaptar esto al nuevo formato
            if (todosDias) {
                listaBarcosByManga = regataService.getClasificacion(regata, null, tipoSeleccionado);
            } else {
                diaSeleccionado = regata.getDiasManga().get(offsetDia);
                listaBarcosByManga = regataService.getClasificacion(regata, diaSeleccionado, tipoSeleccionado);
            }
        }
        //Si tenemos resultados los mostramos
        if ((!listaBarcosByManga.isEmpty()) && (!listaBarcosByManga.get(0).isEmpty())) {

            myModel.addColumn("Vela");
            myModel.addColumn("Patrón");
            myModel.addColumn("Barco");
            myModel.addColumn("PuntosTotales");

            List<Inscripcion> inscripciones = inscripcionService.getInscripciones(regata);

            for (List<Posicion> posBarco : listaBarcosByManga) {

                int puntTotal = 0;
                for (Posicion p : posBarco) {
                    puntTotal += p.getPuntos();
                }
                Barco barco = posBarco.get(0).getBarco();
                String patron = null;
                //buscamos el barco en la inscripcion para obtener el patron
                for (Inscripcion i : inscripciones) {
                    if (barco.equals(i.getBarco())) {
                        patron = i.getPatron();
                    }
                }
                Object[] elementos = {"ESP " + barco.getVela(), patron,
                    barco.getNombre(), puntTotal};
                myModel.addRow(elementos);
            }

            // Recorremos las mangas creando una columna de datos para cada una
            for (int mangaI = 0; mangaI < listaBarcosByManga.get(0).size(); mangaI++) {

                List puntsMangaI = new ArrayList(jTable1.getRowCount());

                // Añadimos por cada barco, una entrada en la lista de puntos de la
                // manga.
                for (List<Posicion> barcoPossList : listaBarcosByManga) {
                    puntsMangaI.add(barcoPossList.get(mangaI).getPuntos() + " "
                            + barcoPossList.get(mangaI).getPenal().toString());
                }

                // Append a new column with copied data
                myModel.addColumn("Manga " + (mangaI + 1), puntsMangaI.toArray());

            }
        }

        return myModel;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        Dia = new javax.swing.JComboBox();
        Categoria = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Vela", "Patrón", "Barco", "Puntuacion Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        Dia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DiaActionPerformed(evt);
            }
        });

        Categoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CategoriaActionPerformed(evt);
            }
        });

        jButton2.setText("Ver Resultados");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setText("Generar PDF");
        jButton1.setToolTipText("");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(Dia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Categoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 716, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Dia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Categoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void DiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DiaActionPerformed

    }//GEN-LAST:event_DiaActionPerformed

    private void CategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CategoriaActionPerformed

    }//GEN-LAST:event_CategoriaActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        model.setRowCount(0);
        jTable1.setModel(rellenarTabla());
        if (Categoria.getSelectedItem().toString().contains(TIPO_NO_SELECCIONADO)) {
            jButton1.setEnabled(false);
        } else {
            jButton1.setEnabled(true);

        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String subtitulo = "Día: " + Dia.getSelectedItem().toString() + ", Tipo: "
                + Categoria.getSelectedItem().toString();

        try {
            PDFUtils.printPDFTable("Clasificación - "
                    + regata.getNombre(), jTable1.getModel(), "Clasificación - "
                    + regata.getNombre(), subtitulo + ".pdf");
        } catch (IOException ex) {
            Logger.getLogger(MostrarClasfJPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (COSVisitorException ex) {
            Logger.getLogger(MostrarClasfJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox Categoria;
    private javax.swing.JComboBox Dia;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
