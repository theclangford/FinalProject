package group.finalproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import group.finalproject.movie.AsyncTaskLoadImage;
import group.finalproject.movie.DataBaseHelper;

public class FullScreenImageActivity extends AppCompatActivity {

    private ImageView imageView = null;
    private String fullScreenInd;
    DataBaseHelper mDataBaseHelper = new DataBaseHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        Toolbar tb = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        imageView = findViewById(R.id.imageView);
        if(getIntent().getStringExtra("Activity_Source").equals("MovieActivity")){
            new AsyncTaskLoadImage(imageView).execute(getIntent().getStringExtra("poster"));
        }else {
            byte[] imgByte = mDataBaseHelper.getImage_fromDB(getIntent().getStringExtra("Title"));

            imageView.setImageBitmap(mDataBaseHelper.getImage(imgByte));
        }


        fullScreenInd = getIntent().getStringExtra("fullScreenIndicator");
        if ("y".equals(fullScreenInd)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //getSupportActionBar().hide();

            imageView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            imageView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            imageView.setAdjustViewBounds(false);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }



    }
}
