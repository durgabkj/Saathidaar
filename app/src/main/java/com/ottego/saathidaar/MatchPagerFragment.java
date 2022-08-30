package com.ottego.saathidaar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.ottego.saathidaar.Adapter.ViewPagerMatchDetailAdapter;
import com.ottego.saathidaar.databinding.FragmentMatchPagerBinding;
import com.ottego.saathidaar.viewmodel.MatchViewModel;

import java.util.Objects;


public class MatchPagerFragment extends DialogFragment {
   FragmentMatchPagerBinding b;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    //    model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    MatchViewModel viewModel;

    int position=0;
    int Size_Of_list=0;
    public MatchPagerFragment() {
        // Required empty public constructor
    }

    public static MatchPagerFragment newInstance(String param1, String param2) {
        MatchPagerFragment fragment = new MatchPagerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getTheme() {
        return R.style.FullScreenDialogTheme;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            position=Integer.parseInt(mParam1);
            Size_Of_list=Integer.parseInt(mParam2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentMatchPagerBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MatchViewModel.class);

        Log.e("position",mParam1);
        Log.e("size of list",mParam2);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.fcvDetailsShow, MatchDetailsFragment.newInstance(viewModel.list.getValue().get(position).member_id, ""))
                .commit();

//Previous button
        if (position == 0) {
            b.llPrevious.setVisibility(View.INVISIBLE);
        }

// Next button...
        if (Size_Of_list-1==position) {
            b.llNext.setVisibility(View.INVISIBLE);
        }

        b.llNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  position=  position+1;
                Log.e("next", String.valueOf(position));
                if (Size_Of_list-1==position) {
                    b.llNext.setVisibility(View.INVISIBLE);
                    Toast.makeText(requireActivity(), "This Is Your Last Matches", Toast.LENGTH_SHORT).show();
                } else {
                    ++position;
                    b.llPrevious.setVisibility(View.VISIBLE);
                    b.llNext.setVisibility(View.VISIBLE);
                }


                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fcvDetailsShow, MatchDetailsFragment.newInstance(viewModel.list.getValue().get(position).member_id, ""))
                        .commit();

            }
        });
        b.llPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // position=  position-1;
                Log.e("position", String.valueOf(position));
                if (position == 0) {
                    Toast.makeText(requireActivity(), "This Is Your First Matches", Toast.LENGTH_SHORT).show();
                    b.llPrevious.setVisibility(View.INVISIBLE);
                } else {
                    --position;
                    b.llPrevious.setVisibility(View.VISIBLE);
                    b.llNext.setVisibility(View.VISIBLE);
                }
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fcvDetailsShow, MatchDetailsFragment.newInstance(viewModel.list.getValue().get(position).member_id, ""))
                        .commit();
            }
        });
        return b.getRoot();
    }
}

