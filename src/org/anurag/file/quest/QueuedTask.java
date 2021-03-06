/**
 * Copyright(c) 2015 DRAWNZER.ORG PROJECTS -> ANURAG
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
 *                             anuraxsharma1512@gmail.com
 *
 */

package org.anurag.file.quest;

import java.util.ArrayList;



/**
 * 
 * @author anurag
 *
 */
public class QueuedTask {
	
	private int TASK_ID;
	private ArrayList<Item> ls;
	private String parentDir;
	private int folders;
	private int files;
	/**
	 * 
	 * @param item
	 * @param task_ID
	 */
	public QueuedTask(ArrayList<Item> item , int task_ID , String parentPath
			, int folder_c , int file_c) {
		// TODO Auto-generated constructor stub
		this.ls = item;
		this.TASK_ID = task_ID;
		this.parentDir = parentPath;
		this.folders = folder_c;
		this.files = file_c;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<Item> getList(){
		return this.ls;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getTaskID(){
		return this.TASK_ID;
	}
	
	/**
	 * 
	 * @return
	 */
	public String folder_count(){
		return (folders + " folders ");
	}
	
	/**
	 * 
	 * @return
	 */
	public String file_count(){
		return (files + " files");
	}
	
	/**
	 * 
	 * @return
	 */
	public String get_parent_dir(){
		return this.parentDir;
	}
}
