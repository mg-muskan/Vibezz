package com.vibezz.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.vibezz.Fragments.LikeFragment;
import com.vibezz.Fragments.PostFragment;

public class FragmentsAdapter extends FragmentStateAdapter {

    public FragmentsAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0: return new PostFragment();
            case 1: return new LikeFragment();
            default: return new PostFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
