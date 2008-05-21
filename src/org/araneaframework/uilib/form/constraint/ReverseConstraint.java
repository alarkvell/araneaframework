package org.araneaframework.uilib.form.constraint;

import java.util.Collection;
import java.util.Set;
import org.araneaframework.Environment;
import org.araneaframework.uilib.form.Constraint;

/**
 * A <code>Constraint</code> that reverses the effect of the contained
 * <code>Constraint</code> &mdash; validating this constraint only when the
 * contained <code>Constraint</code> is invalid. When contained
 * <code>Constraint</code> is valid, an error message (set with
 * {@link Constraint#setCustomErrorMessage(String)}) is produced.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class ReverseConstraint extends BaseConstraint {

  private static final long serialVersionUID = 1L;

  /**
   * The contained constraint to use.
   */
  protected Constraint toReverse;

  /**
   * An instance of a custom error message factory that will be used to produce
   * an error message if the contained constraint validates.
   */
  protected ReverseConstraintErrorMessageFactory errorMessageFactory;

  /**
   * Creates a new reverse constraint with given contained constraint. If the
   * latter validates, the reverse constraint does not, and <i>vice versa</i>.
   * 
   * @param toReverse The constraint to reverse.
   */
  public ReverseConstraint(Constraint toReverse) {
    this.toReverse = toReverse;
  }

  /**
   * Creates a new reverse constraint with given contained constraint and an
   * error message. If the given <code>constraint</code> validates, this
   * reverse constraint does not, and <i>vice versa</i>.
   * 
   * @param toReverse The constraint to reverse.
   * @param customErrorMessage The error message to produce when the contained
   *            constraint <i>validates</i>.
   */
  public ReverseConstraint(Constraint toReverse, String customErrorMessage) {
    this(toReverse);
    setCustomErrorMessage(customErrorMessage);
  }

  /**
   * Creates a new reverse constraint with given contained constraint and error
   * message factory. If the <code>constraint</code> validates, then this
   * reverse constraint does not, and <i>vice versa</i>.
   * <p>
   * The <code>errorMessageFactory</code> is factory implementation that
   * should be able to provide a custom error message for this reverse
   * constraint.
   * 
   * @param toReverse The constraint to reverse.
   * @param errorMessageFactory A custom factory that returns appropriate error
   *            messages.
   */
  public ReverseConstraint(Constraint toReverse,
      ReverseConstraintErrorMessageFactory errorMessageFactory) {
    this(toReverse);
    this.errorMessageFactory = errorMessageFactory;
  }

  /**
   * Provides access to the contained constrained.
   * 
   * @return The <code>Constraint</code> reversed by this
   *         <code>ReverseConstraint</code>.
   */
  public Constraint getConstraint() {
    return toReverse;
  }

  /**
   * Can be used to produce reverse constraint validation error message, where
   * {@link ReverseConstraint#ReverseConstraint(Constraint, String)} or
   * {@link #setCustomErrorMessage(String)} do not suffice.
   * 
   * @author Taimo Peelo (taimo@araneaframework.org)
   */
  public static interface ReverseConstraintErrorMessageFactory {

    /**
     * Should return validation errors for <code>ReverseConstraint</code>
     * which did not validate.
     * 
     * @param c The <code>ReverseConstraint</code> where validation failed.
     * @return A collection of {@link String}s containing constraint validation
     *         error messages.
     */
    public Collection getErrorMessage(ReverseConstraint c);
  }

  /**
   * It has the opposite effect on the contained constraint. If the latter validates,
   * this method does not, and <i>vice versa</i>.
   */
  protected void validateConstraint() throws Exception {
    toReverse.validate();
    Set errors = toReverse.getErrors();
    // Reverse constraint is invalid when wrapped constraint validates
    if (errors.isEmpty()) {
      if (customErrorMessage != null)
        addError(customErrorMessage);
      else if (errorMessageFactory != null)
        addErrors(errorMessageFactory.getErrorMessage(this));
      else
        addError("Reverse constraint validation failed for constraint "
            + getConstraint()
            + ". No details available because custom validation error message was not set.");
    }
    toReverse.clearErrors();
  }

  public void clearErrors() {
    toReverse.clearErrors();
    super.clearErrors();
  }

  public void setEnvironment(Environment environment) {
    super.setEnvironment(environment);
    toReverse.setEnvironment(environment);
  }
}
