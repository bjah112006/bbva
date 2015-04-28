package pe.ibm.rest;

import java.util.Date;
import java.util.List;

public interface RestBPM {

	public void crearProcessInstance();	
	
	public String crearProcessInstance(String json);
	
	public void completeProcess(String piid);	
	
	public void completeTask(String idTarea);
	
	public void completeTask(String idTask,String json);	
	
	public void assignTask(String taskId,String userAssing);
	
	//public void reAssignTask(String taskId,String userAssing);
	
	//public void assingToMe(String idTarea);
	
	public String searchTemplate(String nameSearch,String filtroUser);
	
	public String getTaksDetails(String idTask);
	
	public String getTaksDetailsBulk(List<String> lstIdTask);
	
	public void setDataTask(String taskId,String json);
	
	public void completeWaitingOperation(String json);

	public void reassignTask(String json);
	
	public Date getDateServer() ;
	
	public String getListTaskBBVA(String json);
}
