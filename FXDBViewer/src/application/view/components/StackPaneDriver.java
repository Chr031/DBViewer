package application.view.components;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class StackPaneDriver extends HBox {

	private StackPane stackPane;

	private LinearGradient lgDefault;
	private LinearGradient lgSelected;

	private IntegerProperty frontStackIndex;

	public StackPaneDriver() {
		super(10);
		setAlignment(Pos.CENTER);
		setMinHeight(15);
		Stop[] stopsDefault = new Stop[] { new Stop(0, Color.LIGHTGRAY), new Stop(0.2, Color.web("#aabbcc")),
				new Stop(1, Color.web("#aabbcc")) };
		Stop[] stopsSelected = new Stop[] { new Stop(0, Color.WHITE), new Stop(0.3, Color.web("#aabbcc")),
				new Stop(1, Color.web("#aabbcc")) };

		lgDefault = new LinearGradient(0.3, 0, 1, 1, true, CycleMethod.NO_CYCLE, stopsDefault);
		lgSelected = new LinearGradient(0.2, 0, 1, 1, true, CycleMethod.NO_CYCLE, stopsSelected);

		frontStackIndex = new SimpleIntegerProperty(0);
	}

	public StackPane getStackPane() {
		return stackPane;
	}

	public void setStackPane(StackPane stackPane) {
		this.stackPane = stackPane;
		setChildrenDrivenCount(stackPane.getChildrenUnmodifiable().size());
		
		stackPane.getChildren().addListener(new ListChangeListener<Node>() {
			@Override
			public void onChanged(Change<? extends Node> c) {
				setChildrenDrivenCount(c.getList().size());
			}
		});
	}

	private void setChildrenDrivenCount(int size) {

		if (getChildren().size() < size) {
			for (int i = getChildren().size(); i < size; i++) {
				Rectangle c = new Rectangle(5,5, lgDefault);
				final int j = i;
				c.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
					frontStackIndex.set(j);
					System.out.println(j);
				});
				getChildren().add(c);
			}
		} else {
			while (getChildren().size() > size) {
				getChildren().remove(size);
			}
		}

		refreshBindings();

	}

	private void refreshBindings() {
		int i = 0;
		frontStackIndex = new SimpleIntegerProperty();
		for (Node n : stackPane.getChildren()) {
			n.managedProperty().bind(Bindings.equal(i, frontStackIndex));
			n.visibleProperty().bind(Bindings.equal(i++, frontStackIndex));
		}

		frontStackIndex.set(0);
		
	}

}
