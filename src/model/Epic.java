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
      if (id==super.getId())
         throw new RuntimeException("Subtasks Id equals to Epics Id");
      subTasks.add(id);
   }
   @Override
   public String toString() {
      return "Epic{" +
              "subtaskIds=" + subTasks + "name='" + getName() + '\'' +
              ", description=" + getDescription() +
              ", id='" + getId()+ '\'' +
              ", status=" + getStatus() +
              '}';
   }
}

