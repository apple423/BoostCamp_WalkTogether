package com.example.han.boostcamp_walktogether.view.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.han.boostcamp_walktogether.Adapters.LibraryAdapter;
import com.example.han.boostcamp_walktogether.R;

/**
 * Created by Han on 2017-08-24.
 */

public class LibraryActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.library_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        LibraryAdapter libraryAdapter = new LibraryAdapter();
        String[] titles = getResources().getStringArray(R.array.library_titles);
        String[] urls = getResources().getStringArray(R.array.library_url);
        String[] copyrights = getResources().getStringArray(R.array.library_copyright);
        String[] licesnes = getResources().getStringArray(R.array.library_license);
        libraryAdapter.setLibrary(titles,urls,copyrights,licesnes);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(libraryAdapter);
    }
}
