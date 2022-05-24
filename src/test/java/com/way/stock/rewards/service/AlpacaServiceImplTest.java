package com.way.stock.rewards.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.way.exception.util.ExceptionUtil;
import com.way.exception.util.WayServiceException;
import com.way.stock.rewards.enums.JournalType;
import com.way.stock.rewards.enums.OrderType;
import com.way.stock.rewards.enums.TimeInForce;
import com.way.stock.rewards.properties.AlpacaProperties;
import com.way.stock.rewards.repository.StockRewardsRepository;
import com.way.stock.rewards.request.dto.AgreementRequestDto;
import com.way.stock.rewards.request.dto.ContactRequestDto;
import com.way.stock.rewards.request.dto.DisclosuresRequestDto;
import com.way.stock.rewards.request.dto.IdentityRequestDto;
import com.way.stock.rewards.request.dto.JournalRequestDto;
import com.way.stock.rewards.request.dto.OrderRequestDto;
import com.way.stock.rewards.request.dto.RegisterUserRequestDto;
import com.way.stock.rewards.response.dto.AccountDetailsResponseDto;
import com.way.stock.rewards.response.dto.OrderDetailsResponseDto;
import com.way.stock.rewards.response.dto.PositionDetailResponseDto;
import com.way.stock.rewards.util.DateUtil;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:config/config-${spring.profiles.active:local}.properties")
public class AlpacaServiceImplTest {

	@InjectMocks
	AlpacaServiceImpl alpacaService;

	@Mock
	ExceptionUtil exceptionUtil;

	@Mock
	AlpacaProperties alpacaProperties;

	@Mock
	StockRewardsRepository stockRewardsRepository;

	@Value("${way.alpaca.apiKey}")
	private String apiKey;

	@Value("${way.alpaca.url}")
	private String url;

	Integer userId = 5;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() throws Exception {
		Mockito.when(alpacaProperties.getUrl()).thenReturn(url);
		Mockito.when(alpacaProperties.getApiKey()).thenReturn(apiKey);
		Mockito.when(stockRewardsRepository.fetchAccountIdByUserId(userId))
				.thenReturn("2e0ad164-242d-4cde-9f02-5cb480c78ba0");
	}

	@Test
	public void testGetAccountDetails() throws Exception {
		AccountDetailsResponseDto expected = alpacaService.getAccountDetails(userId);
		assertThat(expected).isNotNull();
	}

	@Test
	public void testGetAccountPositionDetails() throws Exception {
		List<PositionDetailResponseDto> expected = alpacaService.getAccountPositionDetails(userId);
		assertThat(expected).isNotNull();
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
		agreementDetail.setIpAddress("0.0.0.1");
		agreementDetail.setSignedAt(DateUtil.getCurrentDateTime());

		AgreementRequestDto agreementDetail1 = new AgreementRequestDto();
		agreementDetail1.setAgreement("account_agreement");
		agreementDetail1.setIpAddress("0.0.0.1");
		agreementDetail1.setSignedAt(DateUtil.getCurrentDateTime());

		AgreementRequestDto agreementDetail2 = new AgreementRequestDto();
		agreementDetail2.setAgreement("customer_agreement");
		agreementDetail2.setIpAddress("0.0.0.1");
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
		thrown.expect(WayServiceException.class);
		thrown.expectMessage(
				containsString("There is already an existing account registered with the same email address"));
		alpacaService.registerUser(userId, registerUserInfo);
	}

	@Test
	public void testBuySecurityOrder() throws Exception {
		OrderRequestDto order = new OrderRequestDto();
		order.setType(OrderType.market);
		order.setTimeInForce(TimeInForce.gtc);
		order.setQuantity("1");
		order.setSymbol("APOLOLOOO"); // no such security symbol
		thrown.expect(WayServiceException.class);
		thrown.expectMessage(containsString("Invalid input value"));
		alpacaService.buySecurityOrder(order, userId);
	}

	@Test
	public void testSellSecurityOrder() throws Exception {
		OrderDetailsResponseDto expected = alpacaService.sellSecurityOrder("APOLOLOOO", userId);
		assertThat(expected).isNull();
	}

	@Test
	public void testSellSecurityOrderWithQuantity() throws Exception {
		OrderDetailsResponseDto expected = alpacaService.sellSecurityOrderWithQuantity("APOLOLOOO", "11", userId);
		assertThat(expected).isNull();
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
		thrown.expect(WayServiceException.class);
		thrown.expectMessage(containsString("Insufficient Assets (JNLS)"));
		alpacaService.createJournalSecurityOrder(journalSecurityOrder, userId);

	}

	@Test
	public void testCreateJournalCashOrder() throws Exception {
		JournalRequestDto journalCashOrder = new JournalRequestDto();
		journalCashOrder.setToAccount("2e0ad164-242d-4cde-9f02-5cb480c78ba0");
		journalCashOrder.setFromAccount("9a02ece5-4bfe-38bf-842e-51f713bc7360");
		journalCashOrder.setAmount("100000000000000");
		journalCashOrder.setEntryType(JournalType.JNLC);
		journalCashOrder.setDescription("Test case");
		thrown.expect(WayServiceException.class);
		thrown.expectMessage(containsString("Insufficient Amount (JNLC)"));
		alpacaService.createJournalCashOrder(journalCashOrder, userId);
	}

}
