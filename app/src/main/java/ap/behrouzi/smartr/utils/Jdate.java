package ap.behrouzi.smartr.utils;

import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Calendar;

/**
 * Jdate Provided by Amirhossein Meydani
 * @license MIT LICENSE
 */
public class Jdate {

    public static DateFormat gregorian_to_jalali(int gy, int gm, int gd, boolean b) {
        int days, jm, jd;
        {
            int gy2 = (gm > 2) ? (gy + 1) : gy;
            int[] g_d_m = { 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334 };
            days = 355666 + (365 * gy) + (gy2 + 3) / 4 - (gy2 + 99) / 100 + (gy2 + 399) / 400 + gd + g_d_m[gm - 1];
        }
        int jy = -1595 + (33 * (days / 12053));
        days %= 12053;
        jy += 4 * (days / 1461);
        days %= 1461;
        if (days > 365) {
            jy += (days - 1) / 365;
            days = (days - 1) % 365;
        }
        if (days < 186) {
            jm = 1 + (days / 31);
            jd = 1 + (days % 31);
        } else {
            jm = 7 + ((days - 186) / 30);
            jd = 1 + ((days - 186) % 30);
        }
        String[] bd = new String[3];
        int[] dates ={ jy, jm, jd };
        if (b) {
            for (int l = 0; l <= dates.length - 1;l++) {
                if (dates[l] <= 9) {
                    bd[l] = "0" + dates[l];
                }else {
                    bd[l] = String.valueOf(dates[l]);
                }
            }
        }
        return new DateFormat(dates[0], dates[1], dates[2], bd[0], bd[1], bd[2], b);
    }

    public static String getDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case 0:
                return "shanbeh";
            case 1:
                return "1shanbeh";
            case 2:
                return "2shanbeh";
            case 3:
                return "3shanbeh";
            case 4:
                return "4shanbeh";
            case 5:
                return "5shanbeh";
            case 6:
                return "jome";
            default:
                return "shanbeh";
        }
    }

    public static String getDayOfWeek(int preferDay) {
        switch (preferDay) {
            case 1:
                return "1shanbeh";
            case 2:
                return "2shanbeh";
            case 3:
                return "3shanbeh";
            case 4:
                return "4shanbeh";
            case 5:
                return "5shanbeh";
            case 6:
                return "jome";
            default:
                return "shanbeh";
        }
    }

    public static int getDayOfWeek(String preferDay) {
        switch (preferDay) {
            case "1shanbeh":
                return 1;
            case "2shanbeh":
                return 2;
            case "3shanbeh":
                return 3;
            case "4shanbeh":
                return 4;
            case "5shanbeh":
                return 5;
            case "jome":
                return 6;
            default:
                return 7;
        }
    }

    public static int DAY_OF_WEEK() {
        Calendar calendar = Calendar.getInstance();
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
                return 5;
            case 5:
                return 6;
            case 6:
                return 7;
            default:
                return 0;
        }
    }

    public static DateFormat jalali_to_gregorian(int jy, int jm, int jd, @Nullable boolean beautify) {
        jy += 1595;
        int days = -355668 + (365 * jy) + (jy / 33 * 8) + ((jy % 33) + 3) / 4 + jd + ((jm < 7) ? (jm - 1) * 31 : ((jm - 7) * 30) + 186);
        int gy = 400 * (days / 146097);
        days %= 146097;
        if (days > 36524) {
            gy += 100 * (--days / 36524);
            days %= 36524;
            if (days >= 365)
                days++;
        }
        gy += 4 * (days / 1461);
        days %= 1461;
        if (days > 365) {
            gy += (days - 1) / 365;
            days = (days - 1) % 365;
        }
        int gm, gd = days + 1;
        {
            int[] sal_a = { 0, 31, ((gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0)) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
            for (gm = 0; gm < 13 && gd > sal_a[gm]; gm++) gd -= sal_a[gm];
        }
        String[] beautifyDates = new String[3];
        int[] dates = { gy, gm + 1, gd };
        if (beautify) {
            for (int l = 0; l <= dates.length - 1;l++) {
                if (dates[l] <= 9) {
                    beautifyDates[l] = "0" + dates[l];
                }else {
                    beautifyDates[l] = String.valueOf(dates[l]);
                }
            }
        }
        return new DateFormat(dates[0], dates[1], dates[2], beautifyDates[0], beautifyDates[1], beautifyDates[2], beautify);
    }

    public static class DateFormat {

        private final int iYear;
        private final int iMonth;
        private final int iDay;

        private final String sYear;
        private final String sMonth;
        private final String sDay;

        private boolean beautify;

        public DateFormat(int iYear, int iMonth, int iDay, String sYear, String sMonth, String sDay, boolean beautify) {
            this.beautify = beautify;
            this.iYear = iYear;
            this.iMonth = iMonth;
            this.iDay = iDay;
            this.sYear = sYear;
            this.sMonth = sMonth;
            this.sDay = sDay;
        }

        public int getiYear() {
            return iYear;
        }

        public int getiMonth() {
            return iMonth;
        }

        public int getiDay() {
            return iDay;
        }

        public String getsYear() {
            return sYear;
        }

        public String getsMonth() {
            return sMonth;
        }

        public String getsDay() {
            return sDay;
        }

        public boolean isBeautify() {
            return beautify;
        }

    }
}

