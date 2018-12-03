package group.finalproject.food;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

/**
 * Singleton database to store food items.
 * Attributes include name, calories, fat, brand, protein, carbohydrates, fiber and tag
 * Used guide from https://guides.codepath.com/android/local-databases-with-sqliteopenhelper
  */

public class FoodDatabaseHelper extends SQLiteOpenHelper {

    /**
     * Activity name
     */
    private final String ACTIVITY_NAME = "FoodDatabaseHelper";

    /**
     * Database name
     */
    private static final String DATABASE_NAME = "Favorites.db";

    /**
     * Version number
     */
    private static final int VERSION_NUM = 1;

    /**
     * Table name
     */
    private static final String TABLE_NAME = "FAVORITES";

    /**
     * Name of ID column
     */
    private static final String KEY_ID = "_id";

    /**
     * Name of name column
     */
    private static final String KEY_NAME = "NAME";

    /**
     * Name of calorie column
     */
    private static final String KEY_CAL = "CAL";

    /**
     * Name of fat column
     */
    private static final String KEY_FAT = "FAT";

    /**
     * Name of brand column
     */
    private static final String KEY_BRAND = "BRAND";

    /**
     * Name of protein column
     */
    private static final String KEY_PROTEIN = "PROTEIN";

    /**
     * Name of carbohydrates column
     */
    private static final String KEY_CARB = "CARB";

    /**
     * Name of fiber column
     */
    private static final String KEY_FIBER = "FIBER";

    /**
     * Name of tag column
     */
    private static final String KEY_TAG = "TAG";

    /**
     * Static variable of FoodDatabaseHelper
     */
    private static FoodDatabaseHelper helperInstance;

    /**
     * Gets the static instance of FoodDatabaseHelper
     * @param ctx
     * @return helperInstance
     */
    public static synchronized FoodDatabaseHelper getInstance(Context ctx) {
        if (helperInstance == null)
            helperInstance = new FoodDatabaseHelper(ctx.getApplicationContext());
        return helperInstance;
    }

    /**
     * Add a food item to the database.
     * @param food
     */
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

    /**
     * Retrieve the sum, average, max, and min calories from unique tags in the database.
     * @return an ArrayList of Tags
     */
    public ArrayList<Tag> getTags() {
        ArrayList<Tag> tags = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(true, TABLE_NAME,
                new String[] {KEY_TAG,
                        "SUM(" + KEY_CAL + ") AS TotalCal",
                        "AVG(" + KEY_CAL + ") AS AvgCal",
                        "MAX(" + KEY_CAL + ") AS MaxCal",
                        "MIN(" + KEY_CAL + ") AS MinCal"},
                KEY_TAG + " <> ? ",
                new  String[] {""}, KEY_TAG, null, null, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex(KEY_TAG));
                    double sum = cursor.getDouble(cursor.getColumnIndex("TotalCal"));
                    double average = cursor.getDouble(cursor.getColumnIndex("AvgCal"));
                    double max = cursor.getDouble(cursor.getColumnIndex("MaxCal"));
                    double min = cursor.getDouble(cursor.getColumnIndex("MinCal"));
                    Tag tag = new Tag(name, sum, average, max, min);
                    tags.add(tag);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get tags from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return tags;
    }

    /**
     * Retrieve all food items contained in the database.
     * @return an ArrayList of Food
     */
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
                    food.setTag(cursor.getString(cursor.getColumnIndex(KEY_TAG)));
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

    /**
     * Updates the tag attribute of an entry.
     * @param id
     * @param tag
     */
    public void updateFoodTag(double id, String tag) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TAG, tag);

        db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[] {String.valueOf(id)});
    }

    /**
     * Deletes a entry from the database by searching the id.
     * @param id
     */
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

    /**
     * Deletes an entry from the database by searching the name / brand.
     * @param name
     * @param brand
     */
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

    /**
     * Checks if an entry exists in the database by searching the name / brand.
     * @param name
     * @param brand
     * @return true if entry exists in database.
     */
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

    /**
     * Private constructor
     * @param ctx
     */
    private FoodDatabaseHelper(Context ctx) { super(ctx, DATABASE_NAME, null, VERSION_NUM); }

    /**
     * Create table in a database with the static final specifications
     * @param db
     */
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

    /**
     * Drop and create a new table on upgrade
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.i(ACTIVITY_NAME, "Calling onUpgrade, oldVersion=" + oldVersion + "newVersion=" + newVersion);
    }

}
