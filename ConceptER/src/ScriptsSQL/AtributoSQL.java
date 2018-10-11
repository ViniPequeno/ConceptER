package ScriptsSQL;


public class AtributoSQL {

    private String nome;
    private String tipo;
    private boolean pk;
    private boolean fk;
    private String nomeFK;
    private String tabelaFK;
    private boolean unico;
    private boolean nulo;
    private boolean autoIncremento;
    private String padrao;

    public AtributoSQL(String nome, String tipo, boolean pk, boolean fk, String nomeFK, String tabelaFK, boolean unico, boolean nulo, boolean autoIncremento, String padrao) {
        this.nome = nome;
        this.tipo = tipo;
        this.pk = pk;
        this.fk = fk;
        this.nomeFK = nomeFK;
        this.tabelaFK = tabelaFK;
        this.unico = unico;
        this.nulo = nulo;
        this.autoIncremento = autoIncremento;
        this.padrao = padrao;
    }
    
    public AtributoSQL(AtributoSQL asql) {
        this.nome = asql.getNome();
        this.tipo = asql.getTipo();
        this.pk = asql.isPk();
        this.fk = asql.isFk();
        this.nomeFK = asql.getNomeFK();
        this.tabelaFK = asql.getTabelaFK();
        this.unico = asql.isUnico();
        this.nulo = asql.isNulo();
        this.autoIncremento = asql.isAutoIncremento();
        this.padrao = asql.getPadrao();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isPk() {
        return pk;
    }

    public void setPk(boolean pk) {
        this.pk = pk;
    }

    public boolean isFk() {
        return fk;
    }

    public void setFk(boolean fk) {
        this.fk = fk;
    }

    public String getNomeFK() {
        return nomeFK;
    }

    public void setNomeFK(String nomeFK) {
        this.nomeFK = nomeFK;
    }

    public boolean isUnico() {
        return unico;
    }

    public void setUnico(boolean unico) {
        this.unico = unico;
    }

    public boolean isNulo() {
        return nulo;
    }

    public void setNulo(boolean nulo) {
        this.nulo = nulo;
    }

    public boolean isAutoIncremento() {
        return autoIncremento;
    }

    public void setAutoIncremento(boolean autoIncremento) {
        this.autoIncremento = autoIncremento;
    }

    public String getPadrao() {
        return padrao;
    }

    public void setPadrao(String padrao) {
        this.padrao = padrao;
    }

    public String getTabelaFK() {
        return tabelaFK;
    }

    public void setTabelaFK(String tabelaFK) {
        this.tabelaFK = tabelaFK;
    }

    
}
