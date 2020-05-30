package com.example.kuaishou.demokuaishou.search;

import com.example.kuaishou.demokuaishou.base.BasePresenter;
import com.example.kuaishou.demokuaishou.base.IView;
import com.example.kuaishou.demokuaishou.search.mode.SearchBean;

public class SearchContract {

    public interface ISearchView extends IView {
        void onSearchData(SearchBean searchBean);
    }

    public static abstract class SearchPresenter extends BasePresenter<ISearchView> {
        public abstract void searchData(String rId);
    }
}
