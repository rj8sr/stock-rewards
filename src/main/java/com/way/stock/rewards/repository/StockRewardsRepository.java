package com.way.stock.rewards.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.way.exception.constants.ExceptionModule;
import com.way.exception.util.BaseDao;
import com.way.exception.util.WayDaoException;
import com.way.stock.rewards.datasource.config.ConnectionUtil;
import com.way.stock.rewards.enums.AccountType;
import com.way.stock.rewards.enums.OrderType;
import com.way.stock.rewards.request.dto.AgreementRequestDto;
import com.way.stock.rewards.request.dto.RegisterUserRequestDto;
import com.way.stock.rewards.response.AccountActivityDetailsResponse;
import com.way.stock.rewards.response.dto.JournalOrderResponseDto;
import com.way.stock.rewards.response.dto.OrderDetailsResponseDto;
import com.way.stock.rewards.response.dto.UserAccountInfoResponseDto;
import com.way.stock.rewards.util.DateUtil;

/**
 * This repository layer is used to fetch details or save response data into the
 * database
 *
 *
 */
@Repository
public class StockRewardsRepository extends BaseDao {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ConnectionUtil connectionUtil;

	/**
	 * This method is used to fetch the user's accountId from the database by using
	 * it's userId
	 *
	 * @param userId
	 * @return accountId
	 * @throws WayDaoException
	 */
	public String fetchAccountIdByUserId(Integer userId) throws WayDaoException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String query = "select UAI_Account_ID from WAY_STOCK_REWARDS.tbl_user_account_info where UAI_USR_UserID = ?;";
			conn = connectionUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setInt(1, userId);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("UAI_Account_ID");
			}
		} catch (Exception ex) {
			logger.error("fetchAccountIdByUserId:(StockRewards Repository layer) {}", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(),
					"fetchAccountIdByUserId", methodArgs);
		} finally {
			ConnectionUtil.release(rs, ps, conn);
		}
		return null;
	}

	/**
	 * This method is used to fetch the user's email address from the database
	 *
	 * @param userId
	 * @return emailAddress
	 * @throws WayDaoException
	 */
	public String fetchUserEmailAddressByUserId(Integer userId) throws WayDaoException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String query = "select USR_EmailID from PROD_WAY_DB.tbl_user where USR_UserID = ?;";
			conn = connectionUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setInt(1, userId);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("USR_EmailID");
			}
		} catch (Exception ex) {
			logger.error("fetchUserEmailAddressByUserId:(StockRewards Repository layer) {}", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(),
					"fetchUserEmailAddressByUserId", methodArgs);
		} finally {
			ConnectionUtil.release(rs, ps, conn);
		}
		return null;
	}

	/**
	 * This method is used to fetch the accountId of the Firm from the database
	 *
	 * @param userId
	 * @return accountId
	 * @throws WayDaoException
	 */
	public String fetchAccountIdOfFirm(Integer userId) throws WayDaoException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String query = "select UAI_Account_ID from WAY_STOCK_REWARDS.tbl_user_account_info where UAI_UserAccountInfoID = 1;";

			conn = connectionUtil.getConnection();
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("UAI_Account_ID");
			}
		} catch (Exception ex) {
			logger.error("fetchAccountIdOfFirm:(StockRewards Repository layer) {}", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(), "fetchAccountIdOfFirm",
					methodArgs);
		} finally {
			ConnectionUtil.release(rs, ps, conn);
		}
		return null;
	}

	/**
	 * This method is used to save the UserAccountInfoResponse into the database on
	 * successful registration of a user and return the generated key value of the
	 * successfully created record in the database
	 *
	 * @param conn
	 * @param userId
	 * @param userAccountInfoDto
	 * @return userAccountInfoId
	 * @throws WayDaoException
	 */
	public Integer saveUserAccountInfo(Connection conn, Integer userId, UserAccountInfoResponseDto userAccountInfoDto)
			throws WayDaoException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);

		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer userAccountInfoId = null;
		try {
			String query = "INSERT INTO WAY_STOCK_REWARDS.tbl_user_account_info SET UAI_USR_UserID = ?,"
					+ "UAI_Account_ID = ?, UAI_Account_Number = ?, UAI_Account_Type = ?, "
					+ "UAI_Account_Status = ?, UAI_Currency = ?, UAI_Last_Equity = ?, UAI_Created_At = ?, "
					+ "UAI_CreatedUserID = ?, UAI_CreatedDateTime = current_timestamp() , UAI_LastModifiedUserID = ?, "
					+ "UAI_LastModifiedDateTime = current_timestamp() ;";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, userId);
			ps.setString(2, userAccountInfoDto.getAccountId());
			ps.setString(3, userAccountInfoDto.getAccountNumber());
			ps.setString(4, AccountType.Customer.toString());
			ps.setString(5, userAccountInfoDto.getStatus());
			ps.setString(6, userAccountInfoDto.getCurrency());
			ps.setString(7, userAccountInfoDto.getLastEquity());
			ps.setString(8, DateUtil.getTimeStampFromResponseDate(userAccountInfoDto.getCreatedAt()));
			ps.setInt(9, userId);
			ps.setInt(10, userId);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			while (rs.next()) {
				userAccountInfoId = rs.getInt(1);
			}
		} catch (Exception ex) {
			logger.error("saveUserAccountInfo:(StockRewards Repository layer) {}", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(), "saveUserAccountInfo",
					methodArgs);
		} finally {
			ConnectionUtil.release(rs, ps);
		}
		return userAccountInfoId;
	}

	/**
	 * This method is used to save the userDisclosures along with the generated key
	 * from the saveUserAccountInfo method into the database on successful
	 * registration of a user
	 *
	 * @param conn
	 * @param userId
	 * @param registerUserRequestDto
	 * @param accountId
	 * @throws WayDaoException
	 */
	public void saveUserDisclosures(Connection conn, Integer userId, RegisterUserRequestDto registerUserRequestDto,
			Integer accountId) throws WayDaoException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String query = "INSERT INTO WAY_STOCK_REWARDS.tbl_user_disclosures SET USD_UAI_UserAccountInfoID =?"
					+ ",USD_Is_Affiliated_Exchange_Or_Finra=? ,USD_Is_Control_Person=?"
					+ ",USD_Is_Politically_Exposed=?,USD_Immediate_Family_Exposed=?,USD_CreatedUserID=?,"
					+ "USD_CreatedDateTime = current_timestamp(),USD_LastModifiedUserID=? ,USD_LastModifiedDateTime = current_timestamp();";

			ps = conn.prepareStatement(query);
			ps.setInt(1, accountId);
			ps.setBoolean(2, registerUserRequestDto.getDisclosures().isAffiliatedExchangeOrFinra());
			ps.setBoolean(3, registerUserRequestDto.getDisclosures().isControlPerson());
			ps.setBoolean(4, registerUserRequestDto.getDisclosures().isPoliticallyExposed());
			ps.setBoolean(5, registerUserRequestDto.getDisclosures().isImmediateFamilyExposed());
			ps.setInt(6, userId);
			ps.setInt(7, userId);
			ps.executeUpdate();
		} catch (Exception ex) {
			logger.error("saveUserDisclosures:(StockRewards Repository layer) {}", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(), "saveUserDisclosures",
					methodArgs);
		} finally {
			ConnectionUtil.release(rs, ps);
		}
	}

	/**
	 * This method save the userAgreements along with the generated key from the
	 * saveUserAccountInfo into the database on successful registration of a user
	 *
	 * @param conn
	 * @param userId
	 * @param agreementDetailDto
	 * @param accountId
	 * @throws WayDaoException
	 */
	public void saveUserAgreements(Connection conn, Integer userId, List<AgreementRequestDto> agreementDetailDto,
			Integer accountId) throws WayDaoException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String query = "INSERT INTO WAY_STOCK_REWARDS.tbl_user_agreements SET USA_UAI_UserAccountInfoID =?,"
					+ "USA_Agreement_Document_Type=?,USA_Signed_At=?,USA_IP_Address=?,USA_CreatedUserID=?,"
					+ "USA_CreatedDateTime=current_timestamp(),"
					+ "USA_LastModifiedUserID=?,USA_LastModifiedDateTime=current_timestamp();";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			for (AgreementRequestDto agreementDetail : agreementDetailDto) {
				ps.setInt(1, accountId);
				ps.setString(2, agreementDetail.getAgreement());
				ps.setString(3, DateUtil.getTimeStampFromString(agreementDetail.getSignedAt()));
				ps.setString(4, agreementDetail.getIpAddress());
				ps.setInt(5, userId);
				ps.setInt(6, userId);
				ps.addBatch();
			}
			ps.executeBatch();
		} catch (Exception ex) {
			logger.error("saveUserAgreements:(StockRewards Repository layer) {}", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(), "saveUserAgreements",
					methodArgs);
		} finally {
			ConnectionUtil.release(rs, ps);
		}
	}

	/**
	 * This method save the journalOrder Response into the database on successful
	 * creation of the journal order
	 *
	 * @param conn
	 * @param userId
	 * @param journalOrderDetails
	 * @throws WayDaoException
	 */
	public void saveJournalOrderDetails(Connection conn, Integer userId, JournalOrderResponseDto journalOrderDetails)
			throws WayDaoException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String query = "INSERT INTO WAY_STOCK_REWARDS.tbl_journals_order SET JUO_JournalID =?,"
					+ "JUO_UserAccountID_From=?,JUO_UserAccountID_To=?,JUO_Entry_Type=?,JUO_Symbol=?,"
					+ "JUO_Quantity=?,JUO_Price=?,JUO_Status=?,JUO_Net_Amount=?,JUO_Description=?,JUO_CreatedUserID=?,"
					+ "JUO_CreatedDateTime=current_timestamp(),JUO_LastModifiedUserID=?,"
					+ "JUO_LastModifiedDateTime=current_timestamp();";
			ps = conn.prepareStatement(query);

			ps.setString(1, journalOrderDetails.getJournalId());
			ps.setString(2, journalOrderDetails.getFromAccount());
			ps.setString(3, journalOrderDetails.getToAccount());
			ps.setString(4, journalOrderDetails.getEntryType());
			ps.setString(5, journalOrderDetails.getSymbol());
			ps.setString(6, journalOrderDetails.getQuantity());
			ps.setString(7, journalOrderDetails.getPrice());
			ps.setString(8, journalOrderDetails.getStatus());
			ps.setString(9, journalOrderDetails.getNetAmount() != null ? journalOrderDetails.getNetAmount() : null);
			ps.setString(10, journalOrderDetails.getDescription());
			ps.setInt(11, userId);
			ps.setInt(12, userId);

			ps.executeUpdate();
		} catch (Exception ex) {
			logger.error("saveJournalOrderDetails:(StockRewards Repository layer) {}", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(),
					"saveJournalOrderDetails", methodArgs);
		} finally {
			ConnectionUtil.release(rs, ps);
		}
	}

	/**
	 * This method is use to save Order Details response into the database on
	 * successful creation of an order
	 *
	 * @param conn
	 * @param userId
	 * @param orderDetails
	 * @throws WayDaoException
	 */
	public void saveSecurityOrderDetails(Connection conn, Integer userId, OrderDetailsResponseDto orderDetails)
			throws WayDaoException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String query = "INSERT INTO WAY_STOCK_REWARDS.tbl_securities_order SET SCO_USR_UserID =?,"
					+ "SCO_OrderID=?,SCO_Client_Order_ID=?,SCO_Created_At=?,SCO_Updated_At=?,"
					+ "SCO_Submitted_At=?,SCO_Filled_At=?,SCO_Asset_ID=?,SCO_Symbol=?,SCO_Quantity=?,SCO_Filled_Quantity=?,SCO_Filled_Average_Price=?,"
					+ "SCO_Type=?,SCO_Side=?,SCO_Time_In_Force=?,SCO_Limit_Price=?,SCO_Status=?,SCO_Entry_Type=?,SCO_CreatedUserID=?,"
					+ "SCO_CreatedDateTime=current_timestamp(),SCO_LastModifiedUserID=?,"
					+ "SCO_LastModifiedDateTime=current_timestamp();";
			ps = conn.prepareStatement(query);
			if (orderDetails.getSide().equals("buy")) {
				ps.setInt(1, 5);
			}
			ps.setInt(1, userId);
			ps.setString(2, orderDetails.getOrderId());
			ps.setString(3, orderDetails.getClientOrderId());
			ps.setString(4, DateUtil.getTimeStampFromResponseDate(orderDetails.getCreatedAt()));
			ps.setString(5, DateUtil.getTimeStampFromResponseDate(orderDetails.getUpdatedAt()));
			ps.setString(6, DateUtil.getTimeStampFromResponseDate(orderDetails.getSubmittedAt()));
			ps.setString(7,
					orderDetails.getFilledAt() != null
							? DateUtil.getTimeStampFromResponseDate(orderDetails.getFilledAt())
							: null);
			ps.setString(8, orderDetails.getAssetId());
			ps.setString(9, orderDetails.getSymbol());
			ps.setString(10, orderDetails.getQuantity());
			ps.setString(11, orderDetails.getFilledQuantity());
			ps.setString(12, orderDetails.getFilledAveragePrice());
			ps.setString(13, orderDetails.getType());
			ps.setString(14, orderDetails.getSide());
			ps.setString(15, orderDetails.getTimeInForce());
			ps.setString(16, orderDetails.getLimitPrice());
			ps.setString(17, orderDetails.getStatus());
			ps.setString(18, OrderType.ORDER.toString());
			ps.setInt(19, userId);
			ps.setInt(20, userId);

			ps.executeUpdate();
		} catch (Exception ex) {
			logger.error("saveSecurityOrderDetails:(StockRewards Repository layer) {}", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(),
					"saveSecurityOrderDetails", methodArgs);
		} finally {
			ConnectionUtil.release(rs, ps);
		}

	}

	/**
	 * This method is used to fetch the user's security order details from the
	 * database by using it's userId
	 * 
	 * @param page
	 * @param size
	 * @param range
	 * @param userId
	 * @return securityOrderDetailsList
	 * @throws WayDaoException
	 */
	public List<AccountActivityDetailsResponse> fetchSecurityOrderDetailsByUserId(Integer page, Integer size,
			Integer range, Integer userId) throws WayDaoException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AccountActivityDetailsResponse> securityOrderDetailsList = new ArrayList<>();
		try {
			String query = "SELECT  SCO_OrderID,SCO_Client_Order_ID,SCO_Created_At,SCO_Updated_At,"
					+ " SCO_Submitted_At,SCO_Filled_At,SCO_Asset_ID,SCO_Symbol,SCO_Quantity,SCO_Filled_Quantity,SCO_Filled_Average_Price,"
					+ " SCO_Type,SCO_Side,SCO_Time_In_Force,SCO_Limit_Price,SCO_Status,SCO_Entry_Type,SCO_CreatedDateTime from WAY_STOCK_REWARDS.tbl_securities_order "
					+ " where SCO_USR_UserID = ? and SCO_CreatedDateTime > now() - INTERVAL ? month "
					+ " order by SCO_CreatedDateTime desc " + " limit ?,?  ;";

			conn = connectionUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setInt(1, userId);
			ps.setInt(2, range != null ? range : 1);
			ps.setInt(3, (page - 1) * size);
			ps.setInt(4, size);
			rs = ps.executeQuery();
			while (rs.next()) {
				AccountActivityDetailsResponse securityOrderDetail = new AccountActivityDetailsResponse();
				securityOrderDetail.setId(rs.getString("SCO_OrderID"));
				securityOrderDetail.setClientOrderId(rs.getString("SCO_Client_Order_ID"));
				securityOrderDetail.setCreatedAt(rs.getDate("SCO_Created_At"));
				securityOrderDetail.setUpdatedAt(rs.getDate("SCO_Updated_At"));
				securityOrderDetail.setSubmittedAt(rs.getDate("SCO_Submitted_At"));
				securityOrderDetail.setSettleDate(rs.getDate("SCO_Filled_At"));
				securityOrderDetail.setAssetId(rs.getString("SCO_Asset_ID"));
				securityOrderDetail.setSymbol(rs.getString("SCO_Symbol"));
				securityOrderDetail.setQuantity(rs.getString("SCO_Quantity"));
				securityOrderDetail.setFilledQuantity(rs.getString("SCO_Filled_Quantity"));
				securityOrderDetail.setFilledAveragePrice(rs.getString("SCO_Filled_Average_Price"));
				securityOrderDetail.setType(rs.getString("SCO_Type"));
				securityOrderDetail.setLimitPrice(rs.getString("SCO_Limit_Price"));
				securityOrderDetail.setSide(rs.getString("SCO_Side"));
				securityOrderDetail.setTimeInForce(rs.getString("SCO_Time_In_Force"));
				securityOrderDetail.setStatus(rs.getString("SCO_Status"));
				securityOrderDetail.setEntryType(rs.getString("SCO_Entry_Type"));
				securityOrderDetail.setCreatedDate(rs.getDate("SCO_CreatedDateTime"));
				securityOrderDetailsList.add(securityOrderDetail);
			}
		} catch (Exception ex) {
			logger.error("fetchSecurityOrderDetailsByUserId:(StockRewards Repository layer) {}", methodArgs);
			handleExceptions(ex, logger, 1, ExceptionModule.STOCK_REWARDS, this.getClass(),
					"fetchSecurityOrderDetailsByUserId", methodArgs);
		} finally {
			ConnectionUtil.release(rs, ps, conn);
		}
		return securityOrderDetailsList;
	}

	/**
	 * 
	 * This method is used to fetch the user's journal order details from the
	 * database by using it's accountId
	 * 
	 * @param page
	 * @param size
	 * @param range
	 * @param accountId
	 * @return journalOrderDetailsList
	 * @throws WayDaoException
	 */
	public List<AccountActivityDetailsResponse> fetchJournalOrderDetailsByAccountId(Integer page, Integer size,
			Integer range, String accountId) throws WayDaoException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AccountActivityDetailsResponse> journalOrderDetailsList = new ArrayList<>();
		try {
			String query = "SELECT JUO_JournalID,JUO_Entry_Type,JUO_Symbol,JUO_Quantity,JUO_Price,JUO_Status,JUO_Description,JUO_Settle_Date,JUO_Net_Amount,JUO_CreatedDateTime "
					+ " from WAY_STOCK_REWARDS.tbl_journals_order "
					+ " where JUO_UserAccountID_To = ? OR JUO_UserAccountID_From= ? and JUO_CreatedDateTime > now() - INTERVAL ? month "
					+ " order by JUO_CreatedDateTime desc " + " limit  ? , ? ;";

			conn = connectionUtil.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, accountId);
			ps.setString(2, accountId);
			ps.setInt(3, range != null ? range : 1);
			ps.setInt(4, (page - 1) * size);
			ps.setInt(5, size);
			rs = ps.executeQuery();
			while (rs.next()) {
				AccountActivityDetailsResponse journalOrderDetails = new AccountActivityDetailsResponse();
				journalOrderDetails.setId(rs.getString("JUO_JournalID"));
				journalOrderDetails.setEntryType(rs.getString("JUO_Entry_Type"));
				journalOrderDetails.setSymbol(rs.getString("JUO_Symbol"));
				journalOrderDetails.setQuantity(rs.getString("JUO_Quantity"));
				journalOrderDetails.setPrice(rs.getString("JUO_Price"));
				journalOrderDetails.setStatus(rs.getString("JUO_Status"));
				journalOrderDetails.setDescription(rs.getString("JUO_Description"));
				journalOrderDetails.setSettleDate(rs.getDate("JUO_Settle_Date"));
				journalOrderDetails.setNetAmount(rs.getString("JUO_Net_Amount"));
				journalOrderDetails.setCreatedDate(rs.getDate("JUO_CreatedDateTime"));
				journalOrderDetailsList.add(journalOrderDetails);
			}
		} catch (Exception ex) {
			logger.error("fetchJournalOrderDetailsByAccountId:(StockRewards Repository layer) {}", methodArgs);
			handleExceptions(ex, logger, 1, ExceptionModule.STOCK_REWARDS, this.getClass(),
					"fetchJournalOrderDetailsByAccountId", methodArgs);
		} finally {
			ConnectionUtil.release(rs, ps, conn);
		}
		return journalOrderDetailsList;
	}

}
