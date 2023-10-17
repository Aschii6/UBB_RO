public class TaskContainerFactory implements Factory {
    private final static TaskContainerFactory taskContainerFactory = new TaskContainerFactory();
    private TaskContainerFactory(){}

    public static TaskContainerFactory getInstance(){
        return taskContainerFactory;
    }

    @Override
    public Container createContainer(Strategy strategy) {
        return switch (strategy){
            case FIFO -> new StackContainer();
            case LIFO -> new QueueContainer();
        };
    }
}
