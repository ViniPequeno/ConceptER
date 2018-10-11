/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ScriptsSQL;

import Model.Relacionamento;
import Model.Atributo;
import Estaticos.EnumCardinalidade;
import Estaticos.PalavrasEstaticas;
import Estaticos.Valores;
import Model.Agregacao;
import Telas.PanelPrincipal;
import Model.Entidade;
import Model.Heranca;
import java.util.ArrayList;

/**
 *
 * @author PedroEYan
 */
public class Scripts {

    PanelPrincipal pp = null;
    ArrayList<TabelaSQL> tabelas = new ArrayList<>();
    public static String mapeamento;

    private boolean erro = false;

    public Scripts(PanelPrincipal pp) {
        this.pp = pp;
    }

    public ArrayList<TabelaSQL> gerar() {
        String nomeTabela = "";
        EnumCardinalidade primeiro, segundo;
        ArrayList<AtributoSQL> asql = new ArrayList<>();
        asql.clear();
        mapeamento = "";
        mapeamento += "Primeiro cria-se todas as entidades com seus devidos atributos.\n\n";
        for (Entidade e : pp.getEntidades()) {
            TabelaSQL tab = new TabelaSQL(e.getNome());
            tabelas.add(tab);
            for (Atributo a : e.getAtributos()) {
                asql.clear();
                if (a.isMultivalorado()) {
                    mapeamento += "Na tabela " + e.getNome() + ", o atributo multivalorado " + a.getNome() + " transforma-se em uma nova tabela.\n";
                    TabelaSQL tab2 = new TabelaSQL(a.getNome());
                    tabelas.add(tab2);
                    asql.clear();
                    for (TabelaSQL t : tabelas) {
                        if (t.getNome().equals(e.getNome())) {
                            nomeTabela = t.getNome();
                            for (int i = 0; i < t.getAtributos().size(); i++) {
                                if (t.getAtributos().get(i).isPk()) {
                                    asql.add(t.getAtributos().get(i));
                                }
                            }
                            break;
                        }
                    }
                    for (TabelaSQL t : tabelas) {
                        if (t.getNome().equals(a.getNome())) {
                            for (AtributoSQL aa : asql) {
                                t.getAtributos().add(new AtributoSQL("FK" + aa.getNome() + nomeTabela, aa.getTipo(), true, true, aa.getNome(), nomeTabela, aa.isUnico(), aa.isNulo(), aa.isAutoIncremento(),
                                        aa.getPadrao()));
                            }
                            break;
                        }
                    }
                    if (a.isComposto()) {
                        mapeamento += "Nesse caso, a nova tabela " + a.getNome() + ",que era um atributo multivalorado composto, "
                                + "poossui todos os atributos que o acompanham\n";
                        for (Atributo aa : a.getAtributos()) {
                            this.modificaTipoAtributo(a);
                            tab2.atributos.add(new AtributoSQL(aa.getNome(), aa.getDominio(), true, true, null, null,
                                    aa.isUnique(), aa.isNotNull(), aa.isAutoIncrement(), aa.getPadrao()));
                        }
                    } else {
                        mapeamento += "Nesse caso, a nova tabela " + a.getNome() + ",que era um atributo multivalorado, "
                                + "poossui apenas a chave estrangeira e ele mesmo com atributo de único valor.\n";
                        this.modificaTipoAtributo(a);
                        tab2.atributos.add(new AtributoSQL(a.getNome(), a.getDominio(), true, false, null, null,
                                a.isUnique(), a.isNotNull(), a.isAutoIncrement(), a.getPadrao()));
                    }
                } else if (a.isComposto()) {
                    mapeamento += "Na tabela " + e.getNome() + ", o atributo composto " + a.getNome() + " desaparece e seus atributos únicos são mapeados.";
                    for (Atributo aa : a.getAtributos()) {
                        this.modificaTipoAtributo(a);
                        tab.atributos.add(new AtributoSQL(aa.getNome(), aa.getDominio(), aa.isPk(), false, null, null,
                                aa.isUnique(), aa.isNotNull(), aa.isAutoIncrement(), aa.getPadrao()));
                    }
                } else {
                    mapeamento += "Na tabela " + e.getNome() + ", o atributo simples " + a.getNome() + " é mapeados\n\n";
                    this.modificaTipoAtributo(a);
                    tab.atributos.add(new AtributoSQL(a.getNome(), a.getDominio(), a.isPk(), false, null, null,
                            a.isUnique(), a.isNotNull(), a.isAutoIncrement(), a.getPadrao()));
                }

            }

        }

        for (Agregacao a : pp.getAgregacoes()) {
            TabelaSQL tabA = new TabelaSQL(a.getNome());
            tabelas.add(tabA);
            mapeamento += "A agregação " + a.getNome() + " vira uma nova tabela com as chaves estrangeiras apontando para chaves primários das entidades dependentes";
            for (int y = 0; y < a.getLigacoes().size(); y++) {
                String e1 = a.getLigacoes().get(y).getSource().getTipo().equals("entidade")
                        ? (String) a.getLigacoes().get(y).getSource().getValue()
                        : (String) a.getLigacoes().get(y).getTarget().getValue();
                for (TabelaSQL t : tabelas) {
                    if (t.getNome().equals(e1)) {
                        nomeTabela = e1;
                        for (int i = 0; i < t.getAtributos().size(); i++) {
                            if (t.getAtributos().get(i).isPk()) {
                                asql.add(new AtributoSQL(t.getAtributos().get(i)));
                            }
                        }
                        break;
                    }
                }
                for (TabelaSQL t : tabelas) {
                    if (t.getNome().equals(tabA.getNome())) {
                        for (AtributoSQL aa : asql) {
                            t.getAtributos().add(new AtributoSQL("FK" + aa.getNome() + nomeTabela, aa.getTipo(), true, true, aa.getNome(), nomeTabela, aa.isUnico(), aa.isNulo(), aa.isAutoIncremento(),
                                    aa.getPadrao()));
                        }
                    }
                }
                asql.clear();
            }

            if (a.getAtributos().size() > 0) {
                for (Atributo a2 : a.getAtributos()) {
                    if (a2.isMultivalorado()) {
                        mapeamento += "Na tabela " + a.getNome() + ", o atributo multivalorado " + a.getNome() + " transforma-se em uma nova tabela.\n";
                        TabelaSQL tab2 = new TabelaSQL(a2.getNome());
                        tabelas.add(tab2);
                        asql.clear();
                        for (TabelaSQL t : tabelas) {
                            if (t.getNome().equals(a.getNome())) {
                                nomeTabela = t.getNome();
                                for (int i = 0; i < t.getAtributos().size(); i++) {
                                    if (t.getAtributos().get(i).isPk()) {
                                        asql.add(t.getAtributos().get(i));
                                    }
                                }
                                break;
                            }
                        }
                        for (TabelaSQL t : tabelas) {
                            if (t.getNome().equals(a2.getNome())) {
                                for (AtributoSQL aa : asql) {
                                    t.getAtributos().add(new AtributoSQL("FK" + aa.getNome() + nomeTabela, aa.getTipo(), true, true, aa.getNome(), nomeTabela, aa.isUnico(), aa.isNulo(), aa.isAutoIncremento(),
                                            aa.getPadrao()));
                                }
                                break;
                            }
                        }
                        if (a2.isComposto()) {
                            mapeamento += "Nesse caso, a nova tabela " + a2.getNome() + ",que era um atributo multivalorado composto, "
                                    + "poossui todos os atributos que o acompanham\n";
                            for (Atributo aa : a.getAtributos()) {
                                this.modificaTipoAtributo(a2);
                                tab2.atributos.add(new AtributoSQL(aa.getNome(), aa.getDominio(), true, true, null, null,
                                        aa.isUnique(), aa.isNotNull(), aa.isAutoIncrement(), aa.getPadrao()));
                            }
                        } else {
                            mapeamento += "Nesse caso, a nova tabela " + a2.getNome() + ",que era um atributo multivalorado, "
                                    + "poossui apenas a chave estrangeira e ele mesmo com atributo de único valor.\n";
                            this.modificaTipoAtributo(a2);
                            tab2.atributos.add(new AtributoSQL(a.getNome(), a2.getDominio(), true, false, null, null,
                                    a2.isUnique(), a2.isNotNull(), a2.isAutoIncrement(), a2.getPadrao()));
                        }
                    } else if (a2.isComposto()) {
                        mapeamento += "Na tabela " + a.getNome() + ", o atributo composto " + a2.getNome() + " desaparece e seus atributos únicos são mapeados";
                        for (Atributo aa : a.getAtributos()) {
                            this.modificaTipoAtributo(a2);
                            tabA.atributos.add(new AtributoSQL(aa.getNome(), aa.getDominio(), aa.isPk(), false, null, null,
                                    aa.isUnique(), aa.isNotNull(), aa.isAutoIncrement(), aa.getPadrao()));
                        }
                    } else {
                        mapeamento += "Na tabela " + a.getNome() + ", o atributo simples " + a.getNome() + " é mapeados\n\n";
                        this.modificaTipoAtributo(a2);
                        tabA.atributos.add(new AtributoSQL(a.getNome(), a2.getDominio(), a2.isPk(), false, null, null,
                                a2.isUnique(), a2.isNotNull(), a2.isAutoIncrement(), a2.getPadrao()));
                    }
                }
            }

        }
        for (Relacionamento r : pp.getRelacionamentos()) {
            if (r.isAutoRelacionamento()) {
                //verificar a cardinalidade
                //Se for 1:1 ou 1:N cria uma chave estrangeira
                //Se M;N uma nova tabela com nomes diferentes
            } else if (r.getAtributos().size() > 0 && r.getLigacoes().size() == 2) {
                //logica para criar uma outra tabela com todas chaves primárias das tabelas e atributos
                TabelaSQL tab = new TabelaSQL(r.getNomeSQL());

                mapeamento += "O relacionamento binário com atributos " + tab.getNome()
                        + "gera uma nova tabela com seus devidos atributos e chaves estrangeiras apontando para chaves primárias das duas entidades";
                for (Atributo a : r.getAtributos()) {
                    if (a.isComposto()) {
                        this.modificaTipoAtributo(a);
                        for (Atributo aa : a.getAtributos()) {
                            tab.atributos.add(new AtributoSQL(a.getNome(), a.getDominio(), a.isPk(), false, null, null,
                                    a.isUnique(), a.isNotNull(), a.isAutoIncrement(), a.getPadrao()));
                        }
                    } else {
                        this.modificaTipoAtributo(a);
                        tab.atributos.add(new AtributoSQL(a.getNome(), a.getDominio(), a.isPk(), false, null, null,
                                a.isUnique(), a.isNotNull(), a.isAutoIncrement(), a.getPadrao()));
                    }
                }
                tabelas.add(tab);
            } else if (r.getLigacoes().size() == 2) {
                // criar um relacionamento binário

                primeiro = r.getLigacoes().get(0).getCardinalidade();
                segundo = r.getLigacoes().get(1).getCardinalidade();
                boolean primeiro1 = r.getLigacoes().get(0).isTotal();
                boolean segundo2 = r.getLigacoes().get(1).isTotal();
                String e1 = r.getLigacoes().get(0).getSource().getTipo().equals("entidade")
                        ? (String) r.getLigacoes().get(0).getSource().getValue()
                        : (String) r.getLigacoes().get(0).getTarget().getValue();
                String e2 = r.getLigacoes().get(1).getSource().getTipo().equals("entidade")
                        ? (String) r.getLigacoes().get(1).getSource().getValue()
                        : (String) r.getLigacoes().get(1).getTarget().getValue();

                //1:1
                if ((primeiro == EnumCardinalidade.ZERO_PARA_UM || primeiro == EnumCardinalidade.UM_PARA_UM)
                        && (segundo == EnumCardinalidade.ZERO_PARA_UM || segundo == EnumCardinalidade.UM_PARA_UM)) {
                    mapeamento += "O relacionamento binário de 1:1 " + r.getNome()
                            + "gera uma chave estrangeira em qualquer uma das tabelas vinculadas apontando para chave primária da outra tabela.\n";
                    for (TabelaSQL t : tabelas) {
                        if (t.getNome().equals(e1)) {
                            nomeTabela = t.getNome();
                            for (int i = 0; i < t.getAtributos().size(); i++) {
                                if (t.getAtributos().get(i).isPk()) {
                                    asql.add(t.getAtributos().get(i));
                                }
                            }
                            break;
                        }
                    }
                    for (TabelaSQL t : tabelas) {
                        if (t.getNome().equals(e2)) {
                            mapeamento += "Nese caso, a tabela " + t.getNome() + " recebe uma chave estrangeira apontando para chave primária de " + nomeTabela;
                            for (AtributoSQL aa : asql) {
                                t.getAtributos().add(new AtributoSQL("FK" + aa.getNome() + nomeTabela, aa.getTipo(), false, true, aa.getNome(), nomeTabela, aa.isUnico(), segundo2, aa.isAutoIncremento(),
                                        aa.getPadrao()));
                            }
                        }
                    }

                } //1:N
                else if ((primeiro == EnumCardinalidade.ZERO_PARA_UM || primeiro == EnumCardinalidade.UM_PARA_UM)
                        && (segundo == EnumCardinalidade.UM_PARA_MUITOS || segundo == EnumCardinalidade.MUITOS_PARA_MUITOS)) {
                    mapeamento += "O relacionamento binário de 1:M " + r.getNome()
                            + "gera uma chave estrangeira na tabela que possui M como cardinalidade\n";
                    for (TabelaSQL t : tabelas) {
                        if (t.getNome().equals(e1)) {
                            nomeTabela = e1;
                            for (int i = 0; i < t.getAtributos().size(); i++) {
                                if (t.getAtributos().get(i).isPk()) {
                                    asql.add(new AtributoSQL(t.getAtributos().get(i)));
                                }
                            }
                            break;
                        }
                    }
                    for (TabelaSQL t : tabelas) {
                        if (t.getNome().equals(e2)) {
                            mapeamento += "Nese caso, a tabela " + t.getNome() + " recebe uma chave estrangeira apontando para chave primária de " + nomeTabela;
                            for (AtributoSQL aa : asql) {
                                t.getAtributos().add(new AtributoSQL("FK" + aa.getNome() + nomeTabela, aa.getTipo(), false, true, aa.getNome(), nomeTabela, aa.isUnico(), segundo2, aa.isAutoIncremento(),
                                        aa.getPadrao()));
                            }
                        }
                    }
                } //N:1
                else if ((segundo == EnumCardinalidade.ZERO_PARA_UM || segundo == EnumCardinalidade.UM_PARA_UM)
                        && (primeiro == EnumCardinalidade.UM_PARA_MUITOS || primeiro == EnumCardinalidade.MUITOS_PARA_MUITOS)) {
                    for (TabelaSQL t : tabelas) {
                        if (t.getNome().equals(e2)) {
                            nomeTabela = e2;
                            for (int i = 0; i < t.getAtributos().size(); i++) {
                                if (t.getAtributos().get(i).isPk()) {
                                    asql.add(new AtributoSQL(t.getAtributos().get(i)));
                                }
                            }
                            break;
                        }
                    }
                    for (TabelaSQL t : tabelas) {
                        if (t.getNome().equals(e1)) {
                            mapeamento += "Nese caso, a tabela " + t.getNome() + " recebe uma chave estrangeira apontando para chave primária de " + nomeTabela;
                            for (AtributoSQL aa : asql) {
                                t.getAtributos().add(new AtributoSQL("FK" + aa.getNome() + nomeTabela, aa.getTipo(), false, true, aa.getNome(), nomeTabela, aa.isUnico(), primeiro1, aa.isAutoIncremento(),
                                        aa.getPadrao()));
                            }
                        }
                    }
                } //N:N
                else {
                    TabelaSQL ta = new TabelaSQL(r.getNome());
                    tabelas.add(ta);
                    mapeamento += "O relacionamento binário de N:M " + r.getNome()
                            + " gera uma nova tabela com as chaves estrangeiras apontando para duas entidades ligadas\n";
                    asql.clear();
                    for (TabelaSQL t : tabelas) {
                        if (t.getNome().equals(e2)) {
                            nomeTabela = e2;
                            for (int i = 0; i < t.getAtributos().size(); i++) {
                                if (t.getAtributos().get(i).isPk()) {
                                    asql.add(new AtributoSQL(t.getAtributos().get(i)));
                                }
                            }
                            break;
                        }
                    }
                    for (TabelaSQL t : tabelas) {
                        if (t.getNome().equals(ta.getNome())) {
                            for (AtributoSQL aa : asql) {
                                ta.getAtributos().add(new AtributoSQL("FK" + aa.getNome() + nomeTabela, aa.getTipo(), true, true, aa.getNome(), nomeTabela, aa.isUnico(), aa.isNulo(), aa.isAutoIncremento(),
                                        aa.getPadrao()));
                            }
                        }
                    }
                    asql.clear();
                    for (TabelaSQL t : tabelas) {
                        if (t.getNome().equals(e1)) {
                            nomeTabela = e1;
                            for (int i = 0; i < t.getAtributos().size(); i++) {
                                if (t.getAtributos().get(i).isPk()) {
                                    asql.add(new AtributoSQL(t.getAtributos().get(i)));
                                }
                            }
                            break;
                        }
                    }
                    for (TabelaSQL t : tabelas) {
                        if (t.getNome().equals(ta.getNome())) {
                            for (AtributoSQL aa : asql) {
                                t.getAtributos().add(new AtributoSQL("FK" + aa.getNome() + nomeTabela, aa.getTipo(), true, true, aa.getNome(), nomeTabela, aa.isUnico(), aa.isNulo(), aa.isAutoIncremento(),
                                        aa.getPadrao()));
                            }
                        }
                    }
                }
            } else {
                //criar um relacionamento de tres ou mais entidades.
                TabelaSQL tabelaNova = new TabelaSQL(r.getNome());
                tabelas.add(tabelaNova);
                mapeamento += "O relacionamento ternário " + r.getNome()
                        + "gera uma nova tabela com as chaves estrangeiras apontando para todas as entidades ligadas\n";
                for (int y = 0; y < r.getLigacoes().size(); y++) {
                    String e1 = r.getLigacoes().get(y).getSource().getTipo().equals("entidade")
                            ? (String) r.getLigacoes().get(0).getSource().getValue()
                            : (String) r.getLigacoes().get(0).getTarget().getValue();

                    for (TabelaSQL t : tabelas) {
                        if (t.getNome().equals(e1)) {
                            nomeTabela = e1;
                            for (int i = 0; i < t.getAtributos().size(); i++) {
                                if (t.getAtributos().get(i).isPk()) {
                                    asql.add(new AtributoSQL(t.getAtributos().get(i)));
                                }
                            }
                            break;
                        }
                    }
                    for (TabelaSQL t : tabelas) {
                        mapeamento += "Nesse caso, a nova tabela " + r.getNome() + " liga com as chaves primárias de " + nomeTabela;
                        if (t.getNome().equals(tabelaNova.getNome())) {
                            for (AtributoSQL aa : asql) {
                                t.getAtributos().add(new AtributoSQL("FK" + aa.getNome() + nomeTabela, aa.getTipo(), true, true, aa.getNome(), nomeTabela, aa.isUnico(), aa.isNulo(), aa.isAutoIncremento(),
                                        aa.getPadrao()));
                            }
                        }
                    }
                    asql.clear();
                }
                if (r.getAtributos().size() > 0) {
                    for (Atributo a : r.getAtributos()) {
                        if (a.isMultivalorado()) {
                            TabelaSQL tab2 = new TabelaSQL(a.getNome());
                            tabelas.add(tab2);
                            if (a.isComposto()) {
                                for (Atributo aa : a.getAtributos()) {
                                    this.modificaTipoAtributo(a);
                                    tabelaNova.atributos.add(new AtributoSQL(aa.getNome(), aa.getDominio(), true, true, null, null,
                                            aa.isUnique(), aa.isNotNull(), aa.isAutoIncrement(), aa.getPadrao()));
                                }
                            } else {
                                this.modificaTipoAtributo(a);
                                tabelaNova.atributos.add(new AtributoSQL(a.getNome(), a.getDominio(), true, false, null, null,
                                        a.isUnique(), a.isNotNull(), a.isAutoIncrement(), a.getPadrao()));
                            }
                        } else if (a.isComposto()) {
                            for (Atributo aa : a.getAtributos()) {
                                this.modificaTipoAtributo(a);
                                tabelaNova.atributos.add(new AtributoSQL(aa.getNome(), aa.getDominio(), aa.isPk(), false, null, null,
                                        aa.isUnique(), aa.isNotNull(), aa.isAutoIncrement(), aa.getPadrao()));
                            }
                        } else {
                            this.modificaTipoAtributo(a);
                            tabelaNova.atributos.add(new AtributoSQL(a.getNome(), a.getDominio(), a.isPk(), false, null, null,
                                    a.isUnique(), a.isNotNull(), a.isAutoIncrement(), a.getPadrao()));
                        }

                    }
                }

            }
        }
        for (Heranca h : pp.getHerancas()) {
            asql.clear();
            mapeamento += "Nesse caso de herança da entidade-mãe " + (String) h.getEntidadeMae().getValue()
                    + " e as entidades filhas: ";
            for (int i = 0; i < h.getEntidadesFilhas().size(); i++) {
                if (i == h.getEntidadesFilhas().size() - 1) {
                    mapeamento += h.getEntidadesFilhas().get(i).getNome() + " ";
                } else {
                    mapeamento += h.getEntidadesFilhas().get(i).getNome() + ", ";
                }
            }
            if (h.isTotal() || !mae(h)) {//É obrigatório ou a entidade mãe não tem chave primária
                mapeamento += "a entidade-mãe não possui chave primária ou é obrigatório a participação, então "
                        + " as entidades-filhas são mapeadas junto com os atributos da entidade-mãe";
                for (TabelaSQL tsql : tabelas) {
                    if (tsql.getNome().equals((String) h.getEntidadeMae().getValue())) {
                        for (AtributoSQL a : tsql.getAtributos()) {
                            asql.add(new AtributoSQL(a));
                        }
                        tabelas.remove(tsql);
                        break;
                    }
                }
                for (int i = 0; i < h.getEntidadesFilhas().size(); i++) {
                    for (TabelaSQL tsql : tabelas) {
                        if (tsql.getNome().equals(h.getEntidadesFilhasEm(i).getNome())) {
                            tsql.getAtributos().addAll(asql);
                        }
                        break;
                    }
                }

            } else {
                if (filhas(h) == false) {  //Entidades filhas não tem chave primária
                    mapeamento += "as entidade-filhas não possuem chave primárias ou não é obrigatório a participação, então "
                            + " a entidade-mãe é mapeada junto com os atributos das entidades-filhas";
                    for (Entidade e : h.getEntidadesFilhas()) {
                        asql.clear();
                        for (TabelaSQL tsqle : tabelas) {
                            if (tsqle.getNome().equals(e.getNome())) {
                                for (AtributoSQL a : tsqle.getAtributos()) {
                                    asql.add(a);
                                    tabelas.remove(tsqle);
                                }
                                break;
                            }
                        }
                        for (TabelaSQL tsql : tabelas) {
                            if (tsql.getNome().equals((String) h.getEntidadeMae().getValue())) {
                                tsql.getAtributos().addAll(asql);
                                break;
                            }
                        }
                    }
                } else {// Todas tem PK
                    asql.clear();
                    mapeamento += "a entidade-mãe e as entidades-filhas possuem chave primária, então "
                            + " tanto a entidade-mãe e as entidades filhas são mapeadas, com as entidades-filhas tendo chave estrangeira apontando para chave primária da entidade-mãe";
                    for (TabelaSQL tsql : tabelas) {
                        if (tsql.getNome().equals((String) h.getEntidadeMae().getValue())) {
                            nomeTabela = tsql.getNome();
                            for (AtributoSQL a : tsql.getAtributos()) {
                                if (a.isPk()) {
                                    asql.add(new AtributoSQL(a.getNome(), a.getTipo(),
                                            false, true, "FK" + a.getNome() + nomeTabela, nomeTabela, a.isUnico(), a.isNulo(), a.isAutoIncremento(), a.getPadrao()));
                                }
                            }
                            break;
                        }
                    }
                    for (Entidade e : h.getEntidadesFilhas()) {
                        for (TabelaSQL tsql : tabelas) {
                            if (e.getNome().equals(tsql.getNome())) {
                                tsql.getAtributos().addAll(asql);
                            }
                        }
                    }
                }
            }
        }

        return tabelas;
    }

