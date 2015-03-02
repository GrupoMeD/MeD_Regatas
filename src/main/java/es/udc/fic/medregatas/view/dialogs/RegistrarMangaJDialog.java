/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.view.dialogs;

import es.udc.fic.medregatas.model.barco.Barco;
import es.udc.fic.medregatas.model.inscripcion.Inscripcion;
import es.udc.fic.medregatas.model.manga.Manga;
import es.udc.fic.medregatas.model.posicion.Posicion;
import es.udc.fic.medregatas.model.regata.Regata;
import es.udc.fic.medregatas.model.service.InscripcionService;
import es.udc.fic.medregatas.model.service.MangaService;
import es.udc.fic.medregatas.model.service.RegataService;
import es.udc.fic.medregatas.model.tipo.Tipo;
import es.udc.fic.medregatas.util.TimeUtil;
import es.udc.fic.medregatas.util.exceptions.EmptyTextException;
import es.udc.fic.medregatas.util.exceptions.InvalidMilesException;
import es.udc.fic.medregatas.util.exceptions.InvalidTimeException;
import es.udc.fic.medregatas.util.exceptions.MangaVaciaException;
import es.udc.fic.medregatas.view.MainAppjFrame;
import es.udc.fic.medregatas.view.panels.PanelEnum;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Adrián
 */
public class RegistrarMangaJDialog extends javax.swing.JDialog {

    private final int PANNEL_MANGAS_INDEX = 1;
    private final int DEFAULT_TIPO_INDEX = 0;

    private final MainAppjFrame parent;

    private final RegataService regataService;
    private final InscripcionService inscripcionService;
    private final MangaService mangaService;

    private Regata regata;
    private Manga manga;
    private List<Posicion.Penalizacion> penalizaciones;
    private List<Tipo> tipos;

    private Map<Tipo, List<Posicion>> posicionesByTipo = new HashMap<>();
    private Component frame;

    /**
     * Creates new form CrearMangaJDialog
     */
    public RegistrarMangaJDialog(MainAppjFrame parent, boolean modal, Manga mangaAEditar) {
        super(parent, modal);
        initComponents();

        this.parent = parent;
        regataService = parent.getAppContext().getBean(RegataService.class);
        inscripcionService = parent.getAppContext().getBean(InscripcionService.class);
        mangaService = parent.getAppContext().getBean(MangaService.class);

        //establecemos la regata
        this.regata = parent.getSelectedRegata();

        
        //Inicializamos el hashmap con los tipos y sus listas asociadas
        tipos = regataService.getTiposAsociadosByRegata(regata);
        for (Tipo t : tipos) {
            posicionesByTipo.put(t, new ArrayList<Posicion>());
        }

        /**
         * Añadimos la manga y sus atributos(manga,millas,barcos,fecha) Crear
         * Manga ->mangaAEditar = null;Millas = 0;tabla vacia Editar Manga ->
         * Millas = mangaAEditar.getMillas(),completarHashmap con manga
         */
        if (mangaAEditar != null) {

            this.manga = mangaAEditar;

            jTextMillas.setText(String.valueOf(manga.getMillas()));

            for (Posicion p : manga.getPosiciones()) {
                System.out.println("\n VELA:" + p.getBarco().getVela() + "TIPO: " + p.getBarco().getTipo().toString() + "\n");
                Tipo tipo = p.getBarco().getTipo();
                List<Posicion> posTipo = posicionesByTipo.get(tipo);
                posTipo.add(p);
            }
            //Ver lo del nomrbe del boton
            CloseButton.setName("Guardar cambios");

            jCalendar1.setCalendar(manga.getFecha());

        } else {

            this.manga = new Manga(Calendar.getInstance(), this.regata, new ArrayList<Posicion>(), 0);

            jTextMillas.setText("0");

        }

        //añadimos las penalizaciones
        penalizaciones = regataService.getPenalizaciones();
        for (Posicion.Penalizacion p : penalizaciones) {
            jComboBoxPenalizacion.addItem(p.name());

        }

        //Cargamos los valores iniciales del comboBox
        rellenarTiposComboBox();
        jComboBoxTipos.setSelectedIndex(DEFAULT_TIPO_INDEX);

        //Rellenamos la tabla y el selector de Barco, que dependen del tipo seleccionado
        Tipo defaultTipo = tipos.get(DEFAULT_TIPO_INDEX);
        rellenarBarcosComboBox(defaultTipo);
        jTable1.setModel(generarTableModel(defaultTipo));

    }

