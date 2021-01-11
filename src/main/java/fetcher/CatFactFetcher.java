package fetcher;

import com.google.gson.Gson;
import dto.CatFactDTO;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import utils.HttpUtils;

public class CatFactFetcher {

    private static String URL = "https://cat-fact.herokuapp.com/facts";

    public static String fetchCatFactsParallel(Gson GSON, ExecutorService ES) throws InterruptedException, ExecutionException, TimeoutException {

        

        Callable<CatFactDTO[]> task = new Callable<CatFactDTO[]>() {
            @Override
            public CatFactDTO[] call() throws Exception {
                String fetchedData = HttpUtils.fetchData(URL);
                CatFactDTO[] catFactDTOArray = GSON.fromJson(fetchedData, CatFactDTO[].class);
                return catFactDTOArray;
            }
        };
        Future<CatFactDTO[]> future = ES.submit(task);
        
        CatFactDTO[] result = future.get(2, TimeUnit.SECONDS);
        
        return GSON.toJson(result);
        
        
    }

}
