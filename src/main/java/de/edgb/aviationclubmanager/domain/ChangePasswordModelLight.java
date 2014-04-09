package de.edgb.aviationclubmanager.domain;

import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;

@RooJavaBean
public class ChangePasswordModelLight {

	@NotNull
	private String newPassword;

	@NotNull
	private String confirmNewPassword;
	

	public Long getId() {
		return 1L;
	}
	public void setId(Long id) {
	//	this.id = id;
	}
	public Long getVersion() {
		return 1L;
	}
	public void setVersion(Long version) {
//		this.version = version;
	}
}
