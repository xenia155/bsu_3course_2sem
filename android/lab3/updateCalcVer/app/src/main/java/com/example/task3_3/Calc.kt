import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import java.util.*
import com.example.task3_3.R

class Calc : Fragment() {

    private lateinit var spinner1: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var etInput1: EditText
    private lateinit var etInput2: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calc, container, false)

        spinner1 = view.findViewById(R.id.spinner1)
        spinner2 = view.findViewById(R.id.spinner2)
        etInput1 = view.findViewById(R.id.editTextGenerated1)
        etInput2 = view.findViewById(R.id.editTextGenerated2)

        val generationMethods = resources.getStringArray(R.array.generation_methods)
        val adapter1 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, generationMethods)
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1.adapter = adapter1

        val operationAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.operation_array,
            android.R.layout.simple_spinner_item
        )
        operationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2.adapter = operationAdapter

        view.findViewById<Button>(R.id.buttonGenerate).setOnClickListener {
            generateRandomNumber()
        }

        view.findViewById<Button>(R.id.buttonCalculate).setOnClickListener {
            performOperation()
        }

        return view
    }

    private fun generateRandomNumber() {
        val min = etInput1.text.toString().toDoubleOrNull() ?: return
        val max = etInput2.text.toString().toDoubleOrNull() ?: return
        val random = Random()

        val randomNumber1 = when (spinner1.selectedItemPosition) {
            0 -> random.nextInt((max - min).toInt() + 1) + min.toInt()
            1 -> (random.nextDouble() * (max - min)) + min
            2 -> {
                var randomEven = random.nextInt(((max - min) / 2).toInt() + 1) * 2 + min.toInt()
                if (randomEven > max) randomEven -= 2
                randomEven
            }
            3 -> {
                var randomOdd = random.nextInt(((max - min) / 2).toInt() + 1) * 2 + 1 + min.toInt()
                if (randomOdd > max) randomOdd -= 2
                randomOdd
            }
            else -> return
        }

        etInput1.setText(randomNumber1.toString())

        val randomNumber2 = when (spinner1.selectedItemPosition) {
            0 -> random.nextInt((max - min).toInt() + 1) + min.toInt()
            1 -> (random.nextDouble() * (max - min)) + min
            2 -> {
                var randomEven = random.nextInt(((max - min) / 2).toInt() + 1) * 2 + min.toInt()
                if (randomEven > max) randomEven -= 2
                randomEven
            }
            3 -> {
                var randomOdd = random.nextInt(((max - min) / 2).toInt() + 1) * 2 + 1 + min.toInt()
                if (randomOdd > max) randomOdd -= 2
                randomOdd
            }
            else -> return
        }
        etInput2.setText(randomNumber2.toString())
    }

    private fun performOperation() {
        val num1 = etInput1.text.toString().toDoubleOrNull() ?: return
        val num2 = etInput2.text.toString().toDoubleOrNull() ?: return

        val operation = when (spinner2.selectedItemPosition) {
            0 -> num1 + num2
            1 -> num1 - num2
            2 -> num1 * num2
            3 -> {
                if (num2 != 0.0) num1 / num2 else {
                    Toast.makeText(requireContext(), getString(R.string.divide_by_zero), Toast.LENGTH_SHORT).show()
                    return
                }
            }
            else -> return
        }

        Toast.makeText(requireContext(), getString(R.string.result, operation), Toast.LENGTH_SHORT).show()
    }
}
