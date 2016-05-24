package application.view.components;

import java.util.concurrent.Callable;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;

public class HistoryPane extends Pane {

	public class HistoryData {

		private String title;
		private Node node;
		private int index;

		public HistoryData(String title, Node node) {
			super();
			this.setTitle(title);
			this.setNode(node);
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public Node getNode() {
			return node;
		}

		public void setNode(Node node) {
			this.node = node;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int historyIndex) {
			index = historyIndex;

		}

	}

	private StackPane stackPane;

	private final ObservableList<HistoryData> historyList;

	private IntegerProperty activeHistoryIndex;
	private IntegerProperty hoverHistoryIndex;
	// private IntegerProperty visibleHistoryIndex;

	private int spacing;

	public HistoryPane() {
		super();

		historyList = FXCollections.observableArrayList();
		activeHistoryIndex = new SimpleIntegerProperty();
		hoverHistoryIndex = new SimpleIntegerProperty(-1);
		historyList.addListener((Change<? extends HistoryData> c) -> {
			buildHistoryBar();
		});

		this.widthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

				System.out.println(newValue);
			}
		});
	}

	private void buildHistoryBar() {
		// remove all children // TODO do it in a more surgical way !!!
		this.getChildren().clear();

		Label previous = null;
		int index = 0;
		for (HistoryData data : historyList) {

			data.setIndex(index);
			Label lTitle = new Label(data.getTitle());
			lTitle.setPadding(new Insets(2));
			this.getChildren().add(lTitle);
			if (previous != null) {
				lTitle.layoutXProperty().bind(Bindings.add(spacing, Bindings.add(previous.layoutXProperty(), previous.widthProperty())));
			}
			lTitle.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
				activeHistoryIndex.set(data.getIndex());
				if (data.getIndex()>0 && e.getClickCount()>2)
					Platform.runLater(()-> removeHistoryIndex(data.getIndex()));
			});

			lTitle.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> {
				hoverHistoryIndex.set(data.getIndex());
			});

			lTitle.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> {
				hoverHistoryIndex.set(-1);
			});

			lTitle.setTooltip(new Tooltip(data.getTitle()));
			createBGShape(lTitle, index);
			previous = lTitle;

			index++;
		}

	}

	private void createBGShape(Region r, final int historyIndex) {
		double slashCoefficient = 3 / 4d;

		Polygon p = new Polygon();
		r.layoutBoundsProperty().addListener(new ChangeListener<Bounds>() {

			@Override
			public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
				p.getPoints().clear();
				// origin point
				p.getPoints().addAll(r.getLayoutX() - (historyIndex == 0 ? 0 : spacing * (slashCoefficient)), r.getLayoutY());
				// top right
				p.getPoints().addAll(r.getLayoutX(), r.getLayoutY() + r.getHeight());
				// bottom right
				p.getPoints().addAll(r.getLayoutX() + r.getWidth() + spacing * (slashCoefficient), r.getLayoutY() + r.getHeight());
				// bottom left
				p.getPoints().addAll(r.getLayoutX() + r.getWidth(), r.getLayoutY());

			}
		});

		Callable<Paint> paintCall = new Callable<Paint>() {

			@Override
			public Paint call() throws Exception {
				if (historyIndex == activeHistoryIndex.get()) {
					return Color.web("#aabbcc");
				} else
					return Color.web("#dadbdc");

			}
		};

		// p.setStroke(Color.web("#aabbcc"));
		// p.setFill(Color.web("#aabbcc"));
		p.strokeProperty().bind(Bindings.createObjectBinding(paintCall, activeHistoryIndex));
		p.fillProperty().bind(Bindings.createObjectBinding(paintCall, activeHistoryIndex));
		this.getChildren().add(p);
		p.toBack();
	}

	public int getSpacing() {
		return spacing;
	}

	public void setSpacing(int spacing) {
		this.spacing = spacing;
	}

	public StackPane getStackPane() {
		return stackPane;
	}

	public void setStackPane(StackPane stackPane) {
		this.stackPane = stackPane;
	}

	public HistoryData addHistoryFirstPane(String historyTitle, Pane pane) {
		historyList.clear();
		stackPane.getChildren().clear();
		this.getChildren().clear();
		return addHistoryNewPane(historyTitle, pane);

	}

	public synchronized HistoryData addHistoryNewPane(String historyTitle, Node historyNode) {

		HistoryData historyData = new HistoryData(historyTitle, historyNode);
		final int historyIndex = stackPane.getChildren().size();
		historyData.setIndex(historyIndex);
		historyList.add(historyData);
		stackPane.getChildren().add(historyNode);

		historyNode.visibleProperty().bind(Bindings.createBooleanBinding(() -> {
			return (activeHistoryIndex.get() == historyData.getIndex() && hoverHistoryIndex.get() == -1)
					|| (hoverHistoryIndex.get() == historyData.getIndex());
		},
				activeHistoryIndex, hoverHistoryIndex));

		activeHistoryIndex.set(historyIndex);

		return historyData;
	}

	public void removeHistoryLast() {
		if (historyList.size() == 0)
			return;
		removeHistoryIndex(historyList.size() - 1);

	}

	public void removeActiveHistory() {

		removeHistoryIndex(activeHistoryIndex.get());

	}

	public void removeHistoryIndex(int historyIndex) {
		stackPane.getChildren().remove(historyIndex);
		historyList.remove(historyIndex);
		if (historyIndex > 0)
			activeHistoryIndex.set(historyIndex - 1);
	}

}
