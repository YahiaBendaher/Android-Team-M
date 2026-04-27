package edu.polytech.filrouge_teamM.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import edu.polytech.filrouge_teamM.R;
import edu.polytech.filrouge_teamM.contract.Notifiable;
import edu.polytech.filrouge_teamM.factory.AccidentFactory;
import edu.polytech.filrouge_teamM.factory.HighDangerFactory;
import edu.polytech.filrouge_teamM.factory.LowDangerFactory;
import edu.polytech.filrouge_teamM.factory.MediumDangerFactory;
import edu.polytech.filrouge_teamM.model.Issue;

public class DescriptionObstacleFragment extends Fragment {

    public final static int FRAGMENT_ID = 2;
    private final String TAG = "teamM " + getClass().getSimpleName();

    private Notifiable notifiable;

    private String selectedCategory = "Obstacle";
    private String selectedSize = "Petit";
    private String selectedDanger = "Modéré";

    private TextView categoryDechet;
    private TextView categoryObstacle;
    private TextView categoryBranche;
    private TextView categoryCarton;
    private TextView categoryVerre;
    private TextView categoryMobilier;
    private TextView categoryAutre;

    private TextView sizePetit;
    private TextView sizeMoyen;
    private TextView sizeGrand;

    private TextView dangerFaible;
    private TextView dangerModere;
    private TextView dangerEleve;

    public DescriptionObstacleFragment() {
        Log.d(TAG, "DescriptionObstacleFragment created");
    }

