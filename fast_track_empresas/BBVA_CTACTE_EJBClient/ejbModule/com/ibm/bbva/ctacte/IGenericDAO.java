package com.ibm.bbva.ctacte;

import java.io.Serializable;
import java.util.List;

public interface IGenericDAO<T, PK extends Serializable> {
	
	public T save(T entity);
	public T update(T entity);
	public void delete(T entity);
	public void deleteById(PK id);
	public T load(PK id);
	public List<T> findAll();
	public List<T> findRange(int[] range);
	public int count();

}
