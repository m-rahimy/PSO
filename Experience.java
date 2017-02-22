package ir.mrgkrahimy.pso_implementation;

/**
 * Created by vincent on 2/19/2017.
 */
public class Experience {
    public float[] position;
    public float cost;

    public float[] getPosition() {
        return position;
    }

    public void setPosition(float[] position) {
        this.position = position;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public Experience(float[] position, float cost) {

        this.position = position;
        this.cost = cost;
    }
}
