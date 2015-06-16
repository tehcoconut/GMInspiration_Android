package com.gminspiration.tehcoconut.mobileapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.gminspiration.mobileapi.GMIConnection;

/**
 * Created by tehcoconut on 5/29/15.
 */
public class MainActivity extends AppCompatActivity {
    private String[] mListItemTitles;
    private DrawerLayout dl_left;
    private RecyclerView rv_left;
    private ActionBarDrawerToggle mDrawerToggle;
    private SearchView sv_top;

    private MyAdapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private GMIConnection gmic;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gmic = new GMIConnection("AGRE37H5F0D3GHJ8");

        mListItemTitles = getResources().getStringArray(R.array.drawerNavNames);
        dl_left = (DrawerLayout) findViewById(R.id.dl_left);
        rv_left = (RecyclerView) findViewById(R.id.rv_left);

        rv_left.setHasFixedSize(true);

        myAdapter = new MyAdapter(mListItemTitles, "tehcoconut", "2015-05-20");

        rv_left.setAdapter(myAdapter);
        layoutManager = new LinearLayoutManager(this);
        rv_left.setLayoutManager(layoutManager);


        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });

        rv_left.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    dl_left.closeDrawers();
                    HomeFragment home;

                    switch (rv_left.getChildAdapterPosition(child)) {
                        case 0:
                            Toast.makeText(MainActivity.this, "CASE 0", Toast.LENGTH_SHORT).show();
                            home = new HomeFragment();
                            home.setContext(MainActivity.this);
                            home.setConnection(gmic);
                            gmic.getHotContributions(home);
                            openFragment(home);
                            break;
                        case 1:
                            Toast.makeText(MainActivity.this, "CASE 1", Toast.LENGTH_SHORT).show();
                            home = new HomeFragment();
                            home.setContext(MainActivity.this);
                            home.setConnection(gmic);
                            gmic.getHotContributions(home);
                            openFragment(home);
                            break;
                        case 2:
                            Toast.makeText(MainActivity.this, "CASE 2", Toast.LENGTH_SHORT).show();
                            openFragment(new InboxFragment());
                            break;
                        case 3:
                            Toast.makeText(MainActivity.this, "CASE 3", Toast.LENGTH_SHORT).show();
                            openFragment(new MyContributionsFragment());
                            break;
                        case 4:
                            Toast.makeText(MainActivity.this, "CASE 4", Toast.LENGTH_SHORT).show();
                            openFragment(new ContributeFragment());
                            break;
                        case 5:
                            Toast.makeText(MainActivity.this, "CASE 5", Toast.LENGTH_SHORT).show();
                            openFragment(new CollectionsFragment());
                            break;

                    }

                    return true;

                }


                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }

        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerToggle = new ActionBarDrawerToggle(this, dl_left, toolbar, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(getTitle());
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

        dl_left.setDrawerListener(mDrawerToggle);

        HomeFragment home = new HomeFragment();
        home.setContext(this);
        home.setConnection(gmic);
        gmic.getHotContributions(home);
        openFragment(home);

    }

    public void openFragment(Fragment fragment){
        String backStateName = fragment.getClass().getName();

        FragmentManager fm = getSupportFragmentManager();

        // make the flag param 0 for each search to be saved on the backstack
        boolean fragPopped = fm.popBackStackImmediate(backStateName, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        FragmentTransaction fg = fm.beginTransaction();
        fg.replace(R.id.container, fragment);
        fg.addToBackStack(backStateName);
        fg.commit();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        //boolean drawerOpen = dl_left.isDrawerOpen(Gravity.LEFT);
       // menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        // hide icons not related to drawer on title
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mDrawerToggle.onOptionsItemSelected(item))
            return true;

        switch (item.getItemId()){
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        sv_top = (SearchView) menu.findItem(R.id.sv_top).getActionView();

        sv_top.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchFragment sf = new SearchFragment();
                sf.setContext(MainActivity.this);
                sf.setConnection(gmic);
                sf.setQuerySortOffset(query, GMIConnection.SORT_RELEVANCE, 0);
                sf.setCategoryGame("All", "All");
                gmic.searchQuery(query, GMIConnection.SORT_RELEVANCE, 0, "All", "All", sf);
                openFragment(sf);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {

        if(dl_left.isDrawerOpen(Gravity.LEFT)){
            dl_left.closeDrawer(Gravity.LEFT);
        }else if(getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        }else{
            super.onBackPressed();
        }
    }
}
