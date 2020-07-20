package com.example.madcampserverapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madcampserverapp.MainActivity;
import com.example.madcampserverapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class BeforeActivity extends Activity {
    EditText editText;
    private String phonenumber;
    ExpandableListView eplist;
    private ArrayList<String> arrayLocation=new ArrayList<>();
    private HashMap<String,ArrayList<String>> arrayChild = new HashMap<>();
    Button button;
    private String location;
    private String location1;
    private String location2;
    TextView textView1;
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.before_main);




        eplist=(ExpandableListView) this.findViewById(R.id.expandable_listview);
        setArrayData();
        eplist.setAdapter(new AdptMain(this,arrayLocation,arrayChild));

        textView1=(TextView) findViewById(R.id.location_result);
        textView2=(TextView) findViewById(R.id.location_result2);

        /*Group click event*/
        eplist.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                int groupCount=(int) expandableListView.getExpandableListAdapter().getGroupCount();
                int childCount = (int) expandableListView.getExpandableListAdapter().getChildrenCount(i);
                textView1.setText(arrayLocation.get(i));
                location1=arrayLocation.get(i);
                return false;
            }
        });

        /*Child click event*/
        eplist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                textView2.setText(arrayChild.get(arrayLocation.get(i)).get(i1));
                location2=arrayChild.get(arrayLocation.get(i)).get(i1);
                return false;
            }
        });

        button=(Button) findViewById(R.id.before_btn);
        button.setOnClickListener(new Button.OnClickListener(){

            /*intent로 phonenumber, location 넘김*/
            @Override
            public void onClick(View view) {
                /*phonenumber에 editText값 전달*/
                editText=(EditText)findViewById(R.id.input_phonenumber);
                phonenumber=editText.getText().toString();
                location=location1+" "+location2;
                Intent intent1;
                intent1=new Intent(getApplicationContext(), MainActivity.class);
                intent1.putExtra("phonenumber",phonenumber);
                intent1.putExtra("location",location);
                startActivity(intent1);
            }
        });

    }
    private void setArrayData(){
        arrayLocation.add("서울");
        arrayLocation.add("대전");

        ArrayList<String> arraySeoulLocation = new ArrayList<String>();
        arraySeoulLocation.add("서초구");
        arraySeoulLocation.add("동작구");
        arraySeoulLocation.add("노원구");

        ArrayList<String > arrayDaejeonLocation = new ArrayList<String>();
        arrayDaejeonLocation.add("유성구");
        arrayDaejeonLocation.add("ㅇㅇ구");
        arrayDaejeonLocation.add("ㅁㅁ구");

        arrayChild.put(arrayLocation.get(0),arraySeoulLocation);
        arrayChild.put(arrayLocation.get(1),arrayDaejeonLocation);
    }
}
