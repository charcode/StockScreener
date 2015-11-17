package com.oak.api.finance.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

import com.oak.api.finance.model.economy.Economic;

public class FinancialAnalysis {
    public enum Acceptance{
	BUY,SELL,HOLD
    }

    private final Stock stock;
    private final Economic economic;
    private final List<FinancialComment>comments;
    private final Acceptance acceptance;
    
    public FinancialAnalysis(Stock stock, Economic economic,
	    List<FinancialComment> comments, Acceptance acceptance) {
	super();
	this.stock = stock;
	this.economic = economic;
	this.acceptance = acceptance;
	this.comments = comments == null? new ArrayList<FinancialComment>():comments;
    }
    public Stock getStock() {
        return stock;
    }
    public Economic getEconomic() {
        return economic;
    }
    public void addComment(FinancialComment comment){
	this.comments.add(comment);
    }
    public Collection<FinancialComment>comments(){
	return Collections.unmodifiableCollection(comments);
    }
    public Acceptance getAcceptance(){
	return acceptance;
    }
}
