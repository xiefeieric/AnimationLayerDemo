package uk.me.feixie.basketdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jauker.widget.BadgeView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvMain;
    private String[] mNames;
    private ImageView ivBasket;
    private ViewGroup mViewGroup;
    private int basketCount;
    private BadgeView mBadgeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initViews();
    }

    private void initData() {
        mNames = new String[20];
        for (int i = 0; i < 20; i++) {
            mNames[i] = "Item Name "+i;
        }
    }

    private void initViews() {

        rvMain = (RecyclerView) findViewById(R.id.rvMain);
        if (rvMain!=null) {
            rvMain.setHasFixedSize(true);
        }
        rvMain.setLayoutManager(new LinearLayoutManager(this));
        rvMain.setAdapter(new MyAdapter());

        ivBasket = (ImageView) findViewById(R.id.ivBasket);
        mBadgeView = new BadgeView(this);
        mBadgeView.setTargetView(ivBasket);
//        badgeView.setBadgeCount(20);
    }

    /*******************Recycler View Adapter**************************/

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvItemName;
        private Button btnBuy;
        private CardView cvItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvItemName = (TextView) itemView.findViewById(R.id.tvItemName);
            cvItem = (CardView) itemView.findViewById(R.id.cvItem);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = 20;
            cvItem.setLayoutParams(layoutParams);

            btnBuy = (Button) itemView.findViewById(R.id.btnBuy);
            btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int[] startPosition = new int[2];
                    v.getLocationInWindow(startPosition);
//                    System.out.println(startPosition[0]+";"+startPosition[1]);

                    ImageView imageView = new ImageView(MainActivity.this);
                    imageView.setImageResource(R.drawable.icon_red_ball);
                    startAnim(imageView,startPosition);
                }
            });
        }
    }

    private void startAnim(final ImageView imageView, int[] startPosition) {
        mViewGroup = null;
        mViewGroup = (ViewGroup) getWindow().getDecorView();
        //create anim view layer
        LinearLayout animView = createAnimView();
        mViewGroup.addView(animView);
        //add image red ball to anim layer
        View view = addBallImage(animView,imageView,startPosition);
        //define end position
        int[] endPosition = new int[2];
        ivBasket.getLocationInWindow(endPosition);

        //calculate offset distance
        int offsetX = endPosition[0]-startPosition[0];
        int offsetY = endPosition[1] - startPosition[1];

        TranslateAnimation translateAnimationX = new TranslateAnimation(0,offsetX,0,0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0,0,0,offsetY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);
        translateAnimationY.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.addAnimation(translateAnimationX);
        set.addAnimation(translateAnimationY);
        set.setDuration(800);
        imageView.startAnimation(set);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                imageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.setVisibility(View.GONE);
                basketCount++;
                mBadgeView.setBadgeCount(basketCount);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private View addBallImage(LinearLayout animView, ImageView imageView, int[] startPosition) {
        int x = startPosition[0];
        int y = startPosition[1];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50, 50);
        params.leftMargin = x;
        params.topMargin = y;
        imageView.setLayoutParams(params);
        animView.addView(imageView);
        return imageView;
    }

    private LinearLayout createAnimView() {
        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(params);
        linearLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        return linearLayout;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(MainActivity.this, R.layout.rv_item,null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tvItemName.setText(mNames[position]);
        }

        @Override
        public int getItemCount() {
            if (mNames!=null) {
                return mNames.length;
            }
            return 0;
        }
    }

}
