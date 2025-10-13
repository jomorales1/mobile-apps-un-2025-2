package com.example.reto8;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AlertDialogFragment extends DialogFragment {

    public interface AlertDialogListener {
        void onDeleteConfirmed();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setMessage(R.string.deleteContact)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    if (getActivity() instanceof AlertDialogListener) {
                        ((AlertDialogListener) getActivity()).onDeleteConfirmed();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .create();
    }
}
