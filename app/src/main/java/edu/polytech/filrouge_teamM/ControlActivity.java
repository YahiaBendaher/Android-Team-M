package edu.polytech.filrouge_teamM;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ControlActivity extends AppCompatActivity implements Menuable, Notifiable {
    public static final int TAB_DETAIL = 0;
    public static final int TAB_MAP = 1;
    public static final int TAB_REPORT = 2;
    public static final int TAB_LIST = 3;
    public static final int TAB_TRACKING = 4;

    private static final String DATA_IS_STARTING = "sauvegarde";
    private static final String DATA_MENU_NUMBER = "num";
    private final String TAG = "teamM " + getClass().getSimpleName();
    private Fragment mainFragment;
    private MenuFragment menu;
    private boolean isStarting = true;
    private Fragment[] tabFragments = {
            new ReportDetailFragment(),
            new MapFragment(),
            new ReportNewFragment(),
            new ReportListFragment(),
            new TrackingFragment()
    };
    private int menuNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        if (savedInstanceState == null) {
            menuNumber = TAB_DETAIL;
        }

        Intent intent = getIntent();
        if (intent != null) {
            menuNumber = intent.getIntExtra(getString(R.string.index), TAB_DETAIL);
        }

        Bundle args = new Bundle();
        args.putInt(getString(R.string.index), menuNumber);

        if (savedInstanceState == null) {
            menu = new MenuFragment();
            menu.setArguments(args);
            mainFragment = tabFragments[menuNumber];

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_menu, menu);
            transaction.replace(R.id.fragment_main, mainFragment);
            transaction.commit();
        }
    }

    @Override
    public void onMenuChange(int index) {
        menuNumber = index;
        mainFragment = tabFragments[index];
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_main, mainFragment);

        if (!isStarting) {
            transaction.addToBackStack(null);
        } else {
            isStarting = false;
        }
        transaction.commit();
    }

    @Override
    public void onFragmentDisplayed(int fragmentId) {
        if (menuNumber != fragmentId) {
            menuNumber = fragmentId;
            if (menu != null) {
                menu.setCurrentActivatedIndex(menuNumber);
            }
        }
    }

    @Override
    public void onClick(int numFragment) {
        Log.d(TAG, "Click on fragment " + numFragment);
    }

    @Override
    public void onDataChange(int numFragment, Object object, int actionCode, Object argsAction) {
        if (actionCode == 0) {
            Issue newIssue = (Issue) object;
            ReportMapModel.getInstance().addIssue(newIssue);
            mainFragment = ReportDetailFragment.newInstance(newIssue);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_main, mainFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (actionCode == 1) {
            Issue selectedIssue = (Issue) object;
            mainFragment = ReportDetailFragment.newInstance(selectedIssue);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_main, mainFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (actionCode == 3) {
            mainFragment = new ReportLocationFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_main, mainFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (actionCode == 4) {
            Bundle locationData = (Bundle) object;
            String address = locationData.getString("address");
            double lat = locationData.getDouble("lat");
            double lng = locationData.getDouble("lng");
            mainFragment = ReportDescriptionFragment.newInstance(address, lat, lng);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_main, mainFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(DATA_IS_STARTING, isStarting);
        outState.putInt(DATA_MENU_NUMBER, menuNumber);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isStarting = savedInstanceState.getBoolean(DATA_IS_STARTING);
        menuNumber = savedInstanceState.getInt(DATA_MENU_NUMBER);
    }
}
