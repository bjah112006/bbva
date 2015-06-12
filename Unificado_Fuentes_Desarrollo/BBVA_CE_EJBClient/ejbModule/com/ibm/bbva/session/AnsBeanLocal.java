package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.Ans;

public interface AnsBeanLocal {

	public Ans obtenerAns(long idProducto, long idTarea, long idTipoOferta, long idGrupoSegmento, long idEstado);
	public List<Ans> buscarTodos();
	public List<Ans> buscarConsultaParametrizable(Ans objConsulta);
	public Ans create(Ans ans);
	public void edit(Ans ans);
	public void remove(Ans ans);
	public List<Ans> cargarValoresAns(List<Long> listIdsTareas, List<Long> listIdsProd);
	
}
