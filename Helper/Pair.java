package Helper;

public class Pair<T,T1>
{
    private T first;
    private T1 second;

    public T getFirst()
    {
        return first;
    }

    public T1 getSecond()
    {
        return second;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public void setSecond(T1 second) {
        this.second = second;
    }

    public Pair()
    {

    }

    public Pair(T _1, T1 _2)
    {
        first = _1;
        second = _2;
    }
}
