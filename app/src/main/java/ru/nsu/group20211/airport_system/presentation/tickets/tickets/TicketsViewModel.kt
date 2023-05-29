package ru.nsu.group20211.airport_system.presentation.tickets.tickets

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.nsu.group20211.airport_system.data.flight.FlightScheduleRepository
import ru.nsu.group20211.airport_system.data.passengers.PassengerRepository
import ru.nsu.group20211.airport_system.data.passengers.TicketRepository
import ru.nsu.group20211.airport_system.domain.flights.models.FlightSchedule
import ru.nsu.group20211.airport_system.domain.passengers.models.Passenger
import ru.nsu.group20211.airport_system.domain.passengers.models.Ticket
import ru.nsu.group20211.airport_system.presentation.BaseDbViewModel
import ru.nsu.group20211.airport_system.runCatchingNonCancellation
import java.sql.Timestamp
import javax.inject.Inject

class TicketsViewModel @Inject constructor(
    override val repository: TicketRepository,
    private val scheduleRepository: FlightScheduleRepository,
    private val passengerRepository: PassengerRepository
) : BaseDbViewModel<Ticket>() {
    override val stateProvider = MutableStateFlow<List<Ticket>>(emptyList())
    override val errorProvider = MutableSharedFlow<Throwable>()


    suspend fun getSchedule(): List<FlightSchedule> {
        return runCatchingNonCancellation {
            scheduleRepository.getAll()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            emptyList()
        }
    }


    suspend fun getMinPrice(): Float {
        return runCatchingNonCancellation {
            repository.getMinPrice()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            0F
        }
    }

    suspend fun getMaxPrice(): Float {
        return runCatchingNonCancellation {
            repository.getMaxPrice()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            Float.MAX_VALUE
        }
    }


    suspend fun getMinTakeOffTime(): Timestamp {
        return runCatchingNonCancellation {
            repository.getMinTakeOffTIme()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            Timestamp.valueOf("1999-01-01 00:00:00")
        }
    }

    suspend fun getMaxTakeOffTime(): Timestamp {
        return runCatchingNonCancellation {
            repository.getMaxTakeOffTIme()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            Timestamp.valueOf("2023-01-01 00:00:00")
        }
    }

    suspend fun getPassenger(): List<Passenger> {
        return runCatchingNonCancellation {
            passengerRepository.getAll()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            emptyList()
        }
    }

    suspend fun getEmptyPlace(flightId: Int): List<Int> {
        return runCatchingNonCancellation {
            val result = repository.getAll(listOf(""" "idFlight" = $flightId """))
            if(result.isEmpty()) return (0..scheduleRepository.getAll(listOf(""" "flightSchedule"."id" = $flightId """)).first()!!.planeEntity!!.numberPassengerSeats).toList()
            val all = (1..result.first().schedule!!.planeEntity!!.numberPassengerSeats)
            all.filterNot {
                result.find { ticket -> ticket.place == it } != null
            }
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            emptyList()
        }
    }
}