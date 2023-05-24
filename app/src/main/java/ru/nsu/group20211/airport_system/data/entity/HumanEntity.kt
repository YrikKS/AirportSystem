package entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDate
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.or

object Humans : IntIdTable("human".addQuo(), columnName = "id".addQuo()) {
    val name = varchar("name".addQuo(), 255)
    val surname = varchar("surname".addQuo(), 255)
    val patronymic = varchar("patronymic".addQuo(), 255).nullable()
    val sex = char("sex".addQuo()).check { (it eq 'M') or (it eq 'W') }
    val dateOfBirth = date("dateOfBirth".addQuo()).defaultExpression(CurrentDate)
    val countChildren = integer("countChildren".addQuo()).default(0)
}

class HumanEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<HumanEntity>(Humans)

    var name by Humans.name
    var surname by Humans.surname
    var patronymic by Humans.patronymic
    var sex by Humans.sex
    var dateOfBirth by Humans.dateOfBirth
    var countChildren by Humans.countChildren
}