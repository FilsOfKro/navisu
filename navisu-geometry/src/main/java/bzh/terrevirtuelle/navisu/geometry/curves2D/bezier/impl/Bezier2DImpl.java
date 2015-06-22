/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.geometry.curves2D.bezier.impl;

import bzh.terrevirtuelle.navisu.geometry.curves2D.bezier.Bezier2D;
import bzh.terrevirtuelle.navisu.geometry.curves2D.bezier.Bezier2DServices;
import bzh.terrevirtuelle.navisu.geometry.curves2D.bezier.impl.model.Bezier2DModel;
import bzh.terrevirtuelle.navisu.util.Pair;
import java.util.ArrayList;
import java.util.List;
import org.capcaval.c3.component.ComponentState;

/**
 * NaVisu
 *
 * @date 20 juin 2015
 * @author Serge Morvan
 */
public class Bezier2DImpl
        implements Bezier2D, Bezier2DServices, ComponentState {

    Bezier2DModel bezier;

    /**
     *
     */
    @Override
    public void componentInitiated() {
        bezier = new Bezier2DModel();
    }

    /**
     *
     * @param si
     * @return
     */
    @Override
    public List<Pair<Double, Double>> compute(List<Pair<Double, Double>> si) {
        return compute(si, 0.1);
    }

    /**
     *
     * @param si
     * @param inc
     * @return
     */
    @Override
    public List<Pair<Double, Double>> compute(List<Pair<Double, Double>> si, double inc) {
        bezier.setSi(si);
        return bezier.compute(inc);
    }

    /**
     *
     * @param data
     * @return
     */
    @Override
    public List<Pair<Double, Double>> leastSquareCompute(List<Pair<Double, Double>> data) {
        return leastSquareCompute(data, 0.1);
    }

    /**
     *
     * @param data
     * @param inc
     * @return
     */
    @Override
    public List<Pair<Double, Double>> leastSquareCompute(List<Pair<Double, Double>> data, double inc) {
        return leastSquareCompute(data, 0.1, 5);
    }

    /**
     *
     * @param data
     * @param degree
     * @return
     */
    @Override
    public List<Pair<Double, Double>> leastSquareCompute(List<Pair<Double, Double>> data, int degree) {   
        return leastSquareCompute(data, 0.1, degree);
    }

    /**
     *
     * @param data
     * @param inc
     * @param degree
     * @return
     */
    @Override
    public List<Pair<Double, Double>> leastSquareCompute(List<Pair<Double, Double>> data, double inc, int degree) {
        List<Pair<Double, Double>> filtered = filter(data);
        return null;
    }

    private List<Pair<Double, Double>> filter(List<Pair<Double, Double>> data) {
        List<Pair<Double, Double>> dataFiltered = new ArrayList<>();
        int size = data.size();
        Pair<Double, Double> tmp = data.get(0);
        dataFiltered.add(tmp);
        for (int i = 1; i < size; i++) {
            if (tmp != data.get(i)) {
                dataFiltered.add(data.get(i));
                tmp = data.get(i);
            }
        }
        return dataFiltered;
    }

    /**
     *
     */
    @Override
    public void componentStarted() {
    }

    /**
     *
     */
    @Override
    public void componentStopped() {
    }

}
