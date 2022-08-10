package com.peacefulotter.selfdrivingcar.ml.genetic;

public class GeneticParams
{
    public int population;
    public double crossoverRate, mutationStrength, mutationRate;

    public GeneticParams(int population, double crossoverRate, double mutationStrength, double mutationRate)
    {
        this.population = population;
        this.crossoverRate = crossoverRate;
        this.mutationStrength = mutationStrength;
        this.mutationRate = mutationRate;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public void setCrossoverRate(double crossoverRate) {
        this.crossoverRate = crossoverRate;
    }

    public void setMutationStrength(double mutationStrength) {
        this.mutationStrength = mutationStrength;
    }

    public void setMutationRate(double mutationRate) { this.mutationRate = mutationRate; }
}
