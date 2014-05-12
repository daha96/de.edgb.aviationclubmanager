package de.edgb.aviationclubmanager.web;

import org.springframework.security.core.userdetails.User;

import de.edgb.aviationclubmanager.domain.UserAccount;

public class UserAccountDetails extends User {
	
	private static final long serialVersionUID = 1L;
	
	private UserAccount userAccount;
	
	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public UserAccountDetails(UserAccount account)
	{
		super(account.getUsername(), account.getPassword(), account.getEnabled(), true, true, true, account.getAuthorities());
		
		this.userAccount = account;
	}
}
