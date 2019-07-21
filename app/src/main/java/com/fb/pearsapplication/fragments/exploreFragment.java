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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fb.pearsapplication.R;
import com.fb.pearsapplication.adapters.exploreAdapter;
import com.fb.pearsapplication.models.Group;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import static com.parse.Parse.getApplicationContext;

public class exploreFragment extends Fragment {

    public static final Group.Query groupsQuery = new Group.Query();

    protected ArrayList<Group> exploreGroups;
   protected exploreAdapter eAdapter;
   private RecyclerView rvExploreGroups;
   private SwipeRefreshLayout swipeContainer;
   EditText etSearch;

   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_explore, container, false);
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
       rvExploreGroups = view.findViewById(R.id.rvExploreGroups);
       exploreGroups = new ArrayList<>();
       eAdapter = new exploreAdapter(exploreGroups);
       rvExploreGroups.setAdapter(eAdapter);
       GridLayoutManager exploreGridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
       rvExploreGroups.setLayoutManager(exploreGridLayoutManager);

       swipeContainer = view.findViewById(R.id.swipeContainer);
       etSearch = view.findViewById(R.id.etSearch);


       updatingListAdapter(getQuery(groupsQuery),getSearchedText());
       setUpEditorListener();
       setUpSwipeContainer();
       setUpOnTextChanged();
   }

   public void setUpSwipeContainer(){
       swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {

               eAdapter.clear();
               exploreGroups.clear();
               updatingListAdapter(getQuery(groupsQuery),getSearchedText());
               swipeContainer.setRefreshing(false);

           }
       });
       swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
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

   protected ParseQuery getQuery(Group.Query groupsQuery){
       if (getSearchedText().equals("")){
           groupsQuery.getTop();
       }
       groupsQuery.addDescendingOrder(Group.KEY_CREATED_AT);
       return groupsQuery;
   }

       // can be used to alphabatize : groupsQuery.addAscendingOrder(Group.KEY_GROUP_NAME);
       //groupsQuery.whereContains(Group.KEY_GROUP_NAME, getSearchedText());

   public void setUpOnTextChanged(){
       etSearch.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

           @Override
           public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
               updatingListAdapter(getQuery(groupsQuery),getSearchedText());
           }

           @Override
           public void afterTextChanged(Editable editable) { }
       });
   }

   protected void updatingListAdapter(ParseQuery groupsQuery, final String getSearchedText){
       exploreGroups.clear();
       eAdapter.notifyDataSetChanged();
       groupsQuery.findInBackground(new FindCallback<Group>() {
           @Override
           public void done(List<Group> objects, ParseException e) {
               if (e == null) {
                   if(getSearchedText.equals("")) {
                       exploreGroups.addAll(objects);
                       eAdapter.notifyDataSetChanged();
                   }
                   else {
                       for (int i = 0; i < objects.size(); i++) {
                               Group group = objects.get(i);
                               String lowercaseGroup = group.getGroupName().toLowerCase();
                               if (lowercaseGroup.length()>= getSearchedText().length() && getSearchedText().equals(lowercaseGroup.substring(0, getSearchedText().length()))) {
                                   exploreGroups.add(group);
                                   eAdapter.notifyDataSetChanged();
                               }
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
       user searches .. should not contain their groups

*/



}
