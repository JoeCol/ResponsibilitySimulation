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

    @Override
    public boolean equals(Object a)
    {
        if (a != null && a.getClass() == this.getClass())
        {
            Pair<T,T1> toCompare = (Pair<T,T1>)a;
            return toCompare.first.equals(getFirst()) && toCompare.second.equals(getSecond());
        }
        return false;
    }
}
