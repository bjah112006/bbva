package com.ibm.bbva.ctacte.dao;

import java.sql.SQLException;
import java.util.List;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.Attachment;

public interface AttachmentDAO extends IGenericDAO<Attachment, Integer> {

	List<Attachment> listaAttachment(Attachment filtro)throws SQLException;
	Attachment obtener(Long id)throws SQLException;
		
}
