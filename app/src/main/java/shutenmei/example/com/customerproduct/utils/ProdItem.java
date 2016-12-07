package shutenmei.example.com.customerproduct.utils;

/**
 * Created by shutenmei on 16/4/27.
 */
public class ProdItem {
    private String id;
    private String url;
    private String title;
    private String quantity;
    private String price;
    private String discription;


    public ProdItem(String id, String url, String title, String quantity, String price, String discription){
        this.id=id;
        this.url=url;
        this.title=title;
        this.quantity=quantity;
        this.price=price;
        this.discription=discription;
    }

    public String getId(){return id;}
    public String getTitle(){return title;}
    public String getUrl(){return url;}
    public String getQuantity(){return quantity;}
    public String getPrice(){return price;}
    public String getDiscription(){return discription;}

}
