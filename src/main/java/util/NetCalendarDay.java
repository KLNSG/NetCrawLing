package util;

import java.util.Date;

/**
 * @author ：Lxin
 * @date ：Created in 2020/9/23 15:30
 */
public class NetCalendarDay {
    private Date solarDate;//日期
    private String solarDay;//日
    private boolean isVacation;//是否节假
    private String VacationName;//节假名称
    private boolean isWorkFlag = false;//是否上班
    private boolean isSaturday = false;//是否周六
    private boolean isSunday = false;//是否周日

    public Date getSolarDate() {
        return solarDate;
    }

    public void setSolarDate(Date solarDate) {
        this.solarDate = solarDate;
    }

    public String getSolarDay() {
        return solarDay;
    }

    public void setSolarDay(String solarDay) {
        this.solarDay = solarDay;
    }

    public boolean isVacation() {
        return isVacation;
    }

    public void setVacation(boolean vacation) {
        isVacation = vacation;
    }

    public String getVacationName() {
        return VacationName;
    }

    public void setVacationName(String vacationName) {
        VacationName = vacationName;
    }

    public boolean isWorkFlag() {
        return isWorkFlag;
    }

    public void setWorkFlag(boolean workFlag) {
        isWorkFlag = workFlag;
    }

    public boolean isSaturday() {
        return isSaturday;
    }

    public void setSaturday(boolean saturday) {
        isSaturday = saturday;
    }

    public boolean isSunday() {
        return isSunday;
    }

    public void setSunday(boolean sunday) {
        isSunday = sunday;
    }
}
