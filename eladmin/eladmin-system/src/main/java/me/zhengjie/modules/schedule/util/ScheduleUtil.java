package me.zhengjie.modules.schedule.util;

import me.zhengjie.modules.schedule.domain.Shift;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class ScheduleUtil {

    public static boolean isTimeOverlap(Time start1, Time end1, Time start2, Time end2) {
        long s1 = start1.getTime();
        long e1 = end1.getTime();
        long s2 = start2.getTime();
        long e2 = end2.getTime();
        
        if (e1 < s1) e1 += 24 * 60 * 60 * 1000;
        if (e2 < s2) e2 += 24 * 60 * 60 * 1000;
        
        return !(e1 <= s2 || e2 <= s1);
    }

    public static List<String> validateShift(Shift shift) {
        List<String> errors = new ArrayList<>();
        
        if (shift.getStartTime() == null) {
            errors.add("开始时间不能为空");
        }
        if (shift.getEndTime() == null) {
            errors.add("结束时间不能为空");
        }
        
        if (shift.getStartTime() != null && shift.getEndTime() != null) {
            if (shift.getBreakStartTime() != null && shift.getBreakEndTime() != null) {
                if (!isTimeOverlap(shift.getStartTime(), shift.getEndTime(), 
                    shift.getBreakStartTime(), shift.getBreakEndTime())) {
                    errors.add("休息时间不在工作时间内");
                }
            }
        }
        
        return errors;
    }

    public static String getShiftTypeDisplayName(String shiftType) {
        switch (shiftType) {
            case "早班":
                return "早班";
            case "晚班":
                return "晚班";
            case "夜班":
                return "夜班";
            case "弹性班":
                return "弹性班";
            default:
                return shiftType;
        }
    }

    public static String getRuleTypeDisplayName(String ruleType) {
        switch (ruleType) {
            case "A/B轮班":
                return "A/B轮班";
            case "三班倒":
                return "三班倒";
            case "四班三倒":
                return "四班三倒";
            case "自定义":
                return "自定义";
            default:
                return ruleType;
        }
    }
}
