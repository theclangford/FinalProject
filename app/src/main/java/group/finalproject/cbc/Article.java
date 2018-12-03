package group.finalproject.cbc;

public class Article {
    private long id;
    private String title;
    private String text;
    private String link;
    private int wordCount;

    public Article() {
    }

    public long getId() {
        return id;
    }

    public Article(long id, String title, String text, String link, int wordCount) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.link = link;
        this.wordCount = wordCount;
    }

    public Article(String title, String text, String link, int wordCount) {
        this.title = title;
        this.text = text;
        this.link = link;
        this.wordCount = wordCount;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }
}
