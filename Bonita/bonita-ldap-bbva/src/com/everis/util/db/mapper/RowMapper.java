package com.everis.util.db.mapper;

import java.sql.ResultSet;
import java.util.List;

import com.everis.core.exception.BussinesException;
import com.everis.util.db.Metadata;

public interface RowMapper<T> {

    T proccess(ResultSet result, List<Metadata> metadata) throws BussinesException;
}
