package com.ibm.bbva.ctacte.dao.servicio;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import com.ibm.bbva.ctacte.bean.servicio.core.ClientePJCore;
import com.ibm.bbva.ctacte.bean.servicio.core.CuentaCore;
import com.ibm.bbva.ctacte.bean.servicio.core.DatosClientePJCore;
import com.ibm.bbva.ctacte.bean.servicio.core.ParticipeCore;
import com.ibm.bbva.ctacte.bean.servicio.sfp.DatosClientePJSFP;

public class Servidor {
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private HSSFWorkbook dataCore;
	
	public Servidor() {
		try {
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Archvo XLS");
			dataCore = new HSSFWorkbook(new FileInputStream("C:\\Data_Pruebas_Funcionales_CE_ACCPJ (30 Jul).xls"));
			//datosCliente("10101029");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<ClientePJCore> doiLike (String numeroDOI) {
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("doiLike (String numeroDOI)");
		HSSFSheet hoja = dataCore.getSheet("cLIENTE PJ");
		Iterator<Row> rowIterator = hoja.rowIterator();
		ArrayList<ClientePJCore> list = new ArrayList<ClientePJCore>();
		rowIterator.next();
		while (rowIterator.hasNext()) {
			HSSFRow fila = (HSSFRow) rowIterator.next();
			String doi = String.valueOf((long)fila.getCell(3).getNumericCellValue());
			if (doi.startsWith(numeroDOI)) {
				list.add(crearClientePJCore(fila));
			}
		}
		return list;
	}
	
	public List<ClientePJCore> ccEq(String codigoCentral) {
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("ccEq(String codigoCentral)");
		HSSFSheet hoja = dataCore.getSheet("cLIENTE PJ");
		Iterator<Row> rowIterator = hoja.rowIterator();
		ArrayList<ClientePJCore> list = new ArrayList<ClientePJCore>();
		rowIterator.next();
		while (rowIterator.hasNext()) {
			HSSFRow fila = (HSSFRow) rowIterator.next();
			String cc = String.valueOf((long)fila.getCell(4).getNumericCellValue());
			if (cc.equals(codigoCentral)) {
				list.add(crearClientePJCore(fila));
			}
		}
		return list;
	}
	
	private ClientePJCore crearClientePJCore (HSSFRow fila) {
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("crearClientePJCore (HSSFRow fila)");
		ClientePJCore cliente = new ClientePJCore();
		cliente.setCodigoCentral(String.valueOf((long)fila.getCell(4).getNumericCellValue()));
		cliente.setFechaConstitucion(fila.getCell(6).getDateCellValue());
		cliente.setNumeroDOI(String.valueOf((long)fila.getCell(3).getNumericCellValue()));
		cliente.setRazonSocial(fila.getCell(5).getStringCellValue());
		cliente.setTipoDOI(fila.getCell(1).getStringCellValue());
		cliente.setDescripcionDOI(fila.getCell(2).getStringCellValue());
		return cliente;
	}
	
	public DatosClientePJCore datosCliente(String codigoCentral) {
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("datosCliente(String codigoCentral)");
		HSSFSheet hoja = dataCore.getSheet("cLIENTE PJ");
		Iterator<Row> rowIterator = hoja.rowIterator();
		rowIterator.next();
		while (rowIterator.hasNext()) {
			HSSFRow fila = (HSSFRow) rowIterator.next();
			String cc = String.valueOf((long)fila.getCell(4).getNumericCellValue());
			if (cc.equals(codigoCentral)) {
				return crearDatosClientePJCore(fila);
			}
		}
		return null;
	}
	
	
	private DatosClientePJCore crearDatosClientePJCore(HSSFRow fila) {
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("crearDatosClientePJCore(HSSFRow fila)");
		DatosClientePJCore dc = new DatosClientePJCore();
		dc.setCodigoActEconomica(fila.getCell(9).getStringCellValue());
		dc.setCodigoCentral(String.valueOf((long)fila.getCell(4).getNumericCellValue()));
		dc.setCodigoDepartamento(fila.getCell(12).getStringCellValue());
		dc.setCodigoDistrito(fila.getCell(16).getStringCellValue());
		dc.setCodigoProvincia(fila.getCell(14).getStringCellValue());
		dc.setCorreoElectronico1(fila.getCell(20).getStringCellValue());
		if (fila.getCell(21)!=null)
		dc.setCorreoElectronico2(fila.getCell(21).getStringCellValue());

		dc.setDescActEconomica(fila.getCell(10).getStringCellValue());
		dc.setDescDepartamento(fila.getCell(13).getStringCellValue());
		dc.setDescDistrito(fila.getCell(17).getStringCellValue());
		dc.setDescProvincia(fila.getCell(15).getStringCellValue());
		dc.setDescTipoDOI(fila.getCell(1).getStringCellValue());//**

		dc.setDireccion(fila.getCell(11).getStringCellValue());
		dc.setFechaConstitucion(dateFormat.format(fila.getCell(6).getDateCellValue()));
		dc.setNombreRazonSocial(fila.getCell(5).getStringCellValue());
		if (fila.getCell(18)!=null) {
			long num = (long)fila.getCell(18).getNumericCellValue();
			if (num!=0) {
				dc.setNroCelular1(String.valueOf(num));
			}
		}
		if (fila.getCell(19)!=null) {
			long num = (long)fila.getCell(19).getNumericCellValue();
			if (num!=0) {
				dc.setNroCelular2(String.valueOf(num));
			}
		}
		dc.setNumeroDOI(String.valueOf((long)fila.getCell(3).getNumericCellValue()));
		//dc.setFechaConstitucion("20-10-1995");
		dc.setSectorCodigo(fila.getCell(7).getStringCellValue());
		dc.setSectorDescripcion(fila.getCell(8).getStringCellValue());
		
		dc.setTipoDOI(fila.getCell(1).getStringCellValue());//**
		//dc.setUbicacion(fila.getCell(1).getStringCellValue());
		
		ArrayList<CuentaCore> ctas = listaCuentas (dc.getCodigoCentral());
		dc.setCuentas(ctas);
		return dc;
	}
	
	private ArrayList<CuentaCore> listaCuentas (String codCentral) {
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("listaCuentas (String codCentral)");
		HSSFSheet hoja = dataCore.getSheet("Cuentas");
		Iterator<Row> rowIterator = hoja.rowIterator();
		ArrayList<CuentaCore> list = new ArrayList<CuentaCore>();
		rowIterator.next();
		rowIterator.next();
		while (rowIterator.hasNext()) {
			HSSFRow fila = (HSSFRow) rowIterator.next();
			String cc = String.valueOf((long)fila.getCell(0).getNumericCellValue());
			if (cc.equals(codCentral)) {
				list.add(crearCuentaCore(fila, codCentral));
			}
		}
		return list;
	}

	private CuentaCore crearCuentaCore(HSSFRow fila, String codigoCent) {
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("crearCuentaCore(HSSFRow fila, String codigoCent)");
		CuentaCore cuenta = new CuentaCore();
		cuenta.setCodigoProducto(fila.getCell(1).getStringCellValue());
		cuenta.setCodigoSubProducto(fila.getCell(3).getStringCellValue());
		cuenta.setDescProducto(fila.getCell(2).getStringCellValue());
		cuenta.setDescSubProducto(fila.getCell(4).getStringCellValue());
		cuenta.setFechCreacionCtaCte(fila.getCell(8).getDateCellValue());
		cuenta.setMonedaCodigo(fila.getCell(6).getStringCellValue());
		cuenta.setMonedaDescripcion(fila.getCell(7).getStringCellValue());
		cuenta.setNumeroContrato(fila.getCell(5).getStringCellValue());
		cuenta.setSituacionCuenta(fila.getCell(9).getStringCellValue());
		
		cuenta.setParticipes(listaParticipes (codigoCent));
		return cuenta;
	}
	
	private ArrayList<ParticipeCore> listaParticipes (String codCentral) {
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("listaParticipes (String codCentral)");
		HSSFSheet hoja = dataCore.getSheet("pARTICIPES");
		Iterator<Row> rowIterator = hoja.rowIterator();
		ArrayList<ParticipeCore> list = new ArrayList<ParticipeCore>();
		rowIterator.next();
		rowIterator.next();
		while (rowIterator.hasNext()) {
			HSSFRow fila = (HSSFRow) rowIterator.next();
			String cc = String.valueOf((long)fila.getCell(0).getNumericCellValue());
			if (cc.equals(codCentral)) {
				list.add(crearParticipeCore(fila));
			}
		}
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("listlist "+list.size());
		return list;
	}

	private ParticipeCore crearParticipeCore(HSSFRow fila) {
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("crearParticipeCore(HSSFRow fila)");
		ParticipeCore participe = new ParticipeCore();
		participe.setApellidoMaterno(fila.getCell(9).getStringCellValue());
		participe.setApellidoPaterno(fila.getCell(8).getStringCellValue());
		participe.setCodigoCentral(fila.getCell(3).getStringCellValue());
		participe.setCodigoDepartamento(fila.getCell(12).getStringCellValue());
		participe.setCodigoDistrito(fila.getCell(16).getStringCellValue());
		participe.setCodigoProvincia(fila.getCell(14).getStringCellValue());
		//participe.setCombinacion(fila.getCell(9).getStringCellValue());
		participe.setDescDepartamento(fila.getCell(13).getStringCellValue());
		participe.setDescDistrito(fila.getCell(17).getStringCellValue());
		participe.setDescProvincia(fila.getCell(15).getStringCellValue());
		participe.setDescTipoDOI(fila.getCell(5).getStringCellValue());
		participe.setDireccion(fila.getCell(10).getStringCellValue());
		//participe.setFacultades(fila.getCell(9).getStringCellValue());
		participe.setFechaSerialFirma(dateFormat.format(fila.getCell(19).getDateCellValue()));
		participe.setIndFirma(String.valueOf((long)fila.getCell(18).getNumericCellValue()));
		
		//participe.setIndFirmaAsociada(fila.getCell(9).getStringCellValue());
		//participe.setIndFirmanteCtaCte(fila.getCell(9).getStringCellValue());
		//participe.setIndFirmaSerializada(fila.getCell(9).getStringCellValue());
		participe.setNivelIntervencion(fila.getCell(20).getStringCellValue());
		participe.setNombres(fila.getCell(7).getStringCellValue());
		participe.setNumeroDOI(fila.getCell(6).getStringCellValue());
		participe.setSecIntervencion(fila.getCell(21).getStringCellValue());
		participe.setTipoDOI(fila.getCell(4).getStringCellValue());
		//participe.setUbicacion(fila.getCell(9).getStringCellValue());
		return participe;
	}
	
	public DatosClientePJSFP datosClienteSFP (String codigoCentral) {
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("datosClienteSFP (String codigoCentral)");
		HSSFSheet hoja = dataCore.getSheet("SFP");
		Iterator<Row> rowIterator = hoja.rowIterator();
		rowIterator.next();
		while (rowIterator.hasNext()) {
			HSSFRow fila = (HSSFRow) rowIterator.next();
			String cc = String.valueOf((long)fila.getCell(0).getNumericCellValue());
			if (cc.equals(codigoCentral)) {
				DatosClientePJSFP dc = new DatosClientePJSFP();
				dc.setCodigoCentral(codigoCentral);
				dc.setEstadoPJ(fila.getCell(1).getStringCellValue());
				if (fila.getCell(2)!=null) {
					dc.setTipoPJ("0"+String.valueOf((long)fila.getCell(2).getNumericCellValue()));
				}
				if (fila.getCell(3)!=null) {
					if (fila.getCell(3).getDateCellValue() != null) {
						dc.setFechaEscritura(dateFormat.format(fila.getCell(3).getDateCellValue()));
					} else {
						dc.setFechaEscritura(null);
					}
				}
				return dc;
			}
		}
		return null;
		
	}

	public static void main(String[] args) {
		new Servidor();
	}
}
