package edu.polytech.filrouge_teamM.navigation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

import edu.polytech.filrouge_teamM.R;

public class MenuFragment extends Fragment {
    private Menuable menuable;
    private int currentActivatedIndex = 0;
    private View layout;

    private final int[] normalIcons = {
            R.mipmap.accueil,
            R.mipmap.carte,
            R.mipmap.plus,
            R.mipmap.liste,
            R.mipmap.suivi
    };

    private final int[] selectedIcons = {
            R.mipmap.accueil_s,
            R.mipmap.carte_s,
            R.mipmap.plus_s,
            R.mipmap.liste_s,
            R.mipmap.suivi_s
    };

    public MenuFragment() {
    }

    public void setCurrentActivatedIndex(int index) {
        if (layout == null) {
            currentActivatedIndex = index;
            return;
        }
        List<ImageView> imageViews = findPicturesMenuFromId(layout.findViewById(R.id.itemsMenu));
        
        for (int i = 0; i < imageViews.size(); i++) {
            if (i < normalIcons.length && i < selectedIcons.length) {
                if (i == index) {
                    imageViews.get(i).setImageResource(selectedIcons[i]);
                } else {
                    imageViews.get(i).setImageResource(normalIcons[i]);
                }
            }
        }
        currentActivatedIndex = index;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_menu, container, false);

        if (getArguments() != null) {
            currentActivatedIndex = getArguments().getInt(getString(R.string.index), 0);
        }

        List<ImageView> imageViews = findPicturesMenuFromId(layout.findViewById(R.id.itemsMenu));

        for (int i = 0; i < imageViews.size(); i++) {
            if (i < normalIcons.length && i < selectedIcons.length) {
                if (i == currentActivatedIndex) {
                    imageViews.get(i).setImageResource(selectedIcons[i]);
                } else {
                    imageViews.get(i).setImageResource(normalIcons[i]);
                }
            }
            
            final int index = i;
            ImageView imageView = imageViews.get(i);
            
            if (imageView.getParent() instanceof View) {
                ((View) imageView.getParent()).setOnClickListener(v -> handleMenuClick(index));
            }
            imageView.setOnClickListener(v -> handleMenuClick(index));
        }

        return layout;
    }

    private void handleMenuClick(int index) {
        if (menuable != null) {
            menuable.onMenuChange(index);
        }
        setCurrentActivatedIndex(index);
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (requireActivity() instanceof Menuable) {
            menuable = (Menuable) requireActivity();
        } else {
            throw new AssertionError("Classe " + requireActivity().getClass().getName() + " ne met pas en œuvre Menuable.");
        }
    }
}
