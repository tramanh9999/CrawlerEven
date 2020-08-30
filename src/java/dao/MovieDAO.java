package dao;
public class MovieDAO {
    public MovieDAO() {
    }

    private static MovieDAO instance;
    private final static Object LOCK = new Object();

    public static MovieDAO getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new MovieDAO();
            }
        }
        return instance;
    }

}
