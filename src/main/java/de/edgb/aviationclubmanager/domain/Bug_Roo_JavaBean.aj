// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.edgb.aviationclubmanager.domain;

import de.edgb.aviationclubmanager.domain.Bug;
import de.edgb.aviationclubmanager.domain.Person;
import java.util.Date;

privileged aspect Bug_Roo_JavaBean {
    
    public void Bug.setBugDate(Date bugDate) {
        this.bugDate = bugDate;
    }
    
    public Person Bug.getPerson() {
        return this.person;
    }
    
    public void Bug.setPerson(Person person) {
        this.person = person;
    }
    
    public String Bug.getDescription() {
        return this.description;
    }
    
    public void Bug.setDescription(String description) {
        this.description = description;
    }
    
}
