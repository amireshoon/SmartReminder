package ap.behrouzi.smartr.utils;

import android.util.Log;

import androidx.annotation.Nullable;

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

