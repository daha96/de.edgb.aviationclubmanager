package de.edgb.aviationclubmanager.web.controller;
import de.edgb.aviationclubmanager.domain.Accounting;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/accountings")
@Controller
@RooWebScaffold(path = "accountings", formBackingObject = Accounting.class)
public class AccountingController {

	@PreAuthorize("hasRole('PERMISSION_ACCOUNTING_CREATE')")
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Accounting accounting, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, accounting);
            return "accountings/create";
        }
        uiModel.asMap().clear();
        accounting.persist();
        return "redirect:/accountings/" + encodeUrlPathSegment(accounting.getId().toString(), httpServletRequest);
    }

	@PreAuthorize("hasRole('PERMISSION_ACCOUNTING_CREATE')")
    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Accounting());
        return "accountings/create";
    }

	@PreAuthorize("hasRole('PERMISSION_ACCOUNTING')")
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("accounting", Accounting.findAccounting(id));
        uiModel.addAttribute("itemId", id);
        return "accountings/show";
    }

	@PreAuthorize("hasRole('PERMISSION_ACCOUNTING')")
    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("accountings", Accounting.findAccountingEntries(firstResult, sizeNo));
            float nrOfPages = (float) Accounting.countAccountings() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("accountings", Accounting.findAllAccountings());
        }
        return "accountings/list";
    }

	@PreAuthorize("hasRole('PERMISSION_ACCOUNTING_UPDATE')")
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Accounting accounting, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, accounting);
            return "accountings/update";
        }
        uiModel.asMap().clear();
        accounting.merge();
        return "redirect:/accountings/" + encodeUrlPathSegment(accounting.getId().toString(), httpServletRequest);
    }

	@PreAuthorize("hasRole('PERMISSION_ACCOUNTING_UPDATE')")
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Accounting.findAccounting(id));
        return "accountings/update";
    }

	@PreAuthorize("hasRole('PERMISSION_ACCOUNTING_DELETE')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Accounting accounting = Accounting.findAccounting(id);
        accounting.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/accountings";
    }

	void populateEditForm(Model uiModel, Accounting accounting) {
        uiModel.addAttribute("accounting", accounting);
    }
}
