/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Telas;

import Utils.Aba;
import Estaticos.EnumCardinalidade;
import Utils.Historico;
import Utils.PersistenciaGrafica;
import Estaticos.Valores;
import Model.Atributo;
import Model.Entidade;
import Model.Relacionamento;
import Model.Agregacao;
import Model.Heranca;
import Model.Ligacao;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

/**
 *
 * @author User
 */
public class PanelPrincipal extends javax.swing.JPanel implements ActionListener {

    //Cada guia corresponde a uma instância de mxGraph(pincel)
    //Cada guia corresponde a uma instância de mxGraphComponent(tela)
    private ArrayList<Aba> abas = new ArrayList<>();
    //Auxiliares que armazenam objetos (Entidade, ligação...)
    private mxCell cell, objetoSelecionado, cola = null;

    private mxCell cellLigacao1, cellLigacao2;

    private ArrayList<File> file = new ArrayList<>();
    private ArrayList<String> nomes = new ArrayList<>();
    private TelaPrincipal frame;

    //Tamanho da Tela
    int x, y;
    //Estilo da linha
    private Map<String, Object> edge = new HashMap<String, Object>();

    public int px, py;
    JPopupMenu btnDireito, popTabs;
    JMenuItem copiar, deletar, colar, fechar, fecharTodos, fecharOutros;

    //Posição da tabela selecionada
    int tabSelecionada = 0;

    JButton buttonSelecionado;

