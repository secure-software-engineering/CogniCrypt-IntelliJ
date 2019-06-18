package de.fraunhofer.iem.icognicrypt.core.FileSystem;

import java.nio.file.Path;

public class FileSystemEventsArgs
{
    private Object _sender;
    private boolean _handled;

    private Path _target;
    private String _targetName;

    public Object GetSender()
    {
        return _sender;
    }

    public boolean GetHandled()
    {
        return _handled;
    }

    public void SetHandled(boolean handled)
    {
        _handled = handled;
    }

    public Path GetTarget(){
        return _target;
    }

    public String GetTargetName(){
        return _targetName;
    }

    public FileSystemEventsArgs(Object sender, Path target, String targetName){
        _sender = sender;
        _target = target;
        _targetName = targetName;
    }
}
