package de.fraunhofer.iem.icognicrypt.results;

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
import crypto.analysis.EnsuredCryptSLPredicate;
import crypto.analysis.IAnalysisSeed;
import crypto.analysis.errors.AbstractError;
import crypto.extractparameter.CallSiteWithParamIndex;
import crypto.extractparameter.ExtractedValue;
import crypto.interfaces.ISLConstraint;
import crypto.rules.CryptSLPredicate;
import de.fraunhofer.iem.icognicrypt.analysis.AndroidProjectAnalysis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sync.pds.solver.nodes.Node;
import typestate.TransitionFunction;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class AnalysisListener extends CrySLAnalysisListener {

    private static final Logger logger = LoggerFactory.getLogger(AnalysisListener.class);
    @Override
    public void beforeAnalysis() {

        //Remove errors before rerunning Cognicrypt
        ErrorProvider.clearError();
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
        //Add error to error provider
        if (abstractError.getErrorLocation().getUnit().isPresent()) {
            logger.info("Error found in method {} in line {}", abstractError.getErrorLocation().getMethod(), abstractError.getErrorLocation().getUnit().get().getJavaSourceStartLineNumber());
            ErrorProvider.addError(abstractError.getErrorLocation().getUnit().get().getJavaSourceStartLineNumber() - 1, new CognicrpyptError(abstractError));
        }
    }

    @Override
    public void ensuredPredicates(Table<Statement, Val, Set<EnsuredCryptSLPredicate>> table, Table<Statement, IAnalysisSeed, Set<CryptSLPredicate>> table1, Table<Statement, IAnalysisSeed, Set<CryptSLPredicate>> table2) {

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

    }

    @Override
    public void onSecureObjectFound(IAnalysisSeed iAnalysisSeed) {

    }
}
