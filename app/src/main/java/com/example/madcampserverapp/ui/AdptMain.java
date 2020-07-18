package com.example.madcampserverapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.madcampserverapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AdptMain extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<String> arrayGroup;
    private HashMap<String , ArrayList<String>> arrayChild;

    public AdptMain(Context context, ArrayList<String> arrayGroup, HashMap<String , ArrayList<String>> arrayChild){
        super();
        this.context=context;
        this.arrayGroup=arrayGroup;
        this.arrayChild=arrayChild;
    }

    @Override
    public int getGroupCount() {
        return arrayGroup.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return arrayChild.get(arrayGroup.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return arrayGroup.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return arrayChild.get(arrayGroup.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String groupName=arrayGroup.get(i);
        View v=view;
        if (v==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=(RelativeLayout)inflater.inflate(R.layout.lvi_group,null);
        }
        TextView textGroup=(TextView) v.findViewById(R.id.textGroup);
        textGroup.setText(groupName);
        return v;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        String childName=arrayChild.get(arrayGroup.get(i)).get(i1);
        View v=view;
        if (v==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=(RelativeLayout)inflater.inflate(R.layout.lvi_child,null);
        }
        TextView textChild=(TextView) v.findViewById(R.id.textChild);
        textChild.setText(childName);

        return v;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

/*    public boolean onChildClick(ExpandableListView parent, View v, int i, int i1, long id){

    }*/
}
