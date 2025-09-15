package com.example.reto3;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DifficultyDialogFragment extends DialogFragment {

    private TicTacToeGame game;

    public DifficultyDialogFragment(TicTacToeGame game) {
        this.game = game;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.difficulty_choose);

        final CharSequence[] levels = {
                getString(R.string.difficulty_easy),
                getString(R.string.difficulty_harder),
                getString(R.string.difficulty_expert)
        };

        // Get current difficulty from game
        int selected = 0;
        TicTacToeGame.DifficultyLevel current = game.getDifficultyLevel();
        if (current == TicTacToeGame.DifficultyLevel.Harder) {
            selected = 1;
        } else if (current == TicTacToeGame.DifficultyLevel.Expert) {
            selected = 2;
        }

        builder.setSingleChoiceItems(levels, selected, (dialog, item) -> {
            dialog.dismiss();
            if (item == 0) {
                game.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
            } else if (item == 1) {
                game.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
            } else {
                game.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
            }
            Toast.makeText(requireContext(), levels[item], Toast.LENGTH_SHORT).show();
        });

        return builder.create();
    }
}
