package com.oak.view.vaadin.main;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.gwt.thirdparty.guava.common.collect.Sets;
import com.oak.api.MainController;
import com.oak.api.MainController.TickersData;
import com.oak.api.finance.model.dto.Control;
import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringUI
@Theme("valo")
public class StockScreenerControlUI extends UI {
	private static final long serialVersionUID = 1L;
	
	private final MainController mainController;
	private final Grid controlGrid = new Grid();
	private final Grid screen0Grid = new Grid();
	private final Button processBtn = new Button("Start Processing");
	private final Button loadHistoricalQuotesBtn = new Button("Historical Quote Refresh");
	private final Button mainBtn = new Button("Main");
	private final Button filtersBtn = new Button("Filters");
	private Label topLbl = new Label();
	private final Label statusLbl = new Label();
	private final ScreenResultsAdapter adapter = new ScreenResultsAdapter();

	private VerticalLayout main;
	private HorizontalSplitPanel middle;
	private HorizontalLayout bottom;

	
	public StockScreenerControlUI(MainController mainController) {
		this.mainController = mainController;
		
	}

	@Override
	protected void init(VaadinRequest request) {
		processBtn.addClickListener( e -> { 
			Notification.show("Starting run");
			mainController.launchAnalysis(new AnalysisResultListenerImpl());
		});
		loadHistoricalQuotesBtn.addClickListener(e -> {
			Notification.show("Collecting historical Quotes");
			mainController.loadHistoricalQuotes();
		});
//		initContent0();
//		initContent1();
		initContent2();
		
		initScreen0Grid();
		initControlGrid();
		main.addComponents(screen0Grid,controlGrid);
		main.setWidth("100%");
	}

	private void initContent2() {
		VerticalLayout content = new VerticalLayout();
		content.setWidth("100%");
		setContent(content);
		Label envLbl = createEnvLabel();
		HorizontalLayout topMenu = new HorizontalLayout();
		middle = new HorizontalSplitPanel();
		bottom = new HorizontalLayout();
		VerticalLayout leftMenu = new VerticalLayout();
		main = new VerticalLayout();
		leftMenu.addComponents(mainBtn,filtersBtn);
		int buttonSize = 100;
		mainBtn.setWidth(buttonSize,Unit.PIXELS);
		filtersBtn.setWidth(buttonSize,Unit.PIXELS);
		mainBtn.setEnabled(false);
		filtersBtn.setEnabled(false);
		filtersBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
		leftMenu.setVisible(false);
		
		topMenu.addComponents(envLbl,processBtn,loadHistoricalQuotesBtn);
		leftMenu.setWidth(buttonSize,Unit.PIXELS);
		
		middle.setFirstComponent(leftMenu);
		middle.setSecondComponent(main);
		middle.setSplitPosition(buttonSize, Sizeable.Unit.PIXELS);
		content.addComponents(topMenu,middle,bottom);
		
	}

	private void initContent1() {
		
		VerticalLayout content = new VerticalLayout();
		setContent(content);

		Label envLbl = createEnvLabel();
		
		HorizontalLayout top = new HorizontalLayout();
		top.setSpacing(true);
		top.setMargin(new MarginInfo(false, true, false, true));
		top.addComponent(envLbl);
		top.addComponent(processBtn);
		top.addStyleName("backColor");
		
		
		content.setSizeFull();
		content.setSpacing(true);
		content.setMargin(true);
		content.addComponent(top);
		content.setExpandRatio(top,1);
		content.addComponent(screen0Grid);
//		content.setExpandRatio(screen0Grid, 10);
//		content.addComponent(controlGrid);
//		content.setExpandRatio(controlGrid, 10);
	}

	private Label createEnvLabel() {
		String profile="unknown";
		if(isProfile("dev")) {
			profile = "dev";
		}else if(isProfile("prod")) {
			profile = "prod";
		}
		Label envLbl = new Label(profile);
		return envLbl;
	}
	private boolean isProfile(String profile) {
		boolean ret = false;
		Optional<String[]>ps = mainController.getActiveProfiles();
		String[] activeProfiles;
		if(ps.isPresent()) {
			activeProfiles = ps.get();
		}else {
			activeProfiles = new String[0];
		}
		if(activeProfiles.length>0) {
			List<String> ap = Arrays.asList(activeProfiles);
			ret = ap.contains(profile);
		}
		return ret;
	}

	private void initContent0() {
		HorizontalLayout top = new HorizontalLayout(topLbl);
		HorizontalLayout hz = new HorizontalLayout(processBtn,statusLbl);
		VerticalLayout main = new VerticalLayout(top,hz,screen0Grid, controlGrid);
//		hz.setSpacing(true);
		main.setMargin(true);
		main.setSpacing(true);
		Optional<String[]>ps = mainController.getActiveProfiles();
		String[] activeProfiles;
		if(ps.isPresent()) {
			activeProfiles = ps.get();
		}else {
			activeProfiles = new String[0];
		}
		
		if(activeProfiles.length>0) {
			List<String> ap = Arrays.asList(activeProfiles);
			if(ap.contains("prod")){
//				topLbl.setValue("PROD");
				topLbl= new Label("<center><p style=\"background-color:rgb(255,0,0);font-size:50px;color:rgb(255,255,255)\">PROD</p><center>",ContentMode.HTML);
			}else if(ap.contains("dev")) {
				topLbl= new Label("<center><p style=\"background-color:rgb(0,255,0);font-size:50px;color:rgb(255,255,255)\">DEV</p><center>",ContentMode.HTML);
			}
		}
		
		initControlGrid();
		initScreen0Grid();
		
		
		setContent(main);
	}

	private void initScreen0Grid() {
		screen0Grid.setColumns("runDate", "ticker", "priceBid","targetPrice", "per","eps","marketCap","peg","companyName", "currency",  "bookValuePerShare", "perCalculated");
		screen0Grid.setWidth("100%");
		Set<String>floatColumns = Sets.newHashSet("priceBid","targetPrice", "per","eps","marketCap","peg", "currency",  "bookValuePerShare", "perCalculated");
		screen0Grid.setContainerDataSource(new BeanItemContainer<>(Screen0ResultAdapter.class,adapter.adapt(mainController.getResults())));
		screen0Grid.setCellStyleGenerator(cellReference ->
	                floatColumns.contains(cellReference.getPropertyId()) ?
	                			"v-align-right"
	                			: // otherwise, align text to left
	                			"v-align-left"
	            			);
	}

	private void initControlGrid() {
		controlGrid.setColumns( "timeStamp", "type", "timeStamp", "status", "comments");
		controlGrid.setWidth("100%");
		controlGrid.setContainerDataSource(new BeanItemContainer<>(Control.class,mainController.getStatuses()));
		controlGrid.addSelectionListener(e -> {
			if(e.getSelected().isEmpty()) {
				//
			}else {
				//
			}
		});
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
