package application.utils;

import java.util.Random;

import org.junit.Test;

import application.utils.AnnealingAlgo.SolutionCallback;
import application.utils.AnnealingAlgo.SolutionManager;

public class AnnealingAlgoTest {

	static class Solution {
		static final int[] base = new int[] { 1, 3, 5, 9, 8, 66, 5, 4, 22, 5, 599, 964, 2, 1, 2231, 645, 47, 86, 3, 11, 4, 56, 96, 4, 5,
				2555 };

		final boolean[] population;

		Solution(boolean[] population) {
			this.population = population;
		}

		int evaluate() {
			int sum = 0;
			for (int i = 0; i < population.length; i++) {
				if (population[i])
					sum += base[i];
			}
			return sum;
		}
	}

	@Test
	public void test() throws InterruptedException

	{

		AnnealingAlgo<Solution> aa = new AnnealingAlgo<>(new SolutionManager<Solution>() {
			Random r = new Random(System.nanoTime());

			@Override
			public Solution getRandomSolution() {

				boolean pop[] = new boolean[Solution.base.length];
				for (int i = 0; i < pop.length; i++) {
					pop[i] = r.nextBoolean();
				}
				return new Solution(pop);
			}

			@Override
			public Solution randomize(Solution solution, int b, int n) {
				int size = Solution.base.length;
				boolean pop[] = new boolean[size];
				System.arraycopy(solution.population, 0, pop, 0, size);
				int index = r.nextInt(size);
				pop[index] = !pop[index];
				return new Solution(pop);

			}

			@Override
			public double evaluate(Solution solution) {
				return solution.evaluate();
			}

			@Override
			public boolean isAcceptable(double evaluation) {
				// TODO Auto-generated method stub
				return evaluation == 1000;
			}

			@Override
			public boolean isBetter(double evaluation1, double evaluation2) {
				return Math.abs(evaluation1 - 1000) < Math.abs(evaluation2 - 1000);
			}

		});

		Thread t = aa.getThreadRunner(1000000, 0, 0.999, new SolutionCallback<AnnealingAlgoTest.Solution>() {

			@Override
			public void onBestSolutionFound(Solution solution) {
				System.out.println("best : " + solution);
				System.out.println(solution.evaluate());

			}
		});

		t.start();
		t.join();

	}

}
