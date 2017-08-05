package com.dsgly.bixin.biz.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BaseActivity;
import com.dsgly.bixin.net.requestParam.BaseParam;

/**
 * 个人主页
 * Created by dengxuan on 2017/8/1.
 */

public class SelfMainPageActivity extends BaseActivity {

    private RecyclerView recyclerView;

    @Override
    public void initViews() {
        setContentView(R.layout.self_main_page_activity);
        initListView();
    }

    private void initListView(){
        recyclerView = (RecyclerView) findViewById(R.id.self_page_listview);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
    }
}
