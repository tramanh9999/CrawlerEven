package dao;

import entities.TblCategory;
import entities.TblProduct;
import utilities.DBUtilities;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryDAO {
    public CategoryDAO() {
    }

    private static CategoryDAO instance;
    private final static Object LOCK = new Object();

    public static CategoryDAO getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new CategoryDAO();
            }
            ;
        }

        return instance;
    }

//    protected TblCategory createCategory(String name) {
//        synchronized (LOCK) {
//            TblCategory category = null;
//            String realCategoryName = CategoryEnum.getRealCategoryName(name);
//            if (realCategoryName != null) {
//                CategoryDAO dao = CategoryDAO.getInstance();
//                category = dao.getFirstCategoryByName(realCategoryName);
//                if (category == null) {
//                    category = dao.getFirstCategoryByName(realCategoryName);
//                    dao.create(category);
//
//                }
//            }
//        }
//    }

//    private synchronized TblCategory getFirstCategoryByName(String realCategoryName) {
//        EntityManager em = DBUtilities.getEntityManager();
//        try {
//            List<TblCategory> result = em.createNamedQuery("TblCategory.findByCategoryName", TblCategory.class);
//        } catch (Exception e) {
//            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }

    public TblProduct getProductBy(String productName, String categoryld, String domain) {
        EntityManager em = DBUtilities.getEntityManager();
        try {
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            List<TblProduct> result = em.createNamedQuery("TblCategory.findByCategoryName", TblProduct.class)
                    .setParameter("categoryName", productName).
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
}
