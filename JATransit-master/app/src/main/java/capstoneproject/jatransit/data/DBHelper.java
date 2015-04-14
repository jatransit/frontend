package capstoneproject.jatransit.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Caliph Cole on 04/10/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "routes.db";

    public static final String ROUTES_TABLE_NAME = "routes";
    public static final String ROUTES_TABLE_ID = "id";
    public static final String ROUTE_COLUMN_ROUTE = "route";
    public static final String ROUTE_COLUMN_ORIGIN ="origin";
    public static final String ROUTE_COLUMN_DESTINATION ="destination";
    public static final String ROUTE_COLUMN_VIA = "via";
    public static final String ROUTE_COLUMN_ROUTETYPE ="route_type";

    private HashMap hp;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ROUTE_TABLE = "CREATE TABLE "+ ROUTES_TABLE_NAME+ "("+ROUTES_TABLE_ID +" INTEGER PRIMARY KEY,"+ ROUTE_COLUMN_ROUTE+" TEXT,"+ROUTE_COLUMN_ORIGIN+" TEXT,"+ ROUTE_COLUMN_DESTINATION+" TEXT,"+ROUTE_COLUMN_VIA+" TEXT,"+ROUTE_COLUMN_ROUTETYPE+" TEXT"+")";
        db.execSQL(CREATE_ROUTE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + ROUTES_TABLE_NAME);

        //create table again
        onCreate(db);
    }

    public boolean insertRoute(String route, String origin, String destination, String via,String route_type)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ROUTE_COLUMN_ROUTE, "Route Number: " + route);
        contentValues.put(ROUTE_COLUMN_ORIGIN, "Origin: " + origin);
        contentValues.put(ROUTE_COLUMN_DESTINATION, "Destination: " + destination);
        contentValues.put(ROUTE_COLUMN_VIA,"Via: " + via);
        contentValues.put(ROUTE_COLUMN_ROUTETYPE,"Route Type: " + route_type);

        db.insert(ROUTES_TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public List<FeedItem> getDataByRoute(String route){
        SQLiteDatabase db = this.getReadableDatabase();
       Cursor res =  db.rawQuery( "SELECT * FROM routes WHERE route LIKE '%"+route+"%'", null );


        List<FeedItem> routeQuery = new ArrayList<FeedItem>();
        if(res.moveToFirst()){
            do{
               FeedItem item = new FeedItem();
                item.setRoute( res.getString(1));
                item.setOrigin( res.getString(2));
                item.setDestination( res.getString(3));
                item.setVia(res.getString(4));
                item.setRouteType(res.getString(5));

                routeQuery.add(item);
                Log.d("routes", "id: " + res.getString(0)+" Route: "+ res.getString(1) + " Origin: " + res.getString(2));
            }while(res.moveToNext());
        }

        return routeQuery;
    }

    public Cursor getDataByOriginAndDestination(String origin, String destination){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = " SELECT * FROM routes where origin like '"+ origin + "' AND destination like '"+ destination+"' ORDER BY route";
        Cursor  res = db.rawQuery(query,null);


        return res;
    }

    public Cursor getDataByVia(String via){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = " SELECT * FROM routes where via like '"+ via + "' ORDER BY route";
        Cursor  res = db.rawQuery(query,null);

        return res;
    }

    public Cursor getDataByOrigin(String origin){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = " SELECT * FROM routes where origin like '"+ origin +"' ORDER BY route";
        Cursor  res = db.rawQuery(query,null);

        return res;
    }

    //Mainly for the trip planner
    public Cursor getDataByOriginAndDestination( String destination){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = " SELECT * FROM routes where destination like '"+ destination+"' ORDER BY route";
        Cursor  res = db.rawQuery(query,null);

        return res;
    }
    public Cursor getDataByType(String type){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = " SELECT * FROM routes where route_type like '"+ type + "' ORDER BY route";
        Cursor  res = db.rawQuery(query,null);

        return res;
    }


    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, ROUTES_TABLE_NAME);
        return numRows;
    }

    public boolean updateRoute (Integer id,String route, String origin, String destination, String via, String route_type)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ROUTE_COLUMN_ROUTE, route);
        contentValues.put(ROUTE_COLUMN_ORIGIN, origin);
        contentValues.put(ROUTE_COLUMN_DESTINATION, destination);
        contentValues.put(ROUTE_COLUMN_VIA, via);
        contentValues.put(ROUTE_COLUMN_ROUTETYPE, route_type);
        db.update("routes", contentValues, "id = ? ", new String[] { Integer.toString(id) } );

        return true;
    }

    public List<FeedItem> getAllRoutesByQuery(String s ){
        List<FeedItem>  routeList = new ArrayList<FeedItem>();

        String org = "";
        String des = "";
        try{
            org = s.split(" to ")[0];
            des = s.split(" to ")[1];

        }catch (Exception e){

            e.printStackTrace();
        }


        // Select All Query
        String selectQuery = "SELECT  * FROM " + ROUTES_TABLE_NAME +" WHERE (origin LIKE '%"+ org + "%' AND destination LIKE '%" + des+"%') OR route LIKE '%" +s +"%'" ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                FeedItem item = new FeedItem();
                item.setOrigin(cursor.getString(2));
                // Log.d("origin",""+cursor.getString(2));
                item.setDestination(cursor.getString(3));
                //Log.d("des",""+cursor.getString(3));
                item.setRoute(cursor.getString(1));
                //Log.d("route",""+cursor.getString(1));
                item.setVia(cursor.getString(4));
                // Log.d("via",""+cursor.getString(4));
                item.setRouteType(cursor.getString(5));
                // Log.d("type",""+cursor.getString(5));

                // Adding contact to list
                routeList.add(item);
            } while (cursor.moveToNext());
        }

        // return contact list
        return routeList;
    }


    // Getting All Contacts
    public List<FeedItem> getAllRoutes() {
        List<FeedItem>  routeList = new ArrayList<FeedItem>();


        // Select All Query
        String selectQuery = "SELECT  * FROM " + ROUTES_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                FeedItem item = new FeedItem();
                item.setOrigin(cursor.getString(2));
               // Log.d("origin",""+cursor.getString(2));
                item.setDestination(cursor.getString(3));
                //Log.d("des",""+cursor.getString(3));
                item.setRoute(cursor.getString(1));
                //Log.d("route",""+cursor.getString(1));
                item.setVia(cursor.getString(4));
               // Log.d("via",""+cursor.getString(4));
                item.setRouteType(cursor.getString(5));
                // Log.d("type",""+cursor.getString(5));

                // Adding contact to list
                routeList.add(item);
            } while (cursor.moveToNext());
        }

        // return contact list
        return routeList;
    }


    public boolean checkForTables(){
        boolean hasTables = false;
       SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +ROUTES_TABLE_NAME, null);

        if(cursor != null && cursor.getCount() > 0){
            hasTables=true;
            cursor.close();
        }

        return hasTables;
    }
}
