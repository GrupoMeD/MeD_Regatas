package es.udc.fic.medregatas.util.exceptions;

@SuppressWarnings("serial")
public class InstanceNotFoundException extends InstanceException {

    public InstanceNotFoundException(Object key, String className) {
        super("Instance not found", key, className);
    }

}
