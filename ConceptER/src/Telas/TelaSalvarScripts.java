/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Telas;

import ScriptsSQL.AtributoSQL;
import ScriptsSQL.Scripts;
import ScriptsSQL.TabelaSQL;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author PedroEYan
 */
public class TelaSalvarScripts extends javax.swing.JFrame {

    PanelPrincipal pp;
    String sql = "";

    public TelaSalvarScripts(PanelPrincipal pp) {
        this.pp = pp;
        initComponents();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToggleButton1 = new javax.swing.JToggleButton();
        nomeArquivo = new javax.swing.JTextField();
        btnArquivo = new javax.swing.JButton();
        btnConfirma = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        txtArquivo = new javax.swing.JLabel();

        jToggleButton1.setText("jToggleButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Salvar script");

        nomeArquivo.setEditable(false);

        btnArquivo.setText("Escolher");
        btnArquivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArquivoActionPerformed(evt);
            }
        });

        btnConfirma.setText("Confirmar");
        btnConfirma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmaActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        txtArquivo.setText("Localização do arquivo: ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnCancelar)
                        .addGap(18, 18, 18))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtArquivo)
                            .addComponent(nomeArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnConfirma)
                    .addComponent(btnArquivo))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(46, Short.MAX_VALUE)
                .addComponent(txtArquivo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nomeArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnArquivo))
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConfirma)
                    .addComponent(btnCancelar))
                .addGap(22, 22, 22))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnArquivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArquivoActionPerformed
        JFileChooser jfc = new JFileChooser();
        if (jfc.showSaveDialog(btnArquivo) == JFileChooser.APPROVE_OPTION) {
            nomeArquivo.setText(jfc.getSelectedFile().getPath());
        }

    }//GEN-LAST:event_btnArquivoActionPerformed

    private void btnConfirmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmaActionPerformed
        if (nomeArquivo.isVisible() == false) {
            try {
                File file = new File(nomeArquivo.getText());
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(sql);
                writer.flush();
                writer.close();
                JOptionPane.showMessageDialog(null, "Salvo");
                JOptionPane.showMessageDialog(null, "AVISO - Lembre-se de realizar o Modelo Relacional de Tabelas para completar o projeto de banco de dados",
                        "AVISO", JOptionPane.WARNING_MESSAGE);
                JOptionPane.showMessageDialog(null, Scripts.mapeamento);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "ERRO");
            }
        } else {
            if (nomeArquivo.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Caminho do arquivo não pode fica vazio");
            } else {

                Scripts scripts = new Scripts(pp);
                ArrayList<TabelaSQL> alt = scripts.gerar();
                boolean possui = false;
                boolean erro = false;
                for (TabelaSQL alt2 : alt) {
                    for (AtributoSQL a : alt2.getAtributos()) {
                        if (a.isPk()) {
                            possui = true;
                        }
                    }
                    if (possui == false) {
                        JOptionPane.showMessageDialog(null,
                                "A tabela " + alt2.getNome() + " não possui chave primária");
                        erro = true;
                        break;
                    }
                }
                if (!erro) {
                    boolean tem = false;
                    for (TabelaSQL alt2 : alt) {
                        for (AtributoSQL asql : alt2.getAtributos()) {
                            if (asql.isFk()) {
                                tem = true;
                            }
                        }
                        if (tem == false) {
                            sql += alt2.toString() + "\n";
                        } else {
                            tem = false;
                        }
                    }
                    for (TabelaSQL alt2 : alt) {

                        tem = false;
                        for (AtributoSQL asql : alt2.getAtributos()) {
                            if (asql.isFk()) {
                                tem = true;
                            }
                        }
                        if (tem == true) {
                            sql += alt2.toString() + "\n";

                        }
                    }
                    nomeArquivo.setVisible(false);
                    btnArquivo.setVisible(false);
                    txtArquivo.setVisible(false);
                    JTextArea jta = new JTextArea(sql);
                    jta.setEditable(false);
                    JScrollPane scroll = new JScrollPane(jta);
                    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                    scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
                    add(scroll);

                    this.setSize(600, 500);
                    scroll.setBounds(25, 25, this.getWidth() - 50, 300);
                    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                    this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
                }
            }
        }
    }//GEN-LAST:event_btnConfirmaActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnArquivo;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnConfirma;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JTextField nomeArquivo;
    private javax.swing.JLabel txtArquivo;
    // End of variables declaration//GEN-END:variables
}
