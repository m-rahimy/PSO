package ir.mrgkrahimy.pso_implementation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static ir.mrgkrahimy.pso_implementation.CostFunctions.sphere;
import static ir.mrgkrahimy.pso_implementation.MatrixHelper.*;
import static ir.mrgkrahimy.pso_implementation.MatrixHelper.max;

/**
 * Created by vincent on 2/21/2017.
 */
public class PSO {

    private List<Particle[]> particlesList = new ArrayList<>();
    private Random random = new Random(new Date().getTime());
    private float[] bestCosts;

    private int nVar, varMin, varMax, nPop, maxIteration;
    private float w, c1, c2;
    float[] lowerBounds, upperBounds;

    public PSO(int nVar, int varMin, int varMax, int nPop, int maxIteration, float w, float c1, float c2) {
        this.nVar = nVar;
        this.varMax = varMax;
        this.varMin = varMin;
        this.nPop = nPop;
        this.maxIteration = maxIteration;
        this.w = w;
        this.c1 = c1;
        this.c2 = c2;
    }

    public void compute() {

        Particle[] particles = new Particle[nPop];
        float[] init = new float[nVar];
        Experience globalBest = new Experience(init, (float) Math.pow(2, 32)); // 2^32 is max here
        lowerBounds = new float[maxIteration];
        upperBounds = new float[maxIteration];

        for (int i = 0; i < nPop; i++) {
            float[] position = new float[nVar];
            for (int j = 0; j < nVar; j++) {
                position[j] = random.nextFloat() * (varMax - varMin) + varMin;
            }
            float cost = sphere(position);
            particles[i] = new Particle(position, cost, init, new Experience(position, cost));
            if (cost < globalBest.cost) {
                globalBest = particles[i].privateBest;
            }
        }

        bestCosts = new float[maxIteration];
        for (int ii = 0; ii < maxIteration; ii++) {
            bestCosts[ii] = 0.0f;
        }

        List<Float[]> positions = new ArrayList<>();
        for (int iter = 0; iter < maxIteration; iter++) {

            Particle[] temp = new Particle[nPop];
            for (int cop = 0; cop < nPop; cop++) {
                Particle p = particles[cop];
                temp[cop] = new Particle(p);
            }

            particlesList.add(temp);
            for (int i = 0; i < nPop; i++) {
                //1
                float rand = Math.abs(random.nextFloat());
                particles[i].velocity =
                        addArrays(
                                multArray(particles[i].velocity, w),
                                multArray(
                                        subtractArray(
                                                particles[i].privateBest.position,
                                                particles[i].position),
                                        c1 * rand
                                ),
                                multArray(
                                        subtractArray(
                                                globalBest.position,
                                                particles[i].position
                                        ),
                                        c2 * rand));

                //2 update pos
                particles[i].position = addArrays(particles[i].position, particles[i].velocity);
                // rebound pos
                particles[i].position = min(max(particles[i].position, varMin), varMax);

                // add to positions
                Float[] f = new Float[nVar];
                int it = 0;
                for (float fl : particles[i].position) {
                    f[it] = fl;
                }
                positions.add(f);

                //eval pos
                particles[i].cost = sphere(particles[i].position);

                // compare pos with pbest
                if (particles[i].cost < particles[i].privateBest.cost) {
                    particles[i].privateBest.position = particles[i].position;
                    particles[i].privateBest.cost = particles[i].cost;

                    // compare pbest with global best
                    if (particles[i].privateBest.cost < globalBest.cost) {
                        globalBest = particles[i].privateBest;
                    }

                }
            }
            bestCosts[iter] = globalBest.cost;
            System.out.println("iter : " + iter + " best: " + globalBest.cost);
        }
    }

    public List<Particle[]> getParticlesList() {
        return particlesList;
    }

    public int getnVar() {
        return nVar;
    }

    public void setnVar(int nVar) {
        this.nVar = nVar;
    }

    public int getVarMin() {
        return varMin;
    }

    public void setVarMin(int varMin) {
        this.varMin = varMin;
    }

    public int getVarMax() {
        return varMax;
    }

    public void setVarMax(int varMax) {
        this.varMax = varMax;
    }

    public int getnPop() {
        return nPop;
    }

    public void setnPop(int nPop) {
        this.nPop = nPop;
    }

    public int getMaxIteration() {
        return maxIteration;
    }

    public void setMaxIteration(int maxIteration) {
        this.maxIteration = maxIteration;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }

    public float getC1() {
        return c1;
    }

    public void setC1(float c1) {
        this.c1 = c1;
    }

    public float getC2() {
        return c2;
    }

    public void setC2(float c2) {
        this.c2 = c2;
    }

    public List<Particle[]> getAll() {
        return particlesList;
    }

    public float[] getBestCosts() {
        return bestCosts;
    }
}
