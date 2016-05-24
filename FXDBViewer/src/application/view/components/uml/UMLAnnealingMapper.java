package application.view.components.uml;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javafx.scene.control.Control;
import application.model.Model;
import application.utils.AnnealingAlgo;
import application.utils.AnnealingAlgo.SolutionManager;
import application.utils.Callback;

public class UMLAnnealingMapper extends UMLAbstractMapper {

	private Thread annealingThreadRunner;

	public UMLAnnealingMapper() {
		super();

	}

	public void setModel(Model model) {

		stopThread();

		super.setModel(model);

	}

	private void stopThread() {
		if (annealingThreadRunner != null && annealingThreadRunner.isAlive()) {
			annealingThreadRunner.interrupt();

			try {
				annealingThreadRunner.join(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void runAnnealingAlgorithm() {

		PositionSolutionManager solutionManager = new PositionSolutionManager(this);
		AnnealingAlgo<ClassPositionSolution> annealing = new AnnealingAlgo<>(solutionManager);

		stopThread();

		annealingThreadRunner = annealing.getThreadRunner(100000, 100, 0.999d, (s) -> {
			applySolution(s);
			System.out.println("Solution applied : " + solutionManager.evaluate(s));
		});

		annealingThreadRunner.setDaemon(true);
		annealingThreadRunner.start();

	}

	public void applySolution(ClassPositionSolution solution) {
		for (ClassPosition cp : solution.getClassPositionList()) {
			Control c = classMap.get(cp.clazz);
			// c.setLayoutX(cp.rectangle.getX());
			// c.setLayoutY(cp.rectangle.getY());
			c.layoutXProperty().set(cp.rectangle.getX());
			c.layoutYProperty().set(cp.rectangle.getY());
		}
	}

	public void initializeObjects(Callback<Class<?>, Void> classActionCallback) {
		stopThread();
		super.initializeObjects(classActionCallback);
	}

	class ClassPosition {

		Class<?> clazz;
		Rectangle rectangle;

		public ClassPosition(Class<?> clazz, Rectangle rect) {
			this.clazz = clazz;
			this.rectangle = rect;
		}

	}

	class ClassPositionSolution {
		private final List<ClassPosition> classPositionList;
		private final Map<Class<?>, ClassPosition> classPositionMap;

		ClassPositionSolution() {
			classPositionList = new ArrayList<>();
			classPositionMap = new HashMap<>();
		}

		public void addPosition(Class<?> clazz, Rectangle rect) {
			ClassPosition cp = new ClassPosition(clazz, rect);
			classPositionList.add(cp);
			classPositionMap.put(clazz, cp);

		}

		public ClassPositionSolution clone() {

			ClassPositionSolution p = new ClassPositionSolution();

			for (ClassPosition cp : getClassPositionList()) {
				p.addPosition(cp.clazz, (Rectangle) cp.rectangle.clone());
			}
			return p;
		}

		List<ClassPosition> getClassPositionList() {
			return classPositionList;
		}

		ClassPosition getClassPosition(int index) {
			return classPositionList.get(index);
		}

		ClassPosition getClassPosition(Class<?> clazz) {
			return classPositionMap.get(clazz);
		}

	}

	class PositionSolutionManager implements SolutionManager<ClassPositionSolution> {
		UMLAnnealingMapper pane;
		Random r = new Random(System.nanoTime());

		public PositionSolutionManager(UMLAnnealingMapper umlAnnealingMapper) {
			pane = umlAnnealingMapper;

		}

		@Override
		public ClassPositionSolution getRandomSolution() {

			ClassPositionSolution position = new ClassPositionSolution();
			int width = (int) pane.getWidth();
			int height = (int) pane.getHeight();
			width = width > 0 ? width : 100;
			height = height > 0 ? height : 100;

			for (Entry<Class<?>, Control> e : pane.classMap.entrySet()) {
				Rectangle rect = new Rectangle((int) e.getValue().getWidth(), (int) e.getValue().getHeight());

				rect.setLocation(r.nextInt(width - rect.width), r.nextInt(height - rect.height));
				position.addPosition(e.getKey(), rect);
			}

			return position;
		}

		@Override
		public ClassPositionSolution randomize(ClassPositionSolution solution, int b, int n) {
			ClassPositionSolution newPosition = solution.clone();
			int indexToRandomize = r.nextInt(newPosition.getClassPositionList().size());

			int width = (int) pane.getWidth();
			int height = (int) pane.getHeight();
			width = width > 0 ? width : 100;
			height = height > 0 ? height : 100;

			ClassPosition cp = newPosition.getClassPositionList().get(indexToRandomize);
			Rectangle rect = cp.rectangle;
			boolean intersect = true;
			while (intersect) {
				boolean gInter = false;
				rect.setLocation(r.nextInt(width - rect.width), r.nextInt(height - rect.height));
				for (ClassPosition lcp : newPosition.getClassPositionList()) {
					if (lcp.rectangle != rect)
						gInter |= rect.intersects(lcp.rectangle);
				}
				intersect = gInter;
			}

			rect.width = (int) classMap.get(cp.clazz).getWidth();
			rect.height = (int) classMap.get(cp.clazz).getHeight();

			return newPosition;
		}

		@Override
		public double evaluate(ClassPositionSolution solution) {

			// TODO optimize lookup....
			double evaluate = 0;

			for (ClassPosition cp1 : solution.getClassPositionList()) {
				for (ClassPosition cp2 : solution.getClassPositionList()) {
					// evaluate +=
					// cp1.rectangle.getLocation().distance(cp2.rectangle.getLocation());
				}
			}

			for (Link<Class<?>> link1 : pane.classLinkSet) {
				Rectangle lRect1 = solution.getClassPosition(link1.getLeft()).rectangle;
				Rectangle rRect1 = solution.getClassPosition(link1.getRight()).rectangle;

				Point l1 = new Point(lRect1.x + lRect1.width / 2, lRect1.y + lRect1.height / 2);
				Point r1 = new Point(rRect1.x + rRect1.width / 2, rRect1.y + rRect1.height / 2);

				for (Link<Class<?>> link2 : pane.classLinkSet) {
					Rectangle lRect2 = solution.getClassPosition(link2.getLeft()).rectangle;
					Rectangle rRect2 = solution.getClassPosition(link2.getRight()).rectangle;
					Point l2 = new Point(lRect2.x + lRect2.width / 2, lRect2.y + lRect2.height / 2);
					Point r2 = new Point(rRect2.x + rRect2.width / 2, rRect2.y + rRect2.height / 2);

					Line2D line1 = new Line2D.Double(l1, r1);
					Line2D line2 = new Line2D.Double(l2, r2);
					boolean result = line2.intersectsLine(line1);

					if (result)
						evaluate -= l1.distance(r1) + l2.distance(r2);

				}
			}

			return evaluate;
		}

		@Override
		public boolean isAcceptable(double evaluation) {
			return false;
		}

		public boolean isBetter(double evaluation1, double evaluation2) {
			return evaluation1 > evaluation2;
		}

	}

}
