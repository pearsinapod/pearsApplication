package com.fb.pearsapplication.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.fb.pearsapplication.R;

public class exploreFragmentParent extends Fragment {

    static EditText etSearch;
    Button btnSearchSubmit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore_parent, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        etSearch = view.findViewById(R.id.etSearch);
        btnSearchSubmit = view.findViewById(R.id.btnSearchSubmit);
        insertNestedFragment();
        setUpOnSubmitListener();
    }

    private void insertNestedFragment() {
        Fragment childFragment = new exploreFragmentChildNoSearch();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container_rv, childFragment).commit();
    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static String getSearchedText(){
        return etSearch.getText().toString();
    }

    public static int lengthSearchedText(){
        return getSearchedText().length();
    }

    public void setUpOnSubmitListener(){
        btnSearchSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(getActivity());
                Log.d("Search Submit","Clicked");
                displayFragmentA();

            }
        });
    }

    protected void displayFragmentA() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.child_fragment_container_rv,new exploreFragmentChildWhenSearch());
        ft.commit();
    }


}
