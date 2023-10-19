public class StrategyTaskRunner implements TaskRunner {
    private Container container;

    public StrategyTaskRunner(Strategy strategy){
        container = TaskContainerFactory.getInstance().createContainer(strategy);
    }

    @Override
    public void executeOneTask() {
        Task t = container.remove();
        t.execute();
    }

    @Override
    public void executeAll() {
        while (this.hasTask())
            executeOneTask();
    }

    @Override
    public void addTask(Task task) {
        container.add(task);
    }

    @Override
    public boolean hasTask() {
        return !container.isEmpty();
    }
}
