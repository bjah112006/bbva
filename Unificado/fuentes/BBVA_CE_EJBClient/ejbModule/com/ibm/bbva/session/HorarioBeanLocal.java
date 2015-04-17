package com.ibm.bbva.session;

import java.util.Date;
import java.util.List;

import com.ibm.bbva.entities.Horario;

public interface HorarioBeanLocal {
	public List<Horario> buscarTodos();
	public Horario buscarPorId(long id);
	public List<Horario> buscarXcriterios(Horario h);
	public List<Horario>  buscarHorarioNormalXoficina(long oficina);
	public Horario buscarFeriado(Date fechaInicio, String codOficina);
	
    public Horario create(Horario horario);
	public void edit(Horario horario);
	public void remove(Horario entity);
}
