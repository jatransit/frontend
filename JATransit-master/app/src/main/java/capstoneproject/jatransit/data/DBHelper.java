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
        super(context, DATABASE_NAME, null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ROUTE_TABLE = "CREATE TABLE "+ ROUTES_TABLE_NAME+ "("+ROUTES_TABLE_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+ ROUTE_COLUMN_ROUTE+" TEXT,"+ROUTE_COLUMN_ORIGIN+" TEXT,"+ ROUTE_COLUMN_DESTINATION+" TEXT,"+ROUTE_COLUMN_VIA+" TEXT,"+ROUTE_COLUMN_ROUTETYPE+" TEXT"+")";
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

        contentValues.put(ROUTE_COLUMN_ROUTE,route);
        contentValues.put(ROUTE_COLUMN_ORIGIN, origin);
        contentValues.put(ROUTE_COLUMN_DESTINATION,destination);
        contentValues.put(ROUTE_COLUMN_VIA, via);
        contentValues.put(ROUTE_COLUMN_ROUTETYPE,route_type);

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

    public List<FeedItem> getDataByOrigin(String origin){
        SQLiteDatabase db = this.getReadableDatabase();
        List<FeedItem>  routeList = new ArrayList<FeedItem>();
        String query = " SELECT * FROM routes where origin like '"+ origin +"' ORDER BY route";
        Cursor  cursor = db.rawQuery(query,null);

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

                // Adding route to list
                routeList.add(item);
            } while (cursor.moveToNext());
        }

        // return Route list
        return routeList;
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
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            int numRows = (int) DatabaseUtils.queryNumEntries(db, ROUTES_TABLE_NAME);
            return numRows;
        }catch(Exception e){
            e.printStackTrace();

        }
        return 99999;
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

   /* public List<FeedItem> tripPlannerQuery(String org,String des) {

        // first query for only one bus
        String selectQuery = "SELECT  * FROM " + ROUTES_TABLE_NAME +" WHERE origin LIKE '%"+ org + "%' AND destination LIKE '%" + des+"%'" ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // if one bus doesnt satisfy the query then do another query
        if(cursor.moveToFirst()== false){

            // get data from table separately
          String selectQueryorg1 = "SELECT  * FROM " + ROUTES_TABLE_NAME +" WHERE origin LIKE '%"+ org + "%'" ;
          String selectQuerydes1 = "SELECT  * FROM " + ROUTES_TABLE_NAME +" WHERE destination LIKE '%"+ des + "%'" ;

            SQLiteDatabase dborg1 = this.getWritableDatabase();
            Cursor cursorOrg1 = dborg1.rawQuery(selectQueryorg1, null);
            SQLiteDatabase dbdes1 = this.getWritableDatabase();
            Cursor cursorDes1 = dbdes1.rawQuery(selectQuerydes1, null);
            if(cursorOrg1.moveToFirst() &&cursorDes1.moveToFirst()) {
                do {
                    if(cursorOrg1.getString(3) == cursorDes1.getString(2)) {
                        //String selectQuery1 = "SELECT  * FROM " + ROUTES_TABLE_NAME + " WHERE origin LIKE '%" + org + "%'";
                    }
                } while (cursorOrg1.moveToNext()&& cursorOrg1.moveToNext() );
            }else{

            }
        }

        return null;
    }*/
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

                // Adding route to list
                routeList.add(item);
            } while (cursor.moveToNext());
        }

        // return Route list
        return routeList;
    }


    // Getting All Routes
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
                item.setOrigin("Origin: " +cursor.getString(2));
               // Log.d("origin",""+cursor.getString(2));
                item.setDestination("Destination: " + cursor.getString(3));
                //Log.d("des",""+cursor.getString(3));
                item.setRoute("Route Number: " + cursor.getString(1));
                //Log.d("route",""+cursor.getString(1));
                item.setVia("Via: " + cursor.getString(4));
               // Log.d("via",""+cursor.getString(4));
                item.setRouteType("Route Type: " + cursor.getString(5));
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


    public List<FeedItem> tripPlanner(String org, String des){

        SQLiteDatabase db = this.getWritableDatabase();
        List<FeedItem>  routeList = new ArrayList<FeedItem>();

        //String example = "SELECT * from routes AS rt1 JOIN routes AS rt2 ON rt1.id = rt2.id ";//WHERE rt1.destination = rt2.origin";// AND rt1.origin LIKE '%"+org+"' AND rt2.destination LIKE '%"+des+"'";
        //String example = "SELECT * from routes where routes.origin LIKE '%"+"Three Miles"+"%' AND routes.destination  (SELECT routes.origin FROM routes Where routes.destination LIKE '%"+ "City"+"%')" ;
        //String example ="SELECT * FROM routes AS rt1 JOIN routes AS rt2 ON rt1.destination <> rt2.origin WHERE rt1.origin LIKE '%"+ org +"' AND rt2.destination LIKE '%"+des+"'";// AND rt1.destination = rt2.origin";

        String example2 = "SELECT * FROM routes Where routes.destination LIKE '%"+ des+"%'";
        String example1 = "SELECT * FROM routes Where routes.origin LIKE '%"+ org+"%'";

        String routequery = "SELECT route, origin, destination FROM routes";
        //One bus
        String firstquery1 = "SELECT * FROM routes WHERE origin LIKE '%" +org+"' AND destination LIKE '%" +des+"'";
        String firstquery2 = "SELECT * FROM routes WHERE origin LIKE '%" +des+"' AND destination LIKE '%" +org+"'";

        //Origin from one bus destination from another bus
        String secondquery1 = "SELECT * FROM routes WHERE routes.origin LIKE '%"+org+"%'";
        String thirdquery1 = "SELECT * FROM routes WHERE routes.destination LIKE '%"+ des+"%'";
        String secondquery2 = "SELECT * FROM routes WHERE routes.origin LIKE '%"+des+"%'";
        String thirdquery2 = "SELECT * FROM routes WHERE routes.destination LIKE '%"+ org+"%'";




        // Orgin and destination in the via attribute
        String fourthquery = "SELECT * FROM routes WHERE via LIKE '%"+org+"%'";
        String fifthquery = "SELECT * FROM routes WHERE via LIKE '%"+des+"%'";

        //switch
       // String secondquery = "SELECT route, origin, destination FROM routes WHERE origin ='"+des+"'";
       // String thirdquery = "SELECT route, origin, destination FROM routes WHERE destination ='"+ org+"'";

        // get Data with query strings
        Cursor cursorRoutes = db.rawQuery(routequery,null);
        Cursor cursorFirst1 = db.rawQuery(firstquery1,null);
        Cursor cursorFirst2 = db.rawQuery(firstquery2,null);
        Cursor cursorSecond1 = db.rawQuery(secondquery1,null);
        Cursor cursorThird1 = db.rawQuery(thirdquery1,null);
        Cursor cursorSecond2 = db.rawQuery(secondquery2,null);
        Cursor cursorThird2 = db.rawQuery(thirdquery2,null);
        Cursor cursorFourth = db.rawQuery(fourthquery,null);
        Cursor cursorFifth = db.rawQuery(fifthquery,null);
        Cursor cursorexample1 = db.rawQuery(example1,null);
        Cursor cursorexample2 = db.rawQuery(example2,null);

        // First check if there is a bus that goes from origin to destination
        if (cursorFirst1.getCount()>0 && cursorFirst1 != null) {

            Log.d("Tag","First");
            // looping through all rows and adding to list
            if (cursorFirst1.moveToFirst()) {
                do {

                    FeedItem item = new FeedItem();
                    item.setOrigin("Origin: " + cursorFirst1.getString(2));
                    // Log.d("origin",""+cursor.getString(2));
                    item.setDestination("Destination: " + cursorFirst1.getString(3));
                    //Log.d("des",""+cursor.getString(3));
                    item.setRoute("Route Number: " + cursorFirst1.getString(1));
                    //Log.d("route",""+cursor.getString(1));
                    item.setVia("Via: " + cursorFirst1.getString(4));
                    // Log.d("via",""+cursor.getString(4));
                    item.setRouteType("Route Type: " + cursorFirst1.getString(5));
                    // Log.d("type",""+cursor.getString(5));

                    // Adding contact to list
                    routeList.add(item);
                } while (cursorFirst1.moveToNext());

                return routeList;
            }

        }else if(cursorFirst2.getCount()>0 && cursorFirst2 != null){
            // looping through all rows and adding to list
            Log.d("Tag","Second");
            if (cursorFirst2.moveToFirst()) {
                do {

                    FeedItem item = new FeedItem();
                    item.setOrigin("Origin: " + cursorFirst2.getString(3));
                    // Log.d("origin",""+cursor.getString(2));
                    item.setDestination("Destination: " + cursorFirst2.getString(2));
                    //Log.d("des",""+cursor.getString(3));
                    item.setRoute("Route Number: " + cursorFirst2.getString(1));
                    //Log.d("route",""+cursor.getString(1));
                    item.setVia("Via: " + cursorFirst2.getString(4));
                    // Log.d("via",""+cursor.getString(4));
                    item.setRouteType("Route Type: " + cursorFirst2.getString(5));
                    // Log.d("type",""+cursor.getString(5));

                    // Adding contact to list
                    routeList.add(item);
                } while (cursorFirst2.moveToNext());
                return routeList;
            }


        }else if(cursorSecond1.getCount() >0 && cursorSecond2.getCount()>0){// if origin correct origin =destination
            // looping through all rows and adding to list
            Log.d("Tag","origin = destination");
            if (cursorSecond1.moveToFirst() && cursorSecond2.moveToFirst()) {

                boolean end =true;
                FeedItem item = new FeedItem();
                FeedItem item1 = new FeedItem();
                do {
                    try {

                        item.setOrigin("Origin: " + cursorSecond1.getString(2));
                        item1.setOrigin("Origin: " + cursorSecond2.getString(2));
                        // Log.d("origin",""+cursor.getString(2));
                        item.setDestination("Destination: " + cursorSecond1.getString(3));
                        item1.setDestination("Destination: " + cursorSecond2.getString(3));
                        //Log.d("des",""+cursor.getString(3));
                        item.setRoute("Route Number: " + cursorSecond1.getString(1));
                        item1.setRoute("Route Number: " + cursorSecond2.getString(1));
                        //Log.d("route",""+cursor.getString(1));
                        item.setVia("Via: " + cursorSecond1.getString(4));
                        item1.setVia("Via: " + cursorSecond2.getString(4));
                        // Log.d("via",""+cursor.getString(4));
                        item.setRouteType("Route Type: " + cursorSecond1.getString(5));
                        item1.setRouteType("Route Type: " + cursorSecond2.getString(5));
                        // Log.d("type",""+cursor.getString(5));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    // Adding contact to list
                    //routeList.add(item);

                    if (!cursorSecond2.moveToNext()) {
                        cursorSecond2.moveToFirst();
                        end = cursorSecond1.moveToNext();

                    }

                    try {
                        if (cursorSecond1.getString(3).equals(cursorSecond2.getString(3))) {
                            routeList.add(0, item);
                            routeList.add(0, item1);

                            end = false;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                } while (end);
                return routeList;
            }


        }else if(cursorThird2.getCount()>0 && cursorThird1.getCount()>0){ //destination =origin and destination correct

            // looping through all rows and adding to list
            Log.d("Tag"," destination=origin ");
            if (cursorThird2.moveToFirst() && cursorThird1.moveToFirst()) {

                boolean end =true;
                FeedItem item = new FeedItem();
                FeedItem item1 = new FeedItem();
                do {
                    try {

                        item.setOrigin("Origin: " + cursorThird2.getString(2));
                        item1.setOrigin("Origin: " + cursorThird1.getString(2));
                        // Log.d("origin",""+cursor.getString(2));
                        item.setDestination("Destination: " + cursorThird2.getString(3));
                        item1.setDestination("Destination: " + cursorThird1.getString(3));
                        //Log.d("des",""+cursor.getString(3));
                        item.setRoute("Route Number: " + cursorThird2.getString(1));
                        item1.setRoute("Route Number: " + cursorThird1.getString(1));
                        //Log.d("route",""+cursor.getString(1));
                        item.setVia("Via: " + cursorThird2.getString(4));
                        item1.setVia("Via: " + cursorThird1.getString(4));
                        // Log.d("via",""+cursor.getString(4));
                        item.setRouteType("Route Type: " + cursorThird2.getString(5));
                        item1.setRouteType("Route Type: " + cursorThird1.getString(5));
                        // Log.d("type",""+cursor.getString(5));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    // Adding contact to list
                    //routeList.add(item);

                    if (!cursorThird1.moveToNext()) {
                        cursorSecond2.moveToFirst();
                        end = cursorThird2.moveToNext();

                    }
                    try {
                        if (cursorThird2.getString(3).equals(cursorThird1.getString(3))) {
                            routeList.add(0, item);
                            routeList.add(0, item1);

                            end = false;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                } while (end);
                return routeList;
            }

        }

        else if (cursorexample1.getCount()>0 && cursorexample2.getCount()>0){//two buses if origin and destination correct

            // looping through all rows and adding to list
            Log.d("Tag","two buses if origin and destination correct");
            if (cursorexample1.moveToFirst() && cursorexample2.moveToFirst()) {

                boolean end =true;
                FeedItem item = new FeedItem();
                FeedItem item1 = new FeedItem();
                do {
                try {

                    item.setOrigin("Origin: " + cursorexample1.getString(2));
                    item1.setOrigin("Origin: " + cursorexample2.getString(2));
                    // Log.d("origin",""+cursor.getString(2));
                    item.setDestination("Destination: " + cursorexample1.getString(3));
                    item1.setDestination("Destination: " + cursorexample2.getString(3));
                    //Log.d("des",""+cursor.getString(3));
                    item.setRoute("Route Number: " + cursorexample1.getString(1));
                    item1.setRoute("Route Number: " + cursorexample2.getString(1));
                    //Log.d("route",""+cursor.getString(1));
                    item.setVia("Via: " + cursorexample1.getString(4));
                    item1.setVia("Via: " + cursorexample2.getString(4));
                    // Log.d("via",""+cursor.getString(4));
                    item.setRouteType("Route Type: " + cursorexample1.getString(5));
                    item1.setRouteType("Route Type: " + cursorexample2.getString(5));
                    // Log.d("type",""+cursor.getString(5));
                }catch (Exception e){
                    e.printStackTrace();
                }
                    // Adding contact to list
                    //routeList.add(item);


                    if (!cursorexample2.moveToNext()) {
                        cursorexample2.moveToFirst();
                        end = cursorexample1.moveToNext();

                    }
                    try {
                        if (cursorexample1.getString(3).equals(cursorexample2.getString(2)) || cursorexample1.getString(2).equals(cursorexample2.getString(2))|| cursorexample1.getString(3).equals(cursorexample2.getString(3))|| cursorexample1.getString(2).equals(cursorexample2.getString(3))) {
                            routeList.add(0, item);
                            routeList.add(0, item1);

                            end = false;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                } while (end);
                return routeList;
            }
        }else if(cursorFourth.getCount()>0 && cursorFifth.getCount()>0 ){// via and via
            // looping through all rows and adding to list
            Log.d("Tag"," via via ");
            if (cursorFourth.moveToFirst() && cursorFifth.moveToFirst()) {

                boolean end =true;
                FeedItem item = new FeedItem();
                FeedItem item1 = new FeedItem();
                do {
                    try {

                        item.setOrigin("Origin: " + cursorFourth.getString(2));
                        item1.setOrigin("Origin: " + cursorFifth.getString(2));
                        // Log.d("origin",""+cursor.getString(2));
                        item.setDestination("Destination: " + cursorFourth.getString(3));
                        item1.setDestination("Destination: " + cursorFifth.getString(3));
                        //Log.d("des",""+cursor.getString(3));
                        item.setRoute("Route Number: " + cursorFourth.getString(1));
                        item1.setRoute("Route Number: " + cursorFifth.getString(1));
                        //Log.d("route",""+cursor.getString(1));
                        item.setVia("Via: " + cursorFourth.getString(4));
                        item1.setVia("Via: " + cursorFifth.getString(4));
                        // Log.d("via",""+cursor.getString(4));
                        item.setRouteType("Route Type: " + cursorFourth.getString(5));
                        item1.setRouteType("Route Type: " + cursorFifth.getString(5));
                        // Log.d("type",""+cursor.getString(5));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    // Adding contact to list
                    //routeList.add(item);

                    try {
                        if (cursorFourth.getString(3).equals(cursorFifth.getString(2))||cursorFourth.getString(2).equals(cursorFifth.getString(3)) ) {
                            routeList.add(0, item);
                            routeList.add(0, item1);

                            end = false;
                        }

                    if (!cursorFifth.moveToNext()) {
                        cursorFifth.moveToFirst();
                        end = cursorFourth.moveToNext();

                    }


                    }catch (Exception e){
                        e.printStackTrace();
                    }

                } while (end);
                return routeList;
            }

        }else if(cursorSecond1.getCount()>0 && cursorFifth.getCount()>0){//origin and via
            // looping through all rows and adding to list
            Log.d("Tag"," origin to via ");
            if (cursorSecond1.moveToFirst() && cursorFifth.moveToFirst()) {

                boolean end =true;
                FeedItem item = new FeedItem();
                FeedItem item1 = new FeedItem();
                do {
                    try {

                        item.setOrigin("Origin: " + cursorSecond1.getString(2));
                        item1.setOrigin("Origin: " + cursorFifth.getString(2));
                        // Log.d("origin",""+cursor.getString(2));
                        item.setDestination("Destination: " + cursorSecond1.getString(3));
                        item1.setDestination("Destination: " + cursorFifth.getString(3));
                        //Log.d("des",""+cursor.getString(3));
                        item.setRoute("Route Number: " + cursorSecond1.getString(1));
                        item1.setRoute("Route Number: " + cursorFifth.getString(1));
                        //Log.d("route",""+cursor.getString(1));
                        item.setVia("Via: " + cursorSecond1.getString(4));
                        item1.setVia("Via: " + cursorFifth.getString(4));
                        // Log.d("via",""+cursor.getString(4));
                        item.setRouteType("Route Type: " + cursorSecond1.getString(5));
                        item1.setRouteType("Route Type: " + cursorFifth.getString(5));
                        // Log.d("type",""+cursor.getString(5));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    // Adding contact to list
                    //routeList.add(item);

                    if (!cursorFifth.moveToNext()) {
                        cursorFifth.moveToFirst();
                        end = cursorSecond1.moveToNext();

                    }

                    try {
                        if (cursorSecond1.getString(3).equals(cursorFifth.getString(3)) || cursorSecond1.getString(2).equals(cursorFifth.getString(2))) {
                            routeList.add(0, item);
                            routeList.add(0, item1);

                            end = false;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                } while (end);
                return routeList;
            }

        }else if(cursorFourth.getCount()>0 && cursorThird1.getCount()>0 ){ //via and destination



        }else if(cursorFourth.getCount()>0 && cursorSecond2.getCount()>0){// via and des=origin

        }else if(cursorFifth.getCount()>0 && cursorThird2.getCount()>0){//via and origin=des

        }
      else{
            Log.d("results: ","No results");
        }
        return routeList;
    }
}
