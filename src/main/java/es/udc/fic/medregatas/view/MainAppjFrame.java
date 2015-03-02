/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.view;

import es.udc.fic.medregatas.model.regata.Regata;
import es.udc.fic.medregatas.model.service.RegataService;
import es.udc.fic.medregatas.model.tipo.Tipo;
import es.udc.fic.medregatas.util.exceptions.InstanceNotFoundException;
import es.udc.fic.medregatas.view.dialogs.AboutJDialog;
import es.udc.fic.medregatas.view.dialogs.NuevoBarcoJDialog;
import es.udc.fic.medregatas.view.dialogs.RegistrarDatosRegataJDialog;
import es.udc.fic.medregatas.view.dialogs.NuevoTipoJDialog;
import es.udc.fic.medregatas.view.panels.BienvenidaJPanel;
import es.udc.fic.medregatas.view.panels.MostrarClasfJPanel;
import es.udc.fic.medregatas.view.panels.MostrarRegataJPanel;
import es.udc.fic.medregatas.view.panels.MostrarTiposJPanel;
import es.udc.fic.medregatas.view.panels.MostratMangasJPanel;
import es.udc.fic.medregatas.view.panels.PanelEnum;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import javax.swing.JDialog;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author iago
 */
public class MainAppjFrame extends javax.swing.JFrame {

    private final ApplicationContext appContext;

    private final RegataService regataService;

    private final int defaultNumTabs = 4;

    private Regata selectedRegata;

    /**
     * El constructr debe ser vacio para que spring pueda rellenar los atributos
     * con @Autowired, antes de inicializar los componentes
     *
     * @param appContext
     */
    public MainAppjFrame(ApplicationContext appContext) {
        initComponents();

        // Guardamos el contexto de la Aplicación, para acceder y dar acceso, en la
        // interfaz
        this.appContext = appContext;
        regataService = appContext.getBean(RegataService.class);

        //Componente encargado de reescalar el tamaño de las pestañas
        jTabbedPane1.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (selectedRegata != null) {
                    jTabbedPane1.setTitleAt(0, getTabTitle("Datos", defaultNumTabs));
                    jTabbedPane1.setTitleAt(1, getTabTitle("Mangas", defaultNumTabs));
                    jTabbedPane1.setTitleAt(2, getTabTitle("Clasificación", defaultNumTabs));
                    jTabbedPane1.setTitleAt(3, getTabTitle("Clases", defaultNumTabs));
                }
            }

            @Override
            public void componentMoved(ComponentEvent ce) {
            }

            @Override
            public void componentShown(ComponentEvent ce) {
            }

