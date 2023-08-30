package com.example.project.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.project.Helper.MyDatabaseHelper;
import com.example.project.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.math.BigDecimal;
import com.example.project.Helper.ManagementCart;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private TextView textView;
    private PieChart pieChart1, pieChart2;
    private double selfEnergy;
    private double totalEnergy;
    private double remaining;
    private ManagementCart managementCart;
    private TextView totalCalorie;
    private BarChart barChart;
    private SeekBar seekBarX, seekBarY;
    private TextView tvX, tvY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        managementCart = new ManagementCart(this);
        setlistener();
        MyDatabaseHelper dbHelper;
        ArrayList<String> itemArray = new ArrayList<>();

        dbHelper = new MyDatabaseHelper(this, "info3.db", null, 1);
        dbHelper.getWritableDatabase();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query("Book", null, null, null, null, null, null);
        //调用moveToFirst()将数据指针移动到第一行的位置。
        if (cursor.moveToFirst()) {
            do {
                itemArray.add(cursor.getString(cursor.getColumnIndex("gender")));
                itemArray.add(cursor.getString(cursor.getColumnIndex("age")));
                itemArray.add(cursor.getString(cursor.getColumnIndex("high")));
                itemArray.add(cursor.getString(cursor.getColumnIndex("now_weight")));
                itemArray.add(cursor.getString(cursor.getColumnIndex("tar_weight")));
                itemArray.add(cursor.getString(cursor.getColumnIndex("target")));

            } while (cursor.moveToNext());
        }
        cursor.close();
        System.out.println("我的数据：" + itemArray);

        double remainEnergy;

        if (itemArray.get(5).equals("gain-muscle")) {
            System.out.println("MUSCLE");
            totalEnergy = buildMuscle(Double.parseDouble(itemArray.get(2)), Double.parseDouble(itemArray.get(3)), Double.parseDouble(itemArray.get(1)), itemArray.get(0));
        } else if (itemArray.get(5).equals("lose-fat")) {
            System.out.println("lose-fat");
            totalEnergy = fatReduction(Double.parseDouble(itemArray.get(2)), Double.parseDouble(itemArray.get(3)), Double.parseDouble(itemArray.get(1)), itemArray.get(0));
        } else if (itemArray.get(5).equals("keep")) {
            System.out.println("KEEP");
            totalEnergy = keepWeight(Double.parseDouble(itemArray.get(2)), Double.parseDouble(itemArray.get(3)), Double.parseDouble(itemArray.get(1)), itemArray.get(0));
        }


        double addEnergy = Math.round((managementCart.getTotalFee()) * 100.0) / 1000.0;
        System.out.println("TOTAL ENERGY: " + totalEnergy);

        totalCalorie = findViewById(R.id.totalCalorie);
        totalCalorie.setText(totalEnergy + " KCAL");

        System.out.println("ADD ENERGY: " + addEnergy);
        remainEnergy = totalEnergy - addEnergy;
        System.out.println("REMAIN ENERGY: " + remainEnergy);

        pieChart1 = (PieChart) findViewById(R.id.todayCalorie);
        pieChart1.setUsePercentValues(false);//设置value是否用显示百分数,默认为false
//        pieChart.setDescription("Your calorie intake today");//设置描述
//        pieChart.setDescriptionTextSize(20);//设置描述字体大小

        pieChart1.setExtraOffsets(5, 5, 5, 5);//设置饼状图距离上下左右的偏移量

        pieChart1.setDrawCenterText(true);//是否绘制中间的文字
        pieChart1.setCenterTextColor(Color.RED);//中间的文字颜色
        pieChart1.setCenterTextSize(15);//中间的文字字体大小

        pieChart1.setDrawHoleEnabled(true);//是否绘制饼状图中间的圆
        pieChart1.setHoleColor(Color.WHITE);//饼状图中间的圆的绘制颜色
        pieChart1.setHoleRadius(40f);//饼状图中间的圆的半径大小

        pieChart1.setTransparentCircleColor(Color.BLACK);//设置圆环的颜色
        pieChart1.setTransparentCircleAlpha(100);//设置圆环的透明度[0,255]
        pieChart1.setTransparentCircleRadius(40f);//设置圆环的半径值

        // enable rotation of the chart by touch
        pieChart1.setRotationEnabled(true);//设置饼状图是否可以旋转(默认为true)
        pieChart1.setRotationAngle(10);//设置饼状图旋转的角度

        pieChart1.setHighlightPerTapEnabled(true);//设置旋转的时候点中的tab是否高亮(默认为true)

        //右边小方框部分
