package com.ibm.bbva.ctacte.util;

import java.io.Serializable;

public class SortHeader implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3244218779112249404L;
	private static final String NEUTRAL_ICON_CLASSES 	= "ui-sortable-column-icon ui-icon ui-icon-carat-2-n-s";
	private static final String ASC_ICON_CLASSES 		= "ui-sortable-column-icon ui-icon ui-icon-carat-2-n-s ui-icon-triangle-1-s";
	private static final String DSC_ICON_CLASSES 		= "ui-sortable-column-icon ui-icon ui-icon-carat-2-n-s ui-icon-triangle-1-n";

	private String classes = NEUTRAL_ICON_CLASSES;
	private Integer order=0;
	private String label;
	private String spanStyle="display:none";
	

	public String getSpanStyle() {
		return spanStyle;
	}

	public void setSpanStyle(String spanStyle) {
		this.spanStyle = spanStyle;
	}

	public SortHeader(String label) {
		super();
		this.label = label;
	}

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	public void doAsc(){
		spanStyle="float:none;text-align:center;";
		this.classes = ASC_ICON_CLASSES;
	}

	public void doDsc(){
		spanStyle="float:none;text-align:center;";
		this.classes = DSC_ICON_CLASSES;
	}

	public void doNeutral(){
		spanStyle="display:none";
		this.classes = NEUTRAL_ICON_CLASSES;
	}

	public boolean isNeutral(){
		return classes.equals(NEUTRAL_ICON_CLASSES);
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void doReverse(){
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("classes ENTRANDO : "+classes);
		if(classes.equals(NEUTRAL_ICON_CLASSES)){
			doAsc();
		}else{
			if(classes.equals(ASC_ICON_CLASSES)){
				doDsc();
			}else{
				if(classes.equals(DSC_ICON_CLASSES)){
					doAsc();
				}
			}
		}
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("classes SALIENDO : "+classes);
	}
	
	public void setOrder(Integer order) {
		this.order = order;
	}
	
	public Integer getOrder() {
		return order;
	}

	
	
}
