package com.example.curs9.ubbcluj.map.domain.MyUtils;

import com.example.curs9.ubbcluj.map.domain.MyModels.Friendship;
import com.example.curs9.ubbcluj.map.domain.MyModels.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LongestRoad {
    private ArrayList<User> users;
    private ArrayList<Friendship> friendships;
    private ArrayList<Path> allPossiblePaths;
    ArrayList<Path> allPathsBetween;

    public LongestRoad(ArrayList<User> users, ArrayList<Friendship> friendships) {
        this.users = users;
        this.friendships = friendships;
        this.allPossiblePaths = new ArrayList<>();
        ArrayList<Path> allPathsBetween = new ArrayList<>();
    }

    public Path maxLength(ArrayList<Path> all) {

        Integer maxLength = 0;
        Path result = null;

        for (var x : all) {
            if (x.getUsersInPath().size() > maxLength) {
                maxLength = x.getUsersInPath().size();
                result = x;
            }
        }

        return result;
    }

    public ArrayList<String> calculate() {
        calculateAllPossiblePaths();

        var result = maxLength(allPossiblePaths);

        return result.getUsersInPath();
    }

    public void calculateAllPossiblePaths() {

        for (var x : users) {
            for (var y : users) {
                if (y.getId().compareTo(x.getId()) > 0)   {
                    ArrayList<Path> pathArrayList = null;
                    pathArrayList = getAllPathsBetween(x.getId(), y.getId());

                    if (pathArrayList != null) {
                        for (var z : pathArrayList) {
                            allPossiblePaths.add(z);
                        }
                    }
                }
            }
        }

    }

    private ArrayList<Path> getAllPathsBetween(String id1, String id2) {
        Path currentPath = new Path();
        Map<String, Integer> visited = new HashMap<>();

        allPathsBetween = new ArrayList<>();

        for (var x : users) {
            visited.put(x.getId(), 0);
        }

        currentPath.addToPath(id1);

        DFS(id1, id2, visited, currentPath);

        return allPathsBetween;
    }

    private void DFS(String id1, String id2, Map<String, Integer> visited, Path currentPath) {
        /*if (visited.get(id1) == 1) {
            return;
        }

        visited.put(id1, 1);

        currentPath.addToPath(id1);

        if (id1.equals(id2)) {
            allPathsBetween.add(currentPath);

            visited.put(id1, 0);

            currentPath.removeLastFromPath();

            return;
        }

        for (var x : getFriendsOfUserID(id1)) {
            DFS(x, id2, visited, allPathsBetween, currentPath);
        }

        currentPath.removeLastFromPath();
        visited.put(id1, 0);*/

        if (id1.equals(id2)) {
            allPathsBetween.add(currentPath);

            return;
        }

        visited.put(id1, 1);

        var friendsofId1 = getFriendsOfUserID(id1);
        String y;

        for (var x : friendsofId1) {
            y = x;
            if (visited.get(y) == 0) {
                currentPath.addToPath(y);
                DFS(y, id2, visited, currentPath);
                currentPath.removeFromPath(y);
            }
        }

        visited.put(id1, 0);
    }

    private ArrayList<String> getFriendsOfUserID (String ID) {
        ArrayList<String> friendsOfUser = new ArrayList<>();

        for (var x : friendships) {
            if (x.hasUserByID(ID)) {
                if (x.getId1().equals(ID)) {
                    friendsOfUser.add(x.getId2());
                }
                else
                {
                    friendsOfUser.add(x.getId1());
                }
            }
        }

        return friendsOfUser;
    }
}