    public boolean filhas(Heranca h) {
        boolean tem = false;
        for (Entidade e : h.getEntidadesFilhas()) {
            tem = false;
            for (Atributo a : e.getAtributos()) {
                if (a.isPk()) {
                    tem = true;
                    break;
                }
            }
            if (tem == false) {
                return tem;
            }
        }
        return tem;
    }

    public boolean mae(Heranca h) {
        boolean tem = false;
        for (TabelaSQL t : tabelas) {
            if (t.getNome().equals(h.getEntidadeMae().getValue())) {
                for (AtributoSQL a : t.getAtributos()) {
                    if (a.isPk()) {
                        tem = true;
                        break;
                    }
                }
            }
        }
        return tem;
    }

    public boolean isErro() {
        return erro;
    }

    public void setErro(boolean erro) {
        this.erro = erro;
    }

    public ArrayList<TabelaSQL> getTabelas() {
        return tabelas;
    }

    public void setTabelas(ArrayList<TabelaSQL> tabelas) {
        this.tabelas = tabelas;
    }

    private void modificaTipoAtributo(Atributo aa) {
        for (int i = 0; i < Valores.getDOMINIOS().size(); i++) {
            if (Valores.getDOMINIOS().get(i).equals(aa.getDominio())) {
                if (i >= Valores.getAtributosCT()) {
                    aa.setDominio(aa.getDominio() + PalavrasEstaticas.PARETESESINICIO
                            + aa.getComplemento() + PalavrasEstaticas.PARENTESESFINAL);
                }
            }
        }
    }
}
