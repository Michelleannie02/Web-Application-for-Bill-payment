/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.expressunion.services;

import com.camtel.expressunion.entities.Expressunion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author herma
 */
@Local
public interface ExpressunionFacadeLocal {

    void create(Expressunion expressunion);

    void edit(Expressunion expressunion);

    void remove(Expressunion expressunion);

    Expressunion find(Object id);

    List<Expressunion> findAll();

    List<Expressunion> findRange(int[] range);

    int count();
    
}
