package report;

import calculating.CostCalculating;
import models.UserModel;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;


public class ReportCreater {
    private final String phoneNumber;
    private final List<UserModel> calls;
    public ReportCreater(String phoneNumber, List<UserModel> calls) {
        this.phoneNumber = phoneNumber;
        this.calls = calls;
    }
    // Создаём данные для выходного файла
    private StringBuilder outputGenerator() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tariff index: ").append(calls.get(0).getTariffType()).append("\n");
        sb.append("----------------------------------------------------------------------------\n");
        sb.append("Report for phone number ").append(phoneNumber).append(":\n");
        sb.append("----------------------------------------------------------------------------\n");
        sb.append("| Call Type |   Start Time        |     End Time        | Duration | Cost  |\n");
        sb.append("----------------------------------------------------------------------------\n");
        CostCalculating calc = new CostCalculating(calls.get(0).getTariffType());
        DecimalFormat dec = new DecimalFormat("#0.00");
        for(UserModel call : calls) {
            long dateDifference = getDateDifference(call.getStartCall(), call.getEndCall());
            Duration dur = Duration.ofSeconds(dateDifference);//   .ofMillis(dateDifference*1000);
            String duration = String.format("%02d:%02d:%02d", dur.toHours(),
                    dur.toMinutesPart(), dur.toSecondsPart());
            // Звонок вычисляется как количество минут(округляется в большую сторону) умножить на стоимость по тарифу
            double cost = calc.calculate(call.getCallType(), (long) Math.ceil((double)dateDifference/60));
            String pattern = "yyyy-MM-dd HH:mm:ss";
            DateFormat df = new SimpleDateFormat(pattern);
            String startCall = df.format(call.getStartCall());
            String endCall = df.format(call.getEndCall());

            sb.append("|     ").append(call.getCallType()).append("    | ");
            sb.append(startCall).append(" | ");
            sb.append(endCall).append(" | ");
            sb.append(duration).append(" |  ");
            sb.append(dec.format(cost)).append(" |\n");
        }

        sb.append("----------------------------------------------------------------------------\n");
        sb.append("|                                           Total Cost: |     " );
        sb.append(dec.format(calc.getTotalCost())).append(" rubles |\n");
        sb.append("----------------------------------------------------------------------------\n");

        return sb;
    }
    // Вычисляем количество секунд, которое длился звонок
    private long getDateDifference(Date startCall, Date endCall) {
        return (endCall.getTime() - startCall.getTime())/1000;
    }
    // Сохранение в файл текущего пользователя
    public void outputSave() throws FileNotFoundException {
        StringBuilder output = outputGenerator();
        String outputPath = "./reports/report_" + phoneNumber + ".txt";
        try (PrintWriter out = new PrintWriter(outputPath)) {
            out.println(output);
        }
    }
}
