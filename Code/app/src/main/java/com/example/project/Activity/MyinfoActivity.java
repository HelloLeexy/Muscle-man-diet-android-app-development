package com.example.project.Activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.BitmapUtils;
import com.example.CameraUtils;
import com.example.SPUtils;
import com.example.project.Helper.MyDatabaseHelper;
import com.example.project.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MyinfoActivity extends AppCompatActivity {
    ContentValues values = new ContentValues();
    private MyDatabaseHelper dbHelper;
    private PopupWindow popupWindow;
    // picture
//权限请求
    private RxPermissions rxPermissions;
    //是否拥有权限
    private boolean hasPermissions = false;
    //底部弹窗
    private BottomSheetDialog bottomSheetDialog;
    //弹窗视图
    private View bottomView;
    //存储拍完照后的图片
    private File outputImagePath;
    //启动相机标识
    public static final int TAKE_PHOTO = 1;
    //启动相册标识
    public static final int SELECT_PHOTO = 2;
    //图片控件
    private ShapeableImageView ivHead;
    //Base64
    private String base64Pic;
    //拍照和相册获取图片的Bitmap
    private Bitmap orc_bitmap;

    //Glide请求图片选项配置
    private RequestOptions requestOptions = RequestOptions.circleCropTransform()
            .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
            .skipMemoryCache(true);//不做内存缓存


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfo);
        dbHelper = new MyDatabaseHelper(this, "info3.db", null, 1);
        dbHelper.getWritableDatabase();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //指明去查询Book表。
        Cursor cursor = db.query("Book", null, null, null, null, null, null);
