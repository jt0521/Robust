package com.meituan.sample;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.meituan.robust.patch.RobustModify;
import com.meituan.robust.patch.annotaion.Modify;
import com.meituan.robust.patch.annotaion.Add;

import java.lang.reflect.Field;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    protected static String name = "SecondActivity";
    private ListView listView;
    private String[] multiArr = {"列表1", "列表2", "列表3", "列表4"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        listView = (ListView) findViewById(R.id.listview);
        TextView textView = (TextView) findViewById(R.id.secondtext);
        textView.setOnClickListener(v -> {
            //RobustModify.modify();
            Toast.makeText(this, "点我 点我", Toast.LENGTH_SHORT).show();
                }
        );
        //change text on the  SecondActivity
        textView.setText("info title: " + getTextInfo());

        //test array
        BaseAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, multiArr);
        listView.setAdapter(adapter);
        printLog("robust", new String[][]{new String[]{"1", "2", "3"}, new String[]{"4", "5", "6"}});
    }

    public String getTextInfo() {
        return "error fixed success ";
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {

        return super.onCreateView(name, context, attrs);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(SecondActivity.this, "from implements onclick ", Toast.LENGTH_SHORT).show();

    }

    public static Field getReflectField(String name, Object instance) throws NoSuchFieldException {
        for (Class<?> clazz = instance.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Field field = clazz.getDeclaredField(name);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                return field;
            } catch (NoSuchFieldException e) {
                // ignore and search next
            }
        }
        throw new NoSuchFieldException("Field " + name + " not found in " + instance.getClass());
    }

    public static Object getFieldValue(String name, Object instance) {
        try {
            return getReflectField(name, instance).get(instance);
        } catch (Exception e) {
            Log.d("robust", "getField error " + name + "   target   " + instance);
            e.printStackTrace();
        }
        return null;
    }

    private void printLog(@NonNull String tag, @NonNull String[][] args) {
        int i = 0;
        int j = 0;
        for (String[] array : args) {
            for (String arg : array) {
                Log.d(tag, "args[" + i + "][" + j + "] is: " + arg);
                j++;
            }
            i++;
        }
    }
}
