package de.edgb.aviationclubmanager.web;


import javax.persistence.NoResultException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.edgb.aviationclubmanager.domain.UserAccount;

@Service
public class UserAccountDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		
		UserAccount account = null;
		
		try
		{
			account = UserAccount.findUserAccountsByUsernameEquals(username).getSingleResult();
		}
		catch (NoResultException e)
		{
			throw new UsernameNotFoundException("User not found: " + username);
		}
		
		return new UserAccountDetails(account);
	}
}
