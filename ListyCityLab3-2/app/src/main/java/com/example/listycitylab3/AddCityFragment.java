package com.example.listycitylab3;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {

    interface AddCityDialogListener {
        void addCity(City city);
        void updateCity(City city);
    }

    private static final String ARG_CITY = "arg_city";
    private AddCityDialogListener listener;

    public static AddCityFragment newInstance(@Nullable City city) {
        AddCityFragment fragment = new AddCityFragment();
        if (city != null) {
            Bundle args = new Bundle();
            args.putSerializable(ARG_CITY, city);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        City editing = null;
        Bundle args = getArguments();
        if (args != null) {
            Object obj = args.getSerializable(ARG_CITY);
            if (obj instanceof City) editing = (City) obj;
        }

        if (editing != null) {
            editCityName.setText(editing.getName());
            editProvinceName.setText(editing.getProvince());
        }

        City finalEditing = editing;
        boolean isEdit = (finalEditing != null);

        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Add/edit city")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", (dialog, which) -> {
                    String name = editCityName.getText().toString();
                    String prov = editProvinceName.getText().toString();
                    if (isEdit) {
                        finalEditing.setName(name);
                        finalEditing.setProvince(prov);
                        listener.updateCity(finalEditing);
                    } else {
                        listener.addCity(new City(name, prov));
                    }
                })
                .create();
    }
}
