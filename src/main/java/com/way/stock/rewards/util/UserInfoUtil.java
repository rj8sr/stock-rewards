package com.way.stock.rewards.util;

import javax.servlet.http.HttpServletRequest;

import com.way.util.enumutil.WayRequestHeaders;

public class UserInfoUtil {

	public static String getUserIpFromRequest(HttpServletRequest request) {

		String remoteAddr = null;
		try {
			remoteAddr = request.getHeader(WayRequestHeaders.X_FORWARDED_FOR.getHeader());

			if (remoteAddr == null) {
				remoteAddr = request.getRemoteAddr();
			}

			if (remoteAddr.contains(",")) {
				remoteAddr = remoteAddr.split(",")[0];
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return remoteAddr;
	}
}
