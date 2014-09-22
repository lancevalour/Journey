package app.googlemaptestapp;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
	private final static int DATABASE_VERSION = 1;

	private final static String DATABASE_NAME = "placeDatabase.db";

	public static final String TABLE_PLACE = "place";
	public static final String TABLE_JOURNEY = "journey";

	private String TABLE_NAME;


	private final String KEY_ID = "id";
	private final String KEY_LATITUDE = "latitude";
	private final String KEY_LONGITUDE = "longitude";
	private final String KEY_DATE = "date";
	private final String KEY_TITLE = "title";
	private final String KEY_DESCRIPTION = "description";
	private final String KEY_PHOTOPATH = "photoPath";




	public SQLiteHelper(Context context, String tableName){

		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		TABLE_NAME = tableName;

	}



	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_LATITUDE + " TEXT,"
				+ KEY_LONGITUDE + " TEXT," + KEY_DATE + " TEXT," + KEY_TITLE + " TEXT," 
				+ KEY_DESCRIPTION + " TEXT," + KEY_PHOTOPATH + " TEXT" +
				")";
		db.execSQL(CREATE_TABLE);

	}





	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

		// Create tables again
		onCreate(db);
	}
	
	
	
	
	
	
	public void addPlace(Place place) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_LATITUDE, String.valueOf(place.getLatitude())); 
        values.put(KEY_LONGITUDE, String.valueOf(place.getLongitude())); 
        values.put(KEY_DATE, place.getDate());
        values.put(KEY_TITLE, place.getTitle());
        values.put(KEY_DESCRIPTION, place.getDescription());
        values.put(KEY_PHOTOPATH, place.getPhotoPath());
        
        
        
 
        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }
	
	
	public Place getPlace(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
        		KEY_LATITUDE, KEY_LONGITUDE, KEY_DATE, KEY_TITLE, KEY_DESCRIPTION, KEY_PHOTOPATH}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        Place place = new Place(
        		Double.parseDouble(cursor.getString(1)), Double.parseDouble(cursor.getString(2)) ,cursor.getString(3) ,cursor.getString(4), cursor.getString(5), cursor.getString(6));
        // return contact
        return place;
    }
	
	
	public List<Place> getAllPlaces() {
        List<Place> placeList = new ArrayList<Place>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Place place = new Place();
            
            	
            	/*place.setId(Integer.parseInt(cursor.getString(0)));*/
            	place.setLatitude(Double.parseDouble(cursor.getString(1)));
            	place.setLongitude(Double.parseDouble(cursor.getString(2)));
            	place.setDate(cursor.getString(3));
            	place.setTitle(cursor.getString(4));
            	place.setDescription(cursor.getString(5));
            	place.setPhotoPath(cursor.getString(6));
            	
                // Adding contact to list
            	placeList.add(place);
            } while (cursor.moveToNext());
        }
 
        // return contact list
        return placeList;
    }
	
	
	
	public int updatePlace(int id, Place place) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_LATITUDE, String.valueOf(place.getLatitude())); 
        values.put(KEY_LONGITUDE, String.valueOf(place.getLongitude())); 
        values.put(KEY_DATE, place.getDate());
        values.put(KEY_TITLE, place.getTitle());
        values.put(KEY_DESCRIPTION, place.getDescription());
        values.put(KEY_PHOTOPATH, place.getPhotoPath());
 
        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

	
	
	   public void deletePlace(int id) {
	        SQLiteDatabase db = this.getWritableDatabase();
	        db.delete(TABLE_NAME, KEY_ID + " = ?",
	                new String[] { String.valueOf(id)});
	        db.close();
	    }
	
	
	   public int getPlacesCount() {
	        String countQuery = "SELECT  * FROM " + TABLE_NAME;
	        SQLiteDatabase db = this.getReadableDatabase();
	        Cursor cursor = db.rawQuery(countQuery, null);
	        cursor.close();
	 
	        // return count
	        return cursor.getCount();
	    }
	

}
