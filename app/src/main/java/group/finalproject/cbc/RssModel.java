package group.finalproject.cbc;

public class RssModel {

    private String title;
    private String link;

    public RssModel(String title, String link) {
        this.title = title;
        this.link = link;
   }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }
}