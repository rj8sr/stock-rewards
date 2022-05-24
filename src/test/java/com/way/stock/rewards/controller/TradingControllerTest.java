package com.way.stock.rewards.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import com.way.authutil.filter.WayMetadataFilter;
import com.way.core.util.dto.PaginationData;
import com.way.exception.dao.SystemUserConfiguration;
import com.way.exception.util.ExceptionUtil;
import com.way.stock.rewards.enums.JournalType;
import com.way.stock.rewards.enums.OrderType;
import com.way.stock.rewards.enums.TimeInForce;
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
import com.way.stock.rewards.response.dto.OrderDetailsResponseDto;
import com.way.stock.rewards.response.dto.PositionDetailResponseDto;
import com.way.stock.rewards.response.dto.UserAccountInfoResponseDto;
import com.way.stock.rewards.service.TradingServiceImpl;
import com.way.stock.rewards.util.DateUtil;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = TradingController.class)
public class TradingControllerTest {

	@MockBean
	TradingServiceImpl tradingService;

	@MockBean
	ExceptionUtil exceptionUtil;

	@MockBean
	HazelcastInstance cacheInstance;

	@MockBean
	SystemUserConfiguration systemUserConfiguration;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WayMetadataFilter wayMetadataFilter;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	private Integer userId = 5;

	@Before
	public void init() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).addFilter(wayMetadataFilter, "/").build();
	}

	@Test
	public void testGetAccountDetails() throws Exception {
		AccountDetailsResponse accountDetails = new AccountDetailsResponse();
		Mockito.when(tradingService.getAccountDetails(userId)).thenReturn(accountDetails);
		this.mockMvc.perform(get("/v1/trading/account/details").requestAttr("userId", userId))
				.andExpect(status().isOk()).andExpect(jsonPath("$.apiStatus", is("Success")));
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

		Mockito.when(tradingService.registerUser(Mockito.any(RegisterUserRequestDto.class), Mockito.anyString(),
				Mockito.anyInt())).thenReturn(accountDetails);
		this.mockMvc.perform(post("/v1/trading/account/register/user").requestAttr("userId", userId)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerUserInfo)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.apiStatus", is("Success")));
	}

	@Test
	public void testGetAccountActivityDetails() throws Exception {
		PaginationData<AccountActivityDetailsResponse> accountActivityDetails = new PaginationData<>();
		Mockito.when(tradingService.getAccountActivityDetails(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(),
				Mockito.anyInt())).thenReturn(accountActivityDetails);
		this.mockMvc.perform(
				get("/v1/trading/account/activity/details?page=1&size=25&range=3").requestAttr("userId", userId))
				.andExpect(status().isOk()).andExpect(jsonPath("$.apiStatus", is("Success")));
	}

	@Test
	public void testGetAccountPositionDetails() throws Exception {
		PaginationData<PositionDetailResponseDto> accountPositionDetails = new PaginationData<>();
		Mockito.when(tradingService.getAccountPositionDetails(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()))
				.thenReturn(accountPositionDetails);
		this.mockMvc.perform(get("/v1/trading/account/position/details?page=1&size=2").requestAttr("userId", userId))
				.andExpect(status().isOk()).andExpect(jsonPath("$.apiStatus", is("Success")));
	}

	@Test
	public void testBuySecurityOrder() throws Exception {
		OrderRequestDto order = new OrderRequestDto();
		order.setType(OrderType.market);
		order.setTimeInForce(TimeInForce.gtc);
		order.setQuantity("1");
		order.setSymbol("APOLOLOOO");
		List<OrderDetailsResponseDto> orderDetailList = new ArrayList<>();
		OrderDetailsResponse orderResponse = new OrderDetailsResponse();
		OrderDetailsResponseDto orderResponseDto = new OrderDetailsResponseDto();
		orderDetailList.add(orderResponseDto);
		orderResponse.setOrderDetails(orderDetailList);

		Mockito.when(tradingService.buySecurityOrder(Mockito.any(OrderRequestDto.class), Mockito.anyInt()))
				.thenReturn(orderResponse);
		this.mockMvc
				.perform(post("/v1/trading/account/buy/order").requestAttr("userId", userId)
						.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(order)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.apiStatus", is("Success")));
	}

	@Test
	public void testSellSecurityOrder() throws Exception {
		List<OrderDetailsResponseDto> orderDetailList = new ArrayList<>();
		OrderDetailsResponse orderResponse = new OrderDetailsResponse();
		OrderDetailsResponseDto orderResponseDto = new OrderDetailsResponseDto();
		orderDetailList.add(orderResponseDto);
		orderResponse.setOrderDetails(orderDetailList);
		Mockito.when(tradingService.sellSecurityOrder(Mockito.anyString(), Mockito.anyInt())).thenReturn(orderResponse);
		this.mockMvc.perform(delete("/v1/trading/account/order/sell/{symbol}", "APPL").requestAttr("userId", userId))
				.andExpect(status().isOk()).andExpect(jsonPath("$.apiStatus", is("Success")));
	}

	@Test
	public void testCreateJournalSecurityOrder() throws Exception {
		JournalRequestDto journalSecurityOrder = new JournalRequestDto();
		journalSecurityOrder.setToAccount("2e0ad164-242d-4cde-9f02-5cb480c78ba0");
		journalSecurityOrder.setFromAccount("9a02ece5-4bfe-38bf-842e-51f713bc7360");
		journalSecurityOrder.setSymbol("APPLOO");
		journalSecurityOrder.setEntryType(JournalType.JNLS);
		journalSecurityOrder.setDescription("Test case");
		journalSecurityOrder.setQuantity("1");
		JournalOrderResponse orderResponse = new JournalOrderResponse();
		Mockito.when(tradingService.createJournalSecurityOrder(Mockito.any(JournalRequestDto.class), Mockito.anyInt()))
				.thenReturn(orderResponse);
		this.mockMvc.perform(post("/v1/trading/account/journal/security/order").requestAttr("userId", userId)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(journalSecurityOrder)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.apiStatus", is("Success")));
	}

	@Test
	public void testCreateJournalCashOrder() throws Exception {
		JournalRequestDto journalCashOrder = new JournalRequestDto();
		journalCashOrder.setToAccount("2e0ad164-242d-4cde-9f02-5cb480c78ba0");
		journalCashOrder.setFromAccount("9a02ece5-4bfe-38bf-842e-51f713bc7360");
		journalCashOrder.setAmount("100000000000000");
		journalCashOrder.setEntryType(JournalType.JNLC);
		journalCashOrder.setDescription("Test case");

		JournalOrderResponse orderResponse = new JournalOrderResponse();
		Mockito.when(tradingService.createJournalCashOrder(Mockito.any(JournalRequestDto.class), Mockito.anyInt()))
				.thenReturn(orderResponse);
		this.mockMvc.perform(post("/v1/trading/account/journal/cash/order").requestAttr("userId", userId)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(journalCashOrder)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.apiStatus", is("Success")));
	}

}
