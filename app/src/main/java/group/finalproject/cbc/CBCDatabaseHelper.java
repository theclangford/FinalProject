package group.finalproject.cbc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class CBCDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "CBCDatabaseHelper";
    private static final String DATABASE_NAME = "CBCArticles.db";
    private static int VERSION_NUM = 5;

    private static final String KEY_ID = "_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TEXT = "text";
    private static final String KEY_LINK = "link";
    private static final String TABLE_NAME = "article";
    private static final String KEY_WORD_COUNT = "word_count";

    private static final String CREATE_CBC_ARTICLE_TABLE = "create table "
            + TABLE_NAME + "(" + KEY_ID
            + " integer primary key autoincrement, "
            + KEY_TITLE + " text not null, "
            + KEY_TEXT + " text not null, "
            + KEY_LINK + " text not null, "
            + KEY_WORD_COUNT + " integer not null" + ");";

    private static final String SELECT_STATISTIC = "select SUM(" + KEY_WORD_COUNT + ") as sum, " +
            " AVG(" + KEY_WORD_COUNT + ") as avg, " +
            " MAX(" + KEY_WORD_COUNT + ") as max, " +
            " MIN(" + KEY_WORD_COUNT + ") as min " +
            " from " + TABLE_NAME;

    private static final String DROP_ARTICLE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    //create class
    public CBCDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    //method creates table
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "Calling onCreate");
        db.execSQL(CREATE_CBC_ARTICLE_TABLE);
    }

    // old version - when create new base onUpgrade method is called
    // new version -
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);
        db.execSQL(DROP_ARTICLE_TABLE);
        onCreate(db);
    }

    public void saveArticle(Article article) {
        final SQLiteDatabase db = getWritableDatabase();

        final ContentValues values = new ContentValues();
        values.put(KEY_TITLE, article.getTitle());
        values.put(KEY_TEXT, article.getText());
        values.put(KEY_LINK, article.getLink());
        values.put(KEY_WORD_COUNT, article.getWordCount());
        long insertId = db.insert(TABLE_NAME, null, values);

        if (insertId > 0) {
            Log.i(TAG, "News inserted successfully");
        } else {
            Log.e(TAG, "Failed to insert news");
        }
    }

    public Statistic getStatistic() {
        final SQLiteDatabase db = getReadableDatabase();
        Statistic stat = null;
        System.out.println("KU1");
        Cursor cur = db.rawQuery(SELECT_STATISTIC, null);

        try {
            System.out.println("Cursor = " + cur.toString());
            if (cur.moveToFirst()) {
                System.out.println("Cur is First");
                int sum = cur.getInt(cur.getColumnIndex("sum"));
                int average = cur.getInt(cur.getColumnIndex("avg"));
                int max = cur.getInt(cur.getColumnIndex("max"));
                int min = cur.getInt(cur.getColumnIndex("min"));
                System.out.println("sum="+sum + " average=" + average + " max=" + max + " min=" + min);
                stat = new Statistic(sum, average, max, min);
            }
        } catch (Exception e) {
            Log.d(TAG, "Error during getting statistics from DB");
        } finally {
            if (cur != null && !cur.isClosed()) {
                cur.close();
            }
        }
        return stat;
    }

    public ArrayList<Article> getArticles() {
        ArrayList<Article> articles = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.query(TABLE_NAME, new String[] {KEY_ID, KEY_TITLE, KEY_TEXT, KEY_LINK, KEY_WORD_COUNT}, null, null, null, null, null);

        try {
            if (cur.moveToFirst()) {
                do {
                    Article article = new Article();
                    article.setId(cur.getInt(cur.getColumnIndex(KEY_ID)));
                    article.setTitle(cur.getString(cur.getColumnIndex(KEY_TITLE)));
                    article.setText(cur.getString(cur.getColumnIndex(KEY_TEXT)));
                    article.setLink(cur.getString(cur.getColumnIndex(KEY_LINK)));
                    article.setWordCount(cur.getInt(cur.getColumnIndex(KEY_WORD_COUNT)));
                    articles.add(article);
                } while(cur.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get foods from database");
        } finally {
            if (cur != null && !cur.isClosed()) {
                cur.close();
            }
        }
        return articles;
    }

}
