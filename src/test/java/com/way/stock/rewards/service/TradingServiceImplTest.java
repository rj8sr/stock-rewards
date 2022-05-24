package com.way.stock.rewards.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.way.core.util.dto.PaginationData;
import com.way.exception.util.ExceptionUtil;
import com.way.stock.rewards.datasource.config.ConnectionUtil;
import com.way.stock.rewards.enums.OrderType;
import com.way.stock.rewards.enums.TimeInForce;
import com.way.stock.rewards.repository.StockRewardsRepository;
import com.way.stock.rewards.request.dto.AgreementRequestDto;
import com.way.stock.rewards.request.dto.ContactRequestDto;
import com.way.stock.rewards.request.dto.DisclosuresRequestDto;
import com.way.stock.rewards.request.dto.IdentityRequestDto;
import com.way.stock.rewards.request.dto.JournalRequestDto;
import com.way.stock.rewards.request.dto.OrderRequestDto;
import com.way.stock.rewards.request.dto.RegisterUserRequestDto;
import com.way.stock.rewards.response.AccountActivityDetailsResponse;
import com.way.stock.rewards.response.AccountDetailsResponse;
import com.way.stock.rewards.response.JournalOrderResponse;
import com.way.stock.rewards.response.OrderDetailsResponse;
import com.way.stock.rewards.response.UserAccountInfoResponse;
import com.way.stock.rewards.response.dto.AccountDetailsResponseDto;
import com.way.stock.rewards.response.dto.PositionDetailResponseDto;
import com.way.stock.rewards.response.dto.UserAccountInfoResponseDto;
import com.way.stock.rewards.util.DateUtil;

@RunWith(MockitoJUnitRunner.class)
public class TradingServiceImplTest {

	@InjectMocks
	TradingServiceImpl tradingService;

	@Mock
	AlpacaServiceImpl alpacaService;

	@Mock
	ExceptionUtil exceptionUtil;

	@Mock
	StockRewardsRepository stockRewardsRepository;

	@Mock
	private ConnectionUtil connectionUtil;

	@Mock
	Connection jdbcConnection;

	Integer userId = 5;

	@Test
	public void testGetAccountDetails() throws Exception {
		Mockito.when(alpacaService.getAccountDetails(userId)).thenReturn(Mockito.any(AccountDetailsResponseDto.class));
		AccountDetailsResponse expected = tradingService.getAccountDetails(userId);
		assertThat(expected.getAccountDetails()).isNull();
	}

	@Test
	public void testGetAccountActivityDetails() throws Exception {
		PaginationData<AccountActivityDetailsResponse> expected = tradingService.getAccountActivityDetails(1, 2, 3,
				userId);
		assertEquals(2, expected.getSize().intValue());
	}

	@Test
	public void testGetAccountPositionDetails() throws Exception {
		PaginationData<PositionDetailResponseDto> expected = tradingService.getAccountPositionDetails(1, 2, userId);
		assertEquals(2, expected.getSize().intValue());
	}

