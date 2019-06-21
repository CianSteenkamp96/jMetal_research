package org.uma.jmetal.experiment;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.smpso.SMPSOBuilder;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.multiobjective.dtlz.*;
import org.uma.jmetal.problem.multiobjective.wfg.*;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
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
import java.util.Objects;

public class CDAS_SMPSO {

//	private static final int INDEPENDENT_RUNS = 30; // Update here before running on CHPC !!!!!!!!!!!!!!!!!!!!!!!!!!
	private static final int INDEPENDENT_RUNS = 2;

//	private static final int ITERATIONS = 10000; // Update here before running on CHPC !!!!!!!!!!!!!!!!!!!!!!!!!!
	private static final int ITERATIONS = 100;

	static List<ExperimentProblem<DoubleSolution>> genProblemList_WFG(List<ExperimentProblem<DoubleSolution>> problemList, int k, int numDecisionVariables, int numObjectives) {
		problemList.add(new ExperimentProblem<>(new WFG1(k, numDecisionVariables, numObjectives)));
		problemList.add(new ExperimentProblem<>(new WFG2(k, numDecisionVariables, numObjectives)));
		problemList.add(new ExperimentProblem<>(new WFG3(k, numDecisionVariables, numObjectives)));
		problemList.add(new ExperimentProblem<>(new WFG4(k, numDecisionVariables, numObjectives)));
		problemList.add(new ExperimentProblem<>(new WFG5(k, numDecisionVariables, numObjectives)));
		problemList.add(new ExperimentProblem<>(new WFG6(k, numDecisionVariables, numObjectives)));
		problemList.add(new ExperimentProblem<>(new WFG7(k, numDecisionVariables, numObjectives)));
		problemList.add(new ExperimentProblem<>(new WFG8(k, numDecisionVariables, numObjectives)));
		problemList.add(new ExperimentProblem<>(new WFG9(k, numDecisionVariables, numObjectives)));
		return problemList;
	}

	static List<ExperimentProblem<DoubleSolution>> genProblemList_DTLZ(List<ExperimentProblem<DoubleSolution>> problemList, int numDecisionVariables, int numObjectives) {
		problemList.add(new ExperimentProblem<>(new DTLZ1(numDecisionVariables, numObjectives)));
		problemList.add(new ExperimentProblem<>(new DTLZ2(numDecisionVariables, numObjectives)));
		problemList.add(new ExperimentProblem<>(new DTLZ3(numDecisionVariables, numObjectives)));
		problemList.add(new ExperimentProblem<>(new DTLZ4(numDecisionVariables, numObjectives)));
		problemList.add(new ExperimentProblem<>(new DTLZ5(numDecisionVariables, numObjectives)));
		problemList.add(new ExperimentProblem<>(new DTLZ6(numDecisionVariables, numObjectives)));
		problemList.add(new ExperimentProblem<>(new DTLZ7(numDecisionVariables, numObjectives)));
		return problemList;
	}

