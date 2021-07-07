package de.fraunhofer.iem.icognicrypt.analysis;

import boomerang.BackwardQuery;
import boomerang.Query;
import boomerang.jimple.Statement;
import boomerang.jimple.Val;
import boomerang.results.ForwardBoomerangResults;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import crypto.analysis.AnalysisSeedWithSpecification;
import crypto.analysis.CrySLAnalysisListener;
import crypto.analysis.EnsuredCrySLPredicate;
import crypto.analysis.IAnalysisSeed;
import crypto.analysis.errors.AbstractError;
import crypto.analysis.errors.IncompleteOperationError;
import crypto.extractparameter.CallSiteWithParamIndex;
import crypto.extractparameter.ExtractedValue;
import crypto.interfaces.ISLConstraint;
import crypto.rules.CrySLPredicate;
import de.fraunhofer.iem.icognicrypt.Constants;
import de.fraunhofer.iem.icognicrypt.actions.RunCogniCryptAction;
import de.fraunhofer.iem.icognicrypt.core.Language.JvmClassNameUtils;
import de.fraunhofer.iem.icognicrypt.results.CogniCryptError;
import de.fraunhofer.iem.icognicrypt.results.IResultProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.SootClass;
import sync.pds.solver.nodes.Node;
import typestate.TransitionFunction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

// TODO: I think it would be a good idea to somehow merge this and IResultProvider implementation.
// TODO: Make implement stubs in base class
@SuppressWarnings("NonDefaultConstructor")
class AnalysisListenerService extends CrySLAnalysisListener implements Disposable {

    private static final Logger logger = LoggerFactory.getLogger(AnalysisListenerService.class);
    private final Project _project;
    private IResultProvider _resultProvider;
    public static boolean EnabledFlag;

    private final List<String> _sourceCodeFiles;

    private AnalysisListenerService(Project project, IResultProvider resultProvider){
        _project = project;
        _resultProvider = resultProvider;
        _sourceCodeFiles = new ArrayList<>();
    }

    @Override
    public void beforeAnalysis() {
        EnabledFlag= false;
        _resultProvider.RemoveAllResults();
        _sourceCodeFiles.clear();

        // TODO: This call should not be in the listener but in class that manages on single analysis
        findClassFiles();
    }

    @Override
    public void afterAnalysis() {
        //After analysis completes, restart code analyzer so that error markers can be updated
        //for (Project project : ProjectManager.getInstance().getOpenProjects())
            DaemonCodeAnalyzer.getInstance(_project).restart();
            RunCogniCryptAction.SetFlag(true);
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
    public void reportError(AbstractError abstractError)
    {
        if(isIgnoredErrorType(abstractError))
            return;
        String name = abstractError.getErrorLocation().getMethod().getDeclaringClass().getName();
        int line = abstractError.getErrorLocation().getUnit().get().getJavaSourceStartLineNumber();
        _resultProvider.AddResult(name, line, new CogniCryptError(abstractError.toErrorMarkerString(), name, line));
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
        System.out.println(i);
    }

    @Override
    public void dispose()
    {
        _resultProvider.RemoveAllResults();
        _resultProvider = null;
    }

    private boolean isIgnoredErrorType(AbstractError abstractError) {
        SootClass affectedClass = abstractError.getErrorLocation().getMethod().getDeclaringClass();
        if(Constants.WARNINGS_IN_SOURCECODECLASSES_ONLY && !isSourceCodeClass(affectedClass)){
            return true;
        }
        //TODO Add options in preference page to enable / disable error types
        return abstractError instanceof IncompleteOperationError;
    }

    private boolean isSourceCodeClass(SootClass affectedClass)
    {
        String className = affectedClass.getName();
        // For nested private classes we need to find the root container, which is always the first entry of any possible '$' delimiter.
        String containerClass = className.split("\\$")[0];

        return JvmClassNameUtils.FindFileFromFullyQualifiedName(containerClass, _project) != null;

        //return _sourceCodeFiles.contains(containerClass);
    }

    private void findClassFiles()
    {
        if(Constants.WARNINGS_IN_SOURCECODECLASSES_ONLY) {
            _sourceCodeFiles.addAll(JvmClassNameUtils.findFullyQualifiedClassNames(_project));
        } else {
            _sourceCodeFiles.clear();
        }
    }
}
