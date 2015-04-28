package com.ibm.bbva.ctacte.controller.form.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.faces.model.ListDataModel;

import com.ibm.bbva.ctacte.vo.TareaBandejaVO;

public class TareaBandejaVOModel extends ListDataModel<TareaBandejaVO> implements Collection<TareaBandejaVO>, Serializable {
	// TODO migrar a Trinidad
		//implements SelectableDataModel<TareaBandejaVO>, Serializable{

	private static final long serialVersionUID = -8623476131754470554L;
	
	public HashMap<String, TareaBandejaVO> mapa;
	
	public TareaBandejaVOModel(List<TareaBandejaVO> lista) {
		super(lista);
		mapa = new HashMap<String, TareaBandejaVO> ();
		for (TareaBandejaVO tb : lista) {
			mapa.put(tb.getId(), tb);
		}
	}
	
	public TareaBandejaVO getRowData(String rowKey) {
		return mapa.get(rowKey);
	}

	public Object getRowKey(TareaBandejaVO tareaBandejaVO) {
		return tareaBandejaVO.getCodCentral();
	}

	@Override
	public boolean add(TareaBandejaVO object) {
		List<TareaBandejaVO> entity = (List<TareaBandejaVO>) getWrappedData();
		return entity.add(object);
	}

	@Override
	public boolean addAll(Collection<? extends TareaBandejaVO> collection) {
		List<TareaBandejaVO> entity = (List<TareaBandejaVO>) getWrappedData();
		return entity.addAll(collection);
	}

	@Override
	public void clear() {
		List<TareaBandejaVO> entity = (List<TareaBandejaVO>) getWrappedData();
		entity.clear();
	}

	@Override
	public boolean contains(Object object) {
		List<TareaBandejaVO> entity = (List<TareaBandejaVO>) getWrappedData();
		if (entity == null)
			return false;
		else
			return entity.contains(object);
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		List<TareaBandejaVO> entity = (List<TareaBandejaVO>) getWrappedData();
		if (entity == null)
			return false;
		else
			return entity.containsAll(collection);
	}

	@Override
	public boolean isEmpty() {
		List<TareaBandejaVO> entity = (List<TareaBandejaVO>) getWrappedData();
        return (entity == null) || entity.isEmpty();
	}

	@Override
	public boolean remove(Object object) {
		List<TareaBandejaVO> entity = (List<TareaBandejaVO>) getWrappedData();
		return entity.remove(object);
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		List<TareaBandejaVO> entity = (List<TareaBandejaVO>) getWrappedData();
		return entity.removeAll(collection);
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		List<TareaBandejaVO> entity = (List<TareaBandejaVO>) getWrappedData();
		return entity.removeAll(collection);
	}

	@Override
	public int size() {
		List<TareaBandejaVO> entity = (List<TareaBandejaVO>) getWrappedData();
		return entity.size();
	}

	@Override
	public Object[] toArray() {
		List<TareaBandejaVO> entity = (List<TareaBandejaVO>) getWrappedData();
		return entity.toArray();
	}

	@Override
	public <T> T[] toArray(T[] array) {
		List<TareaBandejaVO> entity = (List<TareaBandejaVO>) getWrappedData();
		return entity.toArray(array);
	}

}