    private void rellenarTiposComboBox() {

        if (jComboBoxTipos.getItemCount() > 0) {
            jComboBoxTipos.removeAllItems();
        }

        for (Tipo t : tipos) {
            jComboBoxTipos.addItem(t);
        }

    }

    private void rellenarBarcosComboBox(Tipo tipo) {
        //Obtenemos todas las inscripciones en funcion del tipo seleccionado
        List<Inscripcion> inscr = inscripcionService.getInscripcionesByTipo(regata,
                tipo);

        //Cogemos aquellos barcos para los cuales ya hemos registrado una posicion
        List<Barco> barcosRegistrados = new ArrayList<>();
        for (Posicion p : posicionesByTipo.get(tipo)) {
            barcosRegistrados.add(p.getBarco());
        }

        //Borramos los ya existentes
        jComboBoxVela.removeAllItems();

        //los insertamos en el combobox si no han sido registrados
        for (Inscripcion i : inscr) {
            if (!barcosRegistrados.contains(i.getBarco())) {
                jComboBoxVela.addItem(i.getBarco());
            }
        }

        //Actualizamos el botton de registrar
        if (jComboBoxVela.getItemCount() == 0) {
            RegisterButton.setEnabled(false);
        } else {
            RegisterButton.setEnabled(true);
        }

        uptadeTableButtonsState();
    }

    private TableModel generarTableModel(Tipo tipo) {
        //Obtenemos todas las posiciones en funcion del tipo seleccionado
        List<Posicion> poss = posicionesByTipo.get(tipo);

        DefaultTableModel tableModel = new DefaultTableModel();

        //Generamos la tabla adecuada al tipo de barco que queremos mostrar
        if (tipo.getCompiteTmpReal()) {

            tableModel.addColumn("Vela");
            tableModel.addColumn("Nombre");
            tableModel.addColumn("Penalización");
        } else {
            tableModel.addColumn("Vela");
            tableModel.addColumn("Nombre");
            tableModel.addColumn("Tiempo");
            tableModel.addColumn("Penalización");
        }
        //Insertamos los datos
        for (Posicion p : poss) {
            Object[] elementos;
            if (tipo.getCompiteTmpReal()) {
                elementos = new Object[]{p.getBarco().getVela(),
                    p.getBarco().getNombre(), p.getPenal().toString()};
            } else {

                elementos = new Object[]{p.getBarco().getVela(),
                    p.getBarco().getNombre(), TimeUtil.conversorSecToHMinSec(p.getSegTiempo()),
                    p.getPenal().toString()};
            }
            tableModel.addRow(elementos);
        }
        return tableModel;
    }

