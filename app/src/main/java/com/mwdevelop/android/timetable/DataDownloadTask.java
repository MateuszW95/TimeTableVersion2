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

    private static void getDataTimeTableFromWebsite(String URL) throws Exception{


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
        Document doc = Jsoup.connect(URL).timeout(40000).validateTLSCertificates(false).get();
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
                        if(splitText.length==3) {
                            tmp.setName(splitText[0]);
                            tmp.setLecturer(splitText[1]);
                            tmp.setRoom(splitText[2]);
                        }
                        else if(splitText.length>3)
                        {
                            tmp.setName(splitText[0]);
                            StringBuilder tmpp=new StringBuilder();
                            for(int k=1;k<splitText.length-1;++k){
                                tmpp.append(splitText[k]+" ");
                            }
                            tmp.setLecturer(tmpp.toString());
                            if(splitText[splitText.length-1].contains("s."))
                            tmp.setRoom(splitText[splitText.length-1]);
                            else {
                                tmp.setRoom("");
                                tmp.setLecturer(tmp.getLecturer()+" "+splitText[splitText.length-1]);
                            }
                        }
                        else {
                            tmp.setName(splitText[0]);
                            tmp.setLecturer(splitText[splitText.length-1]);
                        }


                    tmp.setBeginTime(times.get(i).getBeginTime());
                    tmp.setEndTime(times.get(i).getEndTime());
                }

                dayArrayList.get(j).getSubjectArrayList().set(i, tmp);
            }

        }

        DataLab.Companion.get(DayPagerActivity.Companion.getContext()).setDayArray(dayArrayList);


    }
}
