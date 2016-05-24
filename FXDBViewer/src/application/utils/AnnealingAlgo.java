package application.utils;

import java.util.Random;

public class AnnealingAlgo<S> {

	public interface SolutionManager<S> {
		/**
		 * Generate a random solution
		 * 
		 * @return
		 */
		S getRandomSolution();

		/**
		 * Randomize a solution
		 * 
		 * @param solution
		 * @param b
		 *            number off iteration without any local best solution
		 * @param n
		 *            total number of iteration
		 * @return a new solution close to the one given in parameter.
		 */
		S randomize(S solution, int b, int n);

		/**
		 * Evaluate the solution.
		 * 
		 * @param solution
		 * @return
		 */
		double evaluate(S solution);

		/**
		 * Return true if the algorithm should consider a solution with such an
		 * evaluation as an acceptable solution.
		 * 
		 * @param evaluation
		 * @return
		 */
		boolean isAcceptable(double evaluation);
		
		/**
		 * return true if evaluation1 is better than evaluation2 and false otherwise 
		 * @param evaluation1
		 * @param evaluation2
		 * @return
		 */
		boolean isBetter(double evaluation1, double evaluation2) ;
	}

	public interface SolutionCallback<S> {
		void onBestSolutionFound(S solution);
	}

	private final SolutionManager<S> solutionManager;

	public AnnealingAlgo(SolutionManager<S> solutionManager) {
		super();
		this.solutionManager = solutionManager;
	}
	
	public Thread getThreadRunner(double tStart, double tMin, double coolingCoeficient, SolutionCallback<S> callback) {
		Thread t =  new Thread(()->{
			run(tStart,tMin,coolingCoeficient, callback);
		});
		
		t.setName("Annealing Algo");
		return t;
	}
	

	protected S run(double tStart, double tMin, double coolingCoeficient, SolutionCallback<S> callback) {

		Random random = new Random(System.nanoTime());

		S solution = solutionManager.getRandomSolution();
		S bestSolution = solution;
		
		callback.onBestSolutionFound(bestSolution);
		
		double T = tStart;
		double evaluation = solutionManager.evaluate(solution);
		double bestEvaluation = evaluation;
		int n = 0;
		int b = 0;
		
		while (!solutionManager.isAcceptable(evaluation) && !Thread.currentThread().isInterrupted()) {
			
			n++;
			b++;
			S tempSolution = solutionManager.randomize(solution, b, n);
			double newEval = solutionManager.evaluate(tempSolution);
			if (solutionManager.isBetter(newEval , evaluation)) {
				solution = tempSolution;
				evaluation = newEval;
			} else {
				double metro = Math.exp(-(newEval - evaluation) / T);
				if (random.nextDouble() < metro) {
					solution = tempSolution;
					evaluation = newEval;
				} else {
					continue;
				}
				b = 0;
			}

			T *= coolingCoeficient;

			if (solutionManager.isBetter( evaluation, bestEvaluation)) {
				bestSolution = solution;
				bestEvaluation = evaluation;
				callback.onBestSolutionFound(bestSolution);
			}

			if (T < tMin) {
				T = tStart;

			}

		}

		return bestSolution;

	}
}
