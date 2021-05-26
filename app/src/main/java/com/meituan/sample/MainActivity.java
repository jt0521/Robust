package com.meituan.sample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.meituan.robust.PatchExecutor;
import com.meituan.robust.patch.RobustModify;
import com.meituan.robust.patch.annotaion.Add;
import com.meituan.sample.patch.PatchManipulateImp;

/**
 * For users of Robust you may only to use MainActivity or SecondActivity,other classes are used for test.<br>
 * <br>
 * If you just want to use Robust ,we recommend you just focus on MainActivity SecondActivity and PatchManipulateImp.Especially three buttons in MainActivity<br>
 * <br>
 * in the MainActivity have three buttons; "SHOW TEXT " Button will change the text in the MainActivity,you can patch the show text.<br>
 * <br>
 * "PATCH" button will load the patch ,the patch path can be configured in PatchManipulateImp.<br>
 * <br>
 * "JUMP_SECOND_ACTIVITY" button will jump to the second ACTIVITY,so you can patch a Activity.<br>
 * <br>
 * Attention to this ,We recommend that one patch is just for one built apk ,because every  built apk has its unique mapping.txt and resource id<br>
 *
 * @author mivanzhang
 */

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);
        Button patch = (Button) findViewById(R.id.patch);
        //beigin to patch
        patch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkWriteSDCardPermission(MainActivity.this)) {
                    runRobust();
                } else {
                    requestWriteSDCardPermission(MainActivity.this, 1);
                }
            }
        });

        findViewById(R.id.jump_second_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RobustModify.modify();
                show();
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RobustModify.modify();
                Toast.makeText(MainActivity.this, "arrived 舒服点搜房电视里发生", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Add
    private void show() {
        Toast.makeText(MainActivity.this, "=========== ", Toast.LENGTH_SHORT).show();
    }

    private boolean isGrantSDCardReadPermission() {
        return PermissionUtils.isGrantSDCardReadPermission(this);
    }

    private void requestPermission() {
        PermissionUtils.requestSDCardReadPermission(this, REQUEST_CODE_SDCARD_READ);
    }

    private static final int REQUEST_CODE_SDCARD_READ = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_SDCARD_READ:
                handlePermissionResult();
                break;

            default:
                break;
        }
    }

    private void handlePermissionResult() {
        if (isGrantSDCardReadPermission()) {
            runRobust();
        } else {
            Toast.makeText(this, "failure because without sd card read permission", Toast.LENGTH_SHORT).show();
        }

    }

    private void runRobust() {
        new PatchExecutor(getApplicationContext(), new PatchManipulateImp(), new RobustCallBackSample()).start();
    }

    //请求读写外部存储
    public static void requestWriteSDCardPermission(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 先判断有没有权限
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                activity.startActivityForResult(intent, requestCode);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
        }
    }

    //检查是否允许读写外部存储
    public static boolean checkWriteSDCardPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
            if (activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
            return true;
        }
        return true;
    }
}
