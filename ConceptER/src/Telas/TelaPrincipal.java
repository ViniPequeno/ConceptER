/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Telas;

import Utils.PersistenciaGrafica;
import com.mxgraph.util.mxCellRenderer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author User
 */
public class TelaPrincipal extends javax.swing.JFrame {

    PanelPrincipal panel;

    /**
     * Creates new form TelaPrincipal
     */
    public TelaPrincipal() {
        initComponents();
        setTitle("ConceptER");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        mItemSalvar.setEnabled(false);
        mItemSalvarComo.setEnabled(false);
        mItemSalvarTudo.setEnabled(false);
        mItemExportaImagem.setEnabled(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                sair();
            }
        });
        mItemZoom();
        carregarAtalhos(this);
        setVisible(true);
        JOptionPane.showMessageDialog(null, "A geração dos Scripts SQL através do MER é apenas uma facilidade. "
                + "Porém, o projeto de banco de dados requer as três visões bem definidas e desenhadas");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        menuBar = new javax.swing.JMenuBar();
        menuArquivo = new javax.swing.JMenu();
        mItemNovo = new javax.swing.JMenuItem();
        mItemAbrir = new javax.swing.JMenuItem();
        mItemSalvar = new javax.swing.JMenuItem();
        mItemSalvarComo = new javax.swing.JMenuItem();
        mItemSalvarTudo = new javax.swing.JMenuItem();
        separator1 = new javax.swing.JPopupMenu.Separator();
        mItemExportaImagem = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        exportaSGBD = new javax.swing.JMenuItem();
        salvarScripts = new javax.swing.JMenuItem();
        separator3 = new javax.swing.JPopupMenu.Separator();
        mItemSair = new javax.swing.JMenuItem();
        menuEditar = new javax.swing.JMenu();
        mItemCopiar = new javax.swing.JMenuItem();
        mItemColar = new javax.swing.JMenuItem();
        mItemDesfazer = new javax.swing.JMenuItem();
        mItemRefazer = new javax.swing.JMenuItem();
        menuExibir = new javax.swing.JMenu();
        mItemMaisZoom = new javax.swing.JMenuItem();
        mItemMenosZoom = new javax.swing.JMenuItem();
        menuNavegar = new javax.swing.JMenu();
        mItemProximaGuia = new javax.swing.JMenuItem();
        mItemGuiaAnterior = new javax.swing.JMenuItem();
        menuSobre = new javax.swing.JMenu();

        jMenuItem1.setText("jMenuItem1");

        jMenuItem2.setText("jMenuItem2");

        jMenuItem3.setText("jMenuItem3");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height-100);

        menuArquivo.setText("Arquivo");

        mItemNovo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mItemNovo.setText("Novo");
        mItemNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItemNovoActionPerformed(evt);
            }
        });
        menuArquivo.add(mItemNovo);

        mItemAbrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        mItemAbrir.setText("Abrir...");
        mItemAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItemAbrirActionPerformed(evt);
            }
        });
        menuArquivo.add(mItemAbrir);

        mItemSalvar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mItemSalvar.setText("Salvar");
        mItemSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItemSalvarActionPerformed(evt);
            }
        });
        menuArquivo.add(mItemSalvar);

        mItemSalvarComo.setText("Salvar como...");
        mItemSalvarComo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItemSalvarComoActionPerformed(evt);
            }
        });
        menuArquivo.add(mItemSalvarComo);

        mItemSalvarTudo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mItemSalvarTudo.setText("Salvar tudo");
        mItemSalvarTudo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItemSalvarTudoActionPerformed(evt);
            }
        });
        menuArquivo.add(mItemSalvarTudo);
        menuArquivo.add(separator1);

        mItemExportaImagem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        mItemExportaImagem.setText("Exporta para imagem");
        mItemExportaImagem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItemExportaImagemActionPerformed(evt);
            }
        });
        menuArquivo.add(mItemExportaImagem);

        jMenu1.setText("Scripts SQL");

        exportaSGBD.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        exportaSGBD.setText("Exporta para um SGBD");
        exportaSGBD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportaSGBDActionPerformed(evt);
            }
        });
        jMenu1.add(exportaSGBD);

        salvarScripts.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        salvarScripts.setText("Salvar Scripts");
        salvarScripts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salvarScriptsActionPerformed(evt);
            }
        });
        jMenu1.add(salvarScripts);

        menuArquivo.add(jMenu1);
        menuArquivo.add(separator3);

        mItemSair.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        mItemSair.setText("Sair");
        mItemSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItemSairActionPerformed(evt);
            }
        });
        menuArquivo.add(mItemSair);

        menuBar.add(menuArquivo);

        menuEditar.setText("Editar");
        menuEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEditarActionPerformed(evt);
            }
        });

        mItemCopiar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        mItemCopiar.setText("Copiar");
        mItemCopiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItemCopiarActionPerformed(evt);
            }
        });
        menuEditar.add(mItemCopiar);

        mItemColar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        mItemColar.setText("Colar");
        mItemColar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItemColarActionPerformed(evt);
            }
        });
        menuEditar.add(mItemColar);

        mItemDesfazer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        mItemDesfazer.setText("Desfazer");
        mItemDesfazer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItemDesfazerActionPerformed(evt);
            }
        });
        menuEditar.add(mItemDesfazer);

        mItemRefazer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        mItemRefazer.setText("Refazer");
        mItemRefazer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItemRefazerActionPerformed(evt);
            }
        });
        menuEditar.add(mItemRefazer);

        menuBar.add(menuEditar);

        menuExibir.setText("Exibir");
        menuExibir.setEnabled(false);

        mItemMaisZoom.setText("Mais zoom");
        menuExibir.add(mItemMaisZoom);

        mItemMenosZoom.setText("Menos zoom");
        menuExibir.add(mItemMenosZoom);

        menuBar.add(menuExibir);

        menuNavegar.setText("Navegar");

        mItemProximaGuia.setText("Próxima Guia");
        menuNavegar.add(mItemProximaGuia);

        mItemGuiaAnterior.setText("Guia Anteior");
        menuNavegar.add(mItemGuiaAnterior);

        menuBar.add(menuNavegar);

        menuSobre.setText("Sobre");
        menuBar.add(menuSobre);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 550, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 377, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void mItemNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItemNovoActionPerformed
        if (panel == null) {
            panel = new PanelPrincipal(getWidth(), getHeight(), this);
            add(panel);
            panel.setBounds(0, 0, getWidth(), getHeight());
            revalidate();
            menuExibir.setEnabled(true);
        } else {
            mItemSalvarTudo.setEnabled(true);
            panel.addTab();
            panel.tabs.setSelectedIndex(panel.tabs.getTabCount() - 1);
        }
    }//GEN-LAST:event_mItemNovoActionPerformed

    private void mItemSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItemSalvarActionPerformed
        if (panel.getFile(panel.tabSelecionada) == null) {
            JFileChooser jfc = new JFileChooser();
            if (jfc.showSaveDialog(mItemSalvar) == JFileChooser.APPROVE_OPTION) {
                panel.setFile(jfc.getSelectedFile(), panel.tabSelecionada);
                PersistenciaGrafica.saveGraph(panel.getAba(panel.tabSelecionada), panel.getFile(panel.tabSelecionada).getPath());
                setTitle("ConceptER - " + panel.getFile(panel.tabSelecionada).getName() + " \\ " + panel.getFile(panel.tabSelecionada).getPath());
                panel.tabs.setTitleAt(panel.tabSelecionada, panel.getFile(panel.tabSelecionada).getName());
                panel.tabs.setFont(new Font(panel.tabs.getFont().getFontName(), Font.PLAIN, panel.tabs.getFont().getSize()));
                panel.setNomes(panel.getFile(panel.tabSelecionada).getName(), panel.tabSelecionada);
            }
        } else {
            PersistenciaGrafica.saveGraph(panel.getAba(panel.tabSelecionada), panel.getFile(panel.tabSelecionada).getPath());
        }

    }//GEN-LAST:event_mItemSalvarActionPerformed

    private void mItemSalvarComoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItemSalvarComoActionPerformed
        JFileChooser jfc = new JFileChooser();
        if (jfc.showSaveDialog(mItemSalvarComo) == JFileChooser.APPROVE_OPTION) {
            panel.setFile(jfc.getSelectedFile(), panel.tabSelecionada);

            PersistenciaGrafica.saveGraph(panel.getAba(panel.tabSelecionada), panel.getFile(panel.tabSelecionada).getPath());
            panel.tabs.setTitleAt(panel.tabSelecionada, panel.getFile(panel.tabSelecionada).getName());
            panel.setNomes(panel.getFile(panel.tabSelecionada).getName(), panel.tabSelecionada);
        }
    }//GEN-LAST:event_mItemSalvarComoActionPerformed

    private void sair() {
        if (panel != null) {
            for (int i = 0; i < panel.tabs.getTabCount(); i++) {
                if (panel.tabs.getTitleAt(i).contains("*")) {

                    int resp = JOptionPane.showConfirmDialog(null, "Deseja salvar " + panel.getNomes(i) + "?", "Sair", JOptionPane.YES_NO_CANCEL_OPTION);
                    switch (resp) {
                        case JOptionPane.YES_OPTION:
                            if (panel.getFile(i) == null) {
                                JFileChooser jfc = new JFileChooser();
                                
                                if (jfc.showSaveDialog(mItemSalvar) == JFileChooser.APPROVE_OPTION) {
                                    panel.setFile(jfc.getSelectedFile(), i);
                                    PersistenciaGrafica.saveGraph(panel.getAba(i), panel.getFile(i).getPath());
                                    panel.tabs.setTitleAt(panel.tabSelecionada, panel.getFile(panel.tabSelecionada).getName());
                                }
                            }   if (panel.getFile(i) != null) {
                                PersistenciaGrafica.saveGraph(panel.getAba(i), panel.getFile(i).getPath());
                            }   break;
                        case JOptionPane.NO_OPTION:
                            continue;
                        case JOptionPane.CANCEL_OPTION:
                            return;
                        default:
                            break;
                    }
                }
            }
            System.exit(0);
        } else {
            System.exit(0);
        }
    }
    private void mItemSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItemSairActionPerformed
        sair();
    }//GEN-LAST:event_mItemSairActionPerformed

    private void mItemExportaImagemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItemExportaImagemActionPerformed
        JFileChooser jfc = new JFileChooser();

        jfc.addChoosableFileFilter(new FileNameExtensionFilter("PNG", ".png"));
        if (jfc.showSaveDialog(menuBar) == JFileChooser.APPROVE_OPTION) {
            BufferedImage image = mxCellRenderer.createBufferedImage(panel.getGrafico(panel.tabSelecionada), null, 1, Color.WHITE, true, null);
            try {

                ImageIO.write(image, "PNG", new File(jfc.getSelectedFile().getPath() + ".png"));
            } catch (IOException ex) {
                Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_mItemExportaImagemActionPerformed

    private void mItemAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItemAbrirActionPerformed
        JFileChooser jfc = new JFileChooser();

        if (panel == null) {
            if (jfc.showOpenDialog(mItemAbrir) == JFileChooser.APPROVE_OPTION) {
                panel = new PanelPrincipal(getWidth(), getHeight(), this);
                add(panel);
                panel.setBounds(0, 0, getWidth(), getHeight());
                revalidate();
                PersistenciaGrafica.loadGraph(panel.getAreaGraficas().get(panel.tabs.getTabCount() - 1), jfc.getSelectedFile().getPath());
                panel.tabs.setSelectedIndex(panel.tabs.getTabCount() - 1);
                panel.setFile(jfc.getSelectedFile(), panel.tabSelecionada);
                panel.atualizarAreaGrafica(0);
                panel.tabs.setSelectedIndex(panel.tabs.getTabCount() - 1);
                panel.setFile(jfc.getSelectedFile(), panel.tabSelecionada);
            }
        } else {
            if (jfc.showOpenDialog(mItemAbrir) == JFileChooser.APPROVE_OPTION) {
                panel.addTab();
                PersistenciaGrafica.loadGraph(panel.getAreaGraficas().get(panel.tabs.getTabCount() - 1), jfc.getSelectedFile().getPath());
                panel.tabs.setSelectedIndex(panel.tabs.getTabCount() - 1);
                panel.setFile(jfc.getSelectedFile(), panel.tabSelecionada);
                panel.atualizarAreaGrafica(0);
                panel.tabs.setTitleAt(panel.tabs.getSelectedIndex(), panel.getFile(panel.tabSelecionada).getName());
                panel.setNomes(panel.getFile(panel.tabSelecionada).getName(), panel.tabSelecionada);

            }
        }

    }//GEN-LAST:event_mItemAbrirActionPerformed

    private void menuEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEditarActionPerformed

    }//GEN-LAST:event_menuEditarActionPerformed

    private void mItemDesfazerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItemDesfazerActionPerformed
        panel.desfazer();
    }//GEN-LAST:event_mItemDesfazerActionPerformed

    private void mItemRefazerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItemRefazerActionPerformed
        // TODO add your handling code here:
        panel.refazer();
    }//GEN-LAST:event_mItemRefazerActionPerformed

    private void mItemCopiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItemCopiarActionPerformed
        panel.copiar();
    }//GEN-LAST:event_mItemCopiarActionPerformed

    private void mItemColarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItemColarActionPerformed
        panel.colar(panel.px, panel.py);
    }//GEN-LAST:event_mItemColarActionPerformed

    private void mItemSalvarTudoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItemSalvarTudoActionPerformed
        for (int i = 0; i < panel.tabs.getTabCount(); i++) {
            if (panel.getFile(panel.tabSelecionada) == null) {
                JFileChooser jfc = new JFileChooser();
                if (jfc.showSaveDialog(mItemSalvar) == JFileChooser.APPROVE_OPTION) {
                    panel.setFile(jfc.getSelectedFile(), panel.tabSelecionada);
                    PersistenciaGrafica.saveGraph(panel.getAba(panel.tabSelecionada), panel.getFile(panel.tabSelecionada).getPath());
                    setTitle("ConceptER - " + panel.getFile(panel.tabSelecionada).getName() + " \\ " + panel.getFile(panel.tabSelecionada).getPath());
                    panel.tabs.setTitleAt(panel.tabSelecionada, panel.getFile(panel.tabSelecionada).getName());
                    panel.tabs.setFont(new Font(panel.tabs.getFont().getFontName(), Font.PLAIN, panel.tabs.getFont().getSize()));
                    panel.setNomes(panel.getFile(panel.tabSelecionada).getName(), panel.tabSelecionada);
                }
            } else {
                PersistenciaGrafica.saveGraph(panel.getAba(panel.tabSelecionada), panel.getFile(panel.tabSelecionada).getPath());
            }
        }
    }//GEN-LAST:event_mItemSalvarTudoActionPerformed

    private void exportaSGBDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportaSGBDActionPerformed
        TelaExportaScripts tes = new TelaExportaScripts(panel);
        tes.setVisible(true);
    }//GEN-LAST:event_exportaSGBDActionPerformed

    private void salvarScriptsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salvarScriptsActionPerformed
        TelaSalvarScripts tss = new TelaSalvarScripts(panel);
        tss.setVisible(true);
    }//GEN-LAST:event_salvarScriptsActionPerformed

    private void mItemZoom() {
        menuExibir.remove(mItemMaisZoom);
        Action zoomInAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                panel.maisZoom();
            }
        };
        if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
            zoomInAction.putValue(AbstractAction.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, KeyEvent.META_DOWN_MASK));
        } else {
            zoomInAction.putValue(AbstractAction.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, KeyEvent.CTRL_DOWN_MASK));
        }

        mItemMaisZoom = new JMenuItem(zoomInAction);
        mItemMaisZoom.setText("Mais zoom");
        menuExibir.add(mItemMaisZoom);

        menuExibir.remove(mItemMenosZoom);
        Action zoomOutAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                panel.menosZoom();
            }
        };
        if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
            zoomOutAction.putValue(AbstractAction.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, KeyEvent.META_DOWN_MASK));
        } else {
            zoomOutAction.putValue(AbstractAction.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, KeyEvent.CTRL_DOWN_MASK));
        }

        mItemMenosZoom = new JMenuItem(zoomOutAction);
        mItemMenosZoom.setText("Menos zoom");
        menuExibir.add(mItemMenosZoom);

    }

    private void carregarAtalhos(TelaPrincipal frame) {
        //Botão Novo Arquivo
        menuArquivo.remove(mItemNovo);
        Action novoAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (panel == null) {
                    panel = new PanelPrincipal(getWidth(), getHeight(), frame);
                    add(panel);
                    panel.setBounds(0, 0, getWidth(), getHeight());
                    revalidate();
                    menuExibir.setEnabled(true);
                } else {
                    mItemSalvarTudo.setEnabled(true);
                    panel.addTab();
                    panel.tabs.setSelectedIndex(panel.tabs.getTabCount() - 1);
                }
            }
        };
        if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
            novoAction.putValue(AbstractAction.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.META_DOWN_MASK));
        } else {
            novoAction.putValue(AbstractAction.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        }
        mItemNovo = new JMenuItem(novoAction);
        mItemNovo.setText("Novo");
        menuArquivo.add(mItemNovo, 0);

        //Botão Abrir Arquivo
        menuArquivo.remove(mItemAbrir);
        Action abrirAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser jfc = new JFileChooser();

                if (panel == null) {
                    if (jfc.showOpenDialog(mItemAbrir) == JFileChooser.APPROVE_OPTION) {
                        panel = new PanelPrincipal(getWidth(), getHeight(), frame);
                        add(panel);
                        panel.setBounds(0, 0, getWidth(), getHeight());
                        revalidate();
                        PersistenciaGrafica.loadGraph(panel.getAreaGraficas().get(panel.tabs.getTabCount() - 1), jfc.getSelectedFile().getPath());
                        panel.tabs.setSelectedIndex(panel.tabs.getTabCount() - 1);
                        panel.setFile(jfc.getSelectedFile(), panel.tabSelecionada);
                        panel.atualizarAreaGrafica(0);
                        panel.tabs.setTitleAt(panel.tabs.getSelectedIndex(), panel.getFile(panel.tabSelecionada).getName());
                        panel.setNomes(panel.getFile(panel.tabSelecionada).getName(), panel.tabSelecionada);
                    }
                } else {
                    if (jfc.showOpenDialog(mItemAbrir) == JFileChooser.APPROVE_OPTION) {
                        panel.addTab();
                        PersistenciaGrafica.loadGraph(panel.getAreaGraficas().get(panel.tabs.getTabCount() - 1), jfc.getSelectedFile().getPath());
                        panel.tabs.setSelectedIndex(panel.tabs.getTabCount() - 1);
                        panel.setFile(jfc.getSelectedFile(), panel.tabSelecionada);
                        panel.atualizarAreaGrafica(0);
                        panel.tabs.setSelectedIndex(panel.tabs.getTabCount() - 1);
                        panel.setFile(jfc.getSelectedFile(), panel.tabSelecionada);
                        panel.tabs.setTitleAt(panel.tabs.getSelectedIndex(), panel.getFile(panel.tabSelecionada).getName());
                        panel.setNomes(panel.getFile(panel.tabSelecionada).getName(), panel.tabSelecionada);

                    }
                }
            }
        };
        if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
            abrirAction.putValue(AbstractAction.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.META_DOWN_MASK));
        } else {
            abrirAction.putValue(AbstractAction.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        }
        mItemAbrir = new JMenuItem(abrirAction);
        mItemAbrir.setText("Abrir");
        menuArquivo.add(mItemAbrir, 1);

        //Botão Salvar Arquivo
        menuArquivo.remove(mItemSalvar);
        Action salvarAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (panel != null) {
                    if (panel.getFile(panel.tabSelecionada) == null) {
                        JFileChooser jfc = new JFileChooser();
                        if (jfc.showSaveDialog(mItemSalvar) == JFileChooser.APPROVE_OPTION) {
                            panel.setFile(jfc.getSelectedFile(), panel.tabSelecionada);
                            PersistenciaGrafica.saveGraph(panel.getAba(panel.tabSelecionada), panel.getFile(panel.tabSelecionada).getPath());
                            setTitle("ConceptER - " + panel.getFile(panel.tabSelecionada).getName() + " \\ " + panel.getFile(panel.tabSelecionada).getPath());
                            panel.tabs.setTitleAt(panel.tabSelecionada, panel.getFile(panel.tabSelecionada).getName());
                            panel.getAba(panel.tabSelecionada).setFont(new Font(panel.tabs.getFont().getFontName(), Font.PLAIN, panel.tabs.getFont().getSize()));
                            panel.setNomes(panel.getFile(panel.tabSelecionada).getName(), panel.tabSelecionada);
                        }
                    } else {
                        PersistenciaGrafica.saveGraph(panel.getAba(panel.tabSelecionada), panel.getFile(panel.tabSelecionada).getPath());
                        panel.getAba(panel.tabSelecionada).setFont(new Font(panel.tabs.getFont().getFontName(), Font.PLAIN, panel.tabs.getFont().getSize()));
                        panel.tabs.setTitleAt(panel.tabSelecionada, panel.getFile(panel.tabSelecionada).getName());
                    }
                }
            }
        };
        if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
            salvarAction.putValue(AbstractAction.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.META_DOWN_MASK));
        } else {
            salvarAction.putValue(AbstractAction.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        }
        mItemSalvar = new JMenuItem(salvarAction);
        mItemSalvar.setText("Salvar");
        menuArquivo.add(mItemSalvar, 2);

        //Botão Copiar
        menuEditar.remove(mItemCopiar);
        Action copiarAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                panel.copiar();
            }
        };
        if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
            copiarAction.putValue(AbstractAction.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.META_DOWN_MASK));
        } else {
            copiarAction.putValue(AbstractAction.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        }
        mItemCopiar = new JMenuItem(copiarAction);
        mItemCopiar.setText("Copiar");
        menuEditar.add(mItemCopiar, 0);

        //Botão Colar
        menuEditar.remove(mItemColar);
        Action colarAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                panel.colar(panel.px, panel.py);
            }
        };
        if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
            colarAction.putValue(AbstractAction.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.META_DOWN_MASK));
        } else {
            colarAction.putValue(AbstractAction.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
        }
        mItemColar = new JMenuItem(colarAction);
        mItemColar.setText("Colar");
        menuEditar.add(mItemColar, 1);

        //Botão Desfazer
        menuEditar.remove(mItemDesfazer);
        Action desfazerAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                panel.desfazer();
            }
        };
        if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
            desfazerAction.putValue(AbstractAction.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.META_DOWN_MASK));
        } else {
            desfazerAction.putValue(AbstractAction.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
        }
        mItemDesfazer = new JMenuItem(desfazerAction);
        mItemDesfazer.setText("Desfazer");
        menuEditar.add(mItemDesfazer, 2);

        //Botão Colar
        menuEditar.remove(mItemRefazer);
        Action refazerAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                panel.refazer();
            }
        };
        if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
            refazerAction.putValue(AbstractAction.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.META_DOWN_MASK));
        } else {
            refazerAction.putValue(AbstractAction.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
        }
        mItemRefazer = new JMenuItem(refazerAction);
        mItemRefazer.setText("Refazer");
        menuEditar.add(mItemRefazer, 3);

        //Botão Próxima guia
        menuNavegar.remove(mItemProximaGuia);
        Action proximaGuiaAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                panel.proximaGuia();
            }
        };
        if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
            proximaGuiaAction.putValue(AbstractAction.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.SHIFT_DOWN_MASK));
        } else {
            proximaGuiaAction.putValue(AbstractAction.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.CTRL_DOWN_MASK));
        }
        mItemProximaGuia = new JMenuItem(proximaGuiaAction);
        mItemProximaGuia.setText("Próxima Guia");
        menuNavegar.add(mItemProximaGuia);

        //Botão Guia Anterior
        menuNavegar.remove(mItemGuiaAnterior);
        Action guiaAnteriorAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                panel.guiaAnterior();
            }
        };
        if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
            guiaAnteriorAction.putValue(AbstractAction.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.SHIFT_DOWN_MASK));
        } else {
            guiaAnteriorAction.putValue(AbstractAction.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.CTRL_DOWN_MASK + KeyEvent.SHIFT_DOWN_MASK));
        }

        mItemGuiaAnterior = new JMenuItem(guiaAnteriorAction);
        mItemGuiaAnterior.setText("Guia anterior");
        menuNavegar.add(mItemGuiaAnterior);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem exportaSGBD;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem mItemAbrir;
    private javax.swing.JMenuItem mItemColar;
    private javax.swing.JMenuItem mItemCopiar;
    private javax.swing.JMenuItem mItemDesfazer;
    private javax.swing.JMenuItem mItemExportaImagem;
    private javax.swing.JMenuItem mItemGuiaAnterior;
    private javax.swing.JMenuItem mItemMaisZoom;
    private javax.swing.JMenuItem mItemMenosZoom;
    private javax.swing.JMenuItem mItemNovo;
    private javax.swing.JMenuItem mItemProximaGuia;
    private javax.swing.JMenuItem mItemRefazer;
    private javax.swing.JMenuItem mItemSair;
    private javax.swing.JMenuItem mItemSalvar;
    private javax.swing.JMenuItem mItemSalvarComo;
    private javax.swing.JMenuItem mItemSalvarTudo;
    private javax.swing.JMenu menuArquivo;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu menuEditar;
    private javax.swing.JMenu menuExibir;
    private javax.swing.JMenu menuNavegar;
    private javax.swing.JMenu menuSobre;
    private javax.swing.JMenuItem salvarScripts;
    private javax.swing.JPopupMenu.Separator separator1;
    private javax.swing.JPopupMenu.Separator separator3;
    // End of variables declaration//GEN-END:variables

    public JMenuItem getjExportaImagem() {
        return mItemExportaImagem;
    }

    public JMenuItem getjSalvar() {
        return mItemSalvar;
    }

    public JMenuItem getjSalvarComo() {
        return mItemSalvarComo;
    }

    public JMenu getMenuExibir() {
        return menuExibir;
    }
}
