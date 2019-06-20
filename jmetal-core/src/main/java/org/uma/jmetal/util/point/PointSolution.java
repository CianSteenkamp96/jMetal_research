package org.uma.jmetal.util.point;

import org.uma.jmetal.solution.Solution;

import java.util.*;

/**
 * Solution used to wrap a {@link Point} object. Only objectives are used.
 *
 * @author Antonio J. Nebro
 */
@SuppressWarnings("serial")
public class PointSolution implements Solution<Double> {
  private int numberOfObjectives ;
  private double[] objectives;
  protected Map<Object, Object> attributes ;

  /**
   * Constructor
   *
   * @param numberOfObjectives
   */
  public PointSolution(int numberOfObjectives) {
    this.numberOfObjectives = numberOfObjectives ;
    objectives = new double[numberOfObjectives] ;
    attributes = new HashMap<>() ;
  }

  /**
   * Constructor
   *
   * @param point
   */
  public PointSolution(Point point) {
    this.numberOfObjectives = point.getDimension() ;
    objectives = new double[numberOfObjectives] ;

    for (int i = 0; i < numberOfObjectives; i++) {
      this.objectives[i] = point.getValue(i) ;
    }
  }

  /**
   * Constructor
   *
   * @param solution
   */
  public PointSolution(Solution<?> solution) {
    this.numberOfObjectives = solution.getNumberOfObjectives() ;
    objectives = new double[numberOfObjectives] ;

    for (int i = 0; i < numberOfObjectives; i++) {
      this.objectives[i] = solution.getObjective(i) ;
    }
  }

  /**
   * Copy constructor
   *
   * @param point
   */
  public PointSolution(PointSolution point) {
    this(point.getNumberOfObjectives()) ;

    for (int i = 0; i < numberOfObjectives; i++) {
      this.objectives[i] = point.getObjective(i) ;
    }
  }

  @Override public void setObjective(int index, double value) {
    objectives[index]=  value ;
  }

  @Override public double getObjective(int index) {
    return objectives[index];
  }

  // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! NEW !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // See ./jmetal-core/src/main/java/org/uma/jmetal/solution/impl/AbstractGenericSolution.java for the implementation.
  // Also see ./jmetal-core/src/main/java/org/uma/jmetal/solution/Solution.java
  // Also ./jmetal-core/src/main/java/org/uma/jmetal/util/comparator/DominanceComparator.java
  @Override
  public double getObjective_CDAS(int index) {  // CDAS changes f_i(x) => f_i`(x) = (r * sin(w_i - phi_i)) / sin(phi_i) where r = ||f(x)||, w_i = arccos(f_i(x) / ||f(x)||), and phi_i = S_i * pi.
    return 0.0; // this instance of the function never called
  }
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Override
  public double[] getObjectives() {
    return objectives ;
  }

  @Override
  public List<Double> getVariables() {
    return Collections.emptyList() ;
  }

  @Override public Double getVariableValue(int index) {
    return null;
  }

  @Override public void setVariableValue(int index, Double value) {
	  //This method is an intentionally-blank override.
  }

  @Override public String getVariableValueString(int index) {
    return null;
  }

  @Override public int getNumberOfVariables() {
    return 0;
  }

  @Override public int getNumberOfObjectives() {
    return numberOfObjectives;
  }

  @Override public PointSolution copy() {
    return new PointSolution(this);
  }

  @Override public void setAttribute(Object id, Object value) {
    attributes.put(id, value) ;
  }

  @Override public Object getAttribute(Object id) {
    return attributes.get(id);
  }

  @Override public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    PointSolution that = (PointSolution) o;

    if (numberOfObjectives != that.numberOfObjectives)
      return false;
    if (!Arrays.equals(objectives, that.objectives))
      return false;

    return true;
  }

  @Override public int hashCode() {
    return Arrays.hashCode(objectives);
  }
  
  @Override
	public String toString() {
		return Arrays.toString(objectives);
	}

	@Override
	public Map<Object, Object> getAttributes() {
		return attributes;
	}
}
