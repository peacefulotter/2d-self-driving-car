package com.peacefulotter.selfdrivingcar.ml.genetic;

public class GeneticParams
{
    private int population;
    private double crossoverRate, mutationStrength, mutationRate;

    public GeneticParams(int population, double crossoverRate, double mutationStrength, double mutationRate)
    {
        this.population = population;
        this.crossoverRate = crossoverRate;
        this.mutationStrength = mutationStrength;
        this.mutationRate = mutationRate;
    }

    public int getPopulation() {
        return population;
    }

    public double getCrossoverRate() {
        return crossoverRate;
    }

    public double getMutationStrength() {
        return mutationStrength;
    }

    public double getMutationRate() {
        return mutationRate;
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

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }
}
