package com.example.anton.listtask;

import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MainActivity extends AppCompatActivity {


    private CustomAdapter mAdapter;
    private ArrayList<Project> projects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("assets/fonts/OpenSans-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Load_Projects();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> projects_title = new ArrayList<>();
                for (Project item: projects) {
                    projects_title.add(item.getTitle());
                }

                Bundle b = new Bundle();
                b.putStringArrayList("LIST_PROJECT",projects_title);
                Intent intent = new Intent(MainActivity.this, TodoAddActivity.class);
                intent.putExtras(b);
                startActivityForResult(intent, 1);
            }
        });

   }


   public void Load_Projects()
   {
       Ion.with(this)

               .load(getString(R.string.kIndexRequest))

               .asJsonArray()

               .setCallback(new FutureCallback<JsonArray>() {

                   @Override
                   public void onCompleted(Exception e, JsonArray result) {

                       if (result != null) {

                           projects = new ArrayList<Project>();

                           for (final JsonElement projectJsonElement : result) {

                               projects.add(new Gson().fromJson(projectJsonElement, Project.class));

                           }

                           setViewList();
                       }

                   }

               });
   }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK)

        {
            Load_Projects();
            setViewList();
        }
    }


    public void setViewList()
    {
        mAdapter = new CustomAdapter(this);

        for (Project item: projects) {
            mAdapter.addSectionHeaderItem(item.getTitle());
            for ( Todo todo: item.getTodos() ) {
                mAdapter.addItem(todo);
            }

        }
        ListView ProjectList = (ListView) findViewById(R.id.TodosList);

        ProjectList.setAdapter(mAdapter);
    }
}