    @Override
    public void onStart() {
        super.onStart();

        if (notifiable != null) {
            notifiable.onFragmentDisplayed(FRAGMENT_ID);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (requireActivity() instanceof Notifiable) {
            notifiable = (Notifiable) requireActivity();
        } else {
            throw new AssertionError("Classe " + requireActivity().getClass().getName()
                    + " ne met pas en œuvre Notifiable.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_description_obstacle, container, false);

        EditText commentEditText = view.findViewById(R.id.editComment);
        TextView validateButton = view.findViewById(R.id.btnValidateDescription);
        TextView backButton = view.findViewById(R.id.btnBack);

        initCategoryViews(view);
        initSizeViews(view);
        initDangerViews(view);

        configureCategoryClicks();
        configureSizeClicks();
        configureDangerClicks();

        updateCategorySelection();
        updateSizeSelection();
        updateDangerSelection();

        validateButton.setOnClickListener(v -> {
            String comment = commentEditText.getText().toString().trim();

            AccidentFactory factory;

            if (selectedDanger.equals("Faible")) {
                factory = new LowDangerFactory();
            } else if (selectedDanger.equals("Élevé")) {
                factory = new HighDangerFactory();
            } else {
                factory = new MediumDangerFactory();
            }

            String title = selectedCategory;

            String description = "Catégorie : " + selectedCategory
                    + "\nTaille estimée : " + selectedSize;

            if (!comment.isEmpty()) {
                description += "\nCommentaire : " + comment;
            } else {
                description += "\nCommentaire : Aucun commentaire renseigné.";
            }

            Issue newIssue = factory.createIssue(title, description);

            Toast.makeText(requireContext(), "Signalement ajouté", Toast.LENGTH_SHORT).show();

            if (notifiable != null) {
                notifiable.onDataChange(
                        FRAGMENT_ID,
                        newIssue,
                        0,
                        newIssue.getSafetyProtocol()
                );
            }
        });

        backButton.setOnClickListener(v -> requireActivity().onBackPressed());

        return view;
    }

    private void initCategoryViews(View view) {
        categoryDechet = view.findViewById(R.id.categoryDechet);
        categoryObstacle = view.findViewById(R.id.categoryObstacle);
        categoryBranche = view.findViewById(R.id.categoryBranche);
        categoryCarton = view.findViewById(R.id.categoryCarton);
        categoryVerre = view.findViewById(R.id.categoryVerre);
        categoryMobilier = view.findViewById(R.id.categoryMobilier);
        categoryAutre = view.findViewById(R.id.categoryAutre);
    }

    private void initSizeViews(View view) {
        sizePetit = view.findViewById(R.id.sizePetit);
        sizeMoyen = view.findViewById(R.id.sizeMoyen);
        sizeGrand = view.findViewById(R.id.sizeGrand);
    }

    private void initDangerViews(View view) {
        dangerFaible = view.findViewById(R.id.dangerFaible);
        dangerModere = view.findViewById(R.id.dangerModere);
        dangerEleve = view.findViewById(R.id.dangerEleve);
    }

    private void configureCategoryClicks() {
        categoryDechet.setOnClickListener(v -> {
            selectedCategory = "Déchet";
            updateCategorySelection();
        });

        categoryObstacle.setOnClickListener(v -> {
            selectedCategory = "Obstacle";
            updateCategorySelection();
        });

        categoryBranche.setOnClickListener(v -> {
            selectedCategory = "Branche";
            updateCategorySelection();
        });

        categoryCarton.setOnClickListener(v -> {
            selectedCategory = "Carton/Sac";
            updateCategorySelection();
        });

        categoryVerre.setOnClickListener(v -> {
            selectedCategory = "Verre/Débris";
            updateCategorySelection();
        });

        categoryMobilier.setOnClickListener(v -> {
            selectedCategory = "Mobilier";
            updateCategorySelection();
        });

        categoryAutre.setOnClickListener(v -> {
            selectedCategory = "Autre";
            updateCategorySelection();
        });
    }

    private void configureSizeClicks() {
        sizePetit.setOnClickListener(v -> {
            selectedSize = "Petit";
            updateSizeSelection();
        });

        sizeMoyen.setOnClickListener(v -> {
            selectedSize = "Moyen";
            updateSizeSelection();
        });

        sizeGrand.setOnClickListener(v -> {
            selectedSize = "Grand";
            updateSizeSelection();
        });
    }

    private void configureDangerClicks() {
        dangerFaible.setOnClickListener(v -> {
            selectedDanger = "Faible";
            updateDangerSelection();
        });

        dangerModere.setOnClickListener(v -> {
            selectedDanger = "Modéré";
            updateDangerSelection();
        });

        dangerEleve.setOnClickListener(v -> {
            selectedDanger = "Élevé";
            updateDangerSelection();
        });
    }

    private void updateCategorySelection() {
        resetBlueChip(categoryDechet);
        resetBlueChip(categoryObstacle);
        resetBlueChip(categoryBranche);
        resetBlueChip(categoryCarton);
        resetBlueChip(categoryVerre);
        resetBlueChip(categoryMobilier);
        resetBlueChip(categoryAutre);

        if (selectedCategory.equals("Déchet")) selectBlueChip(categoryDechet);
        if (selectedCategory.equals("Obstacle")) selectBlueChip(categoryObstacle);
        if (selectedCategory.equals("Branche")) selectBlueChip(categoryBranche);
        if (selectedCategory.equals("Carton/Sac")) selectBlueChip(categoryCarton);
        if (selectedCategory.equals("Verre/Débris")) selectBlueChip(categoryVerre);
        if (selectedCategory.equals("Mobilier")) selectBlueChip(categoryMobilier);
        if (selectedCategory.equals("Autre")) selectBlueChip(categoryAutre);
    }

    private void updateSizeSelection() {
        resetBlueChip(sizePetit);
        resetBlueChip(sizeMoyen);
        resetBlueChip(sizeGrand);

        if (selectedSize.equals("Petit")) selectBlueChip(sizePetit);
        if (selectedSize.equals("Moyen")) selectBlueChip(sizeMoyen);
        if (selectedSize.equals("Grand")) selectBlueChip(sizeGrand);
    }

    private void updateDangerSelection() {
        resetDangerChip(dangerFaible);
        resetDangerChip(dangerModere);
        resetDangerChip(dangerEleve);

        if (selectedDanger.equals("Faible")) selectBlueChip(dangerFaible);
        if (selectedDanger.equals("Modéré")) selectOrangeChip(dangerModere);
        if (selectedDanger.equals("Élevé")) selectBlueChip(dangerEleve);
    }

    private void resetBlueChip(TextView chip) {
        chip.setBackgroundResource(R.drawable.bg_screen3_chip);
        chip.setTextColor(0xFF1D2A44);
    }

    private void resetDangerChip(TextView chip) {
        chip.setBackgroundResource(R.drawable.bg_screen3_chip);
        chip.setTextColor(0xFF1D2A44);
    }

    private void selectBlueChip(TextView chip) {
        chip.setBackgroundResource(R.drawable.bg_screen3_chip_selected_blue);
        chip.setTextColor(0xFF0D4DFF);
    }

    private void selectOrangeChip(TextView chip) {
        chip.setBackgroundResource(R.drawable.bg_screen3_chip_selected_orange);
        chip.setTextColor(0xFFC94A1B);
    }
}