package com.mwdevelop.android.timetable;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by mateusz on 26.02.18.
 */

public class DataDownloadTask extends AsyncTask<String,Void,Boolean>  {


    @Override
    protected Boolean doInBackground(String... strings)  {
        try {
              getDataTimeTableFromWebsite(strings[0]);
              return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean s) {
        super.onPostExecute(s);
    }

    private static void getDataTimeTableFromWebsite(String subjectName) throws Exception{
        Document doc = Jsoup.connect("https://wimii.pcz.pl/pl/plan-zajec").timeout(40000).validateTLSCertificates(false).get();
        Element element=doc.select("article[about=/pl/plan-zajec] p").select("a").first();
        String link=element.attr("abs:href");
        doc= (Document) Jsoup.connect(link).timeout(40000).validateTLSCertificates(false).get();
        element=doc.select("a:contains("+subjectName+")").first();
        link=element.attr("abs:href");
        System.out.println(link);

        DataLab dataLab=DataLab.Companion.get(DayPagerActivity.Companion.getContext());
        ArrayList<String> nameDay =dataLab.getMDaysName();
        ArrayList<TimeClass> times=dataLab.getMTimes();


        String a="";
        if(a.isEmpty()) System.out.printf("sdsd");
        ArrayList<Day> dayArrayList = new ArrayList<>();
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<String> array = new ArrayList<>();

        for (int i = 0; i < 5; ++i) {
            Day tmp_day = new Day();
            tmp_day.setName(nameDay.get(i));
            dayArrayList.add(tmp_day);

        }
        doc = Jsoup.connect(link).timeout(10000).validateTLSCertificates(false).get();
        System.out.println(doc.title());
        doc.select("br").append("#");
        Elements newsHeadlines = doc.select("tr");
        newsHeadlines = doc.select("td");
        for (Element headline : newsHeadlines) {

            arrayList.add(headline.text());
        }

        for (int i = 7; i < arrayList.size(); i = i + 6) {
            for (int j = i; j < i + 5; j++) {
                array.add(arrayList.get(j));
            }
        }
        for (int i = 0; i < 12; ++i) {
            for (int j = 0; j < 5; ++j) {
                String row = array.get(i * 5 + j);

                Subject tmp = new Subject();
                if (!row.isEmpty()) {
                    String[] splitText = row.split("#");
                    tmp.setName(splitText[0]);
                    tmp.setLecturer(splitText[1]);
                    tmp.setRoom(splitText[2]);
                    tmp.setBeginTime(times.get(i).getBeginTime());
                    tmp.setEndTime(times.get(i).getEndTime());
                }

                dayArrayList.get(j).getSubjectArrayList().set(i, tmp);
            }

        }
        DataLab.Companion.get(DayPagerActivity.Companion.getContext()).setDayArray(dayArrayList);


    }
}
