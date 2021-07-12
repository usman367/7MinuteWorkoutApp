package com.example.a7minuteworkout2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

     val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW" // Metric Unit View
     val US_UNITS_VIEW = "US_UNIT_VIEW" // US Unit View

    // A variable to hold a value to make a selected view visible (Currently making the metric system visible)
    var currentVisibleView: String = METRIC_UNITS_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_m_i)

        //Setting up an action bar in BMI calculator activity using toolbar and adding a back arrow button along with click event.
        val toolbar_bmi_activity = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_bmi_activity)
        setSupportActionBar(toolbar_bmi_activity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true) //set back button
        supportActionBar?.title = "CALCULATE BMI" // Setting a title in the action bar.
        //Same as:
//        val actionbar = supportActionBar
//        if(actionbar != null){
//            actionbar.setDisplayHomeAsUpEnabled(true)
//        actionBar.title = "CALCULATE BMI"
//        }

        //When the back button is pressed on the toolbar, go back
        toolbar_bmi_activity.setNavigationOnClickListener {
            onBackPressed()
        }


        // Adding a click event to METRIC UNIT Calculate button and after valid input calculating it.
        // Button will calculate the input values in Metric Units
        val btnCalculateUnits = findViewById<Button>(R.id.btnCalculateUnits)
        btnCalculateUnits.setOnClickListener {


            if(currentVisibleView.equals(METRIC_UNITS_VIEW)){

                // The values are validated.
                if (validateMetricUnits()) {
                    val etMetricUnitWeight = findViewById<EditText>(R.id.etMetricUnitWeight)
                    val etMetricUnitHeight = findViewById<EditText>(R.id.etMetricUnitHeight)

                    // The height value is converted to a float value and divided by 100 to convert it to meter.
                    val heightValue: Float = etMetricUnitHeight.text.toString().toFloat() / 100

                    // The weight value is converted to a float value
                    val weightValue: Float = etMetricUnitWeight.text.toString().toFloat()

                    // BMI value is calculated in METRIC UNITS using the height and weight value.
                    val bmi = weightValue / (heightValue * heightValue)

                    //Pass in the calculated BMI to the method which also gives a description of the result
                    displayBMIResult(bmi)
                } else {
                    //If the user doesn't enter valid values, tell them about it
                    Toast.makeText(this@BMIActivity, "Please enter valid values.", Toast.LENGTH_SHORT)
                        .show()
                }
            }else{
                //Otherwise, calculate the BMI for the US units
                if (validateUsUnits()) {
                    //If the values are valid, then calculate the BMI
                    val etUsUnitWeight = findViewById<EditText>(R.id.etUSUnitWeight)
                    val etUsUnitHeightFeet = findViewById<EditText>(R.id.etUSUnitHeightFeet)
                    val etUsUnitHeightInch = findViewById<EditText>(R.id.etUSUnitHeightInch)

                    //Convert the height values into strings and the weight into a float
                    val usUnitHeightValueFeet: String = etUsUnitHeightFeet.text.toString() // Height Feet value entered in EditText component.
                    val usUnitHeightValueInch: String = etUsUnitHeightInch.text.toString() // Height Inch value entered in EditText component.
                    val usUnitWeightValue: Float = etUsUnitWeight.text.toString().toFloat() // Weight value entered in EditText component.

                    // Here the Height Feet and Inch values are merged and multiplied by 12 for converting it to inches.
                    val heightValue = usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12

                    // This is the Formula for US UNITS result.
                    // Reference Link : https://www.cdc.gov/healthyweight/assessing/bmi/childrens_bmi/childrens_bmi_formula.html
                    val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))

                    displayBMIResult(bmi) // Displaying the result into UI
                } else {
                    //Otherwise, let the user know they haven't entered correct values
                    Toast.makeText(this@BMIActivity, "Please enter valid values.", Toast.LENGTH_SHORT).show()
                }

            }

        }

        //At the start the metric system is shown
        makeVisibleMetricUnitsView()

        // Adding a check change listener to the radio group and according to the radio button.
        // Radio Group change listener is set to the radio group which is added in XML.
        val rgUnits = findViewById<RadioGroup>(R.id.rgUnits)
        rgUnits.setOnCheckedChangeListener { radioGroup: RadioGroup, checkedId: Int ->

            // Here if the checkId is METRIC UNITS view then make the view visible else US UNITS view.
            //If the checkID is set to the metric units, then make the metrics units visible
            if (checkedId == R.id.rbMetricUnits) {
                makeVisibleMetricUnitsView()
            } else {
                //Otherwise make the US units visible
                makeVisibleUsUnitsView()
            }
        }
    }

    // Validating the METRIC UNITS CALCULATION input.
    /**
     * Function is used to validate the input values for METRIC UNITS.
     */
    private fun validateMetricUnits(): Boolean {
        var isValid = true
        val etMetricUnitWeight = findViewById<EditText>(R.id.etMetricUnitWeight)
        val etMetricUnitHeight = findViewById<EditText>(R.id.etMetricUnitHeight)

//        If any of the values are empty set the is Valid to false
        if (etMetricUnitWeight.text.toString().isEmpty()) {
            isValid = false
        } else if (etMetricUnitHeight.text.toString().isEmpty()) {
            isValid = false
        }

        return isValid
    }

    // Validating the US UNITS view input values.
    /**
     * Function is used to validate the input values for US UNITS.
     */
    private fun validateUsUnits(): Boolean {
        var isValid = true
        val etUsUnitWeight = findViewById<EditText>(R.id.etUSUnitWeight)
        val etUsUnitHeightFeet = findViewById<EditText>(R.id.etUSUnitHeightFeet)
        val etUsUnitHeightInch = findViewById<EditText>(R.id.etUSUnitHeightInch)

        if (etUsUnitWeight.text.toString().isEmpty()) {
            isValid = false
        } else if (etUsUnitHeightFeet.text.toString().isEmpty()) {
            isValid = false
        } else if (etUsUnitHeightInch.text.toString().isEmpty()) {
            isValid = false
        }

        return isValid
    }


    // Displaying the calculated BMI UI what we have designed earlier.
    private fun displayBMIResult(bmi: Float) {

        val bmiLabel: String
        val bmiDescription: String

        //Give them an appropriate message based on their BMI score
        //compareTo() gives 0 if the values are the same, a negative number if its less than the other number,
        // and a positive number if its greater
        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (java.lang.Float.compare(bmi, 25f) > 0 && java.lang.Float.compare(bmi, 30f) <= 0) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        val tvYourBMI = findViewById<TextView>(R.id.tvYourBMI)
        val tvBMIValue = findViewById<TextView>(R.id.tvBMIValue)
        val tvBMIType = findViewById<TextView>(R.id.tvBMIType)
        val tvBMIDescription = findViewById<TextView>(R.id.tvBMIDescription)

        //Making sure all the text views are set to visible
        tvYourBMI.visibility = View.VISIBLE
        tvBMIValue.visibility = View.VISIBLE
        tvBMIType.visibility = View.VISIBLE
        tvBMIDescription.visibility = View.VISIBLE

        // This is used to round the result value to 2 decimal values after "."
        //Using the bmi that's passed in as a parameter to this method
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        //Set the values to the TextViews
        tvBMIValue.text = bmiValue // Value is set to TextView
        tvBMIType.text = bmiLabel // Label is set to TextView
        tvBMIDescription.text = bmiDescription // Description is set to TextView
    }


    /**
     * Function is used to make the METRIC UNITS VIEW visible and hide the US UNITS VIEW.
     */
    private fun makeVisibleMetricUnitsView() {

        val llMetricUnitsView = findViewById<LinearLayout>(R.id.llMetricUnitsView)
        val llUsUnitsView = findViewById<LinearLayout>(R.id.llUsUnitsView)

        val tvYourBMI = findViewById<TextView>(R.id.tvYourBMI)
        val tvBMIValue = findViewById<TextView>(R.id.tvBMIValue)
        val tvBMIType = findViewById<TextView>(R.id.tvBMIType)
        val tvBMIDescription = findViewById<TextView>(R.id.tvBMIDescription)

        val etMetricUnitHeight = findViewById<EditText>(R.id.etMetricUnitHeight)
        val etMetricUnitWeight = findViewById<EditText>(R.id.etMetricUnitWeight)

        //Make the metric view visible amd the US view gone
        currentVisibleView = METRIC_UNITS_VIEW
        llMetricUnitsView.visibility = View.VISIBLE // METRIC UNITS VIEW is Visible
        llUsUnitsView.visibility = View.GONE // US UNITS VIEW is hidden

        //Clearing the values of the text boxes (We want the boxes to not have any values when we click on the other radio button)
        etMetricUnitHeight.text!!.clear() // height value is cleared if it is added.
        etMetricUnitWeight.text!!.clear() // weight value is cleared if it is added.

        tvYourBMI.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
        tvBMIValue.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
        tvBMIType.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
        tvBMIDescription.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
    }

    // Making a function to make the US UNITS view visible.
    private fun makeVisibleUsUnitsView() {

        val llMetricUnitsView = findViewById<LinearLayout>(R.id.llMetricUnitsView)
        val llUsUnitsView = findViewById<LinearLayout>(R.id.llUsUnitsView)

        val etUsUnitWeight = findViewById<EditText>(R.id.etUSUnitWeight)
        val etUsUnitHeightFeet = findViewById<EditText>(R.id.etUSUnitHeightFeet)
        val etUsUnitHeightInch = findViewById<EditText>(R.id.etUSUnitHeightInch)

        val tvYourBMI = findViewById<TextView>(R.id.tvYourBMI)
        val tvBMIValue = findViewById<TextView>(R.id.tvBMIValue)
        val tvBMIType = findViewById<TextView>(R.id.tvBMIType)
        val tvBMIDescription = findViewById<TextView>(R.id.tvBMIDescription)

        //Making the Metric view gone and the US view visible
        currentVisibleView = US_UNITS_VIEW // Current View is updated here.
        llMetricUnitsView.visibility = View.GONE // METRIC UNITS VIEW is hidden
        llUsUnitsView.visibility = View.VISIBLE // US UNITS VIEW is Visible

        //Clearing the values of the text boxes (We want the boxes to not have any values when we click on the other radio button)
        etUsUnitWeight.text!!.clear() // weight value is cleared.
        etUsUnitHeightFeet.text!!.clear() // height feet value is cleared.
        etUsUnitHeightInch.text!!.clear() // height inch is cleared.

        tvYourBMI.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
        tvBMIValue.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
        tvBMIType.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
        tvBMIDescription.visibility = View.INVISIBLE // Result is cleared and the labels are hidden
    }
}