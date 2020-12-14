package ap.behrouzi.smartr.utils;

import android.app.Application;

import ir.map.sdk_map.Mapir;

public class ApplicationController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //TODO Please add your API_KEY
        Mapir.getInstance(this, "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjcyODk0NzJkZTc4ZjJhZjVmMmY3ZGU1OWQ2NzBiNGJlYzVhMDQyNmZjMDg2ZDIyODdjNjZjZDA3NDNjMzliZWE3NDE1Y2RiN2YwNjI5NTVhIn0.eyJhdWQiOiIxMTg5OSIsImp0aSI6IjcyODk0NzJkZTc4ZjJhZjVmMmY3ZGU1OWQ2NzBiNGJlYzVhMDQyNmZjMDg2ZDIyODdjNjZjZDA3NDNjMzliZWE3NDE1Y2RiN2YwNjI5NTVhIiwiaWF0IjoxNjA3OTQ1NzYwLCJuYmYiOjE2MDc5NDU3NjAsImV4cCI6MTYxMDQ1MTM2MCwic3ViIjoiIiwic2NvcGVzIjpbImJhc2ljIl19.MiDoNBYL0nrXAvaqSchgR1W-EJMhYIfQy00DMSaaR2DQJdQx3hIGccmvywQh5yYtJltIQl0TMxffNh0GoDDRAnehYn-t8YQM5ry-N8ERkv_v__5qeinRFNAq4L3-BLbPBDm4InL6X88OTh1qwkeicqiv05DYoQ96rHbVsf8S46LEwUkrepHr0W8Jq8SXoLOoIMg2vaSkA5ZRj_LdgTS0wHmo-1xTxm32vEEGhL6SJVyiQsTv7GjDi_esdeW2E02w0eHBzv8Ixq4jFwTxki3h-VkTWfXPaOXxtoL_hm4bwiKB-wEQhGsR2a9VfqyCJ3OE1eYeqxnd2NHANqZbmTBV7A");
    }
}
