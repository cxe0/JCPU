package se.liu.ferpe211.api.event;

/**
 * Interface for classes that are listening to cpu events
 */
public interface CPUListener
{
    public void cpuChanged(EventType eventType);
}
