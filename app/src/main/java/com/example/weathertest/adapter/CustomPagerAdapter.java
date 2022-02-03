package com.example.weathertest.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.weathertest.view.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomPagerAdapter extends PagerAdapter {

    private List<Fragment> pages = new ArrayList<>();
    private Map<Fragment, Integer> fragmentsPosition = new HashMap<>();

    private Fragment currentPrimaryItem;
    private FragmentManager fragmentManager;
    private FragmentTransaction currentTransaction;


    public CustomPagerAdapter(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (currentTransaction == null) {
            currentTransaction = fragmentManager.beginTransaction();
        }

        Fragment pageFragment = pages.get(position);
        String tag = pageFragment.getArguments().getString(MainActivity.FRAGMENT_TAG_ARG);
        Fragment fragment = fragmentManager.findFragmentByTag(tag);

        if (fragment != null) {
            if (fragment.getId() == container.getId()) {
                currentTransaction.attach(fragment);
            } else {
                fragmentManager.beginTransaction().remove(fragment).commit();
                fragmentManager.executePendingTransactions();
                currentTransaction.add(container.getId(), fragment, tag);
            }
        } else {
            fragment = pageFragment;
            currentTransaction.add(container.getId(), fragment, tag);
        }

        if (fragment != currentPrimaryItem) {
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (currentTransaction == null) {
            currentTransaction = fragmentManager.beginTransaction();
        }
        currentTransaction.detach((Fragment) object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment) object;

        if (fragment != currentPrimaryItem) {
            if (currentPrimaryItem != null) {
                currentPrimaryItem.setMenuVisibility(false);
                currentPrimaryItem.setUserVisibleHint(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            currentPrimaryItem = fragment;
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (currentTransaction != null) {
            currentTransaction.commitAllowingStateLoss();
            currentTransaction = null;
            fragmentManager.executePendingTransactions();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((Fragment) object).getView() == view;
    }
    @Override
    public int getItemPosition (Object object)
    {
        int index = pages.indexOf (object);
        if (index == -1)
            return POSITION_NONE;
        else
            return index;
    }
    // ---------------------------------- Page actions ----------------------------------

    public void addPage(Fragment fragment) {
        fragmentsPosition.clear();
        pages.add(fragment);
        notifyDataSetChanged();
    }


    public void removePage(int position) {
        fragmentsPosition.clear();

        Fragment pageFragment = pages.get(position);
        String tag = pageFragment.getArguments().getString(MainActivity.FRAGMENT_TAG_ARG);

        Fragment fragment = fragmentManager.findFragmentByTag(tag);

        if (fragment != null) {
            fragmentsPosition.put(fragment, PagerAdapter.POSITION_NONE);
        }

        for (int i = position + 1; i < pages.size(); i++) {
            pageFragment = pages.get(i);
            tag = pageFragment.getArguments().getString(MainActivity.FRAGMENT_TAG_ARG);
            fragment = fragmentManager.findFragmentByTag(tag);

            if (fragment != null) {
                fragmentsPosition.put(fragment, i - 1);
            }
        }
        pages.remove(position);
        notifyDataSetChanged();
    }

}