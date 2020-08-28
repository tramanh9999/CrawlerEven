package dao;

import entities.TblProduct;
import utilities.DBUtilities;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductDao extends BaseDao<TblProduct, Long> {
    private static ProductDao instance;

    public ProductDao() {
    }

    private final static Object LOCK = new Object();

    public static ProductDao getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new ProductDao();
            }
            ;
        }

        return instance;
    }

    public TblProduct getProductBy(String productName, String categoryld, String domain) {
        EntityManager em = DBUtilities.getEntityManager();
        try {
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            List<TblProduct> result = em.createNamedQuery("TblProduct.findByNameAndCategoryId", TblProduct.class)
                    .setParameter("productName", productName).
                            setParameter("category", categoryld)
                    .setParameter("resourceDomain", domain).
                            getResultList();
            transaction.commit();
            if (result == null && result.isEmpty()) {
                return result.get(0);
            }
        } catch (Exception ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (em == null) {
                em.close();
            }
        }
        return null;
    }

//    public synchronized void saveProductWhenCrawling(TblProduct product) {
//        TblProduct existedProduct = getProductBy(product.getProductHame(), product.getCategoryID()
//                , product.getResourceDomain());
//        TblProduct result;
//        if (existedProduct == null) {
//            result = create(product);
//        } else {
//            result = update(product);
//        }
//    }
}
