// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.edgb.aviationclubmanager.domain;

import de.edgb.aviationclubmanager.domain.Aircraft;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Aircraft_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager Aircraft.entityManager;
    
    public static final List<String> Aircraft.fieldNames4OrderClauseFilter = java.util.Arrays.asList("registration", "callsign", "type", "model", "holder", "seats", "comment");
    
    public static final EntityManager Aircraft.entityManager() {
        EntityManager em = new Aircraft().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Aircraft.countAircrafts() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Aircraft o", Long.class).getSingleResult();
    }
    
    public static List<Aircraft> Aircraft.findAllAircrafts(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Aircraft o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Aircraft.class).getResultList();
    }
    
    public static Aircraft Aircraft.findAircraft(Long id) {
        if (id == null) return null;
        return entityManager().find(Aircraft.class, id);
    }
    
    public static List<Aircraft> Aircraft.findAircraftEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Aircraft o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Aircraft.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void Aircraft.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Aircraft.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Aircraft attached = Aircraft.findAircraft(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Aircraft.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void Aircraft.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public Aircraft Aircraft.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Aircraft merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