//        Legend l = pieChart.getLegend(); //设置比例图
//        //l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);//设置每个tab的显示位置（这个位置是指下图右边小方框部分的位置 ）
////        l.setForm(Legend.LegendForm.LINE);  //设置比例图的形状，默认是方形
//        l.setXEntrySpace(0f);
//        l.setYEntrySpace(0f);//设置tab之间Y轴方向上的空白间距值
//        l.setYOffset(0f);

        //饼状图上字体的设置
        // entry label styling
        pieChart1.setDrawEntryLabels(true);//设置是否绘制Label
        pieChart1.setEntryLabelColor(Color.BLACK);//设置绘制Label的颜色
        pieChart1.setEntryLabelTextSize(10f);//设置绘制Label的字体大小

//       pieChart.setOnChartValueSelectedListener(this);//设值点击时候的回调
        pieChart1.animateY(3400, Easing.EaseInQuad);//设置Y轴上的绘制动画


        ArrayList<PieEntry> pieEntries = new ArrayList<PieEntry>();
        pieEntries.add(new PieEntry((float) remainEnergy, "REMAIN CALORIE (kcal)"));
        pieEntries.add(new PieEntry((float) addEnergy, "TODAY's CALORIE (kcal)"));
        //pieEntries.add(new PieEntry(30, "hello"));

        String centerText = "You can still eat";
        pieChart1.setCenterText(centerText);//设置圆环中间的文字
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        ArrayList<Integer> colors = new ArrayList<Integer>();

        // 饼图颜色
        colors.add(Color.rgb(205, 205, 205));
        colors.add(Color.rgb(114, 188, 223));
        //colors.add(Color.rgb(255, 123, 124));
//        colors.add(Color.rgb(57, 135, 200));
        pieDataSet.setColors(colors);

        pieDataSet.setSliceSpace(0f);//设置选中的Tab离两边的距离
        pieDataSet.setSelectionShift(5f);//设置选中的tab的多出来的
        PieData pieData = new PieData();
        pieData.setDataSet(pieDataSet);

        //各个饼状图所占比例数字的设置
        pieData.setValueFormatter(new PercentFormatter());//设置%
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.BLUE);

        pieChart1.setData(pieData);
        // undo all highlights
        pieChart1.highlightValues(null);
        pieChart1.invalidate();










        pieChart2 = (PieChart) findViewById(R.id.nutIntake);
        pieChart2.setUsePercentValues(false);//设置value是否用显示百分数,默认为false
//        pieChart.setDescription("Your calorie intake today");//设置描述
//        pieChart.setDescriptionTextSize(20);//设置描述字体大小

        pieChart2.setExtraOffsets(5, 5, 5, 5);//设置饼状图距离上下左右的偏移量

        pieChart2.setDrawCenterText(true);//是否绘制中间的文字
        pieChart2.setCenterTextColor(Color.RED);//中间的文字颜色
        pieChart2.setCenterTextSize(15);//中间的文字字体大小

        pieChart2.setDrawHoleEnabled(true);//是否绘制饼状图中间的圆
        pieChart2.setHoleColor(Color.WHITE);//饼状图中间的圆的绘制颜色
        pieChart2.setHoleRadius(40f);//饼状图中间的圆的半径大小

        pieChart2.setTransparentCircleColor(Color.BLACK);//设置圆环的颜色
        pieChart2.setTransparentCircleAlpha(100);//设置圆环的透明度[0,255]
        pieChart2.setTransparentCircleRadius(40f);//设置圆环的半径值

        // enable rotation of the chart by touch
        pieChart2.setRotationEnabled(true);//设置饼状图是否可以旋转(默认为true)
        pieChart2.setRotationAngle(10);//设置饼状图旋转的角度

        pieChart2.setHighlightPerTapEnabled(true);//设置旋转的时候点中的tab是否高亮(默认为true)

        //右边小方框部分
//        Legend l = pieChart.getLegend(); //设置比例图
//        //l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);//设置每个tab的显示位置（这个位置是指下图右边小方框部分的位置 ）
////        l.setForm(Legend.LegendForm.LINE);  //设置比例图的形状，默认是方形
//        l.setXEntrySpace(0f);
//        l.setYEntrySpace(0f);//设置tab之间Y轴方向上的空白间距值
//        l.setYOffset(0f);

        //饼状图上字体的设置
        // entry label styling
        pieChart2.setDrawEntryLabels(true);//设置是否绘制Label
        pieChart2.setEntryLabelColor(Color.BLACK);//设置绘制Label的颜色
        pieChart2.setEntryLabelTextSize(10f);//设置绘制Label的字体大小

