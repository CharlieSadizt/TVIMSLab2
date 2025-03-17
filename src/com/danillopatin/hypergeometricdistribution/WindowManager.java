package com.danillopatin.hypergeometricdistribution;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class WindowManager extends JFrame {

    private CalculateManager cm;

    private int height_;
    private int width_;
    private int cases = 0;
    private int N = 0;
    private int M = 0;
    private int n = 0;

    private Object[][] data;
    private final Object[] TITLE = {"ξ", "Частота", "Относ. част.", "Вер. теор."};
    private Canvas histogram;
    private JPanel panel;
    private JTable table;
    private JScrollPane jsp;

    private JButton casesEnterButton;
    private JTextField casesEnterField;

    private JButton NEnterButton;
    private JTextField NEnterField;
    
    private JButton MEnterButton;
    private JTextField MEnterField;

    private JButton nEnterButton;
    private JTextField nEnterField;

    private JButton calculateButton;
    

    public WindowManager(int height, int width){
        super("Гипергеометрическое распределение");
        height_ = height;
        width_ = width;
        GUIInit();
    }

    private void GUIInit(){
        setSize(width_, height_);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        panel = new JPanel(); 
        histogram = new Canvas();
        panel.setLayout(null);

        panel.add(histogram);
        histogram.setLocation(10, 10);

        initTable();
        panel.add(jsp);
        jsp.setBounds(920, 10, 1200 - 910, 640);

        NEnterField = new JTextField();
        panel.add(NEnterField);
        NEnterField.setBounds(160, 550, 120, 25);
        JLabel NText = new JLabel("N: ");
        panel.add(NText);
        NText.setBounds(145, 550, 40,20);
        NEnterButton = new JButton("Ввести");
        panel.add(NEnterButton);
        NEnterButton.setBounds(290, 550, 100, 25);
        ActionListener NInput = (ActionEvent e) -> {
            try{
                N = Integer.parseInt(NEnterField.getText());
            } catch (Exception ex){
                N = 0;
            }
            System.out.println("Input N: " + N);
        };
        NEnterButton.addActionListener(NInput);

        MEnterField = new JTextField();
        panel.add(MEnterField);
        MEnterField.setBounds(160, 580, 120, 25);
        JLabel MText = new JLabel("M: ");
        panel.add(MText);
        MText.setBounds(145, 580, 40,20);
        MEnterButton = new JButton("Ввести");
        panel.add(MEnterButton);
        MEnterButton.setBounds(290, 580, 100, 25);
        ActionListener MInput = (ActionEvent e) -> {
            try{
                M = Integer.parseInt(MEnterField.getText());
            } catch (Exception ex){
                M = 0;
            }
            System.out.println("Input M: " + M);
        };
        MEnterButton.addActionListener(MInput);

        nEnterField = new JTextField();
        panel.add(nEnterField);
        nEnterField.setBounds(160, 610, 120, 25);
        JLabel nText = new JLabel("n: ");
        panel.add(nText);
        nText.setBounds(145, 610, 40,20);
        nEnterButton = new JButton("Ввести");
        panel.add(nEnterButton);
        nEnterButton.setBounds(290, 610, 100, 25);
        ActionListener nInput = (ActionEvent e) -> {
            try{
                n = Integer.parseInt(nEnterField.getText());
            } catch (Exception ex){
                n = 0;
            }
            System.out.println("Input n: " + n);
        };
        nEnterButton.addActionListener(nInput);

        casesEnterField = new JTextField();
        panel.add(casesEnterField);
        casesEnterField.setBounds(160, 640, 120, 25);

        JLabel casesText = new JLabel("Число розыгрышей: ");
        panel.add(casesText);
        casesText.setBounds(35, 640, 130,20);

        casesEnterButton = new JButton("Ввести");
        panel.add(casesEnterButton);
        casesEnterButton.setBounds(290, 640, 100, 25);
        ActionListener casesInput = (ActionEvent e) -> {
            try{
                cases = Integer.parseInt(casesEnterField.getText());
            } catch (Exception ex){
                cases = 0;
            }
            System.out.println("Input cases: " + cases);
        };
        casesEnterButton.addActionListener(casesInput);

        calculateButton = new JButton("РАСЧИТАТЬ");
        panel.add(calculateButton);
        calculateButton.setBounds(400, 550, 150, 55);
        ActionListener doCalculate = (ActionEvent e) -> {
            //CALCULATE
            
            if ((N == 0) || (cases == 0) || (M == 0) || (n == 0)){
                return;
            }

            if((M > N) || (n > N)){
                JOptionPane.showMessageDialog(null, "Должно выполняться условие N > M и N > n");
                return;
            }

            cm = new CalculateManager(N , M, n, cases);
            System.out.printf("Calculate manager created with N = %d, M = %d, n = %d, cases = %d\n", N, M, n, cases);
            panel.remove(jsp);
            initTable();
            histogram.repaint();
        };
        calculateButton.addActionListener(doCalculate);
        
        JLabel info = new JLabel("Синяя гистограмма - относительная частота.");
        panel.add(info);
        info.setBounds(580, 550, 400, 20);

        JLabel info2 = new JLabel("Красная - теоретическая вероятность.");
        panel.add(info2);
        info2.setBounds(580, 570, 400, 20);

        setContentPane(panel);
        setVisible(true);
    }

    class Canvas extends JPanel{

        private static final int MAX_HEIGHT = 480; 
        private static final int MAX_WIDTH = 850;
        private static final int LOWER_Y = 10;
        private static final int LOWER_X = 25;
        private static final int HIGHER_Y = 490;
        private static final int HIGHER_X = 875;
        private static final int BASE_LINE = 450;
        private double multiIndex = 1.0;

        public Canvas(){
            super();
            setSize(900, 500);
            setBackground(new Color(0xffffff));
        }

        public void paintMarking(Graphics2D g2d, double firstValue){
            Stroke tempStroke = g2d.getStroke();
            float[] dashPattern = {10, 10};
            Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0);
            g2d.setStroke(dashed);
            g2d.setColor(Color.LIGHT_GRAY);

            g2d.drawLine(LOWER_X, (int)((MAX_HEIGHT - (0 * multiIndex)) + LOWER_Y), HIGHER_X, (int)((MAX_HEIGHT - (0 * multiIndex)) + LOWER_Y));
            g2d.drawLine(LOWER_X, (int)((MAX_HEIGHT - (120 * multiIndex)) + LOWER_Y), HIGHER_X, (int)((MAX_HEIGHT - (120 * multiIndex)) + LOWER_Y));
            g2d.drawLine(LOWER_X, (int)((MAX_HEIGHT - (240 * multiIndex)) + LOWER_Y), HIGHER_X, (int)((MAX_HEIGHT - (240 * multiIndex)) + LOWER_Y));
            g2d.drawLine(LOWER_X, (int)((MAX_HEIGHT - (360 * multiIndex)) + LOWER_Y), HIGHER_X, (int)((MAX_HEIGHT - (360 * multiIndex)) + LOWER_Y));
            g2d.drawLine(LOWER_X, (int)((MAX_HEIGHT - (480 * multiIndex)) + LOWER_Y), HIGHER_X, (int)((MAX_HEIGHT - (480 * multiIndex)) + LOWER_Y));

            if (firstValue != 0.0){
                g2d.drawLine(LOWER_X, (int)((MAX_HEIGHT - ((firstValue * MAX_HEIGHT) * multiIndex)) + LOWER_Y), HIGHER_X, (int)((MAX_HEIGHT - ((firstValue * MAX_HEIGHT) * multiIndex)) + LOWER_Y));
            }

            g2d.setStroke(tempStroke);
            g2d.setColor(Color.BLACK);
            g2d.drawString("0,0", 0, (int)((MAX_HEIGHT - (0 * multiIndex)) + LOWER_Y + 5));
            g2d.drawString("0,25", 0, (int)((MAX_HEIGHT - (120 * multiIndex)) + LOWER_Y + 5));
            g2d.drawString("0,50", 0, (int)((MAX_HEIGHT - (240 * multiIndex)) + LOWER_Y + 5));
            g2d.drawString("0,75", 0, (int)((MAX_HEIGHT - (360 * multiIndex)) + LOWER_Y + 5));
            g2d.drawString("1,0", 0, (int)((MAX_HEIGHT - (480 * multiIndex)) + LOWER_Y + 5));

            if (firstValue != 0.0){
                g2d.drawString(String.format("%.2f", firstValue), 0, (int)((MAX_HEIGHT - ((firstValue * MAX_HEIGHT) * multiIndex)) + LOWER_Y + 5));
            }
        }

        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;

            if (cm == null){
                paintMarking(g2d, 0.0);
            }

            else {

                //рисование гистограмм
                double[] probability = cm.getPracticeFrequence();
                double[] probabilityTheor = cm.getTProbabilities();
                int hWidth = (int) ((MAX_WIDTH / probability.length) / 2);
                double practiceLarger = maxElem(probability);
                double theorLarger = maxElem(probabilityTheor);
                double larger = (practiceLarger > theorLarger) ? practiceLarger : theorLarger;
                multiIndex = BASE_LINE / (larger * MAX_HEIGHT);
                paintMarking(g2d, larger);
                
                for(int i = 0; i < probability.length; i++){
                    g2d.setColor(Color.BLUE);
                    int blueTall = (int) Math.ceil(probability[i] * MAX_HEIGHT * multiIndex);
                    g2d.fillRect(LOWER_X + (i * hWidth * 2), HIGHER_Y - blueTall, hWidth, blueTall);

                    g2d.setColor(Color.RED);
                    int redTall = (int) Math.ceil(probabilityTheor[i] * MAX_HEIGHT * multiIndex);
                    g2d.fillRect(LOWER_X + (i * hWidth * 2) + hWidth, HIGHER_Y - redTall, hWidth - 5, redTall);
                }
            }
        }
    }

    private void initData(){
        if (data == null){
            data = new Object[][] {
                {"", "", "", ""}
            };
        }
        else {
            double[] tempProbability = cm.getTProbabilities();
            double[] tempPFrequence = cm.getPracticeFrequence();
            Map<Integer, Integer> tempFrequence = cm.getFrequence();
            data = new Object[tempProbability.length][4];
            for(int i = 0; i < tempProbability.length; i++){
                
                data[i] = new Object[] {cm.getStartIndex() + i, tempFrequence.get(cm.getStartIndex() + i), String.format("%.6f", tempPFrequence[i]), String.format("%.6f", tempProbability[i]) };
            }
        }
    }

    private void initTable(){
        initData();
        table = new JTable(data, TITLE);
        jsp = new JScrollPane(table);
        panel.add(jsp);
        jsp.setBounds(920, 10, 1200 - 910, 640);
    }

    private double maxElem(double[] arr){
        double max = arr[0];
        for(double i: arr){
            if (i > max) max = i;
        }
        return max;
    }
}
