package com.didiglobal.common.db.dbproxy;

import com.didiglobal.common.db.ForwardingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author liyanling
 * @date 2021/11/23 4:11 下午
 */
public class DbProxyDataSource extends ForwardingDataSource {

    public DbProxyDataSource(DataSource delegate) {
        super(delegate);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DbProxyConnection.wrap(super.getConnection()).buildStatementComment();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return DbProxyConnection.wrap(super.getConnection(username, password))
                .buildStatementComment();
    }
}
