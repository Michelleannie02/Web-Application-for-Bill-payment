/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.expressunion.services;

import com.camtel.expressunion.entities.Expressunion;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author herma
 */
@Stateless
public class ExpressunionFacade extends AbstractFacade<Expressunion> implements ExpressunionFacadeLocal {

    @PersistenceContext(unitName = "com.camtel_ExpressUnion_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ExpressunionFacade() {
        super(Expressunion.class);
    }
    
}
