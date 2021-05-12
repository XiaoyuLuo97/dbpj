package it.polimi.db2.entities;
import java.io.Serializable;
import javax.persistence.*;
import java.util.List;
@Entity
@Table(name = "marketing_question", schema = "db2")
@NamedQuery(name = "answer.getTodayQuestionByProdId", query = "SELECT r FROM MarketingQuestion r  WHERE r.productId = ?1")
public class MarketingQuestion implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    private int id;
    @Column(name = "question_content")
    private String questionContent;

    @Column(name = "product_id")
    private int productId;

    //这是儿子表，他没有外键
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "marketingQuestion")
    private List<MarketingAnswer> marketingAnswers;

    public List<MarketingAnswer> getMarketingAnswers() {
        return marketingAnswers;
    }

    public void setMarketingAnswers(List<MarketingAnswer> marketingAnswers) {
        this.marketingAnswers = marketingAnswers;
    }

    @ManyToOne
    @JoinColumn(name = "product_id",insertable = false,updatable = false)
    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

}
