package graph;

import com.jgraph.layout.JGraphFacade;
import com.jgraph.layout.graph.JGraphSimpleLayout;
import common.Util;
import exceptions.InvalidStateException;
import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgrapht.DirectedGraph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultListenableGraph;
import org.jgrapht.graph.DirectedMultigraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.dynamic.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class JGraphDynamicStateChangeViewer extends JApplet {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    //private static final long serialVersionUID = 3256444702936019250L;
    private static final Color DEFAULT_BG_COLOR = Color.decode("#FAFBFF");
    private static final Dimension DEFAULT_SIZE = new Dimension(530, 420);

    private JGraphModelAdapter<String, DefaultEdge> jgAdapter;

    public static void main(String [] args) {
        JGraphDynamicStateChangeViewer component = new JGraphDynamicStateChangeViewer();

        component.init();

        JFrame frame = new JFrame();
        frame.getContentPane().add(component);
        frame.setTitle("JGraphT Adapter to JGraph Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void init() {
        String fileNameValidTransitionTable = null;
        try {
            fileNameValidTransitionTable = Util.getStringProperty("FileNameValidTransitionTable");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            transitions = createValidTransitionTable(fileNameValidTransitionTable);
        } catch (IOException e) {
            log.error("JGraphAdapterDemo.init() IOException " + e.getMessage());
            return;
        } catch (InvalidStateException e) {
            log.error("JGraphAdapterDemo.init() InvalidStateException " + e.getMessage());
            return;
        }

        // create a JGraphT graph
        ListenableGraph<String, DefaultEdge> g = new ListenableDirectedMultigraph<String, DefaultEdge>( DefaultEdge.class);

        // create a visualization using JGraph, via an adapter
        jgAdapter = new JGraphModelAdapter<String, DefaultEdge>(g);

        JGraph jgraph = new JGraph(jgAdapter);

        adjustDisplaySettings(jgraph);
        getContentPane().add(jgraph);
        resize(DEFAULT_SIZE);

        for (int i=1; i<colNames.length; i++) {
            g.addVertex(colNames[i]);
           // positionVertexAt(colNames[i], 520-i*100, 0+i*50);
        }

        for (int i=1; i<colNames.length; i++) {
            for (int j=1;j<colNames.length; j++) {
                if (transitions[j-1][i-1].doTransition) {
                    g.addEdge(colNames[i], colNames[j]);
                }
            }
        }
        final JGraphSimpleLayout layout = new JGraphSimpleLayout(0, 400, 200);
        final JGraphFacade facade = new JGraphFacade(jgraph);
        layout.run(facade);
        jgraph.getGraphLayoutCache().edit(facade.createNestedMap(true, true));
    }

    private void adjustDisplaySettings(JGraph jg) {
        jg.setPreferredSize(DEFAULT_SIZE);
        Color c = DEFAULT_BG_COLOR;
        String colorStr = null;
        try {
            colorStr = getParameter("bgcolor");
        } catch (Exception e) {
            log.error("JGraphAdapterDemo.adjustDisplaySettings() Exception. No such parameter. " + e.getMessage());
        }

        if (colorStr != null) {
            c = Color.decode(colorStr);
        }
        jg.setBackground(c);
    }

    @SuppressWarnings("unchecked")
    private void positionVertexAt(Object vertex, int x, int y) {
        DefaultGraphCell cell = jgAdapter.getVertexCell(vertex);
        AttributeMap attr = cell.getAttributes();
        Rectangle2D bounds = GraphConstants.getBounds(attr);

        Rectangle2D newBounds = new Rectangle2D.Double( x, y, bounds.getWidth(), bounds.getHeight());

        GraphConstants.setBounds(attr, newBounds);

        AttributeMap cellAttr = new AttributeMap();
        cellAttr.put(cell, attr);
        jgAdapter.edit(cellAttr, null, null, null);
    }

    static DefaultGraphCell oldCell = null;
    protected void colourVertex(Object vertex) {
        if (oldCell != null) {
            AttributeMap oldAttr = oldCell.getAttributes();
            oldAttr.applyValue(GraphConstants.BACKGROUND, new Color(255,153,0));
            AttributeMap oldCellAttr = new AttributeMap();
            oldCellAttr.put(oldCell, oldAttr);
            jgAdapter.edit(oldCellAttr, null, null, null);
        }
        DefaultGraphCell cell = jgAdapter.getVertexCell(vertex);

        AttributeMap attr = cell.getAttributes();
        attr.applyValue(GraphConstants.BACKGROUND, Color.RED);

        AttributeMap cellAttr = new AttributeMap();
        cellAttr.put(cell, attr);
        jgAdapter.edit(cellAttr, null, null, null);
        revalidate();
        repaint();
        oldCell=cell;
    }

    private Node[][] transitions = null;
    private String[] colNames = null;
    private Node[][] createValidTransitionTable(String fileName) throws IOException, InvalidStateException {
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();
        colNames = line.split(",");
        int len = colNames.length - 1;
        Node[][] transitionTable = new Node[len][len];
        int row = 0;
        while ((line = bufferedReader.readLine()) != null) {
            String[] vals = line.split(",");
            java.util.List<String> cells = Arrays.asList(vals);
            int col = 0;
            String rowName = null;
            for (String cell : cells) {
                if (col == 0) {
                    rowName = cell;
                } else {
                    if (cell.contains("Y")) {
                        Node node = new Node(col - 1, row);
                        transitionTable[row][col - 1] = node;
                    } else {
                        Node node = new Node(colNames[col], rowName, false);
                        transitionTable[row][col - 1] = node;
                    }
                }
                col++;
            }
            row++;
        }
        fileReader.close();
        return transitionTable;
    }

    private static class ListenableDirectedMultigraph<V, E> extends DefaultListenableGraph<V, E>  implements DirectedGraph<V, E> {
        private static final long serialVersionUID = 1L;
        ListenableDirectedMultigraph(Class<E> edgeClass) {
            super(new DirectedMultigraph<V, E>(edgeClass));
        }
    }
}

