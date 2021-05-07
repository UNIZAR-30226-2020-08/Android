package com.app.guinote.TorneoFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager.widget.ViewPager;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.guinote.R;
import com.app.guinote.TorneoAdapter.Adapter2;
import com.app.guinote.TorneoCustomViews.View2;
import com.app.guinote.TorneoModel.Model1;
import com.app.guinote.TorneoModel.Model3;
import com.app.guinote.TorneoModel.Model2;
import com.app.guinote.TorneoUtility.Utility1;

import java.util.ArrayList;
import java.util.List;




public class BracketsFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private View2 viewPager;
    private Adapter2 sectionAdapter;
    private ArrayList<Model1> sectionList;
    private int mNextSelectedScreen;
    private int mCurrentPagerState;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_brackts, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        setData();
        intialiseViewPagerAdapter();
    }

    private void setData() {
        sectionList = new ArrayList<>();
        ArrayList<Model3> Colomn1matchesList = new ArrayList<>();
        ArrayList<Model3> colomn2MatchesList = new ArrayList<>();
        ArrayList<Model3> colomn3MatchesList = new ArrayList<>();
        Model2 competitorOne = new Model2("Arxn", "59");
        Model2 competitorTwo = new Model2("Andres200gb", "30");
        Model2 competitorThree = new Model2("DIEGOL", "44");
        Model2 competitorFour = new Model2("nibi", "19");
        Model2 competitorFive = new Model2("Bichooo", "34");
        Model2 competitorSix = new Model2("Azueless", "89");
        Model2 competitorSeven = new Model2("Moraddd ", "78");
        Model2 competitorEight = new Model2("MDLR", "45");
        Model3 model31 = new Model3(competitorOne,competitorTwo);
        Model3 model32 = new Model3(competitorThree, competitorFour);
        Model3 model33 = new Model3(competitorFive,competitorSix);
        Model3 model34 = new Model3(competitorSeven, competitorEight);
        Colomn1matchesList.add(model31);
        Colomn1matchesList.add(model32);
        Colomn1matchesList.add(model33);
        Colomn1matchesList.add(model34);
        Model1 model11 = new Model1(Colomn1matchesList);
        sectionList.add(model11);
        Model2 competitorNine = new Model2("Arxn", "56");
        Model2 competitorTen = new Model2("bichoo", "45");
        Model2 competitorEleven = new Model2("Moradd", "43");
        Model2 competitorTwelve = new Model2("MDLR", "34");
        Model3 model35 = new Model3(competitorNine,competitorTen);
        Model3 model36 = new Model3(competitorEleven, competitorTwelve);
        colomn2MatchesList.add(model35);
        colomn2MatchesList.add(model36);
        Model1 model12 = new Model1(colomn2MatchesList);
        sectionList.add(model12);
        Model2 competitorThirteen = new Model2("arxn", "2");
        Model2 competitorForteen = new Model2("MDLR", "1");
        Model3 model37 = new Model3(competitorThirteen, competitorForteen);
        colomn3MatchesList.add(model37);
        Model1 model13 = new Model1(colomn3MatchesList);
        sectionList.add(model13);

    }

    private void intialiseViewPagerAdapter() {

        sectionAdapter = new Adapter2(getChildFragmentManager(),this.sectionList);
        viewPager.setOffscreenPageLimit(10);
        viewPager.setAdapter(sectionAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setPageMargin(-200);
        viewPager.setHorizontalFadingEdgeEnabled(true);
        viewPager.setFadingEdgeLength(50);

        viewPager.addOnPageChangeListener(this);
    }

    private void initViews() {

        viewPager = (View2) getView().findViewById(R.id.container);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if (mCurrentPagerState != ViewPager.SCROLL_STATE_SETTLING) {
            // We are moving to next screen on right side
            if (positionOffset > 0.5) {
                // Closer to next screen than to current
                if (position + 1 != mNextSelectedScreen) {
                    mNextSelectedScreen = position + 1;
                    //update view here
                    if (getBracketsFragment(position).getColomnList().get(0).getHeight()
                            != Utility1.dpToPx(131))
                        getBracketsFragment(position).shrinkView(Utility1.dpToPx(131));
                    if (getBracketsFragment(position + 1).getColomnList().get(0).getHeight()
                            != Utility1.dpToPx(131))
                        getBracketsFragment(position + 1).shrinkView(Utility1.dpToPx(131));
                }
            } else {
                // Closer to current screen than to next
                if (position != mNextSelectedScreen) {
                    mNextSelectedScreen = position;
                    //updateViewhere

                    if (getBracketsFragment(position + 1).getCurrentBracketSize() ==
                            getBracketsFragment(position + 1).getPreviousBracketSize()) {
                        getBracketsFragment(position + 1).shrinkView(Utility1.dpToPx(131));
                        getBracketsFragment(position).shrinkView(Utility1.dpToPx(131));
                    } else {
                        int currentFragmentSize = getBracketsFragment(position + 1).getCurrentBracketSize();
                        int previousFragmentSize = getBracketsFragment(position + 1).getPreviousBracketSize();
                        if (currentFragmentSize != previousFragmentSize) {
                            getBracketsFragment(position + 1).expandHeight(Utility1.dpToPx(262));
                            getBracketsFragment(position).shrinkView(Utility1.dpToPx(131));
                        }
                    }
                }
            }
        } else {
            // We are moving to next screen left side
            if (positionOffset > 0.5) {
                // Closer to current screen than to next
                if (position + 1 != mNextSelectedScreen) {
                    mNextSelectedScreen = position + 1;
                    //update view for screen

                }
            } else {
                // Closer to next screen than to current
                if (position != mNextSelectedScreen) {
                    mNextSelectedScreen = position;
                    //updateviewfor screem
                }
            }
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public BracketsColomnFragment getBracketsFragment(int position) {
        BracketsColomnFragment bracktsFrgmnt = null;
        if (getChildFragmentManager() != null) {
            List<Fragment> fragments = getChildFragmentManager().getFragments();
            if (fragments != null) {
                for (Fragment fragment : fragments) {
                    if (fragment instanceof BracketsColomnFragment) {
                        bracktsFrgmnt = (BracketsColomnFragment) fragment;
                        if (bracktsFrgmnt.getSectionNumber() == position)
                            break;
                    }
                }
            }
        }
        return bracktsFrgmnt;
    }
}
