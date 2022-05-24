package com.way.stock.rewards.datasource.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ConnectionUtil {

	private static Logger logger = LoggerFactory.getLogger(ConnectionUtil.class);

	@Qualifier("primaryDataSource")
	@Autowired
	private DataSource dataSource;

	public static void releaseConnection(Connection conn) {

		if (conn != null) {
			try {
				conn.close();
				logger.debug("Database connection released");
			} catch (Exception e) {
				logger.error("Error releasing connection");
			}
		}
	}

	public static void releaseStatement(Statement st) {
		if (st != null) {
			try {
				st.close();
				logger.debug("Statement released");
			} catch (Exception e) {
				logger.error("Error releasing statement");
			}
		}
	}

	public static void releaseResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
				logger.debug("ResultSet released");
			} catch (Exception e) {
				logger.error("Error releasing ResultSet");
			}
		}
	}

	public static void releaseResultSetStatementConnection(ResultSet rs, Statement st, Connection con) {
		releaseResultSet(rs);
		releaseStatement(st);
		releaseConnection(con);
	}

	public static void release(Object... releaseObjs) {

		for (Object obj : releaseObjs) {
			if (obj instanceof ResultSet) {
				releaseResultSet((ResultSet) obj);
			}
			if (obj instanceof Statement) {
				releaseStatement((Statement) obj);
			}
			if (obj instanceof Connection) {
				releaseConnection((Connection) obj);
			}
		}

	}

	public Connection getConnection() {

		Connection conn = null;
		try {
			logger.debug("Calling getConnection() first time");
			conn = dataSource.getConnection();
		} catch (Exception e) {
			logger.error("Exception caught during getConnection(), trying again...");
			conn = retryGrabbingConnection(4000, 3); // change to config
		}
		return conn;
	}

	private Connection retryGrabbingConnection(final int conn_retry_interval, final int conn_retry_times) {
		Connection conn = null;
		boolean exceptionCaught;

		for (int cnt = 1; cnt <= conn_retry_times; cnt++) {

			logger.debug(String.format("Attempt: %d, to grab conn ...", cnt));
			// Sleep first...
			try {
				logger.debug(String.format("Sleeping for %d ms on iteration: %d", conn_retry_interval, cnt));
				Thread.currentThread();
				Thread.sleep(conn_retry_interval);

			} catch (Exception eSleep) {
				logger.error(String.format("Exception caught during sleep interval: %d, times: %d, iteration: %d",
						conn_retry_interval, conn_retry_times, cnt));
				Thread.currentThread().interrupt();
			}

			// Then, call getConnection().
			exceptionCaught = false;
			try {
				logger.debug(String.format("Calling getConnection(%d, %d) on iteration: %d", conn_retry_interval,
						conn_retry_times, cnt));
				conn = dataSource.getConnection();
			} catch (Exception econn3) {
				exceptionCaught = true; // getConnection() failed, so jot this down.
				logger.error(String.format("Exception caught during getConnection(%d, %d) on iteration: %d",
						conn_retry_interval, conn_retry_times, cnt));
			}

			// Now, do we have a success or not ? If so, just bail out of the for-loop.
			if (!exceptionCaught) {
				logger.debug(String.format("Grabbed a connection on iteration: %d, we can stop looping", cnt));
				break;
			}

			logger.debug(String.format("Fail to grab a conn on iteration: %d, try again..", cnt));
		}

		return conn;
	}
}
