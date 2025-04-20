import service.InMemoryTaskManager;
import service.Managers;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager createManager() {
        return new InMemoryTaskManager(Managers.getDefaultHistory());
    }
}