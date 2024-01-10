package SREnvironment;

public class Human 
{
    private int timePickedUp = 0;
    private int timeRescued = 0;
    int x;
    int y;

    public Human(Integer _x, Integer _y) 
    {
        x = _x;
        y = _y;
    }

    public void setTimePickedUp(int timePickedUp) {
        if (timePickedUp == 0)
        {
            this.timePickedUp = timePickedUp;
        }
    }

    public void setTimeRescued(int timeRescued) {
        this.timeRescued = timeRescued;
    }

    public int getRescueLength() 
    {
        return timeRescued - timePickedUp;
    }

    public int getTimePickedUp() 
    {
        return timePickedUp;
    }

    public void setNewPos(int x2, int y2) 
    {
        x = x2;
        y = y2;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