            @Override
            public void componentHidden(ComponentEvent ce) {
            }
        });

        loadEmphyTabs();
    }

    /**
     * Calcula el tamaño de una pestaña
     *
     * @return
     */
    private int calcTabWith(int numTab) {
        int margins = 25;
        int tabWith = (jTabbedPane1.getWidth() - (margins * numTab)) / numTab;
        return tabWith;
    }

    /**
     * Calcula el mensaje html que tendrá la pestaña, y que le da el aspecto
     * visual
     *
     * @param titleMsg
     * @return
     */
    private String getTabTitle(String titleMsg, int numTab) {
        return "<html><body width=" + calcTabWith(numTab) + " topmargin=8 marginwidth"
                + "=15 marginheight=5>" + titleMsg + "</body></html>";
    }

    /**
     * Carga las pestañas, cuando no hay una regata seleccionada, introduciento
     * el mensaje Selecciona/Crea una Regata, en ellas.
     */
    private void loadEmphyTabs() {
        //Añadimos las regatas a la lista
        regatasList.setListData(regataService.getRegatas().toArray());
        jTabbedPane1.removeAll();
        jLabelTitle.setText("Bienvenido a MeD Regatas");
        jTabbedPane1.addTab(getTabTitle("Selecciona/Crea una regata", 2), new BienvenidaJPanel(this));
        jTabbedPane1.addTab(getTabTitle("Clases", 2), new MostrarTiposJPanel(this));
    }

    /**
     * Carga las pestañas cuando hay una regata seleccionada
     *
     * @param regata
     */
    private void loadTabs() {
        regatasList.setListData(regataService.getRegatas().toArray());

        //Añadimos las regatas a la lista
        regatasList.setListData(regataService.getRegatas().toArray());
        jTabbedPane1.removeAll();

        jTabbedPane1.addTab(getTabTitle("Datos", defaultNumTabs), new MostrarRegataJPanel(this));
        jTabbedPane1.addTab(getTabTitle("Mangas", defaultNumTabs), new MostratMangasJPanel(this));
        jTabbedPane1.addTab(getTabTitle("Clasificación", defaultNumTabs),
                new MostrarClasfJPanel(this));
        jTabbedPane1.addTab(getTabTitle("Clases", defaultNumTabs), new MostrarTiposJPanel(this));
    }

    public void refresh() {
        refresh(PanelEnum.DEFAULT);
    }

    public void refresh(PanelEnum panel) {
        
        if(selectedRegata != null) {
            regatasList.setListData(regataService.getRegatas().toArray());
            jTabbedPane1.setComponentAt(0, new MostrarRegataJPanel(this));
            jTabbedPane1.setComponentAt(1, new MostratMangasJPanel(this));
            jTabbedPane1.setComponentAt(2, new MostrarClasfJPanel(this));
            jTabbedPane1.setComponentAt(3, new MostrarTiposJPanel(this));

            //TODO Esto es para el caso de que no haya regata asociada
            try {
                jTabbedPane1.setTitleAt(0, getTabTitle("Datos", defaultNumTabs));
                jTabbedPane1.setTitleAt(1, getTabTitle("Mangas", defaultNumTabs));
                jTabbedPane1.setTitleAt(2, getTabTitle("Clasificación", defaultNumTabs));
                jTabbedPane1.setTitleAt(3, getTabTitle("Clases", defaultNumTabs));
            } catch (Exception e) {


            }
        } else {
            
            loadEmphyTabs();
            
            if(panel.equals(PanelEnum.PANNEL_CLASES)) {
                
                jTabbedPane1.setSelectedIndex(1);
                
            }
            
        }


    }

    public ApplicationContext getAppContext() {
        return appContext;
    }

    public Regata getSelectedRegata() {
        return selectedRegata;
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
        regatasList = new javax.swing.JList();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jLabelTitle = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        archivoJMenu = new javax.swing.JMenu();
        newRegataMenuItem = new javax.swing.JMenuItem();
        newBarcoMenuItem = new javax.swing.JMenuItem();
        NewTipoMenuItem = new javax.swing.JMenuItem();
        editarMenu = new javax.swing.JMenu();
        ayudaMenu = new javax.swing.JMenu();
        AcercaDeMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MeD Regatas");
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(800, 400));

        regatasList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        regatasList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                regatasListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(regatasList);

        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.setToolTipText("");
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jLabelTitle.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabelTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTitle.setText("Bienvenido a MeD Regatas");

        jButton1.setText("Nueva Regata");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        archivoJMenu.setText("Archivo");

        newRegataMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        newRegataMenuItem.setText("Nueva Regata");
        newRegataMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newRegataMenuItemActionPerformed(evt);
            }
        });
        archivoJMenu.add(newRegataMenuItem);

        newBarcoMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        newBarcoMenuItem.setText("Añadir Barco");
        newBarcoMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newBarcoMenuItemActionPerformed(evt);
            }
        });
        archivoJMenu.add(newBarcoMenuItem);

        NewTipoMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        NewTipoMenuItem.setText("Nueva Clase");
        NewTipoMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewTipoMenuItemActionPerformed(evt);
            }
        });
        archivoJMenu.add(NewTipoMenuItem);

        jMenuBar1.add(archivoJMenu);

        editarMenu.setText("Editar");
        jMenuBar1.add(editarMenu);

        ayudaMenu.setText("Ayuda");
        ayudaMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ayudaMenuActionPerformed(evt);
            }
        });

        AcercaDeMenuItem.setText("Acerca de ");
        AcercaDeMenuItem.setToolTipText("");
        AcercaDeMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AcercaDeMenuItemActionPerformed(evt);
            }
        });
        ayudaMenu.add(AcercaDeMenuItem);

        jMenuBar1.add(ayudaMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 549, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTabbedPane1)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelTitle)
                        .addGap(3, 3, 3)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void regatasListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_regatasListMouseClicked
        
        if(evt.getButton() == MouseEvent.BUTTON1) {
            selectedRegata = (Regata) regatasList.getSelectedValue();
            jLabelTitle.setText(selectedRegata.toString());
            loadTabs();
        }
    }//GEN-LAST:event_regatasListMouseClicked

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged

        if (selectedRegata != null) {
            switch (jTabbedPane1.getSelectedIndex()) {
                case 0:
                    jTabbedPane1.setComponentAt(0, new MostrarRegataJPanel(this));
                    break;
                case 1:
                    jTabbedPane1.setComponentAt(1, new MostratMangasJPanel(this));
                    break;
                case 2:
                    jTabbedPane1.setComponentAt(2, new MostrarClasfJPanel(this));
                    break;
                case 3:
                    jTabbedPane1.setComponentAt(3, new MostrarTiposJPanel(this));
                    break;
            }

            //TODO Esto es para el caso de que no haya regata asociada
            try {
                jTabbedPane1.setTitleAt(0, getTabTitle("Datos", defaultNumTabs));
                jTabbedPane1.setTitleAt(1, getTabTitle("Mangas", defaultNumTabs));
                jTabbedPane1.setTitleAt(2, getTabTitle("Clasificación", defaultNumTabs));
                jTabbedPane1.setTitleAt(3, getTabTitle("Clases", defaultNumTabs));
            } catch (Exception e) {

            }
        }
     }//GEN-LAST:event_jTabbedPane1StateChanged

    private void ayudaMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ayudaMenuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ayudaMenuActionPerformed

    private void AcercaDeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AcercaDeMenuItemActionPerformed

        JDialog dialog = new AboutJDialog(this, true);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

    }//GEN-LAST:event_AcercaDeMenuItemActionPerformed

    private void newRegataMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newRegataMenuItemActionPerformed

        JDialog dialog = new RegistrarDatosRegataJDialog(this, null);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        regatasList.setListData(regataService.getRegatas().toArray());
    }//GEN-LAST:event_newRegataMenuItemActionPerformed

    private void NewTipoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewTipoMenuItemActionPerformed
        Tipo aEditar = null;
        JDialog dialog = new NuevoTipoJDialog(this, true, aEditar);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

    }//GEN-LAST:event_NewTipoMenuItemActionPerformed

    private void newBarcoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newBarcoMenuItemActionPerformed

        JDialog dialog = new NuevoBarcoJDialog(this, true);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }//GEN-LAST:event_newBarcoMenuItemActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JDialog dialog = new RegistrarDatosRegataJDialog(this, null);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        regatasList.setListData(regataService.getRegatas().toArray());
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem AcercaDeMenuItem;
    private javax.swing.JMenuItem NewTipoMenuItem;
    private javax.swing.JMenu archivoJMenu;
    private javax.swing.JMenu ayudaMenu;
    private javax.swing.JMenu editarMenu;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JMenuItem newBarcoMenuItem;
    private javax.swing.JMenuItem newRegataMenuItem;
    private javax.swing.JList regatasList;
    // End of variables declaration//GEN-END:variables

}
