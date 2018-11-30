package group.finalproject.cbc;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import group.finalproject.R;

/**
 * Article fragment
 */
public class ArticleFragment extends Fragment {

    private Bundle messageBundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        messageBundle = this.getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.cbc_article, container, false);

        TextView articleTitleView = view.findViewById(R.id.articleTitle);
        TextView articleTextView = view.findViewById(R.id.articleText);
        TextView articleLinkView = view.findViewById(R.id.articleLink);

        Button saveArticleButton = view.findViewById(R.id.saveArticleButton);

        String title = messageBundle.getString("articleTitle");
        String text = messageBundle.getString("articleText");
        String link = messageBundle.getString("articleLink");

        articleTitleView.setText(title);
        articleTextView.setText(text);
        articleLinkView.setText(Html.fromHtml("<a href=\""+ link + "\">" + link + "</a>"));

        articleLinkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(browserIntent);
            }
        });

        saveArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}
