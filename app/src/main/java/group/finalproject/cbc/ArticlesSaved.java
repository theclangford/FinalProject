package group.finalproject.cbc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import group.finalproject.R;

public class ArticlesSaved extends AppCompatActivity {

    private final String ACTIVITY = "ArticleSavedActivity";

    private List<Article> articles;
    private CBCDatabaseHelper cbcDatabaseHelper;
    private ListView savedArticlesView;
    private TextView statisticView;
    private ArticleItemAdapter articleItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_saved);

        savedArticlesView = findViewById(R.id.savedArticleList);
        statisticView = findViewById(R.id.statistic);

        cbcDatabaseHelper = new CBCDatabaseHelper(this.getApplicationContext());
        articles = cbcDatabaseHelper.getArticles();
        articleItemAdapter = new ArticleItemAdapter(this);
        articleItemAdapter.setList(articles);
        savedArticlesView.setAdapter(articleItemAdapter);

        savedArticlesView.setOnItemClickListener((parent, view, position, id) -> {
            Article currentItem = articles.get(position);
            Intent i = new Intent(ArticlesSaved.this, ArticleDetails.class);
            System.out.println("Title before sending " + currentItem.getTitle());
            i.putExtra("articleTitle", currentItem.getTitle());
            i.putExtra("articleText", currentItem.getText());
            i.putExtra("articleLink", currentItem.getLink());
            i.putExtra("showSaveButton", false);
            startActivityForResult(i, 11);
        });
        Statistic statistic = cbcDatabaseHelper.getStatistic();
        String statisticText = "Number of saved news: " + statistic.getSum() + "\n" +
                "Average word count: " + statistic.getAvg() + "\n" +
                "Max word count: " + statistic.getAvg() + "\n" +
                "Min word count:" + statistic.getMin();
        statisticView.setText(statisticText);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 11) {
            if (resultCode == RESULT_OK) {
            }
        }
    }
}
