/**
 * 
 */
package com.live106.mars.account.bean;

/**
 * <h1>账号通行证信息，服务器 动态生成，有时效</h1>
 * <br>
 * @author live106 @creation Oct 23, 2015
 */
public class UserPassport {
	private String identify;
	private String secureKey;

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	public String getSecureKey() {
		return secureKey;
	}

	public void setSecureKey(String secureKey) {
		this.secureKey = secureKey;
	}

}
