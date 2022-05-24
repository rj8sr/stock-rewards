package com.way.stock.rewards.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.way.exception.constants.ExceptionModule;
import com.way.exception.util.BaseDao;
import com.way.exception.util.WayDaoException;
import com.way.stock.rewards.datasource.config.ConnectionUtil;

@Repository
public class ApiPrivilegeMapRepository extends BaseDao {

	Logger logger = LoggerFactory.getLogger(ApiPrivilegeMapRepository.class);

	@Autowired
	ConnectionUtil connectionUtil;

	public Integer getPrivilege(String requestUrl, String userRole, Integer userId) throws WayDaoException {

		String methodArgs = getExceptionUtil().methodInputArgsAsString(requestUrl, userRole, userId);

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			String getPrivilage = "select ? REGEXP concat('^',APM_Url) as isAuthorized ,APM_Url from view_acl WHERE ROL_RoleID IN "
					+ "(select ROL_RoleID from tbl_role where ROL_RoleName =?) order by isAuthorized desc limit 1";

			conn = connectionUtil.getConnection();
			ps = conn.prepareStatement(getPrivilage);
			ps.setString(1, requestUrl);
			ps.setString(2, userRole);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt("isAuthorized");
			}
		} catch (final Exception ex) {
			logger.error("getPrivilege:{}", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(), "getPrivilege",
					methodArgs);
		} finally {
			ConnectionUtil.release(rs, ps, conn);
		}
		return null;
	}

}