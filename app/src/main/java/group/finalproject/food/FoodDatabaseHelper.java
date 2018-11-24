package group.finalproject.food;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

// used guide from https://guides.codepath.com/android/local-databases-with-sqliteopenhelper

public class FoodDatabaseHelper extends SQLiteOpenHelper {

    final String ACTIVITY_NAME = "FoodDatabaseHelper";
    static final String DATABASE_NAME = "Favorites.db";
    static final int VERSION_NUM = 1;

    static final String TABLE_NAME = "FAVORITES";

    static final String KEY_ID = "_id";
    static final String KEY_NAME = "NAME";
    static final String KEY_CAL = "CAL";
    static final String KEY_FAT = "FAT";
    static final String KEY_BRAND = "BRAND";
    static final String KEY_PROTEIN = "PROTEIN";
    static final String KEY_CARB = "CARB";
    static final String KEY_FIBER = "FIBER";
    static final String KEY_TAG = "TAG";

    private static FoodDatabaseHelper helperInstance;

    public static synchronized FoodDatabaseHelper getInstance(Context ctx) {
        if (helperInstance == null)
            helperInstance = new FoodDatabaseHelper(ctx.getApplicationContext());
        return helperInstance;
    }

    public void addFood(Food food) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, food.getName());
            values.put(KEY_CAL, food.getCalories());
            values.put(KEY_FAT, food.getFats());
            values.put(KEY_BRAND, food.getBrand());
            values.put(KEY_PROTEIN, food.getProtein());
            values.put(KEY_CARB, food.getCarbs());
            values.put(KEY_FIBER, food.getFiber());
            values.put(KEY_TAG, "");

            db.insertOrThrow(TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Food> getAllFoods() {
        ArrayList<Food> foods = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] {KEY_ID, KEY_NAME, KEY_CAL, KEY_FAT, KEY_BRAND, KEY_PROTEIN, KEY_CARB, KEY_FIBER, KEY_TAG}, null, null, null, null, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Food food = new Food();
                    food.setId(cursor.getDouble(cursor.getColumnIndex(KEY_ID)));
                    food.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                    food.setCalories(cursor.getDouble(cursor.getColumnIndex(KEY_CAL)));
                    food.setFats(cursor.getDouble(cursor.getColumnIndex(KEY_FAT)));
                    food.setBrand(cursor.getString(cursor.getColumnIndex(KEY_BRAND)));
                    food.setProtein(cursor.getDouble(cursor.getColumnIndex(KEY_PROTEIN)));
                    food.setCarbs(cursor.getDouble(cursor.getColumnIndex(KEY_CARB)));
                    food.setFiber(cursor.getDouble(cursor.getColumnIndex(KEY_FIBER)));
                    foods.add(food);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get foods from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return foods;
    }

    public void updateFoodTag(double id, String tag) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TAG, tag);

        db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[] {String.valueOf(id)});
    }

    public void deleteFood(double id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] {String.valueOf(id)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete food with id: " + id);
        } finally {
            db.endTransaction();
        }
    }

    public void deleteFood(String name, String brand) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_NAME, KEY_NAME + " = ? AND " + KEY_BRAND + " = ?", new String[] {name, brand});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete food with name = " + name + " and brand = " + brand);
        } finally {
            db.endTransaction();
        }
    }

    public boolean contains(String name, String brand) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] {KEY_NAME, KEY_BRAND}, KEY_NAME + " = ? AND " + KEY_BRAND + " = ?", new String[] {name, brand}, null, null, null);

        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }

        cursor.close();
        return true;
    }

    private FoodDatabaseHelper(Context ctx) { super(ctx, DATABASE_NAME, null, VERSION_NUM); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME +
                " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_NAME + " TEXT, " + KEY_CAL + " REAL, " +
                KEY_FAT + " REAL, " + KEY_BRAND + " TEXT, " +
                KEY_PROTEIN + " REAL, " + KEY_CARB + " REAL, "+
                KEY_FIBER + " REAL, " + KEY_TAG + " TEXT);");
        Log.i(ACTIVITY_NAME, "Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.i(ACTIVITY_NAME, "Calling onUpgrade, oldVersion=" + oldVersion + "newVersion=" + newVersion);
    }

}
