package shutenmei.example.com.customerproduct;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by shutenmei on 16/4/27.
 */
public class DBhelper extends SQLiteOpenHelper {

    static String DBPath = "/data/data/shutenmei.example.com.customerproduct/databases/";
    String DB_PATH;
    private final static String DB_NAME = "db_order";
    public final static int DB_VERSION = 1;
    public static SQLiteDatabase db;

    private final Context context;

    private final String TABLE_NAME = "tbl_order";
    private final String ID = "id";
    private final String MENU_NAME = "Menu_name";
    private final String QUANTITY = "Quantity";
    private final String TOTAL_PRICE = "Total_price";

    public DBhelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        DB_PATH = DBPath;
    }

    public void createDataBase() throws IOException{

        boolean dbExist = checkDataBase();
        SQLiteDatabase db_Read = null;


        if(dbExist){
            //do nothing - database already exist

        }else{
            db_Read = this.getReadableDatabase();
            db_Read.close();

            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }

    }



    private boolean checkDataBase(){

        File dbFile = new File(DB_PATH + DB_NAME);

        return dbFile.exists();

    }


    private void copyDataBase() throws IOException{

        InputStream myInput = context.getAssets().open(DB_NAME);

        String outFileName = DB_PATH + DB_NAME;

        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }


    @Override
    public void close() {
        db.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void openDataBase() throws SQLException{
        String myPath = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    /** this code is used to get all data from database */
    public ArrayList<ArrayList<Object>> getAllData(){
        ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();

        Cursor cursor = null;

        try{
            cursor = db.query(
                    TABLE_NAME,
                    new String[]{ID, MENU_NAME, QUANTITY, TOTAL_PRICE},
                    null,null, null, null, null);
            cursor.moveToFirst();

            if (!cursor.isAfterLast()){
                do{
                    ArrayList<Object> dataList = new ArrayList<Object>();

                    dataList.add(cursor.getLong(0));
                    dataList.add(cursor.getString(1));
                    dataList.add(cursor.getString(2));
                    dataList.add(cursor.getString(3));

                    dataArrays.add(dataList);
                }

                while (cursor.moveToNext());
            }
            cursor.close();
        }catch (SQLException e){
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }

        return dataArrays;
    }

    /** this code is used to get all data from database */
    public boolean isDataExist(String id){
        boolean exist = false;

        Cursor cursor = null;

        try{
            cursor = db.query(
                    TABLE_NAME,
                    new String[]{ID},
                    ID +"="+id,
                    null, null, null, null);
            if(cursor.getCount() > 0){
                exist = true;
            }

            cursor.close();
        }catch (SQLException e){
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }

        return exist;
    }

    /** this code is used to get all data from database */
    public boolean isPreviousDataExist(){
        boolean exist = false;

        Cursor cursor = null;

        try{
            cursor = db.query(
                    TABLE_NAME,
                    new String[]{ID},
                    null,null, null, null, null);
            if(cursor.getCount() > 0){
                exist = true;
            }

            cursor.close();
        }catch (SQLException e){
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }

        return exist;
    }

    public void addData(String id, String menu_name, int quantity, double total_price){

        db.execSQL("create table if not exists tbl_order(id text,Menu_name text,Quantity text,Total_price text)");
        // this is a key value pair holder used by android's SQLite functions
        ContentValues values = new ContentValues();
        values.put(ID, id);
        values.put(MENU_NAME, menu_name);
        values.put(QUANTITY, quantity);
        values.put(TOTAL_PRICE, total_price);

        // ask the database object to insert the new data
        try{db.insert(TABLE_NAME, null, values);}
        catch(Exception e)
        {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    public void deleteData(String id){
        // ask the database manager to delete the row of given id
        try {db.delete(TABLE_NAME, ID + "=" + id, null);}
        catch (Exception e)
        {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    public void deleteAllData(){
        // ask the database manager to delete the row of given id
        try {db.delete(TABLE_NAME, null, null);}
        catch (Exception e)
        {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    public void updateData(String id, int quantity, double total_price){

        String prequantity;
        String pre_total_price;
        int prequan;
        double pretot;

        Cursor cursor=null;
        cursor = db.query(
                TABLE_NAME,
                new String[]{ID, MENU_NAME, QUANTITY, TOTAL_PRICE},
                ID + "=" + id,
                null, null, null, null);
        cursor.moveToFirst();

        prequantity = cursor.getString(cursor.getColumnIndex(QUANTITY));
        pre_total_price=cursor.getString(cursor.getColumnIndex(TOTAL_PRICE));

        cursor.close();

        prequan=Integer.parseInt(prequantity);
        pretot=Double.parseDouble(pre_total_price);

        int total_quan=prequan+quantity;
        double tot_price=pretot+total_price;

        // this is a key value pair holder used by android's SQLite functions
        ContentValues values = new ContentValues();
        values.put(QUANTITY, total_quan);
        values.put(TOTAL_PRICE, tot_price);

        // ask the database object to update the database row of given rowID
        try {db.update(TABLE_NAME, values, ID + "=" + id, null);}
        catch (Exception e)
        {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }
    }

    public void transferdata(){
        db.execSQL("create table if not exists tbl_order2(id text,Menu_name text,Quantity text,Total_price text)");
        db.execSQL("INSERT INTO tbl_order2 SELECT * FROM tbl_order;");
    }

    public void increaseData(String id){

        String prequantity;
        String pre_total_price;
        int prequan;
        double pretot;

        Cursor cursor=null;
        cursor = db.query(
                TABLE_NAME,
                new String[]{ID, MENU_NAME, QUANTITY, TOTAL_PRICE},
                ID + "=" + id,
                null, null, null, null);
        cursor.moveToFirst();

        prequantity = cursor.getString(cursor.getColumnIndex(QUANTITY));
        pre_total_price=cursor.getString(cursor.getColumnIndex(TOTAL_PRICE));

        cursor.close();

        prequan=Integer.parseInt(prequantity);
        pretot=Double.parseDouble(pre_total_price);

        int total_quan=prequan+1;
        double tot_price=pretot*(total_quan)/prequan;

        // this is a key value pair holder used by android's SQLite functions
        ContentValues values = new ContentValues();
        values.put(QUANTITY, total_quan);
        values.put(TOTAL_PRICE, tot_price);

        // ask the database object to update the database row of given rowID
        try {db.update(TABLE_NAME, values, ID + "=" + id, null);}
        catch (Exception e)
        {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }
    }

    public void decreaseData(String id){

        String prequantity;
        String pre_total_price;
        int prequan;
        double pretot;

        Cursor cursor=null;
        cursor = db.query(
                TABLE_NAME,
                new String[]{ID, MENU_NAME, QUANTITY, TOTAL_PRICE},
                ID + "=" + id,
                null, null, null, null);
        cursor.moveToFirst();

        prequantity = cursor.getString(cursor.getColumnIndex(QUANTITY));
        pre_total_price=cursor.getString(cursor.getColumnIndex(TOTAL_PRICE));

        cursor.close();

        prequan=Integer.parseInt(prequantity);
        pretot=Double.parseDouble(pre_total_price);

        int total_quan=prequan-1;
        double tot_price=pretot*(total_quan)/prequan;

        // this is a key value pair holder used by android's SQLite functions
        ContentValues values = new ContentValues();
        values.put(QUANTITY, total_quan);
        values.put(TOTAL_PRICE, tot_price);

        // ask the database object to update the database row of given rowID
        try {db.update(TABLE_NAME, values, ID + "=" + id, null);}
        catch (Exception e)
        {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }
    }
}