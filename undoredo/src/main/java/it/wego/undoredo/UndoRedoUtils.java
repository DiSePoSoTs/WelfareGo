package it.wego.undoredo;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.DepthFirstIterator;

/**
 *
 * @author aleph
 */
public class UndoRedoUtils {

    public static <ThisOperation extends Operation> Set<ThisOperation> getConsequences(Graph<ThisOperation, DefaultEdge> graph, ThisOperation rollbackPoint) {
        Set<ThisOperation> sons = Sets.newHashSet(new DepthFirstIterator<ThisOperation, DefaultEdge>(graph, rollbackPoint));
        sons.remove(rollbackPoint);
        return sons;
    }

    public static <ThisOperation extends Operation> List<ThisOperation> getUndoSequence(Graph<ThisOperation, DefaultEdge> graph, ThisOperation rollbackPoint) {
        Set<ThisOperation> sons = UndoRedoUtils.getConsequences(graph, rollbackPoint);
        List<ThisOperation> undoSequence = new ArrayList<ThisOperation>();
        graph = (Graph) ((AbstractBaseGraph) graph).clone();
        while (!sons.isEmpty()) {
            for (ThisOperation son : sons) {
                if (graph.outDegreeOf(son) == 0) {
                    undoSequence.add(son);
                    graph.removeVertex(son);
                    sons.remove(son);
                    break;
                }
            }
        }
        return undoSequence;
    }

    public static <ThisOperation extends Operation> Collection<ThisOperation> getParents(final Graph<ThisOperation, DefaultEdge> graph, ThisOperation operation) {
        return Collections2.transform(graph.incomingEdgesOf(operation), new Function<DefaultEdge, ThisOperation>() {

            public ThisOperation apply(DefaultEdge input) {
                return graph.getEdgeSource(input);
            }
        });
    }

    public static <ThisOperation extends Operation> List<ThisOperation> getRedoSequence(Graph<ThisOperation, DefaultEdge> graph) {
        graph = (Graph<ThisOperation, DefaultEdge>) ((AbstractBaseGraph) graph).clone();
        List<ThisOperation> sequence = Lists.newArrayList();
        while (!graph.vertexSet().isEmpty()) {
            for (ThisOperation operation : graph.vertexSet()) {
                if (graph.inDegreeOf(operation) == 0) {
                    sequence.add(operation);
                    graph.removeVertex(operation);
                    break;
                }
            }
        }
        return sequence;
    }

    public static <O extends Operation> Set<O> getRelatedOperations(Graph<O, DefaultEdge> graph, O operation) {
        ConnectivityInspector<O, DefaultEdge> inspector = new ConnectivityInspector<O, DefaultEdge>(graph);
        return inspector.connectedSetOf(operation);
    }

    public static <O extends Operation> Set<O> getUnrelatedOperations(Graph<O, DefaultEdge> graph, O operation) {
        Set<O> relatedOperations = getRelatedOperations(graph, operation);
        return Sets.newHashSet(Sets.difference(graph.vertexSet(), relatedOperations));
    }

    public static <O extends Operation> List<O> getRedoSequence(Graph<O, DefaultEdge> graph, O rollbackPoint) {
        Set<O> consequences = getConsequences(graph, rollbackPoint),unrelatedOperations = getUnrelatedOperations(graph, rollbackPoint);
        graph = (Graph<O, DefaultEdge>) ((AbstractBaseGraph) graph).clone();
        graph.removeAllVertices(consequences);
        graph.removeAllVertices(unrelatedOperations);
        return getRedoSequence(graph);
    }
}
