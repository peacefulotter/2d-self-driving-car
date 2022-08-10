package com.peacefulotter.selfdrivingcar.scenarios.defaults;

import com.peacefulotter.selfdrivingcar.ml.genetic.Genetic;

public class DefaultGenetic {
    private static final int POPULATION = 500;
    private static final double CROSSOVER_RATE = 0.1d;
    private static final double MUTATION_STRENGTH = 2d;
    private static final double MUTATION_RATE = 0.030d;

    public static Genetic GENETIC = new Genetic( POPULATION, CROSSOVER_RATE, MUTATION_STRENGTH, MUTATION_RATE );
}
