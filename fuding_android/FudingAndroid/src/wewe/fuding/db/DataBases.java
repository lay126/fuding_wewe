package wewe.fuding.db;


import android.provider.BaseColumns;

// DataBase Table
public final class DataBases {
	
	
	// 요리 프레임 테이블 
	public static final class CreateFrameDB implements BaseColumns{ 
		public static final String USERID = "userId";			// 글쓴이 아이디
		public static final String FOODNAME = "foodName";		// 음식 이름 
		public static final String INGRE = "ingre";				// 재료 태그 
		public static final String AMOUNT = "amount";			// 요리 양 (아영이한테 말해줘야댐!!)
		public static final String TOTALTIME = "totalTime";		// 총 소요시간 	
		public static final String TAG = "tag";					// 음식 관련 기타 태그  
		public static final String _TABLENAME = "writeFrame";
		public static final String _CREATE_FRAME = "create table "+_TABLENAME+"(" 
												+_ID+" integer primary key autoincrement, " 	
												+USERID+" text not null , " 
												+FOODNAME+" text not null , " 
												+INGRE+" text not null , " 
												+AMOUNT+" text not null , " 
												+TOTALTIME+" text not null , " 
												+TAG+" text not null);";
	}
	
	// 요리 단계별 테이블 
	public static final class CreateContentDB implements BaseColumns{ 
		public static final String FOODID = "foodId";					// 글 프레임 고유 번호 
		public static final String CONTENTID = "contentId";			// 단계 별 고유 번호 (1~10) 
		public static final String CONTENT = "content";				// 단계 별 셜명 
		public static final String CONTENTTIME = "contentTime";		// 단계 별 소요시간 	
		public static final String PHOTO = "photo";					// 사진 URL   
		public static final String _TABLENAME = "writeContent";
		public static final String _CREATE_CONTENT = "create table "+_TABLENAME+"(" 
													+FOODID+" integer not null , " 
													+CONTENTID+" integer not null , " 
													+CONTENT+" text not null , " 
													+CONTENTTIME+" text not null , " 
													+PHOTO+" text not null);";
	}
	
	public static final class CreateNotiDB implements BaseColumns{ 
		public static final String FRIENDID = "friendId";
		public static final String FRIENDIMAGE = "friendImage";
		public static final String TYPE = "type";
		public static final String DATE = "date";
		public static final String _TABLENAME = "noti";
		public static final String _CREATE_NOTI = "create table "+_TABLENAME+"(" 
													+_ID+" integer primary key autoincrement, " 	
													+FRIENDID+" text not null , " 
													+FRIENDIMAGE+" text not null , " 
													+TYPE+" text not null , " 
													+DATE+" text not null);";
	}
}
