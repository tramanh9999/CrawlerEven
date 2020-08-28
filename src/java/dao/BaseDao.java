/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import utilities.DBUtilities;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AnhTHT
 */
public class BaseDao<T, PK extends Serializable> implements IGenericDAO<T, PK>{


    protected  Class<T> entityClass;

    public BaseDao() {
        ParameterizedType genericSuperClass= (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass= (Class<T>) genericSuperClass.getActualTypeArguments()[0];
    }

    @Override
    public T create(T t) {
        return null;
    }

    @Override
    public T findByID(PK pk) {
        return null;
    }

    @Override
    public T update(T t) {
        return null;
    }

    @Override
    public boolean delete(T t) {
        return false;
    }

    @Override
    public List<T> getAll(String namedQuery) {
        EntityManager em= DBUtilities.getEntityManager();
        try{

        }catch (Exception e){
            Logger.getLogger(BaseDao.class.getName()).log(Level.SEVERE, null, e);
        }finally {
            if(em!= null){
                em.close();
            }
        }
        return null;
    }


}
