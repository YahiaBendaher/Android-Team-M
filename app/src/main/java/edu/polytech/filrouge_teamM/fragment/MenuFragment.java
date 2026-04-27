package edu.polytech.filrouge_teamM.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import edu.polytech.filrouge_teamM.R;
import edu.polytech.filrouge_teamM.contract.Menuable;


public class MenuFragment extends Fragment{
    private final String TAG = "teamM "+getClass().getSimpleName();
    private Menuable menuable;
    private int currentActivatedIndex = 0;
    private View layout;
    private final String[] iconNames = {"accueil", "carte", "plus", "liste", "suivi"};


    public MenuFragment() {
    }

    public void setCurrentActivatedIndex(int index){
        Log.d(TAG,"setCurrentActivatedIndex updated to " + index +" (currentActivatedIndex = "+currentActivatedIndex+")");
        List<ImageView> imageViews = findPicturesMenuFromId(layout.findViewById(R.id.itemsMenu));
        
        if (currentActivatedIndex >= 0 && currentActivatedIndex < imageViews.size() && currentActivatedIndex < iconNames.length) {
            imageViews.get(currentActivatedIndex).setImageResource(layout.getResources().getIdentifier(iconNames[currentActivatedIndex], "mipmap", layout.getContext().getPackageName()));
        }
        
        if (index >= 0 && index < imageViews.size() && index < iconNames.length) {
            imageViews.get(index).setImageResource(layout.getResources().getIdentifier(iconNames[index] + "_s", "mipmap", layout.getContext().getPackageName()));
        }
        currentActivatedIndex = index;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_menu, container, false);

        List<ImageView> imageViews = findPicturesMenuFromId(layout.findViewById(R.id.itemsMenu));

        if (getArguments() != null) {
            currentActivatedIndex = getArguments().getInt(getString(R.string.index), 0);
        }
        
        if (currentActivatedIndex >= 0 && currentActivatedIndex < imageViews.size() && currentActivatedIndex < iconNames.length) {
            imageViews.get(currentActivatedIndex).setImageResource(layout.getResources().getIdentifier(iconNames[currentActivatedIndex] + "_s", "mipmap", layout.getContext().getPackageName()));
        }

        menuable.onMenuChange(currentActivatedIndex);

        for(ImageView imageView : imageViews) {
            if (imageView.getParent() instanceof View) {
                ((View) imageView.getParent()).setOnClickListener(v -> imageView.performClick());
            }

            imageView.setOnClickListener(v -> {
                int oldIndex = currentActivatedIndex;
                currentActivatedIndex = Integer.parseInt(imageView.getTag().toString());

                menuable.onMenuChange(currentActivatedIndex);

                if (oldIndex >= 0 && oldIndex < imageViews.size() && oldIndex < iconNames.length) {
                    imageViews.get(oldIndex).setImageResource(layout.getResources().getIdentifier(iconNames[oldIndex], "mipmap", layout.getContext().getPackageName()));
                }

                if (currentActivatedIndex >= 0 && currentActivatedIndex < imageViews.size() && currentActivatedIndex < iconNames.length) {
                    imageView.setImageResource(layout.getResources().getIdentifier(iconNames[currentActivatedIndex] + "_s", "mipmap", layout.getContext().getPackageName()));
                }
            });
        }
        return layout;
    }

    private List<ImageView> findPicturesMenuFromId(View view) {
        List<ImageView> pictures = new ArrayList<>();
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = viewGroup.getChildAt(i);
                if (child instanceof ImageView) {
                    String idString = getResources().getResourceEntryName(child.getId());
                    if (idString.matches("menu[1-9]?")) {
                        pictures.add((ImageView) child);
                    }
                } else if (child instanceof ViewGroup) {
                    pictures.addAll(findPicturesMenuFromId(child));
                }
            }
        }
        return pictures;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (requireActivity() instanceof Menuable) {
            menuable = (Menuable) requireActivity();
        } else {
            throw new AssertionError("Classe " + requireActivity().getClass().getName() + " ne met pas en œuvre Notifiable.");
        }
    }

}
