package com.example.uniquitousprototype;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    ViewPager vp;






    private String[] menuLists = {"프로필", "친구찾기", "친구목록", "로그아웃"};
    private ListView listView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuLists));
        listView.setOnItemClickListener(new DrawerItemClickListener());
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, null, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            @Override
            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_create:
                create(null,null,-1,-1,-1,0);
                return true;
            case R.id.action_websearch:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_search, menu);
        getMenuInflater().inflate(R.menu.main_create, menu);
        return true;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            switch (position) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }
            drawerLayout.closeDrawer(listView);
        }
    }
    public void createTask(View v)
    {
        create(null,null,-1,-1,-1,0);
    }

    public void create(String content, String category, int cost, int reward,int id, int type) {
        if (!apiApplication.isLogedIn()) {
            final Intent loginPageIntent = new Intent(this, LoginPage.class);
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.not_login);
            dialog.setTitle("에러");
            dialog.findViewById(R.id.cancel).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    }
            );
            dialog.findViewById(R.id.accept_to_login_button).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(loginPageIntent);
                            dialog.dismiss();
                            finish();
                        }
                    }
            );
            dialog.show();
            return;
        }
        Intent createTaskIntent = new Intent(this, CreateTaskIntent.class);
        createTaskIntent.putExtra("content",content);
        createTaskIntent.putExtra("category",category);
        createTaskIntent.putExtra("cost",cost);
        createTaskIntent.putExtra("reward",reward);
        createTaskIntent.putExtra("type",type);
        createTaskIntent.putExtra("id",id);
        startActivity(createTaskIntent);
    }





    public void startNegotiation(View v) {
        CardView cardView = (CardView) v.getParent().getParent().getParent().getParent();
        TextView costText = cardView.findViewById(R.id.recycler_view_cost);
        TextView rewardText = cardView.findViewById(R.id.recycler_view_reward);
        String costString = costText.getText().toString();
        String rewardString = rewardText.getText().toString();
        int costInt = Integer.parseInt(costString);
        int rewardInt = Integer.parseInt(rewardString);

        Intent startNegotiationIntent = new Intent(this, StartNegotiationIntent.class);
        startNegotiationIntent.putExtra("costInt", costInt);
        startNegotiationIntent.putExtra("rewardInt", rewardInt);
        startActivity(startNegotiationIntent);
        startActivity(startNegotiationIntent);
    }

    public void update(final View v) {
        if (!apiApplication.isLogedIn()) {
            final Intent loginPageIntent = new Intent(this, LoginPage.class);
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.not_login);
            dialog.setTitle("에러");
            dialog.findViewById(R.id.cancel).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    }
            );
            dialog.findViewById(R.id.accept_to_login_button).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(loginPageIntent);
                            dialog.dismiss();
                            finish();
                        }
                    }
            );
            dialog.show();
            return;
        }

        final RelativeLayout relativeLayout = (RelativeLayout) v.getParent().getParent().getParent();
        TextView t = (TextView)relativeLayout.findViewById(R.id.recycler_view_id);
        final int id = Integer.parseInt(t.getText().toString());
        final CharSequence[] items = {"수정", "삭제", "취소"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("작업을 선택하세요")
                .setItems(items, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int index){
                        switch (index)
                        {
                            case 0:
                                TextView tcontent = (TextView)relativeLayout.findViewById(R.id.recycler_view_content);
                                TextView tcategory = (TextView)relativeLayout.findViewById(R.id.recycler_view_category);
                                TextView tcost = (TextView)relativeLayout.findViewById(R.id.recycler_view_cost);
                                TextView treward = (TextView)relativeLayout.findViewById(R.id.recycler_view_reward);
                                String content = tcontent.getText().toString();
                                String category = tcategory.getText().toString();
                                int cost = Integer.parseInt(tcost.getText().toString());
                                int reward = Integer.parseInt(treward.getText().toString());
                                create(content,category,cost,reward,id,1);
                                break;

                            case 1:
                                if (!apiApplication.isLogedIn()) {
                                    return;
                                }
                                String token = "Token ";
                                token += apiApplication.getLoginUser().getToken();
                                Call<Void> call = apiService.deleteTask(token,id);
                                call.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {

                                    }
                                });
                                category_get(v);
                                break;

                            case 2:
                                break;
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