    public PanelPrincipal(int x, int y, TelaPrincipal frame) {
        file = null;
        this.x = x;
        this.y = y;
        initComponents();

        btnDireito = new JPopupMenu();
        popTabs = new JPopupMenu();
        fechar = new JMenuItem("Fechar");
        fechar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (tabs.getTitleAt(tabs.getSelectedIndex()).contains("*")) {
                    int resp = JOptionPane.showConfirmDialog(null, "Deseja salvar " + getNomes(tabs.getSelectedIndex()) + "?", "Sair", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (resp == JOptionPane.YES_OPTION) {
                        if (getFile(tabs.getSelectedIndex()) == null) {
                            JFileChooser jfc = new JFileChooser();
                            if (jfc.showSaveDialog(jfc) == JFileChooser.APPROVE_OPTION) {
                                setFile(jfc.getSelectedFile(), tabs.getSelectedIndex());
                            }
                        }
                        if (getFile(tabSelecionada) != null) {
                            PersistenciaGrafica.saveGraph(getAba(tabs.getSelectedIndex()), getFile(tabs.getSelectedIndex()).getPath());
                            remover(tabs.getSelectedIndex());
                        }
                    } else {
                        if (resp == JOptionPane.NO_OPTION) {
                            remover(tabs.getSelectedIndex());
                        }
                    }
                } else {
                    tabs.getComponentAt(tabs.getSelectedIndex()).setVisible(false);
                    remover(tabs.getSelectedIndex());

                }
                if (tabs.getTabCount() == 0) {
                    frame.remove(frame.panel);
                    frame.panel = null;
                    frame.getMenuExibir().setEnabled(false);
                    frame.revalidate();
                    frame.repaint();
                }
            }
        });
        fecharTodos = new JMenuItem("Fechar Todos");
        fecharTodos.addActionListener((e) -> {
            frame.remove(this);
            frame.revalidate();
            frame.panel = null;
            frame.getMenuExibir().setEnabled(false);
            frame.repaint();
        });

        fecharOutros = new JMenuItem("Fechar Outros");
        fecharOutros.addActionListener((e) -> {
            for (int i = tabs.getTabCount() - 1; i >= 0; i--) {
                if (i != tabs.getSelectedIndex()) {
                    if (tabs.getTitleAt(i).contains("*")) {
                        int resp = JOptionPane.showConfirmDialog(null, "Deseja salvar " + getNomes(i) + "?", "Sair", JOptionPane.YES_NO_CANCEL_OPTION);
                        if (resp == JOptionPane.YES_OPTION) {
                            if (getFile(tabs.getSelectedIndex()) == null) {
                                JFileChooser jfc = new JFileChooser();

                                if (jfc.showSaveDialog(jfc) == JFileChooser.APPROVE_OPTION) {
                                    setFile(jfc.getSelectedFile(), i);
                                }
                            }
                            if (getFile(tabSelecionada) != null) {
                                PersistenciaGrafica.saveGraph(getAba(i), getFile(i).getPath());
                                remover(i);
                            }
                        } else {
                            if (resp == JOptionPane.NO_OPTION) {
                                remover(i);
                            }
                        }
                    } else {
                        remover(i);

                    }
                }
            }
        });
        popTabs.add(fechar);
        popTabs.add(fecharTodos);
        popTabs.add(fecharOutros);
        tabs.setComponentPopupMenu(popTabs);

        copiar = new JMenuItem("Copiar");
        btnDireito.add(copiar);
        colar = new JMenuItem("Colar");
        btnDireito.add(colar);
        deletar = new JMenuItem("Delete");
        deletar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                deletar();
            }
        });
        btnDireito.add(deletar);

        this.frame = frame;
        objetoSelecionado = null;

        this.abas.add(new Aba(new mxGraph()));
        //txtNome.getDocument().addDocumentListener(new BindingListener(objetoSelecionado, "f1"));
        propriedades.setBounds(0, 30, 230, y);
        propriedades.setVisible(false);
        frame.getjSalvar().setEnabled(true);
        frame.getjSalvarComo().setEnabled(true);
        frame.getjExportaImagem().setEnabled(true);
        tabs.setSize(x - 250, y - 30);

        file = new ArrayList<>();

        file.add(null);
        UIManager.put("TabbedPane.selected.font", Font.BOLD);
        initGrafico(tabSelecionada);
        tabs.add(abas.get(tabSelecionada));

        tabs.setTitleAt(tabs.getSelectedIndex(), "Novo Arquivo " + tabs.getSelectedIndex());
        nomes.add(tabs.getSelectedIndex(), "Novo Arquivo " + tabs.getSelectedIndex());

        btnEntidade.addActionListener(this);
        btnAtributo.addActionListener(this);
        btnRelacionamento.addActionListener(this);
        btnAgregacao.addActionListener(this);
        btnCriarRelacaoEntreEntidades.addActionListener(this);
        btnAutoRelacionamento.addActionListener(this);
        btnConectaEntidadeRelacionamento.addActionListener(this);
        btnDeletar.addActionListener(this);
        objectToolbar.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, objectToolbar.getHeight());
        setBtnsIcons();
        btnSelecionar.setSelected(true);
        buttonSelecionado = btnSelecionar;
        for (String s : Valores.getDOMINIOS()) {
            comboxDominio.addItem(s);
        }
    }

    public void initGrafico(int index) {
        abas.get(index).getArrayCell().clear();
        try {
            for (Entidade e : abas.get(tabs.getTabCount() - 1).getEntidades()) {
                abas.get(tabs.getTabCount() - 1).getArrayCell().add((mxCell) e.getCell().clone());
            }

            for (Relacionamento r : abas.get(tabs.getTabCount() - 1).getRelacionamentos()) {
                abas.get(tabs.getTabCount() - 1).getArrayCell().add((mxCell) r.getCell().clone());

            }
            for (Ligacao l : abas.get(tabs.getTabCount() - 1).getLigacoes()) {
                mxCell aux = (mxCell) l.getLigacao().clone();
                aux.setSource((mxCell) l.getLigacao().getSource().clone());
                aux.setTarget((mxCell) l.getLigacao().getTarget().clone());
                abas.get(tabs.getTabCount() - 1).getArrayCell().add(aux);

            }
            for (Atributo a : abas.get(tabs.getTabCount() - 1).getAtributos()) {
                mxCell aux = (mxCell) a.getCell().clone();
                mxCell at = (mxCell) a.getCell().getTarget().clone();
                aux.setTarget(at);
                abas.get(tabs.getTabCount() - 1).getArrayCell().add(aux);
            }
        } catch (Exception e) {

        }

        this.abas.get(index).setToolTipText("2112");
        this.abas.get(index).add(btnDireito);
        this.abas.get(index).setBounds(210, 30, x, y);          //Nao permite que setas sejam criadas de dentro de um objeto
        this.abas.get(index).addMouseListener(new Mouse());
        this.abas.get(index).addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_DELETE:
                        deletar();
                        break;
                }
                if (!System.getProperties().contains("windows")) {
                    if (e.isMetaDown() && (e.getKeyCode() == KeyEvent.VK_EQUALS)) {
                        maisZoom();
                    }
                } else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_EQUALS)) {
                    maisZoom();
                }
            }
        });
        this.abas.get(index).getGraphControl().addMouseListener(new Mouse());

        edge.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ORTHOGONAL);
        edge.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
        edge.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
        edge.put(mxConstants.STYLE_STROKECOLOR, "#000000"); // default is #6482B9
        edge.put(mxConstants.STYLE_FONTCOLOR, "#446299");

        mxStylesheet edgeStyle = new mxStylesheet();
        edgeStyle.setDefaultEdgeStyle(edge);
        abas.get(index).getGraph().setStylesheet(edgeStyle);
    }

    /* Criação da tela*/
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        objectToolbar = new javax.swing.JToolBar();
        btnSelecionar = new javax.swing.JButton();
        btnEntidade = new javax.swing.JButton();
        btnAtributo = new javax.swing.JButton();
        btnRelacionamento = new javax.swing.JButton();
        btnCriarRelacaoEntreEntidades = new javax.swing.JButton();
        btnConectaEntidadeRelacionamento = new javax.swing.JButton();
        btnAutoRelacionamento = new javax.swing.JButton();
        btnAgregacao = new javax.swing.JButton();
        btnSobreposicao = new javax.swing.JButton();
        btnDisjuncao = new javax.swing.JButton();
        btnDeletar = new javax.swing.JButton();
        propriedades = new javax.swing.JPanel();
        txtNome = new javax.swing.JTextField();
        lblNome = new javax.swing.JLabel();
        lblLargura = new javax.swing.JLabel();
        txtLargura = new javax.swing.JTextField();
        txtAltura = new javax.swing.JTextField();
        lblAltura = new javax.swing.JLabel();
        propriedadesAtributo = new javax.swing.JPanel();
        jSeparator = new javax.swing.JSeparator();
        isNotNull = new javax.swing.JCheckBox();
        isPK = new javax.swing.JCheckBox();
        isAutoIncrement = new javax.swing.JCheckBox();
        txtDefaultValue = new javax.swing.JTextField();
        isFK = new javax.swing.JCheckBox();
        comboxDominio = new javax.swing.JComboBox<>();
        txtDominio = new javax.swing.JTextField();
        isDefault = new javax.swing.JCheckBox();
        chkBoxComposto = new javax.swing.JCheckBox();
        chkBoxMultivalorado = new javax.swing.JCheckBox();
        chkBoxDerivado = new javax.swing.JCheckBox();
        isUnique = new javax.swing.JCheckBox();
        lblDominio = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        chkBoxFraca = new javax.swing.JCheckBox();
        chkBoxIdentificador = new javax.swing.JCheckBox();
        chkBoxParticipacaoTotal = new javax.swing.JCheckBox();
        comboxCardinalidade = new javax.swing.JComboBox<>();
        lblCardinalidade = new javax.swing.JLabel();
        tabs = new javax.swing.JTabbedPane();

        jButton1.setText("jButton1");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        setLayout(null);

        objectToolbar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        objectToolbar.setFloatable(false);
        objectToolbar.setRollover(true);

        btnSelecionar.setText("Selecionar objeto");
        btnSelecionar.setToolTipText("<html>\n<font face=\"sansserif\" color=\"blue\">\n<h3>Selecionar objeto</h3>\n</html>");
        btnSelecionar.setFocusable(false);
        btnSelecionar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSelecionar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSelecionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionarActionPerformed(evt);
            }
        });
        objectToolbar.add(btnSelecionar);

        btnEntidade.setText("Entidade");
        btnEntidade.setToolTipText("<html>\n<font face=\"sansserif\" color=\"blue\">\n<div>\n<h3>Entidade</h3>\n<p>Uma entidade representa um objeto do mundo real, seja ela concreta,<br> como uma pessoa, um carro ou um livro, ou abstrata, decorrente das<br> interações de outras entidades, como uma venda, uma turma ou um cargo.</p></div>\n</html>");
        btnEntidade.setFocusable(false);
        btnEntidade.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEntidade.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEntidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntidadeActionPerformed(evt);
            }
        });
        objectToolbar.add(btnEntidade);

        btnAtributo.setText("Atributo");
        btnAtributo.setToolTipText("<html>\n<font face=\"sansserif\" color=\"blue\">\n<h3>Atributo</h3>\n<p>Um atributo é uma característica que descreve uma entidade<br> e assume um valor permitido dentro de um domínio.</p>\n</html>");
        btnAtributo.setFocusable(false);
        btnAtributo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAtributo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAtributo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtributoActionPerformed(evt);
            }
        });
        objectToolbar.add(btnAtributo);

        btnRelacionamento.setText("Relacionamento");
        btnRelacionamento.setToolTipText("<html>\n<font face=\"sansserif\" color=\"blue\">\n<h3>Relacionamento</h3>\n<p>Um relacionamento é definido como uma <br>associação entre uma ou mais entidades.</p>\n</html>");
        btnRelacionamento.setFocusable(false);
        btnRelacionamento.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRelacionamento.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRelacionamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRelacionamentoActionPerformed(evt);
            }
        });
        objectToolbar.add(btnRelacionamento);

        btnCriarRelacaoEntreEntidades.setText("Criar relação");
        btnCriarRelacaoEntreEntidades.setToolTipText("<html>\n<font face=\"sansserif\" color=\"blue\">\n<h3>Gerar relacionamento entre entidades</h3>\n<p>Clique em duas entidades para criar um novo relacionamento entre elas.</p>\n</html>");
        btnCriarRelacaoEntreEntidades.setFocusable(false);
        btnCriarRelacaoEntreEntidades.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCriarRelacaoEntreEntidades.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCriarRelacaoEntreEntidades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCriarRelacaoEntreEntidadesActionPerformed(evt);
            }
        });
        objectToolbar.add(btnCriarRelacaoEntreEntidades);

        btnConectaEntidadeRelacionamento.setText("Ligar relação");
        btnConectaEntidadeRelacionamento.setToolTipText("<html>\n<font face=\"sansserif\" color=\"blue\">\n<h3>Conectar objeto</h3>\n<p>Permite conectar:\n<ul>\n <li>Entidade e relacionamento </li>\n <li>Entidade e agregação</li>\n <li>Relacionamento e agregação</li>\n</p>\n</html>");
        btnConectaEntidadeRelacionamento.setFocusable(false);
        btnConectaEntidadeRelacionamento.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnConectaEntidadeRelacionamento.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnConectaEntidadeRelacionamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConectaEntidadeRelacionamentoActionPerformed(evt);
            }
        });
        objectToolbar.add(btnConectaEntidadeRelacionamento);

        btnAutoRelacionamento.setText("Auto relacionamento");
        btnAutoRelacionamento.setToolTipText("<html>\n<font face=\"sansserif\" color=\"blue\">\n<h3>Auto relacionamento</h3>\n<p>Clique em uma entidade para gerar um relacionamento com ela própria.</p>\n</html>");
        btnAutoRelacionamento.setFocusable(false);
        btnAutoRelacionamento.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAutoRelacionamento.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAutoRelacionamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAutoRelacionamentoActionPerformed(evt);
            }
        });
        objectToolbar.add(btnAutoRelacionamento);

        btnAgregacao.setText("Agregação");
        btnAgregacao.setToolTipText("<html>\n<font face=\"sansserif\" color=\"blue\">\n<h3>Agregação</h3>\n<p> A agregação é um processo de abstração para construir <br>\nobjetos compostos a partir de seus objetos componenetes  <br>\ne também é uma abstração pela qual os relacionamentos <br>\nsão tratados como entidades de nivel superior.</p>\n</html>");
        btnAgregacao.setFocusable(false);
        btnAgregacao.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAgregacao.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAgregacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregacaoActionPerformed(evt);
            }
        });
        objectToolbar.add(btnAgregacao);

        btnSobreposicao.setText("Sobreposição");
        btnSobreposicao.setToolTipText("<html>\n<font face=\"sansserif\" color=\"blue\">\n<h3>Sobreposição</h3>\n<p>Clique em uma entidade para gerar uma entidade filha do tipo <strong>sobreposição</strong>.</p>\n</html>");
        btnSobreposicao.setFocusable(false);
        btnSobreposicao.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSobreposicao.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSobreposicao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSobreposicaoActionPerformed(evt);
            }
        });
        objectToolbar.add(btnSobreposicao);

        btnDisjuncao.setText("Disjunção");
        btnDisjuncao.setToolTipText("<html>\n<font face=\"sansserif\" color=\"blue\">\n<h3>Disjunção</h3>\n<p>Clique em uma entidade para gerar uma entidade filha do tipo <strong>disjunção</strong>.</p>\n</html>");
        btnDisjuncao.setFocusable(false);
        btnDisjuncao.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDisjuncao.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDisjuncao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisjuncaoActionPerformed(evt);
            }
        });
        objectToolbar.add(btnDisjuncao);

        btnDeletar.setText("Apagar");
        btnDeletar.setToolTipText("<html>\n<font face=\"sansserif\" color=\"blue\">\n<h3>Delete</h3>\n<p>Clique em vários objetos para deletá-los do diagrama.</p>\n</html>");
        btnDeletar.setFocusable(false);
        btnDeletar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDeletar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletarActionPerformed(evt);
            }
        });
        objectToolbar.add(btnDeletar);

        add(objectToolbar);
        objectToolbar.setBounds(0, 0, 1010, 30);

        propriedades.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        propriedades.setForeground(new java.awt.Color(204, 204, 204));

        txtNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeActionPerformed(evt);
            }
        });

        lblNome.setText("Nome");

        lblLargura.setText("Largura");

        txtLargura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLarguraActionPerformed(evt);
            }
        });

        txtAltura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAlturaActionPerformed(evt);
            }
        });

        lblAltura.setText("Altura");

        propriedadesAtributo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        isNotNull.setText("Não nulo");
        isNotNull.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isNotNullActionPerformed(evt);
            }
        });

        isPK.setText("Atributo chave");
        isPK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isPKActionPerformed(evt);
            }
        });

        isAutoIncrement.setText("Auto Increment");
        isAutoIncrement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isAutoIncrementActionPerformed(evt);
            }
        });

        txtDefaultValue.setEnabled(false);
        txtDefaultValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDefaultValueFocusLost(evt);
            }
        });

        isFK.setText("Chave parcial");
        isFK.setToolTipText("");
        isFK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isFKActionPerformed(evt);
            }
        });

        comboxDominio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboxDominioActionPerformed(evt);
            }
        });

        txtDominio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDominioActionPerformed(evt);
            }
        });

        isDefault.setText("Default");
        isDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isDefaultActionPerformed(evt);
            }
        });

        chkBoxComposto.setText("Composto");
        chkBoxComposto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkBoxCompostoActionPerformed(evt);
            }
        });

        chkBoxMultivalorado.setText("Multivalorado");
        chkBoxMultivalorado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkBoxMultivaloradoActionPerformed(evt);
            }
        });

        chkBoxDerivado.setText("Derivado");
        chkBoxDerivado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkBoxDerivadoActionPerformed(evt);
            }
        });

        isUnique.setText("Único");
        isUnique.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isUniqueActionPerformed(evt);
            }
        });

        lblDominio.setText("Domínio:");

        jLabel1.setText("Tamanho:");

        javax.swing.GroupLayout propriedadesAtributoLayout = new javax.swing.GroupLayout(propriedadesAtributo);
        propriedadesAtributo.setLayout(propriedadesAtributoLayout);
        propriedadesAtributoLayout.setHorizontalGroup(
            propriedadesAtributoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator)
            .addGroup(propriedadesAtributoLayout.createSequentialGroup()
                .addGroup(propriedadesAtributoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(propriedadesAtributoLayout.createSequentialGroup()
                        .addComponent(isDefault)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDefaultValue))
                    .addGroup(propriedadesAtributoLayout.createSequentialGroup()
                        .addGroup(propriedadesAtributoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(isAutoIncrement)
                            .addComponent(isNotNull)
                            .addComponent(isUnique)
                            .addComponent(chkBoxComposto)
                            .addComponent(chkBoxDerivado)
                            .addComponent(chkBoxMultivalorado)
                            .addComponent(isFK)
                            .addComponent(isPK)
                            .addGroup(propriedadesAtributoLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblDominio))
                            .addComponent(comboxDominio, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(propriedadesAtributoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(propriedadesAtributoLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtDominio, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE))))
                .addContainerGap())
        );
        propriedadesAtributoLayout.setVerticalGroup(
            propriedadesAtributoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(propriedadesAtributoLayout.createSequentialGroup()
                .addComponent(chkBoxComposto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkBoxDerivado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkBoxMultivalorado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(isPK)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(isFK)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(isAutoIncrement)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(isNotNull)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(isUnique)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(propriedadesAtributoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(isDefault)
                    .addComponent(txtDefaultValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(propriedadesAtributoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDominio)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(propriedadesAtributoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboxDominio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDominio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        chkBoxFraca.setText("Entidade Fraca");
        chkBoxFraca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkBoxFracaActionPerformed(evt);
            }
        });

        chkBoxIdentificador.setText("Relacionamento Identificador");
        chkBoxIdentificador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkBoxIdentificadorActionPerformed(evt);
            }
        });

        chkBoxParticipacaoTotal.setText("Participação Total");
        chkBoxParticipacaoTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkBoxParticipacaoTotalActionPerformed(evt);
            }
        });

        comboxCardinalidade.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0:1", "1:1", "0:N", "1:N" }));
        comboxCardinalidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboxCardinalidadeActionPerformed(evt);
            }
        });

        lblCardinalidade.setText("Cardinalidade:");

        javax.swing.GroupLayout propriedadesLayout = new javax.swing.GroupLayout(propriedades);
        propriedades.setLayout(propriedadesLayout);
        propriedadesLayout.setHorizontalGroup(
            propriedadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, propriedadesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(propriedadesAtributo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
            .addComponent(jSeparator1)
            .addGroup(propriedadesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(propriedadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(propriedadesLayout.createSequentialGroup()
                        .addComponent(lblCardinalidade)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboxCardinalidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(chkBoxFraca)
                    .addComponent(chkBoxIdentificador)
                    .addComponent(chkBoxParticipacaoTotal)
                    .addGroup(propriedadesLayout.createSequentialGroup()
                        .addGroup(propriedadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblLargura, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                            .addComponent(lblNome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblAltura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(6, 6, 6)
                        .addGroup(propriedadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtLargura, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAltura, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        propriedadesLayout.setVerticalGroup(
            propriedadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(propriedadesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(propriedadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNome)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(propriedadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLargura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLargura))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(propriedadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAltura)
                    .addComponent(txtAltura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkBoxFraca)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkBoxIdentificador)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkBoxParticipacaoTotal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(propriedadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCardinalidade)
                    .addComponent(comboxCardinalidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(propriedadesAtributo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(propriedades);
        propriedades.setBounds(0, 30, 230, 500);

        tabs.setInheritsPopupMenu(true);
        tabs.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabsStateChanged(evt);
            }
        });
        add(tabs);
        tabs.setBounds(230, 30, 780, 510);
    }// </editor-fold>//GEN-END:initComponents

    public void setBtnsIcons() {
        ImageIcon btnDelIcon = new ImageIcon(getClass().getResource("/images/ok.png"));
        Image btnDelImg = btnDelIcon.getImage();
        Image btnDelNewImg = btnDelImg.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        btnDelIcon = new ImageIcon(btnDelNewImg);
        btnDeletar.setIcon(btnDelIcon);
        btnDeletar.setText("");

        ImageIcon btnEntIcon = new ImageIcon(getClass().getResource("/images/entidade_2.png"));
        Image btnEntImg = btnEntIcon.getImage();
        Image btnEntNewImg = btnEntImg.getScaledInstance(30, 20, java.awt.Image.SCALE_SMOOTH);
        btnEntIcon = new ImageIcon(btnEntNewImg);
        btnEntidade.setIcon(btnEntIcon);
        btnEntidade.setText("");

        ImageIcon btnAtrIcon = new ImageIcon(getClass().getResource("/images/atributo_2.png"));
        Image btnAtrImg = btnAtrIcon.getImage();
        Image btnAtrNewImg = btnAtrImg.getScaledInstance(30, 20, java.awt.Image.SCALE_SMOOTH);
        btnAtrIcon = new ImageIcon(btnAtrNewImg);
        btnAtributo.setIcon(btnAtrIcon);
        btnAtributo.setText("");

        ImageIcon btnRelIcon = new ImageIcon(getClass().getResource("/images/relacionamento_2.png"));
        Image btnRelImg = btnRelIcon.getImage();
        Image btnRelNewImg = btnRelImg.getScaledInstance(30, 20, java.awt.Image.SCALE_SMOOTH);
        btnRelIcon = new ImageIcon(btnRelNewImg);
        btnRelacionamento.setIcon(btnRelIcon);
        btnRelacionamento.setText("");

        ImageIcon btnSelecionarIcon = new ImageIcon(getClass().getResource("/images/mouse_2.png"));
        Image btnSelecionarImg = btnSelecionarIcon.getImage();
        Image btnSelecionarNewImg = btnSelecionarImg.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        btnSelecionarIcon = new ImageIcon(btnSelecionarNewImg);
        btnSelecionar.setIcon(btnSelecionarIcon);
        btnSelecionar.setText("");

        ImageIcon btnAutoRelacionamentoIcon = new ImageIcon(getClass().getResource("/images/autoRelacionamento_2.png"));
        Image btnAutoRelacionamentoImg = btnAutoRelacionamentoIcon.getImage();
        Image btnAutoRelacionamentoNewImg = btnAutoRelacionamentoImg.getScaledInstance(30, 20, java.awt.Image.SCALE_SMOOTH);
        btnAutoRelacionamentoIcon = new ImageIcon(btnAutoRelacionamentoNewImg);
        btnAutoRelacionamento.setIcon(btnAutoRelacionamentoIcon);
        btnAutoRelacionamento.setText("");

        ImageIcon btnAgregacaoIcon = new ImageIcon(getClass().getResource("/images/agregacao_2.png"));
        Image btnAgregacaoImg = btnAgregacaoIcon.getImage();
        Image btnAgregacaoNewImg = btnAgregacaoImg.getScaledInstance(30, 20, java.awt.Image.SCALE_SMOOTH);
        btnAgregacaoIcon = new ImageIcon(btnAgregacaoNewImg);
        btnAgregacao.setIcon(btnAgregacaoIcon);
        btnAgregacao.setText("");

        ImageIcon btnCriarRelacaoIcon = new ImageIcon(getClass().getResource("/images/relacao_2.png"));
        Image btnCriarRelacaoImg = btnCriarRelacaoIcon.getImage();
        Image btnCriarRelacaoNewImg = btnCriarRelacaoImg.getScaledInstance(50, 20, java.awt.Image.SCALE_SMOOTH);
        btnCriarRelacaoIcon = new ImageIcon(btnCriarRelacaoNewImg);
        btnCriarRelacaoEntreEntidades.setIcon(btnCriarRelacaoIcon);
        btnCriarRelacaoEntreEntidades.setText("");

        ImageIcon btnLigarRelacaoIcon = new ImageIcon(getClass().getResource("/images/ligaRelacao_2.png"));
        Image btnLigarRelacaoImg = btnLigarRelacaoIcon.getImage();
        Image btnLigarRelacaoNewImg = btnLigarRelacaoImg.getScaledInstance(40, 20, java.awt.Image.SCALE_SMOOTH);
        btnLigarRelacaoIcon = new ImageIcon(btnLigarRelacaoNewImg);
        btnConectaEntidadeRelacionamento.setIcon(btnLigarRelacaoIcon);
        btnConectaEntidadeRelacionamento.setText("");

        ImageIcon btnDisjuncaoIcon = new ImageIcon(getClass().getResource("/images/disjuncao_1.png"));
        Image btnDisjuncaoImg = btnDisjuncaoIcon.getImage();
        Image btnDisjuncaoNewImg = btnDisjuncaoImg.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        btnDisjuncaoIcon = new ImageIcon(btnDisjuncaoNewImg);
        btnDisjuncao.setIcon(btnDisjuncaoIcon);
        btnDisjuncao.setText("");

        ImageIcon btnSobreposicaoIcon = new ImageIcon(getClass().getResource("/images/sobreposicao_1.png"));
        Image btnSobreposicaoImg = btnSobreposicaoIcon.getImage();
        Image btnSobreposicaoNewImg = btnSobreposicaoImg.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        btnSobreposicaoIcon = new ImageIcon(btnSobreposicaoNewImg);
        btnSobreposicao.setIcon(btnSobreposicaoIcon);
        btnSobreposicao.setText("");
    }

    public void addTab() {
        file.add(null);
        mxGraph gAux = new mxGraph();
        Aba aba = new Aba(gAux);
        tabs.getSelectedComponent().setFont(new Font(tabs.getFont().getFontName(), Font.PLAIN, tabs.getFont().getSize()));
        tabs.add("Novo Arquivo " + (tabs.getTabCount()), aba);
        abas.add(aba);
        initGrafico(tabs.getTabCount() - 1);
        nomes.add(tabs.getTitleAt(+(tabs.getTabCount() - 1)));
        btnSelecionar.setSelected(true);
    }

    private void txtNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeActionPerformed
        //atualiza o nome quando o usuário digitar na tela de propriedades
        if (objetoSelecionado.getStyle().contains("rectangle") || objetoSelecionado.getStyle().contains("Rectangle")) {
            for (Entidade e : abas.get(tabSelecionada).getEntidades()) {
                if (e.getCell().equals(objetoSelecionado)) {
                    e.setNome(txtNome.getText());
                    e.atualiza();
                    modificarTitle();
                    break;
                }
            }
        } else if (objetoSelecionado.getStyle().contains("ellipse") || objetoSelecionado.getStyle().contains("Ellipse")) {
            for (Atributo a : abas.get(tabSelecionada).getAtributos()) {
                if (a.getCell().equals(objetoSelecionado)) {
                    a.setNome(txtNome.getText());
                    a.atualiza();
                    modificarTitle();
                    break;
                }
            }

        } else if (objetoSelecionado.getStyle().contains("rhombus") || objetoSelecionado.getStyle().contains("Rhombus")) {
            for (Relacionamento r : abas.get(tabSelecionada).getRelacionamentos()) {
                if (r.getCell().equals(objetoSelecionado)) {
                    r.setNome(txtNome.getText());
                    r.atualiza();
                    modificarTitle();
                    break;
                }
            }

        } else if (objetoSelecionado.getStyle().contains("aggregation")) {
            for (Agregacao ag : abas.get(tabSelecionada).getAgregacoes()) {
                if (ag.getCell().equals(objetoSelecionado)) {
                    ag.setNome(txtNome.getText());
                    ag.atualiza();
                    modificarTitle();
                    break;
                }
            }
        }
    }//GEN-LAST:event_txtNomeActionPerformed

    public ArrayList<Entidade> getEntidades() {
        return abas.get(tabSelecionada).getEntidades();
    }

    public ArrayList<Relacionamento> getRelacionamentos() {
        return abas.get(tabSelecionada).getRelacionamentos();
    }

    public ArrayList<Atributo> getAtributos() {
        return abas.get(tabSelecionada).getAtributos();
    }

    public ArrayList<Agregacao> getAgregacoes() {
        return abas.get(tabSelecionada).getAgregacoes();
    }

    public ArrayList<Heranca> getHerancas() {
        return this.abas.get(tabSelecionada).getHerancas();
    }

    public ArrayList<Ligacao> getLigacoes() {
        return abas.get(tabSelecionada).getLigacoes();
    }

    public ArrayList<Historico> getHist() {
        return abas.get(tabSelecionada).getHistorico();
    }

    private void txtLarguraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLarguraActionPerformed
        for (Entidade e : abas.get(tabSelecionada).getEntidades()) {
            if (objetoSelecionado.equals(e.getCell())) {
                e.setLargura(Integer.parseInt(txtLargura.getText()));
                e.atualiza();
                modificarTitle();
                break;
            }
        }
        for (Atributo a : abas.get(tabSelecionada).getAtributos()) {
            if (objetoSelecionado.equals(a.getCell())) {
                a.setAltura(Integer.parseInt(txtLargura.getText()));
                a.atualiza();
                modificarTitle();
                break;
            }
        }
        for (Relacionamento r : abas.get(tabSelecionada).getRelacionamentos()) {
            if (objetoSelecionado.equals(r.getCell())) {
                r.setAltura(Integer.parseInt(txtLargura.getText()));
                r.atualiza();
                modificarTitle();
                break;
            }
        }
        for (Agregacao ag : abas.get(tabSelecionada).getAgregacoes()) {
            if (objetoSelecionado.equals(ag.getCell())) {
                ag.setAltura(Integer.parseInt(txtLargura.getText()));
                ag.atualiza();
                modificarTitle();
                break;
            }
        }
    }//GEN-LAST:event_txtLarguraActionPerformed

    private void isPKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isPKActionPerformed
        // TODO add your handling code here:
        for (Atributo a : abas.get(tabSelecionada).getAtributos()) {
            if (a.getCell().equals(objetoSelecionado)) {
                if (isPK.isSelected()) {
                    isNotNull.setSelected(true);
                    isUnique.setSelected(true);
                    a.setPk(true);
                    a.setUnique(true);
                    a.setNotNull(true);
                } else {
                    a.setPk(false);
                    a.setUnique(false);
                    a.setNotNull(false);
                    isNotNull.setSelected(false);
                    isUnique.setSelected(false);
                }
                a.atualiza();
                modificarTitle();
                break;
            }
        }
    }//GEN-LAST:event_isPKActionPerformed

    private void isNotNullActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isNotNullActionPerformed
        for (Atributo a : abas.get(tabSelecionada).getAtributos()) {
            if (a.getCell().equals(objetoSelecionado)) {
                if (isNotNull.isSelected()) {
                    isNotNull.setSelected(true);
                    a.setNotNull(true);
                } else {
                    a.setNotNull(false);
                    isNotNull.setSelected(false);
                }
                a.atualiza();
                modificarTitle();
                break;
            }
        }
    }//GEN-LAST:event_isNotNullActionPerformed

    private void isDefaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isDefaultActionPerformed
        for (Atributo a : abas.get(tabSelecionada).getAtributos()) {
            if (a.getCell().equals(objetoSelecionado)) {
                if (isDefault.isSelected()) {
                    txtDefaultValue.setEnabled(true);
                    txtDefaultValue.setText(a.getPadrao());
                } else if (!isDefault.isSelected()) {
                    txtDefaultValue.setEnabled(false);
                    txtDefaultValue.setText(null);
                    a.setPadrao(null);
                }
                a.atualiza();
                modificarTitle();
                break;
            }
        }

    }//GEN-LAST:event_isDefaultActionPerformed

    private void isAutoIncrementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isAutoIncrementActionPerformed
        for (Atributo a : abas.get(tabSelecionada).getAtributos()) {
            if (a.getCell().equals(objetoSelecionado)) {
                if (isAutoIncrement.isSelected()) {
                    isAutoIncrement.setSelected(true);
                    a.setAutoIncrement(true);
                } else {
                    isAutoIncrement.setSelected(false);
                    a.setAutoIncrement(false);
                }
                a.atualiza();
                modificarTitle();
                break;
            }
        }
    }//GEN-LAST:event_isAutoIncrementActionPerformed

    private void isUniqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isUniqueActionPerformed
        for (Atributo a : abas.get(tabSelecionada).getAtributos()) {
            if (a.getCell().equals(objetoSelecionado)) {
                if (isUnique.isSelected()) {
                    isUnique.setSelected(true);
                    a.setUnique(true);
                } else {
                    isUnique.setSelected(false);
                    a.setUnique(false);
                }
                a.atualiza();
                modificarTitle();
                break;
            }
        }
    }//GEN-LAST:event_isUniqueActionPerformed

    private void txtDefaultValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDefaultValueFocusLost
        for (Atributo a : abas.get(tabSelecionada).getAtributos()) {
            if (a.getCell().equals(objetoSelecionado)) {
                if (isDefault.isSelected()) {

                    a.setPadrao(txtDefaultValue.getText());
                } else {
                    a.setPadrao(null);
                }
                a.atualiza();
                modificarTitle();
                break;
            }
        }
    }//GEN-LAST:event_txtDefaultValueFocusLost

    private void chkBoxCompostoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBoxCompostoActionPerformed
        for (Atributo a : abas.get(tabSelecionada).getAtributos()) {
            if (a.getCell().equals(objetoSelecionado)) {
                if (chkBoxComposto.isSelected()) {
                    a.setComposto(true);
                } else {
                    a.setComposto(false);
                }
                a.atualiza();
                modificarTitle();
                break;
            }
        }
    }//GEN-LAST:event_chkBoxCompostoActionPerformed

    private void chkBoxMultivaloradoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBoxMultivaloradoActionPerformed
        for (Atributo a : abas.get(tabSelecionada).getAtributos()) {
            if (a.getCell().equals(objetoSelecionado)) {
                if (chkBoxMultivalorado.isSelected()) {
                    a.setMultivalorado(true);
                } else {
                    a.setMultivalorado(false);
                }
                a.atualiza();
                modificarTitle();
                break;
            }
        }
    }//GEN-LAST:event_chkBoxMultivaloradoActionPerformed

    private void chkBoxDerivadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBoxDerivadoActionPerformed
        for (Atributo a : abas.get(tabSelecionada).getAtributos()) {
            if (a.getCell().equals(objetoSelecionado)) {
                if (chkBoxDerivado.isSelected()) {
                    a.setDerivado(true);
                } else {
                    a.setDerivado(false);
                }
                a.atualiza();
                modificarTitle();
                break;
            }
        }
    }//GEN-LAST:event_chkBoxDerivadoActionPerformed

    private void tabsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabsStateChanged
        tabSelecionada = ((JTabbedPane) evt.getSource()).getSelectedIndex();
    }//GEN-LAST:event_tabsStateChanged

    private void txtAlturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAlturaActionPerformed
        for (Entidade e : abas.get(tabSelecionada).getEntidades()) {
            if (objetoSelecionado.equals(e.getCell())) {
                e.setAltura(Integer.parseInt(txtAltura.getText()));
                e.atualiza();
                modificarTitle();
                break;
            }
        }
        for (Atributo a : abas.get(tabSelecionada).getAtributos()) {
            if (objetoSelecionado.equals(a.getCell())) {
                a.setAltura(Integer.parseInt(txtAltura.getText()));
                a.atualiza();
                modificarTitle();
                break;
            }
        }
        for (Relacionamento r : abas.get(tabSelecionada).getRelacionamentos()) {
            if (objetoSelecionado.equals(r.getCell())) {
                r.setAltura(Integer.parseInt(txtAltura.getText()));
                r.atualiza();
                modificarTitle();
                break;
            }
        }
        for (Agregacao ag : abas.get(tabSelecionada).getAgregacoes()) {
            if (objetoSelecionado.equals(ag.getCell())) {
                ag.setAltura(Integer.parseInt(txtAltura.getText()));
                ag.atualiza();
                modificarTitle();
                break;
            }
        }
    }//GEN-LAST:event_txtAlturaActionPerformed

    private void comboxCardinalidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboxCardinalidadeActionPerformed
        for (Ligacao l : getLigacoes()) {
            if (l.getLigacao().equals(objetoSelecionado)) {
                switch (comboxCardinalidade.getSelectedIndex()) {
                    case 0:
                        l.setCardinalidade(EnumCardinalidade.ZERO_PARA_UM);
                        break;
                    case 1:
                        l.setCardinalidade(EnumCardinalidade.UM_PARA_UM);
                        break;
                    case 2:
                        l.setCardinalidade(EnumCardinalidade.UM_PARA_MUITOS);
                        break;
                    case 3:
                        l.setCardinalidade(EnumCardinalidade.MUITOS_PARA_MUITOS);
                        break;
                }
                l.atualiza();
                break;
            }
        }
    }//GEN-LAST:event_comboxCardinalidadeActionPerformed

    private void isFKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isFKActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_isFKActionPerformed

    private void chkBoxIdentificadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBoxIdentificadorActionPerformed
        for (Relacionamento r : abas.get(tabSelecionada).getRelacionamentos()) {
            if (r.getCell().equals(objetoSelecionado)) {
                if (chkBoxIdentificador.isSelected()) {
                    r.setIdentificacao(true);
                } else {
                    r.setIdentificacao(false);
                }
                r.atualiza();
                modificarTitle();
                break;
            }
        }
    }//GEN-LAST:event_chkBoxIdentificadorActionPerformed

    private void chkBoxParticipacaoTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBoxParticipacaoTotalActionPerformed
        for (Ligacao l : abas.get(tabSelecionada).getLigacoes()) {
            if (l != null && l.getLigacao().equals(objetoSelecionado)) {
                if (chkBoxParticipacaoTotal.isSelected()) {
                    l.setTotal(true);
                } else {
                    l.setTotal(false);
                }
                l.atualiza();
                modificarTitle();
                break;
            }
        }
    }//GEN-LAST:event_chkBoxParticipacaoTotalActionPerformed

    private void chkBoxFracaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBoxFracaActionPerformed
        // TODO add your handling code here:
        for (Entidade e : abas.get(tabSelecionada).getEntidades()) {
            if (e.getCell().equals(objetoSelecionado)) {
                if (chkBoxFraca.isSelected()) {
                    e.setFraca(true);
                } else {
                    e.setFraca(false);
                }
                e.atualiza();
                modificarTitle();
                break;
            }
        }
    }//GEN-LAST:event_chkBoxFracaActionPerformed

    private void comboxDominioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboxDominioActionPerformed
        for (Atributo a : abas.get(tabSelecionada).getAtributos()) {
            if (a.getCell().equals(objetoSelecionado)) {
                a.setDominio((String) comboxDominio.getSelectedItem());
                for (int i = 0; i < Valores.getDOMINIOS().size(); i++) {
                    if (Valores.getDOMINIOS().get(i).equals(a.getDominio())) {
                        if (i >= Valores.getAtributosCT()) {
                            if (a.getComplemento().equals("")) {
                                a.setComplemento("10");
                            } else {
                                a.setComplemento(a.getComplemento());
                            }
                        } else {
                            a.setComplemento("");
                        }
                        txtDominio.setText(a.getComplemento());
                    }
                }
                a.atualiza();
            }
        }
    }//GEN-LAST:event_comboxDominioActionPerformed

    private void txtDominioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDominioActionPerformed
        for (Atributo a : abas.get(tabSelecionada).getAtributos()) {
            if (a.getCell().equals(objetoSelecionado)) {
                for (int i = 0; i < Valores.getDOMINIOS().size(); i++) {
                    if (Valores.getDOMINIOS().get(i).equals(a.getDominio())) {
                        if (i >= Valores.getAtributosCT()) {
                            a.setComplemento(txtDominio.getText());
                            a.atualiza();
                        } else {
                            txtDominio.setText("");
                            JOptionPane.showMessageDialog(null, "Não há tamanho");
                        }
                    }
                }
            }
        }

    }//GEN-LAST:event_txtDominioActionPerformed

    private void btnSelecionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionarActionPerformed
        if (buttonSelecionado != null && btnSelecionar != buttonSelecionado) {
            buttonSelecionado.setSelected(false);
        }
        cellLigacao1 = null;
        cellLigacao2 = null;
        btnSelecionar.setSelected(true);
        buttonSelecionado = btnSelecionar;
    }//GEN-LAST:event_btnSelecionarActionPerformed

    private void btnEntidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntidadeActionPerformed
        // TODO add your handling code here:
        if (btnEntidade.isSelected()) {
            btnEntidade.setSelected(false);
            buttonSelecionado = null;
        } else {
            buttonSelecionado.setSelected(false);
            btnEntidade.setSelected(true);
            buttonSelecionado = btnEntidade;
        }
    }//GEN-LAST:event_btnEntidadeActionPerformed

    private void btnAtributoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtributoActionPerformed
        // TODO add your handling code here:
        buttonSelecionado.setSelected(false);
        if (btnAtributo.isSelected()) {
            btnAtributo.setSelected(false);
            buttonSelecionado = null;
        } else {
            buttonSelecionado.setSelected(false);
            btnAtributo.setSelected(true);
            buttonSelecionado = btnAtributo;
        }
    }//GEN-LAST:event_btnAtributoActionPerformed

    private void btnRelacionamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRelacionamentoActionPerformed
        // TODO add your handling code here:
        if (btnRelacionamento.isSelected()) {
            btnRelacionamento.setSelected(false);
            buttonSelecionado = null;
        } else {
            buttonSelecionado.setSelected(false);
            btnRelacionamento.setSelected(true);
            buttonSelecionado = btnRelacionamento;
        }
    }//GEN-LAST:event_btnRelacionamentoActionPerformed

    private void btnAgregacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregacaoActionPerformed
        // TODO add your handling code here:
        if (btnAgregacao.isSelected()) {
            btnAgregacao.setSelected(false);
            buttonSelecionado = null;
        } else {
            buttonSelecionado.setSelected(false);
            btnAgregacao.setSelected(true);
            buttonSelecionado = btnAgregacao;
        }
    }//GEN-LAST:event_btnAgregacaoActionPerformed

    private void btnCriarRelacaoEntreEntidadesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCriarRelacaoEntreEntidadesActionPerformed
        // TODO add your handling code here:
        if (btnCriarRelacaoEntreEntidades.isSelected()) {
            btnCriarRelacaoEntreEntidades.setSelected(false);
            buttonSelecionado = null;
        } else {
            buttonSelecionado.setSelected(false);
            btnCriarRelacaoEntreEntidades.setSelected(true);
            buttonSelecionado = btnCriarRelacaoEntreEntidades;
        }
    }//GEN-LAST:event_btnCriarRelacaoEntreEntidadesActionPerformed

    private void btnConectaEntidadeRelacionamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConectaEntidadeRelacionamentoActionPerformed
        // TODO add your handling code here:
        if (btnConectaEntidadeRelacionamento.isSelected()) {
            btnConectaEntidadeRelacionamento.setSelected(false);
            buttonSelecionado = null;
        } else {
            buttonSelecionado.setSelected(false);
            btnConectaEntidadeRelacionamento.setSelected(true);
            buttonSelecionado = btnConectaEntidadeRelacionamento;
        }
    }//GEN-LAST:event_btnConectaEntidadeRelacionamentoActionPerformed

    private void btnAutoRelacionamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAutoRelacionamentoActionPerformed
        // TODO add your handling code here:
        if (btnAutoRelacionamento.isSelected()) {
            btnAutoRelacionamento.setSelected(false);
            buttonSelecionado = null;
        } else {
            buttonSelecionado.setSelected(false);
            btnAutoRelacionamento.setSelected(true);
            buttonSelecionado = btnAutoRelacionamento;
        }
    }//GEN-LAST:event_btnAutoRelacionamentoActionPerformed

    private void btnDisjuncaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisjuncaoActionPerformed
        // TODO add your handling code here:
        if (btnDisjuncao.isSelected()) {
            btnDisjuncao.setSelected(false);
            buttonSelecionado = null;
        } else {
            buttonSelecionado.setSelected(false);
            btnDisjuncao.setSelected(true);
            buttonSelecionado = btnDisjuncao;
        }
    }//GEN-LAST:event_btnDisjuncaoActionPerformed

    private void btnSobreposicaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSobreposicaoActionPerformed
        // TODO add your handling code here:
        if (btnSobreposicao.isSelected()) {
            btnSobreposicao.setSelected(false);
            buttonSelecionado = null;
        } else {
            buttonSelecionado.setSelected(false);
            btnSobreposicao.setSelected(true);
            buttonSelecionado = btnSobreposicao;
        }
    }//GEN-LAST:event_btnSobreposicaoActionPerformed

    private void btnDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletarActionPerformed
        // TODO add your handling code here:
        if (btnDeletar.isSelected()) {
            btnDeletar.setSelected(false);
            buttonSelecionado = null;
        } else {
            buttonSelecionado.setSelected(false);
            btnDeletar.setSelected(true);
            buttonSelecionado = btnDeletar;
        }
    }//GEN-LAST:event_btnDeletarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        for (Entidade e : getEntidades()) {
            //System.out.println(e.getNome());
        }
        //System.out.println("Atributos");
        for (Atributo a : getAtributos()) {
            //System.out.println(a.getNome() + " " + a.getCellDono().getValue());
        }
        //System.out.println("Relacionamentos");
        for (Relacionamento r : getRelacionamentos()) {
            //System.out.println(r.getNome());
        }
        //System.out.println("Ligações");
        for (Ligacao l : getLigacoes()) {
            //System.out.println(l.getSource().getValue() + " " + l.getTarget().getValue());
        }
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregacao;
    private javax.swing.JButton btnAtributo;
    private javax.swing.JButton btnAutoRelacionamento;
    private javax.swing.JButton btnConectaEntidadeRelacionamento;
    private javax.swing.JButton btnCriarRelacaoEntreEntidades;
    private javax.swing.JButton btnDeletar;
    private javax.swing.JButton btnDisjuncao;
    private javax.swing.JButton btnEntidade;
    private javax.swing.JButton btnRelacionamento;
    private javax.swing.JButton btnSelecionar;
    private javax.swing.JButton btnSobreposicao;
    private javax.swing.JCheckBox chkBoxComposto;
    private javax.swing.JCheckBox chkBoxDerivado;
    private javax.swing.JCheckBox chkBoxFraca;
    private javax.swing.JCheckBox chkBoxIdentificador;
    private javax.swing.JCheckBox chkBoxMultivalorado;
    private javax.swing.JCheckBox chkBoxParticipacaoTotal;
    private javax.swing.JComboBox<String> comboxCardinalidade;
    private javax.swing.JComboBox<String> comboxDominio;
    private javax.swing.JCheckBox isAutoIncrement;
    private javax.swing.JCheckBox isDefault;
    private javax.swing.JCheckBox isFK;
    private javax.swing.JCheckBox isNotNull;
    private javax.swing.JCheckBox isPK;
    private javax.swing.JCheckBox isUnique;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSeparator jSeparator;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblAltura;
    private javax.swing.JLabel lblCardinalidade;
    private javax.swing.JLabel lblDominio;
    private javax.swing.JLabel lblLargura;
    private javax.swing.JLabel lblNome;
    private javax.swing.JToolBar objectToolbar;
    private javax.swing.JPanel propriedades;
    private javax.swing.JPanel propriedadesAtributo;
    javax.swing.JTabbedPane tabs;
    private javax.swing.JTextField txtAltura;
    private javax.swing.JTextField txtDefaultValue;
    private javax.swing.JTextField txtDominio;
    private javax.swing.JTextField txtLargura;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent ae) {
        //quando for selecionado um  botão do TooBar
        propriedades.setVisible(false);
        if (buttonSelecionado != null && (JButton) ae.getSource() != null) {
            buttonSelecionado.setSelected(false);
        }
        if (!((JButton) ae.getSource()).equals(buttonSelecionado)) {
            buttonSelecionado = (JButton) ae.getSource();
        } else {
            buttonSelecionado.setSelected(false);
        }
    }

    public void copiar() {
        cola = (mxCell) objetoSelecionado;
    }

    public void colar(int px, int py) {
        if (cola != null) {
            if (cola.getStyle().contains("rectangle") || cola.getStyle().contains("Rectangle")) {
                Entidade entidade = new Entidade(cola.getValue().toString(), px, py, (int) cola.getGeometry().getWidth(), (int) cola.getGeometry().getHeight(), abas.get(tabSelecionada).getGraph());
                entidade.setCaracteristicas(cola.getStyle());
                entidade.desenha();
                abas.get(tabSelecionada).getEntidades().add(entidade);
                adicionaChild(cola, entidade.getCell(), 0);
                modificarTitle();
            }
            if (cola.getStyle().contains("rhombus") || cola.getStyle().contains("Rhombus")) {// criar relacionamento
                Relacionamento rel = new Relacionamento("NovoRelacionamento", px, py, (int) cola.getGeometry().getWidth(), (int) cola.getGeometry().getHeight(), abas.get(tabSelecionada).getGraph(), cola.isAutoRelacionamento());
                rel.setCaracteristicas(cell.getStyle());
                rel.setIdentificacao(cell.isIdentificacao());
                rel.desenha();
                abas.get(tabSelecionada).getRelacionamentos().add(rel);
                modificarTitle();
            }
            if (cola.getStyle().contains("ellipse") || cola.getStyle().contains("Ellipse") && cell != null) {
                if (cell.getStyle().contains("rectangle") || cell.getStyle().contains("Rectangle")) {
                    Atributo atributo = new Atributo("NovoAtributo", px, py, 100, 50, abas.get(tabSelecionada).getGraph(), cell);
                    atributo.setUnique(cell.isUnique());
                    atributo.setAutoIncrement(cell.isAuto_increment());
                    atributo.setNotNull(cell.isAuto_increment());
                    atributo.setPk(cell.isPk());
                    atributo.setDerivado(cell.isDerivado());
                    atributo.setComposto(cell.isComposto());
                    atributo.setPadrao(cell.getDefaultee());
                    atributo.desenha();
                    for (Entidade e : abas.get(tabSelecionada).getEntidades()) {
                        if (e.getCell().getId().equals(cell.getId())) {
                            e.getAtributos().add(atributo);
                            abas.get(tabSelecionada).getAtributos().add(atributo);
                            break;
                        }
                    }

                    adicionaChild(cola, atributo.getCell(), 1);
                } else if (cell.getStyle().contains("ellipse") || cell.getStyle().contains("Ellipse")) {
                    for (Atributo a : abas.get(tabSelecionada).getAtributos()) {
                        if (a.getCell().getId().equals(cell.getId())) {
                            Atributo atributo = new Atributo("NovoAtributo", px, py, 100, 50, abas.get(tabSelecionada).getGraph(), (mxCell) abas.get(tabSelecionada).getCellAt(px, py));
                            atributo.setUnique(cell.isUnique());
                            atributo.setAutoIncrement(cell.isAuto_increment());
                            atributo.setNotNull(cell.isAuto_increment());
                            atributo.setPk(cell.isPk());
                            atributo.setDerivado(cell.isDerivado());
                            atributo.setPadrao(cell.getDefaultee());
                            atributo.desenha();
                            a.setComposto(true);
                            a.getAtributos().add(atributo);
                            abas.get(tabSelecionada).getAtributos().add(atributo);
                            adicionaChild(cola, atributo.getCell(), 1);
                            break;
                        }
                    }

                } else if (cell.getStyle().contains("rhombus") || cell.getStyle().contains("Rhombus")) {
                    for (Relacionamento r : abas.get(tabSelecionada).getRelacionamentos()) {
                        if (r.getCell().getId().equals(cell.getId())) {
                            Atributo atributo = new Atributo("NovoAtributo", px, py, 120, 50, abas.get(tabSelecionada).getGraph(), (mxCell) abas.get(tabSelecionada).getCellAt(px, py));
                            atributo.setUnique(cell.isUnique());
                            atributo.setAutoIncrement(cell.isAuto_increment());
                            atributo.setNotNull(cell.isAuto_increment());
                            atributo.setPk(cell.isPk());
                            atributo.setDerivado(cell.isDerivado());
                            atributo.setPadrao(cell.getDefaultee());
                            atributo.desenha();
                            r.getAtributos().add(atributo);
                            abas.get(tabSelecionada).getAtributos().add(atributo);
                            adicionaChild(cola, atributo.getCell(), 1);
                            break;
                        }
                    }
                }
            }
            if (cola.getStyle().contains("aggregation")) {
                Agregacao agregacao = new Agregacao(cola.getValue().toString(), px, py, (int) cola.getGeometry().getWidth(), (int) cola.getGeometry().getHeight(), abas.get(tabSelecionada).getGraph());
                agregacao.setCaracteristicas(cola.getStyle());
                agregacao.desenha();
                abas.get(tabSelecionada).getAgregacoes().add(agregacao);
                adicionaChild(cola, agregacao.getCell(), 0);
                modificarTitle();
            }
            modificarTitle();
        }
    }

    //Mostrar propriedades do objeto clicado
    public void propriedades(mxCell cell2) {
        if (cell2 != null) {
            objetoSelecionado = cell2;
            //É Entidade
            if (cell.getTipo() != null && !cell2.getTipo().equals("")) {
                if (objetoSelecionado.getTipo().contains("heranca")) {
                    propriedades.setVisible(false);
                    return;
                }
                if (cell2.getTipo().contains("entidade")) {
                    lblAltura.setVisible(true);
                    txtAltura.setVisible(true);
                    lblLargura.setVisible(true);
                    txtLargura.setVisible(true);
                    lblNome.setVisible(true);
                    txtNome.setVisible(true);
                    chkBoxFraca.setVisible(true);
                    chkBoxIdentificador.setVisible(false);
                    chkBoxParticipacaoTotal.setVisible(false);
                    lblCardinalidade.setVisible(false);
                    comboxCardinalidade.setVisible(false);
                    propriedadesAtributo.setVisible(false);
                    for (Entidade e : abas.get(tabSelecionada).getEntidades()) {
                        if (e.getCell().equals(objetoSelecionado)) {
                            txtNome.setText(e.getNome());
                            if (e.isFraca()) {
                                chkBoxFraca.setSelected(true);
                            } else {
                                chkBoxFraca.setSelected(false);
                            }
                            txtLargura.setText("" + e.getLargura());
                            txtAltura.setText("" + e.getAltura());
                        }
                    }
                } //É atributo
                else if (cell2.getTipo().contains("atributo")) {
                    lblAltura.setVisible(true);
                    txtAltura.setVisible(true);
                    lblLargura.setVisible(true);
                    txtLargura.setVisible(true);
                    propriedadesAtributo.setVisible(true);
                    lblNome.setVisible(true);
                    txtNome.setVisible(true);
                    chkBoxFraca.setVisible(false);
                    chkBoxIdentificador.setVisible(false);
                    chkBoxParticipacaoTotal.setVisible(false);
                    lblCardinalidade.setVisible(false);
                    comboxCardinalidade.setVisible(false);
                    chkBoxComposto.setSelected(false);
                    chkBoxDerivado.setSelected(false);
                    chkBoxMultivalorado.setSelected(false);
                    isFK.setVisible(false);

                    for (Atributo a : abas.get(tabSelecionada).getAtributos()) {
                        if (a.getCell().equals(objetoSelecionado)) {
                            txtNome.setText(a.getNome());
                            if (a.isComposto()) {
                                chkBoxComposto.setSelected(true);
                            }
                            if (a.isMultivalorado()) {
                                chkBoxMultivalorado.setSelected(true);
                            }
                            if (a.isDerivado()) {
                                chkBoxDerivado.setSelected(true);
                            }
                            if (a.isPk()) {
                                isPK.setSelected(true);
                            } else {
                                isPK.setSelected(false);
                            }
                            if (a.isFk()) {
                                isFK.setSelected(true);
                            } else {
                                isFK.setSelected(false);
                            }
                            if (a.isAutoIncrement()) {
                                isAutoIncrement.setSelected(true);
                            } else {
                                isAutoIncrement.setSelected(false);
                            }
                            if (a.isNotNull()) {
                                isNotNull.setSelected(true);
                            } else {
                                isNotNull.setSelected(false);
                            }
                            if (a.isUnique()) {
                                isUnique.setSelected(true);
                            } else {
                                isUnique.setSelected(false);
                            }
                            if (a.getPadrao() != null) {
                                isDefault.setSelected(true);
                                txtDefaultValue.setText(a.getPadrao());
                                txtDefaultValue.setEnabled(true);
                            } else {
                                txtDefaultValue.setText("");
                                txtDefaultValue.setEnabled(false);
                                isDefault.setSelected(false);
                            }

                            comboxDominio.setSelectedItem(a.getDominio());
                            txtDominio.setText(a.getComplemento());
                            txtLargura.setText("" + a.getLargura());
                            txtAltura.setText("" + a.getAltura());
                        }
                    }
                } // É Relacionamento 
                else if (cell2.getTipo().contains("relacionamento")) {
                    lblAltura.setVisible(true);
                    txtAltura.setVisible(true);
                    lblLargura.setVisible(true);
                    txtLargura.setVisible(true);
                    propriedadesAtributo.setVisible(false);
                    lblNome.setVisible(true);
                    txtNome.setVisible(true);
                    chkBoxFraca.setVisible(false);
                    chkBoxParticipacaoTotal.setVisible(false);
                    lblCardinalidade.setVisible(false);
                    comboxCardinalidade.setVisible(false);
                    chkBoxIdentificador.setVisible(true);
                    for (Relacionamento r : abas.get(tabSelecionada).getRelacionamentos()) {
                        if (r.getCell().equals(cell2)) {
                            txtNome.setText(r.getNome());
                            if (r.isIdentificacao()) {
                                chkBoxComposto.setSelected(true);
                            } else {
                                chkBoxComposto.setSelected(false);
                            }
                            txtLargura.setText("" + r.getLargura());
                            txtAltura.setText("" + r.getAltura());
                        }
                    }
                } //É uma ligação
                else if (cell2.getTipo().equals("ligacao")) {
                    lblAltura.setVisible(false);
                    txtAltura.setVisible(false);
                    lblLargura.setVisible(false);
                    txtLargura.setVisible(false);
                    propriedadesAtributo.setVisible(false);
                    txtNome.setVisible(false);
                    lblNome.setVisible(false);
                    chkBoxFraca.setVisible(false);
                    chkBoxIdentificador.setVisible(false);
                    chkBoxParticipacaoTotal.setVisible(true);
                    lblCardinalidade.setVisible(true);
                    comboxCardinalidade.setVisible(true);
                    comboxCardinalidade.setSelectedIndex(cell.getCardinalidade());
                    for (Ligacao l : abas.get(tabSelecionada).getLigacoes()) {
                        if (l.getLigacao().equals(cell2)) {
                            if (l.isTotal()) {
                                chkBoxParticipacaoTotal.setSelected(true);
                            } else {
                                chkBoxParticipacaoTotal.setSelected(false);
                            }

                        }
                    }
                } else if (cell2.getTipo().equals("ligacaoEntidadeMae")) {
                    lblAltura.setVisible(false);
                    txtAltura.setVisible(false);
                    lblLargura.setVisible(false);
                    txtLargura.setVisible(false);
                    propriedadesAtributo.setVisible(false);
                    txtNome.setVisible(false);
                    lblNome.setVisible(false);
                    chkBoxFraca.setVisible(false);
                    chkBoxIdentificador.setVisible(false);
                    chkBoxParticipacaoTotal.setVisible(true);
                    lblCardinalidade.setVisible(false);
                    comboxCardinalidade.setVisible(false);
                    for (Ligacao l : abas.get(tabSelecionada).getLigacoes()) {
                        if (l.getLigacao().equals(cell2)) {
                            if (l.isTotal()) {
                                chkBoxParticipacaoTotal.setSelected(true);
                            } else {
                                chkBoxParticipacaoTotal.setSelected(false);
                            }
                        }
                    }
                }
                propriedades.setVisible(true);
            } else {
                propriedades.setVisible(false);
            }
            if (buttonSelecionado != null) {
                if (!buttonSelecionado.equals(btnSelecionar) && !buttonSelecionado.equals(btnCriarRelacaoEntreEntidades)) {
                    buttonSelecionado.setSelected(false);
                    buttonSelecionado = btnSelecionar;
                    buttonSelecionado.setSelected(true);
                }
            }
        }
    }

    public void prorFechar() {
        propriedades.setVisible(false);
    }

    public int getTabSelecionada() {
        return tabSelecionada;
    }

    public mxGraph getGrafico(int i) {
        return abas.get(i).getGraph();
    }

    public File getFile(int x) {
        return this.file.get(x);
    }

    public void setFile(File file, int i) {
        this.file.set(i, file);
    }

    public mxGraphComponent getAba(int index) {
        return abas.get(index);
    }

    public ArrayList<Aba> getAreaGraficas() {
        return abas;
    }

    public void modificarTitle() {
        tabs.getComponentAt(tabSelecionada).setFont(new Font(tabs.getFont().getFontName(), Font.BOLD, tabs.getFont().getSize()));
        abas.get(tabSelecionada).getArrayCell().clear();
        try {
            for (Entidade e : abas.get(tabSelecionada).getEntidades()) {
                abas.get(tabSelecionada).getArrayCell().add((mxCell) e.getCell().clone());
            }
            for (Relacionamento r : abas.get(tabSelecionada).getRelacionamentos()) {
                abas.get(tabSelecionada).getArrayCell().add((mxCell) r.getCell().clone());
            }
            for (Ligacao l : abas.get(tabSelecionada).getLigacoes()) {
                mxCell aux = (mxCell) l.getLigacao().clone();
                aux.setSource((mxCell) l.getLigacao().getSource().clone());
                aux.setTarget((mxCell) l.getLigacao().getTarget().clone());
                abas.get(tabSelecionada).getArrayCell().add(aux);
            }
            for (Atributo a : abas.get(tabSelecionada).getAtributos()) {
                mxCell aux = (mxCell) a.getCell().clone();
                mxCell at = (mxCell) a.getCell().getTarget().clone();
                aux.setTarget(at);
                abas.get(tabSelecionada).getArrayCell().add(aux);
            }
            for (Heranca h : abas.get(tabSelecionada).getHerancas()) {
                abas.get(tabSelecionada).getArrayCell().add((mxCell) h.getCellTipo().clone());
            }
            for (Agregacao ag : abas.get(tabSelecionada).getAgregacoes()) {
                abas.get(tabSelecionada).getArrayCell().add((mxCell) ag.getCell().clone());
            }
        } catch (Exception e) {
        }

        abas.get(tabSelecionada).setPosicaoHistorico(abas.get(tabSelecionada).getPosicaoHistorico() + 1); // Adiciona  mais uma posição ao abas.get(tabSelecionada).getHistorico() // PosHist armzarna a psoição do array do abas.get(tabSelecionada).getHistorico()
        abas.get(tabSelecionada).getHistorico().add(abas.get(tabSelecionada).getPosicaoHistorico(), new Historico((ArrayList<mxCell>) abas.get(tabSelecionada).getArrayCell().clone()));
        for (int i = abas.get(tabSelecionada).getPosicaoHistorico() + 1; i < abas.get(tabSelecionada).getHistorico().size(); i++) {
            abas.get(tabSelecionada).getHistorico().remove(i);
        }
        if (tabs.getTitleAt(tabSelecionada).contains("Novo Arquivo") || tabs.getTitleAt(tabSelecionada).contains("*Novo Arquivo")) {
            tabs.setTitleAt(tabSelecionada, "*Novo Arquivo " + tabSelecionada);
        } else {
            tabs.getComponentAt(tabSelecionada).setFont(new Font(tabs.getFont().getFontName(), Font.BOLD, tabs.getFont().getSize()));
            tabs.setTitleAt(tabSelecionada, "*" + file.get(tabSelecionada).getName());
        }
    }

    public void deletar() {
        if (objetoSelecionado != null) {
            abas.get(tabSelecionada).getGraph().getModel().beginUpdate();
            if (objetoSelecionado.getTipo().contains("entidade")) {
                for (Entidade e : getEntidades()) {
                    if (e.getCell().equals(objetoSelecionado)) {
                        for (Heranca h : getHerancas()) {
                            if (e.getCell().equals(h.getEntidadeMae())) {
                                h.getCellTipo().removeFromParent();
                                getHerancas().remove(h);
                                break;
                            }
                            for (Entidade filha : h.getEntidadesFilhas()) {
                                if (filha.equals(e)) {
                                    h.getEntidadesFilhas().remove(filha);
                                    filha.getCell().removeFromParent();
                                    break;
                                }
                            }
                        }
                        getEntidades().remove(e);
                        ArrayList<Atributo> x = new ArrayList<>();
                        for (Atributo a : getAtributos()) {
                            for (Atributo a1 : e.getAtributos()) {
                                if (a.getCell().equals(a1.getCell())) {
                                    x.add(a);
                                    a1.getCell().removeFromParent();
                                }
                            }
                        }
                        removeChild(e.getAtributos());
                        abas.get(tabSelecionada).getGraph().removeCells();
                        abas.get(tabSelecionada).getGraph().refresh();
                        propriedades.setVisible(false);
                        modificarTitle();
                        break;
                    }
                }

            } else if (objetoSelecionado.getTipo().equals("atributo")) {
                for (Atributo a : getAtributos()) {
                    if (a.getCell().equals(objetoSelecionado)) {
                        if (a.getCellDono().getTipo().contains("entidade")) {
                            for (Entidade e : getEntidades()) {
                                if (e.getCell().equals(a.getCellDono())) {
                                    e.getAtributos().remove(a);
                                }
                            }
                        } else if (a.getCellDono().getTipo().equals("atributo")) {
                            for (Atributo at : getAtributos()) {
                                if (at.getCell().equals(a.getCellDono())) {
                                    at.getAtributos().remove(a);
                                }
                            }
                        } else if (a.getCellDono().getTipo().equals("relacionamento")) {
                            for (Relacionamento r : getRelacionamentos()) {
                                if (r.getCell().equals(a.getCellDono())) {
                                    r.getAtributos().remove(a);
                                }
                            }
                        }

                        abas.get(tabSelecionada).getAtributos().remove(a);
                        removeChild(a.getAtributos());
                        abas.get(tabSelecionada).getGraph().removeCells();
                        abas.get(tabSelecionada).getGraph().refresh();
                        propriedades.setVisible(false);
                        modificarTitle();
                        break;
                    }
                }
            } else if (objetoSelecionado.getTipo().equals("relacionamento")) {
                for (Relacionamento r : getRelacionamentos()) {
                    if (r.getCell().equals(objetoSelecionado)) {
                        abas.get(tabSelecionada).getRelacionamentos().remove(r);
                        removeChild(r.getAtributos());
                        abas.get(tabSelecionada).getGraph().removeCells();
                        abas.get(tabSelecionada).getGraph().refresh();
                        propriedades.setVisible(false);
                        modificarTitle();
                        break;
                    }
                }
            } else if (objetoSelecionado.getTipo().equals("agregacao")) {
                for (Agregacao ag : getAgregacoes()) {
                    if (ag.getCell().equals(objetoSelecionado)) {
                        getAgregacoes().remove(ag);
                        removeChild(ag.getAtributos());
                        abas.get(tabSelecionada).getGraph().removeCells();
                        abas.get(tabSelecionada).getGraph().refresh();
                        propriedades.setVisible(false);
                        modificarTitle();
                        break;
                    }
                }
            }
            abas.get(tabSelecionada).getGraph().getModel().endUpdate();
        }
    }

    public void removeChild(ArrayList<Atributo> atrs) {
        for (Atributo a : atrs) {
            if (a.getAtributos().size() <= 0) {
                a.getCell().removeFromParent();
                abas.get(tabSelecionada).getAtributos().remove(a);
            } else {
                a.getCell().removeFromParent();
                removeChild(a.getAtributos());
                abas.get(tabSelecionada).getAtributos().remove(a);
            }
        }
    }

    public void adicionaChild(mxCell a, mxCell b, int modo) {
        if (modo == 0) {
            for (Entidade e : getEntidades()) {
                if (e.getCell().equals(a)) {
                    for (Atributo ab : e.getAtributos()) {
                        Atributo t = new Atributo(ab.getNome(), (int) b.getGeometry().getCenterX(), (int) b.getGeometry().getCenterY(), ab.getLargura(), ab.getAltura(), abas.get(tabSelecionada).getGraph(), b);
                        getAtributos().add(t);
                        for (Entidade er : getEntidades()) {
                            if (er.getCell().equals(b)) {
                                er.getAtributos().add(t);
                                t.setUnique(ab.isUnique());
                                t.setAutoIncrement(ab.isAutoIncrement());
                                t.setNotNull(ab.isNotNull());
                                t.setPk(ab.isPk());
                                t.setComposto(ab.isComposto());
                                t.setMultivalorado(ab.isMultivalorado());
                                t.setDerivado(ab.isDerivado());
                                t.setPadrao(ab.getPadrao());
                                t.desenha();
                                t.atualiza();
                                if (ab.getAtributos().size() >= 1) {
                                    adicionaChild(ab.getCell(), t.getCell(), 1);
                                }
                                break;
                            }

                        }
                    }
                    break;
                }
            }
        } else if (modo == 1) {
            for (Atributo e : getAtributos()) {
                if (e.getCell().equals(a)) {
                    for (Atributo ab : e.getAtributos()) {
                        Atributo t = new Atributo(ab.getNome(), (int) b.getGeometry().getCenterX(), (int) b.getGeometry().getCenterY(), ab.getLargura(), ab.getAltura(), abas.get(tabSelecionada).getGraph(), b);
                        getAtributos().add(t);
                        for (Atributo er : getAtributos()) {
                            if (er.getCell().equals(b)) {
                                er.getAtributos().add(t);
                                t.setUnique(ab.isUnique());
                                t.setAutoIncrement(ab.isAutoIncrement());
                                t.setNotNull(ab.isNotNull());
                                t.setPk(ab.isPk());
                                t.setComposto(ab.isComposto());
                                t.setMultivalorado(ab.isMultivalorado());
                                t.setDerivado(ab.isDerivado());
                                t.setPadrao(ab.getPadrao());
                                t.desenha();
                                t.atualiza();
                                if (ab.getAtributos().size() >= 1) {
                                    adicionaChild(ab.getCell(), t.getCell(), 1);
                                }
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        } else {

        }
    }

    public void desfazer() {
        if (abas.get(tabSelecionada).getPosicaoHistorico() > 0) {
            abas.get(tabSelecionada).setPosicaoHistorico(abas.get(tabSelecionada).getPosicaoHistorico() - 1);
            apagarGeral();
            atualizarAreaGrafica(1);
        }
    }

    public void refazer() {
        if (abas.get(tabSelecionada).getPosicaoHistorico() < abas.get(tabSelecionada).getHistorico().size() - 1) {
            abas.get(tabSelecionada).setPosicaoHistorico(abas.get(tabSelecionada).getPosicaoHistorico() + 1);
            apagarGeral();
            atualizarAreaGrafica(1);
        }
    }

    public void atualizarAreaGrafica(int modo) {
        //Modo abertura de arquivo
        if (modo == 0) {
            //Percorre á area 2 vezes
            //Da esquerda pra direita
            for (int i = 0; i < abas.get(tabs.getTabCount() - 1).getWidth(); i += 10) {
                //De cima pra baixo
                for (int y = 0; y < abas.get(tabs.getTabCount() - 1).getHeight(); y += 10) {
                    //Procura objeto na posição(i,y)
                    mxCell cell = (mxCell) abas.get(tabs.getTabCount() - 1).getCellAt(i, y);
                    //Identificar tipo de objeto
                    if (cell != null && cell.getTipo() != null) {
                        if ((cell.getTipo().contains("entidade"))) {
                            int teste = 0;
                            for (Entidade e : abas.get(tabSelecionada).getEntidades()) {
                                if (e.getCell() != null && e.getCell().getId().equals(cell.getId())) {
                                    teste = 1;
                                    break;
                                }
                            }
                            if (teste == 0) {
                                abas.get(tabSelecionada).getEntidades().add(new Entidade(cell, abas.get(tabs.getTabCount() - 1).getGraph()));
                                break;
                            }
                        } else if ((cell.getTipo().contains("heranca"))) {
                            int teste = 0;
                            for (Heranca h : abas.get(tabSelecionada).getHerancas()) {
                                if (h.getCellTipo() != null && h.getCellTipo().getId().equals(cell.getId())) {
                                    teste = 1;
                                    break;
                                }
                            }
                            if (teste == 0) {
                                Heranca h = new Heranca(cell, abas.get(tabs.getTabCount() - 1).getGraph());
                                for (Object o : h.getCellTipo().getEdges()) {
                                    mxCell ent = (mxCell) o;
                                    boolean existe = false;
                                    for (Entidade e : h.getEntidadesFilhas()) {
                                        if (e.getId().equals(ent.getTarget().getId())) {
                                            existe = true;
                                            break;
                                        }
                                    }
                                    if (!existe) {
                                        Entidade e = new Entidade((mxCell) ent.getTarget(), abas.get(tabs.getTabCount() - 1).getGraph());
                                        if (e.getCell().getTipo().equals("entidadeMae")) {
                                            h.setEntidadeMae(e.getCell());
                                        } else {
                                            h.getEntidadesFilhas().add(e);
                                        }
                                    }

                                }
                                abas.get(tabSelecionada).getHerancas().add(h);
                            }
                        } else if ((cell.getTipo().equals("atributo"))) {
                            int teste = 0;
                            for (Atributo a : abas.get(tabSelecionada).getAtributos()) {
                                if (a.getCell() != null && a.getCell().getId().equals(cell.getId())) {
                                    teste = 1;
                                    break;
                                }
                            }
                            if (teste == 0) {
                                Atributo a = new Atributo(cell, abas.get(tabs.getTabCount() - 1).getGraph());
                                abas.get(tabSelecionada).getAtributos().add(a);
                            }
                        } else if ((cell.getTipo().equals("relacionamento"))) {
                            int teste = 0;
                            for (Relacionamento r : abas.get(tabSelecionada).getRelacionamentos()) {
                                if (r.getCell() != null && r.getCell().getId().equals(cell.getId())) {
                                    teste = 1;
                                    break;
                                }
                            }
                            if (teste == 0) {
                                abas.get(tabSelecionada).getRelacionamentos().add(new Relacionamento(cell, abas.get(tabs.getTabCount() - 1).getGraph()));
                                break;
                            }
                        } else if ((cell.getTipo().contains("ligacao"))) {
                            int teste = 0;
                            for (Ligacao l : abas.get(tabSelecionada).getLigacoes()) {
                                if (l.getLigacao() != null && l.getLigacao().getId().equals(cell.getId())) {
                                    teste = 1;
                                    break;
                                }
                            }
                            if (teste == 0) {
                                Ligacao l = new Ligacao(cell, abas.get(tabs.getTabCount() - 1).getGraph());
                                abas.get(tabSelecionada).getLigacoes().add(l);
                                break;
                            }
                        }
                    }
                }
            }
            for (Atributo a : this.getAtributos()) {
                for (Entidade e : this.getEntidades()) {
                    if (e.getCell().equals(a.getCellDono())) {
                        e.getAtributos().add(a);
                        break;
                    }
                }
                for (Atributo a1 : this.getAtributos()) {
                    if (a1.getCell().equals(a.getCellDono())) {
                        a1.getAtributos().add(a);
                        break;
                    }
                }
                for (Relacionamento r : this.getRelacionamentos()) {
                    if (r.getCell().equals(a.getCellDono())) {
                        r.getAtributos().add(a);
                        break;
                    }
                }
            }
            for (Ligacao l : getLigacoes()) {
                for (Relacionamento r : getRelacionamentos()) {
                    if (r.getCell().equals(l.getSource())) {
                        r.getLigacoes().add(l);
                        break;
                    }
                }
            }
            for (Ligacao l : getLigacoes()) {
                for (Heranca h : getHerancas()) {
                    if (h.getCellTipo().equals(l.getSource())) {
                        h.setCellTotal(l);
                        break;
                    }
                }
            }

        } else { // MODO 1 - HISTORICO
            abas.get(tabSelecionada).setArrayCell((ArrayList<mxCell>) abas.get(tabSelecionada).getHistorico().get(abas.get(tabSelecionada).getPosicaoHistorico()).atual.clone());
            for (mxCell mx : abas.get(tabSelecionada).getArrayCell()) {
                if (mx.getTipo().contains("entidade")) {
                    Entidade e = new Entidade(mx, abas.get(tabSelecionada).getGraph());
                    e.desenha();
                    e.atualiza();
                    abas.get(tabSelecionada).getEntidades().add(e);
                } else if (mx.getTipo().contains("heranca")) {
                    Heranca h = new Heranca(mx, abas.get(tabSelecionada).getGraph());
                    h.desenha();
                    getHerancas().add(h);
                } else if (mx.getTipo().equals("relacionamento")) {
                    Relacionamento r = new Relacionamento(mx, abas.get(tabSelecionada).getGraph());
                    r.desenha();
                    r.atualiza();
                    abas.get(tabSelecionada).getRelacionamentos().add(r);
                } else if (mx.getTipo().equals("agregacao")) {
                    Agregacao ag = new Agregacao(mx, abas.get(tabSelecionada).getGraph());
                    ag.desenha();
                    ag.atualiza();
                    abas.get(tabSelecionada).getAgregacoes().add(ag);
                } else if (mx.getTipo().equals("atributo")) {
                    Atributo a = new Atributo(mx, abas.get(tabSelecionada).getGraph());
                    if (a.getCellDono().getTipo().contains("entidade")) {
                        for (Entidade cell2 : abas.get(tabSelecionada).getEntidades()) {
                            if (a.getCellDono().equals(cell2.getCell())) {
                                a.setCellDono(cell2.getCell());
                            }
                        }
                    } else if (a.getCellDono().getTipo().equals("relacionamento")) {
                        for (Relacionamento cell2 : abas.get(tabSelecionada).getRelacionamentos()) {
                            if (a.getCellDono().equals(cell2.getCell())) {
                                a.setCellDono(cell2.getCell());
                            }
                        }
                    } else if (a.getCellDono().getTipo().equals("atributo")) {
                        for (Atributo cell2 : abas.get(tabSelecionada).getAtributos()) {
                            if (a.getCellDono().equals(cell2.getCell())) {
                                a.setCellDono(cell2.getCell());
                            }
                        }
                    }
                    a.desenha();
                    a.atualiza();
                    abas.get(tabSelecionada).getAtributos().add(a);
                    if (a.getCellDono().getTipo().contains("entidade")) {
                        for (Entidade e : abas.get(tabSelecionada).getEntidades()) {
                            if (e.getCell().equals(a.getCellDono())) {
                                e.getAtributos().add(a);
                            }
                        }
                    } else if (a.getCellDono().getTipo().equals("relacionamento")) {
                        for (Relacionamento r : abas.get(tabSelecionada).getRelacionamentos()) {
                            if (r.getCell().getId().equals(a.getCellDono().getId())) {
                                r.getAtributos().add(a);
                            }
                        }
                    } else if (a.getCellDono().getTipo().equals("atributo")) {
                        for (Atributo ab : abas.get(tabSelecionada).getAtributos()) {
                            if (ab.getCell().getId().equals(a.getCellDono().getId())) {
                                ab.getAtributos().add(a);
                            }
                        }
                    }
                } else if (mx.getTipo().equals("ligacao")) {
                    Ligacao l = new Ligacao(mx, abas.get(tabSelecionada).getGraph());
                    if (l.getSource().getTipo().equals("relacionamento")) {
                        for (Relacionamento r : abas.get(tabSelecionada).getRelacionamentos()) {
                            if (l.getSource().equals(r.getCell())) {
                                l.setSource(r.getCell());
                                l.setLigacao(mx);
                                l.getLigacao().setSource(r.getCell());
                                r.getLigacoes().add(l);
                            }
                        }
                    }
                    if (l.getTarget().getTipo().contains("entidade")) {
                        for (Entidade cell2 : abas.get(tabSelecionada).getEntidades()) {
                            if (l.getTarget().equals(cell2.getCell())) {
                                l.setTarget(cell2.getCell());
                                l.setLigacao(mx);
                                l.getLigacao().setTarget(cell2.getCell());
                            }
                        }
                    }
                    if (l.getTarget().getTipo().equals("agregacao")) {
                        for (Agregacao cell2 : abas.get(tabSelecionada).getAgregacoes()) {
                            if (l.getTarget().equals(cell2.getCell())) {
                                l.setTarget(cell2.getCell());
                                l.setLigacao(mx);
                                l.getLigacao().setTarget(cell2.getCell());
                            }
                        }
                    }
                    l.desenha();
                    l.atualiza();
                    abas.get(tabSelecionada).getLigacoes().add(l);
                }
            }

            for (mxCell mx : abas.get(tabSelecionada).getArrayCell()) {
                if (mx.getTipo().equals("ligacaoEntidadeMae")) {
                    Heranca aux = null;
                    Ligacao l = new Ligacao(mx, abas.get(tabSelecionada).getGraph());
                    l.setCardinalidade(EnumCardinalidade.NENHUM);
                    if (l.getSource().getTipo().contains("heranca")) {
                        for (Heranca h : getHerancas()) {
                            if (l.getSource().equals(h.getCellTipo())) {
                                aux = h;
                                l.setSource(h.getCellTipo());
                                l.setLigacao(mx);
                                l.getLigacao().setSource(h.getCellTipo());
                                h.setCellTotal(l);
                            }
                        }
                        for (Entidade e : getEntidades()) {
                            if (l.getTarget().equals(e.getCell())) {
                                aux.setEntidadeMae(e.getCell());
                                l.setTarget(e.getCell());
                                l.setLigacao(mx);
                                l.getLigacao().setTarget(e.getCell());
                            }
                        }
                    }
                    l.desenha();
                    l.atualiza();
                    abas.get(tabSelecionada).getLigacoes().add(l);
                } else if (mx.getTipo().equals("ligacaoEntidadeFilha")) {
                    Ligacao l = new Ligacao(mx, abas.get(tabSelecionada).getGraph());
                    l.setCardinalidade(EnumCardinalidade.NENHUM);
                    Heranca aux = null;
                    if (l.getSource().getTipo().contains("heranca")) {
                        for (Heranca h : getHerancas()) {
                            if (l.getSource().equals(h.getCellTipo())) {
                                l.setSource(h.getCellTipo());
                                l.setLigacao(mx);
                                l.getLigacao().setSource(h.getCellTipo());
                                h.setCellTotal(l);
                                aux = h;
                            }
                        }
                        for (Entidade e : getEntidades()) {
                            if (l.getTarget().equals(e.getCell())) {
                                aux.getEntidadesFilhas().add(e);
                                l.setTarget(e.getCell());
                                l.setLigacao(mx);
                                l.getLigacao().setTarget(e.getCell());
                            }
                        }
                    }
                    l.desenha();
                    l.atualiza();
                    abas.get(tabSelecionada).getLigacoes().add(l);
                }
            }
        }
    }

    private void apagarGeral() {
        abas.get(tabSelecionada).getEntidades().clear();
        abas.get(tabSelecionada).getAtributos().clear();
        abas.get(tabSelecionada).getRelacionamentos().clear();
        abas.get(tabSelecionada).getLigacoes().clear();
        abas.get(tabSelecionada).getHerancas().clear();
        abas.get(tabSelecionada).getAgregacoes().clear();
        abas.get(tabSelecionada).getGraph().removeCells(abas.get(tabSelecionada).getGraph().getChildVertices(abas.get(tabSelecionada).getGraph().getDefaultParent()));
    }

    public String getNomes(int i) {
        return nomes.get(i);
    }

    public void setNomes(String nomes, int i) {
        this.nomes.set(i, nomes);
    }

    public void remover(int i) {
        tabs.removeTabAt(i);
        nomes.remove(i);

        file.remove(i);
        abas.remove(i);
        tabSelecionada = tabs.getSelectedIndex();
    }

    public void maisZoom() {
        abas.get(tabSelecionada).zoomIn();
    }

    public void menosZoom() {
        abas.get(tabSelecionada).zoomOut();
    }

    public void proximaGuia() {
        if (tabSelecionada < tabs.getTabCount() - 1) {
            tabSelecionada++;
            tabs.setSelectedIndex(tabSelecionada);
        } else {
            tabSelecionada = 0;
            tabs.setSelectedIndex(tabSelecionada);
        }
    }

    public void guiaAnterior() {
        if (tabSelecionada > 0) {
            tabSelecionada--;
            tabs.setSelectedIndex(tabSelecionada);
        } else {
            tabSelecionada = tabs.getTabCount() - 1;
            tabs.setSelectedIndex(tabSelecionada);

        }
    }

    private class Mouse extends MouseAdapter {

        public void mousePressed(MouseEvent me) {
            //quando for clicado em alguma área do painel mxGraph
            px = (int) me.getX();
            py = (int) me.getY();
            cell = null;
            propriedades.setVisible(false);
            if (px <= propriedades.getX() && propriedades.isVisible()) {
                propriedades.revalidate();
            } else {
                cell = (mxCell) abas.get(tabSelecionada).getCellAt(px, py);
                if (((me.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) && cell != null && !cell.isEdge()) {//Botão direito do mouse e encima de um objeto
                    btnDireito.setPreferredSize(new Dimension(copiar.getPreferredSize().width, copiar.getPreferredSize().height * 3 + 15));
                    btnDireito.setSize(btnDireito.getWidth(), copiar.getHeight());
                    btnDireito.show(abas.get(tabSelecionada), px, py);
                    objetoSelecionado = cell;
                    deletar.setVisible(true);
                    copiar.setVisible(true);
                    colar.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent ev) {
                            colar(px, py);
                        }
                    });

                    copiar.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            copiar();
                        }
                    });
                } else if (((me.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) && cell == null) {
                    btnDireito.setPreferredSize(new Dimension(copiar.getPreferredSize().width, copiar.getPreferredSize().height + 5));
                    btnDireito.show(abas.get(tabSelecionada), px, py);
                    deletar.setVisible(false);
                    copiar.setVisible(false);
                    colar.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent ev) {
                            colar(px, py);
                        }
                    });
                    buttonSelecionado = null;
                } else if (cell == null) { //botão esquerdo e não foi encima de um objetoGraph
                    if (buttonSelecionado != null) { // criar entidade
                        if (buttonSelecionado.equals(btnEntidade)) {
                            Entidade entidade = new Entidade("NovaEntidade", px /*- Entidade.LARGURA_INICIAL/2*/, py /*- Entidade.ALTURA_INICIAL/2*/, Entidade.LARGURA_INICIAL, Entidade.ALTURA_INICIAL, abas.get(tabSelecionada).getGraph());
                            entidade.desenha();
                            abas.get(tabSelecionada).getEntidades().add(entidade);
                            modificarTitle();
                            abas.get(tabSelecionada).getHistorico().subList(abas.get(tabSelecionada).getPosicaoHistorico() + 1, getHist().size()).clear();
                            buttonSelecionado.setSelected(false);
                            buttonSelecionado = btnSelecionar;
                            buttonSelecionado.setSelected(true);

                        }
                        if (buttonSelecionado.equals(btnRelacionamento)) {// criar relacionamento
                            Relacionamento rel = new Relacionamento("NovoRelacionamento", px, py, 140, 60, abas.get(tabSelecionada).getGraph(), false);
                            rel.desenha();
                            buttonSelecionado.setSelected(false);
                            buttonSelecionado = btnSelecionar;
                            buttonSelecionado.setSelected(true);
                            abas.get(tabSelecionada).getRelacionamentos().add(rel);
                            modificarTitle();
                            abas.get(tabSelecionada).getHistorico().subList(abas.get(tabSelecionada).getPosicaoHistorico() + 1, getHist().size()).clear();

                        }
                        if (buttonSelecionado.equals(btnAgregacao)) {// criar relacionamento
                            Agregacao ag = new Agregacao("NovaAgregação", px, py, 120, 60, abas.get(tabSelecionada).getGraph());
                            ag.desenha();
                            buttonSelecionado.setSelected(false);
                            buttonSelecionado = btnSelecionar;
                            buttonSelecionado.setSelected(true);
                            abas.get(tabSelecionada).getAgregacoes().add(ag);
                            modificarTitle();
                            abas.get(tabSelecionada).getHistorico().subList(abas.get(tabSelecionada).getPosicaoHistorico() + 1, getHist().size()).clear();
                        }
                        if (buttonSelecionado.equals(btnAtributo)) {// atributo tentando ser criado em nenhum lugar especifico
                            btnAtributo.setSelected(false);
                            buttonSelecionado = btnSelecionar;
                            btnSelecionar.setSelected(true);
                        }
                    }
                } else if (buttonSelecionado != null) { //botão ToolBar selecionado
                    objetoSelecionado = cell;
                    if (buttonSelecionado.equals(btnCriarRelacaoEntreEntidades)) {
                        if (cellLigacao1 == null) {
                            if (cell.getStyle().contains("ellipse") || cell.getStyle().contains("Ellipse")) {
                                JOptionPane.showMessageDialog(null, "Não é possível usar a opção Ligação com atributos", "Erro", JOptionPane.ERROR_MESSAGE);
                                buttonSelecionado = null;
                            } else {
                                cellLigacao1 = cell;
                            }
                        } else {
                            if (cellLigacao2 == null) {
                                if (cell.getStyle().contains("ellipse") || cell.getStyle().contains("ellipse")) {
                                    JOptionPane.showMessageDialog(null, "Não é possível usar a opção Ligação com atributos", "Erro", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    cellLigacao2 = cell;
                                    if ((cellLigacao1.getStyle().contains("rectangle") || cellLigacao1.getStyle().contains("Rectangle"))
                                            && ((cellLigacao2.getStyle().contains("rectangle") || cellLigacao2.getStyle().contains("Rectangle")))) {
                                        int pxRelacao;
                                        int pyRelacao;
                                        if (cellLigacao1.getGeometry().getX() < cellLigacao2.getGeometry().getX()) {
                                            pxRelacao = (int) ((cellLigacao1.getGeometry().getX() + cellLigacao1.getGeometry().getWidth() + cellLigacao2.getGeometry().getX()) / 2);

                                        } else {
                                            pxRelacao = (int) ((cellLigacao2.getGeometry().getX() + cellLigacao2.getGeometry().getWidth() + cellLigacao1.getGeometry().getX()) / 2);
                                        }
                                        if (cellLigacao1.getGeometry().getY() < cellLigacao2.getGeometry().getY()) {
                                            pyRelacao = (int) ((cellLigacao1.getGeometry().getY() + cellLigacao1.getGeometry().getHeight() + cellLigacao2.getGeometry().getY()) / 2);
                                        } else {
                                            pyRelacao = (int) ((cellLigacao1.getGeometry().getY() + cellLigacao1.getGeometry().getHeight() + cellLigacao2.getGeometry().getY()) / 2);
                                        }
                                        Relacionamento r = new Relacionamento("NovoRelacionamento", pxRelacao, pyRelacao, 140, 60, abas.get(tabSelecionada).getGraph(), false);
                                        r.desenha();
                                        getRelacionamentos().add(r);
                                        Ligacao l1 = new Ligacao(abas.get(tabSelecionada).getGraph(), r.getCell(), cellLigacao1, EnumCardinalidade.UM_PARA_UM);
                                        Ligacao l2 = new Ligacao(abas.get(tabSelecionada).getGraph(), r.getCell(), cellLigacao2, EnumCardinalidade.UM_PARA_UM);
                                        l1.desenha();
                                        l2.desenha();
                                        abas.get(tabSelecionada).getLigacoes().add(l1);
                                        abas.get(tabSelecionada).getLigacoes().add(l2);
                                        r.getLigacoes().add(l1);
                                        r.getLigacoes().add(l2);
                                        modificarTitle();
                                        cellLigacao1 = null;
                                        cellLigacao2 = null;
                                        buttonSelecionado.setSelected(false);
                                        buttonSelecionado = btnSelecionar;
                                        buttonSelecionado.setSelected(true);
                                        abas.get(tabSelecionada).getHistorico().subList(abas.get(tabSelecionada).getPosicaoHistorico() + 1, getHist().size()).clear();
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Apenas entidade e entidade", "Erro", JOptionPane.ERROR_MESSAGE);
                                        cellLigacao1 = null;
                                        cellLigacao2 = null;
                                    }
                                }
                            }
                        }
                    } else if (buttonSelecionado.equals(btnConectaEntidadeRelacionamento)) {
                        if (cellLigacao1 == null) {
                            boolean ok = true;
                            if (cell.getTipo().contains("relacionamento")) {
                                for (Relacionamento r : getRelacionamentos()) {
                                    ok = true;
                                    if (r.isAutoRelacionamento()) {
                                        JOptionPane.showMessageDialog(null, "Não é possível usar a opção Ligação com auto relacionamneto", "Erro", JOptionPane.ERROR_MESSAGE);
                                        ok = false;
                                        buttonSelecionado.setSelected(false);
                                        buttonSelecionado = btnSelecionar;
                                        buttonSelecionado.setSelected(true);
                                    }
                                }
                                if (ok==true) {
                                    cellLigacao1 = cell;
                                }
                            } else if (cell.getTipo().equals("atributo")) {
                                JOptionPane.showMessageDialog(null, "Não é possível usar a opção Ligação com Atributos", "Erro", JOptionPane.ERROR_MESSAGE);
                                buttonSelecionado.setSelected(false);
                                buttonSelecionado = btnSelecionar;
                                buttonSelecionado.setSelected(true);
                            } else {
                                cellLigacao1 = cell;
                            }
                        } else {
                            if (cellLigacao2 == null) {
                                if (cell.getTipo().contains("relacionamento")) {
                                    boolean ok = true;
                                    for (Relacionamento r : getRelacionamentos()) {
                                        ok = true;
                                        if (r.isAutoRelacionamento()) {
                                            ok = false;
                                            JOptionPane.showMessageDialog(null, "Não é possível usar a opção Ligação com auto relacionamneto", "Erro", JOptionPane.ERROR_MESSAGE);
                                        }
                                    }
                                    if (ok==true) {
                                        cellLigacao2 = cell;
                                    }
                                } 
                                if (cell.getTipo().equals("atributo")) {
                                    JOptionPane.showMessageDialog(null, "Não é possível usar a opção Ligação com Atributos", "Erro", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    cellLigacao2 = cell;
                                    if ((cellLigacao1.getTipo().contains("entidade") && !cellLigacao2.getTipo().contains("entidade"))
                                            || (cellLigacao1.getTipo().equals("relacionamento") && !cellLigacao2.getTipo().equals("relacionamento"))
                                            || (cellLigacao1.getTipo().equals("agregacao") && !cellLigacao2.getTipo().equals("agregacao"))) {
                                        Ligacao l = new Ligacao(abas.get(tabSelecionada).getGraph(), cellLigacao2, cellLigacao1, EnumCardinalidade.UM_PARA_UM);
                                        if (cellLigacao2.getTipo().equals("relacionamento")) {
                                            for (Relacionamento r : getRelacionamentos()) {
                                                if (r.getCell().equals(cellLigacao2)) {
                                                    r.getLigacoes().add(l);
                                                }
                                            }
                                        } else if (cellLigacao1.getTipo().contains("relacionamento")) {
                                            for (Relacionamento r : getRelacionamentos()) {
                                                if (r.getCell().equals(cellLigacao1)) {
                                                    r.getLigacoes().add(l);
                                                }
                                            }
                                        }
                                        if (cellLigacao2.getTipo().equals("agregacao")) {
                                            for (Agregacao ag : getAgregacoes()) {
                                                if (ag.getCell().equals(cellLigacao2)) {
                                                    ag.getLigacoes().add(l);
                                                }
                                            }
                                        } else if (cellLigacao1.getTipo().equals("agregacao")) {
                                            for (Agregacao ag : getAgregacoes()) {
                                                if (ag.getCell().equals(cellLigacao1)) {
                                                    ag.getLigacoes().add(l);
                                                }
                                            }
                                        }
                                        l.desenha();
                                        getLigacoes().add(l);
                                        modificarTitle();
                                        cellLigacao1 = null;
                                        cellLigacao2 = null;
                                        btnConectaEntidadeRelacionamento.setSelected(false);
                                        buttonSelecionado.setSelected(false);
                                        buttonSelecionado = btnSelecionar;
                                        buttonSelecionado.setSelected(true);
                                        abas.get(tabSelecionada).getHistorico().subList(abas.get(tabSelecionada).getPosicaoHistorico() + 1, getHist().size()).clear();
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Opcões:\n"
                                                + " - Entidade e Relacionamento/Agregação\n"
                                                + " - Relacionamento e Entidade/Agregação\n"
                                                + " - Agregação e Entidade/Relacionamento", "Erro", JOptionPane.ERROR_MESSAGE);
                                        cellLigacao1 = null;
                                        cellLigacao2 = null;
                                    }
                                }
                            }
                        }
                    } else if (buttonSelecionado.equals(btnAutoRelacionamento)) {
                        if (cellLigacao1 == null) {
                            if (cell.getTipo().equals("atributo")) {
                                JOptionPane.showMessageDialog(null, "Não é possível usar a opção Ligação com abas.get(tabSelecionada).getAtributos()", "Erro", JOptionPane.ERROR_MESSAGE);
                                buttonSelecionado.setSelected(false);
                                buttonSelecionado = btnSelecionar;
                                buttonSelecionado.setSelected(true);
                            } else {
                                cellLigacao1 = cell;
                                if (cellLigacao1.getTipo().contains("entidade")) {
                                    int pxRelacao = new Double(cellLigacao1.getGeometry().getX() + cellLigacao1.getGeometry().getWidth() + 20).intValue();
                                    int pyRelacao = new Double(cellLigacao1.getGeometry().getY() + cellLigacao1.getGeometry().getHeight() + 20).intValue();

                                    Relacionamento r = new Relacionamento("NovoRelacionamento",
                                            pxRelacao, pyRelacao,
                                            140, 60, abas.get(tabSelecionada).getGraph(), true);
                                    r.desenha();
                                    abas.get(tabSelecionada).getRelacionamentos().add(r);
                                    Ligacao l1 = new Ligacao(abas.get(tabSelecionada).getGraph(), r.getCell(), cellLigacao1, EnumCardinalidade.UM_PARA_UM);
                                    Ligacao l2 = new Ligacao(abas.get(tabSelecionada).getGraph(), r.getCell(), cellLigacao1, EnumCardinalidade.UM_PARA_UM);
                                    l1.desenha();
                                    l2.desenha();
                                    Ligacao.autoRelacionamento(l1, l2, abas.get(tabSelecionada).getGraph());
                                    abas.get(tabSelecionada).getLigacoes().add(l1);
                                    abas.get(tabSelecionada).getLigacoes().add(l2);
                                    r.getLigacoes().add(l1);
                                    r.getLigacoes().add(l2);
                                    modificarTitle();
                                    cellLigacao1 = null;
                                    cellLigacao2 = null;
                                    buttonSelecionado.setSelected(false);
                                    buttonSelecionado = btnSelecionar;
                                    buttonSelecionado.setSelected(true);
                                    abas.get(tabSelecionada).getHistorico().subList(abas.get(tabSelecionada).getPosicaoHistorico() + 1, getHist().size()).clear();
                                } else {
                                    JOptionPane.showMessageDialog(null, "Entidade e entidade", "Erro", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                    } else if (buttonSelecionado.equals(btnDisjuncao)) {
                        boolean encontrado = false;
                        if (cell.getTipo().contains("entidade") && !cell.getTipo().equals("entidadeFilha")) {
                            for (Heranca h : abas.get(tabSelecionada).getHerancas()) {
                                if (h.getEntidadeMae().equals(cell) && h.getTipo().equals("d")) {
                                    h.addEntidade(abas.get(tabSelecionada).getEntidades(), abas.get(tabSelecionada).getLigacoes());
                                    encontrado = true;
                                    break;
                                }
                            }
                            if (encontrado == false) {
                                Heranca novaHeranca = new Heranca(cell, "d", abas.get(tabSelecionada).getGraph());
                                novaHeranca.desenha(abas.get(tabSelecionada).getEntidades(), abas.get(tabSelecionada).getLigacoes());
                                getHerancas().add(novaHeranca);
                            }
                            modificarTitle();
                            abas.get(tabSelecionada).getHistorico().subList(abas.get(tabSelecionada).getPosicaoHistorico() + 1, getHist().size()).clear();
                        } else {
                            buttonSelecionado.setSelected(false);
                            buttonSelecionado = btnSelecionar;
                            buttonSelecionado.setSelected(true);
                        }
                    } else if (buttonSelecionado.equals(btnSobreposicao)) {
                        boolean encontrado = false;
                        if (cell.getTipo().contains("entidade") && !cell.getTipo().equals("entidadeFilha")) {
                            for (Heranca h : abas.get(tabSelecionada).getHerancas()) {
                                if (h.getEntidadeMae().equals(cell) && h.getTipo().equals("o")) {
                                    h.addEntidade(abas.get(tabSelecionada).getEntidades(), abas.get(tabSelecionada).getLigacoes());
                                    encontrado = true;
                                    break;
                                }
                            }
                            if (encontrado == false) {
                                Heranca novaHeranca = new Heranca(cell, "o", abas.get(tabSelecionada).getGraph());
                                novaHeranca.desenha(abas.get(tabSelecionada).getEntidades(), abas.get(tabSelecionada).getLigacoes());
                                getHerancas().add(novaHeranca);
                            }
                            modificarTitle();
                            abas.get(tabSelecionada).getHistorico().subList(abas.get(tabSelecionada).getPosicaoHistorico() + 1, getHist().size()).clear();
                        } else {
                            buttonSelecionado.setSelected(false);
                            buttonSelecionado = btnSelecionar;
                            buttonSelecionado.setSelected(true);
                        }

                    } else if (buttonSelecionado.equals(btnDeletar)) {
                        deletar();
                    } else if (!buttonSelecionado.equals(btnAtributo)) {
                        propriedades(cell);
                    } else if (cell.getTipo().contains("entidade")) {  //Adiconando atributo à entidade
                        Atributo atributo = new Atributo("NovoAtributo", px, py, 100, 40, abas.get(tabSelecionada).getGraph(), cell);
                        atributo.desenha();
                        buttonSelecionado.setSelected(false);
                        buttonSelecionado = btnSelecionar;
                        buttonSelecionado.setSelected(true);
                        for (Entidade e : abas.get(tabSelecionada).getEntidades()) {
                            if (e.getCell().getId().equals(cell.getId())) {
                                e.getAtributos().add(atributo);
                                abas.get(tabSelecionada).getAtributos().add(atributo);
                                modificarTitle();
                                abas.get(tabSelecionada).getHistorico().subList(abas.get(tabSelecionada).getPosicaoHistorico() + 1, getHist().size()).clear();
                                break;
                            }
                        }

                    } else if (cell.getTipo().equals("atributo")) {  //Adiconando atributo a outro atributo
                        for (Atributo a : abas.get(tabSelecionada).getAtributos()) {
                            if (!a.getCellDono().getTipo().equals("atributo")) {
                                if (a.getCell().equals(cell)) {
                                    Atributo atributo = new Atributo("NovoAtributo", px, py, 100, 50, abas.get(tabSelecionada).getGraph(), (mxCell) abas.get(tabSelecionada).getCellAt(px, py));

                                    atributo.desenha();
                                    buttonSelecionado.setSelected(false);
                                    buttonSelecionado = btnSelecionar;
                                    buttonSelecionado.setSelected(true);
                                    a.setComposto(true);
                                    a.getAtributos().add(atributo);
                                    abas.get(tabSelecionada).getAtributos().add(atributo);
                                    modificarTitle();
                                    abas.get(tabSelecionada).getHistorico().subList(abas.get(tabSelecionada).getPosicaoHistorico() + 1, getHist().size()).clear();
                                    break;
                                }
                            }
                        }

                    } else if (cell.getTipo().equals("relacionamento")) {   //Adiconando atributo a um relacionamento
                        modificarTitle();
                        for (Relacionamento r : abas.get(tabSelecionada).getRelacionamentos()) {
                            if (r.getCell().getId().equals(cell.getId())) {
                                Atributo atributo = new Atributo("NovoAtributo", px, py, 120, 50, abas.get(tabSelecionada).getGraph(), (mxCell) abas.get(tabSelecionada).getCellAt(px, py));
                                atributo.desenha();
                                buttonSelecionado.setSelected(false);
                                buttonSelecionado = btnSelecionar;
                                buttonSelecionado.setSelected(true);
                                r.getAtributos().add(atributo);
                                abas.get(tabSelecionada).getAtributos().add(atributo);
                                modificarTitle();
                                abas.get(tabSelecionada).getHistorico().subList(abas.get(tabSelecionada).getPosicaoHistorico() + 1, getHist().size()).clear();
                                break;
                            }
                        }
                    }
                } else {
                    //Nem chega nesse else
                    propriedades(cell);
                }
            }
        }
    }
}