////        调用moveToFirst()将数据指针移动到第一行的位置。
        if (cursor.moveToFirst()) {
            do {
                Button bt_gend = (Button) findViewById(R.id.bt_gend);
                bt_gend.setText(cursor.getString(cursor.getColumnIndex("gender")));
                Button age = (Button) findViewById(R.id.button2);
                age.setText(cursor.getString(cursor.getColumnIndex("age")));
                Button high = (Button) findViewById(R.id.button3);
                high.setText(cursor.getString(cursor.getColumnIndex("high")));
                Button now_wight = (Button) findViewById(R.id.button4);
                now_wight.setText(cursor.getString(cursor.getColumnIndex("now_weight")));
                Button tar_weight = (Button) findViewById(R.id.button5);
                tar_weight.setText(cursor.getString(cursor.getColumnIndex("tar_weight")));
                Button tar = (Button) findViewById(R.id.button6);
                tar.setText(cursor.getString(cursor.getColumnIndex("target")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        ivHead = findViewById(R.id.ri_portrait);
//        检查版本
        checkVersion();
        //取出缓存
        String imageUrl = SPUtils.getString("imageUrl", null, this);
        //System.out.printf(imageUrl);
        if (imageUrl != null) {
            Glide.with(this).load(imageUrl).apply(requestOptions).into(ivHead);
        }
    }

    private void checkVersion() {
        //Android6.0及以上版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //如果你是在Fragment中，则把this换成getActivity()
            rxPermissions = new RxPermissions(this);
            //权限请求
            rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {//申请成功
                            showMsg("Got it");
                            hasPermissions = true;
                        } else {//申请失败
                            showMsg("Fail open");
                            hasPermissions = false;
                        }
                    });
        } else {
            //Android6.0以下
            showMsg("No need for authority");
        }
    }

    public void changeAvatar(View view) {
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomView = getLayoutInflater().inflate(R.layout.dialog_bottom, null);
        bottomSheetDialog.setContentView(bottomView);
        bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT);
        TextView tvTakePictures = bottomView.findViewById(R.id.tv_take_pictures);
        TextView tvOpenAlbum = bottomView.findViewById(R.id.tv_open_album);
        TextView tvCancel = bottomView.findViewById(R.id.tv_cancel);

        //拍照
        tvTakePictures.setOnClickListener(v -> {
            takePhoto();
            showMsg("Take photos");
            bottomSheetDialog.cancel();
        });
        //打开相册
        tvOpenAlbum.setOnClickListener(v -> {
            openAlbum();
            showMsg("Open Gallery");
            bottomSheetDialog.cancel();
        });
        //取消
        tvCancel.setOnClickListener(v -> {
            bottomSheetDialog.cancel();
        });
        //底部弹窗显示
        bottomSheetDialog.show();
    }

    //拍照
    private void takePhoto() {
        if (!hasPermissions) {
            showMsg("");
            checkVersion();
            return;
        }
        SimpleDateFormat timeStampFormat = new SimpleDateFormat(
                "yyyy_MM_dd_HH_mm_ss");
        String filename = timeStampFormat.format(new Date());
        outputImagePath = new File(getExternalCacheDir(),
                filename + ".jpg");
        Intent takePhotoIntent = CameraUtils.getTakePhotoIntent(this, outputImagePath);
        // 开启一个带有返回值的Activity，请求码为TAKE_PHOTO
        startActivityForResult(takePhotoIntent, TAKE_PHOTO);
    }

    //打开相册
    private void openAlbum() {
        if (!hasPermissions) {
            showMsg("No authority");
            checkVersion();
            return;
        }
        startActivityForResult(CameraUtils.getSelectPhotoIntent(), SELECT_PHOTO);

    }

    public void gender(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your gender");
        final String[] items = new String[]{"Male", "Female"};
        builder.setSingleChoiceItems(items, -1, new OnClickListener() {
            //which指的是用户选择的条目的下标
            //dialog:触发这个方法的对话框
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MyinfoActivity.this, "What you choose:" + items[which], Toast.LENGTH_SHORT).show();
                dialog.dismiss();//当用户选择了一个值后，对话框消失
                Button bt_gend = (Button) findViewById(R.id.bt_gend);
                bt_gend.setText(items[which]);
                values.put("gender", items[which]);
            }
        });
        builder.show();//这样也是一个show对话框的方式，上面那个也可以
    }

    public void age(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your age");
        String[] items = new String[88];
        int index=0;
        for (int i=12;i<100;i++){
            items[index] = Integer.toString(i);
            index++;
        }
        //final String[] items = new String[]{"12","13","14","15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34","35","36","37","38","39"};
        builder.setSingleChoiceItems(items, -1, new OnClickListener() {
            //which指的是用户选择的条目的下标
            //dialog:触发这个方法的对话框
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MyinfoActivity.this, "What you choose:" + items[which], Toast.LENGTH_SHORT).show();
                dialog.dismiss();//当用户选择了一个值后，对话框消失
                Button bt_gend = (Button) findViewById(R.id.button2);
                bt_gend.setText(items[which]);
                values.put("age", items[which]);
            }
        });
        builder.show();//这样也是一个show对话框的方式，上面那个也可以

    }

    public void high(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your height(cm)");
        final String[] items = new String[]{"120", "121", "122", "123", "124", "125", "126", "127", "128", "129", "130", "131", "132", "133", "134", "135", "136", "137", "138", "139", "140", "141", "142", "143", "144", "145", "146", "147", "148", "149", "150", "151", "152", "153", "154", "155", "156", "157", "158", "159", "160", "161", "162", "163", "164", "165", "166", "167", "168", "169", "170", "171", "172", "173", "174", "175", "176", "177", "178", "179", "180", "181", "182", "183", "184", "185", "186", "187", "188", "189", "190", "191", "192", "193", "194", "195", "196", "197", "198", "199", "200", "201", "202", "203", "204", "205", "206", "207", "208", "209", "210", "211", "212", "213", "214", "215", "216", "217", "218", "219",};
        builder.setSingleChoiceItems(items, -1, new OnClickListener() {
            //which指的是用户选择的条目的下标
            //dialog:触发这个方法的对话框
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MyinfoActivity.this, "What you choose:" + items[which], Toast.LENGTH_SHORT).show();
                dialog.dismiss();//当用户选择了一个值后，对话框消失
                Button bt_gend = (Button) findViewById(R.id.button3);
                bt_gend.setText(items[which] + " CM");
                values.put("high", items[which]);
            }
        });
        builder.show();//这样也是一个show对话框的方式，上面那个也可以
    }

    public void now_weight(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your weight(KG)");
        final String[] items = new String[]{"40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "100", "101", "102", "103", "104", "105", "106", "107", "108", "109", "110", "111", "112", "113", "114", "115", "116", "117", "118", "119", "120", "121", "122", "123", "124", "125", "126", "127", "128", "129", "130", "131", "132", "133", "134", "135", "136", "137", "138", "139"};
        builder.setSingleChoiceItems(items, -1, new OnClickListener() {
            //which指的是用户选择的条目的下标
            //dialog:触发这个方法的对话框
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MyinfoActivity.this, "What you choose:" + items[which], Toast.LENGTH_SHORT).show();
                dialog.dismiss();//当用户选择了一个值后，对话框消失
                Button bt_gend = (Button) findViewById(R.id.button4);
                bt_gend.setText(items[which] + " KG");
                values.put("now_weight", items[which]);
            }
        });
        builder.show();//这样也是一个show对话框的方式，上面那个也可以
    }

    public void target_weight(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your target weight(KG)");
        final String[] items = new String[]{"40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "100", "101", "102", "103", "104", "105", "106", "107", "108", "109", "110", "111", "112", "113", "114", "115", "116", "117", "118", "119", "120", "121", "122", "123", "124", "125", "126", "127", "128", "129", "130", "131", "132", "133", "134", "135", "136", "137", "138", "139"};
        builder.setSingleChoiceItems(items, -1, new OnClickListener() {
            //which指的是用户选择的条目的下标
            //dialog:触发这个方法的对话框
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MyinfoActivity.this, "What you choose:" + items[which], Toast.LENGTH_SHORT).show();
                dialog.dismiss();//当用户选择了一个值后，对话框消失
                Button bt_gend = (Button) findViewById(R.id.button5);
                bt_gend.setText(items[which] + " KG");
                values.put("tar_weight", items[which]);
            }
        });
        builder.show();//这样也是一个show对话框的方式，上面那个也可以

    }

    public void bt_target(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your target");
        final String[] items = new String[]{"gain-muscle", "lose-fat", "keep"};
        builder.setSingleChoiceItems(items, -1, new OnClickListener() {
            //which指的是用户选择的条目的下标
            //dialog:触发这个方法的对话框
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MyinfoActivity.this, "What you choose:" + items[which], Toast.LENGTH_SHORT).show();
                dialog.dismiss();//当用户选择了一个值后，对话框消失
                Button bt_gend = (Button) findViewById(R.id.button6);
                bt_gend.setText(items[which]);
                values.put("target", items[which]);
            }
        });
        builder.show();//这样也是一个show对话框的方式，上面那个也可以

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //拍照后返回
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //显示图片
                    displayImage(outputImagePath.getAbsolutePath());
                }
                break;
            //打开相册后返回
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    String imagePath = null;
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                        //4.4及以上系统使用这个方法处理图片
                        imagePath = CameraUtils.getImageOnKitKatPath(data, this);
                    } else {
                        imagePath = CameraUtils.getImageBeforeKitKatPath(data, this);
                    }
                    //显示图片
                    displayImage(imagePath);
                }
                break;
            default:
                break;
        }
    }

    private void displayImage(String imagePath) {
        if (!TextUtils.isEmpty(imagePath)) {

            //放入缓存
            SPUtils.putString("imageUrl", imagePath, this);

            //显示图片
            Glide.with(this).load(imagePath).apply(requestOptions).into(ivHead);

            //压缩图片
            orc_bitmap = CameraUtils.compression(BitmapFactory.decodeFile(imagePath));
            //转Base64
            base64Pic = BitmapUtils.bitmapToBase64(orc_bitmap);

        } else {
            showMsg("Fail to get images");
        }
    }


    public void save(View view) throws IOException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert("Book", null, values);
        values.clear();
        Intent intent = new Intent(MyinfoActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}







