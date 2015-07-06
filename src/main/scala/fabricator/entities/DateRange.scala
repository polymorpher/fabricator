package fabricator.entities

import fabricator.Alphanumeric
import fabricator.enums.{DateFormat, DateRangeType}
import org.joda.time.DateTime

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

class DateRange {

  private var startYear: Int = DateTime.now().getYear

  private var startMonth: Int = DateTime.now().getMonthOfYear

  private var startDay: Int = DateTime.now().getDayOfMonth

  private var startDate: DateTime = DateTime.now()

  private var useStartDate: Boolean = false

  private var endYear: Int = DateTime.now().plusYears(1).getYear

  private var endMonth: Int = DateTime.now().plusYears(1).getMonthOfYear

  private var endDay: Int = DateTime.now().plusYears(1).getDayOfMonth

  private var endDate: DateTime = DateTime.now().plusYears(1)

  private var useEndDate: Boolean = false

  private var stepType = DateRangeType.DAYS

  private var stepValue: Int = 1

  private var format: DateFormat = DateFormat.dd_MM_yyyy

  private val alpha: Alphanumeric = Alphanumeric()

  def startYear(year: Int) : this.type = {
    this.startYear = year
    this
  }

  def startMonth(month: Int) : this.type = {
    this.startMonth = month
    this
  }

  def startDay(day: Int): this.type = {
    this.startDay = day
    this
  }

  def startDate(date: DateTime): this.type = {
    this.startDate = date
    this.useStartDate = true
    this
  }


  def endYear(year: Int): this.type = {
    this.endYear = year
    this
  }

  def endMonth(month: Int): this.type = {
    this.endMonth = month
    this
  }

  def endDay(day: Int): this.type = {
    this.endDay = day
    this
  }

  def endDate(date: DateTime): this.type = {
    this.endDate = date
    this.useEndDate = true
    this
  }

  def stepEvery(stepValue: Int, stepType: DateRangeType): this.type = {
    this.stepValue = stepValue
    this.stepType = stepType
    this
  }

  def format(format: DateFormat): this.type = {
    this.format = format
    this
  }



  private def getRangeList: ListBuffer[DateTime] = {
    if (this.stepValue <= 0) throw new IllegalArgumentException("Step should be > 0")
    var startDate = if (useStartDate) this.startDate else new DateTime(startYear, startMonth, startDay, 0, 0)
    val endDate = if(useEndDate) this.endDate else new DateTime(endYear, endMonth, endDay, 0, 0)
    var rangeList = ListBuffer[DateTime]()
    rangeList += startDate
    while(startDate.compareTo(endDate) == -1) {
      stepType match {
        case DateRangeType.YEARS => startDate = startDate.plusYears(stepValue); if (startDate.compareTo(endDate) == -1) rangeList += startDate
        case DateRangeType.MONTHS => startDate = startDate.plusMonths(stepValue);  if (startDate.compareTo(endDate) == -1) rangeList += startDate
        case DateRangeType.DAYS => startDate = startDate.plusDays(stepValue);   if (startDate.compareTo(endDate) == -1) rangeList += startDate
        case DateRangeType.WEEKS => startDate = startDate.plusWeeks(stepValue);   if (startDate.compareTo(endDate) == -1) rangeList += startDate
        case DateRangeType.HOURS => startDate = startDate.plusHours(stepValue);   if (startDate.compareTo(endDate) == -1) rangeList += startDate
        case DateRangeType.MINUTES => startDate = startDate.plusMinutes(stepValue);   if (startDate.compareTo(endDate) == -1) rangeList += startDate
        case DateRangeType.SECONDS => startDate = startDate.plusSeconds(stepValue);   if (startDate.compareTo(endDate) == -1) rangeList += startDate
      }
    }
    rangeList
  }

  private def stringifyRangeList: ListBuffer[String] = getRangeList.map(_.toString(format.getFormat))

  def asStringsList: List[String] = stringifyRangeList.toList

  def asList: List[DateTime] = getRangeList.toList

  def asStringsJavaList() = stringifyRangeList.asJava

  def asJavaList() = getRangeList.asJava

  def asStringsArray: Array[String] = stringifyRangeList.toArray

  def asArray: Array[DateTime] =  getRangeList.toArray

  def getRandomDate: DateTime = {
    val datesRange = asList
    datesRange(alpha.getInteger(1, datesRange.length - 2))
  }

  def getRandomDateString: String = {
    val datesRange = asStringsList
    datesRange(alpha.getInteger(1, datesRange.length - 2))
  }

}
