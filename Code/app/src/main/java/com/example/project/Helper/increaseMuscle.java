package com.example.project.Helper;

import java.math.BigDecimal;

public class increaseMuscle {
    private double selfEnergy;
    private double totalEnergy;
    private double remaining;


    //计算BMI指数 Calculate BMI
    private double getBMI(double height, double weight) {
        double height_1 = height / 100;
        double square = Math.pow(height_1, 2);
        double BMI_1 = weight / square;
        BigDecimal b = new BigDecimal(BMI_1);
        double BMI = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return BMI;
    }

    //增肌计算 IncreaseMuscle Function
    private double increaseMuscle(double height, double weight, double age, String gender) {
        //double totalEnergy = 0;
        if (gender.equals("男性")) {
            selfEnergy = Math.round((66.5 + (13.8 * weight) + 5.0 * height - 6.8 * age) * 100) / 100;
            totalEnergy = Math.round((selfEnergy + 300) * 100) / 100;
        } else if (gender.equals("女性")) {
            selfEnergy = Math.round((665.1 + (9.6 * weight) + 1.8 * height - 4.7 * age) * 100) / 100;
            totalEnergy = Math.round((selfEnergy + 300) * 100) / 100;
        }
        return totalEnergy;
    }

    //减脂计算 FatReduction Function
    private double fatReduction(double height, double weight, double age, String gender) {
        //double totalEnergy = 0;
        if (gender.equals("男性")) {
            selfEnergy = Math.round((66.5 + (13.8 * weight) + 5.0 * height - 6.8 * age) * 100) / 100;
            totalEnergy = Math.round((selfEnergy - 300) * 100) / 100;
        } else if (gender.equals("女性")) {
            selfEnergy = Math.round((665.1 + (9.6 * weight) + 1.8 * height - 4.7 * age) * 100) / 100;
            totalEnergy = Math.round((selfEnergy - 300) * 100) / 100;
        }
        return totalEnergy;
    }

    //Calculate dailyRemaining
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
