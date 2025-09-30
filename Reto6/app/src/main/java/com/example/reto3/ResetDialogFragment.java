package com.example.reto3;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ResetDialogFragment extends DialogFragment {

    public interface ResetDialogListener {
        void onResetConfirmed();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        builder.setTitle("Reset Scores")
                .setMessage("Are you sure you want to reset all scores?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (getActivity() instanceof ResetDialogListener) {
                        ((ResetDialogListener) getActivity()).onResetConfirmed();
                    }
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        return builder.create();
    }
}
