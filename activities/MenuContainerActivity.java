package br.com.emanoel.oliveira.container.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import br.com.emanoel.oliveira.container.R;
import br.com.emanoel.oliveira.container.fragments.MenuFragment;

public class MenuContainerActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {


        MenuFragment menuFragment;
        String teste;
        String TAG = "MenuContainerActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //checando se usuario é administrativo, e mudando o menu
        if(!userIsAdmin) {
            setContentView(R.layout.activity_menu_container);
        }else {

            setContentView(R.layout.activity_menu_container_admin);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!userIsAdmin) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        } else {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_admin);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_admin);
            navigationView.setNavigationItemSelectedListener(this);
        }



       //instanciando o fragmento padrão

        menuFragment = new MenuFragment();



        addFragment2Frame(R.id.frMenuPrincipal,menuFragment);


    }



    @Override
    public void onBackPressed() {


        if(!userIsAdmin) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                int count = getSupportFragmentManager().getBackStackEntryCount();

                if (count == 1) {
                    super.onBackPressed();
                    //additional code
                   addFragment2Frame(R.id.frMenuPrincipal,menuFragment);

                } else {
                    getSupportFragmentManager().popBackStack();
                }


            }
        }else {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_admin);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                int count = getSupportFragmentManager().getBackStackEntryCount();

                if (count == 1) {
                    super.onBackPressed();
                    //additional code

                    addFragment2Frame(R.id.frMenuPrincipal,menuFragment);

                } else {
                    getSupportFragmentManager().popBackStack();
                }

            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_container, menu);
        //habilita cadastro?
        if (userIsAdmin) {
            Log.d("MENU ITEM", "onCreate: userAdmin");

            // MenuItem menuItem = menu.findItem(R.id.action_cadastro);
            //menuItem.setVisible(true);

            MenuItem configItem = menu.findItem(R.id.action_settings);
            configItem.setVisible(true);

        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    //todo checar if is useradmin because will be an special menu
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.admin) {

            // Handle the admin action
            startActivity(new Intent(getApplicationContext(),AdminActivity.class));

        } else if (id == R.id.nav_gallery) {
            //todo take care!! using this only for test
            startActivity(new Intent(getApplicationContext(),AdminActivity.class));
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

            System.exit(0);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        if(!userIsAdmin) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }else{
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_admin);
            drawer.closeDrawer(GravityCompat.START);
            return true;

        }
    }

    @Override
    protected void onStart() {
        Log.e("MENU_ON_START", "onStart: " + userIsAdmin );
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();}

}
