package edu.polytech.filrouge_teamM;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import edu.polytech.filrouge_teamM.contract.Menuable;
import edu.polytech.filrouge_teamM.contract.Notifiable;
import edu.polytech.filrouge_teamM.fragment.DescriptionObstacleFragment;
import edu.polytech.filrouge_teamM.fragment.MenuFragment;
import edu.polytech.filrouge_teamM.fragment.Screen1Fragment;
import edu.polytech.filrouge_teamM.fragment.Screen2Fragment;
import edu.polytech.filrouge_teamM.fragment.Screen3Fragment;
import edu.polytech.filrouge_teamM.fragment.Screen4Fragment;
import edu.polytech.filrouge_teamM.fragment.Screen5Fragment;
import edu.polytech.filrouge_teamM.fragment.Screen6Fragment;
import edu.polytech.filrouge_teamM.fragment.Screen7Fragment;
import edu.polytech.filrouge_teamM.model.Issue;
import edu.polytech.filrouge_teamM.repository.IssueRepository;

public class ControlActivity extends AppCompatActivity implements Menuable, Notifiable {

    private static final String DATA_IS_STARTING = "sauvegarde";
    private static final String DATA_MENU_NUMBER = "num";

    private final String TAG = "teamM " + getClass().getSimpleName();

    private Fragment mainFragment;
    private MenuFragment menu;
    private boolean isStarting = true;

    private Fragment[] tabFragments = {
            new Screen1Fragment(),
            new Screen2Fragment(),
            new Screen3Fragment(),
            new Screen4Fragment(),
            new Screen5Fragment(),
            new Screen6Fragment(),
            new Screen7Fragment()
    };

    private int menuNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        if (savedInstanceState == null) {
            menuNumber = 0;
        }

        Intent intent = getIntent();

        if (intent != null) {
            menuNumber = intent.getIntExtra(getString(R.string.index), 0);
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

        if (actionCode == 3) {
            mainFragment = new DescriptionObstacleFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_main, mainFragment);
            transaction.addToBackStack(null);
            transaction.commit();

            return;
        }

        if (actionCode == 0) {
            Issue newIssue = (Issue) object;

            IssueRepository.addIssue(newIssue);

            if (argsAction instanceof String) {
                Toast.makeText(this, (String) argsAction, Toast.LENGTH_LONG).show();
            }

            menuNumber = 3;
            mainFragment = new Screen4Fragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_main, mainFragment);
            transaction.addToBackStack(null);
            transaction.commit();

            if (menu != null) {
                menu.setCurrentActivatedIndex(menuNumber);
            }

        } else if (actionCode == 1) {
            Issue selectedIssue = (Issue) object;

            mainFragment = Screen1Fragment.newInstance(selectedIssue);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_main, mainFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        } else if (actionCode == 2) {
            Issue updatedIssue = (Issue) object;
            float newRating = (float) argsAction;

            Log.d(TAG, "Update Issue: " + updatedIssue.getTitle()
                    + " with rating: " + newRating);
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