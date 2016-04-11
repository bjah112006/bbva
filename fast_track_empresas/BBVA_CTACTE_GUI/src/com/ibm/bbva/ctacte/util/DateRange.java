package com.ibm.bbva.ctacte.util;

import java.io.Serializable;
import java.util.Date;

public class DateRange implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7544914911071218868L;
	private Date limInf;
	private Date target;
	private Date limSup;
	public DateRange(Date target) {
		this.target = target;
	}
	
	@Override
	public boolean equals(Object o) {
		boolean b =false;
		
		if(o instanceof DateRange){			
			DateRange temp = (DateRange)o;
			if(temp.getLimInf()!=null && temp.getLimSup() != null){
					b= target.after(temp.getLimInf())&&target.before(temp.getLimSup());	
			}else{
				if(temp.getLimInf()==null && temp.getLimSup() != null){
					b=target.before(temp.getLimSup());
				}else{
					if(temp.getLimInf()!=null && temp.getLimSup()==null){
						b=target.after(temp.getLimInf());
					}else{
						if(target!=null && temp.getTarget()!=null){
							b=target.equals(temp.getTarget());
						}
						if(target!=null && temp.getTarget()==null){
							b=true;
						}
						
					}
				}
			}
			
		}
		return b;
	}


	public Date getLimInf() {
		return limInf;
	}

	public void setLimInf(Date limInf) {
		this.limInf = limInf;
	}

	public Date getTarget() {
		return target;
	}

	public void setTarget(Date target) {
		this.target = target;
	}

	public Date getLimSup() {
		return limSup;
	}

	public void setLimSup(Date limSup) {
		this.limSup = limSup;
	}
	
}
