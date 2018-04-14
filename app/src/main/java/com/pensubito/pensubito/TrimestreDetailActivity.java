package com.pensubito.pensubito;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.pensubito.pensubito.db.DeleteTrimestreAsyncTask;
import com.pensubito.pensubito.db.OnTrimestreDeletedListener;
import com.pensubito.pensubito.db.PensubitoDBUtil;
import com.pensubito.pensubito.db.PensubitoDao;
import com.pensubito.pensubito.di.Injectable;
import com.pensubito.pensubito.vm.TrimestreViewModel;
import com.pensubito.pensubito.vo.Trimestre;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;
import timber.log.Timber;

/**
 * An activity representing a single Trimestre detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link TrimestreListActivity}.
 */
public class TrimestreDetailActivity extends AppCompatActivity implements HasSupportFragmentInjector {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_TRIMESTRE_ID = "act_trim_id";
    @Inject
    PensubitoDao pensubitoDao;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    private int mTrimestreId;
    private TrimestresTabPagerAdapter mTrimestresTabPagerAdapterPagerAdapter;
    private ViewPager mViewPager;
    private TrimestreViewModel viewModel;
    private DeleteTrimestreAsyncTask mDeleteTrimestreAT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        this.getSupportFragmentManager().registerFragmentLifecycleCallbacks(
                new FragmentManager.FragmentLifecycleCallbacks() {
                    @Override
                    public void onFragmentCreated(FragmentManager fm, Fragment f,
                                                  Bundle savedInstanceState) {
                        if (f instanceof Injectable) {
                            AndroidSupportInjection.inject(f);
                        }
                    }
                    }, true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trimestre_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            mTrimestreId = getIntent().getIntExtra(ARG_TRIMESTRE_ID,-1);
        }

        viewModel = ViewModelProviders.of(this,viewModelFactory).get(TrimestreViewModel.class);
        viewModel.init(mTrimestreId);
        viewModel.getTrimestre().observe(this, new Observer<Trimestre>() {
            @Override
            public void onChanged(@Nullable Trimestre trimestre) {
                if(trimestre != null) {
                    String periodoID = PensubitoDBUtil.convertIDPeriodoToString(trimestre.getPeriodoId()) + " " + String.valueOf(trimestre.getAnyo());
                    if (actionBar != null) {
                        actionBar.setTitle(periodoID);
                    }
                }
            }
        });

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mTrimestresTabPagerAdapterPagerAdapter =
                new TrimestresTabPagerAdapter(
                        getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.vp_trimestre_details);
        mViewPager.setAdapter(mTrimestresTabPagerAdapterPagerAdapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trimestre_details, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, TrimestreListActivity.class));
            return true;
        }else if(id == R.id.action_delete){

            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("¿Eliminar este trimestre?")
                    //.setMessage("Are you sure you want to close this activity?")
                    .setPositiveButton("BORRAR", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDeleteTrimestreAT = new DeleteTrimestreAsyncTask(pensubitoDao,mTrimestreId);
                            mDeleteTrimestreAT.setOnTrimestreDeletedListener(new OnTrimestreDeletedListener(){

                                @Override
                                public void onTrimestreDeleted(int trimestreId) {
                                    Context context = getApplicationContext();
                                    CharSequence text = "Trimestre eliminado";
                                    int duration = Toast.LENGTH_SHORT;

                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                    finish();
                                }
                            });
                            mDeleteTrimestreAT.execute();
                        }

                    })
                    .setNegativeButton("CANCELAR", null)
                    .show();

            return true;

        }
        return super.onOptionsItemSelected(item);
    }
    // Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
    public class TrimestresTabPagerAdapter extends FragmentPagerAdapter {

        public TrimestresTabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Timber.d("tr-det position position " + String.valueOf(position));
            Fragment fragment;
            Bundle args;
            switch (position){
                case 0:
                    fragment = new TrimestreTabMateriasFragment();
                    args = new Bundle();
                    args.putInt(ARG_TRIMESTRE_ID, mTrimestreId);
                    fragment.setArguments(args);
                    return fragment;
                case 1:
                    fragment = new TrimestreTabDataFragment();
                    args = new Bundle();
                    args.putInt(ARG_TRIMESTRE_ID, mTrimestreId);
                    fragment.setArguments(args);
                    return fragment;
                default:
                    Timber.d("tr-det position unknown " + String.valueOf(position));
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Materias";
                case 1:
                    return "Datos";
                default:
                    return null;
            }
        }
    }
    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

}
