package com.example.curs9.ubbcluj.map.domain.MyUtils;

import java.util.ArrayList;
import java.util.Objects;

public class Path {
    private ArrayList<String> usersInPath;

    public Path(ArrayList<String> usersInPath) {
        this.usersInPath = usersInPath;
    }

    public Path() {
        this.usersInPath = new ArrayList<>();
    }

    public void addToPath(String ID) {
        usersInPath.add(ID);
    }

    public void removeFromPath(String ID) {
        usersInPath.remove(ID);
    }

    public void removeLastFromPath() {
        usersInPath.remove(usersInPath.size() - 1);
    }

    public ArrayList<String> getUsersInPath() {
        return usersInPath;
    }

    public void setUsersInPath(ArrayList<String> usersInPath) {
        this.usersInPath = usersInPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path path = (Path) o;
        return Objects.equals(usersInPath, path.usersInPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usersInPath);
    }
}