//       pieChart.setOnChartValueSelectedListener(this);//设值点击时候的回调
        pieChart2.animateY(3400, Easing.EaseInQuad);//设置Y轴上的绘制动画


        ArrayList<PieEntry> pieEntries2 = new ArrayList<PieEntry>();
        pieEntries2.add(new PieEntry(3, "Carbon Hydrate"));
        pieEntries2.add(new PieEntry(3.5F, "Protein"));
        pieEntries2.add(new PieEntry(1, "Fat"));
        pieEntries2.add(new PieEntry(4, "cellulose"));
        pieEntries2.add(new PieEntry(0.5F, "Sugar"));
        //pieEntries.add(new PieEntry(30, "hello"));

        String centerText2 = "You can still eat";
        pieChart2.setCenterText(centerText2);//设置圆环中间的文字
        PieDataSet pieDataSet2 = new PieDataSet(pieEntries2, "");
        ArrayList<Integer> colors2 = new ArrayList<Integer>();

        // 饼图颜色
        colors2.add(Color.rgb(205, 205, 205));
        colors2.add(Color.rgb(114, 188, 223));
        colors2.add(Color.rgb(255, 123, 124));
        colors2.add(Color.rgb(57, 135, 200));
        colors2.add(Color.rgb(120, 75, 200));
        pieDataSet2.setColors(colors2);

        pieDataSet2.setSliceSpace(0f);//设置选中的Tab离两边的距离
        pieDataSet2.setSelectionShift(5f);//设置选中的tab的多出来的
        PieData pieData2 = new PieData();
        pieData2.setDataSet(pieDataSet2);

        //各个饼状图所占比例数字的设置
        pieData2.setValueFormatter(new PercentFormatter());//设置%
        pieData2.setValueTextSize(12f);
        pieData2.setValueTextColor(Color.BLUE);

        pieChart2.setData(pieData2);
        // undo all highlights
        pieChart2.highlightValues(null);
        pieChart2.invalidate();



    }

    private void setlistener() {
        textView = findViewById(R.id.back_home);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }


    //增肌计算 buildMuscle Function
    public double buildMuscle(double height, double weight, double age, String gender) {
        //double totalEnergy = 0;
        if (gender.equals("Male")) {
            selfEnergy = Math.round((66.5 + (13.8 * weight) + 5.0 * height - 6.8 * age) * 100) / 100;
            totalEnergy = Math.round((selfEnergy + 300) * 100) / 100;
        } else if (gender.equals("Female")) {
            selfEnergy = Math.round((665.1 + (9.6 * weight) + 1.8 * height - 4.7 * age) * 100) / 100;
            totalEnergy = Math.round((selfEnergy + 300) * 100) / 100;
        }
        return totalEnergy;
    }

    //减脂计算 FatReduction Function
    public double fatReduction(double height, double weight, double age, String gender) {
        //double totalEnergy = 0;
        if (gender.equals("Male")) {
            selfEnergy = Math.round((66.5 + (13.8 * weight) + 5.0 * height - 6.8 * age) * 100) / 100;
            totalEnergy = Math.round((selfEnergy - 300) * 100) / 100;
        } else if (gender.equals("Female")) {
            selfEnergy = Math.round((665.1 + (9.6 * weight) + 1.8 * height - 4.7 * age) * 100) / 100;
            totalEnergy = Math.round((selfEnergy - 300) * 100) / 100;
        }
        return totalEnergy;
    }

    //保持 Keep Function
    public double keepWeight(double height, double weight, double age, String gender) {
        //double totalEnergy = 0;
        if (gender.equals("Male")) {
            selfEnergy = Math.round((66.5 + (13.8 * weight) + 5.0 * height - 6.8 * age) * 100) / 100;
            totalEnergy = Math.round((selfEnergy) * 100) / 100;
        } else if (gender.equals("Female")) {
            selfEnergy = Math.round((665.1 + (9.6 * weight) + 1.8 * height - 4.7 * age) * 100) / 100;
            totalEnergy = Math.round((selfEnergy) * 100) / 100;
        }
        return totalEnergy;
    }

    private String getRemaining(double add, double total) {
        double remaining_1;
//        double remaining = 0;
        String info;
        if (add <= total) {
            remaining_1 = total - add;
            BigDecimal b = new BigDecimal(remaining_1);
            remaining = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            info = "您今天还可以摄入" + remaining + "大卡的食物。";
        } else {
            remaining_1 = add - total;
            BigDecimal b = new BigDecimal(remaining_1);
            remaining = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            info = "警告！！您今天摄入量超标了" + remaining + "大卡！！";
        }
        return info;
    }

}