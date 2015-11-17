package com.oak.api.finance.model;

public class Stock {
	private final String name;
	private final String symbol;
	private final String id;
	private final String description;
	private final String stockExchange;
	private final String currency;

	public Stock(String name, String symbol, String id, String description, String stockExchange, String currency) {
		super();
		this.name = name;
		this.symbol = symbol;
		this.id = id;
		this.description = description;
		this.stockExchange = stockExchange;
		this.currency = currency;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getCode() {
		return symbol;
	}

	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
	    return name;
	}

	public String getStockExchange() {
	    return stockExchange;
	}

	public String getCurrency() {
	    return currency;
	}

	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result
		    + ((currency == null) ? 0 : currency.hashCode());
	    result = prime * result
		    + ((description == null) ? 0 : description.hashCode());
	    result = prime * result + ((id == null) ? 0 : id.hashCode());
	    result = prime * result + ((name == null) ? 0 : name.hashCode());
	    result = prime * result
		    + ((stockExchange == null) ? 0 : stockExchange.hashCode());
	    result = prime * result
		    + ((symbol == null) ? 0 : symbol.hashCode());
	    return result;
	}
	@Override
	public String toString() {
		return "Stock [name=" + name + ", code=" + symbol + ", id=" + id + "]";
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
		return true;
	    if (obj == null)
		return false;
	    if (getClass() != obj.getClass())
		return false;
	    Stock other = (Stock) obj;
	    if (currency == null) {
		if (other.currency != null)
		    return false;
	    } else if (!currency.equals(other.currency))
		return false;
	    if (description == null) {
		if (other.description != null)
		    return false;
	    } else if (!description.equals(other.description))
		return false;
	    if (id == null) {
		if (other.id != null)
		    return false;
	    } else if (!id.equals(other.id))
		return false;
	    if (name == null) {
		if (other.name != null)
		    return false;
	    } else if (!name.equals(other.name))
		return false;
	    if (stockExchange == null) {
		if (other.stockExchange != null)
		    return false;
	    } else if (!stockExchange.equals(other.stockExchange))
		return false;
	    if (symbol == null) {
		if (other.symbol != null)
		    return false;
	    } else if (!symbol.equals(other.symbol))
		return false;
	    return true;
	}

}
