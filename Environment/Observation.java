package Environment;

public abstract class Observation 
{
    private String type;

    public Observation(String _type)
    {
        type = _type;
    }

    public String getType()
    {
        return type;
    }
}
