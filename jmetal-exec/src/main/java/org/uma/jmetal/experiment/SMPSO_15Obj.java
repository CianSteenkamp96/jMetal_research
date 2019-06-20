package org.uma.jmetal.experiment;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.smpso.SMPSOBuilder;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.wfg.*;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.archive.impl.CrowdingDistanceArchive;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.experiment.Experiment;
import org.uma.jmetal.util.experiment.ExperimentBuilder;
import org.uma.jmetal.util.experiment.component.ComputeQualityIndicators;
import org.uma.jmetal.util.experiment.component.ExecuteAlgorithms;
import org.uma.jmetal.util.experiment.component.GenerateLatexTablesWithStatistics;
import org.uma.jmetal.util.experiment.component.GenerateWilcoxonTestTablesWithR;
import org.uma.jmetal.util.experiment.util.ExperimentAlgorithm;
import org.uma.jmetal.util.experiment.util.ExperimentProblem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Example of experimental study based on solving the WFG problems with the algorithms NSGAII and SMPSO
 * Six quality indicators are used for performance assessment.
 * <p>
 * The steps to carry out the experiment are: 1. Configure the experiment 2. Execute the algorithms
 * 3. Compute que quality indicators 4. Generate Latex tables reporting means and medians 5.
 * Generate R scripts to produce latex tables with the result of applying the Wilcoxon Rank Sum Test
 * 6. Generate Latex tables with the ranking obtained by applying the Friedman test 7. Generate R
 * scripts to obtain boxplots
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */

public class SMPSO_15Obj {

//	private static final int INDEPENDENT_RUNS = 30;
	private static final int INDEPENDENT_RUNS = 2; // SMPSO WFG5 and WFG9 (didn't finish on CHPC with 30 indep runs)

	public static void main(String[] args) throws IOException {
		//    if (args.length != 1) {
		//      throw new JMetalException("Missing argument: experimentBaseDirectory");
		//    }
//		String experimentBaseDirectory = "/home/cian/IdeaProjects/jMetal/jmetal-exec/src/main/java/org/uma/jmetal/experiment/";
		String experimentBaseDirectory = ".";

		List<ExperimentProblem<DoubleSolution>> problemList = new ArrayList<>();
//		problemList.add(new ExperimentProblem<>(new WFG1(28, 4, 15)));
//		problemList.add(new ExperimentProblem<>(new WFG2(28, 4, 15)));
//		problemList.add(new ExperimentProblem<>(new WFG3(28, 4, 15)));
//		problemList.add(new ExperimentProblem<>(new WFG4(28, 4, 15)));
//		problemList.add(new ExperimentProblem<>(new WFG5(28, 4, 15)));
//		problemList.add(new ExperimentProblem<>(new WFG6(28, 4, 15)));
//		problemList.add(new ExperimentProblem<>(new WFG7(28, 4, 15)));
//		problemList.add(new ExperimentProblem<>(new WFG8(28, 4, 15)));
		problemList.add(new ExperimentProblem<>(new WFG9(28, 4, 15)));

		List<ExperimentAlgorithm<DoubleSolution, List<DoubleSolution>>> algorithmList =
			configureAlgorithmList(problemList);

		Experiment<DoubleSolution, List<DoubleSolution>> experiment =
//			new ExperimentBuilder<DoubleSolution, List<DoubleSolution>>("SMPSO_15Obj_WFG1")
//			new ExperimentBuilder<DoubleSolution, List<DoubleSolution>>("SMPSO_15Obj_WFG2")
//			new ExperimentBuilder<DoubleSolution, List<DoubleSolution>>("SMPSO_15Obj_WFG3")
//			new ExperimentBuilder<DoubleSolution, List<DoubleSolution>>("SMPSO_15Obj_WFG4")
//			new ExperimentBuilder<DoubleSolution, List<DoubleSolution>>("SMPSO_15Obj_WFG5")
//			new ExperimentBuilder<DoubleSolution, List<DoubleSolution>>("SMPSO_15Obj_WFG6")
//			new ExperimentBuilder<DoubleSolution, List<DoubleSolution>>("SMPSO_15Obj_WFG7")
//			new ExperimentBuilder<DoubleSolution, List<DoubleSolution>>("SMPSO_15Obj_WFG8")
			new ExperimentBuilder<DoubleSolution, List<DoubleSolution>>("SMPSO_15Obj_WFG9")
				.setAlgorithmList(algorithmList)
				.setProblemList(problemList)
				.setReferenceFrontDirectory("/home/cian/IdeaProjects/referenceFronts/wfg15Obj")
//				.setReferenceFrontDirectory("/home/csteenkamp/lustre/msc_phd/jmetal/nsgaii_smpso/referenceFronts/wfg15Obj")
				.setExperimentBaseDirectory(experimentBaseDirectory)
				.setOutputParetoFrontFileName("FUN")
				.setOutputParetoSetFileName("VAR")
				.setIndicatorList(Arrays.asList(
//					new PISAHypervolume<DoubleSolution>(), // Don't trust jMetal's HV - see prof mail for alternative tool for calc of HV
					new InvertedGenerationalDistance<DoubleSolution>()
	))
				.setIndependentRuns(INDEPENDENT_RUNS)
				.setNumberOfCores(8)
				.build();

		new ExecuteAlgorithms<>(experiment).run();
		new ComputeQualityIndicators<>(experiment).run();
		new GenerateLatexTablesWithStatistics(experiment).run();
		new GenerateWilcoxonTestTablesWithR<>(experiment).run();
	}

	/**
	 * The algorithm list is composed of pairs {@link Algorithm} + {@link Problem} which form part of
	 * a {@link ExperimentAlgorithm}, which is a decorator for class {@link Algorithm}.
	 */
	static List<ExperimentAlgorithm<DoubleSolution, List<DoubleSolution>>> configureAlgorithmList(
		List<ExperimentProblem<DoubleSolution>> problemList) {
		List<ExperimentAlgorithm<DoubleSolution, List<DoubleSolution>>> algorithms = new ArrayList<>();
		for (int run = 0; run < INDEPENDENT_RUNS; run++) {

			for (int i = 0; i < problemList.size(); i++) {
				double mutationProbability = 1.0 / problemList.get(i).getProblem().getNumberOfVariables();
				double mutationDistributionIndex = 20.0;
				Algorithm<List<DoubleSolution>> algorithm = new SMPSOBuilder(
					(DoubleProblem) problemList.get(i).getProblem(),
					new CrowdingDistanceArchive<DoubleSolution>(150))
					.setMutation(new PolynomialMutation(mutationProbability, mutationDistributionIndex))
					.setMaxIterations(200)
					.setSwarmSize(150)
					.setSolutionListEvaluator(new SequentialSolutionListEvaluator<DoubleSolution>())
					.build();
				algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i), run));
			}
		}
		return algorithms;
	}
}
