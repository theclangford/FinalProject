package group.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import group.finalproject.cbc.RssModel;

public class CBCActivity extends Activity {
    private final static String TAG = "CBCActivity";
    private String rssUrl = "https://www.cbc.ca/cmlink/rss-world";

    private ListView newsList;
    private List<RssModel> rssNews;
    private RssNewsListAdapter rssNewsListAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbc);

        rssNews = new ArrayList<>();
        newsList = findViewById(R.id.newsList);
        progressBar = findViewById(R.id.progressBar);

        //Run fetching cbc news
        new FetchFeedTask().execute((Void) null);

        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RssModel rssModel = (RssModel) newsList.getItemAtPosition(position);
                Intent i = new Intent(CBCActivity.this, ArticleDetails.class);
                i.putExtra("articleTitle", rssModel.getTitle());
                i.putExtra("articleText", "Text should be here");
                i.putExtra("articleLink", rssModel.getLink());
                startActivityForResult(i, 11);
            }
        });
        //Set adapter
        rssNewsListAdapter = new RssNewsListAdapter(this);
        newsList.setAdapter(rssNewsListAdapter);
    }

    /**
     * Parse feed of news
     * @param inputStream
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private List<RssModel> parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        String title = null;
        String link = null;
        String description = null;
        boolean isItem = false;
        List<RssModel> items = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            progressBar.setVisibility(View.VISIBLE);
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if (name == null)
                    continue;

                if (eventType == XmlPullParser.END_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                Log.d("CBCActivity", "Parsing name ==> " + name);
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("link")) {
                    link = result;
                }

                if (title != null && link != null) {
                    if (isItem) {
                        RssModel item = new RssModel(title, link);
                        items.add(item);
                    }
                    title = null;
                    link = null;
                    isItem = false;
                }
            }
            progressBar.setVisibility(View.INVISIBLE);
            return items;
        } finally {
            inputStream.close();
        }
    }

    /**
     * Task for fetching
     */
    private class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(rssUrl))
                return false;

            try {
                if (!rssUrl.startsWith("http://") && !rssUrl.startsWith("https://"))
                    rssUrl = "http://" + rssUrl;

                URL url = new URL(rssUrl);
                InputStream inputStream = url.openConnection().getInputStream();
                rssNews = parseFeed(inputStream);
                return true;
            } catch (IOException e) {
                Log.e(TAG, "Error", e);
            } catch (XmlPullParserException e) {
                Log.e(TAG, "Error", e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                rssNewsListAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * RssNewsList Adapter
     */
    private class RssNewsListAdapter extends ArrayAdapter<RssModel> {

        public RssNewsListAdapter(Context context) {
            super(context, 0);
        }

        public int getCount() {
            return rssNews.size();
        }

        public RssModel getItem(int position) {

            return rssNews.get(position);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            LayoutInflater inflater;

            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.rss_item, null);
                holder = new ViewHolder();
                holder.rssTitleTextView = convertView.findViewById(R.id.rssTitle);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            RssModel rss = getItem(position);

            holder.rssTitleTextView.setText(
                    Html.fromHtml("<a href=\""+rss.getLink() +"\">" + rss.getTitle() + "</a>"));
            return convertView;
        }

        class ViewHolder {
            private TextView rssTitleTextView;
        }
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
