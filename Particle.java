package ir.mrgkrahimy.pso_implementation;

import java.util.Arrays;

/**
 * Created by vincent on 2/19/2017.
 */
public class Particle {
    public float[] position;
    public float cost;
    public float[] velocity;

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

    public float[] getVelocity() {
        return velocity;
    }

    public void setVelocity(float[] velocity) {
        this.velocity = velocity;
    }

    public Experience getPrivateBest() {
        return privateBest;
    }

    public void setPrivateBest(Experience privateBest) {
        this.privateBest = privateBest;
    }

    public Particle(float[] position, float cost, float[] velocity, Experience privateBest) {

        this.position = position;
        this.cost = cost;
        this.velocity = velocity;
        this.privateBest = privateBest;
    }

    public Particle(Particle p) {
        this.position = p.position;
        this.cost = p.cost;
        this.velocity = p.velocity;
        this.privateBest = p.privateBest;
    }

    public Experience privateBest;

    @Override
    public String toString() {
        return "Particle{" +
                "position=" + Arrays.toString(position) +
                ", cost=" + cost +
                ", velocity=" + Arrays.toString(velocity) +
                ", privateBest=" + privateBest.toString() +
                '}';
    }
}
