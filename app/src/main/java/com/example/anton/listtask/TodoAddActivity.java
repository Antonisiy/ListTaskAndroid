package com.example.anton.listtask;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class TodoAddActivity extends AppCompatActivity {

    private ArrayList<String> projects_title;
    private int Selected_Project_Id;
    private String Title_todo = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_add);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);

        Bundle b = this.getIntent().getExtras();
        projects_title = b.getStringArrayList("LIST_PROJECT");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.projectlist_cell);

        //final String[] projectsArray = {"Работа","Семья","Прочее"};

        adapter.addAll(projects_title);

        ListView ProjectList = (ListView) findViewById(R.id.ProjectList);

        ProjectList.setAdapter(adapter);

        ProjectList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                String selectedItem = projects_title.get(position);
                Selected_Project_Id = position + 1;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_todoadd, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.create_todo:
                create_todo();
                return true;
            case R.id.home:
                TodoAddActivity.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void create_todo()
    {
        EditText editText = (EditText) findViewById(R.id.editText);
        Title_todo = editText.getText().toString();

        JsonObject params = new JsonObject();

        params.addProperty("text", Title_todo);
        params.addProperty("isCompleted", false);
        params.addProperty("projectId", Selected_Project_Id);

        Ion.with(TodoAddActivity.this)
                .load("http://damp-falls-51925.herokuapp.com/projects/"+Selected_Project_Id+"/todos")
                .setJsonObjectBody(params)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Intent intent = new Intent();
                        intent.putExtra("text", Title_todo);
                        intent.putExtra("projectId", Selected_Project_Id);
                        setResult(RESULT_OK, intent);
                        TodoAddActivity.this.finish();
                    }
                });

    }

}
