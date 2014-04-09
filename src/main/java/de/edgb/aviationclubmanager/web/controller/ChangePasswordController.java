package de.edgb.aviationclubmanager.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.edgb.aviationclubmanager.domain.ChangePasswordModel;
import de.edgb.aviationclubmanager.domain.UserAccount;
import de.edgb.aviationclubmanager.domain.validators.ChangePasswordValidator;
import de.edgb.aviationclubmanager.web.UserAccountDetails;

@RequestMapping("/changepassword")
@Controller
public class ChangePasswordController {
	
	@Autowired
	ChangePasswordValidator changePasswordValidator;
	
	@Autowired
	ShaPasswordEncoder encoder;

	@PreAuthorize("hasRole('PERMISSION_CHANGEPASSWORD')")
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String changePassword(@ModelAttribute("changePassword") ChangePasswordModel changePassword, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
			
		
		changePasswordValidator.validate(changePassword, bindingResult);
		
		
        if (bindingResult.hasErrors()) {
        	uiModel.addAttribute("changePassword", changePassword);
    		
            return "changepassword/form";
        }
        uiModel.asMap().clear();
        
        UserAccount userAccount = ((UserAccountDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserAccount();
        userAccount.setPassword(encoder.encodePassword(changePassword.getNewPassword(), null));
        userAccount.merge();
        
        return "changepassword/success";
    }

	@PreAuthorize("hasRole('PERMISSION_CHANGEPASSWORD')")
    @RequestMapping(params = "form", produces = "text/html")
    public String passwordForm(Model uiModel) {
		
		uiModel.addAttribute("changePassword", new ChangePasswordModel());
		
        return "changepassword/form";
    }
}
