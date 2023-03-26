package parser;

import models.UserModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class CDRparser {
    private final String path;
    public CDRparser(String path) {
        this.path = path;
    }

    public Map<String, List<UserModel>> parseUsersInfo() throws IOException {
        Map<String, List<UserModel>> users = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();

            while (line != null) {
                String phoneNumber = null;
                String callType = null;
                Date startCall = null;
                Date endCall = null;
                String tariffType = null;
                String[] parts = line.split(", ");
                for (String part : parts) {
                    switch (part) { // так как cdr файл может быть разным, парсим значения для любой ситуации
                        case "01":
                        case "02":
                            callType = part;
                            break;
                        case "03":
                        case "06":
                        case "11":
                            tariffType = part;
                            break;
                        default:
                            if (part.charAt(0) == '7') {
                                phoneNumber = part;
                                break;
                            }
                            Date tempDate = stringToDate(part);
                            if (startCall == null) {
                                startCall = tempDate;
                            } else if (startCall.compareTo(tempDate) > 0){
                                endCall = startCall;
                                startCall = tempDate;
                            } else {
                                endCall = tempDate;
                            }
                            break;
                    }
                }
                assert phoneNumber != null;
                assert callType != null;
                assert startCall != null;
                assert endCall != null;
                assert tariffType != null;
                // Добавляем вычисленные значения в общий map
                List<UserModel> itemsList = users.get(phoneNumber);
                if(itemsList == null) {
                    itemsList = new ArrayList<>();
                    itemsList.add(new UserModel(
                            callType,
                            startCall,
                            endCall,
                            tariffType
                    ));
                    users.put(phoneNumber, itemsList);
                } else {
                    itemsList.add(new UserModel(
                            callType,
                            startCall,
                            endCall,
                            tariffType
                    ));
                }
                line = br.readLine();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return users;
    }
    // Конвертируем строку в дату по указанному формату
    private Date stringToDate(String stringDate) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.parse(stringDate);
    }
}
