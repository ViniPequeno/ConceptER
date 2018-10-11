/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Estaticos;

/**
 *
 * @author User
 */
public enum EnumCardinalidade {
    ZERO_PARA_UM("0:1"), UM_PARA_UM("1:1"), UM_PARA_MUITOS("0:N"), MUITOS_PARA_MUITOS("1:N"), NENHUM("");

    private final String text;

    /**
     * @param text
     */
    EnumCardinalidade(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}
