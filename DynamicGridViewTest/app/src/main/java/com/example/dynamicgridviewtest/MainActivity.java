package com.example.dynamicgridviewtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import org.askerov.dynamicgrid.DynamicGridView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";

    private DynamicGridView gridView;
    private Button btn1, btn2, btn3;
    private int editMode = 0;

    private MyDynamicGridAdapter myDynamicGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = (Button) findViewById(R.id.editmode_btn);
        btn2 = (Button) findViewById(R.id.add_btn);
        btn3 = (Button) findViewById(R.id.delete_btn);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);

        gridView = (DynamicGridView) findViewById(R.id.dynamic_grid);

        myDynamicGridAdapter = new MyDynamicGridAdapter(this,
                new ArrayList<String>(Arrays.asList(Data.sCheeseStrings)),
                4);
        gridView.setAdapter(myDynamicGridAdapter);

        gridView.setOnDragListener(new DynamicGridView.OnDragListener() {
            @Override
            public void onDragStarted(int position) {

                Log.d(TAG, "drag started at position " + position);

            }

            @Override
            public void onDragPositionsChanged(int oldPosition, int newPosition) {

                Log.d(TAG, String.format("drag item position changed from %d to %d", oldPosition, newPosition));

            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // 开始编辑模式
                gridView.startEditMode(position);

                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, parent.getAdapter().getItem(position).toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // add callback to stop edit mode if needed
//        gridView.setOnDropListener(new DynamicGridView.OnDropListener()
//        {
//            @Override
//            public void onActionDrop()
//            {
//                gridView.stopEditMode();
//            }
//        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editmode_btn:
                if (editMode == 0) {
                    gridView.startEditMode();
                    editMode = 1;
                    Toast.makeText(this, "gridView.startEditMode()", Toast.LENGTH_SHORT).show();
                } else {
                    gridView.stopEditMode();
                    editMode = 0;
                    Toast.makeText(this, "gridView.stopEditMode()", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.add_btn:
                myDynamicGridAdapter.add("999");
                Toast.makeText(this, "myDynamicGridAdapter.add(\"999\")", Toast.LENGTH_SHORT).show();
                break;

            case R.id.delete_btn:
                myDynamicGridAdapter.remove("999");
                break;
        }
    }
}
