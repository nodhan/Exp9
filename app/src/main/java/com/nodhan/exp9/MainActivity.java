package com.nodhan.exp9;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    SharedPreferences sharedPreferences;
    int count, selectedItem;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    boolean added = false, changed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = getSharedPreferences("students", MODE_PRIVATE);

        count = sharedPreferences.getInt("count", 0);

        if (added || changed) {
            if (added) {
                arrayList.add(sharedPreferences.getString("name" + count, ""));        //to add new count in listView
                added = false;
            }

            if (changed) {
                arrayList.set(selectedItem, sharedPreferences.getString("name" + selectedItem, ""));      //to change the count in listView
                changed = false;
            }
            arrayAdapter.notifyDataSetChanged();
        }

        if (count != 0) {
            ((TextView) findViewById(R.id.message)).setText(R.string.message);
            listView = (ListView) findViewById(R.id.listview);
            arrayList = new ArrayList<>();
            for (int i = 0; i < count; i++)
                arrayList.add(sharedPreferences.getString("name" + i, ""));
            arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(arrayAdapter);
            registerForContextMenu(listView);
            arrayAdapter.notifyDataSetChanged();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startActivity(new Intent(getApplicationContext(), DisplayActivity.class).putExtra("selection", position));
                }
            });
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.listview) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            //menu.setHeaderTitle(list[info.position]);
            String[] menuItems = getResources().getStringArray(R.array.contextMenu);
            menu.setHeaderTitle("Select an option");
            for (int i = 0; i < menuItems.length; i++)
                menu.add(Menu.NONE, i, i, menuItems[i]);
            selectedItem = info.position;
            Log.d("selectedItem", selectedItem + "");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getTitle().toString()) {
            case "Remove":
                arrayList.remove(arrayList.get(selectedItem));
                arrayAdapter.notifyDataSetChanged();        //to update the listView

                sharedPreferences = getSharedPreferences("students", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();     //getting editable sharedpreference file

                editor.remove("id" + selectedItem);
                editor.remove("name" + selectedItem);
                editor.remove("department" + selectedItem);
                editor.remove("mentor" + selectedItem);
                editor.remove("contact" + selectedItem);
                editor.putInt("count", --count);
                editor.apply();
                onRestart();
                break;
            case "Edit": {
                changed = true;
                startActivity(new Intent(getApplicationContext(), EditAddActivity.class).putExtra("selection", selectedItem).putExtra("flag", false));          //starts the EditAdd Activity
                break;
            }
            case "Display": {
                startActivity(new Intent(getApplicationContext(), DisplayActivity.class).putExtra("selection", selectedItem));          //starts the Display Activity
                break;
            }
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the bgmenu.xml to display the OptionsMenu in our activity
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.adds:
                startActivity(new Intent(getApplicationContext(), EditAddActivity.class).putExtra("flag", true));              //starts the EditAdd Activity to create a new count
                added = true;
                return true;
            case R.id.sort:
                arrayAdapter.sort(new Comparator<String>() {

                    public int compare(String lhs, String rhs) {            //to sort the students by Name
                        return lhs.compareTo(rhs);   //or whatever your sorting algorithm
                    }
                });
                arrayAdapter.notifyDataSetChanged();
                return true;
            case R.id.exit:
                finish();
                System.exit(0);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
