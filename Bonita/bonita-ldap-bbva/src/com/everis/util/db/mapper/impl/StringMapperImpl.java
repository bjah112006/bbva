package com.everis.util.db.mapper.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.everis.core.exception.BussinesException;
import com.everis.util.db.Metadata;
import com.everis.util.db.mapper.RowMapper;

public class StringMapperImpl implements RowMapper<String> {

    @Override
    public String proccess(ResultSet result, List<Metadata> metadata) throws BussinesException {
        String val = null;
        if (metadata.size() > 1) {
            throw new BussinesException("El n\u00FAmero de campos de resultados supera lo esperado");
        }
        try {
            val = result.getString(metadata.get(0).getColumnName());
        } catch (SQLException e) {
            throw new BussinesException("Error al obtener el valor", e);
        }

        return val;
    }

}
