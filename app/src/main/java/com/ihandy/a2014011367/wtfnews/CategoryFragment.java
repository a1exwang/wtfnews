package com.ihandy.a2014011367.wtfnews;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ihandy.a2014011367.wtfnews.databinding.FragmentNewsListBinding;
import com.ihandy.a2014011367.wtfnews.models.Category;
import com.ihandy.a2014011367.wtfnews.viewmodels.CategoryViewModel;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class CategoryFragment extends Fragment {
    private Category category;
    public CategoryFragment() {
    }

    @Override
    public void setArguments(Bundle bundle) {
        category = (Category) bundle.get(getString(R.string.newsCategory));
    }

    @SuppressLint("ValidFragment")
    public CategoryFragment(Category category) {
        this.category = category;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        FragmentNewsListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news_list, container, false);
        View view = binding.getRoot();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        if (category != null) {
            CategoryViewModel vm = new CategoryViewModel(category);
            binding.setCategoryViewModel(vm);
            recyclerView.addOnScrollListener(vm.getScrollListener());
            recyclerView.setItemAnimator(new SlideInLeftAnimator());
        }
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
