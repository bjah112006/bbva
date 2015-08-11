package com.everis.util.db.mapper.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.everis.core.exception.BussinesException;
import com.everis.util.db.Metadata;
import com.everis.util.db.mapper.RowMapper;

public class MapMapperImpl implements RowMapper<Map<String, Object>> {

    @Override
    public Map<String, Object> proccess(ResultSet result, List<Metadata> metadata) throws BussinesException {
        Map<String, Object> val = new LinkedHashMap<String, Object>();
        try {
            for(Metadata m : metadata) {
                val.put(m.getColumnName(), result.getObject(m.getColumnName()));
            }
        } catch (SQLException e) {
            throw new BussinesException("Error al obtener el valor", e);
        }

        return val;
    }

}
