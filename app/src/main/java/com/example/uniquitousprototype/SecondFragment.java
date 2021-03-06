package com.example.uniquitousprototype;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ydh13 on 2017-08-05.
 */

public class SecondFragment extends Fragment
{
    public SecondFragment()
    {
    }
    private List<Task> myTask;
    private ApiApplication apiApplication;
    private ApiService apiService;
    private ProgressDialog progressDialog;
    private SegmentedGroup categoryGroup;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RelativeLayout layout;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        apiApplication = (ApiApplication) getActivity().getApplicationContext();
        apiService = apiApplication.getApiService();

        if(apiApplication.isLogedIn()) {
            layout = (RelativeLayout) inflater.inflate(R.layout.fragment_second, container, false);

            recyclerView = (RecyclerView) layout.findViewById(R.id.myTaskRecycler_view);
            recyclerView.setHasFixedSize(true);
            linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
            categoryGroup = (SegmentedGroup) layout.findViewById(R.id.segmented2);
            RadioButton radioButton1 = (RadioButton)layout.findViewById(R.id.myTask);
            RadioButton radioButton2 = (RadioButton)layout.findViewById(R.id.undertakeTask);
            radioButton1.setOnClickListener(mClickListener);
            loading();
            Call<TaskResponse> call = apiService.myTask(apiApplication.getToken());
            call.enqueue(new Callback<TaskResponse>() {
                @Override
                public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                    myTask = response.body().getResults();
                    recyclerView.setAdapter(new MyAdapter(myTask));
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<TaskResponse> call, Throwable t) {
                    Toast.makeText(getActivity(), "인터넷 연결을 확인하세요.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
        else {
            layout = (RelativeLayout) inflater.inflate(R.layout.fragment_second2, container, false);

        }
        return layout;
    }
    public void loading() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("로딩중입니다...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            int categoryIndex = categoryGroup.getCheckedRadioButtonId();
            RadioButton c = (RadioButton)layout.findViewById(categoryIndex);
            String categ = c.getText().toString();
            String category = "0";
            switch (categ){
                case "내가 쓴 글":
                    Call<TaskResponse> call = apiService.myTask(apiApplication.getToken());
                    loading();
                    call.enqueue(new Callback<TaskResponse>() {
                        @Override
                        public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                            myTask = response.body().getResults();
                            recyclerView.setAdapter(new MyAdapter(myTask));
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<TaskResponse> call, Throwable t) {
                            Toast.makeText(getActivity(), "인터넷 연결을 확인하세요.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                    break;
                case "지원한 글" :
                    break;

            }
        }
    };

}


