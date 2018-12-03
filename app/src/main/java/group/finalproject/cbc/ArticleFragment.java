package group.finalproject.cbc;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import group.finalproject.R;

/**
 * Article fragment
 */
public class ArticleFragment extends Fragment {
    private CBCDatabaseHelper cbcDatabaseHelper;
    private Bundle messageBundle;
    private String title;
    private String text;
    private String link;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        messageBundle = this.getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.cbc_article, container, false);
        cbcDatabaseHelper = new CBCDatabaseHelper(this.getActivity());

        TextView articleTextView = view.findViewById(R.id.articleText);
        articleTextView.setMovementMethod(new ScrollingMovementMethod());
        TextView articleLinkView = view.findViewById(R.id.articleLink);
        TextView articleTitleView = view.findViewById(R.id.articleTitle);

        boolean showSaveButton = messageBundle.getBoolean("showSaveButton");
        Button saveArticleButton = view.findViewById(R.id.saveArticleButton);
        System.out.println("Title after sending = " +messageBundle.getString("articleTitle") );
        System.out.println("Text = " + messageBundle.getString("articleText"));
        title = messageBundle.getString("articleTitle");
        text = messageBundle.getString("articleText");
        link = messageBundle.getString("articleLink");

        if(showSaveButton){
            articleTextView.setText(text);
            articleTitleView.setText(title);
            articleLinkView.setText(Html.fromHtml("<a href=\""+ link + "\">" + link + "</a>"));
        } else {
            articleLinkView.setText(link);
            articleTextView.setText(text);
            articleTitleView.setText(title);
            saveArticleButton.setVisibility(View.INVISIBLE);
        }


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
                cbcDatabaseHelper.saveArticle(new Article(title, text, link, getWordCount(text)));

                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Article was saved!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        return view;
    }

    private int getWordCount(String text){
        if(text != null && text.length() > 0)
        return text.split(" ").length;
        return 0;
    }
}
