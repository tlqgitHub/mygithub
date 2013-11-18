package com.huatandm.meetme;

import java.util.List;

import android.annotation.SuppressLint;
import com.huatandm.meetme.adapter.DiscussContant;
import com.huatandm.meetme.adapter.DocsContant;
import com.huatandm.meetme.adapter.ExhibitorsContant;
import com.huatandm.meetme.adapter.InvestigateContant;
import com.huatandm.meetme.adapter.NewsContant;
import com.huatandm.meetme.adapter.SchedulesContant;
import com.huatandm.meetme.adapter.SpeakersContant;
import com.huatandm.meetme.adapter.SponsorsContant;

/**
 * @ClassName: CONTANT
 * @Description: 常量和临时共享数据
 * @author tlq
 * @date 2013-9-9 下午5:16:09
 * 
 */
public class CONTANT {

	// api接口
	public static String url = "http://eventscenter.meetmecn.com/xmlrpc/";
	public static String web_info_url = "http://eventscenter.meetmecn.com";
	
	// 官网
	public static String pif_web_url_zh ="http://www.pujiangforum.org/cn/index.aspx";
	public static String pif_web_url_en ="http://www.pujiangforum.org/index.aspx";
	
	// 离线接口
//	public static String offlineurl = "http://offline2.huatandm.com/api/xmlrpc/";
	public static String offlineurl = "http://testoffline2.huatandm.com:8001/api/xmlrpc/";
	// 临时共用数据
	public static List<SpeakersContant> listspeakers;
	public static List<SpeakersContant> listspart;
	public static List<NewsContant> listnews;
	public static List<SchedulesContant> listschedules;
	public static List<SponsorsContant> listsponsors;
	public static List<ExhibitorsContant> listexhibitors;
	public static List<InvestigateContant> listinvestigate;
	public static List<DiscussContant> listdiscussion;
	public static List<DocsContant> listdocs;

	// 数据存放路径
	@SuppressLint("SdCardPath")
	public static String mobilepath = "/data/data/com.huatandm.meetmeevent/files";
	public static String event = "events";
	public static String totalfolder = "huantanmeetme";

	// file name
	public static String maps = "maps";
	public static String speakers = "speakers";
	public static String schedules = "schedules";
	public static String info = "info";
	public static String docs = "docs";
	public static String exhibitors = "exhibitors";
	public static String news = "news";
	public static String sponsors = "sponsors";
	public static String participants = "participants";

	// event info
	public static int event_Id = 5;
	public static String eventIds = "5";
	public static String eventname;
	public static String event_description;

	// user info
	public static String username = "";
	public static String password = "";
	public static String un_read_friends = null;
	public static String un_read_msgs = null;
	public static int member_id;

	// 窗口大小
	public static int widthPixels;
	public static int heightPixels;
}
