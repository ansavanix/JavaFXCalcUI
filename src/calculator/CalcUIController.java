package calculator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
//import javafx.scene.control.Label;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URL;
import java.util.ResourceBundle;



/**
 * FXML Controller class
 *
 * @author rajesh
 */
public class CalcUIController implements Initializable {

    Double tempo = 0.0, secondNumber = 0.0, sum = 0.0;
    String operatorPressed = "";
    boolean isNegative = false;


    @FXML
    TextField outputTF;
    @FXML
    Label negativeLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        outputTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*([\\.]\\d*)?")) {
                    outputTF.setText(oldValue);
                }
            }
        });
    }
    private String getButtonText(ActionEvent pressed)
    {
        Button button = (Button) pressed.getSource();
        return button.getText();
    }
    private double tfDouble()
    {
        double output;
        output = Double.parseDouble(outputTF.getText());
        return output;
    }
    //If operator is pressed set outputTF to the button press, else set outputTF to what's in outputTF and button press
    @FXML
    private void onNumberClick(ActionEvent event)
    {

        String text = getButtonText(event);
        String oldText = outputTF.getText();
        if (oldText.equals(""))
        {
            outputTF.setText(text);
        }
        else if (oldText.equals("0") && !text.equals("."))
        {
            outputTF.setText(text);
        }
        else
        {
            outputTF.setText(oldText + text);
        }

        System.out.println("Number Click: " + text);

    }

    /*
        Checks if outputTF is not empty
        Checks which button was pressed and performs the correct operation on sum
        Checks if button was "=" if so sets outputTF to the sum & operatorPressed to default
            if not sets outputTF to default and operatorPressed to the button pressed
        flags operator pressed to true
     */
    @FXML
    private void onOperatorClick(ActionEvent event) {
        String text = getButtonText(event);
        System.out.println("Operator Click: " + text);
        if (text.equals("-") && outputTF.getText().equals(""))
        {
            isNegative = true;
            negativeLabel.setText("-");
        }
        else if (!text.equals("=") && operatorPressed.equals(""))
        {

                tempo = tfDouble();
                outputTF.setText("");
                negativeLabel.setText("");
                operatorPressed = text;
                if (isNegative)
                {
                    tempo = -tempo;
                }
                isNegative = false;
        }
        else
        {
            secondNumber = tfDouble();
            if (isNegative)
            {
                secondNumber = -secondNumber;
            }

            switch (operatorPressed) {
                case "+":
                    sum = tempo + secondNumber;
                    break;
                case "/":
                    sum = tempo / secondNumber;
                    break;
                case "*":
                    sum = tempo * secondNumber;
                    break;
                case "-":
                    sum = tempo - secondNumber;
                    break;
                case "%":
                    sum = tempo % secondNumber;
                    break;
                default:
                    sum = secondNumber;
                    break;
            }
            if (sum < 0)
            {
                isNegative = true;
                sum = -sum;
            }
            else
            {
                isNegative = false;
            }
            MathContext m = new MathContext(10);
            String sumString = new BigDecimal(sum).round(m).toPlainString();
            if (sumString.contains("."))
            {
                int startIndex = -1;
                for (int i = 0; i < sumString.length() - 1; i++)
                {
                    if (sumString.substring(i,i+1).equals("0") && startIndex == -1)
                    {
                        startIndex = i;
                    }
                    else if (!sumString.substring(i,i+1).equals("0"))
                    {
                        startIndex = -1;
                    }

                }
                if (startIndex > -1)
                {
                    sumString = sumString.substring(0,startIndex);
                }
            }
            if (isNegative)
            {
                negativeLabel.setText("-");
            }
            else
            {
                negativeLabel.setText("");
            }
            outputTF.setText(sumString);
            if (!isNegative)
            {
                System.out.println("Solution: " + sumString);
            }
            else
            {
                System.out.println("Solution: -" + sumString);
            }

            operatorPressed = "";
        }

    }

    //If outputTF is not 1 character long remove 1 character from the outputTF
    //else set outputTF back to default
    @FXML
    private void onDELClick() {
        if (outputTF.getText().length() <= 1)
        {
            outputTF.setText("");
        }
        else
        {
            outputTF.setText(outputTF.getText().substring(0,outputTF.getText().length() -1));
        }
        System.out.println("DEL Click");

    }

    //Reset all values to their default states
    @FXML
    private void onCEClick() {
        outputTF.setText("");
        negativeLabel.setText("");
        isNegative = false;
        operatorPressed = "";
        tempo = 0.0;
        sum = 0.0;
        System.out.println("CE Click");
    }
}
