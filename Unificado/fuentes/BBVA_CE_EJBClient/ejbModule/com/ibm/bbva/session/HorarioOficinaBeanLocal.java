package com.ibm.bbva.session;

import java.util.List;

import javax.persistence.NoResultException;

import com.ibm.bbva.entities.HorarioOficina;

public interface HorarioOficinaBeanLocal {
	public List<HorarioOficina> buscarTodos();
	public HorarioOficina buscarPorId(long id);	
	public HorarioOficina buscarPorIdOficinaIdHorario(long idOficina, long idHorario) throws Exception;	
	public List<HorarioOficina> buscarPorIdHorario(long idHorario);
	public List<HorarioOficina> buscarPorIdOficina(long idOficina);
	public List<HorarioOficina> buscarXcriterios(HorarioOficina h);
    public HorarioOficina create(HorarioOficina horarioOficina);
	
	public void edit(HorarioOficina horarioOficina);
	
	public List<HorarioOficina> buscarPorIdOficinaActivosHorarios(long idOficina);
	List<HorarioOficina> buscarTodaOficinaActivosHorarios();

}
