package com.ibm.mant.tabla.vo;

import java.util.Collection;

public class TablaParametricaVO {

	private TablaVO tablaVO;
	private Collection<ColumnaVO> columnasVO;
	private Collection<RegistroTablaVO> registrosVO;
	
	/**
	 * @param tablaVO
	 * @param columnasVO
	 * @param registrosVO
	 */
	public TablaParametricaVO(TablaVO tablaVO,
			Collection<ColumnaVO> columnasVO,
			Collection<RegistroTablaVO> registrosVO) {
		super();
		this.tablaVO = tablaVO;
		this.columnasVO = columnasVO;
		this.registrosVO = registrosVO;
	}
	/**
	 * @return the tablaVO
	 */
	public TablaVO getTablaVO() {
		return tablaVO;
	}
	/**
	 * @param tablaVO the tablaVO to set
	 */
	public void setTablaVO(TablaVO tablaVO) {
		this.tablaVO = tablaVO;
	}
	/**
	 * @return the columnasVO
	 */
	public Collection<ColumnaVO> getColumnasVO() {
		return columnasVO;
	}
	/**
	 * @param columnasVO the columnasVO to set
	 */
	public void setColumnasVO(Collection<ColumnaVO> columnasVO) {
		this.columnasVO = columnasVO;
	}
	/**
	 * @return the registrosVO
	 */
	public Collection<RegistroTablaVO> getRegistrosVO() {
		return registrosVO;
	}
	/**
	 * @param registrosVO the registrosVO to set
	 */
	public void setRegistrosVO(Collection<RegistroTablaVO> registrosVO) {
		this.registrosVO = registrosVO;
	}
}
