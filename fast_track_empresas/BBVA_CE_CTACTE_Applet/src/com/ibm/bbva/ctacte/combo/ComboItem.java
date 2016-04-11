package com.ibm.bbva.ctacte.combo;

public class ComboItem  implements Comparable<ComboItem> {
	
	private String codigo;
    private String descripcion;

    public ComboItem(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return this.getDescripcion();
    }

    public int compareTo(ComboItem o) {
        if (o == null) {
            return 0;
        }
        return this.toString().compareTo(o.toString());

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ComboItem other = (ComboItem) obj;
        if ((this.descripcion == null) ? (other.descripcion != null) : !this.descripcion.equals(other.descripcion)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (this.descripcion != null ? this.descripcion.hashCode() : 0);
        return hash;
    }

}
