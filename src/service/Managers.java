package service;

public class Managers{
    public static TaskManager getDefault(HistoryManager historyManagers){
        return new InMemoryTaskManager(historyManagers);
    }

    public static HistoryManager getDefaultHistory(){

        return new InMemoryHistoryManager();
    }
}
