package com.dse.se.dao.cql;

import com.datastax.driver.core.*;
import com.dse.se.dao.ISketchDAO;
import com.dse.se.dto.Approximate;
import com.dse.se.dto.TopHashTagsDTO;
import com.dse.se.dto.UniqueUsersDTO;
import com.google.common.base.Stopwatch;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Component
public class SketchDAO implements ISketchDAO {

    //yyyy-mm-dd HH:mm:ssZ
    private static final SimpleDateFormat resultformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd-YYYY");



    public static void main (String[] args) throws ParseException{
        new SketchDAO().getUniqueUsersForToday();
    }

    @Override
    public UniqueUsersDTO getUniqueUsersForToday() throws ParseException {

        Session session = DataStaxSessionFactory.getInstance().getSession();

        String today = dateformat.format(new Date());

        PreparedStatement prepared = session.prepare(
                "select batchtime, uniqueperbatch from approximations.hlldata where id = ? and date = ?");


        BoundStatement bound = prepared.bind("tweets",  today);

        Stopwatch stopwatch = Stopwatch.createStarted();
        ResultSet results = session.execute(bound);
        stopwatch.stop();

        List<Approximate> approximateList = new ArrayList<>(results.getAvailableWithoutFetching());

        //TODO Use Object Mapper
        for(Row oneResult : results){

            Date time = oneResult.getTimestamp(0);
            Integer uniqueUsers = oneResult.getInt(1);

            Approximate oneApproximate = new Approximate();
            oneApproximate.setTime(resultformat.format(time));
            oneApproximate.setUniqueUsers(uniqueUsers);

            approximateList.add(oneApproximate);
        }

        UniqueUsersDTO uniqueUsersDTO = new UniqueUsersDTO();
        uniqueUsersDTO.setApproximates(approximateList.toArray(new Approximate[approximateList.size()]));
        uniqueUsersDTO.setResultTime(stopwatch.elapsed(TimeUnit.MILLISECONDS));

        return uniqueUsersDTO;
    }
    @Override
    public UniqueUsersDTO getUniqueUsersRollup() throws ParseException {

        Session session = DataStaxSessionFactory.getInstance().getSession();

        String today = dateformat.format(new Date());


        PreparedStatement prepared = session.prepare(
                "select batchtime, uniqueperbatch from approximations.hlldata10min where id = ? and date = ?");

        BoundStatement bound = prepared.bind("tweets",  today);

        Stopwatch stopwatch = Stopwatch.createStarted();
        ResultSet results = session.execute(bound);
        stopwatch.stop();

        List<Approximate> approximateList = new ArrayList<>(results.getAvailableWithoutFetching());

        //TODO Use Object Mapper
        for(Row oneResult : results){

            Date time = oneResult.getTimestamp(0);
            Integer uniqueUsers = oneResult.getInt(1);

            Approximate oneApproximate = new Approximate();
            oneApproximate.setTime(resultformat.format(time));
            oneApproximate.setUniqueUsers(uniqueUsers);

            approximateList.add(oneApproximate);
        }

        UniqueUsersDTO uniqueUsersDTO = new UniqueUsersDTO();
        uniqueUsersDTO.setApproximates(approximateList.toArray(new Approximate[approximateList.size()]));
        uniqueUsersDTO.setResultTime(stopwatch.elapsed(TimeUnit.MILLISECONDS));

        return uniqueUsersDTO;
    }
    @Override
    public TopHashTagsDTO getTopTweetsRollup() throws ParseException{

        Session session = DataStaxSessionFactory.getInstance().getSession();

        String today = dateformat.format(new Date());

        PreparedStatement prepared = session.prepare(
                "select batchtime, preview from approximations.cmsdata10min where id = ? and date = ? limit 1");

        BoundStatement bound = prepared.bind("tweets", today);

        Stopwatch stopwatch = Stopwatch.createStarted();
        ResultSet results = session.execute(bound);
        stopwatch.stop();


        Integer count  = results.getAvailableWithoutFetching();

        TopHashTagsDTO topHashTagsDTO = new TopHashTagsDTO();
        topHashTagsDTO.setResultTime(stopwatch.elapsed(TimeUnit.MILLISECONDS));

        if(count > 0){
            Row oneResult = results.one();

            Date time = oneResult.getTimestamp(0);

            Map<String, Long> map = oneResult.getMap(1, String.class, Long.class);

            topHashTagsDTO.setTopTags(map);
        }


        return topHashTagsDTO;
    }
}