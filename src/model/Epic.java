package model;

import java.util.ArrayList;
import java.util.List;


public class Epic extends Task {

   private List<Integer> subTasks = new ArrayList<>();

   public Epic(String name, String description, Status status) {
      super(name, description, status);
   }

   public List<Integer> getSubTasksIds() {
      return subTasks;
   }

   public void setSubTasksIds(int id) {
      subTasks.add(id);
   }
}

