package group.finalproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * KEY_ID - name of the ID column
 * KEY_STOP - name of the Stop number column
 * TABLE_NAME - name of the database table
 */
import static group.finalproject.BusActivity.ChatDatabaseHelper.KEY_ID;
import static group.finalproject.BusActivity.ChatDatabaseHelper.KEY_STOP;
import static group.finalproject.BusActivity.ChatDatabaseHelper.TABLE_NAME;

/**
 * The OC-Transpo Bus app implements code to allow the user to input a 4 digit stop number
 * select available route numbers for that stop and view information about the next bus trip
 * to start along that route.
 *
 * @author  Chase Langford
 * @version 1.0
 * @since   2018-12-02
 */

public class BusActivity extends AppCompatActivity{
    /**
     * ACTIVITY_NAME - String constant used mainly to reference the activity name in log outputs
     */
    protected static final String ACTIVITY_NAME="BusActivity";
    /**
     * stop - listview containing all the stop numbers for the user to select from
     * bus - listview containing all the bus numbers for the user to select from
     * destination - the destination of the selected bus route
     * coordinates - the latitude and longitude of the selected bus route
     * speed - the GPS speed of the bus currently
     * startTime - the time the selected bus routes began its trip
     * adjustedTime - how late the bus is
     * progress - progressBar that updates as the information is populated
     */
    ListView stop;
    ListView bus;
    TextView destination;
    TextView coordinates;
    TextView speed;
    TextView startTime;
    TextView adjustedTime;
    ProgressBar progress;
    /**
     * stopNumber - Currently selected stop
     * busNumber - Currently selected route
     */
    String stopNumber;
    String busNumber;
    /**
     * stopNumbers - arraylist of stop numbers
     * busNumbers - arraylist of available bus numbers
     */
    ArrayList<String> stopNumbers = new ArrayList<>();
    ArrayList<String> busNumbers = new ArrayList<>();
    /**
     * urlRouteSummary - base url to query for the route numbers at a selected stop number
     * urlNextTrips - base url to query for the data of a particular route and stop
     * urlQuery - constructed url to pass to OCTranspo to query for data
     * queryFlag - flag to indicate whether to make a nextTrips or routesSummary query
     */
    String urlRouteSummary;
    String urlNextTrips;
    String urlToQuery;
    String queryFlag;
    /**
     * dbHelper - chatDatabaseHelper to facilitate in operating the database
     * messageAdapter - ChatAdapter to facilitate in accessing and modifying the stored information of stop numbers in the database
     * adptr - ArrayAdapter<String> to facilitate in updating the list of bus numbers
     * db - SQLiteDatabase which is used to store the users entered stop numbers
     * cursor - Cursor used to access the information in the db database
     */
    ChatDatabaseHelper dbHelper;
    ChatAdapter messageAdapter;
    ArrayAdapter<String> adptr;
    SQLiteDatabase db;
    Cursor cursor;
    /**
     * prefs - SharedPreferences used to store information about the average length of bus delay locally
     * totalAdj - String that contains the running total of bus delay
     * countAdj - String that contains the running count of bus delays in average
     * avgAdj - Double that is used to store the currently calculated average adjusted time
     */
    SharedPreferences prefs;
    String totalAdj;
    String countAdj;
    double avgAdj=0;

