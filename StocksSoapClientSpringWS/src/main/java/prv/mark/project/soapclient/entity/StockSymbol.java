package prv.mark.project.soapclient.entity;

/**
 * Entity for the STOCK_SYMBOL table.
 *
 * @author mlglenn on 11/28/2016.
 */
//@Entity
//@Table(name = "STOCK_SYMBOL")
public class StockSymbol {

    private Long id;
    private String tickerSymbol;


    public StockSymbol() {
    }

    public StockSymbol(Long id, String tickerSymbol) {
        this.id = id;
        this.tickerSymbol = tickerSymbol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockSymbol that = (StockSymbol) o;

        if (!getId().equals(that.getId())) return false;
        return getTickerSymbol().equals(that.getTickerSymbol());

    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getTickerSymbol().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "StockSymbol{" +
                "id=" + id +
                ", tickerSymbol='" + tickerSymbol + '\'' +
                '}';
    }
}
