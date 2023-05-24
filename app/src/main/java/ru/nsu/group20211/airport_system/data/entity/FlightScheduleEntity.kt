package entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object FlightSchedule : IntIdTable(name = "flightSchedule".addQuo(), columnName = "id".addQuo()) {
    val plane = reference("plane".addQuo(), Planes)
    val typeFlight = reference("typeFlight".addQuo(), TypesFlights)
    val status = reference("status".addQuo(), FlightStatus)
    val brigadePilots = reference("brigadePilots".addQuo(), Brigades)
    val brigadeWorker = reference("brigadeWorker".addQuo(), Brigades)
    val idApproximateFlights = reference("idApproximateFlights".addQuo(), ApproximateFlights).nullable()

    val idDepartureAirport = reference("idDepartureAirport".addQuo(), Airports.id).nullable()
    val idArrivalAirport = reference("idArrivalAirport".addQuo(), Airports.id).nullable()
    val takeoffTime = datetime("takeoffTime".addQuo()).nullable()
    val boardingTime = datetime("boardingTime".addQuo()).nullable()
    val price = float("price".addQuo())
    val minNumberTickets = integer("minNumberTickets".addQuo())
}

class FlightScheduleEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FlightScheduleEntity>(FlightSchedule)

    var plane by PlaneEntity referencedOn FlightSchedule.plane
    var typeFlight by TypesFlightEntity referencedOn FlightSchedule.typeFlight
    var status by FlightStatusEntity referencedOn FlightSchedule.status
    var brigadePilots by BrigadeEntity referencedOn FlightSchedule.brigadePilots
    var brigadeWorker by BrigadeEntity referencedOn FlightSchedule.brigadeWorker
    var idApproximateFlights by ApproximateFlightsEntity optionalReferencedOn FlightSchedule.idApproximateFlights
    var idDepartureAiroport by AirportsEntity optionalReferencedOn FlightSchedule.idDepartureAirport
    var idArrivalAirport by AirportsEntity optionalReferencedOn FlightSchedule.idArrivalAirport
    var takeoffTime by FlightSchedule.takeoffTime
    var boardingTime by FlightSchedule.boardingTime
    var price by FlightSchedule.price
    var minNumberTickets by FlightSchedule.minNumberTickets
}