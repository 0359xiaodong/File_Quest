/**
 * Copyright(c) 2014 DRAWNZER.ORG PROJECTS -> ANURAG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *      
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *                             
 *                             anurag.dev1512@gmail.com
 *
 */

package org.anurag.file.quest;

import org.anurag.adapters.FileGallery;
import org.anurag.adapters.PagerAdapters;
import org.anurag.adapters.RootPanel;
import org.anurag.adapters.SdCardPanel;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.fuehlbypa.kddcbytnh159110.Prm;


/**
 * main activity class for File quest....
 * 
 * @author anurag
 *
 */
public class FileQuestHD extends ActionBarActivity {

	private ActionBar action_bar;
	private static PagerSlidingTabStrip indicator;
	private ViewPager pager;
	private PagerAdapters adapters;

	private Toolbar toolbar;
	private ActionBarDrawerToggle toggle;
	private DrawerLayout drawer;
	private SharedPreferences prefs;
	
	private boolean mBackPressed;
	
	private Editor prefs_editor;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		prefs = getSharedPreferences("SETTINGS", 0);
		
		Constants.SORT_TYPE = prefs.getInt("SORT_TYPE", 2);
		Constants.FOLDER_ICON = prefs.getInt("ICON", 0);
		Constants.SHOW_HIDDEN_FOLDERS = prefs.getBoolean("SHOW_HIDDEN", false);
		Constants.FOLDER_IMAGE = getResources().getDrawable(R.drawable.folder);
		Constants.PANEL_NO = prefs.getInt("PANEL_NO", 0);
		Constants.COLOR_STYLE = prefs.getInt("COLOR_STYLE", 0xFFFF5D3D);
		Constants.db = new ItemDB(FileQuestHD.this);
		Constants.size = new Point();
		getWindowManager().getDefaultDisplay().getSize(Constants.size);
		
		prefs_editor = prefs.edit();
		
		setContentView(R.layout.fq_ui_hd);
		findViewIds();
		setSupportActionBar(toolbar);	
		action_bar = getSupportActionBar();
		styleActionBar(Constants.COLOR_STYLE);
		toggle = new ActionBarDrawerToggle(FileQuestHD.this, drawer,
				R.drawable.file_quest_icon, R.string.settings){
			public void onDrawerClosed(View view) {
                action_bar.setTitle(getString(R.string.app_name));
                //isDrawerOpen = false;                
            } 
            public void onDrawerOpened(View drawerView) {
                action_bar.setTitle(getString(R.string.settings));
                //isDrawerOpen = true;
            }
		};
		
		drawer.setDrawerListener(toggle);
		
		pager.setAdapter(adapters);
		indicator.setViewPager(pager);
		pager.setOffscreenPageLimit(4);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		init_system_ui();
		mBackPressed = false;
		if(!prefs.getString("VERSION", "0.0.0").equalsIgnoreCase(getString(R.string.appversion))){
			prefs_editor.putString("VERSION", getString(R.string.appversion));
			prefs_editor.commit();
			new WhatsNew(FileQuestHD.this, Constants.size.x*8/9, Constants.size.y*8/9);
		}
		Prm prm = new Prm(FileQuestHD.this, null, true);
		prm.run360Ad(FileQuestHD.this, 0, false, null);
	}
	
	/**
	 * 
	 * @param color
	 */
	private void styleActionBar(int color) {
		// TODO Auto-generated method stub
		toolbar.setBackgroundColor(color);
		indicator.setBackgroundColor(color);
		LinearLayout drawermenu = (LinearLayout) findViewById(R.id.drawer_list);
		drawermenu.setBackgroundColor(color);
		action_bar.setHomeButtonEnabled(true);
		action_bar.setDisplayHomeAsUpEnabled(true);		
	}
	
	/**
	 * this function finds the ids of all view used....
	 */
	private void findViewIds() {
		// TODO Auto-generated method stub
		indicator = (PagerSlidingTabStrip) findViewById(R.id.pagerSlidingTabStrip1);
		pager = (ViewPager) findViewById(R.id.view);
		adapters = new PagerAdapters(getSupportFragmentManager());
		drawer = (DrawerLayout)findViewById(R.id.sliding_drawer);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		
	}
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		int panel = pager.getCurrentItem();
		if(panel == 0){
			if(FileGallery.isGalleryOpened())
				FileGallery.collapseGallery();
			else if(panel == Constants.PANEL_NO)
				detect_back_press();			
		}else if(panel == 1){
			if(RootPanel.isAtTopLevel() && panel != Constants.PANEL_NO)
				pager.setCurrentItem(Constants.PANEL_NO);
			else if(RootPanel.isAtTopLevel())
				detect_back_press();
			else
				RootPanel.navigate_to_back();
		}else if(panel == 2){
			if(SdCardPanel.isAtTopLevel() && panel != Constants.PANEL_NO)
				pager.setCurrentItem(Constants.PANEL_NO);
			else if(SdCardPanel.isAtTopLevel())
				detect_back_press();
			else
				SdCardPanel.navigate_to_back();
		}else if(panel == 3 && panel == Constants.PANEL_NO)
			detect_back_press();
		else
			pager.setCurrentItem(Constants.PANEL_NO);
	}	
	
	/**
	 * this function checks how many times back key is pressed
	 * on second press it finishes the app....
	 */
	private void detect_back_press(){
		if(mBackPressed)
			FileQuestHD.this.finish();
		else{
			mBackPressed = true;
			Toast.makeText(FileQuestHD.this, R.string.pressbackagain, Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 
	 * @param position
	 * @param title
	 */
	public static void notify_Title_Indicator(int position , String title){
		PagerAdapters.setTitles(position, title);
		indicator.notifyDataSetChanged();
	}
	
	/**
	 * restyles the system UI like status bar or navigation bar if present....
	 */
	private void init_system_ui() {
		// TODO Auto-generated method stub
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
			return;
		SystemBarTintManager tint = new SystemBarTintManager(FileQuestHD.this);
		tint.setStatusBarTintEnabled(true);
		tint.setNavigationBarTintEnabled(true);
		tint.setStatusBarTintColor(Constants.COLOR_STYLE);
		tint.setNavigationBarTintColor(Constants.COLOR_STYLE);
		LinearLayout main = (LinearLayout) findViewById(R.id.frame_container);
		main.setPadding(0, getStatusBarHeight(), 0, 0);
		LinearLayout slide_menu = (LinearLayout) findViewById(R.id.drawer_list);
		slide_menu.setPadding(0, getStatusBarHeight(), 0, 0);
	}
	
	private int getStatusBarHeight(){
		int res = 0;
		int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if(resId > 0)
			res = getResources().getDimensionPixelSize(resId);
		return res;
	}
}
