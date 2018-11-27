package group.finalproject;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import group.finalproject.cbc.ArticleFragment;

public class ArticleDetails extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);

        Intent i = getIntent();

        ArticleFragment mf = new ArticleFragment();
        Bundle fragmentArgs = new Bundle();
        fragmentArgs.putString("articleTitle", i.getStringExtra("articleTitle"));
        fragmentArgs.putString("articleText", i.getStringExtra("articleText"));
        fragmentArgs.putString("articleLink", i.getStringExtra("articleLink"));
        mf.setArguments(fragmentArgs);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_article, mf);
        ft.commit();
    }
}