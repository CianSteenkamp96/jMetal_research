package org.uma.jmetal.solution;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Interface representing a Solution
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 * @param <T> Type (Double, Integer, etc.)
 */
public interface Solution<T> extends Serializable {
  void setObjective(int index, double value) ;
  double getObjective(int index) ;
  double[] getObjectives() ;

  // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! NEW !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  // See ./jmetal-core/src/main/java/org/uma/jmetal/solution/impl/AbstractGenericSolution.java for the implementation.
  // and /home/cian/IdeaProjects/jMetal/jmetal-core/src/main/java/org/uma/jmetal/solution/impl/ArrayDoubleSolution.java for implementation which seems to never be used.
  // Also see ./jmetal-core/src/main/java/org/uma/jmetal/solution/Solution.java
  // Also ./jmetal-core/src/main/java/org/uma/jmetal/util/comparator/DominanceComparator.java
  // /home/cian/IdeaProjects/jMetal/jmetal-core/src/main/java/org/uma/jmetal/util/point/PointSolution.java
  ////////////////////////////////////////////////////////////////////////////////////////////////
  double getObjective_CDAS(int index) ;
  static double dotProduct(double[] a, double[] b) {
    double sum = 0;
    for (int i = 0; i < a.length; i++) {
      sum += a[i] * b[i];
    }
    return sum;
  }
  ////////////////////////////////////////////////////////////////////////////////////////////////
  T getVariableValue(int index) ;
  List<T> getVariables() ;
  void setVariableValue(int index, T value) ;
  String getVariableValueString(int index) ;

  int getNumberOfVariables() ;
  int getNumberOfObjectives() ;

  Solution<T> copy() ;

  void setAttribute(Object id, Object value) ;
  Object getAttribute(Object id) ;
  
  public Map<Object, Object> getAttributes();
}
