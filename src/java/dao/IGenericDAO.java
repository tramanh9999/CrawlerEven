/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.util.List;

/**
 * @author AnhTHT
 */
public interface IGenericDAO<T, PK> {
    T create(T t) throws ParserConfigurationException, TransformerException;

    T findByID(PK pk);

    T update(T t);

    boolean delete(T t);

    List<T> getAll(String namedQuery);

}
