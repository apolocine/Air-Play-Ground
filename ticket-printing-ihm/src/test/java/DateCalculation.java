import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateCalculation {

    public static void main(String[] args) {
        // Obtenez la date courante
        LocalDate currentDate = LocalDate.now();
        System.out.println("Date courante : " + currentDate);

        // Calculez une date qui est 8 mois plus tard
        LocalDate eightMonthsLater = currentDate.plus(8, ChronoUnit.MONTHS);
        System.out.println("8 mois plus tard : " + eightMonthsLater);
    }
}
