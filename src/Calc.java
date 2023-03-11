import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Calc {
    private JPanel panel;
    private JTextField textField;
    private JLabel label;
    private JButton button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    private JButton buttonPoint;
    private JButton buttonPlus, buttonMinus, buttonMultiply, buttonDivide;
    private JButton buttonClear, buttonBackspace;
    private JButton buttonEquals;
    double total = 0.0;
    char sign = ' ';


    public Calc() {
        class NumberButtons implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent event) {
                JButton btn = (JButton) event.getSource();
                if(label.getText().contains("=")) {
                    textField.setText(btn.getText());
                    label.setText(" ");
                }
                else
                    textField.setText(textField.getText() + btn.getText());
            }
        }

        button0.addActionListener(new NumberButtons());
        button1.addActionListener(new NumberButtons());
        button2.addActionListener(new NumberButtons());
        button3.addActionListener(new NumberButtons());
        button4.addActionListener(new NumberButtons());
        button5.addActionListener(new NumberButtons());
        button6.addActionListener(new NumberButtons());
        button7.addActionListener(new NumberButtons());
        button8.addActionListener(new NumberButtons());
        button9.addActionListener(new NumberButtons());

        buttonPoint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textField.getText().equals(""))
                    textField.setText("0.");
                else if (textField.getText().contains("."))
                    buttonPoint.setEnabled(false);
                else
                    textField.setText(textField.getText() + buttonPoint.getText());
            }
        });

        class SignButtons implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent event) {
                String lastLabelCharacter = label.getText().substring(label.getText().length() - 1);
                if(lastLabelCharacter.equals("+") || lastLabelCharacter.equals("-") || lastLabelCharacter.equals("*") || lastLabelCharacter.equals("/"))
                    buttonEquals.doClick();
                JButton btn = (JButton) event.getSource();
                total = Double.parseDouble(textField.getText());
                sign = btn.getText().charAt(0);
                label.setText(textField.getText() + sign);
                textField.setText("");
            }
        }

        buttonPlus.addActionListener(new SignButtons());
        buttonMinus.addActionListener(new SignButtons());
        buttonMultiply.addActionListener(new SignButtons());
        buttonDivide.addActionListener(new SignButtons());

        buttonClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                total = 0.0;
                sign = ' ';
                textField.setText("");
                label.setText(" ");
            }
        });

        buttonBackspace.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
            }
        });

        buttonEquals.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(label.getText().substring(label.getText().length() - 1).equals("=")) {
                    label.setText(textField.getText() + "=");
                    sign = ' ';
                }
                else
                    label.setText(label.getText() + textField.getText() + "=");
                switch (sign) {
                    case '+' -> total = total + Double.parseDouble(textField.getText());
                    case '-' -> total = total - Double.parseDouble(textField.getText());
                    case '*' -> total = total * Double.parseDouble(textField.getText());
                    case '/' -> total = total / Double.parseDouble(textField.getText());
                    default -> total = Double.parseDouble(textField.getText());
                }
                DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(new Locale("en", "UK"));
                df.applyPattern("###.#");
                textField.setText(String.valueOf(df.format(total)));
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Calculator");
        frame.setContentPane(new Calc().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Toolkit toolkit = Toolkit.getDefaultToolkit();

        frame.setBounds(toolkit.getScreenSize().width / 2 - 150,toolkit.getScreenSize().height / 2 - 200,300,400);
        frame.setVisible(true);

        Calc.chooseLookAndFeel(frame);
    }

    private static void chooseLookAndFeel(JFrame frame) {
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu styles = new JMenu("Styles");
        menuBar.add(styles);

        ArrayList<String> lafClassNames = new ArrayList<>();

        class ChangeLAF implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent event) {
                JMenuItem source = (JMenuItem) event.getSource();
                for(UIManager.LookAndFeelInfo lookAndFeelInfo : UIManager.getInstalledLookAndFeels()) {
                    if (lookAndFeelInfo.getName().equals(source.getText())) {
                        try {
                            UIManager.setLookAndFeel(lookAndFeelInfo.getClassName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        frame.repaint();
                        break;
                    }
                }
            }
        }

        for(UIManager.LookAndFeelInfo lookAndFeelInfo : UIManager.getInstalledLookAndFeels()) {
            JMenuItem item = new JMenuItem(lookAndFeelInfo.getName());
            styles.add(item);
            item.addActionListener(new ChangeLAF());
            lafClassNames.add(lookAndFeelInfo.getClassName());
        }
    }
}