    /**
     * Actualiza el estado de los botones que hay al lado de la tabla y que
     * permiten hacer que una fila suba descienda o sea eliminada
     */
    public void uptadeTableButtonsState() {
        int selectedRow = jTable1.getSelectedRow();

        Tipo tipo = null;
        if (jComboBoxTipos.getSelectedIndex() != -1) {
            tipo = (Tipo) jComboBoxTipos.getSelectedItem();
        }

        //Up Button
        if (selectedRow == 0 || selectedRow == -1 || tipo == null
                || !tipo.getCompiteTmpReal()) {
            upButton.setEnabled(false);
        } else {
            upButton.setEnabled(true);

        }

        //Down Button
        if (selectedRow == jTable1.getRowCount() - 1 || selectedRow == -1
                || tipo == null || !tipo.getCompiteTmpReal()) {
            downButton.setEnabled(false);
        } else {
            downButton.setEnabled(true);
        }

        //Delete Button
        if (selectedRow == -1) {
            deleteButton.setEnabled(false);
        } else {
            deleteButton.setEnabled(true);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        RegisterButton = new javax.swing.JButton();
        CloseButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTextTiempoH = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jComboBoxPenalizacion = new javax.swing.JComboBox();
        jComboBoxVela = new javax.swing.JComboBox();
        jTextTiempoPenalizacionH = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextMillas = new javax.swing.JTextField();
        jTextTiempoM = new javax.swing.JTextField();
        jTextTiempoS = new javax.swing.JTextField();
        jTextTiempoPenalizacionM = new javax.swing.JTextField();
        jTextTiempoPenalizacionS = new javax.swing.JTextField();
        upButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        jCalendar1 = new com.toedter.calendar.JCalendar();
        jLabel7 = new javax.swing.JLabel();
        jComboBoxTipos = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Registrar nueva manga");
        setLocationByPlatform(true);

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Vela", "Barco", "Tiempo"
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
        jTable1.setDragEnabled(true);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setText("Vela");

        RegisterButton.setText("Registrar Llegada");
        RegisterButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RegisterButtonMouseClicked(evt);
            }
        });

        CloseButton.setText("Cerrar Manga");
        CloseButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CloseButtonMouseClicked(evt);
            }
        });

        jLabel2.setText("Tiempo (HH:MM:SS)");

        jTextTiempoH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextTiempoHActionPerformed(evt);
            }
        });

        jLabel3.setText("Fecha de la Manga");

        jLabel4.setText("Penalización");

        jComboBoxPenalizacion.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxPenalizacionItemStateChanged(evt);
            }
        });

        jComboBoxVela.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxVelaItemStateChanged(evt);
            }
        });

        jLabel5.setText("Penalizacion(HH:MM:SS)");

        jLabel6.setText("Nº millas:");

        jTextMillas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextMillasActionPerformed(evt);
            }
        });

        upButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/arrowup.gif"))); // NOI18N
        upButton.setToolTipText("");
        upButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                upButtonMouseClicked(evt);
            }
        });

        downButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/arrowdown.gif"))); // NOI18N
        downButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                downButtonMouseClicked(evt);
            }
        });

        deleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/remove.png"))); // NOI18N
        deleteButton.setToolTipText("");
        deleteButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteButtonMouseClicked(evt);
            }
        });

        jCalendar1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel7.setText("Clase");

        jComboBoxTipos.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxTipos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTiposActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(CloseButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(upButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(downButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextTiempoH, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jTextTiempoM, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jTextTiempoS, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jComboBoxTipos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(107, 107, 107)
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextMillas, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(0, 92, Short.MAX_VALUE)
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jComboBoxVela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel4)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jComboBoxPenalizacion, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(RegisterButton))
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                    .addComponent(jTextTiempoPenalizacionH, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jTextTiempoPenalizacionM, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jTextTiempoPenalizacionS, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(jLabel5))))))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCalendar1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6)
                    .addComponent(jTextMillas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addComponent(jCalendar1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBoxVela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jComboBoxPenalizacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RegisterButton)
                    .addComponent(jLabel7)
                    .addComponent(jComboBoxTipos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextTiempoH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextTiempoM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextTiempoS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextTiempoPenalizacionH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextTiempoPenalizacionM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextTiempoPenalizacionS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(upButton, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(downButton, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CloseButton)
                .addGap(18, 18, 18))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void RegisterButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RegisterButtonMouseClicked

        try {
            if ((jComboBoxPenalizacion.getSelectedIndex() == -1) || !RegisterButton.isEnabled()) {
                return;
            }

            Barco b = (Barco) jComboBoxVela.getSelectedItem();
            Tipo tipo = (Tipo) jComboBoxTipos.getSelectedItem();
            Posicion.Penalizacion penal = penalizaciones.get(jComboBoxPenalizacion
                    .getSelectedIndex());

            long tiempoPenalizacion = 0;
            //en el caso de que sean penalizaciones de tiempo
            if (!penal.isMaxPointsPenal()) {

                tiempoPenalizacion = TimeUtil.conversorStringToSeg(
                        jTextTiempoPenalizacionH.getText(),
                        jTextTiempoPenalizacionM.getText(),
                        jTextTiempoPenalizacionS.getText());

            }

            /**
             * Comprobar que los campos de tiempo no estan vacios si el barco
             * compite el tiempo computado
             */
            if (jTextTiempoH.getText().isEmpty() || jTextTiempoM.getText().isEmpty()
                    || jTextTiempoS.getText().isEmpty()) {
                throw new EmptyTextException(jTextTiempoH);
            }
            long time = TimeUtil.conversorStringToSeg(
                    jTextTiempoH.getText(),
                    jTextTiempoM.getText(),
                    jTextTiempoS.getText());

            Posicion p = new Posicion(time, penal, manga, b, tiempoPenalizacion);

            //Insertamos la posicion en el array con las otras posiciones de su tipo
            List<Posicion> pos = posicionesByTipo.get(tipo);
            pos.add(p);

            jTable1.setModel(generarTableModel(tipo));
            //Se rellena de nuevo el conbobox, donde ya no estará el dato insertado
            rellenarBarcosComboBox(tipo);
        } catch (EmptyTextException e) {
            JOptionPane.showMessageDialog(frame,
                    "El tiempo no puede estar vacío",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_RegisterButtonMouseClicked

    private void CloseButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CloseButtonMouseClicked

        try {
            int millas = Integer.valueOf(jTextMillas.getText());
            
            if(millas<= 0)
                throw new InvalidMilesException(manga);
            
            boolean mangaVacia = true;
            
            manga.setMillas(millas);
            manga.setFecha(jCalendar1.getCalendar());
            manga.setPosiciones(new ArrayList<Posicion>());

            /**
             * Si ninguna de las clases tiene clasificado ningun barco se
             * muestra una ventana de error
             */
            for (Entry<Tipo, List<Posicion>> entry : posicionesByTipo.entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    mangaVacia = false;
                }
            }
            if (mangaVacia) {
                throw new MangaVaciaException(posicionesByTipo);
            }

            //Añadimos las posiciones
            for (Entry<Tipo, List<Posicion>> entry : posicionesByTipo.entrySet()) {

                if (entry.getKey().getCompiteTmpReal()) {
                    for (int i = 0; i < entry.getValue().size(); i++) {
                        Posicion p = entry.getValue().get(i);
                        p.setSegTiempo(new Long(i));
                        manga.getPosiciones().add(p);

                    }
                } else {
                    for (int i = 0; i < entry.getValue().size(); i++) {
                        Posicion p = entry.getValue().get(i);
               
                        manga.getPosiciones().add(p);
                        //if((p.getSegTiempo() <= 0))
                                //throw new InvalidTimeException(p);
                    }

                }
            }

            mangaService.cerrarYGuardarManga(manga);
            parent.refresh(PanelEnum.PANNEL_MANGAS);

            setVisible(false);
            dispose();
            
        } /*catch (InvalidTimeException e) {

            JOptionPane.showMessageDialog(frame,
                    "El tiempo no puede ser 0",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

        }*/
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame,
                    "El valor del número de millas no es válido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (MangaVaciaException e1) {
            JOptionPane.showMessageDialog(frame,
                    "La manga no puede estar vacía",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (InvalidMilesException ex) {
            JOptionPane.showMessageDialog(frame,
                    "Las manga debe tener un numero de millas mayor que 0",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_CloseButtonMouseClicked

    private void jComboBoxVelaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxVelaItemStateChanged

        if ((jComboBoxVela.getSelectedIndex() == -1)
                || ((Barco) jComboBoxVela.getSelectedItem()).getTipo().getCompiteTmpReal()) {
            //Si no hay barco seleccionado, o este no compite en tiempo real
            jTextTiempoH.setText("0");
            jTextTiempoH.setVisible(false);
            jTextTiempoH.setEditable(false);

            jTextTiempoM.setText("0");
            jTextTiempoM.setVisible(false);
            jTextTiempoM.setEditable(false);

            jTextTiempoS.setText("0");
            jTextTiempoS.setVisible(false);
            jTextTiempoS.setEditable(false);

            jLabel2.setVisible(false);

        } else {

            jTextTiempoH.setText("0");
            jTextTiempoH.setVisible(true);
            jTextTiempoH.setEditable(true);

            jTextTiempoM.setText("0");
            jTextTiempoM.setVisible(true);
            jTextTiempoM.setEditable(true);

            jTextTiempoS.setText("0");
            jTextTiempoS.setVisible(true);
            jTextTiempoS.setEditable(true);

            jLabel2.setVisible(true);

        }

    }//GEN-LAST:event_jComboBoxVelaItemStateChanged

    private void jComboBoxPenalizacionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxPenalizacionItemStateChanged

        if (penalizaciones.get(jComboBoxPenalizacion.getSelectedIndex()).isMaxPointsPenal()
                || penalizaciones.get(jComboBoxPenalizacion.getSelectedIndex()) == Posicion.Penalizacion.NAN) {
            //En el caso de no ser una penalizacion de tiempo

            jLabel5.setVisible(false);

            jTextTiempoPenalizacionH.setVisible(false);
            jTextTiempoPenalizacionH.setText("0");

            jTextTiempoPenalizacionM.setVisible(false);
            jTextTiempoPenalizacionM.setText("0");

            jTextTiempoPenalizacionS.setVisible(false);
            jTextTiempoPenalizacionS.setText("0");

        } else {
            //En el caso de ser una penalizacion de gorda

            jLabel5.setVisible(true);

            jTextTiempoPenalizacionH.setVisible(true);
            jTextTiempoPenalizacionH.setText("0");

            jTextTiempoPenalizacionM.setVisible(true);
            jTextTiempoPenalizacionM.setText("0");

            jTextTiempoPenalizacionS.setVisible(true);
            jTextTiempoPenalizacionS.setText("0");

        }

    }//GEN-LAST:event_jComboBoxPenalizacionItemStateChanged

    private void jTextMillasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextMillasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextMillasActionPerformed

    private void jTextTiempoHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextTiempoHActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextTiempoHActionPerformed

    private void deleteButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteButtonMouseClicked
        if (jTable1.getSelectedRow() == -1) {
            return;
        }

        if (jComboBoxTipos.getSelectedIndex() == -1) {
            return;
        }
        Tipo tipo = (Tipo) jComboBoxTipos.getSelectedItem();

        Posicion posABorrar = posicionesByTipo.get(tipo)
                .remove(jTable1.getSelectedRow());

        if (posABorrar.getIdPosicion() != null) {
            mangaService.borrarPosicion(posABorrar);
        }
        jTable1.setModel(generarTableModel(tipo));
        rellenarBarcosComboBox(tipo);

    }//GEN-LAST:event_deleteButtonMouseClicked

    private void upButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_upButtonMouseClicked

        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow == -1 || !upButton.isEnabled()) {
            return;
        }

        if (jComboBoxTipos.getSelectedIndex() == -1) {
            return;
        }

        Tipo tipo = (Tipo) jComboBoxTipos.getSelectedItem();
        List<Posicion> poss = posicionesByTipo.get(tipo);
        //Cambiamos de posicion
        Collections.rotate(poss.subList(selectedRow - 1, selectedRow + 1), -1);

        jTable1.setModel(generarTableModel(tipo));
        jTable1.setRowSelectionInterval(selectedRow - 1, selectedRow - 1);
        uptadeTableButtonsState();
    }//GEN-LAST:event_upButtonMouseClicked

    private void downButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_downButtonMouseClicked

        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow == -1 || !downButton.isEnabled()) {
            return;
        }

        if (jComboBoxTipos.getSelectedIndex() == -1) {
            return;
        }

        Tipo tipo = (Tipo) jComboBoxTipos.getSelectedItem();
        List<Posicion> poss = posicionesByTipo.get(tipo);
        //Cambiamos de posicion
        Collections.rotate(poss.subList(selectedRow, selectedRow + 2), -1);

        jTable1.setModel(generarTableModel(tipo));
        jTable1.setRowSelectionInterval(selectedRow + 1, selectedRow + 1);
        uptadeTableButtonsState();

    }//GEN-LAST:event_downButtonMouseClicked

    private void jComboBoxTiposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTiposActionPerformed

        if (jComboBoxTipos.getSelectedIndex() == -1) {
            return;
        }

        //Recargamos la tabla y el comboBox
        Tipo tipo = (Tipo) jComboBoxTipos.getSelectedItem();
        jTable1.setModel(generarTableModel(tipo));
        rellenarBarcosComboBox(tipo);
    }//GEN-LAST:event_jComboBoxTiposActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

        uptadeTableButtonsState();
    }//GEN-LAST:event_jTable1MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CloseButton;
    private javax.swing.JButton RegisterButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton downButton;
    private com.toedter.calendar.JCalendar jCalendar1;
    private javax.swing.JComboBox jComboBoxPenalizacion;
    private javax.swing.JComboBox jComboBoxTipos;
    private javax.swing.JComboBox jComboBoxVela;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextMillas;
    private javax.swing.JTextField jTextTiempoH;
    private javax.swing.JTextField jTextTiempoM;
    private javax.swing.JTextField jTextTiempoPenalizacionH;
    private javax.swing.JTextField jTextTiempoPenalizacionM;
    private javax.swing.JTextField jTextTiempoPenalizacionS;
    private javax.swing.JTextField jTextTiempoS;
    private javax.swing.JButton upButton;
    // End of variables declaration//GEN-END:variables

}
