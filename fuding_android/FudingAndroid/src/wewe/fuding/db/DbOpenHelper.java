package wewe.fuding.db;

import wewe.fuding.domain.Frame;
import wewe.fuding.domain.Noti;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbOpenHelper {

	private static final String DATABASE_NAME = "fuding.db";
	private static final int DATABASE_VERSION = 1;
	public static SQLiteDatabase mDB;
	private DatabaseHelper mDBHelper;
	private Context mCtx;

	private class DatabaseHelper extends SQLiteOpenHelper {

		// 생성자
		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		// 최초 DB를 만들때 한번만 호출된다.
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DataBases.CreateFrameDB._CREATE_FRAME);
			db.execSQL(DataBases.CreateContentDB._CREATE_CONTENT);
			db.execSQL(DataBases.CreateNotiDB._CREATE_NOTI);

		}

		// 버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS "
					+ DataBases.CreateFrameDB._TABLENAME);
			db.execSQL("DROP TABLE IF EXISTS "
					+ DataBases.CreateContentDB._TABLENAME);
			db.execSQL("DROP TABLE IF EXISTS "
					+ DataBases.CreateNotiDB._TABLENAME);
			onCreate(db);
		}
	}

	public DbOpenHelper(Context context) {
		this.mCtx = context;
	}

	public DbOpenHelper open() throws SQLException {
		mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null,
				DATABASE_VERSION);
		mDB = mDBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDB.close();
	}

	// [ FRAME TABLE ]
	// Insert DB
	public long insertFrameColumn(Frame food) {
		ContentValues values = new ContentValues();
		values.put(DataBases.CreateFrameDB.USERID, food.getUserId());
		values.put(DataBases.CreateFrameDB.FOODNAME, food.getFoodName());
		values.put(DataBases.CreateFrameDB.INGRE, food.getIngre());
		values.put(DataBases.CreateFrameDB.AMOUNT, food.getAmount());
		values.put(DataBases.CreateFrameDB.TOTALTIME, food.getTotalTime());
		values.put(DataBases.CreateFrameDB.TAG, food.getTag());
		return mDB.insert(DataBases.CreateFrameDB._TABLENAME, null, values);
	}

	// Select All
	public Cursor getFrameAllColumns() {
		return mDB.query(DataBases.CreateFrameDB._TABLENAME, null, null, null,
				null, null, null);
	}

	// [ CONTENT TABLE ]
	// Insert DB
	public long insertContentColumn(int foodId, int contentId, String content, String contentTime, String photo) {
		ContentValues values = new ContentValues();
		values.put(DataBases.CreateContentDB.FOODID, foodId);
		values.put(DataBases.CreateContentDB.CONTENTID, contentId);
		values.put(DataBases.CreateContentDB.CONTENT, content);
		values.put(DataBases.CreateContentDB.CONTENTTIME, contentTime);
		values.put(DataBases.CreateContentDB.PHOTO, photo);
		return mDB.insert(DataBases.CreateContentDB._TABLENAME, null, values);
	}

	// Select All
	public Cursor getContentAllColumns() {
		return mDB.query(DataBases.CreateContentDB._TABLENAME, null, null, null,
				null, null, null);
	}

	// [ NOTI TABLE ]
	// Insert DB
	public long insertNotiColumn(String friendId, String friendImage, String type, String date) {
		ContentValues values = new ContentValues();
		values.put(DataBases.CreateNotiDB.FRIENDID, friendId);
		values.put(DataBases.CreateNotiDB.FRIENDIMAGE, friendImage);
		values.put(DataBases.CreateNotiDB.TYPE, type);
		values.put(DataBases.CreateNotiDB.DATE, date); 
		return mDB.insert(DataBases.CreateNotiDB._TABLENAME, null, values);
	}

	// Select All
	public Cursor getNotiAllColumns() {
		return mDB.query(DataBases.CreateNotiDB._TABLENAME, null, null,
				null, null, null, null);
	}

	// // Update DB
	// public boolean updateColumn(long id , String name, String contact, String
	// email){
	// ContentValues values = new ContentValues();
	// values.put(DataBases.CreateDB.NAME, name);
	// values.put(DataBases.CreateDB.CONTACT, contact);
	// values.put(DataBases.CreateDB.EMAIL, email);
	// return mDB.update(DataBases.CreateDB._TABLENAME, values, "_id="+id, null)
	// > 0;
	// }
	//
	// // Delete ID
	// public boolean deleteColumn(long id){
	// return mDB.delete(DataBases.CreateDB._TABLENAME, "_id="+id, null) > 0;
	// }
	//
	// // Delete Contact
	// public boolean deleteColumn(String number){
	// return mDB.delete(DataBases.CreateDB._TABLENAME, "contact="+number, null)
	// > 0;
	// }
	//
	//
	// // ID 컬럼 얻어 오기
	// public Cursor getColumn(long id){
	// Cursor c = mDB.query(DataBases.CreateDB._TABLENAME, null,
	// "_id="+id, null, null, null, null);
	// if(c != null && c.getCount() != 0)
	// c.moveToFirst();
	// return c;
	// }
	//
	// // 이름 검색 하기 (rawQuery)
	// public Cursor getMatchName(String name){
	// Cursor c = mDB.rawQuery( "select * from address where name=" + "'" + name
	// + "'" , null);
	// return c;
	// }

}
