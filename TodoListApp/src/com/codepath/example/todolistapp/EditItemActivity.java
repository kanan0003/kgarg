package com.codepath.example.todolistapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

/**
 * 
 * @author gargka
 *
 * Edit Activity which is started once the user clicks on a list item to edit.
 */

public class EditItemActivity extends Activity {
	private static final String TITLE = "Edit Item";

	private EditText etItemToEdit;
	
	private String itemText;
	private int itemPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		setTitle(TITLE);

		itemText = getIntent().getStringExtra("itemText");
		itemPosition = getIntent().getIntExtra("itemPosition", 0);

		setItemToEdit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
		return true;
	}

	/**
	 * Sets the item selected in the list for edit in the edit text.
	 */
	private void setItemToEdit() {
		etItemToEdit = (EditText) findViewById(R.id.etEditItemID);
		etItemToEdit.setText(itemText);
		etItemToEdit.setCursorVisible(true);
		etItemToEdit.requestFocus();
		etItemToEdit.setFocusable(true);
	}

	/**
	 * Save onclick handler
	 * @param v
	 */
	public void save(View v) {
		
		// Create an intent to pass the data back to the main activity 
		Intent data = new Intent();
		
		data.putExtra("itemText", etItemToEdit.getText().toString());
		data.putExtra("itemPosition", itemPosition);

		// Activity finished ok, return the data
		setResult(RESULT_OK, data); 
		finish(); 
	}

}
