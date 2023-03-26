import models.UserModel;
import parser.CDRparser;
import report.ReportCreater;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        CDRparser data = new CDRparser("./data/cdr.txt");
        Map<String, List<UserModel>> users = data.parseUsersInfo();
        for ( String key : users.keySet() ) {
            List<UserModel> arr = users.get(key);
            arr.sort(Comparator.comparing(UserModel::getStartCall));
            ReportCreater temp = new ReportCreater(key, arr);
            temp.outputSave();
        }


    }
}
