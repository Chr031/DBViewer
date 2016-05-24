package application.view.builder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import application.model.dataaccessor.condition.AccessorCondition;
import application.model.dataaccessor.condition.FieldCondition;
import application.model.descriptor.ClassDescriptor;
import application.model.descriptor.CollectionForFormPropertyDescriptor;
import application.model.descriptor.PlainObjectPropertyDescriptor;
import application.model.descriptor.PropertyDescriptorI;
import application.utils.StringUtils;
import application.view.TableAndFormService;

public class TableBuilder {

	private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(TableBuilder.class);

	/**
	 * Initialize the columns of the table
	 * 
	 * @param table
	 * @param classDescriptor
	 */
	public <T> void buildTableLayout(TableAndFormService tableAndFormService, TableView<T> table, ClassDescriptor<T> classDescriptor) {

		for (PropertyDescriptorI desc : classDescriptor.getCommonPropertyDescriptors()) {
			// grid.add(new Label(key), column++, 0);

			table.getColumns().add(desc.isLink() ? buildLinkedTableColumn(tableAndFormService,desc) : buildColumnLayout(desc));
		}

		if (classDescriptor.hasChildClasses()) {
			for (Class<? extends T> childClass : classDescriptor.getChildObjectClasses()) {
				for (PropertyDescriptorI desc : classDescriptor.getChildPropertyDescriptors(childClass)) {
					table.getColumns().add(desc.isLink() ? buildLinkedTableColumn(tableAndFormService,desc) : buildColumnLayout(desc));
				}
			}
		}

	}

	/**
	 * Maybe should be part of the presenter itself !!!
	 * 
	 * @param desc
	 * @return
	 */
	private <T> TableColumn<T, String> buildColumnLayout(PropertyDescriptorI desc) {
		TableColumn<T, String> tableColumn = new TableColumn<>(desc.getName());

		initializeStringCellValue(tableColumn, desc);

		return tableColumn;
	}

	private <T> void initializeStringCellValue(TableColumn<T, String> tableColumn, PropertyDescriptorI desc) {
		boolean[] widthChanged = new boolean[] { false };
		tableColumn.setCellValueFactory((cell) -> {
			try {
				if (desc.getAccessor().getDeclaringClass().isAssignableFrom(cell.getValue().getClass())) {
					String propertyValueAsString = desc.getPropertyValueAsString(cell.getValue());
					if (!widthChanged[0])
						propertyValueAsString = StringUtils.wordReducer(propertyValueAsString, 56);

					return new ReadOnlyObjectWrapper<String>(propertyValueAsString);
				}
				else
					return new ReadOnlyObjectWrapper<String>(null);
			} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | IOException e) {
				log.error("Unable to get the value", e);
				return new ReadOnlyObjectWrapper<String>(e.getMessage());
			}

		});

		tableColumn.widthProperty().addListener((ChangeListener<Number>) (e, o, n) -> {
			widthChanged[0] = true;

		});
	}

	private <T> TableColumn<T, T> buildLinkedTableColumn(TableAndFormService tableAndFormService, PropertyDescriptorI desc) {
		TableColumn<T, T> tableColumn = new TableColumn<>(desc.getName());
		tableColumn.setCellValueFactory((cell) -> {
			return new SimpleObjectProperty<T>(cell.getValue());
		});

		tableColumn.setCellFactory((table) -> {
			// TODO update this !!!!
				if (desc instanceof PlainObjectPropertyDescriptor) {
					return new LinkCell<T>(tableAndFormService,(PlainObjectPropertyDescriptor) desc);
				} else if (desc instanceof CollectionForFormPropertyDescriptor) {
					return new CollectionLinkCell<T, Object>(tableAndFormService,(CollectionForFormPropertyDescriptor) desc);
				} else
					return null;
			});
		return tableColumn;
	}

	private class LinkCell<T> extends TableCell<T, T> {

		private final PlainObjectPropertyDescriptor<T, ?> desc;
		private final TableAndFormService tableAndFormService;
		private final HBox box ;
		private final Label label;

		private <F> LinkCell(TableAndFormService tableAndFormService, PlainObjectPropertyDescriptor<T, F> desc) {
			this.desc = desc;
			this.tableAndFormService = tableAndFormService;
			box = new HBox(5);
			label = new Label();
			box.getChildren().add(label);
		}

		@Override
		protected void updateItem(T data, boolean empty) {

			super.updateItem(data, empty);
			String text;
			try {
				text = desc.getPropertyValueAsString(data);
			} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | IOException ex) {
				log.error("unable to get property value ", ex);
				return;
			}

			if (data == null || text == null)
				return;

			label.setText(text);
			label.getStyleClass().add("linkLabel");
			label.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> {
				try {
					tableAndFormService.loadList(desc.getAccessor().getType(), new AccessorCondition(desc.getAccessor(), data));
				} catch (Exception ex) {
					log.error("unable to load table view ", ex);
				}

			});

			this.setGraphic(box);
		}

	}

	private class CollectionLinkCell<T, F> extends TableCell<T, T> {
		private final CollectionForFormPropertyDescriptor<T, F> desc;
		private final TableAndFormService tableAndFormService;
		private final HBox box ;
		private final Label label;
		

		public CollectionLinkCell(TableAndFormService tableAndFormService, CollectionForFormPropertyDescriptor<T, F> desc) {
			this.desc = desc;
			this.tableAndFormService = tableAndFormService;
			box = new HBox(5);
			label = new Label();
			box.getChildren().add(label);
		}

		@Override
		protected void updateItem(T data, boolean empty) {
			super.updateItem(data, empty);
			if (data == null)
				return;
			Collection<F> dataList;
			try {
				dataList = desc.getAccessor().get(data);
			} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | IOException ex) {
				log.error("unable to retrive collection property", ex);
				return;
			}

			if (dataList == null || dataList.size() == 0) {
				return;
			}

			label.setText(desc.getAccessor().getActualTypeArgument().getSimpleName() + "(" + dataList.size() + ")");
			label.getStyleClass().add("linkLabel");

			label.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> {
				try {
					tableAndFormService.loadList(desc.getAccessor().getActualTypeArgument(), new AccessorCondition(desc.getAccessor(), data));
				} catch (Exception ex) {
					log.error("unable to retrive collection property", ex);
					return;
				}
				
				});

			this.setGraphic(box);

		}

	}

}