    /**
     * onCreate - initialize the interface, and prepare all listviews, toolbars and buttons for use
     * @param savedInstanceState - passes stored information
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        /**
         * attach menu to toolbar
         */
        Toolbar bus_toolbar = (Toolbar)findViewById(R.id.bus_toolbar);
        setSupportActionBar(bus_toolbar);
        /**
         * load the necessary information from sharedPreferences for calculation of average adjusted
         * time, and for store the same between sessions
         */
        prefs = getSharedPreferences("OCBusSaveFile", Context.MODE_PRIVATE);
        totalAdj= prefs.getString("Total","0");
        countAdj = prefs.getString("Count","0");
        if(Integer.parseInt(countAdj)!=0){
            avgAdj = Double.parseDouble(totalAdj)/Double.parseDouble(countAdj);
            avgAdj = Math.floor(avgAdj * 100) / 100;
        }
        /**
         * initialize base urls for use in the program
         */
        urlToQuery =queryFlag = new String();
        urlRouteSummary = new String("https://api.octranspo1.com/v1.2/GetRouteSummaryForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo=");
        urlNextTrips = new String("https://api.octranspo1.com/v1.2/GetNextTripsForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo=");
        /**
         * attach destination, coordinates, spped, startTime and adjustedTime to their textviews
         */
        destination = findViewById(R.id.textDestination);
        coordinates = findViewById(R.id.textCoordinates);
        speed = findViewById(R.id.textSpeedGPS);
        startTime = findViewById(R.id.textTimeStart);
        adjustedTime = findViewById(R.id.textAdjustedtime);
        /**
         * progress - progress bar
         */
        progress = findViewById(R.id.progressBarBus);
        progress.setVisibility(View.VISIBLE);
        /**
         * stop - listview of stop numbers
         * bus - listview of bus numbers
         */
        stop = findViewById(R.id.listStopNumber);
        bus = findViewById(R.id.listBusNumber);
        /**
         * adptr - establish an arrayadapter so that busNumbers arrayList can be updated throughout app execution
         */
        adptr = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,busNumbers);
        bus.setAdapter(adptr);

        /**
         * create a button handle for adjust time average button
         */
        Button adjAvg = findViewById(R.id.snackBttn);

        /**
         * prepare the database and stop arraylist to load the numbers stored in the database into
         * the arraylist for initial access in the program
         */
        dbHelper = new ChatDatabaseHelper(this);
        messageAdapter = new ChatAdapter(this);
        db = dbHelper.getWritableDatabase();
        stop.setAdapter(messageAdapter);
        cursor = db.query(false,TABLE_NAME, new String[]{KEY_ID, KEY_STOP}, null, null, null, null, null, null);
        cursor.moveToFirst();
        /**
         * load the stopNumbers array list with the stop numbers stored in the database
         */
        while (!cursor.isAfterLast()) {
            String nuStop = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_STOP));
            stopNumbers.add(nuStop);
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + nuStop);
            cursor.moveToNext();
        }
        messageAdapter.notifyDataSetChanged();

        /**
         * Displays the average of the adjusted time responses in a toast for the users edification
         */
        adjAvg.setOnClickListener(click->{
            //call a toast, display the adjusted time average
            CharSequence text = "The Average Delay time is: " + avgAdj;
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(this,text,duration);
            toast.show();
        });

        /**
         * When a stop number is selected, website is polled for information and interface is updated
         * with the bus numbers that arrive at that stop
         */
        stop.setOnItemClickListener((adapterView, view, position, id)->{
            if(stopNumber!=(stop.getItemAtPosition(position).toString())){
                stopNumber = stop.getItemAtPosition(position).toString();
                busNumbers.clear();
                adptr.notifyDataSetChanged();
                //populate bus numbers at this stop
                urlToQuery = urlRouteSummary + stopNumber;
                queryFlag = "stop";
                Log.i(ACTIVITY_NAME, "Route selected: " + stopNumber);
                ScheduleQuery schedule = new ScheduleQuery();
                schedule.execute();
                Log.i(ACTIVITY_NAME, "list updated");
            }
        });

        /**
         * When a bus route number is selected, website is polled for information and interface is
         * populated with results: destination, speed, start time, coordinates and adjusted time
         */
        bus.setOnItemClickListener((adapterView,view,position,id)->{
            if(busNumber!=(bus.getItemAtPosition(position).toString())) {
                busNumber = bus.getItemAtPosition(position).toString();
                //poll for route information for this bus
                urlToQuery = urlNextTrips + stopNumber + "&routeNo=" + busNumber;
                queryFlag = "bus";
                Log.i(ACTIVITY_NAME, "Route selected: " + stopNumber + " " + busNumber);
                ScheduleQuery schedule = new ScheduleQuery();
                schedule.execute();
            }
        });

    }

    /**
     * ScheduleQuery handles Asychronous tasks in the application
     * in this case, mainly the querying of the OCTranspo website
     */
    private class ScheduleQuery extends AsyncTask<String, Integer, String> {

        /**
         * doInBackground - handles tasks in background so that interface of app remains functional, even
         * as processes are run
         * @param strings - arbitrary number of string inputs, not used
         * @return null
         */
        @Override
        protected String doInBackground(String... strings) {
            Log.i(ACTIVITY_NAME,"Start of doInBackground");
            URL url;

            try{/**
                * Initiate a connection to the OCTranspo website
                */
                url = new URL(urlToQuery);
                Log.i(ACTIVITY_NAME,"Succeeded in accessing website");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                Log.i(ACTIVITY_NAME,"Succeeded in establishing connection");
                this.publishProgress(25);
                /**
                 * create a parser to handle OCTranspo website reply
                 */
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser parser = factory.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(conn.getInputStream(),"UTF-8");
                this.publishProgress(35);

                /**
                 * name - string used to hold the parser tag name for comparison
                 * readFlag - used to ensure that only the first stop information is read in "bus" case
                 * tempString - used to store strings while parsing the XML in the "bus" case
                 */
                String name;
                boolean readFlag =false;
                String tempString = null;

                    switch(queryFlag) {
                        /**
                         *query OCTranspo for a specific stop number
                         * Parse the XML response for the Route Numbers which visit said stop and
                         * store them in the busNumbers ListArray
                         */
                        case "stop":
                            this.publishProgress(55);
                            while(parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                                Log.i(ACTIVITY_NAME, "Begin parsing XML");
                                switch (parser.getEventType()) {
                                    case XmlPullParser.START_TAG:
                                        name = parser.getName();
                                        String tempNum;
                                        if (name.equalsIgnoreCase("RouteNo")) {
                                            parser.next();
                                            tempNum = parser.getText();
                                            if (!busNumbers.contains(tempNum) && !tempNum.equals(null) && !tempNum.equals("")) {
                                                busNumbers.add(tempNum);
                                                Log.i(ACTIVITY_NAME, "New route " + tempNum);
                                            }
                                        }
                                    case XmlPullParser.TEXT:
                                        break;
                                }
                                parser.next();
                            }
                            this.publishProgress(75);
                        break;
                        /**
                         * query OCTranspo for specific bus route and stop information.  Parses the
                         * XML response for desired data.  Destination, Start Time, Adjusted Time,
                         * Latitude, Longitude, and GPS Speed
                         */
                        case "bus":
                            while(parser.getEventType() != XmlPullParser.END_DOCUMENT && !readFlag) {
                                Log.i(ACTIVITY_NAME, "Begin parsing XML");
                                this.publishProgress(90);
                                switch (parser.getEventType()) {
                                    case XmlPullParser.START_TAG:
                                        name = parser.getName();
                                        if (name.equalsIgnoreCase("TripDestination")) {
                                            parser.next();
                                            tempString = parser.getText();
                                            if (!tempString.equals(null) && !tempString.equals("")) {
                                                destination.setText(tempString);
                                            }
                                        } else if (name.equalsIgnoreCase("TripStartTime")) {
                                            parser.next();
                                            tempString = parser.getText();
                                            if (!tempString.equals(null) && !tempString.equals("")) {
                                                startTime.setText(tempString);
                                            }
                                        } else if (name.equalsIgnoreCase("AdjustedScheduleTime")) {
                                            parser.next();
                                            tempString = parser.getText();
                                            if (tempString != null && tempString != "") {
                                                adjustedTime.setText(tempString + " minutes late");
                                                SharedPreferences.Editor edit = prefs.edit();
                                                totalAdj = Integer.toString(Integer.parseInt(totalAdj) + Integer.parseInt(tempString));
                                                countAdj = Integer.toString(Integer.parseInt(countAdj)+1);
                                                edit.putString("Total",totalAdj );
                                                edit.putString("Count",countAdj);
                                                edit.commit();//write to disk
                                                if(Integer.parseInt(countAdj)!=0){
                                                    avgAdj = Double.parseDouble(totalAdj)/Double.parseDouble(countAdj);
                                                    avgAdj = Math.floor(avgAdj * 100) / 100;
                                                }
                                            }
                                        }  else if (name.equalsIgnoreCase("Latitude")) {
                                            parser.next();
                                            tempString = parser.getText() + " / ";
                                        } else if (name.equalsIgnoreCase("Longitude")) {
                                            parser.next();
                                            tempString += parser.getText();
                                            coordinates.setText(tempString);
                                        }else if (name.equalsIgnoreCase("GPSSpeed")) {
                                            parser.next();
                                            tempString = parser.getText();
                                            if (!tempString.equals(null) && !tempString.equals("")) {
                                                speed.setText(tempString + " km/hr");
                                                readFlag=true;
                                                this.publishProgress(100);
                                            }
                                        }
                                    case XmlPullParser.TEXT:
                                        break;
                                }
                                parser.next();
                            }
                        break;
                }
            }catch(Exception e){Log.i(ACTIVITY_NAME,"Exception!");}
            Log.i(ACTIVITY_NAME,"End of doInBackground");
            this.publishProgress(50);
            /**
             * necessary to update the bus numbers listview without exceptions triggered
             */
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adptr.notifyDataSetChanged();
                }
            });
            return null;
        }

        /**
         * onProgressUpdate - used to update the status of the progressbar as the app accesses data
         * @param value - the % value that the progress bar is set to.
         */
        protected void onProgressUpdate(Integer  value){
            progress.setProgress(value);
            progress.setVisibility(View.VISIBLE);
        }
    }

    /**
     * deleteMessage - removes a stop number from the database before updating the listview
     * @param stopNum - the number to be removed from the database
     */
    public void deleteMessage(String stopNum){
        stopNumbers.clear();
        db.delete(dbHelper.TABLE_NAME,dbHelper.KEY_STOP + " = " + stopNum, null);
        cursor = db.query(false,TABLE_NAME, new String[]{KEY_ID, KEY_STOP}, KEY_STOP + " not null", null, null, null, null, null);
        Log.i(ACTIVITY_NAME, "Cursor's column count=" + cursor.getColumnCount());
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Log.i(ACTIVITY_NAME, "Column Names: " + cursor.getColumnName(i));
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            final String msg = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_STOP));
            stopNumbers.add(msg);
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + msg);
            cursor.moveToNext();
        }
        messageAdapter.notifyDataSetChanged();
    }

    /**
     * ChatAdapter - facilitates accessing loading and modifying the stop numbers database
     */
    private class ChatAdapter extends ArrayAdapter<String> {
        /**
         * ChatAdapter constructor
         * @param ctx - current context
         */
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        /**
         * getItemId - returns the database id of the selected item
         * @param position - position in the database
         * @return - id of the item at position value
         */
        public long getItemId(int position){
            cursor = db.query(false,TABLE_NAME, new String[]{KEY_ID, KEY_STOP}, null, null, null, null, null, null);
            cursor.moveToPosition(position);
            return cursor.getLong(cursor.getColumnIndex(ChatDatabaseHelper.KEY_ID));
        }

        /**
         * getCount
         * @return size of the stopNumbers listArray
         */
        public int getCount() {
            return stopNumbers.size();
        }

        /**
         * getItem - returns stop number at specific position
         * @param position - selected list item
         * @return String of stop number selected
         */
        public String getItem(int position) {
            return stopNumbers.get(position);
        }

        /**
         * getID - returns id of selected list item
         * @param position - selected list item
         * @return long - the ID of the selected item
         */
        public long getId(int position){return position;}

        /**
         * getView - prepares the stoplist to be loaded into the listview
         * @param position - where to set the text
         * @param convertView - unused
         * @param parent -unused
         * @return inflated stop_list layout
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = BusActivity.this.getLayoutInflater();

            View result = inflater.inflate(R.layout.stop_list, null);

            TextView message = (TextView) result.findViewById(R.id.stop_number);
            message.setText(getItem(position));
            return result;
        }
    }

    /**
     * ChatDatabaseHelper - creates a handle for more easily building a database
     */
    class ChatDatabaseHelper extends SQLiteOpenHelper {
        /**
         * KEY_ID - integers - key applied to database entries
         * KEY_STOP - BusStop - column name for the stop numbers
         * DATABASE_NAME - Stops.db - the name of the database
         * VERSION_NUM - 1
         * TABLE_NAME - BusStops - the name of the table in the database
         */
        public final static String KEY_ID = "Integers";
        public final static String KEY_STOP = "BusStop";
        public final static String DATABASE_NAME = "Stops.db";
        public final static int VERSION_NUM = 1;
        public final static String TABLE_NAME = "BusStops";

        /**
         * ChatDatabaseHelper - constructor
         * @param ctx - the context in use
         */
        public ChatDatabaseHelper(Context ctx) {
            super(ctx, DATABASE_NAME, null, VERSION_NUM);
        }

        /**
         * onCreate - create database: build table, columns, keys
         * @param db - database input to be instantiated
         */
        public void onCreate(SQLiteDatabase db) {
            Log.i("ChatDatabaseHelper", "Calling onCreate");
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + KEY_ID + " integer primary key autoincrement, " + KEY_STOP + " string not null);");
        }

        /**
         * onUpgrade - used when the database has been upgraded
         * @param db - database to be upgraded
         * @param oldVersion - old version number
         * @param newVersion - new version number
         */
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion = " + newVersion);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    /**
     * onCreateOptionsMenu - sets the menu into the toolbar
     * @param m - the location of where to place the toolbar menu
     * @return true if successful
     */
    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.bus_toolbar_menu,m);
        return true;
    }

    /**
     * onOptionsItemSelected - controls what happens when the various members of the menu are selected(clicked)
     * @param mi - the selected MenuItem of the toolbar
     * @return true as long as butons function properly
     */
    public boolean onOptionsItemSelected(MenuItem mi){
        /**
         * id - the id of the selected menuitem on the toolbar
         */
        int id;
        id = mi.getItemId();

        switch (id){
            /**
             * Toolbar item - Button used to add a new stop to the list of stops.  Prompts the user
             * with a custom dialog to obtain a new stop number and adds it to the list, as well as
             * the database of stop numbers.   Finally a query for the buses that pass the stop is sent
             * to OCTranspo website for the newly entered stop number, and the list of buses populated
             */
            case R.id.newStopButton:
                /**
                 * build the dialog
                 */
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                View alertView = inflater.inflate(R.layout.new_stop_layout,null);
                builder2.setView(alertView);

                EditText newMessageText = (EditText)alertView.findViewById(R.id.newMessageText);
                builder2.setTitle("New Stop");
                builder2.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                    /**
                     * update the list of stop numbers, add new number to database and query OCTranspo
                     * for bus numbers
                     */
                    public void onClick(DialogInterface dialog,int id){
                        stopNumber=newMessageText.getText().toString();
                        if(!stopNumbers.contains(stopNumber)) {
                            stopNumbers.add(stopNumber);
                            ContentValues cv = new ContentValues();
                            cv.put(KEY_STOP, stopNumber);
                            db.insert(TABLE_NAME, "", cv);
                            Log.i("Alert", "new message recorded");

                            //populate the bus numbers list for the newly entered route
                            urlToQuery = urlRouteSummary + stopNumber;
                            queryFlag = "stop";
                            ScheduleQuery schedule = new ScheduleQuery();
                            schedule.execute();
                        }
                    }
                });
                builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){}
                });
                AlertDialog dialog2 = builder2.create();
                dialog2.show();
                break;
            /**
             * Toolbar item - Button used to remove the currently selected bus stop from the list of bus
             * stops.  Prompts the user to verify deletion of the current stop via a snackbar, defaults to
             * not deleting the stop to help prevent unwanted deletions
             */
            case R.id.remStopButton:
                //call a snackbar, confirm that user wants to remove last selected stop
                Snackbar snacky = Snackbar.make(findViewById(R.id.mainLayout),"Delete Current Bus Stop?", Snackbar.LENGTH_SHORT);
                snacky.setAction("OK", (e)-> {
                    deleteMessage(stopNumber);
                    busNumbers.clear();
                    busNumber=null;
                });
                snacky.show();
                break;
            /**
             * Toolbar item - Help menu which contains information, author name of this segment
             * version number and pertinent instruction on how to use this portion of the app
             */
            case R.id.bus_help:
                AlertDialog.Builder builderhelp = new AlertDialog.Builder(this);
                LayoutInflater inflaterhelp = this.getLayoutInflater();
                View alertViewhelp = inflaterhelp.inflate(R.layout.bus_help_layout,null);
                builderhelp.setView(alertViewhelp);
                builderhelp.setTitle("About Application:");
                builderhelp.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){}
                });

                AlertDialog dialoghelp = builderhelp.create();
                dialoghelp.show();
                break;
        }
        return true;
    }
}
