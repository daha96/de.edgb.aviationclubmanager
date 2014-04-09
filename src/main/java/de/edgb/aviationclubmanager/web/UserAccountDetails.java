package de.edgb.aviationclubmanager.web;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import de.edgb.aviationclubmanager.domain.UserAccount;
import de.edgb.aviationclubmanager.domain.UserPermission;

public class UserAccountDetails extends User {
	
	private UserAccount userAccount;
	
	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public UserAccountDetails(UserAccount account)
	{
		// TODO: so in RELEASE:
				super(account.getUsername(), account.getPassword(), account.getEnabled(), true, true, true, account.getAuthorities());
		// TODO: DEBUG:
				//		super(account.getUsername(), account.getPassword(), account.getEnabled(), true, true, true, UserPermission.getAllAuthorities());
		
		this.userAccount = account;
	}
}
