package springbased.service;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import springbased.bean.ConnectionInfo;
import springbased.service.connectionpool.DataSourceFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by I303152 on 7/5/2016.
 */
@Service
public class ManageDataQueryService {

    private static final Logger log = Logger.getLogger(ManageDataQueryService.class);

    public List<Map<String, Object>> query(String sql, ConnectionInfo connectionInfo, Object... bindVars) {
        JdbcTemplate template = new JdbcTemplate(DataSourceFactory.getDataSource(connectionInfo));
        List<Map<String, Object>> result = template.queryForList(sql, bindVars);
        return result;
    }

    public String toSql(String schema, String table, String column, String operator,
                        String value, String orderBy, String selectColumns) {
        String sql = " select " + selectColumns + " from " + schema + "." + table;
        if (!StringUtils.isBlank(column) && !StringUtils.isBlank(operator) && !StringUtils.isBlank(value)) {
            sql += " where " + column + " " + operator + " ? " ;
        }
        if (!selectColumns.contains("distinct")) {
            sql = " select * from ( " + sql + " ) where ROWNUM < 30 ";
        } else {
            // currently, distinct does not work well with limit
        }
        if (!StringUtils.isBlank(orderBy)) {
            sql += " order by " + orderBy;
        }
        return sql;
    }

    public String queryTablesSQL() {
        String sql = "select table_name from dba_tables dt where upper(owner)= ? ";
        return sql;
    }

    public List<String> queryTableNames(String schema, ConnectionInfo connectionInfo) {
        List<String> names = new JdbcTemplate(
                DataSourceFactory.getDataSource(connectionInfo)).query(
                this.queryTablesSQL(), new RowMapper<String>() {
                    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return rs.getString(1);
                    }
                }, schema.toUpperCase());
        return names;
    }

    public List<String> queryTableNames(String schema, ConnectionInfo connectionInfo, String key) {
        List<String> names = new JdbcTemplate(
                DataSourceFactory.getDataSource(connectionInfo)).query(
                this.queryTablesSQL() + " and table_name like ? ", new RowMapper<String>() {
                    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return rs.getString(1);
                    }
                }, schema.toUpperCase(), "%" + key.toUpperCase() + "%");
        return names;
    }

    public String queryTableColumnsSQL() {
        String sql =  " select column_name from dba_tab_columns where Owner = ? and table_name = ? ";
        return sql;
    }

    public List<String> queryColumnNames(String schema, String tableName, ConnectionInfo connectionInfo) {
        List<String> names = new JdbcTemplate(
                DataSourceFactory.getDataSource(connectionInfo)).query(
                this.queryTableColumnsSQL(), new RowMapper<String>() {
                    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return rs.getString(1);
                    }
                }, schema.toUpperCase(), tableName.toUpperCase());
        return names;
    }

    public String querySchemaSQL(){
        String sql = "select username from DBA_USERS where initial_rsrc_consumer_group = 'DEFAULT_CONSUMER_GROUP' ";
        return sql;
    }

    public List<String> querySchemas(ConnectionInfo connectionInfo) {
        List<String> names = new JdbcTemplate(
                DataSourceFactory.getDataSource(connectionInfo)).query(
                this.querySchemaSQL(), new RowMapper<String>() {
                    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return rs.getString(1);
                    }
                });
        return names;
    }

    public List<String> querySchemas(ConnectionInfo connectionInfo, String key) {
        List<String> names = new JdbcTemplate(
                DataSourceFactory.getDataSource(connectionInfo)).query(
                this.querySchemaSQL() + " and username like ?", new RowMapper<String>() {
                    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return rs.getString(1);
                    }
                }, "%" + key.toUpperCase() + "%");
        return names;
    }

}
