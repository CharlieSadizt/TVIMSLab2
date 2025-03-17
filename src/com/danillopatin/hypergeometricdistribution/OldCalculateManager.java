package com.danillopatin.hypergeometricdistribution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OldCalculateManager {

    //лимит для вычисления вероятностей (максимум 1.0)
    private static final double LIMIT = 0.99997;

    private double p;
    private double q;

    //число экспериментов
    private int n;

    //массив для хранения эксперементов
    private double[] randomCases;

    //массив для хранения вероятностей согласно frequence
    private double[] probability;

    //массив для теоретических вероятностей согласно е
    private double[] theorProbability;

    //список для хранения всех точек а
    private List<Double> e = new ArrayList<>();

    //хэштаблица для хранения значений частоты согласно модели "попытка - число успешных экспериментов"
    private Map<Integer, Integer> frequence = new HashMap<>();

    public List<Double> getE(){
        return e;
    }

    public Map<Integer, Integer> getFrequence() {
        return frequence;
    }

    public double[] getProbability() {
        return probability;
    }

    public double[] getTheorProbability(){
        return theorProbability;
    }

    public OldCalculateManager(double p, int n){
        this.p = p;
        this.n = n;
        q = 1 - p;
        randomCases = new double[n];
        
        calculateDistribution();
        calculateRandomCases();
        calculateFrequence();
        calculateProbability();
        calculateTheorProbability();
    }

    public void calculateDistribution(){
        e.add(p);

        for(int m = 2; e.get(e.size() - 1) + Math.pow(q, (double) m - 1) * p < LIMIT; m++){
			double res = Math.pow(q, (double) m - 1) * p;
			e.add(res + e.get(e.size() - 1));
			if(m == n){
				break;
			}
		}
    }

    public void calculateRandomCases(){
        for(int i = 0; i < randomCases.length; i++){
			double randomCase = Math.random();
			if(randomCase > e.get(e.size() - 1)){
				i--;
				continue;
			}
			else{
				randomCases[i] = randomCase;
			}
		}
        Arrays.sort(randomCases);
    }

    public void calculateFrequence(){
        for(int i = 1; i <= e.size(); i++){
			frequence.put(i,0);
		}

        int index = 1;
		for(int i = 0; i < n; i++){
			if(randomCases[i] > e.get(index - 1)){
				index++;
			} 
			frequence.put(index, frequence.get(index) + 1);
		}
    }

    public void calculateProbability(){
        probability = new double[frequence.size()];
        for(int i = 1; i <= frequence.size(); i++){
			double curr = frequence.get(i);
			curr /= n;
			probability[i - 1] = curr;
		}
    }

    public void calculateTheorProbability(){
        theorProbability = new double[e.size()];
        theorProbability[0] = e.get(0);
        for(int i = 1; i < theorProbability.length; i++){
            theorProbability[i] = e.get(i) - e.get(i - 1);
        }
    }
}
