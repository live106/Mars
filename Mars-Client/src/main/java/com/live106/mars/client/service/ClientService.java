/**
 * 
 */
package com.live106.mars.client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.live106.mars.protocol.thrift.ResponseUserLogin;

/**
 * @author live106 @creation Oct 10, 2015
 *
 */
@Service
public class ClientService {
	
	private final static Logger logger = LoggerFactory.getLogger(ClientService.class);
	private ResponseUserLogin loginResponse;
	
	public void setLoginResponse(ResponseUserLogin response) {
		this.loginResponse = response;
	}

	public ResponseUserLogin getLoginResponse() {
		return loginResponse;
	}

}
