/**
 * 
 */
package com.ibm.bbva.ctacte.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

/**
 * @author cfveliz
 *
 */
public abstract class GenericDAO<T, PK extends Serializable> {
	
	private Class<T> entityClass;

	public GenericDAO(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	protected abstract EntityManager getEntityManager();

	public T save(T entity) {
		getEntityManager().persist(entity);
		return entity;
	}

	public T update(T entity) {
		entity = getEntityManager().merge(entity);
		getEntityManager().flush();
		PK id = (PK) getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
		getEntityManager().detach(entity);
		entity = getEntityManager().find(entityClass, id);
		return entity;
	}

	public void delete(T entity) {
		getEntityManager().remove(getEntityManager().merge(entity));
	}
	
	public void deleteById(PK id) {
	 	T obj = load(id);
	 	delete(obj);
	}

	public T load(PK id) {
		return getEntityManager().find(entityClass, id);
	}

	public List<T> findAll() {
		javax.persistence.criteria.CriteriaQuery cq = getEntityManager()
				.getCriteriaBuilder().createQuery();
		cq.select(cq.from(entityClass));
		return getEntityManager().createQuery(cq).getResultList();
	}

	public List<T> findRange(int[] range) {
		javax.persistence.criteria.CriteriaQuery cq = getEntityManager()
				.getCriteriaBuilder().createQuery();
		cq.select(cq.from(entityClass));
		javax.persistence.Query q = getEntityManager().createQuery(cq);
		q.setMaxResults(range[1] - range[0]);
		q.setFirstResult(range[0]);
		return q.getResultList();
	}

	public int count() {
		javax.persistence.criteria.CriteriaQuery cq = getEntityManager()
				.getCriteriaBuilder().createQuery();
		javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
		cq.select(getEntityManager().getCriteriaBuilder().count(rt));
		javax.persistence.Query q = getEntityManager().createQuery(cq);
		return ((Long) q.getSingleResult()).intValue();
	}

}
