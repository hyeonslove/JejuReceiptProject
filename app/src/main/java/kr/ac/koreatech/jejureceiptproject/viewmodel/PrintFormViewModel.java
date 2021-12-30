package kr.ac.koreatech.jejureceiptproject.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.databinding.ObservableField;
import androidx.print.PrintHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import kr.ac.koreatech.jejureceiptproject.view.activity.PrintFormActivity;

public class PrintFormViewModel {
    private static PrintFormViewModel instance;

    public static PrintFormViewModel getInstance() {
        if (instance == null)
            instance = new PrintFormViewModel();
        return instance;
    }

    private ObservableField<String> now_time = new ObservableField<>();
    private ObservableField<String> sum_count = new ObservableField<>();
    private ObservableField<String> sum_total = new ObservableField<>();


    public ObservableField<String> getNow_time() {
        return now_time;
    }

    public void setNow_time(String now_time) {
        this.now_time.set(now_time);
    }

    public ObservableField<String> getSum_count() {
        return sum_count;
    }

    public void setSum_count(String sum_count) {
        this.sum_count.set(sum_count);
    }

    public ObservableField<String> getSum_total() {
        return sum_total;
    }

    public void setSum_total(String sum_total) {
        this.sum_total.set(sum_total);
    }
}
