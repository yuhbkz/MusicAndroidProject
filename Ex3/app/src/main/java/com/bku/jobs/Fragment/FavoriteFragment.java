package com.bku.jobs.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bku.jobs.Adapter.FavoriteJobAdapter;
import com.bku.jobs.Database.OfflineDatabaseHelper;
import com.bku.jobs.Models.JobInfo;
import com.bku.jobs.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Welcome on 5/21/2018.
 */

public class FavoriteFragment extends Fragment {

    @BindView(R.id.rvFavoriteList) RecyclerView recyclerView;
    OfflineDatabaseHelper db;
    FavoriteJobAdapter adapter;
    private FavoriteFragment.OnFragmentInteractionListener mListener;

    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_favorite_job, container, false);
        ButterKnife.bind(this,view);
        db=new OfflineDatabaseHelper(getContext());
        bindView();
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    public void bindView() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        JobInfo job=new JobInfo("1","2","3","4","5","6","7","8","9","http://github-jobs.s3.amazonaws.com/f3315720-554c-11e8-8ca1-a6d2be499f9d.png","11");
//        db.insertJob(job);
        restoreFromDb();
        recyclerView.setAdapter(adapter);

        getActivity().getSupportFragmentManager().addOnBackStackChangedListener(new android.support.v4.app.FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                restoreFromDb();
            }
        });
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void restoreFromDb() {
        ArrayList<JobInfo> lstJob = db.getAllJob();
        adapter=new FavoriteJobAdapter(getActivity(),lstJob);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Restore from db
        restoreFromDb();
    }

}
