package group.finalproject.movie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String MOVIE_INFO_TABLE_NAME = "movie_info";
    private static final String id_movie = "id_movie", id_poster = "id_poster", title = "title",
            year = "year", runtime = "runtime", actors = "actors", plot = "plot", poster = "poster";

    public DataBaseHelper(Context context) {
        super(context, MOVIE_INFO_TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable_movie_info = "CREATE TABLE " + MOVIE_INFO_TABLE_NAME +
                " (id_movie integer primary key autoincrement, " +
                title + " TEXT, " + year + " TEXT, " + runtime + " TEXT, " + actors + " TEXT, " +
                plot + " TEXT, " + poster + " BLOB)";
        sqLiteDatabase.execSQL(createTable_movie_info);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MOVIE_INFO_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


    public boolean addDataTo_movie_info(JSONArray items, byte[] moviePoster) {
        SQLiteDatabase db = this.getWritableDatabase();
        //onUpgrade(db,1,1);

        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put(title, items.getString(0));
            contentValues.put(year, items.getString(1));
            contentValues.put(runtime, items.getString(2));
            contentValues.put(actors, items.getString(3));
            contentValues.put(plot, items.getString(4));
            contentValues.put(poster,moviePoster);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        long results = db.insert(MOVIE_INFO_TABLE_NAME,null,contentValues);

        if(results == -1)
            return false;
        else
            return true;

    }

    //check a table if it has the record already
    public boolean CheckIsDataAlreadyInDBorNot(String TableName,
                                                      String dbfield, String fieldValue) {
        SQLiteDatabase sqldb = this.getWritableDatabase();
        String Query = "SELECT * FROM " + TableName + " WHERE " + dbfield + " = '" + fieldValue + "'";
        Cursor cursor = sqldb.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


    //delete a raw from table from the title
    public int deleteData (String TABLE_NAME, String sub) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "TITLE = ?",new String[] {sub});
    }


  public Cursor getData(String table){
      SQLiteDatabase db = this.getWritableDatabase();
      String query = "SELECT * FROM " + table;
      Cursor data = db.rawQuery(query,null);
      return data;
  }

  public Cursor getRowDataFromoTitle(String title){
      SQLiteDatabase db = this.getWritableDatabase();
      String query = "SELECT * FROM " + MOVIE_INFO_TABLE_NAME + " WHERE TITLE = '" + title + "'" ;
      Cursor data = db.rawQuery(query,null);
      return data;
  }

    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public byte[] getImage_fromDB(String title){
        SQLiteDatabase sqldb = this.getWritableDatabase();
        String Query = "SELECT poster FROM " + MOVIE_INFO_TABLE_NAME + " WHERE title" + " = '" + title + "'";
        Cursor cursor = sqldb.rawQuery(Query, null);

        if (cursor.moveToFirst()){
            byte[] imgByte = cursor.getBlob(0);
            cursor.close();
            return imgByte;
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return null ;
    }

}
