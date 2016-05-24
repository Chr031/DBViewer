package application.view;

import org.junit.Assert;
import org.junit.Test;

import application.model.Model;
import application.model.ModelService;
import application.model.descriptor.ClassDescriptor;
import application.model.descriptor.ClassDescriptorFactory;
import application.model.pojo.tasks.Task;
import application.view.components.FilterEvaluator;

public class FilterEvaluatorTest {

	@Test
	public void test() throws Exception {
		Model taskModel = new ModelService().getModel("Tasks");
		Assert.assertNotNull("Model not defined", taskModel);
		ClassDescriptorFactory cdf = new ClassDescriptorFactory(taskModel);

		ClassDescriptor<Task> cd = cdf.getGridDescriptor(Task.class);

		FilterEvaluator<Task> fe = new FilterEvaluator<>();

		Task t = new Task();
		t.setId(1);
		t.setName("JavaFX abc");
		t.setDescription("abcdef");
		t.setDone(true);

		boolean result = fe.evaluate(cd, "abc", t);
		Assert.assertTrue("abc search must be true", result);

		result = fe.evaluate(cd, "ddd", t);
		Assert.assertFalse("ddd search must be false", result);

		result = fe.evaluate(cd, "javaFX", t);
		Assert.assertTrue("javaFX search must be true", result);

		String filter = "name:javaFX";
		result = fe.evaluate(cd, filter, t);
		Assert.assertTrue(filter + " search must be true", result);

		filter = "name:javaFX;done:true";
		result = fe.evaluate(cd, filter, t);
		Assert.assertTrue(filter + " search must be true", result);

		filter = "name:javaFX;done:false";
		result = fe.evaluate(cd, filter, t);
		Assert.assertFalse(filter + " search must be false", result);

	}

}
