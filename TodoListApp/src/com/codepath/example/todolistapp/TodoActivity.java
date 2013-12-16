package com.codepath.example.todolistapp;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


/**
 * 
 * @author gargka
 * 
 * Main activity class wherein the user can add new items to the list, edit items, remove items and export the list items to a file. 
 */

public class TodoActivity extends Activity {

	private static final String TITLE = "Simple Todo List";
	private static final String EMPTY_STR = "";
	private static final String FILENAME = "todo.txt";
	
	private ListView lvItems;
	private EditText etAddNewItem;

	private final int REQUEST_CODE = 20;

	private ArrayAdapter<String> itemsAdapter;
	private List<String> items;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);
		setTitle(TITLE); 

		lvItems = (ListView) findViewById(R.id.lvItemsID);
		etAddNewItem = (EditText) findViewById(R.id.etAddItemID);

		//read the items for the file
		items = readItems();
		itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
		lvItems.setAdapter(itemsAdapter);

		items.add("item 1");
		items.add("item 2"); 

		setupListViewListener();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo, menu);
		return true;
	}

	/**
	 * Adds the new item to the list
	 * @param v
	 */
	public void addTodoItem(View v) {

		String itemToAdd = etAddNewItem.getText().toString();
		if(!itemToAdd.equals(EMPTY_STR))
			itemsAdapter.add(itemToAdd);

		etAddNewItem.setText(EMPTY_STR);
	}

	/**
	 * Set up the list item listeners to remove a list item on long click and edit an item on click.
	 */
	private void setupListViewListener() {

		lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {			
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				items.remove(position);
				itemsAdapter.notifyDataSetInvalidated();
				saveItems();
				return true;
			}
		});

		lvItems.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				//create a new intent to launch the edit item activity.. pass in the item text and position
				Intent i = new Intent(TodoActivity.this, EditItemActivity.class);

				i.putExtra("itemText", ((TextView) parent.getChildAt(position)).getText().toString());
				i.putExtra("itemPosition", position);

				startActivityForResult(i, REQUEST_CODE);
			}
		});
	}

	/**
	 * Set the edited list item back in the list
	 */	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {

			//extract the edited text from the intent and set it back into the list. 
			String itemText  = data.getExtras().getString("itemText");
			items.set(data.getExtras().getInt("itemPosition"), itemText);

			itemsAdapter.notifyDataSetInvalidated();
			saveItems();
			
			//test to see if the items are read back into the list. For now the list is initialized with no data.
			//readItems();
		}
	} 

	/**
	 * Read items from a text file into the items list. 
	 * Couldnt use FileUtils because of import issues and so using the android openFileInput() api 
	 * directly.   
	 */
	@SuppressWarnings("unchecked")
	private List<String> readItems() {	
		
		FileInputStream fin = null;
		ObjectInputStream ois = null;

		try {

			fin = openFileInput (FILENAME);
			ois = new ObjectInputStream(fin);
			items= (ArrayList<String>)ois.readObject();
			fin.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			try {
				ois.close();
				fin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return items;
	}

	/**
	 * Save the items on file. Couldnt use FileUtils because of import issues and so using the android openFileOutput() api 
	 * directly. 
	 */
	private void saveItems() {		

		FileOutputStream fos = null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;

		try {
			fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);

			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(items);
			byte[] bytes = bos.toByteArray();			
			fos.write(bytes);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				oos.close();
				bos.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
