package com.fb.pearsapplication.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fb.pearsapplication.R;
import com.fb.pearsapplication.adapters.groupsAdapter;
import com.fb.pearsapplication.models.Group;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static com.parse.Parse.getApplicationContext;

public class searchFragment extends Fragment {

    protected ArrayList<Group> searchGroups;
    protected groupsAdapter searchAdapter;
    private RecyclerView rvSearchAdapter;
    private SwipeRefreshLayout searchSwipeContainer;
    EditText etSearch;

   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_search, container, false);
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
       rvSearchAdapter = view.findViewById(R.id.rvSearchGroups);
       searchGroups = new ArrayList<>();
       searchAdapter = new groupsAdapter(getContext(), searchGroups);
       rvSearchAdapter.setAdapter(searchAdapter);
       GridLayoutManager searchExploreGridLayout = new GridLayoutManager(getApplicationContext(), 2);
       rvSearchAdapter.setLayoutManager(searchExploreGridLayout);

       searchSwipeContainer = view.findViewById(R.id.searchSwipeContainer);
       etSearch = view.findViewById(R.id.etSearch);

       ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
       updatingListAdapterSearch(getQuerySearch());
       setUpEditorListener();
       setUpSearchSwipeContainer();
       setUpOnTextChangedSearch();
   }

   public void setUpSearchSwipeContainer(){
       searchSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {

               searchAdapter.clear();
               searchGroups.clear();
               updatingListAdapterSearch(getQuerySearch());
               searchSwipeContainer.setRefreshing(false);

           }
       });
       searchSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
               android.R.color.holo_green_light,
               android.R.color.holo_orange_light,
               android.R.color.holo_red_light);
   }

   public void hideSoftKeyboard(Activity activity) {
       InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
       inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
   }

   public void setUpEditorListener(){
       etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
           @Override
           public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
               hideSoftKeyboard(getActivity());
               return true;
           }
       });
   }

   public String getSearchedText(){
       return etSearch.getText().toString();
   }

   protected ParseQuery getQuerySearch(){
       Group.Query groupsQuery = new Group.Query();
       groupsQuery.addDescendingOrder(Group.KEY_CREATED_AT);
       if (!getSearchedText().equals("")){
           groupsQuery.whereMatches(Group.KEY_GROUP_NAME, "(?i)^" + getSearchedText());
       }
       return groupsQuery;
   }

       // can be used to alphabatize : groupsQuery.addAscendingOrder(Group.KEY_GROUP_NAME);
       //groupsQuery.whereContains(Group.KEY_GROUP_NAME, getSearchedText());

   public void setUpOnTextChangedSearch(){

       etSearch.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

           @Override
           public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
               updatingListAdapterSearch(getQuerySearch());
           }

           @Override
           public void afterTextChanged(Editable editable) { }
       });
   }

   protected void updatingListAdapterSearch(final ParseQuery groupsQuery){
       searchGroups.clear();
       searchAdapter.notifyDataSetChanged();
       groupsQuery.findInBackground(new FindCallback<Group>() {
           @Override
           public void done(List<Group> objects, ParseException e) {
               if (e == null) {
                   for (int i = 0; i < objects.size(); i++) {
                       Group group = objects.get(i);
                       if(!group.getUsers().contains(ParseUser.getCurrentUser())) {
                           searchGroups.add(group);
                           searchAdapter.notifyDataSetChanged();
                       }
                   }

               }
               else{
                   Log.d("Explore Fragment","Loading items failed");
               }
           }
       });
   }
/*   TODO:
       get top should be fixed+ endless scorlling

*/



}
