package com.ottego.saathidaar;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.ottego.saathidaar.Model.DataModelDashboard;
import com.ottego.saathidaar.databinding.FragmentInboxBinding;
import com.ottego.saathidaar.viewmodel.InboxViewModel;

import org.json.JSONObject;

import java.util.List;


public class InboxFragment extends Fragment {
    public String url = "http://103.174.102.195:8080/saathidaar_backend/api/request/count/accept-request/";
    FragmentInboxBinding b;
    DataModelDashboard model;
    Context context;
    SessionManager sessionManager;
    InboxViewModel viewModel;
    InvitationFragment invitationFragment = new InvitationFragment();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
int count=0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InboxFragment() {
        // Required empty public constructor
    }


    public static InboxFragment newInstance(String param1, String param2) {
        InboxFragment fragment = new InboxFragment();
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
        b = FragmentInboxBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(InboxViewModel.class);
        context = getContext();
        sessionManager = new SessionManager(context);

        b.vpInbox.setPagingEnable(false);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.fcvInbox, invitationFragment)
                .commit();

        b.chipGroupInbox.check(b.chipGroupInbox.getChildAt(0).getId());

        listener();
       // getDataCount();
        viewModel.getDataCount();
        return b.getRoot();
    }

    private void listener() {

        viewModel.count.observe(getViewLifecycleOwner(), new Observer<DataModelDashboard>() {
            @Override
            public void onChanged(DataModelDashboard dataModelDashboard) {
                model=dataModelDashboard;
                setData();
            }
        });

        b.chipGroupInbox.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                b.vpInbox.setCurrentItem(group.getCheckedChipId());
                Fragment fragment = null;
                switch (group.getCheckedChipId()) {
                    case R.id.chipInvitation: {
                        fragment = InvitationFragment.newInstance("", "");
                        break;
                    }
                    case R.id.chipAccept: {
                        fragment = AcceptedInboxFragment.newInstance("", "");
                        break;
                    }
                    case R.id.chipSent: {
                        fragment = SentInboxFragment.newInstance("", "");
                        break;
                    }

                    case R.id.chipDelete: {
                        fragment = DeleteInboxFragment.newInstance("", "");
                        break;
                    }

                    case R.id.chipBlock: {
                        fragment = BlockMemberFragment.newInstance("", "");
                        break;
                    }
                    default: {
                        fragment = InvitationFragment.newInstance("", "");
                        break;
                    }

                }
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fcvInbox, fragment)
                        .commit();


            }

        });


    }

    private void getDataCount() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url + sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", String.valueOf((response)));
                Gson gson = new Gson();
                model = gson.fromJson(String.valueOf(response), DataModelDashboard.class);
                setData();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);


    }

    private void setData() {
//        RequestAccept.setText(model.data.get(0).accept_request_count);
//        RequestSent.setText(model.data.get(0).sent_request_count);
//        Visitors.setText(model.data.get(0).recent_visitors_count);
        if (model.data != null && model.data.size() > 0) {

            b.chipInvitation.setText("Invitation" + "(" + model.data.get(0).invitations_count + ")");
            b.chipAccept.setText("Accept " + "(" + model.data.get(0).accept_request_count + ")");
            b.chipSent.setText("Sent " + "(" + model.data.get(0).sent_request_count + ")");
             b.chipDelete.setText("Delete "+"("+model.data.get(0).deleted_request_count+")");
            b.chipBlock.setText("Block " + "(" + model.data.get(0).block_request_count + ")");


        }
    }



    private void refresh(int millisecond) {
        final Handler handler= new Handler();
        final  Runnable runnable=new Runnable() {
            @Override
            public void run() {
                getDataCount();
            }
        };

        handler.postDelayed(runnable, millisecond);
    }



}