package com.ibm.bbva.jobs;

import java.util.ArrayList;
import java.util.List;

import com.bbva.general.entities.Territorio;

public class TerritoriosData {
	
	public List<Territorio> getTerritorioListado(){
		List<Territorio> listaTerritorios = new ArrayList<Territorio>();
		
		Territorio territorio1 = new Territorio();
		territorio1.setCodigoTerritorio("7001");
		territorio1.setDescripcionTerritorio("PRUEBA TERRITORIO 1");
		listaTerritorios.add(territorio1);
		
		Territorio territorio2 = new Territorio();
		territorio2.setCodigoTerritorio("7002");
		territorio1.setDescripcionTerritorio("PRUEBA TERRITORIO 2");
		listaTerritorios.add(territorio2);
		
		Territorio territorio3 = new Territorio();
		territorio3.setCodigoTerritorio("7003");
		territorio1.setDescripcionTerritorio("PRUEBA TERRITORIO 3");
		listaTerritorios.add(territorio3);
				
		Territorio territorio4 = new Territorio();
		territorio4.setCodigoTerritorio("7004");
		territorio1.setDescripcionTerritorio("PRUEBA TERRITORIO 4");
		listaTerritorios.add(territorio4);
		
		System.out.println("listaTerritorios:"+listaTerritorios);
		return listaTerritorios;
	}
	
}
