package prv.mark.project.soapclient;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import prv.mark.project.soapclient.entity.StockSymbol;
import prv.mark.project.soapclient.schemas.GetStockPriceRequest;
import prv.mark.project.soapclient.schemas.GetStockPriceResponse;
import prv.mark.project.soapclient.schemas.ObjectFactory;
import prv.mark.project.soapclient.schemas.RequestHeader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Stock Ticker client using Spring Web Services framework for SOAP clients.
 *
 * @author mlglenn on 11/28/2016.
 */
@SpringBootApplication
public class StocksSpringBootApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(StocksSpringBootApplication.class);
    private static final String LOG_FILE = "StocksSpringBootApplication.log";
    private static int errorCount;
    private static int successCount;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private WebServiceTemplate webServiceTemplate;
    @Autowired
    private Jaxb2Marshaller marshaller;
    @Autowired
    private MessageSource messageSource;

    @Value("${app.stocks.stockListQuery}")
    private String stockListQuery;

    @Value("${app.stocks.stockquote.soap.url}")
    private String stockquoteSoapUrl;

    @Value("${app.stocks.client.name}")
    private String stocksClientName;



    /**
     * Spring Boot application entry point.
     *
     * @param args Array of command line arguments
     */
    public static void main(String[] args) {

        LOGGER.info("Starting StocksSpringBootApplication ...");
        SpringApplication.run(StocksSpringBootApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        /*
        Steps:
            1. Open log file
            2. Query database
            3. Submit requests to web service
            4. Record log output
         */

        BufferedWriter logFileWriter = new BufferedWriter(new PrintWriter(new File(LOG_FILE)));
        List<StockSymbol> stockSymbolList = getStocks();
        webServiceTemplate.setDefaultUri(stockquoteSoapUrl);

        Instant start = Instant.now();
        String message = "*** {}: Processing stock quotes ... ***" + start;
        LOGGER.info(message);
        writeToLog(message, logFileWriter, StringUtils.EMPTY);

        stockSymbolList.parallelStream()
                .forEach(symbol -> {
                    try {
                        GetStockPriceResponse response = (GetStockPriceResponse)
                                webServiceTemplate.marshalSendAndReceive(buildStockPriceRequest(symbol));

                        if (response == null) {
                            ++errorCount;
                            LOGGER.info("Response is null for stock symbol {}", symbol);
                            writeToLog(symbol.toString(), logFileWriter, "Empty Response from Web Service");
                        } else {
                            ++successCount;
                            LOGGER.info("Received valid response for stock symbol {}", symbol);
                            writeToLog(symbol.toString(), logFileWriter, StringUtils.EMPTY);
                        }
                    } catch (RuntimeException e) {
                        ++errorCount;
                        LOGGER.info("");
                        LOGGER.info("!!! Error processing {}: {}", symbol.toString(), e.getMessage());
                        LOGGER.info("");
                        writeToLog(symbol.toString(), logFileWriter, e.getMessage());
                    }
                });

        Instant end = Instant.now();
        message = "*** {}: END processing stock quotes ... ***" + end;
        LOGGER.info(message);
        writeToLog(message, logFileWriter, StringUtils.EMPTY);

        message = "{} records processed in {} MILLISECONDS" + stockSymbolList.size() + Duration.between(start, end).toMillis();
        LOGGER.info(message);
        writeToLog(message, logFileWriter, StringUtils.EMPTY);

        message = "Number of SUCCESSFUL requests to backend service : {}" + successCount;
        LOGGER.info(message);
        writeToLog(message, logFileWriter, StringUtils.EMPTY);

        message = "Number of FAILED requests to backend service     : {}" + errorCount;
        LOGGER.info(message);
        writeToLog(message, logFileWriter, StringUtils.EMPTY);

        logFileWriter.close();
    }



    private void writeToLog(String lineToWrite, BufferedWriter writer, String errorMsg) {
        try {
            writer.write(lineToWrite);
            if (StringUtils.isNotEmpty(errorMsg)) {
                writer.write("!!! ERROR !!! : " + errorMsg);
            }
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            LOGGER.error(messageSource.getMessage(
                    "appmsg.error.writelog", new Object[]{e.getMessage()}, Locale.getDefault()));
        }
    }

    private GetStockPriceRequest buildStockPriceRequest(StockSymbol stockSymbol) {
        ObjectFactory objectFactory = new ObjectFactory();
        RequestHeader requestHeader = objectFactory.createRequestHeader();
        requestHeader.setSource(stocksClientName);
        GetStockPriceRequest request = objectFactory.createGetStockPriceRequest();
        request.setHead(requestHeader);
        request.setTickerSymbol(stockSymbol.getTickerSymbol());
        return request;
    }

    private List<StockSymbol> getStocks() {
        Instant start = Instant.now();
        LOGGER.info("*** {}: Retrieving stock symbols ***", start);

        List<StockSymbol> stockSymbolList = jdbcTemplate.query(stockListQuery, new Object[]{},
                (rs, i) -> {
                    StockSymbol stockSymbol = new StockSymbol();
                    stockSymbol.setId(rs.getLong("id"));
                    stockSymbol.setTickerSymbol(rs.getString("ticker_symbol"));
                    return stockSymbol;
                });
        List<StockSymbol> distinctList = stockSymbolList.stream().distinct().collect(Collectors.toList());

        Instant end = Instant.now();
        LOGGER.info("*** {}: Retrieved {} stock symbols in {} ms ***", end,
                distinctList.size(), Duration.between(start, end).toMillis());

        return stockSymbolList;
    }
}
