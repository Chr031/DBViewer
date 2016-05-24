package application.utils;

/**
 * Interface already exists in JavaFX .... see {@link javafx.util.Callback}
 * 
 * This interface should be removed ...
 * @author Can
 *
 * @param <A>
 * @param <R>
 */
public interface Callback<A,R> {

	public R call(A a);
}
