package com.ibm.mm.viewer;

import java.util.Properties;

import com.ibm.mm.viewer.annotation.CMBAnnotationServices;

public class CMBGenericDocViewer2 extends CMBGenericDocViewer{

	private static final long serialVersionUID = 1L;

	public CMBGenericDocViewer2(CMBStreamingDocServices arg0,
			CMBAnnotationServices arg1, Properties arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	public Boolean habilitarGrabar = null;
  
	public Boolean isHabilitarGrabar() 
	{
		return habilitarGrabar;
	}
	
	public void setHabilitarGrabar(Boolean habilitarGrabar) 
	{
		this.habilitarGrabar = habilitarGrabar;
	}

	public boolean canSave()
	{
		if (isHabilitarGrabar() == null)
		{
			if ((getSelectedModel() != null) && (getSelectedModel().getCurrentPageNumber() <= 0)) 
			{
				return false;
			}
			if ((getSelectedDocument() != null) && (getSelectedDocument().isModified())) 
			{
				return true;
			}
			if ((getSelectedAnnotationSet() != null) && (getSelectedAnnotationSet().isDirty())) 
			{
				return true;
			}
			if ((getSelectedModel() != null) && (getSelectedModel().isRelatedInfoModified())) 
			{
				return true;
			}
		}
		else
		{
			if (isHabilitarGrabar()) 
			{
				if (getSelectedDocument() != null)
				{
					getSelectedDocument().setModified(true);
				}
				if (getSelectedAnnotationSet() != null)
				{	
					getSelectedAnnotationSet().setDirty(true);
				}
				return true;
			}
			else
			{
				if (getSelectedDocument() != null)
				{
					getSelectedDocument().setModified(false);
				}
				if (getSelectedAnnotationSet() != null)
				{	
					getSelectedAnnotationSet().setDirty(false);
				}
				setHabilitarGrabar(null);
				return false;
			}
		}
		return false;
	}
}