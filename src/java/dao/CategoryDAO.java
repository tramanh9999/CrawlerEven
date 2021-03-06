package dao;
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
        }
        return instance;
    }

}
