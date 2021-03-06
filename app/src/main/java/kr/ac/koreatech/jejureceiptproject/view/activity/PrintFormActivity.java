package kr.ac.koreatech.jejureceiptproject.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.print.PrintHelper;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.vipul.hp_hp.library.Layout_to_Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.ac.koreatech.jejureceiptproject.R;
import kr.ac.koreatech.jejureceiptproject.databinding.ActivityPrintFormBinding;
import kr.ac.koreatech.jejureceiptproject.domain.BasketDTO;
import kr.ac.koreatech.jejureceiptproject.domain.ReceiptDTO;
import kr.ac.koreatech.jejureceiptproject.util.MediaScanner;
import kr.ac.koreatech.jejureceiptproject.viewmodel.BasketRecyclerViewModel;
import kr.ac.koreatech.jejureceiptproject.viewmodel.MainActivityViewModel;
import kr.ac.koreatech.jejureceiptproject.viewmodel.PrintFormViewModel;

public class PrintFormActivity extends AppCompatActivity {
    private ActivityPrintFormBinding binding;
    private boolean one = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_print_form);
        ArrayList<ReceiptDTO> items = new ArrayList<>();
        int idx = 0;
        int total = 0, count = 0;
        for (BasketDTO item :
                BasketRecyclerViewModel.getInstance().getItems()) {
            items.add(new ReceiptDTO(++idx, item.getName(), item.getCount(), item.getPrice(), item.getTotal(), ""));
            total += item.getTotal();
            count += item.getCount();
        }
        binding.setViewModel(new PrintFormViewModel(count, total));

        for (int i = idx; i < 24; i++) {
            items.add(new ReceiptDTO(i + 1, "", null, null, null, ""));
        }
        binding.setDatas(items);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // ????????? ??????(????????????)
        getSupportActionBar().hide(); // ????????? ?????????
        one = false;
    }

    public String saveBitmapToJpg(Bitmap bitmap, String name) {
        /**
         * ?????? ??????????????? ???????????? ?????????????????? ???????????? ???????????????.
         *
         * @version target API 28 ??? API29????????? ????????? ?????????????????????.???
         * @param Bitmap bitmap - ??????????????? ?????? ???????????? ?????????
         * @param String fileName - ??????????????? ?????? ???????????? ?????????
         *
         * File storage = ????????? ??? ????????? ??????
         *
         * return = ????????? ???????????? ??????
         *
         * ???????????? ????????? ??????????????? ????????? ???????????? ?????????????????? ???????????????.
         * FileOutputStream?????? ?????????????????? ???????????? ??????????????????.
         */

        File storage = getCacheDir(); //  path = /data/user/0/YOUR_PACKAGE_NAME/cache
        String fileName = name + ".jpg";
        File imgFile = new File(storage, fileName);
        try {
            imgFile.createNewFile();
            FileOutputStream out = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, out); //???????????? ??????????????? ???????????? ????????????
            out.close();
        } catch (FileNotFoundException e) {
            Log.e("saveBitmapToJpg", "FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("saveBitmapToJpg", "IOException : " + e.getMessage());
        }
        Log.d("imgPath", getCacheDir() + "/" + fileName);
        return getCacheDir() + "/" + fileName;
    }


    public Bitmap createViewToBitmap(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((PrintFormActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        view.measure(view.getWidth(), view.getHeight());
        view.layout(0, 0, view.getWidth(), view.getHeight());
        view.buildDrawingCache();

        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        view.draw(canvas);
        return bitmap;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, new SimpleDateFormat("yyyy???MM???dd??? HH???mm???ss???").format(new Date()), null);
        return Uri.parse(path);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!one) {
            one = true;
            PrintHelper photoPrinter = new PrintHelper(this);
            photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
            photoPrinter.setColorMode(PrintHelper.COLOR_MODE_MONOCHROME);
            photoPrinter.setOrientation(PrintHelper.ORIENTATION_LANDSCAPE);
            View view = binding.linear;
            Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(bitmap);
            canvas.translate(view.getScrollX(), view.getScaleY());
            canvas.drawARGB(0, 0, 0, 0);
            view.draw(canvas);
            //??????????????? ?????????  ?????????????????? ???????????? ??????
            Intent intent23 = new Intent();
            ArrayList<Uri> imageUris = new ArrayList<>();
            imageUris.add(getImageUri(this, bitmap));
//            imageUris.add(getImageUri(this, bitmap));
            intent23.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent23.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
            intent23.setType("image/*");
            startActivity(Intent.createChooser(intent23, "?????????"));
            finish();
//            photoPrinter.printBitmap("droids.jpg - test print", bitmap);
        } else {
//            finish();
        }
    }
}