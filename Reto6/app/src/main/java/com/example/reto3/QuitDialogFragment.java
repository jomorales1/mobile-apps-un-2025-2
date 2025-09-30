package com.example.reto3;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class QuitDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setMessage(R.string.quit_question)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, (dialog, id) -> requireActivity().finish())
                .setNegativeButton(R.string.no, null)
                .create();
    }
}
