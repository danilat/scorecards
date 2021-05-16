package com.danilat.scorecards.core.persistence.jdbc;

import com.danilat.scorecards.shared.domain.Entity;
import com.danilat.scorecards.shared.domain.Id;
import com.danilat.scorecards.shared.domain.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

import com.danilat.scorecards.shared.domain.Sort;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public abstract class JdbcBaseRepository<E extends Entity, I extends Id> implements
        Repository<E, I> {

    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcBaseRepository(DataSource dataSource) {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    protected String toSnakeCase(String field) {
        return field.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    @Override
    public Optional<E> get(I id) {
        SqlParameterSource params = new MapSqlParameterSource().addValue("table", tableName()).addValue("id", id.value());
        return queryOne("SELECT * FROM " + tableName() + " WHERE id = :id", params);
    }

    @Override
    public Collection<E> all() {
        return namedParameterJdbcTemplate.query("SELECT * FROM " + tableName(), (resultSet, rowNum) -> mapRow(resultSet));
    }

    @Override
    public Collection<E> all(Sort sort) {
        return namedParameterJdbcTemplate.query("SELECT * FROM " + tableName() + " ORDER BY " + sort.field() + " " + sort.direction().value(), (resultSet, rowNum) -> mapRow(resultSet));
    }

    @Override
    public void clear() {
        namedParameterJdbcTemplate.update("DELETE FROM " + tableName(), new HashMap<>());
    }

    protected Optional<E> queryOne(String sql, SqlParameterSource params) {
        List<E> entities = namedParameterJdbcTemplate
                .query(sql, params, (resultSet, rowNum) -> mapRow(resultSet));
        if (entities.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(entities.get(0));
    }

    protected abstract E mapRow(ResultSet resultSet) throws SQLException;

    protected abstract String tableName();
}
