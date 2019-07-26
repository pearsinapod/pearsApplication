package com.fb.pearsapplication;

import com.fb.pearsapplication.models.Group;

import java.util.ArrayList;
import java.util.List;

public class Powerset {
    public static List<List<Group>> subsets(Group[] groups) {
        List<List<Group>> solution = new ArrayList<>();
        List<Group> temp_list = new ArrayList<>();
        recursion(solution,temp_list,0,groups);
        return solution;
    }

    public static void recursion(List<List<Group>> solution, List<Group> temp_list, int index, Group[] groups){
        if(temp_list.size() > groups.length){
            return;
        }
        solution.add(new ArrayList<>(temp_list));
        for(int i=index; i<groups.length;i++){
            temp_list.add(groups[i]);
            recursion(solution, temp_list, i+1, groups);
            temp_list.remove(temp_list.size()-1);
        }
    }

}

