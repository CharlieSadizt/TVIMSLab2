package com.danillopatin.hypergeometricdistribution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalculateManager {

    private int ballsCount = 0;
    private int whiteBalls = 0;
    private int ballsTake = 0;

    //количество розыгрышей
    private int casesCount = 1000;
    private double[] randomCases;

    //вероятности
    private List<Double> tProbabilities;

    //точки на отрезке [0,1]
    private double[] range;

    //частота
    private Map<Integer, Integer> frequence = new HashMap<>();

    private double[] practiceFrequency;

    private int startIndex = 0;

    public CalculateManager(int N, int M, int n, int cC){
        this.ballsCount = N;
        this.whiteBalls = M;
        this.ballsTake = n;
        casesCount = cC;
        calculateTProbabilities();
        calculateRange();
        calculateRCases();
        calculateFrequence();

        
        while(tProbabilities.get(0) == 0.0){
            tProbabilities.remove(0);
            frequence.remove(startIndex);
            startIndex++;
        }
        calculatePracticeFrequence();
    }

    public int getStartIndex(){
        return startIndex;
    }

    public double[] getPracticeFrequence(){
        return practiceFrequency;
    }

    public Map<Integer, Integer> getFrequence(){
        return frequence;
    }

    private void calculatePracticeFrequence(){
        practiceFrequency = new double[tProbabilities.size()];
        for(int i = 0; i < practiceFrequency.length; i++){
            practiceFrequency[i] =  (double)frequence.get(startIndex + i) / casesCount;
        }
    }

    private void calculateFrequence(){
        for(int i = 0; i < tProbabilities.size(); i++){
            frequence.put(i, 0);
        }

        for(int i = 0; i < randomCases.length; i++){
            for(int j = 0; j < range.length; j++){
                if(randomCases[i] <= range[j]){
                    frequence.put(j, frequence.get(j) + 1);
                    break;
                }
            }
        }
    }

    public double[] getTProbabilities(){
        double[] temp = new double[tProbabilities.size()];
        for(int i = 0; i < temp.length; i++){
            temp[i] = tProbabilities.get(i);
        }
        return temp;
    }

    public double[] getRange(){
        return range;
    }

    public double[] getRandomCases(){
        return randomCases;
    }

    private void calculateRange(){
        range = new double[tProbabilities.size()];
        range[0] = tProbabilities.get(0);
        for(int i = 1; i < tProbabilities.size(); i++){
            range[i] = tProbabilities.get(i) + range[i - 1];
        }
    }

    private void calculateRCases(){
        randomCases = new double[casesCount];
        for(int i = 0; i < randomCases.length; i++){
            double point = Math.random();
            if (point > range[range.length - 1]){
                i--;
            } else {
                randomCases[i] = point;
            }
        }
        Arrays.sort(randomCases);
    }

    private void calculateTProbabilities(){
        tProbabilities = new ArrayList<>();
        for(int i = 0; i <= Math.min(ballsTake, whiteBalls); i++){
            //double temp = (C(i, whiteBalls) * C(ballsTake - i, ballsCount - whiteBalls))/(C(ballsTake, ballsCount));
            tProbabilities.add((C(i, whiteBalls) * C(ballsTake - i, ballsCount - whiteBalls))/(C(ballsTake, ballsCount)));
        }
    }

    public static double C(int n, int k) {
        if (n > k) {
            return 0;
        }
        if (n == 0 || n == k) {
            return 1;
        }
        
        long result = 1;
        for (int i = 1; i <= n; i++) {
            result = result * (k - i + 1) / i;
        }
        return result;
    }
}
