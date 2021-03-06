// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.edgb.aviationclubmanager.web.controller;

import de.edgb.aviationclubmanager.domain.Person;
import de.edgb.aviationclubmanager.domain.UserAccount;
import de.edgb.aviationclubmanager.domain.UserRole;
import de.edgb.aviationclubmanager.web.controller.UserAccountController;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect UserAccountController_Roo_Controller {
    
    void UserAccountController.populateEditForm(Model uiModel, UserAccount userAccount) {
        uiModel.addAttribute("userAccount", userAccount);
        uiModel.addAttribute("people", Person.findAllPeople());
        uiModel.addAttribute("userroles", UserRole.findAllUserRoles());
    }
    
    String UserAccountController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
}
