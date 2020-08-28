package entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Tbl_Category", catalog = "OscarJpa", schema = "dbo")
@NamedQueries({@NamedQuery(name = "Tbl_Category.findAll", query = "SELECT t FROM TblCategory t")})
@XmlRootElement(name = "Category", namespace = "www.category.vn")
public class TblCategory implements Serializable {
    @Override
    public String toString() {
        return "TblCategory{" +
                "categoryId='" + categoryId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TblCategory category = (TblCategory) o;
        return Objects.equals(categoryId, category.categoryId) &&
                Objects.equals(categoryName, category.categoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, categoryName);
    }

    @Id
    @Basic(optional = false)
    @Column(name = "CategoryId", nullable = false, length = 200)
    @XmlAttribute(name = "CategoryId", required = true)
    private String categoryId;

    @Column(name = "CategoryName", length = 250)
    @XmlElement(name = "CategoryName", required = true, namespace = "www.category.vn")
    private String categoryName;


    public TblCategory() {
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
