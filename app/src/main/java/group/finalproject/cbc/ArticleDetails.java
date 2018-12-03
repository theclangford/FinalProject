package group.finalproject.cbc;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import group.finalproject.R;

public class ArticleDetails extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);

        Intent i = getIntent();
        if(i.getBooleanExtra("showSaveButton", false)){
            getArticleText(i.getStringExtra("articleLink"));
        } else {
            String title = i.getStringExtra("articleTitle");
            String text = i.getStringExtra("articleText");
            String link = i.getStringExtra("articleLink");
            displayArticle(title, text, link, false);
        }
    }

    /**
     * getArticle Text - load text of the article for saving and displaying
     * @param articleURL
     */
    private void getArticleText(String articleURL){

        /**
         * Creates thread for reading article text
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try {
                    Document doc = Jsoup.connect(articleURL).get();
                    Elements siteText = doc.select("main");
                    String articleHTML = siteText.get(0).html();
                    Document articleText = Jsoup.parse(articleHTML);
                    builder.append(articleText.text());

                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                //runOnUiThread is used to update UI of main activity
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = getIntent();
                        String title = i.getStringExtra("articleTitle");
                        String text = builder.toString();
                        String link = i.getStringExtra("articleLink");

                        displayArticle(title, text, link, true);
                    }
                });
            }
        }).start();
    }

    /**
     * Display content of an article by using ArticleFragment
     * @param title
     * @param text
     * @param link
     * @param showSaveButton
     */
    private void displayArticle(String title, String text, String link, boolean showSaveButton){
        ArticleFragment mf = new ArticleFragment();
        Bundle fragmentArgs = new Bundle();
        fragmentArgs.putString("articleTitle", title);
        fragmentArgs.putString("articleText", text);
        fragmentArgs.putString("articleLink", link);
        fragmentArgs.putBoolean("showSaveButton", showSaveButton);
        mf.setArguments(fragmentArgs);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_article, mf);
        ft.commit();

    }
}