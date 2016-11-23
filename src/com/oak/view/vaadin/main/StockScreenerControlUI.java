package com.oak.view.vaadin.main;

import com.oak.api.MainController;
import com.oak.api.MainController.TickersData;
import com.oak.api.finance.model.dto.Control;
import com.oak.api.finance.model.dto.Screen0Result;
import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
@Theme("valo")
public class StockScreenerControlUI extends UI {
	
	private final MainController mainController;
	private final Grid controlGrid = new Grid();
	private final Grid screen0Grid = new Grid();
	private final Button processBtn = new Button("Start Processing");
	private final Label statusLbl = new Label();
	
	public StockScreenerControlUI(MainController mainController) {
		this.mainController = mainController;
		
	}

	@Override
	protected void init(VaadinRequest request) {
		processBtn.addClickListener( e -> { 
			Notification.show("Starting run");
			mainController.launchAnalysis(new AnalysisResultListenerImpl());
		});
		HorizontalLayout hz = new HorizontalLayout(processBtn,statusLbl);
		VerticalLayout main = new VerticalLayout(hz,screen0Grid, controlGrid);
//		hz.setSpacing(true);
		main.setMargin(true);
		main.setSpacing(true);
		
		
		controlGrid.setColumns( "timeStamp", "type", "timeStamp", "status", "comments");
		screen0Grid.setColumns("runDate", "ticker", "companyName", "currency",  "priceBid", "bookValuePerShare", "perCalculated");
		controlGrid.setWidth("100%");
		screen0Grid.setWidth("100%");
		controlGrid.addSelectionListener(e -> {
			if(e.getSelected().isEmpty()) {
				//
			}else {
				//
			}
		});
		
		controlGrid.setContainerDataSource(new BeanItemContainer<>(Control.class,mainController.getStatuses()));
		screen0Grid.setContainerDataSource(new BeanItemContainer<>(Screen0Result.class,mainController.getResults()));
		
		setContent(main);
		
		
	}
	class AnalysisResultListenerImpl implements MainController.AnalysisResultsListener{

		@Override
		public void onTickersLoaded(TickersData data) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnalysingTicker(String ticker) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFinished(Control control) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