	public static void main(String[] args) throws IOException {
		if(args.length != 4) {
		  throw new JMetalException("Missing arguments, please specify the following arguments in this order (separated by spaces only): swarmSize (for 3 objectives use swarm size 153, for 5 objectives use swarm size 126, for 8 objectives use swarm size 156, for 10 objectives use swarm size 110, or for 15 objectives use swarm size 135), #objectives (3, 5, 8, 10, or 15), #decisionVariables (30, 100, 500, or 1000), and the benchmarkSuiteName (WFG or DTLZ).\nFor example, 153 3 30 WFG.\n");
		}

		int swarmSize = Integer.parseInt(args[0]);
		int numObjectives = Integer.parseInt(args[1]);
		int numDecisionVariables = Integer.parseInt(args[2]);
		String benchmarkSuiteName = args[3];
		String referenceFrontDir = "";

		if(numObjectives == 3){
//			referenceFrontDir = "/home/csteenkamp/lustre/msc_phd/referenceFronts/wfg3Obj"; // Update here before running on CHPC !!!!!!!!!!!!!!!!!!!!!!!!!!
			referenceFrontDir = "/home/cian/IdeaProjects/referenceFronts/wfg3Obj";
		} else if(numObjectives == 5){
//			referenceFrontDir = "/home/csteenkamp/lustre/msc_phd/referenceFronts/wfg5Obj"; // Update here before running on CHPC !!!!!!!!!!!!!!!!!!!!!!!!!!
			referenceFrontDir = "/home/cian/IdeaProjects/referenceFronts/wfg5Obj";
		} else if(numObjectives == 8){
//			referenceFrontDir = "/home/csteenkamp/lustre/msc_phd/referenceFronts/wfg8Obj"; // Update here before running on CHPC !!!!!!!!!!!!!!!!!!!!!!!!!!
			referenceFrontDir = "/home/cian/IdeaProjects/referenceFronts/wfg8Obj";
		} else if(numObjectives == 10){
//			referenceFrontDir = "/home/csteenkamp/lustre/msc_phd/referenceFronts/wfg10Obj"; // Update here before running on CHPC !!!!!!!!!!!!!!!!!!!!!!!!!!
			referenceFrontDir = "/home/cian/IdeaProjects/referenceFronts/wfg10Obj";
		} else if(numObjectives == 15){
//			referenceFrontDir = "/home/csteenkamp/lustre/msc_phd/referenceFronts/wfg15Obj"; // Update here before running on CHPC !!!!!!!!!!!!!!!!!!!!!!!!!!
			referenceFrontDir = "/home/cian/IdeaProjects/referenceFronts/wfg15Obj";
		} else {
			throw new JMetalException("This experiment only supports #objectives 3, 5, 8, 10, and 15.\n");
		}

		if(numDecisionVariables != 30 && numDecisionVariables != 100 && numDecisionVariables != 500 && numDecisionVariables != 1000){
			throw new JMetalException("This experiment only supports #decisionVariables 30, 100, 500, and 1000.\n");
		}

		if((numObjectives == 3 && swarmSize != 153) || (numObjectives == 5 && swarmSize != 126) || (numObjectives == 8 && swarmSize != 156) || (numObjectives == 10 && swarmSize != 110) || (numObjectives == 15 && swarmSize != 135)){
			throw new JMetalException("#objectives and swarmSize should adhere to the following: for 3obj => 153, 5obj => 126, 8obj => 156, 10obj => 110, 15obj => 135.\n");
		}

		String experimentBaseDirectory = ".";

		List<ExperimentProblem<DoubleSolution>> problemList = new ArrayList<>();

		if(Objects.equals(benchmarkSuiteName, "WFG")){
			// Note: k = (m - 1) * 2 for m > 2
			if(numObjectives == 3){
				problemList = genProblemList_WFG(problemList, 4, numDecisionVariables, numObjectives);
			} else if(numObjectives == 5){
				problemList = genProblemList_WFG(problemList, 8, numDecisionVariables, numObjectives);
			} else if(numObjectives == 8){
				problemList = genProblemList_WFG(problemList, 14, numDecisionVariables, numObjectives);
			} else if(numObjectives == 10){
				problemList = genProblemList_WFG(problemList, 18, numDecisionVariables, numObjectives);
			} else if(numObjectives == 15){
				problemList = genProblemList_WFG(problemList, 28, numDecisionVariables, numObjectives);
			} else {
				throw new JMetalException("This experiment only supports #objectives 3, 5, 8, 10, and 15.\n"); // This case is impossible due to previous check for #objectives when the referenceFrontDir is set above/earlier.
			}
		} else if(Objects.equals(benchmarkSuiteName, "DTLZ")){
			problemList = genProblemList_DTLZ(problemList, numDecisionVariables, numObjectives);
		} else {
			throw new JMetalException("This experiment only supports benchmark suites WFG and DTLZ.\n");
		}

		List<ExperimentAlgorithm<DoubleSolution, List<DoubleSolution>>> algorithmList =
			configureAlgorithmList(problemList, swarmSize);

		Experiment<DoubleSolution, List<DoubleSolution>> experiment =
			new ExperimentBuilder<DoubleSolution, List<DoubleSolution>>("CDAS_SMPSO_" + numObjectives + "obj_" + benchmarkSuiteName + "_" + numDecisionVariables + "D") // Maybe add CDAS s value in name as well?
				.setAlgorithmList(algorithmList)
				.setProblemList(problemList)
				.setReferenceFrontDirectory(referenceFrontDir)
				.setExperimentBaseDirectory(experimentBaseDirectory)
				.setOutputParetoFrontFileName("FUN")
				.setOutputParetoSetFileName("VAR")
				.setIndicatorList(Arrays.asList(new InvertedGenerationalDistance<DoubleSolution>()))
				.setIndependentRuns(INDEPENDENT_RUNS)
				.setNumberOfCores(8) // Maybe increase this for CHPC ??????????????????????????????????????????????????????????????????????????????????
				.build();

		new ExecuteAlgorithms<>(experiment).run();
		new ComputeQualityIndicators<>(experiment).run();
		new GenerateLatexTablesWithStatistics(experiment).run();
		new GenerateWilcoxonTestTablesWithR<>(experiment).run();
	}

	static List<ExperimentAlgorithm<DoubleSolution, List<DoubleSolution>>> configureAlgorithmList(
		List<ExperimentProblem<DoubleSolution>> problemList, int swarmSize) {
		List<ExperimentAlgorithm<DoubleSolution, List<DoubleSolution>>> algorithms = new ArrayList<>();
		for (int run = 0; run < INDEPENDENT_RUNS; run++) {
			for (int i = 0; i < problemList.size(); i++) {
				double mutationProbability = 1.0 / problemList.get(i).getProblem().getNumberOfVariables();
				double mutationDistributionIndex = 20.0;
				Algorithm<List<DoubleSolution>> algorithm = new SMPSOBuilder(
					(DoubleProblem) problemList.get(i).getProblem(),
					new CrowdingDistanceArchive<DoubleSolution>(swarmSize))
					.setMutation(new PolynomialMutation(mutationProbability, mutationDistributionIndex))
					.setMaxIterations(ITERATIONS)
					.setSwarmSize(swarmSize)
					.setSolutionListEvaluator(new SequentialSolutionListEvaluator<DoubleSolution>())
					.build();
				algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i), run));
			}
		}
		return algorithms;
	}
}
