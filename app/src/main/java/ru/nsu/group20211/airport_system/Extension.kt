package ru.nsu.group20211.airport_system

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.nsu.group20211.airport_system.domain.models.DbEntity
import ru.nsu.group20211.airportsystem.databinding.DialogInflatorBinding
import ru.nsu.group20211.airportsystem.databinding.SideDialogParametrsInflatorBinding
import java.sql.Date
import java.sql.ResultSet
import java.sql.Statement
import java.text.SimpleDateFormat

inline fun <R> runCatchingNonCancellation(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}

fun MaterialAutoCompleteTextView.setItems(stringArray: Array<String>) =
    (this as? MaterialAutoCompleteTextView)?.setSimpleItems(
        stringArray
    )

fun Statement.getSingle(query: String): ResultSet {
    var result = executeQuery(query)
    if (!result.next()) throw IllegalStateException("invalid id")
    return resultSet
}

fun getTimeFrom(string: String): Date? {
    return try {
        val format = "yyyy-MM-dd"
        val formater = SimpleDateFormat(format)
        Date(formater.parse(string).time)
    } catch (ex: Exception) {
        null
    } ?: try {
        val format = "dd.MM.yyyy"
        val formater = SimpleDateFormat(format)
        Date(formater.parse(string).time)
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }
}


fun LinearLayoutCompat.addInsertTextField(
    paramsText: List<Pair<String, (String) -> Unit>>,
    context: Context
) {
    paramsText.forEach { data ->
        addView(
            DialogInflatorBinding.inflate(
                LayoutInflater.from(context),
            ).let {
                it.textInputLayout.isVisible = true
                it.textInputLayout.hint = data.first
                it.inputText.addTextChangedListener { text ->
                    data.second(text.toString() ?: "")
                }
                it.root
            }
        )
    }
}

fun LinearLayoutCompat.addInsertPickField(
    paramsPick: List<Triple<String, (DbEntity) -> Unit, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>>>,
    coroutineScope: CoroutineScope,
    context: Context
) {
    paramsPick.forEach { data ->
        addView(
            DialogInflatorBinding.inflate(
                LayoutInflater.from(context),
            ).let {
                it.layoutExposed.isVisible = true
                it.layoutExposed.hint = data.first
                coroutineScope.launch(Dispatchers.IO) {
                    val listItems = data.third.first()
                    withContext(Dispatchers.Main) {
                        it.textExposed.setItems(listItems.map { entity ->
                            data.third.second(entity)
                        }.toTypedArray())

                        it.textExposed.setOnItemClickListener { parent, view, position, id ->
                            data.second(listItems[id.toInt()])
                        }
                    }
                }
                it.root
            }
        )
    }
}

fun LinearLayoutCompat.addInsertButton(
    context: Context,
    clickListener: () -> Unit
) {
    addView(
        DialogInflatorBinding.inflate(
            LayoutInflater.from(context),
        ).let {
            it.bottomButton.isVisible = true
            it.bottomButton.text = "Insert"
            it.bottomButton.setOnClickListener {
                clickListener()
            }
            it.root
        }
    )
}


fun LinearLayoutCompat.addUpdateTextField(
    paramsText: List<Pair<Pair<String, String>, (String) -> Unit>>,
    context: Context
) {
    paramsText.forEach { data ->
        addView(
            DialogInflatorBinding.inflate(
                LayoutInflater.from(context),
            ).let {
                it.textInputLayout.isVisible = true
                it.textInputLayout.hint = data.first.first
                it.inputText.setText(data.first.second)
                it.inputText.addTextChangedListener { text ->
                    data.second(text.toString() ?: "")
                }
                it.root
            }
        )
    }
}

fun LinearLayoutCompat.addUpdatePickField(
    paramsPick: List<Triple<Pair<String, String>, (DbEntity) -> Unit, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>>>,
    coroutineScope: CoroutineScope,
    context: Context
) {
    paramsPick.forEach { data ->
        addView(
            DialogInflatorBinding.inflate(
                LayoutInflater.from(context),
            ).let {
                it.layoutExposed.isVisible = true
                it.layoutExposed.hint = data.first.first
                coroutineScope.launch(Dispatchers.IO) {
                    val dbEntities = data.third.first()
                    withContext(Dispatchers.Main) {
                        it.textExposed.setItems(dbEntities.map { entity ->
                            data.third.second(entity)
                        }
                            .toTypedArray())
                        it.textExposed.setOnItemClickListener { parent, view, position, id ->
                            data.second(dbEntities[id.toInt()])
                        }
                    }
                }
                it.textExposed.setText(data.first.second)
                it.root
            }
        )
    }
}

fun LinearLayoutCompat.addUpdateButton(
    context: Context,
    clickListener: () -> Unit
) {
    addView(
        DialogInflatorBinding.inflate(
            LayoutInflater.from(context),
        ).let {
            it.bottomButton.isVisible = true
            it.bottomButton.text = "Update"
            it.bottomButton.setOnClickListener {
                clickListener()
            }
            it.root
        }
    )
}

fun LinearLayoutCompat.addSlider(
    nameField: String,
    context: Context,
    coroutineScope: CoroutineScope,
    initValue: suspend () -> Pair<Float, Float>,
    getValue: (String) -> Unit,
    getValueSecond: (String) -> Unit
) {
    addView(
        SideDialogParametrsInflatorBinding.inflate(
            LayoutInflater.from(context)
        ).also {
            with(it) {
                coroutineScope.launch(Dispatchers.IO) {
                    val (init1, init2) = initValue()
                    withContext(Dispatchers.Main) {
                        seekBar.setValues(init1, init2)
                        seekBar.isVisible = true
                        seekBar.valueFrom = init1
                        seekBar.valueTo = init2
                        seekBar.stepSize = 1f
                    }
                }
                fieldName.text = nameField
                fieldName.isVisible = true
                seekBar.addOnChangeListener { rangeSlider, value, fromUser ->
                    if (fromUser) {
                        getValue(rangeSlider.values.first().toString())
                        getValueSecond(rangeSlider.values.last().toString())
                    }
                }
            }
        }.root
    )
}