package de.fraunhofer.iem.icognicrypt.analysis;

import boomerang.BackwardQuery;
import boomerang.Query;
import boomerang.jimple.Statement;
import boomerang.jimple.Val;
import boomerang.results.ForwardBoomerangResults;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import crypto.analysis.AnalysisSeedWithSpecification;
import crypto.analysis.CrySLAnalysisListener;
import crypto.analysis.EnsuredCrySLPredicate;
import crypto.analysis.IAnalysisSeed;
import crypto.analysis.errors.AbstractError;
import crypto.extractparameter.CallSiteWithParamIndex;
import crypto.extractparameter.ExtractedValue;
import crypto.interfaces.ISLConstraint;
import crypto.rules.CrySLPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sync.pds.solver.nodes.Node;
import typestate.TransitionFunction;

import java.util.Collection;
import java.util.Set;

class AnalysisListener extends CrySLAnalysisListener {

    private static final Logger logger = LoggerFactory.getLogger(AnalysisListener.class);
    @Override
    public void beforeAnalysis() {


    }

    @Override
    public void afterAnalysis() {

        //After analysis completes, restart code analyzer so that error markers can be updated
        for (Project project : ProjectManager.getInstance().getOpenProjects())
            DaemonCodeAnalyzer.getInstance(project).restart();
    }

    @Override
    public void beforeConstraintCheck(AnalysisSeedWithSpecification analysisSeedWithSpecification) {

    }

    @Override
    public void afterConstraintCheck(AnalysisSeedWithSpecification analysisSeedWithSpecification) {

    }

    @Override
    public void beforePredicateCheck(AnalysisSeedWithSpecification analysisSeedWithSpecification) {

    }

    @Override
    public void afterPredicateCheck(AnalysisSeedWithSpecification analysisSeedWithSpecification) {

    }

    @Override
    public void seedStarted(IAnalysisSeed iAnalysisSeed) {

    }

    @Override
    public void boomerangQueryStarted(Query query, BackwardQuery backwardQuery) {

    }

    @Override
    public void boomerangQueryFinished(Query query, BackwardQuery backwardQuery) {

    }

    @Override
    public void reportError(AbstractError abstractError) {
        int i = 0;
        i++;
    }

    @Override
    public void ensuredPredicates(Table<Statement, Val, Set<EnsuredCrySLPredicate>> table, Table<Statement, IAnalysisSeed, Set<CrySLPredicate>> table1, Table<Statement, IAnalysisSeed, Set<CrySLPredicate>> table2) {

    }

    @Override
    public void checkedConstraints(AnalysisSeedWithSpecification analysisSeedWithSpecification, Collection<ISLConstraint> collection) {

    }

    @Override
    public void onSeedTimeout(Node<Statement, Val> node) {

    }

    @Override
    public void onSeedFinished(IAnalysisSeed iAnalysisSeed, ForwardBoomerangResults<TransitionFunction> forwardBoomerangResults) {

    }

    @Override
    public void collectedValues(AnalysisSeedWithSpecification analysisSeedWithSpecification, Multimap<CallSiteWithParamIndex, ExtractedValue> multimap) {

    }

    @Override
    public void discoveredSeed(IAnalysisSeed iAnalysisSeed) {
        logger.info("CogniCrypt found a seed {}", iAnalysisSeed);
    }

    @Override
    public void onSecureObjectFound(IAnalysisSeed iAnalysisSeed) {

    }

    @Override
    public void addProgress(int i, int i1)
    {

    }
}