	@Test
	public void testRegisterUser() throws Exception {
		RegisterUserRequestDto registerUserInfo = new RegisterUserRequestDto();
		ContactRequestDto contactInfo = new ContactRequestDto();
		contactInfo.setCity("San Mateo");
		contactInfo.setEmailAddress("rajat@gmaill.com");
		contactInfo.setPhoneNumber("555-666-7788");
		contactInfo.setState("USA");

		List<String> streetAddress = new ArrayList<>();
		streetAddress.add("20 N San Mateo Dr");
		contactInfo.setStreetAddress(streetAddress);
		registerUserInfo.setContact(contactInfo);

		AgreementRequestDto agreementDetail = new AgreementRequestDto();
		agreementDetail.setAgreement("margin_agreement");
		agreementDetail.setIpAddress("127.0.0.1");
		agreementDetail.setSignedAt(DateUtil.getCurrentDateTime());

		AgreementRequestDto agreementDetail1 = new AgreementRequestDto();
		agreementDetail1.setAgreement("account_agreement");
		agreementDetail1.setIpAddress("127.0.0.1");
		agreementDetail1.setSignedAt(DateUtil.getCurrentDateTime());

		AgreementRequestDto agreementDetail2 = new AgreementRequestDto();
		agreementDetail2.setAgreement("customer_agreement");
		agreementDetail2.setIpAddress("127.0.0.1");
		agreementDetail2.setSignedAt(DateUtil.getCurrentDateTime());

		List<AgreementRequestDto> agreementsDetails = new ArrayList<>();
		agreementsDetails.add(agreementDetail2);
		agreementsDetails.add(agreementDetail1);
		agreementsDetails.add(agreementDetail);

		registerUserInfo.setAgreements(agreementsDetails);

		IdentityRequestDto identityRequest = new IdentityRequestDto();
		identityRequest.setCountryOfTaxResidence("USA");
		identityRequest.setDateOfBirth("1990-01-01");
		identityRequest.setFamilyName("Rajat");

		List<String> employmentIncome = new ArrayList<>();
		employmentIncome.add("employment_income");
		identityRequest.setFundingSource(employmentIncome);
		identityRequest.setGivenName("Rajat");

		registerUserInfo.setIdentity(identityRequest);

		DisclosuresRequestDto disclosures = new DisclosuresRequestDto();
		disclosures.setAffiliatedExchangeOrFinra(false);
		disclosures.setControlPerson(false);
		disclosures.setImmediateFamilyExposed(false);
		disclosures.setPoliticallyExposed(false);
		registerUserInfo.setDisclosures(disclosures);
		UserAccountInfoResponse accountDetails = new UserAccountInfoResponse();
		UserAccountInfoResponseDto userInfo = new UserAccountInfoResponseDto();
		accountDetails.setUserAccountInfo(userInfo);
		doNothing().when(jdbcConnection).setAutoCommit(false);
		Mockito.when(connectionUtil.getConnection()).thenReturn(jdbcConnection);
		tradingService.registerUser(registerUserInfo, "127.0.0.1", userId);
		assertEquals("USA", registerUserInfo.getContact().getState());

	}

	@Test
	public void testBuySecurityOrder() throws Exception {
		OrderRequestDto order = new OrderRequestDto();
		order.setType(OrderType.market);
		order.setTimeInForce(TimeInForce.gtc);
		order.setQuantity("1");
		order.setSymbol("APOLOLOOO");
		doNothing().when(jdbcConnection).setAutoCommit(false);
		Mockito.when(connectionUtil.getConnection()).thenReturn(jdbcConnection);
		OrderDetailsResponse expected = tradingService.buySecurityOrder(order, userId);
		assertThat(expected.getOrderDetails().get(0)).isNull();
	}

	@Test
	public void testSellSecurityOrderWithQuantity() throws Exception {
		doNothing().when(jdbcConnection).setAutoCommit(false);
		Mockito.when(connectionUtil.getConnection()).thenReturn(jdbcConnection);
		OrderDetailsResponse expected = tradingService.sellSecurityOrderWithQuantity("APOLOLOOO", "11", userId);
		assertThat(expected.getOrderDetails().get(0)).isNull();
	}

	@Test
	public void testSellSecurityOrder() throws Exception {
		doNothing().when(jdbcConnection).setAutoCommit(false);
		Mockito.when(connectionUtil.getConnection()).thenReturn(jdbcConnection);
		OrderDetailsResponse expected = tradingService.sellSecurityOrder("APOLOLOOO", userId);
		assertThat(expected.getOrderDetails().get(0)).isNull();
	}

	@Test
	public void testCreateJournalSecurityOrder() throws Exception {
		JournalRequestDto journalOrderDto = new JournalRequestDto();
		doNothing().when(jdbcConnection).setAutoCommit(false);
		Mockito.when(connectionUtil.getConnection()).thenReturn(jdbcConnection);
		JournalOrderResponse expected = tradingService.createJournalSecurityOrder(journalOrderDto, userId);
		assertThat(expected.getJournalOrderDetails()).isNull();
	}

	@Test
	public void testCreateJournalCashOrder() throws Exception {
		JournalRequestDto journalOrderDto = new JournalRequestDto();
		doNothing().when(jdbcConnection).setAutoCommit(false);
		Mockito.when(connectionUtil.getConnection()).thenReturn(jdbcConnection);
		JournalOrderResponse expected = tradingService.createJournalCashOrder(journalOrderDto, userId);
		assertThat(expected.getJournalOrderDetails()).isNull();
	}

}
