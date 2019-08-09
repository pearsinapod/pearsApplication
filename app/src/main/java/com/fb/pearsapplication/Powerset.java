package com.fb.pearsapplication;

import java.util.ArrayList;
import java.util.List;

public class Powerset {
    public static List<List<String>> subsets(String[] groups) {
        List<List<String>> solution = new ArrayList<>();
        List<String> temp_list = new ArrayList<>();
        recursion(solution,temp_list,0,groups,4);
        return solution;
    }

    public static void recursion(List<List<String>> solution, List<String> temp_list, int index, String[] groups, int limit){
        if(temp_list.size() > groups.length || temp_list.size()>limit){
            return;
        }
        solution.add(new ArrayList<>(temp_list));
        for(int i=index; i<groups.length;i++){
            temp_list.add(groups[i]);
            recursion(solution, temp_list, i+1, groups,4);
            temp_list.remove(temp_list.size()-1);
        }
    }

    public static void main(String args[]){

    }


}

