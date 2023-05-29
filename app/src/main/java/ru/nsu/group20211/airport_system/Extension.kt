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
import ru.nsu.group20211.airport_system.domain.DbEntity
import ru.nsu.group20211.airport_system.presentation.DbFilter
import ru.nsu.group20211.airportsystem.R
import ru.nsu.group20211.airportsystem.databinding.DialogInflatorBinding
import ru.nsu.group20211.airportsystem.databinding.SideDialogParametrsInflatorBinding
import java.sql.Date
import java.sql.ResultSet
import java.sql.Statement
import java.sql.Timestamp
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

fun getTimestampFrom(string: String): Timestamp? {
    return try {
        Timestamp.valueOf(string)
    } catch (ex: Exception) {
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

fun LinearLayoutCompat.addInsertPickFieldFromList(
    paramsPick: List<Triple<String, (String) -> Unit, List<String>>>,
    context: Context
) {
    paramsPick.forEach { data ->
        addView(
            DialogInflatorBinding.inflate(
                LayoutInflater.from(context),
            ).let {
                it.layoutExposed.isVisible = true
                it.layoutExposed.hint = data.first
                val listItems = data.third
//                (it.textExposed as? MaterialAutoCompleteTextView)?.setAdapter(
//                    ArrayAdapter(
//                        context,
//                        R.layout.list_item_exposed_text,
//                        listItems.toTypedArray()
//                    )
//                )
                it.textExposed.setItems(listItems.toTypedArray())
                it.textExposed.setOnItemClickListener { parent, view, position, id ->
                    data.second(listItems[id.toInt()])
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

fun LinearLayoutCompat.addUpdatePickFieldFromList(
    paramsPick: List<Triple<Pair<String, String>, (String) -> Unit, List<String>>>,
    context: Context
) {
    paramsPick.forEach { data ->
        addView(
            DialogInflatorBinding.inflate(
                LayoutInflater.from(context),
            ).let {
                it.layoutExposed.isVisible = true
                it.layoutExposed.hint = data.first.first
                it.textExposed.setText(data.first.second)
                val listItems = data.third
                it.textExposed.setItems(listItems.toTypedArray())
                it.textExposed.setOnItemClickListener { parent, view, position, id ->
                    data.second(listItems[id.toInt()])
                }
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


fun LinearLayoutCompat.addFilterPick(
    context: Context,
    fieldName: List<Pair<String, String>>,
    filter: DbFilter
) {
    addView(
        DialogInflatorBinding.inflate(
            LayoutInflater.from(context)
        ).also {
            with(it) {
                this.layoutExposed.isVisible = true
                this.textExposed.isVisible = true
                this.layoutExposed.hint = "Sort by"
                this.textExposed.setItems(fieldName.map { item -> item.first }.toTypedArray())
                this.textExposed.setOnItemClickListener { parent, view, position, id ->
                    filter.nameFieldSort = fieldName[id.toInt()].second
                }
            }
        }.root
    )
}

fun LinearLayoutCompat.addSlider(
    context: Context,
    listParams: List<Triple<String, suspend () -> Pair<Float, Float>, (Float, Float) -> Unit>>,
    coroutineScope: CoroutineScope
) {
    listParams.forEach { data ->
        addView(
            SideDialogParametrsInflatorBinding.inflate(
                LayoutInflater.from(context)
            ).also {
                with(it) {
                    coroutineScope.launch(Dispatchers.IO) {
                        val (min, max) = data.second()
                        withContext(Dispatchers.Main) {
                            seekBar.setValues(min, max)
                            seekBar.isVisible = true
                            fieldName.isVisible = true
                            seekBar.valueFrom = min
                            seekBar.valueTo = max
                            seekBar.stepSize = 1f
                        }
                    }
                    fieldName.text = data.first
                    seekBar.addOnChangeListener { rangeSlider, value, fromUser ->
                        if (fromUser) {
                            data.third(rangeSlider.values.first(), rangeSlider.values.last())
                        }
                    }
                }
            }.root
        )
    }
}

fun LinearLayoutCompat.addPickParamInternet(
    context: Context,
    listParams: List<Triple<String, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>, (DbEntity?) -> Unit>>,
    coroutineScope: CoroutineScope
) {
    listParams.forEach { data ->
        addView(
            SideDialogParametrsInflatorBinding.inflate(
                LayoutInflater.from(context)
            ).also {
                with(it) {
                    fieldName.text = data.first
                    textExposed.isVisible = true
                    layoutExposed.isVisible = true

                    layoutExposed.hint = data.first
                    textExposed.setText("None")

                    coroutineScope.launch(Dispatchers.IO) {
                        val listItems = data.second.first()
                        withContext(Dispatchers.Main) {
                            textExposed.setItems(listItems.map { item ->
                                data.second.second(
                                    item
                                )
                            }.toMutableList().also {
                                it.add(0, "None")
                            }.toTypedArray())
                        }
                        textExposed.setOnItemClickListener { parent, view, position, id ->
                            val item = listItems.getOrNull(id.toInt() - 1)
                            data.third(item)
                        }
                    }

                }
            }.root
        )
    }
}

fun LinearLayoutCompat.addPickParamNoInternet(
    context: Context,
    listParams: List<Triple<String, List<String>, (String?) -> Unit>>,
) {
    listParams.forEach { data ->
        addView(
            SideDialogParametrsInflatorBinding.inflate(
                LayoutInflater.from(context)
            ).also {
                with(it) {
                    fieldName.text = data.first
                    this.layoutExposed.isVisible = true
                    this.textExposed.isVisible = true
                    this.layoutExposed.hint = data.first
                    this.textExposed.setText("None")
                    val listItems = data.second
                    textExposed.setItems(listItems.toMutableList().also {
                        it.add(0, "None")
                    }.toTypedArray())
                    textExposed.setOnItemClickListener { parent, view, position, id ->
                        val item = listItems.getOrNull(id.toInt() - 1)
                        data.third(item)
                    }
                }

            }.root
        )
    }
}