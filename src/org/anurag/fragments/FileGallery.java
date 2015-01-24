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

package org.anurag.fragments;


import java.util.concurrent.ConcurrentHashMap;

import org.anurag.adapters.FileGalleryAdapter;
import org.anurag.adapters.FileGallerySimpleAdapter;
import org.anurag.file.quest.Constants;
import org.anurag.file.quest.FileQuestHD;
import org.anurag.file.quest.Item;
import org.anurag.file.quest.OpenFileDialog;
import org.anurag.file.quest.R;
import org.anurag.file.quest.Utils;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.extra.libs.JazzyHelper;

public class FileGallery extends Fragment implements OnClickListener{
	
	private static ListView ls;
	private static LinearLayout file_gallery;
	private static boolean is_gallery_opened;
	private static String current_Tile;
	private static Loader loader;
	private static BaseAdapter adpt;
	private static ConcurrentHashMap<String , Item> lists;
	private static ConcurrentHashMap<String , String> keys;
	public static int counter;
	public static int[] ITEMS;
	private static LinearLayout empty;
	private int id;

	public FileGallery() {
		// TODO Auto-generated constructor stub
		counter = 0;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.custom_list_view, container , false);
	}

	@Override
	public void onViewCreated(View v, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(v, savedInstanceState);
		empty = (LinearLayout) v.findViewById(R.id.empty);
		ls = (ListView) v.findViewById(R.id.customListView);
		ls.setSelector(R.drawable.list_selector_hd);
		file_gallery = (LinearLayout) v.findViewById(R.id.file_gallery_layout);
		Utils.setContext(null , getActivity());
		
		//loading file list at last....
		//this allows other threads of other panel to load first as 
		//they need little time and this thread can take long time and blocking
		//other threads....
		
		//Utils.load();
		
		Utils.setView(v);
		is_gallery_opened = false;

		LinearLayout fav = (LinearLayout) v.findViewById(R.id.fav);
		LinearLayout music = (LinearLayout) v.findViewById(R.id.music);
		LinearLayout app = (LinearLayout) v.findViewById(R.id.apps);
		LinearLayout docs = (LinearLayout) v.findViewById(R.id.docs);
		LinearLayout photo = (LinearLayout) v.findViewById(R.id.photos);
		LinearLayout vids = (LinearLayout) v.findViewById(R.id.videos);
		LinearLayout zips = (LinearLayout) v.findViewById(R.id.zips);
		LinearLayout misc = (LinearLayout) v.findViewById(R.id.misc);
		
		music.setOnClickListener(this);
		fav.setOnClickListener(this);
		app.setOnClickListener(this);
		docs.setOnClickListener(this);
		photo.setOnClickListener(this);
		vids.setOnClickListener(this);
		zips.setOnClickListener(this);
		misc.setOnClickListener(this);
		
		ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				if(Constants.LONG_CLICK){
					
					if(ITEMS[position] != 1){
						ITEMS[position] = 1;
						arg1.setBackgroundColor(getResources().getColor(R.color.white_grey));
						++counter;
						getActivity().sendBroadcast(new Intent("update_action_bar_long_click"));
					}else if(ITEMS[position] == 1){
						ITEMS[position] = 0;
						arg1.setBackgroundColor(Color.WHITE);
						if(--counter == 0)
							getActivity().sendBroadcast(new Intent("inflate_normal_menu"));
						else
							getActivity().sendBroadcast(new Intent("update_action_bar_long_click"));					
					}
					
					return;					
				}
				
				
				Item itm = (Item) ls.getAdapter().getItem(position);
				new OpenFileDialog(getActivity(), Uri.parse(itm.getPath()),
						Constants.size.x*8/9);
			}
		});
		
		ls.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				boolean sendBroadcast = false;
				if(ITEMS == null){
					ITEMS = new int[ls.getAdapter().getCount()];
					sendBroadcast = true;
				}
				
				if(ITEMS[arg2] != 1){
					arg1.setBackgroundColor(getResources().getColor(R.color.white_grey));
					ITEMS[arg2] = 1;
					++counter;
					getActivity().sendBroadcast(new Intent("update_action_bar_long_click"));
				}else if(ITEMS[arg2] == 1){
					ITEMS[arg2] = 0;
					arg1.setBackgroundColor(Color.WHITE);
					if(--counter == 0)
						getActivity().sendBroadcast(new Intent("inflate_normal_menu"));
					else
						getActivity().sendBroadcast(new Intent("update_action_bar_long_click"));
				}
				
				if(sendBroadcast)
					getActivity().sendBroadcast(new Intent("inflate_long_click_menu"));
				
				return true;
			}
		});
		
		
		setAnim(ls);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Utils.pause();
		lists = new ConcurrentHashMap<String, Item>();
		keys = new ConcurrentHashMap<String, String>();
		id = v.getId();		
		
		if(loader == null)
			loader = new Loader();
		loader.execute();
	}
	
	private class Loader extends AsyncTask<Void , Void , Void>{
		public Loader() {
			// TODO Auto-generated constructor stub
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			file_gallery.setVisibility(View.GONE);
			if(lists.size() != 0){
				ls.setVisibility(View.VISIBLE);
				adpt = getListAdapter();
				ls.setAdapter(adpt);
			}else{
				empty.setVisibility(View.VISIBLE); 
			}
			is_gallery_opened = true;
			FileQuestHD.notify_Title_Indicator(0, current_Tile);
			loader = new Loader();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub

			switch (id){
			
			case R.id.fav:
				lists = Utils.fav;
				keys = Utils.favKey;
				current_Tile = "Favorite";
				break;
				
			case R.id.music:
				lists = Utils.music;
				keys = Utils.musicKey;
				current_Tile = "Music";
				break;
				
			case R.id.apps:
				lists = Utils.apps;
				keys = Utils.appKey;
				current_Tile = "Apps";
				break;
				
			case R.id.docs:
				lists = Utils.doc;
				keys = Utils.docKey;
				current_Tile = "Docs";
				break;
				
			case R.id.photos:
				lists = Utils.img;
				keys = Utils.imgKey;
				current_Tile = "Images";
				break;
				
			case R.id.videos:
				lists = Utils.vids;
				keys = Utils.videoKey;
				current_Tile = "Videos";
				break;
				
			case R.id.zips:
				lists = Utils.zip;
				keys = Utils.zipKey;
				current_Tile = "Archives";
				break;
				
			case R.id.misc:
				lists = Utils.mis;
				keys = Utils.misKey;
				current_Tile = "Unknown";
				break;
			}

			return null;
		}
		
	}
	
	/**
	 * 
	 * @return true then file gallery is opened....
	 */
	public static boolean isGalleryOpened(){
		return is_gallery_opened;
	}
	
	/**
	 * sets the list view selector as per selected theme
	 * dynamically by user....
	 */
	/*public static void setListSelector(){
		ls.setSelector(Constants.SELECTOR_STYLE);
	}
	*/
	
	public static void collapseGallery(){
		ls.setVisibility(View.GONE);
		empty.setVisibility(View.GONE);
		file_gallery.setVisibility(View.VISIBLE);
		is_gallery_opened = false;
		current_Tile = "FILE GALLERY";
		FileQuestHD.notify_Title_Indicator(0, current_Tile);
	}
	
	/**
	 * refreshes the list view....
	 */
	public static void refresh_list(){
		if(is_gallery_opened)
			return;
		ls.setAdapter(null);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ls.setAdapter(adpt);
	}
	
	/**
	 * this function sets transition effect for list view.... 
	 * @param list2
	 */
	private void setAnim(ListView list2) {
		// TODO Auto-generated method stub
		JazzyHelper help = new JazzyHelper(getActivity(), null);
		help.setTransitionEffect(Constants.LIST_ANIM);
		list2.setOnScrollListener(help);
	}

	/**
	 * this function clears the selected items via long click from lits view....
	 */
	public static void clear_selected_items(){
		ls.setAdapter(adpt);
		counter = 0;
	}
	
	/**
	 * 
	 * @return the adapter as per the settings....
	 * will return detailed list adapter,simple list adapter,simple grid adapter or detailed
	 * grid adapter....
	 */
	private BaseAdapter getListAdapter(){
		switch(Constants.LIST_TYPE){
		case 1:
			return new FileGallerySimpleAdapter(getActivity(), lists, keys);
		case 2:
			return new FileGalleryAdapter(getActivity(), lists, keys);
			
		}
		return null; 
	}

	/**
	 * reloads the adapter when list type is changed....
	 */
	public static void resetAdapter(){
		if(!is_gallery_opened || lists.size() == 0)
			return;
		loader.execute();
	}
}
