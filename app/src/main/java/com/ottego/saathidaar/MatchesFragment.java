package com.ottego.saathidaar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.ChipGroup;
import com.ottego.saathidaar.Model.DataModelDashboard;
import com.ottego.saathidaar.databinding.FragmentMatchesBinding;
import com.ottego.saathidaar.viewmodel.MatchViewModel;

import java.util.List;


public class MatchesFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public String url = "http://103.174.102.195:8080/saathidaar_backend/api/request/count/accept-request/";
    FragmentMatchesBinding b;
    MatchViewModel viewModel;
    SessionManager sessionManager;
    Context context;
    DataModelDashboard model;
    int count = 0;
    MyMatchFragment myMatchFragment = new MyMatchFragment();
    private String mParam1;
    private String mParam2;

    public MatchesFragment() {

    }

    public static MatchesFragment newInstance(String param1, String param2) {
        MatchesFragment fragment = new MatchesFragment();
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
        b = FragmentMatchesBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MatchViewModel.class);
        context = getContext();
        sessionManager = new SessionManager(context);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.fcvMatches, MyMatchFragment.newInstance("", ""))
                .commit();


//        getDataCount();
        viewModel.getDataCount();
        listener();
        return b.getRoot();

    }


    private void listener() {
        viewModel.count.observe(getViewLifecycleOwner(), new Observer<DataModelDashboard>() {
            @Override
            public void onChanged(DataModelDashboard dataModelDashboard) {
                model = dataModelDashboard;
                setData();
            }
        });
        b.chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                Fragment fragment = null;
                switch (group.getCheckedChipId()) {
                    case R.id.chipNewMatch: {
                        fragment = NewMatchesFragment.newInstance("", "");
                        break;
                    }
                    case R.id.chipMyMatch: {
                        fragment = MyMatchFragment.newInstance("", "");
                        break;
                    }
                    case R.id.chipTodayMatch: {
                        fragment = TodayMatchFragment.newInstance("", "");
                        break;
                    }

                    case R.id.chipPremiumMatch: {
                        fragment = PremiumMatchesFragment.newInstance("", "");
                        break;
                    }

                    case R.id.chipShortListed: {
                        fragment = ShortListFragment.newInstance("", "");
                        break;
                    }
//                    case R.id.chipSearch:{
//                        fragment= SearchFragment.newInstance("", "");
//                        break;
//                    }
                    case R.id.chipRecentView: {
                        fragment = RecentViewFragment.newInstance("", "");
                        break;
                    }
                    case R.id.chipRecentlyView: {
                        fragment = RecentlyViewedFragment.newInstance("", "");
                        break;
                    }
                    default: {
                        fragment = RecentlyViewedFragment.newInstance("", "");
                        break;
                    }

                }
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fcvMatches, fragment)
                        .commit();


            }

        });
        b.chipGroup.check(b.chipGroup.getChildAt(1).getId());

    }

    private void setData() {
        if (model.data != null && model.data.size() > 0) {
            b.chipNewMatch.setText("New Matches " + "(" + model.data.get(0).new_matches_count + ")");
            b.chipMyMatch.setText("My Matches " + "(" + model.data.get(0).my_matches_count + ")");
            b.chipTodayMatch.setText("Today's Matches " + "(" + model.data.get(0).todays_matches_count + ")");
            // b.chip1.setText("Premium Matches "+"("+model.data.get(0).new_matches_count+")");
            // b.chip1.setText("Search "+"("+model.data.get(0).new_matches_count+")");
            b.chipRecentView.setText("Recent Visitors " + "(" + model.data.get(0).recent_visitors_count + ")");

            b.chipShortListed.setText("ShortListed " + "(" + model.data.get(0).shortlists_count + ")");
            b.chipPremiumMatch.setText("Premium Matches " + "(" + model.data.get(0).premium_matches_count + ")");
            b.chipRecentlyView.setText("Recently Viewed " + "(" + model.data.get(0).recent_view_to + ")");
        }
    }


}

