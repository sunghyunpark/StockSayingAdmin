package util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.investmentkorea.android.admin.AdminApplication;
import com.investmentkorea.android.admin.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Util {

    public static void showToast(Context context, String message){
        //토스트를 중앙에 띄워준다.
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        View toastView = toast.getView();
        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
        toastMessage.setGravity(Gravity.CENTER);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    public static String parseTimeWithoutTime(String timeStr){
        SimpleDateFormat s = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault());
        s.setTimeZone(TimeZone.getDefault());
        try {
            return s.format(getDate(timeStr));
        }catch (NullPointerException e){
            return AdminApplication.TODAY_YEAR+"-"+AdminApplication.TODAY_MONTH+"-"+AdminApplication.TODAY_DAY;
        }
    }

    public static String parseTimeWithoutTimeByCustom(String timeStr){
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        s.setTimeZone(TimeZone.getDefault());
        try {
            return s.format(getDate(timeStr));
        }catch (NullPointerException e){
            return AdminApplication.TODAY_YEAR+"-"+AdminApplication.TODAY_MONTH+"-"+AdminApplication.TODAY_DAY;
        }
    }

    private static Date getDate(String dateStr) {
        SimpleDateFormat s;
        if (dateStr.endsWith("Z")) {
            s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'", Locale.getDefault());
            s.setTimeZone(TimeZone.getTimeZone("UTC"));
        } else {
            s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.getDefault());
        }
        try {
            return s.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
