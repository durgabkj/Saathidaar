package com.ottego.saathidaar;

import android.animation.ValueAnimator;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;


public class DashBoardFragment extends Fragment {

    ImageView ivPremiumImage;
    TextView tvPremiumText;
    int position = 0;
    Animation animation;
    CountDownTimer countDownTimer;
    int[] images = {R.drawable.smartphone,R.drawable.documents,R.drawable.global};
            String[] text = {"phone Number to Connect Instantly", "100% Verified Biodatas", "Find Common connections"};
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DashBoardFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DashBoardFragment newInstance(String param1, String param2) {
        DashBoardFragment fragment = new DashBoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dash_board, container, false);
        ivPremiumImage=view.findViewById(R.id.ivPremiumImage);
        tvPremiumText=view.findViewById(R.id.tvPremiumText);
             set();

        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
      //  animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(3000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = ivPremiumImage.getWidth();
                final float translationX = width * progress;
                ivPremiumImage.setTranslationX(translationX);
                ivPremiumImage.setTranslationX(translationX - width);

                tvPremiumText.setTranslationX(translationX);
                tvPremiumText.setTranslationX(translationX - width);


            }
        });
        animator.start();
        return view;
    }

    private void set() {
        if(countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(2 * 3000, 2000) {
            public void onTick(long millisUntilFinished) {
                // Text_t_speech();
                if (position < text.length)
                {
                    ivPremiumImage.setImageResource(images[position]);
                    tvPremiumText.setText(text[position]);
                    position = position + 1;
                } else {
                    position = 0;
                }
            }

            @Override
            public void onFinish() {
start();
            }

        }.start();
    }

}