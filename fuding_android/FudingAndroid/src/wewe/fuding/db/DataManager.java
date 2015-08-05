package wewe.fuding.db;

import java.util.ArrayList;

import wewe.fuding.domain.Frame;
import android.content.Context;
import android.database.Cursor;

public class DataManager {
	
	private Context context = null;
	private DbOpenHelper mDbOpenHelper;
	private Cursor mCursor=null;
	
    
    public DataManager(Context context) {
		this.context = context;
		mDbOpenHelper = new DbOpenHelper(context);		
	}
}
/*    
    public void insertFrameColumn(Frame food) {
    	
    	mDbOpenHelper.insertFrameColumn();
    }
    
}

	public ArrayList<GroupTitle> getGrTitleList() {
		ArrayList<GroupTitle> grTitle = new ArrayList<GroupTitle>();
		SQLiteDatabase gDB = gDBHelper.getReadableDatabase();
		String getGrQuery = "SELECT * FROM group_list";
		Cursor cursor = gDB.rawQuery(getGrQuery, null);

		int no = 0;
		String title = "";
		int cnt = 0;
		while (cursor.moveToNext()) {
			no = cursor.getInt(0);
			title = cursor.getString(1);
			cnt = getItemCount(cursor.getInt(0));
			grTitle.add(new GroupTitle(no, title, cnt));
		}
		gDB.close();
		return grTitle;
	}

	public ArrayList<MyItem> getItemList(int gNo) {
		Log.i("getItemList in", "gNo : " + gNo);
		ArrayList<MyItem> itemList = new ArrayList<MyItem>();
		SQLiteDatabase iDB = iDBHelper.getReadableDatabase();
		String getItQuery = "SELECT * FROM item_list";
		Cursor cursor = iDB.rawQuery(getItQuery, null);

		int no = 0;
		String name = "";
		int groupNo = 0;
		while (cursor.moveToNext()) {
			Log.i("getItemList in", "gNo : " + gNo + "cursor.getInt(2) : " + cursor.getInt(2));
			if (cursor.getInt(2) == gNo) {
				no = cursor.getInt(0);
				name = cursor.getString(1);
				groupNo = cursor.getInt(2);
				Log.i("getItemList add", "itemName" + name);

				itemList.add(new MyItem(no, name, groupNo));
			}
		}
		iDB.close();

		return itemList;
	}
	public String getGroupName(int gNo) {
		SQLiteDatabase gDB = gDBHelper.getReadableDatabase();
		String selectQuery = "SELECT * FROM group_list";
		Cursor cursor = gDB.rawQuery(selectQuery, null);
		String gName = null;
		while (cursor.moveToNext()) {
			if (cursor.getInt(0) == gNo) {
				gName = cursor.getString(1);
				break;
			}
		}
		gDB.close();

		return gName;
	}

	public int getItemCount(int gNo) {
		SQLiteDatabase iDB = iDBHelper.getReadableDatabase();
		String countQuery = "SELECT * FROM item_list";
		Cursor cursor = iDB.rawQuery(countQuery, null);
		int i = 0;
		while (cursor.moveToNext()) {
			if (cursor.getInt(2) == gNo) {
				i++;
			}
		}
		iDB.close();
		return i;
	}

	public int addGroup(String gName) {
		int addGrNo = 0;
		SQLiteDatabase gDB = gDBHelper.getWritableDatabase();
		String addQuery = "INSERT INTO group_list VALUES (null,'" + gName
				+ "' )";
		gDB.execSQL(addQuery);
		gDB.close();

		gDB = gDBHelper.getReadableDatabase();
		String selectQuery = "SELECT * FROM group_list";
		Cursor cursor = gDB.rawQuery(selectQuery, null);
		cursor.moveToLast();
		addGrNo = cursor.getInt(0);
		Log.i("addGrNo", "" + addGrNo);

		return addGrNo;
	}
	
	public int addItem(String iName, int gNo) {
		int addItemNo = 0;
		
		SQLiteDatabase iDB = iDBHelper.getWritableDatabase();
		String addQuery = "INSERT INTO item_list VALUES (null,'" + iName
				+ "'," + gNo + ")";
		iDB.execSQL(addQuery);
		iDB.close();

		iDB = iDBHelper.getReadableDatabase();
		String selectQuery = "SELECT * FROM item_list";
		Cursor cursor = iDB.rawQuery(selectQuery, null);
		cursor.moveToLast();
		addItemNo = cursor.getInt(0);
		Log.i("addItemNo", "" + addItemNo);
		
		return addItemNo;
	}

	public void deleteGroup(int gNo) {
		SQLiteDatabase gDB = gDBHelper.getWritableDatabase();
		String delQuery = "DELETE FROM group_list WHERE group_no=" + gNo;
		gDB.execSQL(delQuery);
		gDB.close();
	}

	public void deleteGroupItem(int gNo) {
		logItemList(); //

		SQLiteDatabase iDB = iDBHelper.getWritableDatabase();
		String delQuery = "DELETE FROM item_list WHERE item_g_no=" + gNo;
		iDB.execSQL(delQuery);
		logItemList(); //
		iDB.close();
	}
	
	public void deleteItem(int iNo) {
		SQLiteDatabase iDB = iDBHelper.getWritableDatabase();
		String delQuery = "DELETE FROM item_list WHERE item_no=" + iNo;
		iDB.execSQL(delQuery);
		logItemList(); //
		iDB.close();
	}

	public void logItemList() {
		Log.i("itemListLog", "in");
		SQLiteDatabase iDB = iDBHelper.getReadableDatabase();
		String countQuery = "SELECT * FROM item_list";
		Cursor cursor = iDB.rawQuery(countQuery, null);

		while (cursor.moveToNext()) {
			Log.i("itemListLog", cursor.getInt(0) + " " + cursor.getString(1)
					+ " " + cursor.getInt(2));
		}
		iDB.close();
	}
}
*/