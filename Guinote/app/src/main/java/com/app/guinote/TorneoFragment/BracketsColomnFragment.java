package com.app.guinote.TorneoFragment;

import android.os.Bundle;


import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.app.guinote.R;
import com.app.guinote.TorneoAdapter.Adapter1;
import com.app.guinote.TorneoModel.Model1;
import com.app.guinote.TorneoModel.Model3;
import com.app.guinote.TorneoUtility.Utility1;

import java.util.ArrayList;



public class BracketsColomnFragment extends Fragment {

    private Model1 model1;
    private int sectionNumber = 0;
    private int previousBracketSize;
    private ArrayList<Model3> list;
    private RecyclerView bracketsRV;

    private Adapter1 adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_brackets_colomn, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        getExtras();
        initAdapter();
    }

    private void initViews() {

        bracketsRV = (RecyclerView) getView().findViewById(R.id.rv_score_board);
    }

    public ArrayList<Model3> getColomnList() {
        return list;
    }

    private void getExtras() {
        if (getArguments() != null) {
            list = new ArrayList<>();
            model1 = (Model1) getArguments().getSerializable("colomn_data");
            sectionNumber = getArguments().getInt("section_number");
            previousBracketSize = getArguments().getInt("previous_section_size");
            list.addAll(model1.getMatches());
            setInitialHeightForList();
        }
    }
    public int getSectionNumber() {
        return sectionNumber;
    }

    private void setInitialHeightForList() {
        for (Model3 data : list){
            if (sectionNumber == 0){
                data.setHeight(Utility1.dpToPx(131));
            }else if (sectionNumber == 1 && previousBracketSize != list.size()){
                data.setHeight(Utility1.dpToPx(262));
            }else if (sectionNumber == 1 && previousBracketSize == list.size()) {
                data.setHeight(Utility1.dpToPx(131));
            } else if (previousBracketSize > list.size()) {
                data.setHeight(Utility1.dpToPx(262));
            }else if (previousBracketSize == list.size()) {
                data.setHeight(Utility1.dpToPx(131));
            }
        }

    }

    public void expandHeight(int height) {

        for (Model3 data : list) {
            data.setHeight(height);
        }
        adapter.setList(list);
    }

    public void shrinkView(int height) {
        for (Model3 data : list) {
            data.setHeight(height);
        }
        adapter.setList(list);
    }
    private void initAdapter() {
        adapter = new Adapter1(this, getContext(), list);
        if (bracketsRV != null) {
            bracketsRV.setHasFixedSize(true);
            bracketsRV.setNestedScrollingEnabled(false);
            bracketsRV.setAdapter(adapter);
            bracketsRV.smoothScrollToPosition(0);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            bracketsRV.setLayoutManager(layoutManager);
            bracketsRV.setItemAnimator(new DefaultItemAnimator());
        }
    }

    public int getCurrentBracketSize() {
        return list.size();
    }
    public int getPreviousBracketSize() {
        return previousBracketSize;
    }
}
