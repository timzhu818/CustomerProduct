package shutenmei.example.com.customerproduct.utils;

/**
 * Created by shutenmei on 16/4/26.
 */
public class CateItem {
    private String id;
    private String url;
    private String title;

    public CateItem(String id, String url, String title){
        this.id=id;
        this.url=url;
        this.title=title;
    }

    public String getId(){return id;}
    public String getTitle(){return title;}
    public String getUrl(){return url;}
}
