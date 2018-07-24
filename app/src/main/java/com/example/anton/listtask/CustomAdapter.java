package com.example.anton.listtask;

import java.util.ArrayList;
import java.util.TreeSet;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.MainThread;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class CustomAdapter extends BaseAdapter {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    //лежат либо строки (названия категорий) либо задачи(как объект Todo)
    private ArrayList<Object> mData = new ArrayList<Object>();

    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;

    private  Context context;
    public CustomAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    public void addItem(final Todo item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final String item) {
        mData.add(item);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    //в Todo переопределен метод toString, возвращая название задачи
    public String getItem(int position) {
        return mData.get(position).toString();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.snippet_item1, null);
                    holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkboxTodo);
                    holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            Todo todoInFocus = (Todo) buttonView.getTag();

                            if (todoInFocus.getCompleted() == isChecked) return;

                            todoInFocus.setCompleted(isChecked);

                            if (isChecked) {
                                buttonView.setPaintFlags(buttonView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            } else {
                                buttonView.setPaintFlags(buttonView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                            }

                            Gson gson = new Gson();
                            JsonObject json = new JsonObject();
                            json.add("todo",gson.toJsonTree(todoInFocus));

                            Ion.with(context)
                                    .load("http://damp-falls-51925.herokuapp.com/projects/"+todoInFocus.getProject_id()+"/todos/"+todoInFocus.id)
                                    .setJsonObjectBody(json)
                                    .asJsonObject()
                                    .setCallback(new FutureCallback<JsonObject>() {
                                        @Override
                                        public void onCompleted(Exception e, JsonObject result) {

                                        }
                                    });

                        }
                    });
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.snippet_item2, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.textSeparator);
                    break;
            }
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(holder.textView != null)
        {
            holder.textView.setText(mData.get(position).toString());
        }
        else
        {
            holder.checkBox.setTag((Todo)mData.get(position));
            holder.checkBox.setText( ((Todo) holder.checkBox.getTag()).getText());
            holder.checkBox.setChecked( ((Todo) holder.checkBox.getTag()).getCompleted());

            if (holder.checkBox.isChecked()) {
                holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        }

        return convertView;
    }

    public static class ViewHolder {
        public TextView textView;
        public CheckBox checkBox;
    }
}
