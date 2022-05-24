package com.way.stock.rewards.service;

import com.way.exception.util.WayServiceException;

public interface StockRewardsApiAclService {

	Integer getPrivilege(String requestUrl, String userRole, Integer userId) throws WayServiceException;

}
